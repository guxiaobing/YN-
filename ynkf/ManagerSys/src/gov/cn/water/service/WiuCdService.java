package gov.cn.water.service;

import java.util.List;

import gov.cn.water.dao.SysMenu;
import gov.cn.water.dao.Tree;
import gov.cn.water.dao.User;
import gov.cn.water.dao.wiuCd;
import gov.cn.water.vo.pageVo;

import org.springframework.stereotype.Service;

@Service
public interface WiuCdService {
	public List<Tree> getMenu(User user);
	public User getUserInfo(User user);
	public List<Tree> getMenus();
	public List<SysMenu> getMenuList();
	public  List<SysMenu> getParentMenu();
	public int delMenu(int value);
	public int addMenu(SysMenu sm);
	
	public List<wiuCd> getList(pageVo pv);
	public int getTotal(pageVo pv);
}
