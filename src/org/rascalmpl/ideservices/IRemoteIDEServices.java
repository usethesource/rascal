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

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.type.TypeStore;

/**
 * This interface exposes functionality from `IDEServices` over IPC.
 * It is a subset of `IDEServices`, as not all of its functionality lends
 * itself to be run remotely in a nice way.
 */
public interface IRemoteIDEServices {

    @JsonRequest
    CompletableFuture<Void> edit(ISourceLocation param);

    @JsonRequest
    CompletableFuture<Void> browse(URI uri, IString title, IInteger viewColumn);

    @JsonRequest
    CompletableFuture<ISourceLocation> resolveProjectLocation(ISourceLocation param);

    @JsonRequest
    CompletableFuture<Void> applyDocumentsEdits(DocumentEditsParameter edits);

    @JsonRequest
    CompletableFuture<Void> registerLocations(RegisterLocationsParameters param);

    @JsonRequest
    CompletableFuture<Void> registerDiagnostics(RegisterDiagnosticsParameters param);

    @JsonRequest
    CompletableFuture<Void> unregisterDiagnostics(ISourceLocation[] locs);

    @JsonRequest
    CompletableFuture<Void> startDebuggingSession(int serverPort);

    @JsonRequest
    CompletableFuture<Void> registerDebugServerPort(int processID, int serverPort);

    public static class RegisterDiagnosticsParameters {
        private String messages;

        public RegisterDiagnosticsParameters(IList messages) {
            this.messages = GsonUtils.base64Encode(messages);
        }

        public IList getMessages() {
            return GsonUtils.base64Decode(messages, new TypeStore());
        }
    }

    public static class DocumentEditsParameter {

        private String edits;

        public DocumentEditsParameter(IList edits) {
            this.edits = GsonUtils.base64Encode(edits);
        }

        public IList getEdits() {
            return GsonUtils.base64Decode(edits, new TypeStore());
        }
    }

    public static class RegisterLocationsParameters {
        private final IString scheme;
        private final IString authority;
        private final ISourceLocation[][] mapping;

        public RegisterLocationsParameters(IString scheme, IString authority, IMap mapping) {
            this.scheme = scheme;
            this.authority = authority;
            this.mapping = mapping.stream().map(ITuple.class::cast).map(e -> new ISourceLocation[] { (ISourceLocation) e.get(0), (ISourceLocation) e.get(1)}).toArray(n -> new ISourceLocation[n][2]);
        }

        public RegisterLocationsParameters(IString scheme, IString authority, ISourceLocation[][] mapping) {
            this.scheme = scheme;
            this.authority = authority;
            this.mapping = mapping;
        }

        public IString getScheme() {
            return scheme;
        }

        public IString getAuthority() {
            return authority;
        }

        public IMap getMapping() {
            var vf = ValueFactoryFactory.getValueFactory();
            return Stream.of(mapping).map(e -> vf.tuple(e[0], e[1])).collect(vf.mapWriter());
        }

        public ISourceLocation[][] getMappingAsArray() {
            return mapping;
        }
    }
}
