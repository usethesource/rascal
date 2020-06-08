/** 
 * Copyright (c) 2020, Davy Landman, swat.engineering 
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
package org.rascalmpl.util;

import java.util.concurrent.TimeUnit;

/**
 * A monotonic ticker that fast to frequently access and is updated roughly once every second<br/>
 * <br/>
 * For more reasons why we want a fast time field, see <a href="http://pzemtsov.github.io/2017/07/23/the-slow-currenttimemillis.html">this discussion</a>.
 */
public class SecondsTicker {

    private static volatile int tick = 0;
    
    static {

        Thread t = new Thread(() -> {
            try {
                final long tickRate = TimeUnit.SECONDS.toNanos(1);
                long nextTick = System.nanoTime() + tickRate;
                while (true) {
                    // sleep in "small" increments towards the next tick to avoid thread starvation skewing the ticks too much
                    while (System.nanoTime() < nextTick) {
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                    nextTick += tickRate;
                    tick++; // safe enough, since we are the only thread writing to the field
                }
            }
            catch (InterruptedException e) {
                return;
            }
        });
        t.setName("SecondsTicker");
        t.setDaemon(true);
        t.start();
    }
    
    /**
     * Get a monotonic increasing integer that increments roughly every second
     */
    public static int current() {
        return tick;
    }

}
