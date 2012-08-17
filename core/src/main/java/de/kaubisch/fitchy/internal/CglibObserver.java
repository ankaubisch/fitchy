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
package de.kaubisch.fitchy.internal;

import de.kaubisch.fitchy.FeatureContext;
import de.kaubisch.fitchy.FeatureObserver;
import de.kaubisch.fitchy.exception.CannotCreateProxyException;
import de.kaubisch.fitchy.resolver.FeatureResolverFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static de.kaubisch.fitchy.util.Preconditions.*;

/**
 * {@link CglibObserver} is a {@link FeatureObserver} implementation
 * that uses cglib to surround source object with a proxy. cglib doesn't
 * need interfaces to create a proxy like a {@link java.lang.reflect.Proxy} instance.
 * This {@link FeatureObserver} searches the source class for a standard constructor.
 *
 * If there is no standard constructor the observer try to lookup the first available
 * constructor and use this if it has no primitive arguments. Otherwise it tries another one.
 * When a constructor is found that doesn't have primitive arguments the observer tries to create
 * an instance while all arguments will have a null value.
 *
 * So when you want to use this {@link FeatureObserver} you must ensure that to further logic is in
 * constructor and that a constructor doesn't throw any exceptions.
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 6/24/12
 * Time: 1:06 PM
 */
public class CglibObserver implements FeatureObserver {

    private static final Map<Class<?>, Object> defaultValueMap;

    static {
        defaultValueMap = new HashMap<Class<?>, Object>();
        defaultValueMap.put(Integer.TYPE, -1);
        defaultValueMap.put(Long.TYPE, -1);
        defaultValueMap.put(Double.TYPE, -1);
        defaultValueMap.put(Float.TYPE, -1);
        defaultValueMap.put(Byte.TYPE, -1);
    }

    /**
     * Implementation of {@link MethodInterceptor} that is needed when you want to create
     * a proxy with cglib. This Interceptor works the same way like
     * {@link de.kaubisch.fitchy.internal.JavaProxyObserver.ProxyInvocationHandler}.
     */
    public static class CglibMethodInterceptor implements MethodInterceptor {

        private Object origin;
        private FeatureContext context;

        public CglibMethodInterceptor(Object origin, FeatureContext context) {
            this.origin = origin;
            this.context = context;
        }

        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            return new AnnotatedMethodInvoker(origin, new FeatureResolverFactory(context)).invoke(method, objects);
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> T observe(Object toObserve, FeatureContext context) {
        throwIllegalArgumentExceptionIfNull(context, "FeatureContext instance is required.");
        throwIllegalArgumentExceptionIfNull(toObserve, "Observable object is required.");

        Enhancer e = new Enhancer();
        e.setSuperclass(toObserve.getClass());
        e.setCallback(new CglibMethodInterceptor(toObserve, context));
        T proxiedObject = null;
        if(hasStandardConstructor(toObserve.getClass())) {
            proxiedObject = (T)e.create();
        } else {
            proxiedObject = createProxyWithFirstUsableConstructor(e, toObserve);
        }
        
        if(proxiedObject == null) {
        	throw new CannotCreateProxyException("CglibObserver did not found a constructor to enhance target class");
        }
        return proxiedObject;
    }

    /**
     * Lookup sourceClass for a constructor with zero arguments.
     * It returns true if a constructor with zero argument is found otherwise
     * it returns false.
     *
     * @param sourceClass {@link Class} which needs to be inspected
     * @return returns true if standard constructor is found otherwise it returns false
     */
    private boolean hasStandardConstructor(Class<?> sourceClass) {
        boolean hasStandardConstructor = false;
        try {
            Constructor c = sourceClass.getConstructor(new Class<?>[]{});
            hasStandardConstructor = c != null;
        } catch (NoSuchMethodException e) {
            hasStandardConstructor = false;
        }

        return hasStandardConstructor;
    }

    /**
     * Searches source class for all available constructor and take the first that not have a primitive class
     * in his arguments. After that the function will use cglib to create a proxy and return it.
     *
     * @param e current {@link Enhancer} of cglib
     * @param toObserve source object that needs to be surrounded from a proxy
     * @param <T> target class
     * @return a proxied target class instance surrounding toObserve
     */
    private <T> T createProxyWithFirstUsableConstructor(Enhancer e, Object toObserve) {
        T proxiedObject = null;
        Constructor[] constructors = toObserve.getClass().getConstructors();
        
        for(Constructor c : constructors) {
            Class<?>[] argumentClasses = c.getParameterTypes();
            Object[] values = getConstructorValues(argumentClasses);
            proxiedObject = (T)e.create(argumentClasses, values);
            break;
        }

        return proxiedObject;
    }

    /**
     * Create an array of objects that can be used to instantiate an object
     * with a {@link Constructor} object. It lookup default values in an internal
     * map (especially for native classes). If no default value is found it fill this
     * argument with null
     *
     * @param classes all argument classes of a constructor
     * @return an array of possible argument values for a constructor
     */
    private Object[] getConstructorValues(Class<?>[] classes) {
        Object[] values = new Object[classes.length];
        for(int i = 0; i < classes.length; i++) {
            values[i] = defaultValueMap.get(classes[i]);
        }

        return values;
    }
}
