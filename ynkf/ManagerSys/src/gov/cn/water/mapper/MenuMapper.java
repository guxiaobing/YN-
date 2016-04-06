package gov.cn.water.mapper;

import java.util.List;

import gov.cn.water.dao.SysMenu;
import gov.cn.water.dao.User;

public interface MenuMapper {
	public List<SysMenu> getMenuByUserId(User user)throws Exception;
	public User getUserInfo(User user)throws Exception;
	public List<SysMenu> getMenu()throws Exception;
	
	public List<SysMenu> getMenuList()throws Exception;
	
	public List<SysMenu> getParentMenu()throws Exception;
	
	public int selID(int value)throws Exception;
	public int addMenu(SysMenu sm)throws Exception;
	public int selMAX()throws Exception;
	
	public int delMenuById(int value)throws Exception;
}
