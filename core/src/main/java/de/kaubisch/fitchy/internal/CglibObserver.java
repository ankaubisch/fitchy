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
import de.kaubisch.fitchy.store.FeatureStore;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 6/24/12
 * Time: 1:06 PM
 */
public class CglibObserver implements FeatureObserver {

    public static class CglibMethodInterceptor implements MethodInterceptor {

        private Object origin;
        private FeatureStore store;

        public CglibMethodInterceptor(Object origin, FeatureStore store) {
            this.origin = origin;
            this.store  = store;
        }
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

            AnnotationRetriever retriever = new AnnotationRetriever(FeatureSwitch.class, origin.getClass());
            Object result = null;
            try {
                FeatureSwitch featureSwitch = retriever.getAnnotation(method);
                FeatureResolver resolver = new FeatureResolver(store);
                if(resolver.isFeatureAvailable(featureSwitch)) {
                    result = method.invoke(origin, objects);
                }
            } catch (AnnotationNotFoundException e) {
                result = method.invoke(origin, objects);
            }

            return result;
        }
    }


    public <T> T observe(Object toObserve, FeatureStore store) {
        Enhancer e = new Enhancer();
        e.setSuperclass(toObserve.getClass());
        e.setCallback(new CglibMethodInterceptor(toObserve, store));
        return (T)e.create();
    }
}
