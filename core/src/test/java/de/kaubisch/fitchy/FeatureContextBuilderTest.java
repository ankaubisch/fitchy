package de.kaubisch.fitchy;

import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.net.URL;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import de.kaubisch.fitchy.FeatureContext.Builder;

public class FeatureContextBuilderTest {

	@Test(expected=IllegalArgumentException.class)
	public void fromUrl_withNullUrl_throwsIllegalArgumentException() {
		Builder.fromUrl(null);
	}
	
	@Test
	public void fromUrl_withUrl_returnsNewBuilder() {
		URL urlToFeatures = this.getClass().getResource("/test_features.properties");
		Builder builder = Builder.fromUrl(urlToFeatures);
		assertThat(builder, IsNot.not((Builder)null));
	}

	@Test
	public void create_callOnce_returnsNewBuilder() {
		assertThat(Builder.create(), IsNot.not((Builder)null));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void fromStream_withoutStream_throwsIllegalArgumentException() {
		Builder.fromStream(null);
	}
	
	@Test
	public void fromStream_withStream_returnsNewBuilder() {
		InputStream is = this.getClass().getResourceAsStream("/test_features.properties");
		assertThat(Builder.fromStream(is), IsNot.not((Builder)null));
	}
	
	@Test
	public void build_withEmptyBuilder_returnsNewEmptyContext() {
		 FeatureContext context = Builder.create().build();
		 assertThat(context, IsNot.not((FeatureContext)null));
	}
	
	@Test
	public void build_withEmptyBuilderAndCustomConfig_returnsNewEmptyContextWithCustomConfig() {
		Configuration config = Configuration.getDefault();
		config.enabled = null;
		FeatureContext context = Builder.create().withConfig(config).build();
		assertThat(context.getConfig().enabled, Is.is((FeatureStatus) null));
	}
	
	@Test
	public void build_withUrlBuilder_returnsNewContextWithUrlFeatures() {
		URL urlToFeatures = this.getClass().getResource("/test_features.properties");
		FeatureContext context = Builder.fromUrl(urlToFeatures).build();
		assertThatFeatureIsInContext(context, "feature_test", Configuration.getDefault().enabled);
	}

	
	@Test
	public void build_withStreamBuilder_returnsNewContextWithStreamFeatures() {
		InputStream is = this.getClass().getResourceAsStream("/test_features.properties");
		FeatureContext context = Builder.fromStream(is).build();
		assertThatFeatureIsInContext(context, "feature_test", Configuration.getDefault().enabled);
	}

	@Test
	public void create_callOnce_ReturnsNewBuilder() {
		Builder context = Builder.create();
		assertThat(context, IsNot.not((Builder)null));
	}

	private void assertThatFeatureIsInContext(FeatureContext context, String name, FeatureStatus status) {
		assertThat(context.hasFeature(name), Is.is(true));
		assertThat(context.featureHasStatus(name, status), Is.is(true));
	}
}
