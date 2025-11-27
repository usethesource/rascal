/** 
 * Copyright (c) 2018, Jurgen J. Vinju, Centrum Wiskunde & Informatica (CWI) 
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

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import com.sun.management.HotSpotDiagnosticMXBean;

/*
 * This code is inspired from the following blog:
 *    https://blogs.oracle.com/sundararajan/programmatically-dumping-heap-from-java-applications
 * 
 * Use it to dump the heap to a file for further analysis.
 */
public class HeapDumper {
    private static final class InstanceKeeper {
        /*package*/ 
        static final HotSpotDiagnosticMXBean hotspotMBean = getHotspotMBean();
        
        private static HotSpotDiagnosticMXBean getHotspotMBean() {
            try {
                MBeanServer server = ManagementFactory.getPlatformMBeanServer();
                HotSpotDiagnosticMXBean bean = 
                    ManagementFactory.newPlatformMXBeanProxy(server,
                        "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
                return bean;
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception exp) {
                throw new RuntimeException(exp);
            }
        }
    }
    
    private static HotSpotDiagnosticMXBean getBean() {
        return InstanceKeeper.hotspotMBean;
    }
    
    public static void dumpHeap(String fileName, boolean live) throws IOException {
        getBean().dumpHeap(fileName, live);
    }
}