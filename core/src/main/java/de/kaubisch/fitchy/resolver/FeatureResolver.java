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

import de.kaubisch.fitchy.FeatureStatus;
import de.kaubisch.fitchy.Fitchy;
import de.kaubisch.fitchy.FitchyConfig;
import de.kaubisch.fitchy.annotation.FeatureSwitch;
import de.kaubisch.fitchy.FeatureContext;

/**
 * {@link FeatureResolver} needs a {@link de.kaubisch.fitchy.FeatureContext} and a
 * {@link de.kaubisch.fitchy.annotation.FeatureSwitch} annotation to check if the given expression
 * of the annotation matches with the current feature.
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 6/22/12
 * Time: 11:34 AM
 */
public class FeatureResolver {

    private FeatureContext storage;

    public FeatureResolver(FeatureContext context) {
        this.storage = context;
    }

    /**
     * This functions decides whether a feature is available or not. It processes the {@link FeatureSwitch} annotation
     * and lookup in {@link de.kaubisch.fitchy.FeatureContext} member variable if feature exists. When the annotation doesn't have set the
     * status value so it check if the feature has the status enabled taken from current {@link de.kaubisch.fitchy.FitchyConfig} object.
     *
     * @param annotation given annotation that needs to be checked
     * @return returns true if the annotation matches successful otherwise it returns false
     */
    public boolean isFeatureAvailable(FeatureSwitch annotation) {
        boolean found = false;
        if(annotation != null) {
            FitchyConfig options = Fitchy.getConfig();
            String statusValue = annotation.status();
            if("".equals(statusValue)) {
                statusValue = options.enabled.getSystemName();
            }
            FeatureStatus status = options.statusOf(statusValue);
            found = storage.featureHasStatus(annotation.value(), status);
        }
        return found;
    }
}
