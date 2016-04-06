package gov.cn.water.init;

import gov.cn.water.controller.SysController;
import gov.cn.water.dao.SysMenu;
import gov.cn.water.dao.Tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

public class MenuList {
	private final static Logger log=LoggerFactory.getLogger(MenuList.class);
	static Properties pro;
	static Connection conn=null;
	static PreparedStatement pst=null;
	static ResultSet rs=null;
//	static{
//		InputStream is=MenuList.class.getClassLoader().getResourceAsStream("jdbc.properties");
//		pro=new Properties();
//		try {
//			pro.load(is);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			Class.forName(pro.getProperty("jdbc.driverClassName"));
//			log.info("数据库驱动加载成功。。。。");
//		} catch (ClassNotFoundException e) {
//			log.info("数据库驱动加载失败。。。。");
//			e.printStackTrace();
//		}
//	}
	public static Connection getConn() throws IOException, SQLException{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:gxb", "user_yn_kf",
				"dhckf");
	}
	public static void getMenu() throws IOException, SQLException{
		conn=getConn();
		pst=conn.prepareStatement("select * from sysmenu");
		rs=pst.executeQuery();
		List<SysMenu> list=new ArrayList<SysMenu>();
		while(rs.next()){
			SysMenu sm=new SysMenu();
			sm.setId(rs.getInt("id"));
			sm.setName(rs.getString("name"));
			sm.setParentid(rs.getInt("parentid"));
			sm.setUrl(rs.getString("url"));
			list.add(sm);
		}
		System.out.println(list.toString()+"@@@@@@@@@@@@@@@@@@@@"+list.size());
		if(rs!=null)rs.close();
		if(pst!=null)pst.close();
		if(conn!=null)conn.close();
	}
}
