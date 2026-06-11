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

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.rascalmpl.ideservices.jsonrpc.ApplyDocumentsEditsRequest;
import org.rascalmpl.ideservices.jsonrpc.BrowseRequest;
import org.rascalmpl.ideservices.jsonrpc.EditRequest;
import org.rascalmpl.ideservices.jsonrpc.RegisterDebugServerPortRequest;
import org.rascalmpl.ideservices.jsonrpc.RegisterDiagnosticsRequest;
import org.rascalmpl.ideservices.jsonrpc.RegisterLocationsRequest;
import org.rascalmpl.ideservices.jsonrpc.StartDebuggingSessionRequest;
import org.rascalmpl.ideservices.jsonrpc.UnregisterDiagnosticsRequest;
import org.rascalmpl.uri.remote.jsonrpc.ISourceLocationRequest;
import org.rascalmpl.uri.remote.jsonrpc.SourceLocationResponse;

/**
 * This interface exposes functionality from `IDEServices` over IPC.
 * It is a subset of `IDEServices`, as not all of its functionality lends
 * itself to be run remotely in a nice way.
 */
public interface IRemoteIDEServices {
    @JsonRequest
    CompletableFuture<Void> edit(EditRequest req);

    @JsonRequest
    CompletableFuture<Void> browse(BrowseRequest req);

    @JsonRequest
    CompletableFuture<SourceLocationResponse> resolveProjectLocation(ISourceLocationRequest req);

    @JsonRequest
    CompletableFuture<Void> applyDocumentsEdits(ApplyDocumentsEditsRequest req);

    @JsonRequest
    CompletableFuture<Void> registerLocations(RegisterLocationsRequest req);

    @JsonRequest
    CompletableFuture<Void> registerDiagnostics(RegisterDiagnosticsRequest req);

    @JsonRequest
    CompletableFuture<Void> unregisterDiagnostics(UnregisterDiagnosticsRequest req);

    @JsonRequest
    CompletableFuture<Void> startDebuggingSession(StartDebuggingSessionRequest req);

    @JsonRequest
    CompletableFuture<Void> registerDebugServerPort(RegisterDebugServerPortRequest req);
}
