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
package de.kaubisch.fitchy.exception;

/**
 * Exception that is thrown by the system if the passed object cannot be observed by
 * a {@link de.kaubisch.fitchy.FeatureObserver}.
 *
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 6/24/12
 * Time: 4:54 PM
 */
public class UnableToObserveException extends RuntimeException {
    public UnableToObserveException() {
    }

    public UnableToObserveException(String message) {
        super(message);
    }

    public UnableToObserveException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToObserveException(Throwable cause) {
        super(cause);
    }

    public UnableToObserveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
