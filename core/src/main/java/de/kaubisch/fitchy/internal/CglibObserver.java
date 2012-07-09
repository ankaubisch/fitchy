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

import de.kaubisch.fitchy.FeatureObserver;
import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.resolver.AnnotationNotFoundException;
import de.kaubisch.fitchy.resolver.AnnotationRetriever;
import de.kaubisch.fitchy.resolver.FeatureResolver;
import de.kaubisch.fitchy.store.FeatureContext;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

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

            AnnotationRetriever retriever = new AnnotationRetriever(FeatureSwitch.class, origin.getClass());
            Object result = null;
            try {
                FeatureSwitch featureSwitch = retriever.getAnnotation(method);
                FeatureResolver resolver = new FeatureResolver(context);
                if(resolver.isFeatureAvailable(featureSwitch)) {
                    result = method.invoke(origin, objects);
                }
            } catch (AnnotationNotFoundException e) {
                result = method.invoke(origin, objects);
            }

            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> T observe(Object toObserve, FeatureContext context) {
        Enhancer e = new Enhancer();
        e.setSuperclass(toObserve.getClass());
        e.setCallback(new CglibMethodInterceptor(toObserve, context));
        T proxiedObject = null;
        if(hasStandardConstructor(toObserve.getClass())) {
            proxiedObject = (T)e.create();
        } else {
            proxiedObject = createProxyWithFirstUsableConstructor(e, toObserve);
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
            hasStandardConstructor = true;
        } catch (NoSuchMethodException e) {
            hasStandardConstructor = false;
        }

        return hasStandardConstructor;
    }

    private <T> T createProxyWithFirstUsableConstructor(Enhancer e, Object toObserve) {
        T proxiedObject = null;
        Constructor[] constructors = toObserve.getClass().getConstructors();
        for(Constructor c : constructors) {
            Class<?>[] argumentClasses = c.getParameterTypes();
            if(hasPrimitives(argumentClasses)) {
                continue;
            }
            Object[] values = new Object[argumentClasses.length];
            proxiedObject = (T)e.create(argumentClasses, values);
            break;
        }

        return proxiedObject;
    }

    /**
     * Iterate through array of classes an check if one of this classes
     * is a primitive one.
     *
     * @param classes classes of method arguments
     * @return return true if a primitive class is found
     */
    private boolean hasPrimitives(Class<?>[] classes) {
        for(Class<?> cls : classes) {
            if(cls.isPrimitive()) {
                return true;
            }
        }

        return false;
    }
}