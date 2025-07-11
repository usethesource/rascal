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
import org.checkerframework.checker.nullness.qual.Nullable;
import org.eclipse.lsp4j.debug.TerminatedEventArguments;
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class starts a socket server that listens for incoming debug connections from IDEs.
 */
public class DebugSocketServer {

    private static final Logger logger = LogManager.getLogger(DebugSocketServer.class);
    private final ServerSocket serverSocket;
    private volatile @Nullable Socket clientSocket;
    private volatile @Nullable IDebugProtocolClient debugClient;

    public DebugSocketServer(Evaluator evaluator, /*TerminalIDEClient*/ IDEServices terminal){
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // registerDebugServerPort(terminal);
        startListening(evaluator);
    }

    private void startListening(Evaluator evaluator){
        Thread t = new Thread(() -> {
            while(true){
                try {
                    Socket newClient = serverSocket.accept();
                    if(clientSocket == null || clientSocket.isClosed()){
                        clientSocket = newClient;
                        debugClient = RascalDebugAdapterLauncher.start(evaluator, clientSocket, this);
                    } else {
                        newClient.close();
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void registerDebugServerPort(/*TerminalIDEClient*/ IDEServices terminal){
    //     terminal.registerDebugServerPort((int) ProcessHandle.current().pid(), getPort());
    }

    public boolean isClientConnected(){
        var socket = clientSocket; // local copy for thread safety
        return socket != null && !socket.isClosed();
    }

    public int getPort(){
        return serverSocket.getLocalPort();
    }

    public void terminateDebugSession(){
        var client = debugClient; // take a local copy for thread safety
        if(client != null){
            TerminatedEventArguments args = new TerminatedEventArguments();
            args.setRestart(false);
            client.terminated(args);
            debugClient = null;
        }
    }

    public void closeClientSocket(){
        try {
            var socket = clientSocket; // local copy for thread safety
            if(socket != null && !socket.isClosed()){
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error("Error closing client socket", e);
                }
            }
        } finally {
            clientSocket = null;
            debugClient = null;
        }
    }
}
