package de.kaubisch.fitchy.options;

import java.util.ArrayList;
import java.util.List;

import de.kaubisch.fitchy.FeatureStatus;


public class FitchyOptions {
	public List<FeatureStatus> statusList;
	
	public FeatureStatus enabled;
	public FeatureStatus disabled;
	
	private FitchyOptions() {
		this.statusList = new ArrayList<FeatureStatus>();
	}
	
	public static FitchyOptions newOption(FeatureStatus status) {
		FitchyOptions option = new FitchyOptions();
		for(FeatureStatus e : status.getClass().getEnumConstants()) {
			option.statusList.add(e);
			if(e.isEnabledStatus()) {
				option.enabled = e;
			} else if(e.isDisabledStatus()) {
				option.disabled= e;
			}
		}
		return option;
	}

	public static FitchyOptions getDefault() {
		return newOption(DefaultFeatureStatus.ON);
	}
}
