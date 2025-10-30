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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.IRemoteIDEServices.DocumentEditsParameter;
import org.rascalmpl.ideservices.IRemoteIDEServices.LanguageParameter;
import org.rascalmpl.ideservices.IRemoteIDEServices.SourceLocationParameter;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.library.lang.json.internal.JsonValueReader;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class RemoteIDEServices extends BasicIDEServices {
    private IRemoteIDEServices server;

    public RemoteIDEServices(int replInterfacePort, PrintWriter stderr, IRascalMonitor monitor, Terminal terminal, ISourceLocation projectRoot) {
        super(stderr, monitor, terminal, projectRoot);

        try {
            @SuppressWarnings("resource")
            var socket = new Socket(InetAddress.getLoopbackAddress(), replInterfacePort);
            socket.setTcpNoDelay(true);
            Launcher<IRemoteIDEServices> clientLauncher = new Launcher.Builder<IRemoteIDEServices>()
                .setRemoteInterface(IRemoteIDEServices.class)
                .setLocalService(this)
                .setInput(socket.getInputStream())
                .setOutput(socket.getOutputStream())
                .configureGson(RemoteIDEServices::configureGson)
                // .setExecutorService(?)
                .create();

                clientLauncher.startListening();
                server = clientLauncher.getRemoteProxy();
        }
        catch (Throwable e) {
            warning("Error setting up Remote IDE Services connection " + e.getMessage(), URIUtil.rootLocation("unknown"));
        }

    }

    public static void configureGson(GsonBuilder builder) {
        JsonValueWriter writer = new JsonValueWriter();
        JsonValueReader reader = new JsonValueReader(IRascalValueFactory.getInstance(), new TypeStore(), new NullRascalMonitor(), null);
        writer.setDatesAsInt(true);

        builder.registerTypeHierarchyAdapter(IValue.class, new TypeAdapter<IValue>() {
            @Override
            public IValue read(JsonReader source) throws IOException {
                return reader.read(source, TypeFactory.getInstance().valueType());
            }

            @Override
            public void write(JsonWriter target, IValue value) throws IOException {
                writer.write(target, value);
            }
        });
    }

    @Override
    public void edit(ISourceLocation loc) {
        server.edit(new SourceLocationParameter(loc));
    }

    @Override
    public void browse(URI uri, String title, int viewColumn) {
        super.browse(uri, title, viewColumn);
    }

    @Override
    public ISourceLocation resolveProjectLocation(ISourceLocation input) {
        try {
            return server.resolveProjectLocation(new SourceLocationParameter(input)).get().getLocation();
        }
        catch (Throwable e) {
            return input;
        }
    }

    @Override
    public void registerLanguage(IConstructor language) {
        server.registerLanguage(LanguageParameter.fromRascalValue(language));
    }

    @Override
    public void startDebuggingSession(int serverPort) {
        server.startDebuggingSession(serverPort);
    }

    @Override
    public void applyFileSystemEdits(IList edits) {
        server.applyDocumentsEdits(new DocumentEditsParameter(edits));
    }
    
}
