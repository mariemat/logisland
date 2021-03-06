/**
 * Copyright 2015 Confluent Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.hurence.logisland.kafka.store;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 *
 */
@JsonPropertyOrder(value = {"keytype", "magic"})
public class NoopKey extends RegistryKey {
    private static final int MAGIC_BYTE = 0;

    public NoopKey() {
        super(RegistryKeyType.NOOP);
        this.magicByte = MAGIC_BYTE;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{magic=" + this.magicByte + ",");
        sb.append("keytype=" + this.keyType.keyType + "}");
        return sb.toString();
    }
}
