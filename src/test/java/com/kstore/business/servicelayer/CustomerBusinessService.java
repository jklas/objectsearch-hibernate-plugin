package com.kstore.business.servicelayer;

import com.kstore.business.dto.CustomerDto;
import com.kstore.business.dto.CustomerPersonalInfoDto;

public interface CustomerBusinessService {

	public void customerRegistration(CustomerDto customerDto);
	
	public void customerUpdatePersonalInfo(CustomerPersonalInfoDto personalInfoDto);
}
