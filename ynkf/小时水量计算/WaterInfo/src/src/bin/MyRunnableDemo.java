package src.bin;

import src.qys.abc;
import src.qys.newHour;

public class MyRunnableDemo {
	public static void main(String[] args){
		MyRunnable mr=new MyRunnable("2016-03-25 08","2016-03-26 00:00:00");
		MyRunnable mr1=new MyRunnable("2016-03-25 08","2016-03-26 00:00:00");
		Thread thread=new Thread(mr,"ˮλ");
		Thread thread1=new Thread(mr1,"����");
		
		thread.start();
		thread1.start();
	}
}
