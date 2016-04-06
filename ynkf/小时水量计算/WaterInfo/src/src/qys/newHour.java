package src.qys;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import src.util.ConnUtil;
import src.util.JdbcUtil;

public class newHour {
	static Connection conn=null;
	static PreparedStatement pst=null;
	static Statement pst1=null;
	static ResultSet rs=null;
	static SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd HH");
	static JdbcUtil ju=new JdbcUtil();
//	public static void main(String[] args){
//		Timer timer=new Timer();
//		final int hh=2;
//		timer.schedule(new TimerTask(){
//		String bgDate=ju.getBgDate();
//		
//			@Override
//			public void run() {
//				try {
//					runDate(bgDate);
//					Date dd=df1.parse(ju.getBgDate());
//					dd.setHours(dd.getHours()+4+hh);
//					System.out.println("下次执行时间："+df.format(dd));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}}, 0,hh*60*60*1000);
//	}
	public static String getLastTime(Connection conn,PreparedStatement pst,
			ResultSet rs){
		String endDt1="";
		String dateS="select sysdate-interval'1'hour dd from dual";
		try {
			pst=conn.prepareStatement(dateS);
			rs=pst.executeQuery();
			while(rs.next()){
				endDt1=rs.getString("dd");
			}
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return endDt1;
	}
	public static void runDate(String bgDate,String endDt1) throws ParseException{
		
		//ConnUtil cu=new ConnUtil();
		String shutdownDate="";
		String endDt="";
		String order="desc";
		System.out.println(ju.getBgDate()+"上次计算截止时间");
		List<String> sqlList=new ArrayList<String>();
		try {
			int inCount=0;
			int upCount=0;
			conn=ju.getConnBZ();
			//conn=cu.getConn();
			conn.setAutoCommit(false);
			//String bgDate="2016-03-04 09";
			//System.out.println(ju.getBgDate());
			
			List<String> zqList=getRelAll(conn, pst, rs);
			pst1=conn.createStatement();
			
			endDt=df1.format(df1.parse(endDt1));
			List<String> dateList=firstList(bgDate, endDt, conn, pst, rs,order);
			System.out.println(dateList.toString());
				for(int i=0;i<dateList.size();i++){
					List<List<String>> Sql=new ArrayList<List<String>>();
					String date2=dateList.get(i);
					
					java.util.Date date11=df1.parse(date2);
					date11.setHours(date11.getHours()+1);
					String date=df1.format(date11);
					shutdownDate=date2;
					String beginDate=date2+":00:00";
					String endDate=date+":00:00";
					System.out.println("正在计算 "+endDate+" 的小时流量!");
					List<String> mpCdList=getMpCd(beginDate, endDate, conn, pst, rs);
					List<Map<String,String>> listQ=checkDate(date, conn, pst, rs);
					List<String> resultList=getResult(beginDate, endDate, conn, pst, rs);
					for(int mm=0;mm<mpCdList.size();mm++){
						double hour_w=getResultDate(beginDate, endDate, conn, pst, rs, mpCdList.get(mm),zqList,resultList);
						sqlList=inSql(hour_w, endDate, mpCdList.get(mm), listQ);
						Sql.add(sqlList);
					}
					for(int uu=0;uu<Sql.size();uu++){
						if(Sql.get(uu).get(0).contains("insert into")){
							System.out.println(Sql.get(uu).get(0));
							//pst.executeUpdate();
							//pst.executeUpdate(Sql.get(uu).get(0));
							
							pst1.addBatch(Sql.get(uu).get(0));
							inCount++;
							//pst.close();
						}else{
							System.out.println(Sql.get(uu).get(0));
							//pst1=conn.prepareStatement(Sql.get(uu).get(0));
							//pst.executeUpdate(Sql.get(uu).get(0));
							pst1.addBatch(Sql.get(uu).get(0));
							upCount++;
							//pst.close();
						}
					}
					pst1.executeBatch();
					//pst1.close();
					conn.commit();
					System.out.println(endDate+" 成功插入 ："+inCount+" 条！");
					System.out.println(endDate+" 成功更新 ："+upCount+" 条！");
					inCount=0;
					upCount=0;
					//pst.close();
				}
		} catch (SQLException e) {
			try {
				conn.commit();
				ju.setBgDate(endDt);
				System.out.println("提交成功!上次执行截止时间："+shutdownDate);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(pst!=null)
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			System.out.println("即将计算流量小时水量。。。。。。。。。。");
		}
	}
	public static List<String> firstList(String bgDate,String endDt,Connection conn,PreparedStatement pst,
			ResultSet rs,String order){
		String sql="select to_char(t.tm,'yyyy-mm-dd hh24') tt from wr_mp_z_r t " +
				"group by to_char(t.tm,'yyyy-mm-dd hh24') having to_char(t.tm,'yyyy-mm-dd hh24')>='" +bgDate+
				"' and to_char(t.tm,'yyyy-mm-dd hh24')<='"+endDt+"' order by to_char(t.tm,'yyyy-mm-dd hh24') "+order;
		System.out.println(sql);
		List<String> dateList=new ArrayList<String>();
		try {
			pst=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=pst.executeQuery();
			boolean flag=rs.last();
			rs.beforeFirst();
			if(!flag){
				System.out.println(bgDate+" << 至  >>"+endDt+" 无瞬时水位信息！");
			}
			while(rs.next()){
				dateList.add(rs.getString("tt"));
			}
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dateList;
	}
	public static List<String> getRelAll(Connection conn,PreparedStatement pst,ResultSet rs){
		String sqlY="select t.mp_cd,t.z,t.q from user_yn_clet.rel_mp_z_q t ";
		List<String> list=new ArrayList<String>();
		try {
			pst=conn.prepareStatement(sqlY);
			rs=pst.executeQuery();
				while(rs.next()){
					String str=rs.getString("mp_cd")+"#"+rs.getFloat("z")+"#"+rs.getFloat("q");
					list.add(str);
			}
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static List<String> inSql(double hour_w,String endDate,String mp_cd,List<Map<String,String>> listQ){
		BigDecimal b=new BigDecimal(hour_w);
		double hourw=b.setScale(1, RoundingMode.HALF_UP).doubleValue();//四舍五入算法
		Map<String,String> checkMap=new HashMap<String,String>();
		checkMap.put("mp_cd", mp_cd);
		checkMap.put("dt", endDate);
		List<String> list=new ArrayList<String>();
		if(listQ.contains(checkMap)){
			String updateQ="update wr_mp_hourw_r set hour_w='"+hourw+"',ts=sysdate where mp_cd='"+mp_cd+"'" +
					" and to_char(dt,'yyyy-mm-dd hh24:mi:ss')='"+endDate+"'";
			list.add(updateQ);
		}else{
			String hourSql="insert into wr_mp_hourw_r (mp_cd,dt,hour_w) values('"+mp_cd+"',to_date('"+endDate+"'" +
					",'yyyy-mm-dd hh24:mi:ss')," +
					"'"+hourw+"')";
			list.add(hourSql);
		}
		return list;
	}
	public static List<String> getRel(String beginDate,String endDate,
			Connection conn,PreparedStatement pst,ResultSet rs,String mp_cd,String z){
		String sqlY="select t.mp_cd,t.z,t.q from user_yn_clet.rel_mp_z_q t where t.mp_cd='"+
				mp_cd+"' order by t.q asc";
		List<String> list=new ArrayList<String>();
		try {
			pst=conn.prepareStatement(sqlY);
			rs=pst.executeQuery();
				while(rs.next()){
					float mp_z=Float.parseFloat(z)-rs.getFloat("z");
					float q=rs.getFloat("q");
					String str=mp_cd+"#"+mp_z+"#"+q;
					list.add(str);
			}
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static List<String> getResult(String beginDate,String endDate,Connection conn,PreparedStatement pst,
			ResultSet rs){
		String sqlH="select * from  wr_mp_z_r a where to_char(a.tm,'yyyy-mm-dd hh24:mi:ss')>'"+beginDate+"' " +
				"and  to_char(a.tm,'yyyy-mm-dd hh24:mi:ss')<='"+endDate+"'";
		List<String> resultList=new ArrayList<String>();
		try {
			pst=conn.prepareStatement(sqlH);
			rs=pst.executeQuery();
			while(rs.next()){
//				map.put("mp_cd", rs.getString("mp_cd"));
//				map.put("tm", rs.getString("tm"));
//				map.put("mp_z", Float.parseFloat(rs.getString("mp_z"))+"");
				String str=rs.getString("mp_cd")+"#"+rs.getString("tm")+"#"+Float.parseFloat(rs.getString("mp_z"));
				resultList.add(str);
			}
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	public static double getResultDate(String beginDate,String endDate,Connection conn,PreparedStatement pst,
			ResultSet rs,String mp_cd,List<String> mapList,List<String> resultList1) throws ParseException{
		double hour_w=0;
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();
		for(int ii=0;ii<resultList1.size();ii++){
			if(resultList1.get(ii).contains(mp_cd)){
				Map<String,String> map=new HashMap<String,String>();
				map.put("mp_cd", mp_cd);
				map.put("tm", resultList1.get(ii).split("#")[1]);
				map.put("mp_z", resultList1.get(ii).split("#")[2]);
				resultList.add(map);
			}
		}
		List<String> list=new ArrayList<String>();
		for(int mm=0;mm<resultList.size();mm++){
			for(int pp=0;pp<mapList.size();pp++){
				if(mapList.get(pp).contains((CharSequence) resultList.get(mm).get("mp_cd"))){
					String zq=resultList.get(mm).get("mp_cd")+"#"+
							(Float.parseFloat(resultList.get(mm).get("mp_z"))-Float.parseFloat(mapList.get(pp).split("#")[1]))+"#"
								+mapList.get(pp).split("#")[2];
					list.add(zq);
				}
			}
			//List<String> list=getRel(beginDate, endDate, conn, pst, rs, mp_cd,resultList.get(mm).get("mp_z"));
			if(mm==0){
				String date1=resultList.get(mm).get("tm");
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date d=df.parse(date1);
				int dd=d.getMinutes()*60+d.getSeconds();
				String q=zqFormat(list,mp_cd)+"";
				hour_w=hour_w+dd*Double.parseDouble(q);
			}else{
				String q=zqFormat(list,resultList.get(mm).get("mp_cd"))+"";
				long l=formatDate(resultList.get(mm-1).get("tm"),resultList.get(mm).get("tm"));
				hour_w=hour_w+Double.parseDouble(q)*l;
			}
		}
		return hour_w;
		
	}
	public static List<Map<String,String>> checkDate(String date,Connection conn,PreparedStatement pst,ResultSet rs) throws ParseException{
		String sqlQ="select mp_cd,dt from wr_mp_hourw_r t where to_char(t.dt,'yyyy-mm-dd hh24')='"+date+"'";
		List<Map<String,String>> checkList=new ArrayList<Map<String,String>>();
		try {
			pst=conn.prepareStatement(sqlQ);
			rs=pst.executeQuery();
			while(rs.next()){
				Map<String,String> map=new HashMap<String,String>();
				map.put("mp_cd", rs.getString("mp_cd"));
				map.put("dt", df.format(df.parse(rs.getString("dt"))));
				checkList.add(map);
			}
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return checkList;
	}
	public static List<String> getMpCd(String beginDate,String endDate,Connection conn,PreparedStatement pst,ResultSet rs){
		String sql="select distinct mp_cd from  wr_mp_z_r a where to_char(a.tm,'yyyy-mm-dd hh24:mi:ss')>'"+beginDate+"' " +
				"and  to_char(a.tm,'yyyy-mm-dd hh24:mi:ss')<='"+endDate+"' and a.mp_cd in(select distinct mp_cd from " +
				"rel_mp_z_q)";
		List<String> mpCdList=new ArrayList<String>();
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
	public static long formatDate(String beginDate,String endDate){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long date=0;
		try {
			java.util.Date begin=df.parse(beginDate);
			java.util.Date end=df.parse(endDate);
			date=(end.getTime()-begin.getTime())/1000;
		} catch (ParseException e) {
			System.out.println("时间类型格式转换失败！");
			e.printStackTrace();
		}
		return date;
	}
	public static double zqFormat(List<String> list,String mp_cd){
		String qq="";
			Map<Double,String> list1=new HashMap<Double,String>();
			for(int i=0;i<list.size();i++){
				String[] strs=list.get(i).split("#");
				if(strs[0].equals(mp_cd)){
					if(Float.parseFloat(strs[1])==0.0){
						qq=strs[2];
						list1.clear();
						break;
					}else if(Float.parseFloat(strs[1])<0.0){
						list1.put(Double.parseDouble(strs[1].split("-")[1]), strs[2]);
					}else{
						list1.put(Double.parseDouble(strs[1]), strs[2]);
					}
				}
			}
			if(list1.size()!=0){
				List<Entry<Double, Integer>> infoIds =
					    new ArrayList<Map.Entry<Double, Integer>>((Collection<? extends Entry<Double, Integer>>) list1.entrySet());
				Collections.sort(infoIds, new Comparator<Map.Entry<Double, Integer>>() {   
				    public int compare(Map.Entry<Double, Integer> o1, Map.Entry<Double, Integer> o2) {      
				        //return (o2.getValue() - o1.getValue()); 
				        return (o1.getKey()).toString().compareTo(o2.getKey()+"");
				    }
				}); 
				    String id = infoIds.get(0).toString();
				    qq=id.split("=")[1];
			}
		return Double.parseDouble(qq);
	}
	public static List<Map<String,String>> getConn(){
		Connection conn1=null;
		PreparedStatement pst2=null;
		ResultSet rs2=null;
		JdbcUtil jj=new JdbcUtil();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		try {
			conn1=jj.getConnBZ();
			String sql="select mp_cd,dt from wr_mp_hourw_r";
			pst2=conn1.prepareStatement(sql);
			rs2=pst2.executeQuery();
			while(rs2.next()){
				Map<String,String> map=new HashMap<String,String>();
				map.put("mp_cd", rs2.getString("mp_cd"));
				map.put("dt", rs2.getString("dt"));
				list.add(map);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs2!=null)
				try {
					rs2.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(pst2!=null)
				try {
					pst2.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(conn1!=null)
				try {
					conn1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return list;
	}
}
