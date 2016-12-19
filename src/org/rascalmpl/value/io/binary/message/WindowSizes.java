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
package org.rascalmpl.value.io.binary.message;

import org.rascalmpl.value.IValue;

public class WindowSizes {
    public final int uriWindow;
    public final int typeWindow;
    public final int valueWindow;
    public final int stringsWindow;

    /**
     * @param typeWindowSize the size of the window for type-reuse. normally 1024 should be enough, when storing parse trees, use a larger number (10_000 for example)
     * @param valueWindowSize the size of the window for value-reuse. normally 100_000 should be enough, when expecting large values, you can use a larger number
     * @param uriWindowSize the size of the window for source location reuse. normally 50_000 should be more than enough, when you expect a lot of source locations, increase this number
     */
    public WindowSizes(int valueWindow, int uriWindow, int typeWindow, int stringsWindow) {
        this.stringsWindow = stringsWindow;
        this.typeWindow = typeWindow;
        this.uriWindow = uriWindow;
        this.valueWindow = valueWindow;
    }
    public static final WindowSizes NO_WINDOW = new WindowSizes(0, 0, 0, 0);
    public static final WindowSizes TINY_WINDOW = new WindowSizes(500, 200, 100, 500);
    public static final WindowSizes SMALL_WINDOW = new WindowSizes(5_000, 1_000, 800, 1_000);
    public static final WindowSizes NORMAL_WINDOW = new WindowSizes(100_000, 40_000, 5_000, 10_000);

    private static final int SMALL_SIZE = 512;
    private static final int NORMAL_SIZE = 8*1024;
    public static WindowSizes estimateWindowSize(IValue value) {
        int estimatedSize = IValueSizeEstimator.estimateIValueSize(value, NORMAL_SIZE);
        if (estimatedSize < SMALL_SIZE) {
            return TINY_WINDOW;
        }
        else if (estimatedSize < NORMAL_SIZE) {
            return SMALL_WINDOW;
        }
        else {
            return NORMAL_WINDOW;
        }
    }
}