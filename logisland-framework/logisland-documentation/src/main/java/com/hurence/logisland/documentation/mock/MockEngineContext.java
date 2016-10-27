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
package com.hurence.logisland.documentation.mock;

import com.hurence.logisland.component.PropertyDescriptor;
import com.hurence.logisland.component.PropertyValue;
import com.hurence.logisland.engine.EngineContext;
import com.hurence.logisland.processor.ProcessContext;
import com.hurence.logisland.processor.chain.StandardProcessorChainInstance;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class MockEngineContext implements EngineContext {

    @Override
    public PropertyValue getProperty(PropertyDescriptor descriptor) {
        return null;
    }

    @Override
    public PropertyValue getProperty(String propertyName) {
        return null;
    }

    @Override
    public PropertyValue newPropertyValue(String rawValue) {
        return null;
    }




    @Override
    public Map<PropertyDescriptor, String> getProperties() {
        return Collections.emptyMap();
    }



    @Override
    public String getName() {
        return null;
    }

    @Override
    public Collection<StandardProcessorChainInstance> getProcessorChainInstances() {
        return null;
    }
}