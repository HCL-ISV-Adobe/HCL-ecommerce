package com.hcl.ecomm.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class ButtonModel {

	@Inject
	private String buttonType;

	@Inject
	private String isButtonDisable;

	@PostConstruct
	protected void init() {
		if (StringUtils.isNotEmpty(buttonType) && StringUtils.isNotEmpty(isButtonDisable)) {
			buttonType = buttonType.concat("__disabled");
		}
	}

	public String getButtonType() {
		return buttonType;
	}
	
}
