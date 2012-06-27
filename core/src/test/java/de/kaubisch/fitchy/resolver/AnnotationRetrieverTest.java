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

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 6/24/12
 * Time: 4:01 PM
 */
public class AnnotationRetrieverTest {

    private AnnotationRetriever retriever;

    @Before
    public void setUp() throws Exception {
        retriever = new AnnotationRetriever(Test.class);
    }

    @Test(expected = AnnotationNotFoundException.class)
    public void getAnnotation_WithoutAnnotation_ThrowsException() throws Exception {
        retriever.getAnnotation(this.getClass().getMethod("setUp", new Class<?>[] {}));
    }

    @Test(timeout = 500)
    public void getAnnotation_WithAnnotation_ReturnAnnotation() throws Exception {
        Test testAnnotation = retriever.getAnnotation(this.getClass().getMethod("testGetAnnotationSuccessful", new Class<?>[] {}));
        assertThat(testAnnotation.timeout(), is(500l));
    }
}
