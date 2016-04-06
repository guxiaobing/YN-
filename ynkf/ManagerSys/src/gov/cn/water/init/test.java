package gov.cn.water.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class test implements ServletContextListener{
	public void contextDestroyed(ServletContextEvent sce) {
		
		
	}

	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().log("初始化参数成功。。。。");
		
	}
	
}
