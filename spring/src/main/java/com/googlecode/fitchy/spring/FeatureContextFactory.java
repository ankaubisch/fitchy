package com.googlecode.fitchy.spring;

import java.net.URL;

import com.googlecode.fitchy.Configuration;
import com.googlecode.fitchy.FeatureContext;


public class FeatureContextFactory {

	private URL resourceUrl;

	private Configuration config;

    public FeatureContextFactory(URL resourceUrl, Configuration config) {
        this.resourceUrl = resourceUrl;
        this.config = config;
    }

	public FeatureContext createContext() {
		return FeatureContext.Builder.fromUrl(resourceUrl).withConfig(config).build();
	}
}
