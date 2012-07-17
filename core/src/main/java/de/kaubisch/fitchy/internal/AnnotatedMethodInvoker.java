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
import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.resolver.AnnotationNotFoundException;
import de.kaubisch.fitchy.resolver.AnnotationRetriever;
import de.kaubisch.fitchy.resolver.FeatureResolver;
import de.kaubisch.fitchy.resolver.FeatureResolverFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class lookup Annotations from a method and searches for {@link FeatureSwitch}
 * annotation. If annotation is found it checks if feature that is given by this annotation
 * is enabled and invokes the original method.
 * If the annotation is not found it also invokes the original method.
 * If feature is not available the class returns null.
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/13/12
 * Time: 11:06 AM
 */
public class AnnotatedMethodInvoker {

    private Object origin;
    private FeatureResolverFactory resolverFactory;

    /**
     * Initialize class with orginal object and a {@link FeatureResolverFactory}
     * instance. The original object is required for calling method
     * later.
     *
     * @param origin original object
     * @param resolverFactory a {@link FeatureResolverFactory} that can create a new instance of {@link FeatureResolver}
     */
    public AnnotatedMethodInvoker(Object origin, FeatureResolverFactory resolverFactory) {
        this.origin = origin;
        this.resolverFactory = resolverFactory;
    }

    /**
     * Determine if a passed {@link Method} has {@link FeatureSwitch} annotation and that
     * the feature is enabled. It invokes the original method if the feature is enabled or
     * if the annotation is not found. Otherwise it returns null.
     *
     * @param method {@link Method} that was called from outside
     * @param arguments of method call
     * @return returns the original return value or null
     * @throws Exception rethrow exceptions that a method call can throw
     */
    public Object invoke(Method method, Object[] arguments) {
        AnnotationRetriever retriever = new AnnotationRetriever(FeatureSwitch.class, origin.getClass());
        Object result = null;
        try {
            try {
                FeatureSwitch featureSwitch = retriever.getAnnotation(method);
                FeatureResolver resolver =  resolverFactory.createResolver();
                if(resolver.isFeatureAvailable(featureSwitch)) {
                    result = method.invoke(origin, arguments);
                }
            } catch (AnnotationNotFoundException e) {
                result = method.invoke(origin, arguments);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return result;
    }
}
