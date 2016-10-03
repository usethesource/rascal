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

public class LinearCircularLookupWindow<T> implements TrackLastRead<T>, TrackLastWritten<T> {
    private final T[] data;
    private long written;

    @SuppressWarnings("unchecked")
    public LinearCircularLookupWindow(int size) {
        data = (T[]) new Object[size];
        written = 0;
    }
    
    @Override
    public int howLongAgo(T obj) {
        long stop = Math.max(0, written - data.length);
        for (long ix = written -1; ix >= stop; ix--) {
            if (data[translate(ix)] == obj) {
                return ((int)(written - ix) - 1);
            }
        }
        return -1;
    }
    
    private int translate(long index) {
        return (int) (index % data.length);
    }
    
    
    @Override
    public T lookBack(int offset) {
        assert offset + 1 <= written;
        return data[translate(written - (offset + 1))];
        
    }
    
    @Override
    public void read(T obj) {
        add(obj);
    }
    @Override
    public void write(T obj) {
        add(obj);
    }
    protected T add(T obj) {
        int newIndex = translate(written++);
        T old = data[newIndex];
        data[newIndex] = obj;
        return old;
    }

}
