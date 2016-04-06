package src.qys;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.util.JdbcUtil;

public class abc {
	static Connection conn=null;
	static PreparedStatement pst=null;
	static ResultSet rs=null;
	static SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH");
	static SimpleDateFormat df2=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static List<String> errList=new ArrayList<String>();
	public static void runDate(String beginDate,String endDate) throws ParseException, SQLException{
		JdbcUtil ju=new JdbcUtil();
		try {
			String upSQL="update wr_mp_hourw_r set hour_w=? , ts=sysdate where mp_cd=? and dt=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			String inSQL="insert into wr_mp_hourw_r(mp_cd,dt,hour_w) values(?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)";
			conn=ju.getConnBZ();
			conn.setAutoCommit(false);
			//pst=conn.prepareStatement("select sysdate-interval'1' hour tt from dual");
			//rs=pst.executeQuery();
			String order="desc";
			//String beginDate="2016-03-21 12";
			//String endDate="";
//			while(rs.next()){
//				endDate=rs.getString("tt");
//			}
			//endDate="2016-03-18 11";
			
			String endDt=df.format(df.parse(endDate));
			List<String> dateList=getDate(beginDate, endDt, conn, pst, rs, order);
			for(int i=0;i<dateList.size();i++){
				int inCount=0;
				int upCount=0;
				List<String> valueList=new ArrayList<String>();
				String date=dateList.get(i);
				String bgDate=date+":00:00";
				Date d=df.parse(date);
				d.setHours(d.getHours()+1);
				String endT=df.format(d)+":00:00";
				System.out.println("���ڼ��� "+bgDate+">>>>"+endT+" ��Сʱˮ��....");
				List<String> mpCdList=getMpCd(conn, pst, rs, bgDate, endT);
				List<String> resultList=getResultDate(conn, pst, bgDate, endT, rs);
				List<Map<String,String>> ljList=getLJ(conn, pst, rs, bgDate, endT);
				for(int mm=0;mm<mpCdList.size();mm++){
					List<String> resultList1=new ArrayList<String>();
					for(int ww=0;ww<resultList.size();ww++){
						if(resultList.get(ww).contains(mpCdList.get(mm))){
							resultList1.add(resultList.get(ww));
						}
					}
					List<String> resultDateList=chuli(resultList1, bgDate, endT,ljList);
					if(resultDateList.size()==0){
						continue;
					}else{
						valueList.add(resultDateList.get(0));
					}
				}
				List<Map<String,String>> checkList=checkDate(conn, pst, rs, bgDate, endT);
				List<Map<String,String>> dateL=dateCH(valueList);
				for(int ii=0;ii<dateL.size();ii++){
					Map<String,String> checkMap=new HashMap<String,String>();
					checkMap.put("mp_cd", dateL.get(ii).get("mp_cd"));
					checkMap.put("dt", dateL.get(ii).get("dt"));
					if(checkList.contains(checkMap)){
						pst=conn.prepareStatement(upSQL);
						pst.setString(1, dateL.get(ii).get("hour_w"));
						pst.setString(2, dateL.get(ii).get("mp_cd"));
						pst.setString(3, dateL.get(ii).get("dt"));
						//System.out.println(upSQL);
						pst.addBatch();
						upCount++;
					}else{
						pst=conn.prepareStatement(inSQL);
						pst.setString(3, dateL.get(ii).get("hour_w"));
						pst.setString(1, dateL.get(ii).get("mp_cd"));
						pst.setString(2, dateL.get(ii).get("dt"));
						pst.addBatch();
						//System.out.println(inSQL);
						inCount++;
					}
					pst.executeBatch();
					pst.close();
				}
				conn.commit();
				System.out.println(bgDate+">>>"+endT+" δ������㣺"+errList.toString());
				System.out.println(bgDate+">>>"+endT+" �ɹ����£�"+upCount);
				System.out.println(bgDate+">>>"+endT+" �ɹ����룺"+inCount);
				pst.close();
				errList.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null)rs.close();
			if(pst!=null)pst.close();
			if(conn!=null)conn.close();
			System.out.println("��������Сʱ����ˮ����������������������������");
		}
	}
	public static List<Map<String,String>> dateCH(List<String> list){
		List<Map<String,String>> chList=new ArrayList<Map<String,String>>();
		for(int i=0;i<list.size();i++){
			String[] strs=list.get(i).split("#");
			Map<String,String> map=new HashMap<String,String>();
			map.put("mp_cd", strs[0]);
			map.put("hour_w", strs[1]);
			map.put("dt", strs[2]);
			chList.add(map);
		}
		return chList;
	}
	public static List<Map<String,String>> checkDate(Connection conn,PreparedStatement pst,ResultSet rs,
			String beginDate,String endDate) throws ParseException{
		String sql="select mp_cd,dt from wr_mp_hourw_r where to_char(dt,'yyyy-mm-dd hh24:mi:ss')>'"+beginDate+"' " +
				"and to_char(dt,'yyyy-mm-dd hh24:mi:ss')<='"+endDate+"'";
		List<Map<String,String>> mapList=new ArrayList<Map<String,String>>();
		try {
			pst=conn.prepareStatement(sql);
			rs=pst.executeQuery();
			while(rs.next()){
				Map<String,String> map=new HashMap<String,String>();
				map.put("mp_cd", rs.getString("mp_cd"));
				map.put("dt", df1.format(df1.parse(rs.getString("dt"))));
				mapList.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return mapList;
	}
	public static List<Map<String,String>> getLJ(Connection conn,PreparedStatement pst,ResultSet rs,
			String beginDate,String endDate) throws ParseException{
		Date date=df2.parse(endDate);
		date.setMinutes(date.getMinutes()+10);
		String date1=df2.format(date);
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		String sql="select * from wr_mp_q_r t where to_char(t.tm,'yyyy-mm-dd hh24:mi:ss')>='"+beginDate+"' and "+
				"to_char(t.tm,'yyyy-mm-dd hh24:mi')<='"+date1+"' order by t.tm asc";
		//String sql="select * from wr_mp_q_r t where to_char(t.tm,'yyyy-mm-dd hh24:mi:ss')>='"+beginDate+"' and "+
//				"to_char(t.tm,'yyyy-mm-dd hh24:mi')<='"+date1+"' and t.mp_cd='"+mp_cd+"' order by t.tm asc";
		try {
			pst=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=pst.executeQuery();
			boolean flag=rs.last();
			rs.beforeFirst();
			if(!flag){
				return null;
			}else{
				while(rs.next()){
					if(rs.getString("acc_w")==null||rs.getString("acc_w").equals("")||rs.getString("acc_w").equals("null")){
						continue;
					}else{
						Map<String,String> map=new HashMap<String,String>();
						map.put("mp_cd", rs.getString("mp_cd"));
						map.put("acc_w", rs.getString("acc_w"));
						list.add(map);
					}
				}
				pst.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static List<String> chuli(List<String> list,String beginDate,String endDate,List<Map<String,String>> ljList) throws ParseException{
		List<String> hourList=new ArrayList<String>();
		int count=0;
		double mpq=0;
		double qq=0;
		String mp_cd="";
		boolean flag=false;
		boolean flag1=false;
		for(int i=0;i<list.size();i++){
			String[] strs=list.get(i).split("#");
			mp_cd=strs[0];
			String mp_q=strs[2];
			if(mp_q.equals("null")){
				count=count+0;
			}else{
				count=1;
				if(mp_q.contains("-")){
					if(flag){
						mpq=mpq+0;
					}else{
						mpq=mpq+Double.parseDouble(strs[2]);
						if(new BigDecimal(mpq).setScale(1, RoundingMode.HALF_UP).abs().signum()>9){
							mpq=-99999999.9;
						}
						flag1=true;
					}
				}else{
					flag=true;
					if(flag1){
						mpq=Double.parseDouble(strs[2]);
						flag1=false;
					}else{
						mpq=mpq+Double.parseDouble(strs[2]);
						if(new BigDecimal(mpq).setScale(1, RoundingMode.HALF_UP).signum()>10){
							mpq=999999999.9;
						}
					}					
				}
			}
		}
		BigDecimal q=new BigDecimal(mpq);
		BigDecimal qq1=q.setScale(1, RoundingMode.HALF_UP);
		if(qq1.compareTo(new BigDecimal(0))==-1){
			if(qq1.abs().compareTo(new BigDecimal(99999999.9))==1){
				qq1=new BigDecimal(-99999999.9);
			}
		}else{
			if(qq1.compareTo(new BigDecimal(999999999.9))==1){
				qq1=new BigDecimal(999999999.9);
			}
		}
		if(count==0){
			//List<BigDecimal> ljList=getLJ(conn, pst, rs, beginDate, endDate, mp_cd);
			List<BigDecimal> ljList1=new ArrayList<BigDecimal>();
			//System.out.println(mp_cd+"+++++++++++"+ljList.toString());
			for(int pp=0;pp<ljList.size();pp++){
				if(ljList.get(pp).get("mp_cd").equals(mp_cd)){
					ljList1.add(new BigDecimal(ljList.get(pp).get("acc_w")));
				}
			}
			if(ljList1.size()<2){
				errList.add(mp_cd);
				//System.out.println(mp_cd+"���㣺"+beginDate+">>>>"+endDate+"�쳣");
			}else{
				BigDecimal mpqb=ljList1.get(ljList1.size()-1);
				mpqb=mpqb.subtract(ljList1.get(0).abs());
				BigDecimal ljq=mpqb.setScale(1, RoundingMode.HALF_UP);
				if(ljq.compareTo(new BigDecimal(0))==-1){
					if(ljq.abs().compareTo(new BigDecimal(99999999.9))==1){
						ljq=new BigDecimal(-99999999.9);
					}
				}else{
					if(ljq.abs().compareTo(new BigDecimal(999999999.9))==1){
						ljq=new BigDecimal(999999999.9);
					}
				}
				//System.out.println(ljq+"&&&&&&");
				hourList.add(mp_cd+"#"+ljq+"#"+endDate);
			}
			//System.out.println(beginDate+">>>"+endDate+"Сʱ�ڡ���"+mp_cd+"��˲ʱ�����������ۻ���������");
		}else{
			hourList.add(mp_cd+"#"+qq1+"#"+endDate);
			//System.out.println(beginDate+">>>"+endDate+"Сʱ�ڡ���"+mp_cd+"  Сʱˮ��>>>>"+qq1);
		}
		return hourList;
	}
	public static List<String> getMpCd(Connection conn,PreparedStatement pst,ResultSet rs,
			String beginDate,String endDate){
		List<String> mpCdList=new ArrayList<String>();
		String sql="select distinct mp_cd from  wr_mp_q_r a where to_char(a.tm,'yyyy-mm-dd hh24:mi:ss')>'"+beginDate+"' " +
		"and  to_char(a.tm,'yyyy-mm-dd hh24:mi:ss')<='"+endDate+"' and a.mp_cd in(select distinct mp_cd_ne from " +
		"wr_mp_code)";
		try {
			pst=conn.prepareStatement(sql);
			rs=pst.executeQuery();
			while(rs.next()){
				mpCdList.add(rs.getString("mp_cd"));
			}
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return mpCdList;
	}
	public static List<String> getResultDate(Connection conn,PreparedStatement pst,String beginDate,String endDate,
			ResultSet rs) throws ParseException{
		String sql="select mp_cd,tm,mp_q,acc_w from wr_mp_q_r where to_char(tm,'yyyy-mm-dd hh24:mi:ss')>'"+beginDate+"' and " +
				"to_char(tm,'yyyy-mm-dd hh24:mi:ss')<='"+endDate+"' order by tm desc";
		List<String> list=new ArrayList<String>();
		try {
			pst=conn.prepareStatement(sql);
			rs=pst.executeQuery();
			while(rs.next()){
				String str=rs.getString("mp_cd")+"#"+df1.format(df1.parse(rs.getString("tm")))+"#"+rs.getString("mp_q")+"#"+rs.getString("acc_w");
				list.add(str);
			}
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static List<String> getDate(String beginDate,String endDate,Connection conn,PreparedStatement pst,
			ResultSet rs,String order){
		String sql="select to_char(t.tm,'yyyy-mm-dd hh24') tt from wr_mp_q_r t group by to_char(t.tm,'yyyy-mm-dd hh24') having " +
				"to_char(t.tm,'yyyy-mm-dd hh24')>='"+beginDate+"' and to_char(t.tm,'yyyy-mm-dd hh24')<='"+endDate+"' " +
				"order by to_char(t.tm,'yyyy-mm-dd hh24') "+order;
		List<String> dateList=new ArrayList<String>();
		try {
			pst=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=pst.executeQuery();
			boolean flag=rs.last();
			rs.beforeFirst();
			if(!flag){
				System.out.println(beginDate+">>>>>>"+endDate+"��˲ʱ������Ϣ��");
			}else{
				while(rs.next()){
					dateList.add(rs.getString("tt"));
				}
				pst.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dateList;
	}
}
