package de.kaubisch.fitchy.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.kaubisch.fitchy.Feature;
import de.kaubisch.fitchy.FeatureStatus;

public class PropertyFeatureLoader extends FeatureLoader {

	private InputStream is;
	
	public PropertyFeatureLoader(InputStream is) {
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
				String enabledString = properties.getProperty(keyName, option.disabled.getSystemName());
				FeatureStatus status = getStatus(enabledString);
				if(status != null) {
					Feature feature = new Feature(keyName, status);
					featureList.add(feature);
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return featureList;
	}

	private FeatureStatus getStatus(String enabledString) {
		List<FeatureStatus> statusList = option.statusList;
		FeatureStatus status = null;
		for(FeatureStatus entry : statusList) {
			if(enabledString.equals(entry.getSystemName())) {
				status = entry;
				break;
			}
		}
		return status;
	}

}
