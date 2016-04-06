package src.hourTOday;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class mpComputer {

	public static void main(String[] args) {
		Date date = new Date();
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");// ���ڸ�ʽ����ȷ����
		// String dateStr = df1.format(date);148 156
		String dateStr = "2016-03-06";
		mpComputer mc = new mpComputer();
		mc.findAllMpCdDistinct(dateStr);
	}

	// ��ȡ����ȡ��ˮվ��������Ϣ��
	public void findAllMpCdDistinct(String date) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
//			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.53.8.27:1521:szydbc", "user_yn_clet",
//					"dhcclet");
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.53.8.27:1521:szydbc", "sl_szy_yn_extra",
					"dhcextra");
			String sql = "select * from wr_mp_hourw_r where to_char(dt,'yyyy-MM-dd')='" + date + "'";
			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();// ��ȡ�������е�Сʱˮ����Ϣ
			ResultSetMetaData md = rs.getMetaData(); // �õ������(rs)�Ľṹ��Ϣ�������ֶ������ֶ�����
			int columnCount = md.getColumnCount(); // ���ش� ResultSet �����е�����
			List list = new ArrayList();
			Map rowData = new HashMap();
			while (rs.next()) {
				rowData = new HashMap(columnCount);//����һ�����г�ʼ������hashMap
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);

			}
			// System.out.println("list:" + list.toString());

			Map<String, BigDecimal> mpMap = computerSetValue(list);// ������ˮ�����=========���ؽ������MP_CD,hour_w3������
			System.out.println(mpMap.size() + "������������");
			String daySql = "select distinct(mp_cd) from wr_day_w_r where to_char(dt,'yyyy-MM-dd')='" + date + "'";// ����ˮ�����в��ҽ�������е���ˮ�����еĸ��£��޵Ĳ��룬ֻ���ز�ͬ��MP_CD

			ResultSet rs2 = ps.executeQuery(daySql);
			ResultSetMetaData md2 = rs2.getMetaData(); // �õ������(rs)�Ľṹ��Ϣ�������ֶ������ֶ�����
			int columnCount2 = md2.getColumnCount(); // ���ش� ResultSet �����е�����

			List<String> daywList = new ArrayList<String>();
			List<MPDayW> updateList = new ArrayList<MPDayW>();// ���µĽ����
			List<MPDayW> insertList = new ArrayList<MPDayW>();// ����Ľ����
			if (rs2 != null) {
				while (rs2.next()) {
					String daystcd = (String) rs2.getObject(1);//����վ�����
					daywList.add(daystcd);//����վ����뱣����dayList�У�һ��һ��վ��һ�����룩
				}
			}
			java.sql.Date dt = java.sql.Date.valueOf(date);//������String����ת��Ϊint
			for (String key : mpMap.keySet()) {//ȡ��Key

				MPDayW mpdayw2 = new MPDayW();
				mpdayw2.setMpcd(key);
				mpdayw2.setDay_w(mpMap.get(key));//ȡ����ˮ����Ϣ�����ӵ�mpdayw2�����У�maMap������ֻ����һ��һ��վ�����Ϣ��
				mpdayw2.setDt(dt);//�趨����date���ӵ�������
				mpdayw2.setDt_src("1");
				insertList.add(mpdayw2);//���н�������ӵ�insertList�����С������Ӳ��������Ƿ�ʹ��addAll
				for (String mpcd : daywList) {
					if (key.equals(mpcd)) {// ���²���
						updateList.add(mpdayw2);
						insertList.remove(mpdayw2);//ɾ��������Ϣ
					}
				}
			}

			System.out.println(daywList.size() + ":��ˮ����������");
			System.out.println(updateList.size() + ":���µ�����");
			System.out.println(insertList.size() + ":���������");
			if (updateList != null && !updateList.isEmpty()) {
//				String updateSql = "update wr_day_w_r set day_w=?,spe_reg_data='0',dt_src='1',ts=sysdate where mp_cd=? and dt=?";
				String updateSql = "update wr_day_w_r set day_w=?,spe_reg_data='0',ts=sysdate where mp_cd=? and dt=?";

				updateTables(updateList, updateSql, conn);// ����
			}
			if (insertList != null && !insertList.isEmpty()) {//isEmpty()�ж�insertList�����Ƿ�Ϊ��
//				String insertSql = "insert into wr_day_w_r(mp_cd,dt,day_w,spe_reg_data,ts,dt_src)  values (?,?,?,'0',sysdate,'1')";
				String insertSql = "insert into wr_day_w_r(mp_cd,dt,day_w,spe_reg_data,ts)  values (?,?,?,'0',sysdate)";

				insertTables(insertList, insertSql, conn);// ����
			}

			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ������ˮ�����ݱ�
	 * @param updateList
	 * @param updateSql
	 * @param conn
	 */
	private void updateTables(List<MPDayW> updateList, String updateSql, Connection conn) {
		PreparedStatement pstm = null;
		try {
			pstm = conn.prepareStatement(updateSql);
			conn.setAutoCommit(false);// 1,���Ȱ�Auto commit����Ϊfalse,�������Զ��ύ
			for (MPDayW md : updateList) {
				pstm.setBigDecimal(1, md.getDay_w());//??
				pstm.setString(2, md.getMpcd());
				pstm.setDate(3, md.getDt());
                
				pstm.addBatch();// ��һ�������ӵ��� PreparedStatement ����������������С�
			}
			// System.out.println(pstm.toString());
			// ��һ�������ύ�����ݿ���ִ�У����ȫ������ִ�гɹ����򷵻ظ��¼�����ɵ����顣
			// pstm.executeBatch();
			System.out.println("���³ɹ���������" + pstm.executeBatch().length);
			// ���ɹ�ִ�������еĲ������������������
			conn.commit();// �����ֶ��ύ��commit��
			System.out.println("�ύ�ɹ�!");
			conn.setAutoCommit(true);// �ύ��ɺ�ظ��ֳ���Auto commit,��ԭΪtrue,

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstm != null) {
				try {
					pstm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * ������ˮ�����ݱ�
	 * @param insertList
	 * @param insertSql
	 * @param conn
	 */
	private void insertTables(List<MPDayW> insertList, String insertSql, Connection conn) {
		PreparedStatement pstm = null;
		try {
			pstm = conn.prepareStatement(insertSql);
			conn.setAutoCommit(false);// 1,���Ȱ�Auto commit����Ϊfalse,�������Զ��ύ
			for (MPDayW md : insertList) {
				
				pstm.setBigDecimal(3, md.getDay_w());
				pstm.setString(1, md.getMpcd());
				pstm.setDate(2, md.getDt());

				pstm.addBatch();// ��һ�������ӵ��� PreparedStatement ����������������С�
			}
			// System.out.println(pstm.toString());
			// ��һ�������ύ�����ݿ���ִ�У����ȫ������ִ�гɹ����򷵻ظ��¼�����ɵ����顣
			// pstm.executeBatch();
			System.out.println("����ɹ���������" + pstm.executeBatch().length);
			// ���ɹ�ִ�������еĲ������������������
			conn.commit();// �����ֶ��ύ��commit��
			System.out.println("�ύ�ɹ�!");
			conn.setAutoCommit(true);// �ύ��ɺ�ظ��ֳ���Auto commit,��ԭΪtrue,

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstm != null) {
				try {
					pstm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Map getDayWUpdateValue() {// ���

		return null;
	}

	//
	public Map<String, BigDecimal> computerSetValue(List list) {
		System.out.println(list.size());
		Iterator it = list.iterator();//�������ʹ��Iterator�������
		List<String> tmpList = new ArrayList<String>();
		List allList = new ArrayList();
		allList.addAll(list); //�����list����allList��

		Map<String, BigDecimal> mpData = new HashMap<String, BigDecimal>();
		while (it.hasNext()) {// ���ݲ�ѯ���ȥ�ز���������������ʹ��hasNext()ѭ���ж������ÿ�����󶼽����ж�
			Map hm = (Map) it.next();//next()���
			String stcd = (String) hm.get("MP_CD");//ȡ�ü�����MP_CD��ֵ
			// BigDecimal hour_w=(BigDecimal) hm.get("HOUR_W") ;

			if (tmpList.contains(stcd)) {
				it.remove();
			} else {
				tmpList.add(stcd);
			}
		}

		for (Object object : tmpList) {// ����ȥ��֮������ݣ���Сʱˮ�����мӷ�������foreachѭ���������������Object������object
			String str = (String) object;
			BigDecimal hour_w3 = BigDecimal.ZERO;//����hour_w3��ʼ����
			for (Object obj : allList) {
				Map hm2 = (Map) obj;
				String stcd2 = (String) hm2.get("MP_CD");
				BigDecimal hour_w2 = (BigDecimal) hm2.get("HOUR_W");//ȡ�ö�����HOUR_W��ֵ
				if (str.equals(stcd2)) {// �����key �Ͷ�value ���ӷ�
					hour_w3 = hour_w3.add(hour_w2);
				}
			}

			mpData.put(str, hour_w3);//��MP_CD,��ˮ������mpData������
		}

		// System.out.println(mpData.toString());
		return mpData;
	}

}
