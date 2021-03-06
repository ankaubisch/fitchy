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
package com.googlecode.fitchy;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.fitchy.Configuration;
import com.googlecode.fitchy.Feature;
import com.googlecode.fitchy.FeatureContext;
import com.googlecode.fitchy.FeatureStatus;
import com.googlecode.fitchy.exception.FeatureAlreadyExistsException;

import static org.junit.Assert.*;


/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 6/27/12
 * Time: 9:31 PM
 */
public class FeatureContextTest {

    private Configuration options;

    private FeatureContext context;

    @Before
    public void setUp() throws Exception {
        options = Configuration.getDefault();
        context = new FeatureContext(options);
    }

    @Test
    public void addFeature_WithString_FeatureAddedWithEnabledStatus() throws Exception {
        Feature feature = context.addFeature("test.feature");
        assertThat(feature.getName(), Is.is("test.feature"));
        assertThat(feature.getStatus(), Is.is(options.enabledStatus));
    }

    @Test(expected=IllegalArgumentException.class)
    public void addFeature_WithNullString_ThrowsException() {
        Feature feature = context.addFeature((String)null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void addFeature_WithEmptyString_ThrowsException() {
        Feature feature = context.addFeature("");
    }

    @Test(expected=FeatureAlreadyExistsException.class)
    public void addFeature_WithAlreadyAddedString_ThrowsException() {
        context.addFeature("test.feature");
        context.addFeature("test.feature");
    }

    @Test
    public void addFeature_WithFeature_FeatureAdded() {
        Feature feature = new Feature("test.feature", options.enabledStatus);
        assertSame(feature, context.addFeature(feature));
    }

    @Test(expected=IllegalArgumentException.class)
    public void addFeature_WithoutFeature_ThrowsException() {
        context.addFeature((Feature)null);
    }

    @Test(expected=FeatureAlreadyExistsException.class)
    public void addFeature_WithAlreadyAddedFeature_ThrowsException() {
        Feature feature = new Feature("test.feature", options.enabledStatus);
        context.addFeature(feature);
        context.addFeature(feature);
    }

    @Test
    public void hasFeature_WithString_ReturnsFalse() {
        assertFalse("context should not have feature with name 'test.feature'", context.hasFeature("test.feature"));
    }

    @Test
    public void hasFeature_WithoutString_ReturnsFalse() {
        assertFalse("context should return false if no feature argument is set", context.hasFeature(null));
    }

    @Test
    public void hasFeature_WithString_ReturnsTrue() {
        context.addFeature("test.feature");
        assertTrue("context should find feature with name 'test.feature", context.hasFeature("test.feature"));
    }

    @Test
    public void featureHasStatus_WithAddedFeature_ReturnsTrue() {
        addFeatureToContext("test.feature", options.enabledStatus);
        assertTrue("feature 'test.feature' should have status enabledStatus", context.featureHasStatus("test.feature", options.enabledStatus));
    }

    @Test
    public void featureHasStatus_WithAddedFeature_ReturnsFalse() {
        addFeatureToContext("test.feature", options.enabledStatus);
        assertFalse("feature 'test.feature' should have status enabledStatus", context.featureHasStatus("test.feature", options.disabledStatus));
    }

    @Test(expected=IllegalArgumentException.class)
    public void featureHasStatus_WithoutFeatureKey_ThrowsException() {
        addFeatureToContext("test.feature", options.enabledStatus);
        context.featureHasStatus(null, options.enabledStatus);
    }

    @Test
    public void clear() throws Exception {
        context.addFeature("test.feature");
        context.clear();
        assertFalse("context should have no feature", context.hasFeature("test.feature"));
    }

    @Test
    public void size_addOneFeature_ReturnsOne() {
        context.addFeature("test.feature");
        assertThat(context.size(), Is.is(1));
    }

    private void addFeatureToContext(String name, FeatureStatus status) {
        Feature feature = new Feature(name, status);
        context.addFeature(feature);
    }
}
