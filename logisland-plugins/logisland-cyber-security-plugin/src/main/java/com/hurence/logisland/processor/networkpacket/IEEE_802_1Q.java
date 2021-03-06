/**
 * Copyright (C) 2017 Hurence
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
 */

/** This code is adapted from https://github.com/apache/incubator-metron/blob/master/metron-platform/metron-pcap/src/main/java/org/apache/metron/pcap/IEEE_802_1Q.java */


package com.hurence.logisland.processor.networkpacket;

public class IEEE_802_1Q {

	  int priorityCodePoint = 0;
	  int dropEligibleIndicator = 0;
	  int vLANIdentifier = 0;

	  public IEEE_802_1Q(int priorityCodePoint, int dropEligibleIndicator,
                         int vLANIdentifier) {
	    this.priorityCodePoint = priorityCodePoint;
	    this.dropEligibleIndicator = dropEligibleIndicator;
	    this.vLANIdentifier = vLANIdentifier;
	  }

	  public int getPriorityCodePoint() {
	    return priorityCodePoint;
	  }

	  public int getDropEligibleIndicator() {
	    return dropEligibleIndicator;
	  }

	  public int getvLANIdentifier() {
	    return vLANIdentifier;
	  }
	}
