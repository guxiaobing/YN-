package src.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnUtil {
	//��ȡ���ݿ����ӳ�
	private static ComboPooledDataSource cpd=new ComboPooledDataSource("oracle");
	public static Connection conn=null;
	public static Connection getConn() throws SQLException{
		if(conn!=null){
			return conn;
		}
		return cpd.getConnection();
	}
	public static void main(String[] args){
		try {
			conn=getConn();
			System.out.println(conn.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
