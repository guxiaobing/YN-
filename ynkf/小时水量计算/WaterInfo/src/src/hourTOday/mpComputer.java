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
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");// 日期格式，精确到日
		// String dateStr = df1.format(date);148 156
		String dateStr = "2016-03-06";
		mpComputer mc = new mpComputer();
		mc.findAllMpCdDistinct(dateStr);
	}

	// 获取所有取用水站点代码等信息。
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
			ResultSet rs = ps.executeQuery();// 获取今天所有的小时水量信息
			ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
			int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
			List list = new ArrayList();
			Map rowData = new HashMap();
			while (rs.next()) {
				rowData = new HashMap(columnCount);//构造一个带有初始容量的hashMap
				for (int i = 1; i <= columnCount; i++) {
					rowData.put(md.getColumnName(i), rs.getObject(i));
				}
				list.add(rowData);

			}
			// System.out.println("list:" + list.toString());

			Map<String, BigDecimal> mpMap = computerSetValue(list);// 计算日水量结果=========返回结果集（MP_CD,hour_w3……）
			System.out.println(mpMap.size() + "：计算结果总数");
			String daySql = "select distinct(mp_cd) from wr_day_w_r where to_char(dt,'yyyy-MM-dd')='" + date + "'";// 在日水量表中查找今天的所有的日水量，有的更新，无的插入，只返回不同的MP_CD

			ResultSet rs2 = ps.executeQuery(daySql);
			ResultSetMetaData md2 = rs2.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
			int columnCount2 = md2.getColumnCount(); // 返回此 ResultSet 对象中的列数

			List<String> daywList = new ArrayList<String>();
			List<MPDayW> updateList = new ArrayList<MPDayW>();// 更新的结果集
			List<MPDayW> insertList = new ArrayList<MPDayW>();// 插入的结果集
			if (rs2 != null) {
				while (rs2.next()) {
					String daystcd = (String) rs2.getObject(1);//单个站点编码
					daywList.add(daystcd);//增加站点编码保存在dayList中（一天一个站点一个编码）
				}
			}
			java.sql.Date dt = java.sql.Date.valueOf(date);//将日期String类型转换为int
			for (String key : mpMap.keySet()) {//取得Key

				MPDayW mpdayw2 = new MPDayW();
				mpdayw2.setMpcd(key);
				mpdayw2.setDay_w(mpMap.get(key));//取得日水量信息并增加到mpdayw2集合中（maMap集合中只保存一天一个站点的信息）
				mpdayw2.setDt(dt);//设定日期date增加到集合中
				mpdayw2.setDt_src("1");
				insertList.add(mpdayw2);//所有结果集增加到insertList集合中——增加操作？？是否使用addAll
				for (String mpcd : daywList) {
					if (key.equals(mpcd)) {// 更新操作
						updateList.add(mpdayw2);
						insertList.remove(mpdayw2);//删除已有信息
					}
				}
			}

			System.out.println(daywList.size() + ":日水量现有总数");
			System.out.println(updateList.size() + ":更新的数据");
			System.out.println(insertList.size() + ":插入的数据");
			if (updateList != null && !updateList.isEmpty()) {
//				String updateSql = "update wr_day_w_r set day_w=?,spe_reg_data='0',dt_src='1',ts=sysdate where mp_cd=? and dt=?";
				String updateSql = "update wr_day_w_r set day_w=?,spe_reg_data='0',ts=sysdate where mp_cd=? and dt=?";

				updateTables(updateList, updateSql, conn);// 更新
			}
			if (insertList != null && !insertList.isEmpty()) {//isEmpty()判断insertList集合是否为空
//				String insertSql = "insert into wr_day_w_r(mp_cd,dt,day_w,spe_reg_data,ts,dt_src)  values (?,?,?,'0',sysdate,'1')";
				String insertSql = "insert into wr_day_w_r(mp_cd,dt,day_w,spe_reg_data,ts)  values (?,?,?,'0',sysdate)";

				insertTables(insertList, insertSql, conn);// 插入
			}

			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新日水量数据表
	 * @param updateList
	 * @param updateSql
	 * @param conn
	 */
	private void updateTables(List<MPDayW> updateList, String updateSql, Connection conn) {
		PreparedStatement pstm = null;
		try {
			pstm = conn.prepareStatement(updateSql);
			conn.setAutoCommit(false);// 1,首先把Auto commit设置为false,不让它自动提交
			for (MPDayW md : updateList) {
				pstm.setBigDecimal(1, md.getDay_w());//??
				pstm.setString(2, md.getMpcd());
				pstm.setDate(3, md.getDt());
                
				pstm.addBatch();// 将一组参数添加到此 PreparedStatement 对象的批处理命令中。
			}
			// System.out.println(pstm.toString());
			// 将一批参数提交给数据库来执行，如果全部命令执行成功，则返回更新计数组成的数组。
			// pstm.executeBatch();
			System.out.println("更新成功！数量：" + pstm.executeBatch().length);
			// 若成功执行完所有的插入操作，则正常结束
			conn.commit();// 进行手动提交（commit）
			System.out.println("提交成功!");
			conn.setAutoCommit(true);// 提交完成后回复现场将Auto commit,还原为true,

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
	 * 插入日水量数据表
	 * @param insertList
	 * @param insertSql
	 * @param conn
	 */
	private void insertTables(List<MPDayW> insertList, String insertSql, Connection conn) {
		PreparedStatement pstm = null;
		try {
			pstm = conn.prepareStatement(insertSql);
			conn.setAutoCommit(false);// 1,首先把Auto commit设置为false,不让它自动提交
			for (MPDayW md : insertList) {
				
				pstm.setBigDecimal(3, md.getDay_w());
				pstm.setString(1, md.getMpcd());
				pstm.setDate(2, md.getDt());

				pstm.addBatch();// 将一组参数添加到此 PreparedStatement 对象的批处理命令中。
			}
			// System.out.println(pstm.toString());
			// 将一批参数提交给数据库来执行，如果全部命令执行成功，则返回更新计数组成的数组。
			// pstm.executeBatch();
			System.out.println("插入成功！数量：" + pstm.executeBatch().length);
			// 若成功执行完所有的插入操作，则正常结束
			conn.commit();// 进行手动提交（commit）
			System.out.println("提交成功!");
			conn.setAutoCommit(true);// 提交完成后回复现场将Auto commit,还原为true,

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

	public Map getDayWUpdateValue() {// 获得

		return null;
	}

	//
	public Map<String, BigDecimal> computerSetValue(List list) {
		System.out.println(list.size());
		Iterator it = list.iterator();//集合输出使用Iterator迭代输出
		List<String> tmpList = new ArrayList<String>();
		List allList = new ArrayList();
		allList.addAll(list); //结果集list放入allList中

		Map<String, BigDecimal> mpData = new HashMap<String, BigDecimal>();
		while (it.hasNext()) {// 数据查询结果去重操作，迭代输出结果使用hasNext()循环判断输出，每个对象都进行判断
			Map hm = (Map) it.next();//next()输出
			String stcd = (String) hm.get("MP_CD");//取得集合中MP_CD的值
			// BigDecimal hour_w=(BigDecimal) hm.get("HOUR_W") ;

			if (tmpList.contains(stcd)) {
				it.remove();
			} else {
				tmpList.add(stcd);
			}
		}

		for (Object object : tmpList) {// 根据去重之后的数据，对小时水量进行加法操作，foreach循环输出操作，类型Object，对象object
			String str = (String) object;
			BigDecimal hour_w3 = BigDecimal.ZERO;//定义hour_w3初始量零
			for (Object obj : allList) {
				Map hm2 = (Map) obj;
				String stcd2 = (String) hm2.get("MP_CD");
				BigDecimal hour_w2 = (BigDecimal) hm2.get("HOUR_W");//取得对象中HOUR_W的值
				if (str.equals(stcd2)) {// 如果有key 就对value 做加法
					hour_w3 = hour_w3.add(hour_w2);
				}
			}

			mpData.put(str, hour_w3);//将MP_CD,日水量存入mpData集合中
		}

		// System.out.println(mpData.toString());
		return mpData;
	}

}
