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

import de.kaubisch.fitchy.exception.AnnotationNotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 6/24/12
 * Time: 3:51 PM
 */
public class AnnotationRetriever {

    private static final Logger LOG = Logger.getLogger(AnnotationRetriever.class.getName());

    private Class<? extends Annotation> anClass;

    private Class<?> originClass;

    public AnnotationRetriever(Class<? extends Annotation> annotationClass) {
        this.anClass = annotationClass;
    }

    public AnnotationRetriever(Class<? extends Annotation> annotationClass, Class<?> originClass) {
        this(annotationClass);
        this.originClass = originClass;
    }

    public <T extends Annotation> T getAnnotation(Method method) throws AnnotationNotFoundException {
        T annotation = (T) method.getAnnotation(anClass);
        if(annotation == null && originClass != null && !method.getDeclaringClass().equals(originClass)) {
            try {
                Method implementedMethod = originClass.getMethod(method.getName(), method.getParameterTypes());
                annotation = (T) implementedMethod.getAnnotation(anClass);

            } catch (NoSuchMethodException e) {
                if(LOG.isLoggable(Level.FINE)) {
                    LOG.fine("method in implementing class not found. " + e.getMessage());
                }
            } catch (SecurityException e) {
                if(LOG.isLoggable(Level.FINE)) {
                    LOG. fine("cannot access method. " + e.getMessage());
                }
            }
        }

        if(annotation == null) {
            throw new AnnotationNotFoundException("unable to find annotation with classname " + anClass.getName());
        }

        return annotation;
    }
}
