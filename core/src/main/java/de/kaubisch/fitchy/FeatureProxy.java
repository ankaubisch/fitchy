package de.kaubisch.fitchy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.example.HelloWorld;
import de.kaubisch.fitchy.store.FeatureStore;

public class FeatureProxy implements InvocationHandler {

	private FeatureStore manager;
	
	private Object origin;
	private Class<?> originClass;
	
	public FeatureProxy(FeatureStore manager, Object origin) {
		this.manager = manager;
		this.origin = origin;
		this.originClass = origin.getClass();
	}
	
	public static <T> T newInstance(FeatureStore store, Object obj) {
		T proxyInstance = (T)Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new FeatureProxy(store, obj));
		
		return proxyInstance;
	}
	
	@Override
	public Object invoke(Object instance, Method method, Object[] arguments)
			throws Throwable {
		Object returnValue = null;
		FeatureSwitch annotation = retrieveAnnotation(FeatureSwitch.class, method);
		if(annotation != null) {
			if(manager.hasFeature(annotation)) {
				returnValue = method.invoke(origin, arguments);
			}
		} else {
			returnValue = method.invoke(origin, arguments);			
		}
		return returnValue;
	}
	
	private <T extends Annotation> T retrieveAnnotation(Class<? extends Annotation> annotationClass, Method method) {
		T annotation = (T) method.getAnnotation(annotationClass);
		if(annotation == null && !method.getDeclaringClass().equals(originClass)) {
			try {
				Method implementedMethod = originClass.getMethod(method.getName(), method.getParameterTypes());
				annotation = (T) implementedMethod.getAnnotation(annotationClass);
	
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return annotation;
	}

}
