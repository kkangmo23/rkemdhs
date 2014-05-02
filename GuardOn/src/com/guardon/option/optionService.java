package com.guardon.option;

import java.util.ArrayList;
import java.util.Map;

import com.guardon.option.domain.Option;

public interface optionService {
	
	public void updateOption(Option option) throws Exception;
	public int getPwdLength() throws Exception;
	public int getAutoLogout() throws Exception;
	public int getOtpTimeLimit() throws Exception;
	public int getLoginFailedCount() throws Exception;
	public String pwdComplexity() throws Exception;

}
