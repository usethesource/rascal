/** 
 * Copyright (c) 2020-2025, NWO-I CWI and Swat.engineering
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

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.library.util.WebHandler;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

import io.undertow.Undertow;
import io.usethesource.vallang.IValue;

/*
 * This manages the lifecycle of HTTP servers that follow the Response(Request) contract
 * from the Content module, but aren't managed explicitly through util::Webserver. 
 * This is used in the REPL, from IDEServices and the tutor to provide access to visual
 * content without having to manage the lifecycle of the underlying web server, or having
 * to think about port numbers. 
 */
public class REPLContentServerManager {
    private static final int MAX_SERVER_COUNT = 125;
    private static final long HALFHOUR = 30 * 60 * 1000;
    private final Cache<String, REPLContentServer> servers 
    = Caffeine.newBuilder()
      .maximumSize(MAX_SERVER_COUNT)
      .removalListener(REPLContentServerManager::cleanupServer)
      .build();
      
    private final IRascalMonitor monitor;
    private final IRascalValueFactory vf;
    
    public REPLContentServerManager(IRascalValueFactory vf, IRascalMonitor monitor) {
        this.monitor = monitor;
        this.vf = vf;
    }
    private static void cleanupServer(String contentId, REPLContentServer server, RemovalCause cause) {
        server.server.stop();
    }
    
    public REPLContentServer addServer(String id, Function<IValue, IValue> handler) throws IOException {
        try {
            REPLContentServer oldServer = servers.get(id, (i) -> null);
            
            if (oldServer != null) {
                oldServer.handler.updateCallback(handler);
                return oldServer;
            }
            else {
                REPLContentServer newServer = startContentServer(handler);
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
            
            if (server.handler.getLastServedAt() < now - HALFHOUR) {
                servers.invalidate(contentId);
            }
        }
    }

    private REPLContentServer startContentServer(Function<IValue, IValue> target) throws IOException {
        REPLContentServer cs = null;

        for(int port = 9050; port < 9050+MAX_SERVER_COUNT; port++){
            try {
                var wh = new WebHandler(target, port, vf, monitor);
                 
                Undertow server = Undertow.builder()
                    .addHttpListener(port, "127.0.0.1")
                    .setHandler(wh)
                    .build();

                server.start();

                cs = new REPLContentServer(port, wh, server);
                // success
                break;
            } catch (Throwable e) {
                // assuming this is a BindException wrapped in some RuntimeException
                // we try the next port number
                monitor.warning(e.getMessage(), URIUtil.rootLocation("dunno"));
                continue;
            }
        }

        if (cs == null) {
            throw new IOException("Could not find port to run single page server on");
        }

        return cs;
    }

    /**
     * Wrapper for a running server and the handler it is using.
     * Also a port number for debugging purposes.
     */
    public static class REPLContentServer {
        public final WebHandler handler;
        public final Undertow server;
        public final int port;

        REPLContentServer(int port, WebHandler handler, Undertow server) {
            this.port = port;
            this.handler = handler;
            this.server = server;
        }

        public WebHandler getHandler() {
            return handler;
        }

        public Undertow getServer() {
            return server;
        }

        public int getPort() {
            return port;
        }
    }
}
