/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.dap.breakpoint;

import io.usethesource.vallang.ISourceLocation;
import org.eclipse.lsp4j.debug.Source;
import org.rascalmpl.debug.DebugHandler;
import org.rascalmpl.debug.DebugMessageFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
    Store breakpoints set by user
 **/

public class BreakpointsCollection {
    private final Map<String, Map<ISourceLocation, BreakpointInfo>> breakpoints;
    private final DebugHandler debugHandler;
    private final AtomicInteger breakpointIDCounter = new AtomicInteger(0);

    public BreakpointsCollection(DebugHandler debugHandler){
        this.breakpoints = new ConcurrentHashMap<>();
        this.debugHandler = debugHandler;
    }

    public void clearBreakpointsOfFile(String filePath){
        Map<ISourceLocation, BreakpointInfo> fileBreakpoints = breakpoints.get(filePath);
        if(fileBreakpoints == null){
            return;
        }
        for (ISourceLocation breakpointLocation : fileBreakpoints.keySet()) {
            debugHandler.processMessage(DebugMessageFactory.requestDeleteBreakpoint(breakpointLocation));
        }
        breakpoints.get(filePath).clear();
    }

    public void addBreakpoint(ISourceLocation location, Source source){
        String path = location.getPath();
        BreakpointInfo breakpoint = new BreakpointInfo(breakpointIDCounter.incrementAndGet(), source);
        breakpoints.computeIfAbsent(path, k -> new HashMap<>()).put(location, breakpoint);
        debugHandler.processMessage(DebugMessageFactory.requestSetBreakpoint(location));
    }

    public void addBreakpoint(ISourceLocation location, Source source, String condition){
        String path = location.getPath();
        BreakpointInfo breakpoint = new BreakpointInfo(breakpointIDCounter.incrementAndGet(), source);
        breakpoints.computeIfAbsent(path, k -> new HashMap<>()).put(location, breakpoint);
        debugHandler.processMessage(DebugMessageFactory.requestSetConditionalBreakpoint(location, condition));
    }

    public int getBreakpointID(ISourceLocation location){
        String path = location.getPath();
        Map<ISourceLocation, BreakpointInfo> fileBreakpoints = breakpoints.get(path);
        if(fileBreakpoints == null){
            return -1;
        }
        BreakpointInfo breakpointInfo = fileBreakpoints.get(location);
        if(breakpointInfo == null){
            return -1;
        }
        return breakpointInfo.getId();
    }
}
