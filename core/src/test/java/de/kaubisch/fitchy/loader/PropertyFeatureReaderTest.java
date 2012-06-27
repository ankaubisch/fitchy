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

import de.kaubisch.fitchy.Feature;
import de.kaubisch.fitchy.exception.UnsupportedFormatException;
import de.kaubisch.fitchy.options.FitchyOptions;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 6/27/12
 * Time: 7:33 PM
 */
public class PropertyFeatureReaderTest {

    private FitchyOptions options;
    private InputStream is;

    @Before
    public void setUp() {
        options = FitchyOptions.getDefault();
    }

    @Test
    public void read_readFirstFeature_ReturnTestFeatures() throws Exception {
        PropertyFeatureReader reader = createReaderFromInput("test.feature=on");
        Feature feature = reader.read();
        assertThat(feature.name, Is.is("test.feature"));
    }

    @Test
    public void read_readEmptyStream_ReturnNull() {
        PropertyFeatureReader reader = createReaderFromInput("");
        assertThat(reader.read(), IsNull.nullValue());
    }

    @Test(expected=UnsupportedFormatException.class)
    public void read_readNullStream_ThrowException() {
        PropertyFeatureReader reader = createReaderFromInput(null);
        reader.read();
    }

    @Test
    public void close_CloseInputStream_InputStreamClosed() throws IOException {
        PropertyFeatureReader reader = createReaderFromInput("    ");
        reader.close();
        assertThat(is.available(), Is.is(0));
    }

    private PropertyFeatureReader createReaderFromInput(String input) {
        if(input != null) {
            is = new ByteArrayInputStream(input.getBytes());
        }
        PropertyFeatureReader reader = new PropertyFeatureReader(is, options);

        return reader;
    }
}
