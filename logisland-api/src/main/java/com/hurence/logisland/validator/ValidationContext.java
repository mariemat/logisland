/**
 * Copyright (C) 2016 Hurence (bailet.thomas@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hurence.logisland.validator;

import com.hurence.logisland.component.PropertyDescriptor;
import com.hurence.logisland.component.PropertyValue;
import com.hurence.logisland.controller.ControllerService;
import com.hurence.logisland.controller.ControllerServiceLookup;

import java.util.Map;






public interface ValidationContext {



    /**
     * @return the {@link ControllerServiceLookup} which can be used to obtain
     * Controller Services
     */
    ControllerServiceLookup getControllerServiceLookup();

    /**
     * @param controllerService to lookup the validation context of
     * @return a ValidationContext that is appropriate for validating the given
     * {@link ControllerService}
     */
    ValidationContext getControllerServiceValidationContext(ControllerService controllerService);

    /**
     * @param property being validated
     * @return a PropertyValue that encapsulates the value configured for the
     * given PropertyDescriptor
     */
    PropertyValue getPropertyValue(PropertyDescriptor property);

    /**
     * @param value to make a PropertyValue object for
     * @return a PropertyValue that represents the given value
     */
    PropertyValue newPropertyValue(String value);

    /**
     * @return a Map of all configured Properties
     */
    Map<PropertyDescriptor, String> getProperties();


    /**
     * @param value to test whether expression language is present
     * @return <code>true</code> if the given value contains a NiFi Expression
     * Language expression, <code>false</code> if it does not
     */
    boolean isExpressionLanguagePresent(String value);

    /**
     * @param propertyName to test whether expression language is supported
     * @return <code>true</code> if the property with the given name supports
     * the NiFi Expression Language, <code>false</code> if the property does not
     * support the Expression Language or is not a valid property name
     */
    boolean isExpressionLanguageSupported(String propertyName);

}
