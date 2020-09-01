package com.hcl.ecomm.core.utility;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.Externalizer;

public class CommonUtils {
	
	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private CommonUtils() {
		
	}
	
	public static URI getAbsolutePageUrl(ResourceResolver resolver, String path) throws URISyntaxException {
		Externalizer ext	= resolver.adaptTo(Externalizer.class);
		return new URI(ext !=null ? ext.externalLink(resolver, Externalizer.LOCAL, path) : StringUtils.EMPTY);
	}
	


}
