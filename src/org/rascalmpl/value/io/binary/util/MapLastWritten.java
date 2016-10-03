/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.value.io.binary.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapLastWritten<T> implements TrackLastWritten<T> {
    public static class IdentityWrapper<T> {
        private final T wrapped;
        private final int hashCode;
        public IdentityWrapper(T wrapped) {
            this.wrapped = wrapped;
            this.hashCode = System.identityHashCode(wrapped);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            return ((IdentityWrapper<T>)obj).wrapped == wrapped;
        }
    }
    private final Map<IdentityWrapper<T>, Long> lookupData;
    private long written;

    @SuppressWarnings("serial")
    public MapLastWritten(final int size) {
        lookupData = new LinkedHashMap<IdentityWrapper<T>, Long>(size + 1, 1.0f, false) {
            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<IdentityWrapper<T>, Long> eldest) {
                return super.size() > size;
            }
        };
        written = 0;
    }
    
    @Override
    public int howLongAgo(T obj) {
        Long writtenAt = lookupData.get(new IdentityWrapper<>(obj));
        if (writtenAt != null) {
            return (int) ((written - writtenAt) - 1);
        }
        return -1;
    }
    
    @Override
    public void write(T obj) {
        lookupData.put(new IdentityWrapper<>(obj), written++);
    }


}
