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
package de.kaubisch.fitchy.options;

import de.kaubisch.fitchy.options.FitchyOptions;

import java.io.Serializable;

/**
 * Abstract class that provides function to set and get {@link FitchyOptions} to an Object.
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/3/12
 * Time: 9:35 PM
 */
public abstract class HasFitchyConfig implements Serializable {

    protected FitchyOptions options;

    /**
     * Initializes current object with an option
     *
     * @param options {@link FitchyOptions} current option
     */
    public HasFitchyConfig(FitchyOptions options) {
        this.options = options;
    }

    /**
     * Returns current {@link FitchyOptions}
     * @return {@link FitchyOptions}
     */
    public FitchyOptions getConfig() {
        return options;
    }

    /**
     * Sets new {@link FitchyOptions} to object. If passed config
     * argument is null the old config will not be overridden.
     * @param options {@link FitchyOptions} new config
     */
    public void setConfig(FitchyOptions options) {
        this.options = options;
    }
}
