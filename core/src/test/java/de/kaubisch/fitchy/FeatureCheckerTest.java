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
package de.kaubisch.fitchy;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/11/12
 * Time: 6:55 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class FeatureCheckerTest {
    @Mock
    private FeatureContext context;

    private String featureName;

    private FeatureChecker<String> checker;

    @Before
    public void setUp() {
        checker = new FeatureChecker<String>(context, "test.feature") {
            @Override
            public String onFeatureEnabled() {
                return "enabled";
            }

            @Override
            public String onFeatureDisabled() {
                return "disabled";
            }
        };
    }

    @Test
    public void run_WithFeature_ReturnEnabled(){
        when(context.hasFeature("test.feature")).thenReturn(true);

        assertEquals("checker should return 'enabled' string", "enabled", checker.run());
    }

    @Test
    public void run_WithoutFeature_ReturnDisabled() {
        when(context.hasFeature("test.feature")).thenReturn(false);
        assertEquals("checker should return 'disabled' string", "disabled", checker.run());
    }
}
