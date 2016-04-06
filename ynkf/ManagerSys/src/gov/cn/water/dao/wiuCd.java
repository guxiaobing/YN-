package gov.cn.water.dao;

public class wiuCd {
	private String wiu_cd;
	private String wiu_nm;
	private String lr_nm;
	private String addr;
	private String adl_cd;
	private String tel;
	private String ad_nm;
	private String wiu_tp;
	
	public String getWiu_tp() {
		return wiu_tp;
	}
	public void setWiu_tp(String wiu_tp) {
		this.wiu_tp = wiu_tp;
	}
	public String getWiu_cd() {
		return wiu_cd;
	}
	public void setWiu_cd(String wiu_cd) {
		this.wiu_cd = wiu_cd;
	}
	public String getWiu_nm() {
		return wiu_nm;
	}
	public void setWiu_nm(String wiu_nm) {
		this.wiu_nm = wiu_nm;
	}
	public String getLr_nm() {
		return lr_nm;
	}
	public void setLr_nm(String lr_nm) {
		this.lr_nm = lr_nm;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getAdl_cd() {
		return adl_cd;
	}
	public void setAdl_cd(String adl_cd) {
		this.adl_cd = adl_cd;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAd_nm() {
		return ad_nm;
	}
	public void setAd_nm(String ad_nm) {
		this.ad_nm = ad_nm;
	}
	@Override
	public String toString() {
		return "wiuCd [wiu_cd=" + wiu_cd + ", wiu_nm=" + wiu_nm + ", lr_nm="
				+ lr_nm + ", addr=" + addr + ", adl_cd=" + adl_cd + ", tel="
				+ tel + ", ad_nm=" + ad_nm + ", wiu_tp=" + wiu_tp + "]";
	}
}
