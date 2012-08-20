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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.googlecode.fitchy.annotation.FeatureSwitch;
import com.googlecode.fitchy.exception.AnnotationNotFoundException;
import com.googlecode.fitchy.exception.InvokeException;
import com.googlecode.fitchy.resolver.AnnotationRetriever;
import com.googlecode.fitchy.resolver.FeatureResolver;
import com.googlecode.fitchy.resolver.FeatureResolverFactory;

/**
 * This class lookup Annotations from a method and searches for {@link FeatureSwitch}
 * annotation. If annotation is found it checks if feature that is given by this annotation
 * is enabledStatus and invokes the original method.
 * If the annotation is not found it also invokes the original method.
 * If feature is not available the class returns null.
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/13/12
 * Time: 11:06 AM
 */
public class AnnotatedMethodInvoker {

    private FeatureResolverFactory resolverFactory;
    private MethodInvoke invoke;

    /**
     * With an implementation of this Interface the AnnotatedMethodInvoker
     * can be customized how the method will be called.
     * 
     * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
     */
    public interface MethodInvoke {
    	
    	/**
    	 * Invokes the method of an Object with arguments. 
    	 * 
    	 * @param method the {@link Method} that should be called
    	 * @param args all arguments which are needed to call this method
    	 * @return a return value of this method that was called
    	 * @throws InvokeException container for all exceptions that this method can throw
    	 */
        Object invoke(Method method, Object[] args) throws InvokeException;
        
        /**
         * Returns the {@link Class} of that object, that will be called with
         * the function {@link MethodInvoke#invoke(Method, Object[])}
         * @return the {@link Class} of original instance
         */
        Class<?> getTargetClass();
    }

    /**
     * Initialize class with orginal object and a {@link FeatureResolverFactory}
     * instance. The original object is required for calling method
     * later.
     *
     * @param origin original object
     * @param resolverFactory a {@link FeatureResolverFactory} that can create a new instance of {@link FeatureResolver}
     */
    public AnnotatedMethodInvoker(final Object origin, FeatureResolverFactory resolverFactory) {
        this.resolverFactory = resolverFactory;
        invoke = new MethodInvoke() {
            public Object invoke(Method method, Object[] args) throws InvokeException {
            	boolean accessible = method.isAccessible();
            	try{
            		method.setAccessible(true);
            		return method.invoke(origin, args);
            	} catch (IllegalAccessException e) {
            		throw new InvokeException(e);
            	} catch (InvocationTargetException e) {
            		throw new InvokeException(e);
            	} finally {
            		method.setAccessible(accessible);
            	}
            }

            public Class<?> getTargetClass() {
                return origin.getClass();
            }
        };
    }

    public AnnotatedMethodInvoker(MethodInvoke invoke, FeatureResolverFactory resolverFactory) {
        this.resolverFactory = resolverFactory;
        this.invoke = invoke;
    }

    /**
     * Determine if a passed {@link Method} has {@link FeatureSwitch} annotation and that
     * the feature is enabledStatus. It invokes the original method if the feature is enabledStatus or
     * if the annotation is not found. Otherwise it returns null.
     *
     * @param method {@link Method} that was called from outside
     * @param arguments of method call
     * @return returns the original return value or null
     * @throws Throwable rethrow exceptions that a method call can throw
     */
    public Object invoke(Method method, Object[] arguments) throws Throwable {
        AnnotationRetriever retriever = new AnnotationRetriever(FeatureSwitch.class, invoke.getTargetClass());
        Object result = null;
        try {
            FeatureSwitch featureSwitch = retriever.getAnnotation(method);
            FeatureResolver resolver =  resolverFactory.createResolver();
            if(resolver.isFeatureAvailable(featureSwitch)) {
                result = invoke.invoke(method, arguments);
            }
        } catch (AnnotationNotFoundException e) {
            result = invoke.invoke(method, arguments);
        } catch (InvokeException e) {
        	throw e.getCause();
        }

        return result;
    }
}
