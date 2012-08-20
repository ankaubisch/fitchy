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

import com.googlecode.fitchy.FeatureContext;
import com.googlecode.fitchy.internal.JavaProxyObserver;

import java.io.Serializable;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/17/12
 * Time: 8:09 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class JavaProxyObserverTest {

    @Mock
    private FeatureContext context;

    private JavaProxyObserver observer;

    @Before
    public void setUp() {
        observer = new JavaProxyObserver();
    }

    @Test(expected = IllegalArgumentException.class)
    public void observe_WithoutContext_throwsException() {
        observer.observe(new JavaProxiedClass(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void observe_WithoutObservableObject_throwsException() {
        observer.observe(null, context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void observe_WithoutImplementingObject_throwsException() {
        observer.observe(new JavaProxyObserverTest(), context);
    }

    @Test
    public void observe_withObject_returnsInvocationHandler() {
        Serializable object = observer.observe(new JavaProxiedClass(), context);
        assertThat(Proxy.isProxyClass(object.getClass()),equalTo(true));
        assertThat(Proxy.getInvocationHandler(object).getClass().toString(), equalTo(JavaProxyObserver.ProxyInvocationHandler.class.toString()));
    }
}

class JavaProxiedClass implements Serializable {

}
