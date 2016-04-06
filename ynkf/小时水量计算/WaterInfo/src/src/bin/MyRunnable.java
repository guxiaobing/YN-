package src.bin;

import java.sql.SQLException;
import java.text.ParseException;

import src.qys.abc;
import src.qys.newHour;

public class MyRunnable implements Runnable {
	String beginDate;
	String endDate;
	public MyRunnable(String beginDate,String endDate){
		this.beginDate=beginDate;
		this.endDate=endDate;
	}
	@Override
	public void run() {
		
		System.out.println(Thread.currentThread().getName()+"正在计算小时水量");
		
	}

}
