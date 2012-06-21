/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *	or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *	regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package de.kaubisch.fitchy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.store.FeatureStore;

public class FeatureProxy implements InvocationHandler {

	private FeatureStore store;
	
	private Object origin;
	private Class<?> originClass;
	
	public FeatureProxy(FeatureStore store, Object origin) {
		this.store = store;
		this.origin = origin;
		this.originClass = origin.getClass();
	}
	
	@SuppressWarnings("unchecked")
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
			if(store.featureHasStatus(annotation.value(), Fitchy.getOptions().enabled)) {
				returnValue = method.invoke(origin, arguments);
			}
		} else {
			returnValue = method.invoke(origin, arguments);			
		}
		return returnValue;
	}
	
	@SuppressWarnings("unchecked")
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
