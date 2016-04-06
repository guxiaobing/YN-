package gov.cn.water.vo;

public class pageVo {
	private String searchName;
	private int pageSize;
	private int pageNum;
	private int total;
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "pageVo [searchName=" + searchName + ", pageSize=" + pageSize
				+ ", pageNum=" + pageNum + ", total=" + total + "]";
	}
	
}
