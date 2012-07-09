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
package de.kaubisch.fitchy.sample;

import de.kaubisch.fitchy.Fitchy;
import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.internal.CglibObserver;
import de.kaubisch.fitchy.options.FitchConfig;
import de.kaubisch.fitchy.store.FeatureContext;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/6/12
 * Time: 3:17 PM
 */
public class CglibProxyApp {

    private String message;

    public CglibProxyApp(String message) {
        this.message = message;
    }

    @FeatureSwitch("simple_test3")
    public String getMessage() {
        return message;
    }

    public static void main(String[] args) {
        FeatureContext context = Fitchy.loadStoreFromResource("/sample_features.properties");
        FitchConfig options = Fitchy.getConfig();
        CglibProxyApp app = Fitchy.observeWithObserver(new CglibProxyApp("Hello world"), context, CglibObserver.class);

        System.out.println("Message:" + app.getMessage());


    }
}
