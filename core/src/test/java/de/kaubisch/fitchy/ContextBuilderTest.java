package de.kaubisch.fitchy;

import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.net.URL;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Test;

public class ContextBuilderTest {

	@Test(expected=IllegalArgumentException.class)
	public void fromUrl_withNullUrl_throwsIllegalArgumentException() {
		ContextBuilder.fromUrl(null);
	}
	
	@Test
	public void fromUrl_withUrl_returnsNewBuilder() {
		URL urlToFeatures = this.getClass().getResource("/test_features.properties");
		ContextBuilder builder = ContextBuilder.fromUrl(urlToFeatures);
		assertThat(builder, IsNot.not((ContextBuilder)null));
	}

	@Test
	public void create_callOnce_returnsNewBuilder() {
		assertThat(ContextBuilder.create(), IsNot.not((ContextBuilder)null));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void fromStream_withoutStream_throwsIllegalArgumentException() {
		ContextBuilder.fromStream(null);
	}
	
	@Test
	public void fromStream_withStream_returnsNewBuilder() {
		InputStream is = this.getClass().getResourceAsStream("/test_features.properties");
		assertThat(ContextBuilder.fromStream(is), IsNot.not((ContextBuilder)null));
	}
	
	@Test
	public void build_withEmptyBuilder_returnsNewEmptyContext() {
		 FeatureContext context = ContextBuilder.create().build();
		 assertThat(context, IsNot.not((FeatureContext)null));
	}
	
	@Test
	public void build_withEmptyBuilderAndCustomConfig_returnsNewEmptyContextWithCustomConfig() {
		FitchyConfig config = FitchyConfig.getDefault();
		config.enabled = null;
		FeatureContext context = ContextBuilder.create().withConfig(config).build();
		assertThat(context.getConfig().enabled, Is.is((FeatureStatus) null));
	}
	
	@Test
	public void build_withUrlBuilder_returnsNewContextWithUrlFeatures() {
		URL urlToFeatures = this.getClass().getResource("/test_features.properties");
		FeatureContext context = ContextBuilder.fromUrl(urlToFeatures).build();
		assertThatFeatureIsInContext(context, "feature_test", FitchyConfig.getDefault().enabled);
	}

	
	@Test
	public void build_withStreamBuilder_returnsNewContextWithStreamFeatures() {
		InputStream is = this.getClass().getResourceAsStream("/test_features.properties");
		FeatureContext context = ContextBuilder.fromStream(is).build();
		assertThatFeatureIsInContext(context, "feature_test", FitchyConfig.getDefault().enabled);
	}

	@Test
	public void create_callOnce_ReturnsNewBuilder() {
		ContextBuilder context = ContextBuilder.create();
		assertThat(context, IsNot.not((ContextBuilder)null));
	}

	private void assertThatFeatureIsInContext(FeatureContext context, String name, FeatureStatus status) {
		assertThat(context.hasFeature(name), Is.is(true));
		assertThat(context.featureHasStatus(name, status), Is.is(true));
	}
}
