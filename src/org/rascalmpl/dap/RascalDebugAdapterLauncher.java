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
package org.rascalmpl.dap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.lsp4j.debug.launch.DSPLauncher;
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.rascalmpl.debug.DebugHandler;
import org.rascalmpl.interpreter.Evaluator;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class RascalDebugAdapterLauncher {
    private static final Logger logger = LogManager.getLogger(RascalDebugAdapterLauncher.class);

    public static IDebugProtocolClient start(Evaluator evaluator, Socket clientSocket, DebugSocketServer socketServer, ExecutorService threadPool) {
        try {
            final DebugHandler debugHandler = new DebugHandler();
            debugHandler.setTerminateAction(() -> {
                try {
                    evaluator.removeSuspendTriggerListener(debugHandler);
                    // Wait for the server to send and to the client to receive the disconnect response, then close the socket
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                finally {
                    socketServer.closeClientSocket();
                }
            });
            evaluator.addSuspendTriggerListener(debugHandler);

            RascalDebugAdapter server = new RascalDebugAdapter(debugHandler, evaluator, threadPool);
            Launcher<IDebugProtocolClient> launcher = DSPLauncher.createServerLauncher(server, clientSocket.getInputStream(), clientSocket.getOutputStream());
            server.connect(launcher.getRemoteProxy());
            launcher.startListening();
            return launcher.getRemoteProxy();
        } catch (IOException e) {
            logger.fatal("Error opening communication to DAP", e);
            throw new RuntimeException("Error opening connection to DAP", e);
        }
    }
}
