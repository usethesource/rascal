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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.lsp4j.debug.*;
import org.eclipse.lsp4j.debug.Thread;
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient;
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer;
import org.rascalmpl.dap.breakpoint.BreakpointsCollection;
import org.rascalmpl.dap.variable.RascalVariable;
import org.rascalmpl.debug.DebugHandler;
import org.rascalmpl.debug.DebugMessageFactory;
import org.rascalmpl.debug.IRascalFrame;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.utils.StringUtils;
import org.rascalmpl.interpreter.utils.StringUtils.OffsetLengthTerm;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.util.Reflective;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.util.locations.ColumnMaps;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

/**
 * This class implements the Debug Adapter Protocol (DAP) for Rascal.
 */
public class RascalDebugAdapter implements IDebugProtocolServer {

    public static final int mainThreadID = 1; // hard coded arbitrary thread ID of Rascal Program for Debug Adapter Protocol
    private final int expensiveScopeMinSize = 100; // a scope is marked as expensive when there are more than xxx variables in it

    private IDebugProtocolClient client;
    private final RascalDebugEventTrigger eventTrigger;
    private final DebugHandler debugHandler;
    private final Evaluator evaluator;
    private final SuspendedState suspendedState;
    private final BreakpointsCollection breakpointsCollection;
    private final Pattern emptyAuthorityPathPattern = Pattern.compile("^\\w+:/\\w+[^/]");
    private final IDEServices services;
    private final ExecutorService ownExecutor;
    private final ColumnMaps columns;
    private int lineBase = 1;   // Default in DAP
    private int columnBase = 1; // Default in DAP

    public static final ISourceLocation DEBUGGER_LOC = URIUtil.rootLocation("debugger");


    public RascalDebugAdapter(DebugHandler debugHandler, Evaluator evaluator, IDEServices services, ExecutorService threadPool) {
        this.debugHandler = debugHandler;
        this.evaluator = evaluator;
        this.services = services;
        this.ownExecutor = threadPool;

        this.suspendedState = new SuspendedState(evaluator, services);
        this.breakpointsCollection = new BreakpointsCollection(debugHandler);

        this.eventTrigger = new RascalDebugEventTrigger(this, breakpointsCollection, suspendedState, debugHandler);
        debugHandler.setEventTrigger(eventTrigger);

        columns = new ColumnMaps(l -> {
            try {
                return Prelude.consumeInputStream(URIResolverRegistry.getInstance().getCharacterReader(l.top()));
            }
            catch (IOException e) {
                services.warning("Could not read contents of " + l.top(), l);
                return "";
            }
        });
    }

    public void connect(IDebugProtocolClient client) {
        this.client = client;
        this.eventTrigger.setDebugProtocolClient(client);
    }

    @Override
    public CompletableFuture<Capabilities> initialize(InitializeRequestArguments args) {
        var linesStartAt1 = args.getLinesStartAt1();
        var columnsStartAt1 = args.getColumnsStartAt1();
        if (linesStartAt1 != null && !linesStartAt1.booleanValue()) {
            lineBase = 0;
        }
        if (columnsStartAt1 != null && !columnsStartAt1.booleanValue()) {
            columnBase = 0;
        }
        
        return CompletableFuture.supplyAsync(() -> {
            Capabilities capabilities = new Capabilities();

            capabilities.setSupportsConfigurationDoneRequest(true);
            capabilities.setExceptionBreakpointFilters(new ExceptionBreakpointsFilter[]{});
            capabilities.setSupportsStepBack(false);
            capabilities.setSupportsRestartFrame(false);
            capabilities.setSupportsSetVariable(false);
            capabilities.setSupportsRestartRequest(false);

            return capabilities;
        }, ownExecutor);
    }


