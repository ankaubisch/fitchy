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
package com.googlecode.fitchy.internal;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.googlecode.fitchy.FeatureContext;
import com.googlecode.fitchy.FeatureObserver;
import com.googlecode.fitchy.resolver.FeatureResolverFactory;

import static com.googlecode.fitchy.util.Preconditions.*;

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
     * a {@link com.googlecode.fitchy.annotation.FeatureSwitch} annotation which is marking the method and uses
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
            return new AnnotatedMethodInvoker(origin, new FeatureResolverFactory(context)).invoke(method, objects);
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> T observe(Object obj, FeatureContext context) {
        throwIllegalArgumentExceptionIfNull(context, "FeatureContext instance is required.");
        throwIllegalArgumentExceptionIfNull(obj, "observable object is required.");
        throwIllegalArgumentExceptionIfFalse(obj.getClass().getInterfaces().length != 0, "observable object must implement an interface.");
        return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new ProxyInvocationHandler(obj, context));
    }
}
