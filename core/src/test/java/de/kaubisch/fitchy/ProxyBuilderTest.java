package de.kaubisch.fitchy;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Comparator;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.kaubisch.fitchy.exception.CannotCreateProxyException;

@RunWith(MockitoJUnitRunner.class)
public class ProxyBuilderTest {

	@Mock
	private FeatureContext context;
	
	@Mock
	private FeatureObserver observer;
	
	private Configuration config;
	
	@Before
	public void setUp() {
		config = new Configuration();
		config.observerClass = FeatureObserverMock.class;
		when(context.getConfig()).thenReturn(config);
	}
	
	@After 
	public void tearDown() {
		FeatureObserverMock.isObserveCalled = false;
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void fromContext_withoutContext_throwsIllegalArgumentException() {
		ProxyBuilder.fromContext(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void fromContext_withoutObserverClass_throwsIllegalArgumentException() {
		config.observerClass = null;
		ProxyBuilder.fromContext(context);
	}
	
	@Test
	public void fromContext_withContext_returnsNewProxyBuilder() {
		assertThat(ProxyBuilder.fromContext(context), IsNot.not((ProxyBuilder)null));
	}

	@Test(expected=IllegalArgumentException.class)
	public void withObserver_withoutInstance_throwsIllegalArgumentException() {
		ProxyBuilder.fromContext(context).withObserver(null);
	}
	
	@Test
	public void build_withImplementingObject_returnNewProxy() {
		Comparator<Object> observableObject = new ImplementingClass();
		observableObject = ProxyBuilder.fromContext(context).build(observableObject);
		assertThat(FeatureObserverMock.isObserveCalled, Is.is(true));
	}
	
	@Test
	public void build_withCustomObserver_returnsProxiedObject() {
		ImplementingClass implementingObject = new ImplementingClass();
		implementingObject = ProxyBuilder
								.fromContext(context)
								.withObserver(new NullFeatureObserver())
								.build(implementingObject);
		assertThat(implementingObject, Is.is((ImplementingClass)null));
		assertThat(FeatureObserverMock.isObserveCalled, Is.is(false));
	}
}

class ImplementingClass implements Comparator<Object> {

	public int compare(Object arg0, Object arg1) {
		return 0;
	}
}

class NullFeatureObserver implements FeatureObserver {
	public <T> T observe(Object toObserve, FeatureContext context) {
		return null;
	}
}

class FeatureObserverMock implements FeatureObserver {

	static boolean isObserveCalled;
	
	public <T> T observe(Object toObserve, FeatureContext context) {
		isObserveCalled = true;
		return (T)toObserve;
	}
	
	public boolean isObserveCalled() {
		return isObserveCalled;
	}
}