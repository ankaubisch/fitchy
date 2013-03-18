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
package com.googlecode.fitchy.spring;

import com.googlecode.fitchy.Configuration;
import com.googlecode.fitchy.FeatureContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 3/18/13
 * Time: 8:48 PM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FeatureContext.Builder.class)
public class FeatureContextFactoryTest {

    @Mock private URL resourceUrl;
    @Mock private Configuration config;

    private FeatureContextFactory contextFactory;

    @Before
    public void setUp() {
        contextFactory = new FeatureContextFactory(resourceUrl, config);
    }

    @Test
    public void createContext_WithUrlAndConfig_createsNewFactoryWithBuilder() {
        mockStatic(FeatureContext.Builder.class);
        FeatureContext.Builder builder = mock(FeatureContext.Builder.class, Mockito.RETURNS_DEEP_STUBS);
        when(FeatureContext.Builder.fromUrl(resourceUrl)).thenReturn(builder);

        contextFactory.createContext();

        verifyStatic();
        FeatureContext.Builder.fromUrl(resourceUrl);

        verify(builder).withConfig(config);
        verify(builder.withConfig(config)).build();
    }
}
