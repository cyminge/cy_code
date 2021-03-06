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
package org.apache.mina.filter.codec.statemachine;

/**
 * {@link DecodingState} which skips space (0x20) and tab (0x09) characters.
 * 
 * @author The Apache MINA Project (dev@mina.apache.org)
 * @version $Rev: 689180 $, $Date: 2008-08-26 21:39:26 +0200 (Tue, 26 Aug 2008) $
 */
public abstract class LinearWhitespaceSkippingState extends SkippingState {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean canSkip(byte b) {
        return b == 32 || b == 9;
    }
}
