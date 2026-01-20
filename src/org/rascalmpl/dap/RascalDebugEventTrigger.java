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

import io.usethesource.vallang.ISourceLocation;

import org.eclipse.lsp4j.debug.StoppedEventArguments;
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient;
import org.rascalmpl.dap.breakpoint.BreakpointsCollection;
import org.rascalmpl.debug.*;

/**
 * This class handled events triggered by the Rascal interpreter (suspension, resume, ...)
 */
public class RascalDebugEventTrigger extends AbstractInterpreterEventTrigger {

    private IDebugProtocolClient client;
    private final SuspendedState suspendedState;
    private final BreakpointsCollection breakpointsCollection;
    private final DebugHandler debugHandler;

    public RascalDebugEventTrigger(Object source, BreakpointsCollection breakpointsCollection, SuspendedState suspendedState, DebugHandler debugHandler) {
        super(source);
        this.breakpointsCollection = breakpointsCollection;
        this.suspendedState = suspendedState;
        this.debugHandler = debugHandler;
    }

    public void setDebugProtocolClient(IDebugProtocolClient client) {
        this.client = client;
    }

    @Override
    public void addRascalEventListener(IRascalEventListener listener) {
        throw new UnsupportedOperationException("Method 'addRascalEventListener' shouldn't be called");
    }

    @Override
    public void removeRascalEventListener(IRascalEventListener listener) {
        throw new UnsupportedOperationException("Method 'removeRascalEventListener' shouldn't be called");
    }

    @Override
    protected void fireEvent(RascalEvent event) {
        throw new UnsupportedOperationException("Method 'fireEvent' shouldn't be called");
    }

    @Override
    public void fireSuspendByBreakpointEvent(Object data) {
        if(!(data instanceof ISourceLocation)){
            return;
        }
        ISourceLocation location = (ISourceLocation) data;
        int breakpointID = breakpointsCollection.getBreakpointID(location);
        if(breakpointID < 0){
            return;
        }

        suspendedState.suspended();

        StoppedEventArguments stoppedEventArguments = new StoppedEventArguments();
        stoppedEventArguments.setThreadId(RascalDebugAdapter.mainThreadID);
        stoppedEventArguments.setDescription("Paused on breakpoint.");
        stoppedEventArguments.setReason("breakpoint");
        stoppedEventArguments.setHitBreakpointIds(new Integer[]{breakpointID});
        client.stopped(stoppedEventArguments);
    }

    @Override
    public void fireResumeEvent(RascalEvent.Detail detail) {
        suspendedState.resumed();
    }

    @Override
    public void fireResumeByStepOverEvent() {
        suspendedState.resumed();
    }

    @Override
    public void fireResumeByStepIntoEvent() {
        suspendedState.resumed();
    }

    @Override
    public void fireSuspendByStepEndEvent() {
        ISourceLocation currentLocation = suspendedState.getCurrentLocation();
        if(currentLocation.getPath().length() == 1) {
            debugHandler.processMessage(DebugMessageFactory.requestResumption());
            return;
        }

        suspendedState.suspended();

        StoppedEventArguments stoppedEventArguments = new StoppedEventArguments();
        stoppedEventArguments.setThreadId(RascalDebugAdapter.mainThreadID);
        stoppedEventArguments.setDescription("Paused on step end.");
        stoppedEventArguments.setReason("step");
        client.stopped(stoppedEventArguments);
    }

    @Override
    public void fireSuspendByClientRequestEvent() {
        suspendedState.suspended();

        StoppedEventArguments stoppedEventArguments = new StoppedEventArguments();
        stoppedEventArguments.setThreadId(RascalDebugAdapter.mainThreadID);
        stoppedEventArguments.setDescription("Paused by client.");
        stoppedEventArguments.setReason("pause");
        client.stopped(stoppedEventArguments);
    }

    @Override
    public void fireSuspendByExceptionEvent(Exception exception) {
        suspendedState.suspended();

        StoppedEventArguments stoppedEventArguments = new StoppedEventArguments();
        stoppedEventArguments.setThreadId(RascalDebugAdapter.mainThreadID);
        stoppedEventArguments.setDescription("Paused on exception.");
        stoppedEventArguments.setReason("exception");
        stoppedEventArguments.setText(exception.getMessage());
        client.stopped(stoppedEventArguments);
    }

    @Override
    public void fireSuspendByRestartFrameEndEvent() {
        suspendedState.suspended();

        StoppedEventArguments stoppedEventArguments = new StoppedEventArguments();
        stoppedEventArguments.setThreadId(RascalDebugAdapter.mainThreadID);
        stoppedEventArguments.setDescription("Paused after restarting frame.");
        stoppedEventArguments.setReason("restart");
        client.stopped(stoppedEventArguments);
    }

    @Override
    public void fireSuspendEvent(RascalEvent.Detail detail) {}
}
