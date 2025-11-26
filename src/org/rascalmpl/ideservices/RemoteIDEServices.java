/*
 * Copyright (c) 2015-2025, NWO-I CWI and Swat.engineering
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
package org.rascalmpl.ideservices;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.IRemoteIDEServices.DocumentEditsParameter;
import org.rascalmpl.ideservices.IRemoteIDEServices.RegisterDiagnosticsParameters;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;

/**
 * This class enables interaction with an implementation of `IDEServices` that (potentially) runs in another thread or process
 */
public class RemoteIDEServices extends BasicIDEServices {
    private final IRemoteIDEServices server;

    public RemoteIDEServices(int ideServicesPort, PrintWriter stderr, IRascalMonitor monitor, Terminal terminal, ISourceLocation projectRoot) {
        super(stderr, monitor, terminal, projectRoot);

        try {
            @SuppressWarnings("resource")
            var socket = new Socket(InetAddress.getLoopbackAddress(), ideServicesPort);
            socket.setTcpNoDelay(true);
            Launcher<IRemoteIDEServices> clientLauncher = new Launcher.Builder<IRemoteIDEServices>()
                .setRemoteInterface(IRemoteIDEServices.class)
                .setLocalService(this)
                .setInput(socket.getInputStream())
                .setOutput(socket.getOutputStream())
                .configureGson(GsonUtils::configureGson)
                .create();

                clientLauncher.startListening();
                server = clientLauncher.getRemoteProxy();
        } catch (Throwable e) {
            throw new RuntimeException("Error setting up Remote IDE Services connection", e);
        }
    }

    @Override
    public void edit(ISourceLocation loc, int viewColumn) {
        server.edit(loc, viewColumn);
    }

    @Override
    public void browse(URI uri, IString title, IInteger viewColumn) {
        server.browse(uri, title, viewColumn);
    }

    @Override
    public ISourceLocation resolveProjectLocation(ISourceLocation input) {
        try {
            return server.resolveProjectLocation(input).get(1, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            warning("Error resolving project location", URIUtil.unknownLocation());
        } catch (Throwable e) {}
        return input;
    }

    @Override
    public void startDebuggingSession(int serverPort) {
        server.startDebuggingSession(serverPort);
    }

    @Override
    public void applyFileSystemEdits(IList edits) {
        server.applyDocumentsEdits(new DocumentEditsParameter(edits));
    }

    @Override
    public void registerDiagnostics(IList messages, ISourceLocation projectRoot) {
        server.registerDiagnostics(new RegisterDiagnosticsParameters(messages));
    }
    
    @Override
    public void unregisterDiagnostics(IList resources) {
        server.unregisterDiagnostics(resources.stream().map(ISourceLocation.class::cast).toArray(ISourceLocation[]::new));
    }

    @Override
    public void registerLocations(IString scheme, IString auth, IMap map) {
        // The mappings should be registered both in the REPL itself as well as in the IDE
        super.registerLocations(scheme, auth, map);
        server.registerLocations(scheme, auth, IRemoteIDEServices.mapLocLocToLocArray(map));
    }
}
