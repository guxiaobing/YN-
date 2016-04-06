package gov.cn.water.init;

import java.io.IOException;
import java.sql.SQLException;

import gov.cn.water.dao.User;
import gov.cn.water.service.WiuCdServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AfterSpring implements ServletContextListener, HttpSessionBindingListener{
	private static ApplicationContext context;
	public void valueBound(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void valueUnbound(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

	public void contextInitialized(ServletContextEvent sce) {
		ApplicationContext conject=WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		setContext(conject);
		sce.getServletContext().log("初始化成功");
		loadMenuCache(sce);
	}
	public void loadMenuCache(ServletContextEvent sce){
//		MenuList ml=new MenuList();
//		try {
//			//ml.getMenu();
//		} catch (IOException e) {
//			sce.getServletContext().log("菜单加入缓存失败。。。。。");
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		sce.getServletContext().log("将菜单加入缓存成功。。。。。");
	}
	public static void setContext(ApplicationContext contextinject) {
		if (context != null) {
			return;
		}
		context = contextinject;
	}

}
