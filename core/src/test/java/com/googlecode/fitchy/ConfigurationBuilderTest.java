package com.googlecode.fitchy;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import com.googlecode.fitchy.Configuration;
import com.googlecode.fitchy.FeatureStatus;
import com.googlecode.fitchy.Configuration.Builder;
import com.googlecode.fitchy.internal.JavaProxyObserver;
import com.googlecode.fitchy.internal.PropertyFeatureReader;


public class ConfigurationBuilderTest {

	@Test(expected=IllegalArgumentException.class)
	public void fromStatus_withoutStatus_throwsIllegalArgumentException() {
		Builder.fromStatus(null);
	}
	
	@Test
	public void fromStatus_withStatus_returnNewBuilder() {
		Builder builder = Builder.fromStatus(TestStatus.ON);
		assertThat(builder, IsNot.not((Builder)null));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void fromStream_withoutInputStream_throwsIllegalArgumentException() {
		Builder.fromStream(null);
	}
	
	@Test
	public void fromStream_WithInputStream_returnNewBuilder() {
		Builder builder = Builder.fromStream(this.getClass().getResourceAsStream("/test-config.properties"));
		assertThat(builder, IsNot.not((Builder)null));
	}

	@Test(expected=IllegalArgumentException.class)
	public void withObserver_withoutObserver_throwsIllegalArgumentException() {
		Builder.fromStatus(TestStatus.ON).withObserver(null);
	}
	
	@Test
	public void withObserver_withObserverClass_returnsSameBuilder() {
		Builder builder = Builder.fromStatus(TestStatus.ON);
		assertThat(builder.withObserver(FeatureObserverMock.class), Is.is(builder));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void withReader_withoutReaderClass_throwsIllegalArgumentException() {
		Builder.fromStatus(TestStatus.ON).withReader(null);
	}
	
	@Test
	public void withReader_withReaderClass_returnsSameBuilder() {
		Builder builder = Builder.fromStatus(TestStatus.ON);
		assertThat(builder.withReader(PropertyFeatureReader.class), Is.is(builder));
	}

	@Test
	public void build_withDefaultObserverClass_returnsNewConfigurationWithDefaultObserver() {
		Configuration config = Builder
				.fromStatus(TestStatus.ON)
				.build();
		assertSame("observerClass should be JavaProxyObserver", JavaProxyObserver.class, config.getObserverClass());
	}
	
	@Test
	public void build_withDefaultReaderClass_returnsNewConfigurationWithDefaultReader() {
		Configuration config = Builder
				.fromStatus(TestStatus.ON)
				.build();
		assertSame("readerClass should be PropertyFeatureReader", PropertyFeatureReader.class, config.getReaderClass());
	}
	
	@Test
	public void build_withObserverClass_returnsNewConfiguration() {
		Configuration config = Builder
								.fromStatus(TestStatus.ON)
								.withObserver(FeatureObserverMock.class)
								.build();
		assertSame("observerClass should be FeatureObserverMock", FeatureObserverMock.class, config.getObserverClass());
	}
	
	@Test
	public void build_withDefaultFeatureStatus_returnNewConfigurationWithRightStatus() {
		Configuration config = Builder
				.fromStatus(TestStatus.ON)
				.build();		
		assertThat(config.enabledStatus, Is.is((FeatureStatus)TestStatus.ON));
		assertThat(config.disabledStatus, Is.is((FeatureStatus)TestStatus.OFF));
	}
}

enum TestStatus implements FeatureStatus {
	ON("on", true),
	OFF("off", false)
	;
	
	private String name;
	private boolean enabled;
	
	private TestStatus(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}
	
	public String getSystemName() {
		return name;
	}

	public boolean isEnabledStatus() {
		return enabled;
	}

	public boolean isDisabledStatus() {
		return !enabled;
	}
}
