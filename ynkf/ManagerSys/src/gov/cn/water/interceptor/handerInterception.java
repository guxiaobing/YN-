package gov.cn.water.interceptor;

import gov.cn.water.dao.User;
import gov.cn.water.mapper.UserMapper;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class handerInterception implements HandlerInterceptor {
	private final Logger log=LoggerFactory.getLogger(handerInterception.class);
	@Resource
	private UserMapper um;
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception arg3)
			throws Exception {

	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView arg3) throws Exception {

	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj) throws Exception {
		log.info("来自>>>"+request.getRequestURL()+" 请求！");
		String requestUrl=request.getRequestURI();//获取请求的地址
		String requestContextPath=request.getContextPath();//获取项目路径
		String url=requestUrl.substring(requestContextPath.length());//获取请求的方法 
		//获取session
		User user=(User) request.getSession().getAttribute("User");
		if(user==null){
			Cookie[] cookie=request.getCookies();
			if(cookie!=null && cookie.length>0){
				String cookieValue=null;
				for(int i=0;i<cookie.length;i++){
					//System.out.println(cookie[i].getName());
					if("2016-02-03".equals(cookie[i].getName())){
						cookieValue=cookie[i].getValue();
					}
				}
				if(cookieValue==null){
					if(url.contains("login")){
						request.getRequestDispatcher("/").forward(request, response);
						return true;
					}
					System.out.println("未登录，请登录");
					response.sendRedirect(request.getContextPath()+"/");
					//request.getRequestDispatcher("login.jsp").forward(request, response);
					return false;
				}
			}else{
				if(url.contains("login")){
					return true;
				}
				response.sendRedirect(request.getContextPath()+"/");
				//request.getRequestDispatcher("login.jsp").forward(request, response);
				return false;
			}
		}else{
			if(user.getUsername()==null){
				user.setUsername("");
			}
			if(user.getPassword()==null){
				user.setPassword("");
			}
			int count=um.login(user);
			if(count==1){
				return true;
			}else{
				response.sendRedirect(request.getContextPath()+"/");
				request.getSession().removeValue("User");
				//request.getRequestDispatcher("login.jsp").forward(request, response);
				return false;
			}
		}
			
		return true;
	}

}
