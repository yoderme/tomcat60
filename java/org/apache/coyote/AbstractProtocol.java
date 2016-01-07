/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.coyote;

import java.net.InetAddress;
import java.net.URLEncoder;

import org.apache.tomcat.util.net.AbstractEndpoint;

public abstract class AbstractProtocol implements ProtocolHandler {

    protected abstract AbstractEndpoint getEndpoint();

    public int getMaxHeaderCount() {
        return getEndpoint().getMaxHeaderCount();
    }
    public void setMaxHeaderCount(int maxHeaderCount) {
        getEndpoint().setMaxHeaderCount(maxHeaderCount);
    }

    /**
     * An utility method, used to implement getName() in subclasses.
     */
    protected String createName(String prefix, InetAddress address, int port) {
        StringBuilder name = new StringBuilder(prefix);
        name.append('-');
        if (address != null) {
            String strAddr = address.toString();
            if (strAddr.startsWith("/")) {
                strAddr = strAddr.substring(1);
            }
            name.append(URLEncoder.encode(strAddr)).append('-');
        }
        name.append(port);
        return name.toString();
    }
}
