package gov.cn.water.dao;

import java.io.Serializable;
import java.util.List;

public class SysMenu implements Serializable{
	@Override
	public String toString() {
		return "SysMenu [id=" + id + ", name=" + name + ", parentid="
				+ parentid + ", sequence=" + sequence + ", iconCls=" + iconCls
				+ ", url=" + url + ", enable=" + enable + ", countChildrens="
				+ countChildrens + ", parentMenu=" + parentMenu + ", subMenu="
				+ subMenu + ", hasMenu=" + hasMenu + "]";
	}
	/**
	 * 鑿滃崟ID
	 */
	private int nextID;
	
	public int getNextID() {
		return nextID;
	}
	public void setNextID(int nextID) {
		this.nextID = nextID;
	}
	private Integer id;
	private int jsID;
	
	public int getJsID() {
		return jsID;
	}
	public void setJsID(int jsID) {
		this.jsID = jsID;
	}
	/**
	 * 鑿滃崟鍚嶇О
	 */
	private String name;
	/**
	 * 鐖剁骇鑿滃崟ID 0琛ㄧず鏍硅妭鐐�
	 */
	private Integer parentid;
	/**
	 * 鑿滃崟椤哄簭
	 */
	private String sequence;
	/**
	 * 鑿滃崟鍥炬爣鏍峰紡
	 */
	private String iconCls;	
	/**
	 * 鑿滃崟閾炬帴鍦板潃
	 */
	private String url;
	/**
	 * 鍙敤鐘舵�
	 */
	private Integer enable;
	/**
	 * 瀛愯妭鐐逛釜鏁�
	 */
	private Integer countChildrens;
	
	private SysMenu parentMenu;
	private String parent;
	
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	private List<SysMenu> subMenu;
	private boolean hasMenu = false;
	
	public Integer getCountChildrens() {
		return countChildrens;
	}
	public void setCountChildrens(Integer countChildrens) {
		this.countChildrens = countChildrens;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public SysMenu getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(SysMenu parentMenu) {
		this.parentMenu = parentMenu;
	}
	public List<SysMenu> getSubMenu() {
		return subMenu;
	}
	public void setSubMenu(List<SysMenu> subMenu) {
		this.subMenu = subMenu;
	}
	public boolean isHasMenu() {
		return hasMenu;
	}
	public void setHasMenu(boolean hasMenu) {
		this.hasMenu = hasMenu;
	}
}
