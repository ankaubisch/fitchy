package com.googlecode.fitchy.spring;

import java.net.URL;

import com.googlecode.fitchy.Configuration;
import com.googlecode.fitchy.FeatureContext;

public class FeatureContextFactory {

	private URL resourceUrl;

	private Configuration config;
	
	public FeatureContext createContext() {
		return FeatureContext.Builder.fromUrl(resourceUrl).withConfig(config).build();
	}

	public URL getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(URL resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}
}
