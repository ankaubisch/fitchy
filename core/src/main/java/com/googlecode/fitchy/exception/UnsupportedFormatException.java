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
package com.googlecode.fitchy.exception;

/**
 * Exception that is thrown if a {@link com.googlecode.fitchy.FeatureReader} cannot read {@link java.io.InputStream}
 * format.
 * 
 * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
 */
public class UnsupportedFormatException extends RuntimeException {

	public UnsupportedFormatException() {
		super();
	}

	public UnsupportedFormatException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnsupportedFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedFormatException(String message) {
		super(message);
	}

	public UnsupportedFormatException(Throwable cause) {
		super(cause);
	}

}
