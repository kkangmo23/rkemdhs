package com.guardon.option.impl;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Repository;

import com.guardon.option.optionDAO;
import com.guardon.option.domain.Option;
import com.ibatis.sqlmap.client.SqlMapClient;

@Repository
public class optionDAOImpl implements optionDAO{
	
	 @Inject
	 @Named("sqlMapClient")
	 SqlMapClient sqlMapClient;

	@Override
	public void updateOption(Option option) throws Exception {
		
		sqlMapClient.update("Option.updateOption", option);
	}

	@Override
	public int getPwdLength() throws Exception {
		return (int) sqlMapClient.queryForObject("Option.getPwdLength");
	}

	@Override
	public int getAutoLogout() throws Exception {
		return (int) sqlMapClient.queryForObject("Option.getAutoLogout");
	}

	@Override
	public int getOtpTimeLimit() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLoginFailedCount() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String pwdComplexity() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
