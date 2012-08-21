package com.googlecode.fitchy;
import com.googlecode.fitchy.annotation.FeatureSwitch;


public class SpringHelloWorld {

	@FeatureSwitch("feature_test")
	public void hello() {
		System.out.println("Hello World!");
	}
	
}
