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

public class ArrayUtil {

    // source: http://stackoverflow.com/a/25508988/11098
    // based on: "Java server performance: A case study of building efficient, scalable Jvms" by R. Dimpsey, R. Arora, K. Kuiper.
    // in general, starts at the beginnning and then grows exponentially in the System.arrayCopy size chunks.
    // it is faster due to the memcopy that will be used underneath.
    // it taks log2(data.length) calls to arrayCopy instead of a data.length loop in java
    public static <T> void fill(T[] array, T value) {
        int len = array.length;

        if (len > 0){
          array[0] = value;
        }

        for (int i = 1; i < len; i += i) {
          System.arraycopy(array, 0, array, i, ((len - i) < i) ? (len - i) : i);
        }
    }

    public static <T> void fill(int[] array, int value) {
        int len = array.length;

        if (len > 0){
          array[0] = value;
        }

        for (int i = 1; i < len; i += i) {
          System.arraycopy(array, 0, array, i, ((len - i) < i) ? (len - i) : i);
        }
    }

}
