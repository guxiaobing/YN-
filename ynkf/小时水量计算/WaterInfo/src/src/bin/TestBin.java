package src.bin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import src.hourTOday.mpComputer;
import src.qys.abc;
import src.qys.newHour;
import src.util.JdbcUtil;

public class TestBin {
	static Connection conn=null;
	static PreparedStatement pst=null;
	static ResultSet rs=null;
	static JdbcUtil ju=new JdbcUtil();
	static newHour zHour=new newHour();
	static abc qHour=new abc();
	static mpComputer mct=new mpComputer();
	static SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat df2=new SimpleDateFormat("yyyy-MM-dd HH");
	public static void main(String[] args) throws SQLException{
		conn=ju.getConnBZ();
		Timer timer=new Timer();
		timer.schedule(new TimerTask(){
			String bgDate=ju.getBgDate();
			@Override
			public void run() {
				String endDt=zHour.getLastTime(conn, pst, rs);
				
				//String endDt="2016-03-28 04:00:00";
				try {
					Date beginDate=df.parse(endDt);
					beginDate.setHours(beginDate.getHours()+1);
					System.out.println(bgDate+"<<<<>>>>"+endDt);
					zHour.runDate(bgDate,endDt);
					qHour.runDate(bgDate,endDt);
					if(beginDate.getHours()<=2){
						beginDate.setDate(beginDate.getDay()-1);
						String bg=df1.format(beginDate);
						mct.findAllMpCdDistinct(bg);
						beginDate.setDate(beginDate.getDay()-1);
						String bg1=df1.format(beginDate);
						mct.findAllMpCdDistinct(bg1);
						beginDate.setDate(beginDate.getDay()-1);
						String bg2=df1.format(beginDate);
						mct.findAllMpCdDistinct(bg2);
					}else{
						mct.findAllMpCdDistinct(df1.format(df1.parse(endDt)));
						beginDate.setHours(beginDate.getHours()-24);
						mct.findAllMpCdDistinct(df1.format(beginDate));
						beginDate.setHours(beginDate.getHours()-24);
						mct.findAllMpCdDistinct(df1.format(beginDate));
					}
					Date de=df.parse(endDt);
					de.setHours(de.getHours()+1+2);
					String nextDt=df.format(de);
					System.out.println("Next Starting Time >>>>>>>  "+nextDt+"  <<<<<<<<<<<<<<<<<<");
					de.setHours(de.getHours()-6);
					ju.setBgDate(df2.format(de));
				} catch (ParseException | SQLException e) {
					e.printStackTrace();
				}
			}}, 0, 2*60*60*1000);
	}
}
