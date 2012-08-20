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
package com.googlecode.fitchy;

/**
 * A Feature representation that contains a name
 * and a {@link FeatureStatus}.
 */
public class Feature {
	private String name;
	private FeatureStatus status;
	
	public Feature(String name, FeatureStatus status) {
		this.name   = name;
		this.status = status;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FeatureStatus getStatus() {
        return status;
    }

    public void setStatus(FeatureStatus status) {
        this.status = status;
    }
}
