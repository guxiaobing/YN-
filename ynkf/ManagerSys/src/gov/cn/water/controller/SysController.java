package gov.cn.water.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.cn.water.dao.SysMenu;
import gov.cn.water.dao.Tree;
import gov.cn.water.dao.User;
import gov.cn.water.dao.wiuCd;
import gov.cn.water.service.WiuCdServiceImpl;
import gov.cn.water.vo.pageVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysController {
	private final Logger log=LoggerFactory.getLogger(SysController.class);
	
	@Autowired
	private WiuCdServiceImpl wci;
	@RequestMapping("/")
	public String home(){
		log.info("·µ»ØÊ×Ò³");
		System.out.println("ÇëµÇÂ¼");
		return "jsp/index";
	}
	@RequestMapping("login")
	public String login(HttpServletRequest request,HttpServletResponse response,Model model){
		log.info("ÇëÇóµÇÂ¼ÓÃ»§>>>>"+request.getParameter("loginname"));
		User user=new User();
		user.setUsername(request.getParameter("loginname"));
		user.setPassword(request.getParameter("password"));
		System.out.println(request.getParameter("loginname"));
		request.getSession().setAttribute("User", user);
		return "jsp/index";
	}
	@RequestMapping("getMenu")
	@ResponseBody
	public List<Tree> getMenu(HttpSession session){
		User user=(User) session.getAttribute("User");
		user.setId(1);
		return wci.getMenu(user);
	}
	@RequestMapping(value="/logout")
	public String logout(HttpSession session,HttpServletResponse response){
		session.removeAttribute("User");
		Cookie cookie = new Cookie("2016-02-03", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		return "redirect:/";
	}
	@RequestMapping("userInfo")
	public String getUserInfo(HttpSession session,Model model){
		User user=(User) session.getAttribute("User");
		user.setId(1);
		User use=wci.getUserInfo(user);
		model.addAttribute("user", use);
		return "jsp/list";
	}
	@RequestMapping("getList")
	public String getList(HttpServletRequest request,HttpServletResponse response,Model model){
		System.out.println("!!!!!!!!!!!!!!!!!!!!");
		return "jsp/list";
	}
	@RequestMapping("getUnnomal")
	@ResponseBody
	public Object getWiuCdList(HttpServletRequest request,HttpServletResponse response,Model model){
		int pageSize=Integer.parseInt((request.getParameter("rows")==null?"12":request.getParameter("rows")));
		int pageNum=Integer.parseInt((request.getParameter("page")==null?"1":request.getParameter("page")));
		String searchName=request.getParameter("search_name");
		Map<String,Object> map=new HashMap<String,Object>();
		pageVo pv=new pageVo();
		pv.setPageNum(pageNum);
		pv.setPageSize(pageSize);
		if(searchName!=null){
			pv.setSearchName(searchName);
		}else{
			pv.setSearchName("");
		}
		List<wiuCd> list=wci.getList(pv);
		int total=wci.getTotal(pv);
		map.put("total", total);
		map.put("rows", list);
		
		//System.out.println(searchName+"pageSize:"+pageSize+"   pageNum:"+pageNum+"  total: "+total+"   listsize:"+list.size());
		return map;
	}
	@ResponseBody
	@RequestMapping("/getMenuList1")
	public Object getMenuList1(HttpServletRequest request,HttpServletResponse response,Model model){
		List<SysMenu> list=wci.getMenuList();
		System.out.println(list.toString());
		return list;
	}
	@RequestMapping("/getMenuList")
	public String getMenuList(HttpServletRequest request,HttpServletResponse response,Model model){
		return "jsp/menuList";
	}
	@ResponseBody
	@RequestMapping("/getParentMenu")
	public Object getParentMenu(HttpServletRequest request,HttpServletResponse response,Model model){
		return wci.getParentMenu();
	}
	@RequestMapping("/delMenu")
	public void delMenu(HttpServletRequest request,HttpServletResponse response){
		String id=request.getParameter("id");
		int count=wci.delMenu(Integer.parseInt(id));
		try {
			response.getWriter().print(count);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("/addMenu")
	public void addMenu(HttpServletRequest request,HttpServletResponse response){
		String url=request.getParameter("url");
		String text=request.getParameter("name");
		String parentMenu=request.getParameter("Parentid");
		String enable=request.getParameter("enable");
		SysMenu sm=new SysMenu();
		User user=(User)request.getSession().getAttribute("User");
		sm.setJsID(user.getId());
		sm.setUrl(url);
		sm.setName(text);
		sm.setEnable(Integer.parseInt(enable));
		sm.setParentid(Integer.parseInt(parentMenu));
		int count=wci.addMenu(sm);
		try {
			response.getWriter().print(count);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("/getSession")
	public String getSession(HttpServletRequest request,HttpServletResponse response){
		return "jsp/menu";
	}
}
