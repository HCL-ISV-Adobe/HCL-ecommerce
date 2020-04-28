package com.hcl.ecomm.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class ButtonModel {

	@Inject
	@Default(values="btn-primary")
	private String buttonType;

	@Inject
	@Default(values="false")
	private String isButtonDisable;

	@PostConstruct
	protected void init() {
		if (StringUtils.isNotEmpty(isButtonDisable) && isButtonDisable.equals("true")) {
			buttonType = buttonType.concat("__disabled");
		}
	}

	public String getButtonType() {
		return buttonType;
	}
	
}
