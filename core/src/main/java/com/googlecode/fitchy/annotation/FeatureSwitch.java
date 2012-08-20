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
package com.googlecode.fitchy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface FeatureSwitch {

    /**
     * Represents the name of the feature. This value is required when using this annotation.
     *
     * @return the name pf the feature
     */
    String value();

    /**
     * Represents the system name of a feature status. Default value is an empty String
     * so the system knows to look that the status of the feature return true when calling
     * {@link com.googlecode.fitchy.FeatureStatus#isEnabledStatus()}
     *
     * @return the {@link com.googlecode.fitchy.FeatureStatus#getSystemName()} value or an empty String by default
     */
    String status() default "";
}
