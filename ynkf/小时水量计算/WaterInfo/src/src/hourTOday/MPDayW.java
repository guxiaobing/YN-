package src.hourTOday;

import java.math.BigDecimal;
import java.sql.Date;

public class MPDayW {
	
	private String mpcd;
	
	private Date dt;
	
	private BigDecimal day_w;
	
	private String spe_reg_date;
	
	private Date ts;
	
	private String dt_src;

	public String getMpcd() {
		return mpcd;
	}

	public void setMpcd(String mpcd) {
		this.mpcd = mpcd;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public BigDecimal getDay_w() {
		return day_w;
	}

	public void setDay_w(BigDecimal day_w) {
		this.day_w = day_w;
	}

	public String getSpe_reg_date() {
		return spe_reg_date;
	}

	public void setSpe_reg_date(String spe_reg_date) {
		this.spe_reg_date = spe_reg_date;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getDt_src() {
		return dt_src;
	}

	public void setDt_src(String dt_src) {
		this.dt_src = dt_src;
	}
	
	

}