    @Override
    public CompletableFuture<SetBreakpointsResponse> setBreakpoints(SetBreakpointsArguments args) {
        return CompletableFuture.supplyAsync(() -> {
            SetBreakpointsResponse response = new SetBreakpointsResponse();
            response.setBreakpoints(new Breakpoint[0]);
            String extension = args.getSource().getName().substring(args.getSource().getName().lastIndexOf('.') + 1);
            if (!extension.equals("rsc")) {
                return response;
            }
            ISourceLocation loc = getLocationFromPath(args.getSource().getPath());
            if(loc == null){
                return response;
            }

            var reg = URIResolverRegistry.getInstance();
            if (!reg.exists(loc)) {
                return response;
            }

            var cannotSetMsg = String.format("Cannot set breakpoint%s", args.getBreakpoints().length > 1 ? "s" : "");

            ITree parseTree;
            try {
                parseTree = Reflective.parseModuleWithSpaces(loc);
            } catch (Throw t) {
                if (t.getException().getType().isConstructor()) {
                    var e = (IConstructor) t.getException();
                    var et = e.getConstructorType();
                    if (et != RuntimeExceptionFactory.IO) {
                        services.warning(String.format("%s: %s", cannotSetMsg, t.getMessage()), loc);
                    }
                }
                return response;
            } catch (ParseError e) {
                services.warning(String.format("%s: %s", cannotSetMsg, e.getMessage()), loc);
                return response;
            }
            breakpointsCollection.clearBreakpointsOfFile(loc.getPath());
            Breakpoint[] breakpoints = new Breakpoint[args.getBreakpoints().length];
            for(int i = 0; i<args.getBreakpoints().length; i++){
                SourceBreakpoint breakpoint = args.getBreakpoints()[i];
                ITree treeBreakableLocation = locateBreakableTree(parseTree, breakpoint.getLine());
                if(treeBreakableLocation != null) {
                    ISourceLocation breakableLocation = TreeAdapter.getLocation(treeBreakableLocation);
                    breakpointsCollection.addBreakpoint(breakableLocation, args.getSource());
                }
                Breakpoint b = new Breakpoint();
                b.setId(i);
                b.setLine(breakpoint.getLine());
                b.setColumn(breakpoint.getColumn());
                b.setVerified(treeBreakableLocation != null);
                breakpoints[i] = b;
            }
            response.setBreakpoints(breakpoints);
            return response;
        }, ownExecutor);
    }

    private ISourceLocation getLocationFromPath(String path){
        if(path.startsWith("/") || path.contains(":\\")){
            try {
                return URIUtil.createFileLocation(path);
            } catch (URISyntaxException e) {
                services.warning(e.getMessage(), DEBUGGER_LOC);
                return null;
            }
        } else {
            URI uri = URI.create(path);
            try {
                String finalUri = uri.toString();
                Matcher matcher = emptyAuthorityPathPattern.matcher(finalUri);
                if(matcher.find()){
                    finalUri = finalUri.replaceFirst(":/", ":///");
                }
                return URIUtil.createFromURI(finalUri);
            } catch (URISyntaxException e) {
                services.warning(e.getMessage(), DEBUGGER_LOC);
                return null;
            }
        }
    }

