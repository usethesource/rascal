/*******************************************************************************
 * Copyright (c) 2014-2026 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.rascalmpl.library.util;

import java.util.HashMap;
import java.util.Map;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.TypeStore;

public class Webserver {
    private final Map<IInteger, Undertow> servers;
    private IRascalValueFactory vf;
    private IRascalMonitor monitor;
    
    public Webserver(IRascalValueFactory vf, IRascalMonitor monitor) {
        this.vf = vf;
        this.monitor = monitor;
        this.servers = new HashMap<>();
    }

    /**
     * Serves on localhost:<port> and allows the user to stop the running server
     * later with `shutdown`
     */
    public void serve(IInteger pPort, final IFunction callback, IBool asDeamon) {
        final int port = pPort.intValue();
        HttpHandler handler = new WebHandler(callback, port, vf, monitor);

        Undertow server = Undertow.builder()
            .addHttpListener(port, "127.0.0.1")
            .setHandler(handler)
            .build();

        servers.put(pPort, server);
        server.start();
    }
    
    public void shutdown(IInteger port) {
        Undertow server = servers.get(port);
        if (server != null) {
            server.stop();
            servers.remove(port);
        }
        else {
            monitor.warning("Server at port " + port + " was not running anymore", URIUtil.correctLocation("http", "localhost:" + port, ""));
        }
    }

    @Override
    protected void finalize() throws Throwable {
        for (Undertow server : servers.values()) {
            server.stop();
        }
    }

    /**
     * This exercises the entire server infrastructure without locking the interpreter
     */
    public void startEchoServerJava(IInteger port, IValue htmlStore) {
        HttpHandler handler = new WebHandler(port.intValue(), vf, (IConstructor) htmlStore, monitor);

        Undertow server = Undertow.builder()
            .addHttpListener(port.intValue(), "127.0.0.1")
            .setHandler(handler)
            .build();

        servers.put(port, server);
        server.start();
    }
}
