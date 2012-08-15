package de.kaubisch.fitchy;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.net.URL;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;

public class ContextBuilderTest {

	private ContextBuilder builder;
	
	private FitchyConfig config;
	
	@Before
	public void setUp() {
		config = FitchyConfig.getDefault();
		builder = new ContextBuilder(config);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createFromUrl_withNullUrl_throwsIllegalArgumentException() {
		builder.createFromUrl(null);
	}
	
	@Test
	public void createFromUrl_withUrl_ReturnsNewContext() {
		URL urlToFeatures = this.getClass().getResource("/test_features.properties");
		FeatureContext context = builder.createFromUrl(urlToFeatures);
		assertThat(context, IsNot.not((FeatureContext)null));
		assertThat(context.hasFeature("feature_test"), Is.is(true));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createFromStream_withoutStream_ThrowsIllegalArgumentException() {
		builder.createFromStream(null);
	}
	
	@Test
	public void createFromStream_WithStream_returnsNewContext() {
		InputStream is = this.getClass().getResourceAsStream("/test_features.properties");
		FeatureContext context = builder.createFromStream(is);
		assertThat(context, IsNot.not((FeatureContext)null));
		assertThat(context.hasFeature("feature_test"), Is.is(true));
	}
	
	@Test
	public void createEmpty_callOnce_ReturnsNewContext() {
		FeatureContext context = builder.createEmpty();
		assertThat(context, IsNot.not((FeatureContext)null));
	}
	
	@Test
	public void createEmpty_callTwice_ReturnsNotSameImstance() {
		FeatureContext context1 = builder.createEmpty();
		FeatureContext context2 = builder.createEmpty();
		assertNotSame(context1, context2);
	}
}
