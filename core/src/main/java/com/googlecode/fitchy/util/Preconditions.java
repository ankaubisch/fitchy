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
package com.googlecode.fitchy.util;

/**
 * Utility class that provide methods that checks several conditions.
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 7/17/12
 * Time: 8:49 PM
 */
public final class Preconditions {

    /**
     * hide constructor to prevent instantiation
     */
    private Preconditions() {

    }

    /**
     * Checks whether passed object is null or not. If object is null this static method
     * will throw an {@link IllegalArgumentException} with a given {@param message}
     * @param obj object that needs to be checked
     * @param message message of exception
     * @throws IllegalArgumentException exception will be thrown if object is null
     */
    public static void throwIllegalArgumentExceptionIfNull(Object obj, String message) {
        throwIllegalArgumentExceptionIfFalse(obj != null, message);
    }

    /**
     * Checks whether condition is false and throw an {@link IllegalArgumentException}.
     *
     * @param conditionPass indicates whether a condition is occurred
     * @param message the message a thrown {@link IllegalArgumentException}
     */
    public static void throwIllegalArgumentExceptionIfFalse(boolean conditionPass, String message) {
        if(!conditionPass) {
            throw new IllegalArgumentException(message);
        }
    }

}
