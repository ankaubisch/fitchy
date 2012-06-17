package de.kaubisch.fitchy.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.kaubisch.fitchy.Feature;

public class ResourceFeatureLoader extends FeatureLoader {

	private InputStream is;
	
	public ResourceFeatureLoader(InputStream is) {
		super();
		this.is = is;
	}
	
	@Override
	protected List<Feature> getFeatureList() {
		Properties properties = new Properties();
		List<Feature> featureList = new ArrayList<Feature>();
		try {
			properties.load(is);
			for(Object key : properties.keySet()) {
				String keyName = String.valueOf(key);
				String enabledString = properties.getProperty(keyName, option.disabled);
				Feature feature = new Feature(keyName, option.enabled.equals(enabledString));
				featureList.add(feature);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return featureList;
	}

}
