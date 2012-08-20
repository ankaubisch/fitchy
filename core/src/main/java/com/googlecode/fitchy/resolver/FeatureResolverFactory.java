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
package com.googlecode.fitchy.resolver;

import com.googlecode.fitchy.Configuration;
import com.googlecode.fitchy.FeatureContext;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/13/12
 * Time: 11:44 AM
 */
public class FeatureResolverFactory {

    private FeatureContext context;
    private Configuration config;

    public FeatureResolverFactory(FeatureContext context, Configuration config) {
        this.context = context;
        this.config = config;
    }

    public FeatureResolverFactory(FeatureContext context) {
        this(context, context.getConfig());
    }

    public FeatureResolver createResolver() {
        return new FeatureResolver(context, config);
    }
}
