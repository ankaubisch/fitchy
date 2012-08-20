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
package com.googlecode.fitchy.sample;

import com.googlecode.fitchy.FeatureStatus;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/9/12
 * Time: 7:40 PM
 */
public enum CustomFeatureStatus implements FeatureStatus {
    ENABLED("an", true),
    HIDDEN("hidden"),
    DISABLED("aus", false);

    private String systemName;
    private boolean enabled, disabled;

    private CustomFeatureStatus(String systemName) {
        this(systemName, false);
        this.disabled = false;
    }

    private CustomFeatureStatus(String systemName, boolean status) {
        this.systemName = systemName;
        this.enabled    = status;
        this.disabled   = !status;
    }

    public String getSystemName() {
        return systemName;
    }

    public boolean isEnabledStatus() {
        return enabled;
    }

    public boolean isDisabledStatus() {
        return disabled;
    }
}
