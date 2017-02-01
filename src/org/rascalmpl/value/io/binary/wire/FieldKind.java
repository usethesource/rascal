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
package org.rascalmpl.value.io.binary.wire;

public class FieldKind {
    /* only values from 1-7 are valid (3 bits, 0 is taken by message flag) */
    public static final int PREVIOUS_STR = 1;
    public static final int INT = 2;
    public static final int STRING = 3;
    public static final int NESTED = 4;
    public static final int REPEATED = 5;

    @SuppressWarnings("unused")
    private static final int UNUSED1 = 6;
    @SuppressWarnings("unused")
    private static final int UNUSED2 = 7;
   
    public static class Repeated {
        /* values from 0-7 are valid */
        public static final int BYTES = 0; 
        @SuppressWarnings("unused")
        private static final int UNUSED0 = 1;
        public static final int INTS = FieldKind.INT; // 2
        public static final int STRINGS = FieldKind.STRING; // 3
        public static final int NESTEDS = FieldKind.NESTED; // 4

        @SuppressWarnings("unused")
        private static final int UNUSED1 = 5;
        @SuppressWarnings("unused")
        private static final int UNUSED2 = 6;
        @SuppressWarnings("unused")
        private static final int UNUSED3 = 7;
    }
    private FieldKind() {}
}
