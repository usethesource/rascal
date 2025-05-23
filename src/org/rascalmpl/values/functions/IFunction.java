/** 
 * Copyright (c) 2020, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWOi - CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.values.functions;

import java.util.Collections;
import java.util.Map;

import org.rascalmpl.debug.IRascalMonitor;

import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IValue;

public interface IFunction extends IExternalValue {
    
    @Override
    default int getMatchFingerprint() {
        return 3154628 /* "func".hashCode() */ + 89 * getType().hashCode();
    }

    /**
     * Invokes the receiver function.
     * 
     * @param parameters are all IValue instances
     * @param keywordParameters provide optional named parameters
     * @return an IValue, always, never null
     * @throws CallFailed exception if the function does not apply to the current parameters
     */
    <T extends IValue> T call(Map<String,IValue> keywordParameters, IValue... parameters);
    
    /**
     * Convenience version of call which offers an empty map for keywordParameters
     * @param  parameters are all IValue instances
     * @return an IValue, always, never null
     * @throws CallFailed exception if the function does not apply to the current parameters
     */
    @SuppressWarnings("unchecked")
    default <T extends IValue> T call(IValue... parameters) {
        return (T) call(Collections.emptyMap(), parameters);
    }
    
    /**
     * Invokes the receiver function in the context of a new instance of IRascalMonitor.
     * This allows clients to send cancellation requests and observe progress through the
     * monitor's callback functions.
     */
    @SuppressWarnings("unchecked")
    default <T extends IValue> T monitoredCall(IRascalMonitor monitor, Map<String, IValue> keywordParameters, IValue... parameters) {
        return (T) call(keywordParameters, parameters);
    }
    
    /**
     * Invokes the receiver function in the context of a new instance of IRascalMonitor.
     * This allows clients to send cancellation requests and observe progress through the
     * monitor's callback functions.
     */
    @SuppressWarnings("unchecked")
    default <T extends IValue> T monitoredCall(IRascalMonitor monitor, IValue... parameters) {
        return (T) monitoredCall(monitor, Collections.emptyMap(), parameters);
    }
}
