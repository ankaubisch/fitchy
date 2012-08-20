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


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.googlecode.fitchy.annotation.FeatureSwitch;
import com.googlecode.fitchy.internal.AnnotatedMethodInvoker;
import com.googlecode.fitchy.resolver.FeatureResolver;
import com.googlecode.fitchy.resolver.FeatureResolverFactory;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/13/12
 * Time: 11:28 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotatedMethodInvokerTest {

    @Mock
    private FeatureResolverFactory resolverFactory;

    @Mock
    private FeatureResolver resolver;

    private AnnotatedMethodInvoker invoker;

    @Before
    public void setUp() {
        invoker = new AnnotatedMethodInvoker(new AnnotatedFeatureMock(), resolverFactory);
        when(resolverFactory.createResolver()).thenReturn(resolver);
    }

    @Test
    public void invoke_WithFeature_ReturnsOriginalReturnValue() throws Throwable {
        Method sayHelloWithAnnotation = getMethod("sayHelloWithAnnotation");
        when(resolver.isFeatureAvailable((FeatureSwitch)any())).thenReturn(true);
        assertEquals("result should be orgin value 'hello'", "hello", invoker.invoke(sayHelloWithAnnotation, new Object[]{}));
    }

    @Test
    public void invoke_WithDisabledFeature_ReturnsNullValue() throws Throwable {
        Method sayHelloWithAnnotation = getMethod("sayHelloWithAnnotation");
        when(resolver.isFeatureAvailable((FeatureSwitch)any())).thenReturn(false);
        assertNull("result should be null because feature is disabledStatus", invoker.invoke(sayHelloWithAnnotation, new Object[] {}));
    }

    @Test
    public void invoke_WithoutFeature_ReturnsOrginalReturnValue() throws Throwable {
        Method sayHelloWithoutAnnotation = getMethod("sayHelloWithoutAnnotation");
        assertEquals("result should be origin value 'hello'", "hello", invoker.invoke(sayHelloWithoutAnnotation, new Object[]{}));
        verify(resolver, times(0)).isFeatureAvailable((FeatureSwitch)any());
    }

    private Method getMethod(String methodName, Class... arguments) {
        try {
            return AnnotatedFeatureMock.class.getDeclaredMethod(methodName, arguments);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

}

class AnnotatedFeatureMock {

    @FeatureSwitch("test.feature")
    public String sayHelloWithAnnotation() {
        return "hello";
    }

    public String sayHelloWithoutAnnotation() {
        return "hello";
    }
}
