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
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.Proxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/17/12
 * Time: 8:42 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class CglibObserverTest {

    @Mock
    private FeatureContext context;

    private CglibObserver observer;

    @Before
    public void setUp() {
        observer = new CglibObserver();
    }

    @Test(expected = IllegalArgumentException.class)
    public void observe_WithoutContext_throwsException() {
        observer.observe(new JavaProxiedClass(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void observe_WithoutObservableObject_throwsException() {
        observer.observe(null, context);
    }

    @Test
    public void observe_WithStandardConstructorObject_ReturnsProxy() {
        CglibObserverTest test = observer.observe(new CglibObserverTest(), context);
        assertThat(Enhancer.isEnhanced(test.getClass()), is(equalTo(true)));
    }

    @Test
    public void observe_WithObjectConstructorObject_ReturnsProxy() {
        CglibProxiedObjectConstructorClass test = observer.observe(new CglibProxiedObjectConstructorClass("test"), context);
        assertThat(Enhancer.isEnhanced(test.getClass()), is(equalTo(true)));
    }

    @Test
    public void observe_WithNativeConstructorObject_ReturnsNull() {
        CglibProxiedNativeConstructorClass test = observer.observe(new CglibProxiedNativeConstructorClass(1), context);
        assertThat(Enhancer.isEnhanced(test.getClass()), is(equalTo(true)));
    }
}

class CglibProxiedObjectConstructorClass {

    public CglibProxiedObjectConstructorClass(String test) {

    }
}

class CglibProxiedNativeConstructorClass {
    public CglibProxiedNativeConstructorClass(int i) {

    }
}
