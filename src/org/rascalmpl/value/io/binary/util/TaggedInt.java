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

/**
 * Use the 3 lowest bits of an integer to store a tag.
 * @author Davy Landman
 */
public class TaggedInt {
    private TaggedInt() {}

    private static final int TAG_BITS = 3;
    private static final int TAG_MASK = 0b111;
    public static final int MAX_ORIGINAL_VALUE = 0xFFFFFFFF >>> TAG_BITS;
    static {
        assert (1 << (TAG_BITS - 1)) == Integer.highestOneBit(TAG_MASK);
        assert getOriginal(make(MAX_ORIGINAL_VALUE, 3)) == MAX_ORIGINAL_VALUE;
    }
    
    public static int make(final int original, final int tag) {
        assert (tag & TAG_MASK) == tag && original <= MAX_ORIGINAL_VALUE;
        return (original << TAG_BITS) | tag;
    }
    
    public static int getOriginal(final int i) {
        return i >>> TAG_BITS;
    }
    
    public static int getTag(final int i) {
        return i & TAG_MASK;
    }

}
