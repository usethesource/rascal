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
package org.rascalmpl.repl.rascal;


import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwableMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RascalStackOverflowError;
import org.rascalmpl.exceptions.StackTrace;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.StopREPLException;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.uri.ISourceLocationWatcher.ISourceLocationChanged;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

/**
 * Implementation of an interpreter based Rascal REPL Service. 
 * In most cases you might want to override/extend the {@link #buildIDEService(PrintWriter, IRascalMonitor, Terminal)} and the {@link #buildEvaluator(Reader, PrintWriter, PrintWriter, IDEServices)} functions.
 */
public class RascalInterpreterREPL implements IRascalLanguageProtocol {
    protected IDEServices services;
    protected Evaluator eval;
    protected final Set<String> dirtyModules = ConcurrentHashMap.newKeySet();

    private final URIResolverRegistry reg = URIResolverRegistry.getInstance();
    
    private final RascalValuePrinter printer;

    private static final ISourceLocation PROMPT_LOCATION = URIUtil.rootLocation("prompt");

    @Override
    public ITree parseCommand(String command) {
        Objects.requireNonNull(eval, "Not initialized yet");
        synchronized(eval) {
            return eval.parseCommand(new NullRascalMonitor(), command, PROMPT_LOCATION);
        }
    }

    public RascalInterpreterREPL() {
        this.printer = new RascalValuePrinter() {
            @Override
            protected Function<IValue, IValue> liftProviderFunction(IFunction func) {
                return v -> {
                    Objects.requireNonNull(eval, "Not initialized yet");
                    synchronized(eval) {
                        return func.call(v);
                    }
                };
            }
        };
    }

    @Override
    public ISourceLocation promptRootLocation() {
        return PROMPT_LOCATION;
    }

    /**
     * Build an IDE service, in most places you want to override this function to construct a specific one for the setting you are in.
     */
    protected IDEServices buildIDEService(PrintWriter err, IRascalMonitor monitor, Terminal term) {
        return new BasicIDEServices(err, monitor, term, URIUtil.rootLocation("cwd"));
    }

    /**
     * You might want to override/extend this function for different cases of where we're building a REPL (possible only extend on the result of it, by adding extra search path entries)
     */
    protected Evaluator buildEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr, IDEServices services) {
        return ShellEvaluatorFactory.getBasicEvaluator(input, stdout, stderr, services);
    }

    @Override
    public IDEServices initialize(Reader input, PrintWriter stdout, PrintWriter stderr, IRascalMonitor monitor,
        Terminal term) {
        services = buildIDEService(stderr, monitor, term);
        if (eval != null) {
            throw new IllegalStateException("Already initialized");
        }
        eval = buildEvaluator(input, stdout, stderr, services);

        // Register watches for all watchable locations on the search path for automatic reloading
        eval.getRascalResolver().collect().stream().filter(this::isWatchable).forEach(p -> {
            try {
                reg.watch(p, true, d -> sourceLocationChanged(p, d));
            }
            catch (IOException e) {
                stderr.println("Failed to register watch for " + p);
                e.printStackTrace(stderr);
            }
        });
        return services;
    }

    private boolean isWatchable(ISourceLocation loc) {
        return reg.hasNativelyWatchableResolver(loc) || reg.hasWritableResolver(loc);
    }

    @Override
    public ICommandOutput handleInput(String command) throws InterruptedException, ParseError, StopREPLException {
        Objects.requireNonNull(eval, "Not initialized yet");
        synchronized(eval) {
            try {
                Set<String> changes = new HashSet<>();
                changes.addAll(dirtyModules);
                dirtyModules.removeAll(changes);
                eval.reloadModules(eval.getMonitor(), changes, URIUtil.rootLocation("reloader"));
                return printer.outputResult(eval.eval(eval.getMonitor(), command, PROMPT_LOCATION));
            }
            catch (InterruptException ex) {
                return printer.outputError((w, sw, u) -> {
                    w.println((u ? "»» " : ">> ") + "Interrupted");
                    ex.getRascalStackTrace().prettyPrintedString(w, sw);
                });
            }
            catch (RascalStackOverflowError e) {
                return printer.outputError((w, sw, _u) -> {
                    throwMessage(w, e.makeThrow(), sw);
                });
            }
            catch (StaticError e) {
                return printer.outputError((w, sw, _u) -> {
                    staticErrorMessage(w, e, sw);
                });
            }
            catch (Throw e) {
                return printer.outputError((w, sw, _u) -> {
                    throwMessage(w,e, sw);
                });
            }
            catch (QuitException q) {
                throw new StopREPLException();
            }
            catch (Throwable e) {
                if (e instanceof ParseError) {
                    throw e;
                }
                return printer.outputError((w, sw, _u) -> {
                    throwableMessage(w, e, eval.getStackTrace(), sw);
                });
            }
        }
    }

    @Override
    public void cancelRunningCommandRequested() {
        Objects.requireNonNull(eval, "Not initialized yet");
        eval.interrupt();
        eval.endAllJobs();
    }

    public void cleanEnvironment() {
        Objects.requireNonNull(eval, "Not initialized yet");
        eval.getCurrentModuleEnvironment().reset();
    }

    @Override
    public ICommandOutput stackTraceRequested() {
        Objects.requireNonNull(eval, "Not initialized yet");
        StackTrace trace = eval.getStackTrace();
        return printer.prettyPrinted((w, sw, u) -> {
            w.println("Current stack trace:");
            trace.prettyPrintedString(w, sw);
            w.flush();
        });
    }

    @Override
    public List<String> lookupModules(String modulePrefix) {
        Objects.requireNonNull(eval, "Not initialized yet");
        return eval.getRascalResolver().listModuleEntries(modulePrefix);
    }

    @Override
    public Map<String, String> completePartialIdentifier(String qualifier, String partial) {
        Objects.requireNonNull(eval, "Not initialized yet");
        return eval.completePartialIdentifier(qualifier, partial);
    }

    @Override
    public Map<String, String> availableCommandLineOptions() {
        var commandLineOptions = new HashMap<String, String>();
        commandLineOptions.put(Configuration.GENERATOR_PROFILING_PROPERTY.substring("rascal.".length()), "enable sampling profiler for generator");
        commandLineOptions.put(Configuration.PROFILING_PROPERTY.substring("rascal.".length()), "enable sampling profiler" );
        commandLineOptions.put(Configuration.ERRORS_PROPERTY.substring("rascal.".length()), "print raw java errors");
        commandLineOptions.put(Configuration.TRACING_PROPERTY.substring("rascal.".length()), "trace all function calls (warning: a lot of output will be generated)");
        return commandLineOptions;
    }

    @Override
    public void flush() {
        eval.getErrorPrinter().flush();
        eval.getOutPrinter().flush();
    }

    protected void sourceLocationChanged(ISourceLocation srcPath, ISourceLocationChanged d) {
        if (URIUtil.isParentOf(srcPath, d.getLocation()) && d.getLocation().getPath().endsWith(".rsc")) {
            ISourceLocation relative = URIUtil.relativize(srcPath, d.getLocation());
            relative = URIUtil.removeExtension(relative);

            String modName = relative.getPath();
            if (modName.startsWith("/")) {
                modName = modName.substring(1);
            }
            modName = modName.replace("/", "::");
            modName = modName.replace("\\", "::");
            dirtyModules.add(modName);
        }
    }

}
