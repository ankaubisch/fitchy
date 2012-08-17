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

import java.io.InputStream;
import java.net.URL;

/**
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 8/6/12
 * Time: 8:08 PM
 */
public class AspectJFeatureContext {

    private static FeatureContext context;

    private static final String SEMAPHORE = "semaphore";

    public static FeatureContext initializeFor(FitchyConfig config, URL features) {
        synchronized(SEMAPHORE) {
            context = ContextBuilder
						.fromUrl(features)
						.withConfig(config)
						.build();
        }
        return context;
    }

    public static FeatureContext initializeFor(FitchyConfig config, InputStream is) {
        synchronized (SEMAPHORE) {
            context = ContextBuilder
            			.fromStream(is)
            			.withConfig(config)
            			.build();
        }
        return context;
    }

    public static FeatureContext getInstance() {
        FeatureContext context = null;
        synchronized (SEMAPHORE) {
            context = AspectJFeatureContext.context;
        }

        return context;
    }

}
