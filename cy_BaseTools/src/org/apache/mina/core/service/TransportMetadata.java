/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.mina.core.service;

import java.net.SocketAddress;
import java.util.Set;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;

/**
 * Provides meta-information that describes an {@link IoService}.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 * @version $Rev: 671827 $, $Date: 2008-06-26 10:49:48 +0200 (Thu, 26 Jun 2008) $
 */
public interface TransportMetadata {
    
    /**
     * Returns the name of the service provider (e.g. "nio", "apr" and "rxtx").
     */
    String getProviderName();

    /**
     * Returns the name of the service.
     */
    String getName();

    /**
     * Returns <code>true</code> if the session of this transport type is
     * <a href="http://en.wikipedia.org/wiki/Connectionless">connectionless</a>.
     */
    boolean isConnectionless();

    /**
     * Returns {@code true} if the messages exchanged by the service can be
     * <a href="http://en.wikipedia.org/wiki/IPv4#Fragmentation_and_reassembly">fragmented
     * or reassembled</a> by its underlying transport.
     */
    boolean hasFragmentation();

    /**
     * Returns the address type of the service.
     */
    Class<? extends SocketAddress> getAddressType();

    /**
     * Returns the set of the allowed message type when you write to an
     * {@link IoSession} that is managed by the service.
     */
    Set<Class<? extends Object>> getEnvelopeTypes();

    /**
     * Returns the type of the {@link IoSessionConfig} of the service
     */
    Class<? extends IoSessionConfig> getSessionConfigType();
}
