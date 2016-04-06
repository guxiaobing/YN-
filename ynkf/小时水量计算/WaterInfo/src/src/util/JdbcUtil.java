package src.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class JdbcUtil {
	private static Properties pro1;
	private static Properties proDate;
	static{
		
		//InputStream is=new InputStream("d:/temp/config/jssl.properties");
		//InputStream isDate=JdbcUtil.class.getClassLoader().getResourceAsStream("d:/temp/config/RunnTime.properties");
		pro1=new Properties();
		proDate=new Properties();
		try {
			BufferedReader is=new BufferedReader(new FileReader("d:/temp/config/jssl.properties"));
			BufferedReader isDate=new BufferedReader(new FileReader("d:/temp/config/RunnTime.properties"));
			pro1.load(is);
			proDate.load(isDate);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("加载数据库配置文件失败！");
		}
		try {
			Class.forName(pro1.getProperty("bz.driverClassName"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("加载数据库驱动失败！");
		}
	}
	public Connection getConnBZ() throws SQLException{
		System.out.println("正在加载标准库数据库......"+pro1.getProperty("bz.url")+"用户名:"+pro1.getProperty("bz.username")+"密码:"+pro1.getProperty("bz.passwd"));
		return DriverManager.getConnection(pro1.getProperty("bz.url"), pro1.getProperty("bz.username"),
				pro1.getProperty("bz.passwd"));
	}
//	public static void main(String[] args) throws IOException, ParseException{
//		String path="d:/temp/config/RunnTime.properties";
//		Writer w=new FileWriter(path);
//		Date date1=new Date();
//		date1.setHours(date1.getHours()+24);
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		String dd=df.format(date1);
//		//String ld=df.format();
//		//System.out.println(dd);
//		proDate.setProperty("date", "2016-03-01 00:00:00");
//		proDate.setProperty("bgDate", "2016-03-22 14");
//		proDate.store(w,"bgDate");
//		timer();
//		String date=proDate.getProperty("date");
//		String lastDate=proDate.getProperty("bgDate");
//		System.out.println(lastDate);
//		w.close();
//		
//	}
	public static void timer(){
		Timer time=new Timer();
		time.schedule(new TimerTask(){
			@Override
			public void run() {
				System.out.println(getDate());
				System.out.println(getLastDate());
			}
			
		}, 0,10000);
	}
	public static String getDate(){
		return proDate.getProperty("date");
	}
	public static String getBgDate(){
		return proDate.getProperty("bgDate");
	}
	public static void setBgDate(String value){
		String path="d:/temp/config/RunnTime.properties";
		try {
			Writer w=new FileWriter(path);
			proDate.setProperty("bgDate", value);
			proDate.store(w, "bgDate");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getLastDate(){
		return proDate.getProperty("lastDate");
	}
	public static void setDate(String value){
		//String path=proDate.getClass().getResource("/config/RunnTime.properties").getPath();
		String path="d:/temp/config/RunnTime.properties";
		try {
			Writer w=new FileWriter(path);
			proDate.setProperty("date", value);
			proDate.store(w, "date");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("时间更新成功！");
	}
	public static void setLastDate(String value){
		String path="d:/temp/config/RunnTime.properties";
		try {
			Writer w=new FileWriter(path);
			proDate.setProperty("lastDate", value);
			proDate.store(w, "lastDate");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