    private static final String breakable = "breakable";
    private static ITree locateBreakableTree(ITree tree, int line) {
        ISourceLocation l = TreeAdapter.getLocation(tree);

        if (l == null) {
            throw new IllegalArgumentException("Missing location");
        }

        if (TreeAdapter.isAmb(tree)) {
            INode node = IRascalValueFactory.getInstance().node(breakable);
            if (ProductionAdapter.hasAttribute(TreeAdapter.getProduction(tree), IRascalValueFactory.getInstance().constructor(RascalValueFactory.Attr_Tag, node))) {
                return tree;
            }

            return null;
        }

        if (TreeAdapter.isAppl(tree) && !TreeAdapter.isLexical(tree)) {
            IList children = TreeAdapter.getArgs(tree);

            for (IValue child : children) {
                ISourceLocation childLoc = TreeAdapter.getLocation((ITree) child);

                if (childLoc == null) {
                    continue;
                }

                if (childLoc.getBeginLine() <= line && line <= childLoc.getEndLine() ) {
                    ITree result = locateBreakableTree((ITree) child, line);

                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        INode node = IRascalValueFactory.getInstance().node(breakable);
        if (l.getBeginLine() == line && ProductionAdapter.hasAttribute(TreeAdapter.getProduction(tree), IRascalValueFactory.getInstance().constructor(RascalValueFactory.Attr_Tag, node))) {
            return tree;
        }

        return null;
    }

    @Override
    public CompletableFuture<Void> attach(Map<String, Object> args) {
        client.initialized();

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> configurationDone(ConfigurationDoneArguments args) {
        return CompletableFuture.supplyAsync(() -> {
            ProcessEventArguments eventArgs = new ProcessEventArguments();
            eventArgs.setSystemProcessId((int) ProcessHandle.current().pid());
            eventArgs.setName("Rascal REPL");
            eventArgs.setIsLocalProcess(true);
            eventArgs.setStartMethod(ProcessEventArgumentsStartMethod.ATTACH);
            client.process(eventArgs);

            ThreadEventArguments thread = new ThreadEventArguments();
            thread.setThreadId(mainThreadID);
            thread.setReason(ThreadEventArgumentsReason.STARTED);
            client.thread(thread);

            return null;
        }, ownExecutor);
    }

    @Override
    public CompletableFuture<ThreadsResponse> threads() {
        return CompletableFuture.supplyAsync(() -> {
            ThreadsResponse response = new ThreadsResponse();
            Thread t = new Thread();
            t.setId(mainThreadID);
            t.setName("Main Thread");
            response.setThreads(new Thread[]{
                t
            });
            return response;
        }, ownExecutor);
    }

    @Override
    public CompletableFuture<StackTraceResponse> stackTrace(StackTraceArguments args) {
        if(args.getThreadId() != mainThreadID) {
            return CompletableFuture.completedFuture(new StackTraceResponse());
        }
        return CompletableFuture.supplyAsync(() -> {
            StackTraceResponse response = new StackTraceResponse();
            IRascalFrame[] stackFrames = suspendedState.getCurrentStackFrames();
            response.setTotalFrames(stackFrames.length);
            StackFrame[] stackFramesResponse = new StackFrame[stackFrames.length];
            IRascalFrame currentFrame = suspendedState.getCurrentStackFrame();
            ISourceLocation currentLoc = evaluator.getCurrentPointOfExecution() != null ?
                evaluator.getCurrentPointOfExecution()
                : URIUtil.rootLocation("stdin");
            stackFramesResponse[0] = createStackFrame(stackFrames.length - 1, currentLoc, currentFrame.getName());
            for(int i = 1; i < stackFramesResponse.length; i++) {
                IRascalFrame f = stackFrames[stackFrames.length - i - 1];
                ISourceLocation loc = stackFrames[stackFrames.length-i].getCallerLocation();
                stackFramesResponse[i] = createStackFrame(stackFrames.length - i - 1, loc, f.getName());
            }
            response.setStackFrames(stackFramesResponse);
            return response;
        }, ownExecutor);
    }

    private int shiftLine(int line) {
        // Rascal locations use 1 as line base. If DAP is configured to expect base 0, we shift the line number
        return line + lineBase - 1;
    }

    private int shiftColumn(int column) {
        // Rascal locations use 0 as column base. If DAP is configured to expect base 1, we shift the column
        return column + columnBase;
    }

    private StackFrame createStackFrame(int id, ISourceLocation loc, String name){
        StackFrame frame = new StackFrame();
        frame.setId(id);
        frame.setName(name);
        if(loc != null){
            var offsets = columns.get(loc);
            var line = shiftLine(loc.getBeginLine());
            var column = shiftColumn(offsets.translateColumn(loc.getBeginLine(), loc.getBeginColumn(), false));
            frame.setLine(line);
            frame.setColumn(column);
            frame.setSource(getSourceFromISourceLocation(loc));
        }
        return frame;
    }

    public Source getSourceFromISourceLocation(ISourceLocation loc) {
        Source source = new Source();
        File file = new File(loc.getPath());
        source.setName(file.getName());
        String path = loc.getPath();
        if(!loc.getScheme().equals("file")){
            path = loc.getScheme() + "://" + loc.getAuthority() + path;
        }
        source.setPath(path);
        source.setSourceReference(-1);
        return source;
    }

    @Override
    public CompletableFuture<ScopesResponse> scopes(ScopesArguments args) {
        return CompletableFuture.supplyAsync(() -> {
            int frameId = args.getFrameId();

            List<Scope> scopes = new ArrayList<>();

            IRascalFrame frame = suspendedState.getCurrentStackFrames()[frameId];
            ScopesResponse response = new ScopesResponse();
            scopes.add(createScope("Locals",
                frame.getFrameVariables().size(),
                "locals",
                frame.getFrameVariables().size()>expensiveScopeMinSize,
                suspendedState.addScope(frame)));

            for(String importName : frame.getImports()){
                IRascalFrame module = evaluator.getModule(importName);

                if(module != null && module.getFrameVariables().size() > 0){
                    scopes.add(createScope("Module " + importName,
                        module.getFrameVariables().size(),
                        "module",
                        module.getFrameVariables().size()>expensiveScopeMinSize,
                        suspendedState.addScope(module)));
                }
            }

            response.setScopes(scopes.toArray(new Scope[scopes.size()]));
            return response;
        }, ownExecutor);
    }

    private Scope createScope(String name, int namedVariables, String presentationHint, boolean expensive, int variablesReference){
        Scope scope = new Scope();
        scope.setName(name);
        scope.setNamedVariables(namedVariables);
        scope.setPresentationHint(presentationHint);
        scope.setExpensive(expensive);
        scope.setVariablesReference(variablesReference);
        return scope;
    }

    @Override
    public CompletableFuture<VariablesResponse> variables(VariablesArguments args) {
        return CompletableFuture.supplyAsync(() -> {
            int reference = args.getVariablesReference();
            int minIndex = args.getStart() != null ? args.getStart() : 0;
            int maxCount = args.getCount() != null ? args.getCount() : -1;

            VariablesResponse response = new VariablesResponse();
            List<RascalVariable> variables = suspendedState.getVariables(reference, minIndex, maxCount);

            response.setVariables(variables.stream().map(var -> {
                Variable variable = new Variable();
                variable.setName(var.getName());
                variable.setType(var.getType().toString());
                variable.setValue(var.getDisplayValue());
                variable.setVariablesReference(var.getReferenceID());
                variable.setNamedVariables(var.getNamedVariables());
                variable.setIndexedVariables(var.getIndexedVariables());
                return variable;
            }).toArray(Variable[]::new));
            return response;
        }, ownExecutor);
    }

    @Override
    public CompletableFuture<ContinueResponse> continue_(ContinueArguments args) {
        return CompletableFuture.supplyAsync(() -> {
            ContinueResponse response = new ContinueResponse();
            response.setAllThreadsContinued(true);

            debugHandler.processMessage(DebugMessageFactory.requestResumption());

            return response;
        }, ownExecutor);
    }

    @Override
    public CompletableFuture<Void> next(NextArguments args) {
        return CompletableFuture.supplyAsync(() -> {
            debugHandler.processMessage(DebugMessageFactory.requestStepOver());
            return null;
        }, ownExecutor);
    }

    @Override
    public CompletableFuture<Void> disconnect(DisconnectArguments args) {
        // start Runnable to request termination (which blocks the thread waiting that all messages are sent and received between vscode and debug adapter, then close the socket)
        ownExecutor.execute(new Runnable() {
            public void run() {
                debugHandler.processMessage(DebugMessageFactory.requestTermination());
                if(suspendedState.isSuspended()){
                    debugHandler.processMessage(DebugMessageFactory.requestResumption());
                }
            }
        });

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> stepIn(StepInArguments args) {
        return CompletableFuture.supplyAsync(() -> {
            debugHandler.processMessage(DebugMessageFactory.requestStepInto());

            return null;
        }, ownExecutor);
    }

    @Override
    public CompletableFuture<Void> stepOut(StepOutArguments args) {
        return CompletableFuture.supplyAsync(() -> {
            debugHandler.processMessage(DebugMessageFactory.requestStepOut());

            return null;
        }, ownExecutor);
    }

    @Override
    public CompletableFuture<Void> pause(PauseArguments args) {
        return CompletableFuture.supplyAsync(() -> {
            debugHandler.processMessage(DebugMessageFactory.requestSuspension());

            return null;
        }, ownExecutor);
    }

    @Override
    public CompletableFuture<EvaluateResponse> evaluate(EvaluateArguments args) {
        return CompletableFuture.supplyAsync(() -> {
            EvaluateResponse response = new EvaluateResponse();

            String expr = args.getExpression();
            switch (args.getContext()) {
                case "hover": // Not called until we set the supportsEvaluateForHovers capability to true
                case "clipboard": // Not called until we set the supportsClipboardContext capability to true
                case "repl": // Called from the debugger repl
                case "watch": // Called from watched expressions. Only singular variable references are supported.
                    response.setResult("Only singular variable references are supported");
                    OffsetLengthTerm identSearchResult = StringUtils.findRascalIdentifierAtOffset(expr, 0);
                    if (args.getFrameId() != null && identSearchResult != null && identSearchResult.offset == 0 && identSearchResult.length == expr.length()) {
                        // The entire expression is a valid identifier
                        response.setResult("Undefined variable");
                        List<RascalVariable> variables = suspendedState.getVariables(args.getFrameId(), 0, -1);
                        for (RascalVariable var : variables) {
                            if (var.getName().equals(expr)) {
                                response.setResult(var.getDisplayValue());
                                response.setType(var.getType().toString());
                                response.setVariablesReference(var.getReferenceID());
                                response.setNamedVariables(var.getNamedVariables());
                                response.setIndexedVariables(var.getIndexedVariables());
                                break;
                            }
                        }   
                    }
                    break;

                case "variables": // Called from the "variables" view when copying the value of a variable (and maybe in other situations?)
                    // In this case the expression already contains the value of the variable. We just return it.
                    response.setResult(expr);
                    break;

                default:
                    break;
            }

            return response;
        }, ownExecutor);
    }
    
}

