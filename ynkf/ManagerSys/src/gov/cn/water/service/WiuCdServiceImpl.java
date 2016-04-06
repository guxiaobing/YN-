package gov.cn.water.service;

import gov.cn.water.bzmapper.WiuCdMapper;
import gov.cn.water.dao.SysMenu;
import gov.cn.water.dao.Tree;
import gov.cn.water.dao.User;
import gov.cn.water.dao.wiuCd;
import gov.cn.water.mapper.MenuMapper;
import gov.cn.water.vo.pageVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WiuCdServiceImpl implements WiuCdService {

	@Autowired
	private MenuMapper mm;
	@Autowired
	private WiuCdMapper wcm;
	@Cacheable("cache")
	public List<Tree> getMenus(){
		List<Tree> listTree=new ArrayList<Tree>();
		try {
			List<SysMenu> list=mm.getMenu();
			for(SysMenu sm:list){
				Tree node=new Tree();
				node.setId(sm.getId());
				node.setPid(sm.getParentid());
				node.setText(sm.getName());
				node.setIconCls(sm.getIconCls());
				if(sm.getParentid()!=0){	// 有父节点
					node.setPid(sm.getParentid());
				}
				if(sm.getCountChildrens() > 0){	//有子节点
					node.setState("closed");
				}
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("url", sm.getUrl());
				node.setAttributes(attr);
				listTree.add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(listTree.toString()+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

		return listTree;
	}
	@Cacheable("cache")
	public List<Tree> getMenu(User user) {
		List<Tree> listTree=new ArrayList<Tree>();
		try {
			List<SysMenu> list=mm.getMenuByUserId(user);
			for(SysMenu sm:list){
				Tree node=new Tree();
				node.setId(sm.getId());
				node.setPid(sm.getParentid());
				node.setText(sm.getName());
				node.setIconCls(sm.getIconCls());
				if(sm.getParentid()!=0){	// 有父节点
					node.setPid(sm.getParentid());
				}
				if(sm.getCountChildrens() > 0){	//有子节点
					node.setState("closed");
				}
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("url", sm.getUrl());
				node.setAttributes(attr);
				listTree.add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(listTree.toString()+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

		return listTree;
	}
	public User getUserInfo(User user) {
		User use=new User();
		try {
			use= mm.getUserInfo(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return use;
	}
	public List<wiuCd> getList(pageVo pv) {
		List<wiuCd> list=new ArrayList<wiuCd>();
		try {
			list= wcm.WiuCdList(pv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(list.toString());
		return list;
	}
	public int getTotal(pageVo pv) {
		int total=0;
		try {
			total=wcm.getTotal(pv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	public List<SysMenu> getMenuList() {
		List<SysMenu> menuList=new ArrayList<SysMenu>();
		List<SysMenu> parentList=new ArrayList<SysMenu>();
		List<SysMenu> childList=new ArrayList<SysMenu>();
		try {
			List<SysMenu> list=mm.getMenuList();
			for(SysMenu sm:list){
				if(sm.getParentid()==0){//没有父节点
					parentList.add(sm);
				}else{
					childList.add(sm);
				}
			}
			for(int i=0;i<parentList.size();i++){
				int childId=Integer.parseInt(parentList.get(i).getSequence());
				menuList.add(parentList.get(i));
				for(int m=0;m<childList.size();m++){
					if(childId==childList.get(m).getParentid()){
						childList.get(m).setParent(parentList.get(i).getName());
						menuList.add(childList.get(m));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuList;
	}
	public List<SysMenu> getParentMenu() {
		List<SysMenu> parentList=new ArrayList<SysMenu>();
		try {
			parentList=mm.getParentMenu();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parentList;
	}
	public int delMenu(int value) {
		int count=0;
		try {
			count=mm.delMenuById(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	public int addMenu(SysMenu sm) {
		int count=0;
		try {
			int id=mm.selID(sm.getParentid());
			int nextid=mm.selMAX();
			sm.setNextID(nextid+1);
			sm.setId(id+1);
			count=mm.addMenu(sm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
}
