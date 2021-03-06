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
package com.googlecode.fitchy.internal;


import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.googlecode.fitchy.Configuration;
import com.googlecode.fitchy.Feature;
import com.googlecode.fitchy.FeatureReader;
import com.googlecode.fitchy.exception.UnsupportedFormatException;

/**
 * Implementation of {@link FeatureReader} that can read streams from 
 * a properties file or resource.
 * 
 * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
 */
public class PropertyFeatureReader extends FeatureReader {

	private Properties rawEntryData;
	
	private Enumeration<Object> keyEnum;

	/**
	 * constructor of super class {@link FeatureReader}
	 * 
	 * @param is {@link InputStream} stream of a feature source
     * @param options {@link com.googlecode.fitchy.Configuration} current options that the reader can use to
     *                                     determine which {@link com.googlecode.fitchy.FeatureStatus}
     *                                     a {@link Feature} has.
	 */
	public PropertyFeatureReader(InputStream is, Configuration options) {
		super(is, options);
		try {
			rawEntryData = new Properties();
			rawEntryData.load(is);
			keyEnum = rawEntryData.keys();
		} catch (IOException e) {
            //do nothing. exception will be thrown at function read()
		} catch (NullPointerException e) {
            //do nothing. exception will be thrown at function read()
        }
		
	}

	/* (non-Javadoc)
	 * @see com.googlecode.fitchy.FeatureReader#read()
	 */
	@Override
	public Feature read() {
        if(keyEnum == null || rawEntryData == null) {
            throw new UnsupportedFormatException("unable from InputStream");
        }

		Feature next = null;
		if(keyEnum.hasMoreElements()) {
			Object key = keyEnum.nextElement();
			if(key instanceof String) {
				String value = rawEntryData.getProperty((String)key, options.disabledStatus.getSystemName());
				next = new Feature((String)key, options.statusOf(value));
			}
		}
		return next;
	}
}
