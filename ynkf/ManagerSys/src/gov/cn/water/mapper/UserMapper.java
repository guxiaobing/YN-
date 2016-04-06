package gov.cn.water.mapper;

import gov.cn.water.dao.User;

public interface UserMapper {
	public int login(User user)throws Exception;
}
