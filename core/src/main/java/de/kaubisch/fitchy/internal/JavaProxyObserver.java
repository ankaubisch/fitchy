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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * {@link JavaProxyObserver} is an implementation of {@link FeatureObserver}
 * and uses the builtin java {@link Proxy} feature to surround object with a proxy.
 * The source class needs to implement at least one interface.
 * The internal static class {@link ProxyInvocationHandler} handles the method invocation
 * of the proxied object.
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 6/24/12
 * Time: 1:01 PM
 */
public class JavaProxyObserver implements FeatureObserver {

    /**
     * The {@link ProxyInvocationHandler} implementation that looks for
     * a {@link FeatureSwitch} annotation which is marking the method and uses
     * {@link FeatureContext} attribute to determine whether the method is called or not.
     *
     */
    public static class ProxyInvocationHandler implements InvocationHandler {
        private Object origin;
        private FeatureContext context;

        public ProxyInvocationHandler(Object origin, FeatureContext context) {
            this.origin = origin;
            this.context = context;
        }

        /**
         * {@inheritDoc}
         */
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
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
    public <T> T observe(Object obj, FeatureContext context) {
        T proxyInstance = (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new ProxyInvocationHandler(obj, context));
        return proxyInstance;
    }
}
