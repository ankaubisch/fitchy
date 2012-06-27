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
package de.kaubisch.fitchy.resolver;

import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.options.DefaultFeatureStatus;
import de.kaubisch.fitchy.store.FeatureContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;

import static org.mockito.Mockito.when;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 6/22/12
 * Time: 12:41 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class FeatureResolverTest {

    @Mock
    FeatureContext context;

    @Mock
    FeatureSwitch annotation;

    private FeatureResolver resolver;

    @Before
    public void setUp() throws Exception {
        resolver = new FeatureResolver(context);
    }

    @Test
    public void IsFeatureAvailable_WithoutAnnotationStatus_ReturnsTrue() throws Exception {
        when(context.featureHasStatus("test.feature", DefaultFeatureStatus.ON)).thenReturn(true);
        when(annotation.value()).thenReturn("test.feature");
        when(annotation.status()).thenReturn("");
        assertTrue(resolver.isFeatureAvailable(annotation));
    }

}
