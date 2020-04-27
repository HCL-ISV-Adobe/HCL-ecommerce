package com.hcl.ecomm.core.models;

import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.wcm.core.components.models.Title;
import com.day.cq.wcm.api.Page;


/**
 * The Class TitleModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = Title.class, resourceType = "hclecomm/components/content/title")
public class TitleModel implements Title {
	
	/** The current page. */
	@ScriptVariable
	private Page currentPage;

	/** The title. */
	@Self
	@Via(type = ResourceSuperType.class)
	private Title title;

	/** The element type. */
	@Inject
	@Via("resource")
	private String type;
	

	@Override
	public String getText() {
		return title.getText();
	}

	@Override
	public String getType() {
		return title.getType() != null ? title.getType() : type;
	}

	@Override
	public String getExportedType() {
		return title.getExportedType();
	}

	@Override
	public String getLinkURL() {
		return title.getLinkURL();
	}

	@Override
	public boolean isLinkDisabled() {
		return title.isLinkDisabled();
	}
}
