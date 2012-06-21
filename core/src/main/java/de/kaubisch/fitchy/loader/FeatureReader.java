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
package de.kaubisch.fitchy.loader;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import de.kaubisch.fitchy.Feature;
import de.kaubisch.fitchy.exception.UnsupportedFormatException;

/**
 * An abstract class thats provides functions to read
 * features from an {@link InputStream}.
 * 
 * @author andreas.kaubisch@gmail.com
 */
public abstract class FeatureReader implements Closeable {
	
	private InputStream is;
	
	/**
	 * Constructor that needs an open {@link InputStream} instance.
	 * 
	 * @param is {@link InputStream} source of a source that contains features
	 */
	public FeatureReader(InputStream is) {
		this.is = is;
	}
	
	/**
	 * Reads current {@link Feature} element from {@link InputStream} and returns it
	 * or return null if no further element exists. A call of this function increments
	 * the internal pointer so that another call of this method would return the
	 * next {@link Feature} element.
	 * 
	 * @return {@link Feature} current Feature element
	 * @throws UnsupportedFormatException is thrown if reader doesn't understand stream format
	 */
	public abstract Feature read() throws UnsupportedFormatException;

	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		this.is.close();
	}
}
