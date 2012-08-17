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
package de.kaubisch.fitchy.sample.aspectj;

import de.kaubisch.fitchy.AspectJFeatureContext;
import de.kaubisch.fitchy.Feature;
import de.kaubisch.fitchy.FeatureContext;
import de.kaubisch.fitchy.Configuration;
import de.kaubisch.fitchy.annotation.FeatureSwitch;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 8/9/12
 * Time: 7:28 PM
 */
public class AspectJApp {

    @FeatureSwitch("test.feature")
    public void sayHello() {
        System.out.println("Hello world!");
        sayHello2();
    }

    @FeatureSwitch("test.feature2")
    private void sayHello2() {
        System.out.println("Hello world 2!");
    }


    public static void main(String[] args) {
        FeatureContext context = AspectJFeatureContext.initializeFor(Configuration.getDefault(), AspectJApp.class.getResourceAsStream("/sample_features.properties"));
        context.addFeature("test.feature");
        AspectJApp app = new AspectJApp();
        System.out.println("Start test");
        app.sayHello();
        context.clear();
        System.out.println("start test2");
        app.sayHello();
        System.out.println("start test3");
        context.addFeature("test.feature");
        context.addFeature("test.feature2");
        app.sayHello();
    }
}
