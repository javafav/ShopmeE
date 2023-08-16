package com.shopme.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@Service
public class SettingService {

	@Autowired private SettingRepository repo;
	


public List<Setting> getGeneralSetting() {


	return repo.findByTwoCategories(SettingCategory.CURRENCY, SettingCategory.GENERAL);
	
	
	
}

public EmailSettingBag getEmailSetting() {
 List<Setting> setting = repo.findByCategory(SettingCategory.MAIL_SERVER);
 setting.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));

 return new  EmailSettingBag(setting);
}



}