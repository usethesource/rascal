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
package org.rascalmpl.repl.http;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.function.Function;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

import io.usethesource.vallang.IValue;

public class REPLContentServerManager {
    private static final int MAX_SERVER_COUNT = 125;
    private static final long HALFHOUR = 30 * 60 * 1000;
    private final Cache<String, REPLContentServer> servers 
    = Caffeine.newBuilder()
      .maximumSize(MAX_SERVER_COUNT)
      .removalListener(REPLContentServerManager::cleanupServer)
      .build();
    
    private static void cleanupServer(String contentId, REPLContentServer server, RemovalCause cause) {
        server.stop();
    }
    
    public REPLContentServer addServer(String id, Function<IValue, IValue> target) throws IOException {
        try {
            REPLContentServer oldServer = servers.get(id, (i) -> null);
            
            if (oldServer != null) {
                oldServer.updateCallback(target);
                return oldServer;
            }
            else {
                REPLContentServer newServer = startContentServer(target);
                servers.put(id, newServer);
                return newServer;
            }
        }
        finally {
            collectDeadServers();
        }
    }
    
    private void collectDeadServers() {
        long now = System.currentTimeMillis();
        
        for (Entry<String,REPLContentServer> entry : servers.asMap().entrySet()) {
            REPLContentServer server = entry.getValue();
            String contentId = entry.getKey();
            
            if (server.getLastServedAt() < now - HALFHOUR) {
                servers.invalidate(contentId);
            }
        }
    }

    private REPLContentServer startContentServer(Function<IValue, IValue> target) throws IOException {
        REPLContentServer server = null;

        for(int port = 9050; port < 9050+MAX_SERVER_COUNT; port++){
            try {
                server = new REPLContentServer(port, target);
                server.start();
                // success
                break;
            } catch (IOException e) {
                // failure is expected if the port is taken
                continue;
            }
        }

        if (server == null) {
            throw new IOException("Could not find port to run single page server on");
        }

        return server;
    }
}
