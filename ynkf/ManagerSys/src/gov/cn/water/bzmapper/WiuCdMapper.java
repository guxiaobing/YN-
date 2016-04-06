package gov.cn.water.bzmapper;

import gov.cn.water.dao.wiuCd;
import gov.cn.water.vo.pageVo;

import java.util.List;

public interface WiuCdMapper {
	public List<wiuCd> WiuCdList(pageVo name)throws Exception;
	public int getTotal(pageVo name)throws Exception;
}
