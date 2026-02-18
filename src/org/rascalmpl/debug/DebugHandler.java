/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.debug;

import static org.rascalmpl.debug.AbstractInterpreterEventTrigger.newNullEventTrigger;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.function.IntSupplier;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Command;
import org.rascalmpl.debug.IDebugMessage.Detail;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.control_exceptions.RestartFrameException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.Pair;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.rascal.RascalValuePrinter;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;

import java.util.function.Function;
import org.rascalmpl.exceptions.RascalStackOverflowError;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.result.out.INodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;

import io.usethesource.vallang.io.StandardTextWriter;
import java.io.StringWriter;
import java.io.PrintWriter;
import org.rascalmpl.repl.output.impl.PrinterErrorCommandOutput;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.IValue;

public final class DebugHandler implements IDebugHandler, IRascalRuntimeEvaluation {

	private AbstractInterpreterEventTrigger eventTrigger;

	private static final ISourceLocation DEBUGGER_PROMPT_LOCATION = URIUtil.rootLocation("debugger");

	private final Map<ISourceLocation, String> breakpoints = new java.util.HashMap<>();
			
	/**
	 * Indicates a manual suspend request from the debugger, e.g. caused by a pause action in the GUI.
	 */
	private boolean suspendRequested;

	private boolean suspendOnException = false;

	private Exception lastExceptionHandled = null;

	public boolean getSuspendOnException() {
		return suspendOnException;
	}

	public void setSuspendOnException(boolean suspendOnException) {
		this.suspendOnException = suspendOnException;
	}

	/**
	 * Indicates that the evaluator is suspended. Also used for suspending / blocking the evaluator.
	 */
	private boolean suspended;

	private long suspendThreadId;
	
	private enum DebugStepMode {
		NO_STEP, STEP_INTO, STEP_OVER, STEP_OUT
	}
	
	private DebugStepMode stepMode = DebugStepMode.NO_STEP;
	
	/**
	 * Referring to {@link ISourceLocation} responsible for last suspension.
	 */
	private AbstractAST referenceAST = null;	

	/**
	 * Referring to the stack depth at last suspension suspension.
	 * This information is used to determine if stepping enters a function call.
	 * {@see #suspend(IEvaluator, AbstractAST)}
	 */	
	private Integer referenceEnvironmentStackSize = null;

	/**
	 * Action to execute on termination request, or <code>null</code> if none.
	 */
	private Runnable terminateAction = null;

	/**
	 * Evaluator that is being debugged.
	 */
	private Evaluator evaluator = null;

	/**
	 * Flag indicating a frame restart has been requested.
	 * Set by the debugger thread, checked and used by the evaluated thread in suspended().
	 */
	private AtomicInteger restartFrameId = new AtomicInteger(-1);

	/**
	 * Create a new debug handler with its own interpreter event trigger.
	 */
	public DebugHandler() {
		setEventTrigger(newNullEventTrigger());
	}
	
	private boolean hasBreakpoint(ISourceLocation b) {
		String condition = breakpoints.get(b);
		if(condition == null) {
			return false;
		}
		if(condition.isEmpty()) {
			return true;
		}
		setSuspended(true);
		
		try {
			Result<IValue> condResult = this.evaluate(condition, (Environment) evaluator.getCurrentStack().lastElement()).result;
			setSuspended(false);
			return condResult.isTrue();
		}
		catch (Throwable e) {
			setSuspended(false);
			return false;
		}
	}
	
	private void addBreakpoint(ISourceLocation breakpointLocation) {
		breakpoints.put(breakpointLocation, "");
	}

	private void addBreakpoint(ISourceLocation breakpointLocation, String condition) {
		breakpoints.put(breakpointLocation, condition);
	}

	private void removeBreakpoint(ISourceLocation breakpointLocation) {
		breakpoints.remove(breakpointLocation);
	}
	
	protected void clearSuspensionState() {
		setReferenceAST(null);
		setReferenceEnvironmentStackSize(null);
		setSuspended(false);
	}
	
	protected void updateSuspensionState(int callStackSize, AbstractAST currentAST) {
		setReferenceAST(currentAST);
		
		// TODO: remove cast to {@link Evaluator} and rework {@link IEvaluator}.
		setReferenceEnvironmentStackSize(callStackSize);
		setSuspended(true);
	}
	
	@Override
	public void suspended(Object runtime, IntSupplier getCallStackSize, AbstractAST currentAST) {
		if(isSuspended() && Thread.currentThread().getId() != this.suspendThreadId) {
			// already suspended by another thread, ignore any suspension
			return;
		}
		
	    if (isSuspendRequested()) {
	        updateSuspensionState(getCallStackSize.getAsInt(), currentAST);
	        getEventTrigger().fireSuspendByClientRequestEvent();			
	        setSuspendRequested(false);
	    } else if(getSuspendOnException() && runtime instanceof Evaluator && ((Evaluator) runtime).getCurrentException() != null ) {
	        // Suspension due to exception
			Evaluator eval = (Evaluator) runtime;
			Exception e = eval.getCurrentException();
			if(lastExceptionHandled != null && e == lastExceptionHandled){
				return; // already handled this exception
			}
			if(handleExceptionSuspension(eval, e)){
				lastExceptionHandled = e;
				updateSuspensionState(getCallStackSize.getAsInt(), currentAST);
				getEventTrigger().fireSuspendByExceptionEvent(e);
			} else {
				return;
			}
	    }
	    else {
	        AbstractAST location = currentAST;

			if (hasBreakpoint(location.getLocation())) {
				updateSuspensionState(getCallStackSize.getAsInt(), currentAST);
				getEventTrigger().fireSuspendByBreakpointEvent(location.getLocation());
			}
			
	        switch (getStepMode()) {

	        case STEP_INTO:
	            updateSuspensionState(getCallStackSize.getAsInt(), currentAST);
	            getEventTrigger().fireSuspendByStepEndEvent();
	            break;

	        case STEP_OVER:
	            Integer currentEnvironmentStackSize = getCallStackSize.getAsInt();

	            /*
	             * Stepping over implies:
	             * * either there is a next statement in the same environment stack frame (which might
	             *   equal the reference statement in case of recursion or single statement loops)
	             * * or there is no next statement in the same stack frame and thus the stack frame 
	             *   eventually gets popped from the stack. As long the calls in deeper nesting levels 
	             *   are executed, no action needs to be taken.
	             */
	            switch (currentEnvironmentStackSize.compareTo(getReferenceEnvironmentStackSize())) {
	            case 0:
	                /*
	                 * For the case that we are still within the same stack
	                 * frame, positions are compared to ensure that the
	                 * statement was finished executing.
	                 */
					ISourceLocation stepScope = getReferenceAST().getDebugStepScope();
	                int referenceStart = stepScope.getOffset();
	                int referenceAfter = stepScope.getOffset() + stepScope.getLength();
	                int currentStart = location.getLocation().getOffset();
	                int currentAfter = location.getLocation().getOffset() + location.getLocation().getLength();

	                if (currentStart < referenceStart
	                        || currentStart >= referenceAfter
	                        || currentStart == referenceStart
	                        && currentAfter == referenceAfter) {
	                    updateSuspensionState(currentEnvironmentStackSize, currentAST);
	                    getEventTrigger().fireSuspendByStepEndEvent();
	                }
	                break;

	            case -1:
	                // lower stack size: left scope, thus over
	                updateSuspensionState(currentEnvironmentStackSize, currentAST);
	                getEventTrigger().fireSuspendByStepEndEvent();
	                break;

	            case +1:
	                // higher stack size: not over yet
	                break;

	            default:
	                throw new RuntimeException(
	                        "Requires compareTo() to return exactly either -1, 0, or +1.");
	            }
	            break;

			case STEP_OUT:
				Integer currentEnvStackSize = getCallStackSize.getAsInt();

				if(currentEnvStackSize.compareTo(getReferenceEnvironmentStackSize()) < 0) {
					updateSuspensionState(currentEnvStackSize, currentAST);
					getEventTrigger().fireSuspendByStepEndEvent();
				}

				break;

	        case NO_STEP:
	            break;
	        }
	    }
		
	    /*
	     * Waiting until GUI triggers end of suspension.
	     */
	    while (isSuspended()) {
	        try {
	            runtime.wait(50);
	        } catch (InterruptedException e) {
	            // Ignore
	        }
	    }

	    // Check if a frame restart was requested while suspended
		int frameToRestart = restartFrameId.getAndSet(-1);
	    if (frameToRestart >= 0) {
	        throw new RestartFrameException(frameToRestart);
	    }
	}

	private boolean handleExceptionSuspension(Evaluator eval, Exception e) {
		if(e instanceof Throw){
			Throw thr = (Throw) e;
			IValue excValue = thr.getException();
			if(excValue.getType().isAbstractData()){
				// We ignore suspension that happens due in standard library code for RuntimeExceptions
				if(excValue.getType().getName().equals("RuntimeException")){
					return !eval.getCurrentAST().getLocation().getScheme().equals("std");
				}
			}
			return true;
		}
		return false;
	}

	protected AbstractAST getReferenceAST() {
	  return referenceAST;
	}

	protected void setReferenceAST(AbstractAST referenceAST) {
	  this.referenceAST = referenceAST;
	}

	public Evaluator getEvaluator() {
	  return evaluator;
	}
	
	public void setEvaluator(Evaluator evaluator) {
	  this.evaluator = evaluator;
	}

	public Result<IValue> importModule(String command){
		// Parse the command and check if it is an import statement
		// The parsing code is supposed to be the same as in org.rascalmpl.interpreter.Evaluator.eval()
		// If the parsing fail, the exceptions must be handled by the caller
		IActionExecutor<ITree> actionExecutor =  new NoActionExecutor();
        ITree tree = new RascalParser().parse(Parser.START_COMMAND, DEBUGGER_PROMPT_LOCATION.getURI(), command.toCharArray(), INodeFlattener.UNLIMITED_AMB_DEPTH, actionExecutor, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false));
		Command stat = new ASTBuilder().buildCommand(tree);
        if (!stat.isImport()) {
            return null;
        }
		Environment oldEnvironment = evaluator.getCurrentEnvt();
		evaluator.setCurrentEnvt(evaluator.__getRootScope()); // For import we set the current module to the root to reload modules properly
		Result<IValue> result = evaluator.eval(evaluator.getMonitor(), command, DEBUGGER_PROMPT_LOCATION);
		evaluator.setCurrentEnvt(oldEnvironment);
		return result;
	}

	/**
	 * Evaluate the given command in the provided environment and return both a
	 * printable {@link ICommandOutput} and the raw {@link Result} (if any).
	 * ParseError is propagated to the caller so callers can format it like the REPL.
	 */
	@Override
	public EvalResult evaluate(String command, Environment evalEnv) throws IllegalStateException {
		if (evaluator == null) {
			throw new IllegalStateException("DebugHandler was not initialized with an Evaluator");
		}
		if (!suspended) {
			throw new IllegalStateException("Evaluator must be suspended to evaluate expressions");
		}
		RascalValuePrinter printer = new RascalValuePrinter() {
			@Override
			protected Function<IValue, IValue> liftProviderFunction(IFunction func) {
				return v -> {
					synchronized (evaluator) {
						return func.call(v);
					}
				};
			}
		};

		synchronized (evaluator) { // The evaluator is synchronized here, under the assumption that the evaluator is currently suspended by this thread
			// Save old state
			AbstractInterpreterEventTrigger oldTrigger = evaluator.getEventTrigger();
			Environment oldEnvironment = evaluator.getCurrentEnvt();
			AbstractAST oldAST = evaluator.getCurrentAST();
			try {
				// disable suspend triggers while evaluating expressions from the debugger
				evaluator.removeSuspendTriggerListener(this);
				evaluator.setEventTrigger(AbstractInterpreterEventTrigger.newNullEventTrigger());
				evaluator.setCurrentEnvt(evalEnv);
				Result<IValue> result = importModule(command);
				if(result == null) {
					result = evaluator.eval(evaluator.getMonitor(), command, DEBUGGER_PROMPT_LOCATION);
				}

				ICommandOutput out = printer.outputResult((org.rascalmpl.interpreter.result.IRascalResult) result);
				return new EvalResult(result, out);
			}
			catch (InterruptException ex) {
				ICommandOutput out = printer.outputError((w, sw, u) -> {
					w.println((u ? "»» " : ">> ") + "Interrupted");
					ex.getRascalStackTrace().prettyPrintedString(w, sw);
				});
				return new EvalResult(null, out);
			}
			catch (RascalStackOverflowError e) {
				ICommandOutput out = printer.outputError((w, sw, _u) -> {
					w.println(e.makeThrow().toString());
				});
				return new EvalResult(null, out);
			}
			catch (StaticError e) {
				ICommandOutput out = printer.outputError((w, sw, _u) -> {
					w.println(String.format("%s: %s", e.getLocation(), e.getMessage()));
				});
				return new EvalResult(null, out);
			}
			catch (Throw e) {
				ICommandOutput out = printer.outputError((w, sw, _u) -> {
					w.println(e.toString());
				});
				return new EvalResult(null, out);
			}
			catch (QuitException q) {
				ICommandOutput out = printer.outputError((w, sw, _u) -> {
					w.println("Quit requested");
				});
				return new EvalResult(null, out);
			}
			catch (ParseError pe) {
				// Format parse error using the REPL helper so the message matches REPL output
				var perr = new StringWriter();
				var perrPw = new PrintWriter(perr, true);
				ReadEvalPrintDialogMessages.parseErrorMessage(perrPw, command, DEBUGGER_PROMPT_LOCATION.getScheme(), pe, new StandardTextWriter(false));
				// Remove the initial prompt ("rascal>") that the formatter emits
				String formatted = perr.toString();
				String shifted = formatted.length() > 7 ? formatted.substring(7) : formatted;
				return new EvalResult(null, new PrinterErrorCommandOutput(shifted));
			}
			catch (Throwable e) {
				ICommandOutput out = printer.outputError((w, sw, _u) -> {
					w.println(e.toString());
				});
				return new EvalResult(null, out);
			}
			finally {
				// Restore old state
				evaluator.setCurrentEnvt(oldEnvironment);
				evaluator.setEventTrigger(oldTrigger);
				evaluator.addSuspendTriggerListener(this);
				evaluator.setCurrentAST(oldAST);
			}
		}
	}

	protected Integer getReferenceEnvironmentStackSize() {
	  return referenceEnvironmentStackSize;
	}

	protected void setReferenceEnvironmentStackSize(Integer referenceEnvironmentStackSize) {
	  this.referenceEnvironmentStackSize = referenceEnvironmentStackSize;
	}

	protected boolean isSuspendRequested() {
	  return suspendRequested;
	}

	protected void setSuspendRequested(boolean suspendRequested) {
	  this.suspendRequested = suspendRequested;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void processMessage(IDebugMessage message) {
	  switch (message.getSubject()) {

	  case BREAKPOINT:
	    switch (message.getAction()) {
	    case SET:
		  switch (message.getDetail()) {
			case CONDITIONAL:
				Pair<ISourceLocation, String> payload = (Pair<ISourceLocation, String>) message.getPayload();		
	      		addBreakpoint(payload.getFirst(), payload.getSecond());
				break;
			case UNKNOWN:
	      		ISourceLocation breakpointLocation = (ISourceLocation) message.getPayload();
	      		addBreakpoint(breakpointLocation);
		  }
	      break;

	    case DELETE:
	      ISourceLocation breakpointLocation = (ISourceLocation) message.getPayload();
	      removeBreakpoint(breakpointLocation);
	      break;
	    }
	    break;

	  case INTERPRETER:	
	    switch (message.getAction()) {
	    case SUSPEND:
	      if (message.getDetail() == Detail.CLIENT_REQUEST) {
	        setSuspendRequested(true);
	      }
	      break;

	    case RESUME:
	      setSuspended(false);

	      switch (message.getDetail()) {
	      case STEP_INTO:
	        setStepMode(DebugStepMode.STEP_INTO);
	        getEventTrigger().fireResumeByStepIntoEvent();
	        break;

	      case STEP_OVER:
	        setStepMode(DebugStepMode.STEP_OVER);
	        getEventTrigger().fireResumeByStepOverEvent();
	        break;

	      case STEP_OUT:
	        setStepMode(DebugStepMode.STEP_OUT);
			getEventTrigger().fireResumeByStepOutEvent();
	        break;

	      case CLIENT_REQUEST:
	        setStepMode(DebugStepMode.NO_STEP);
	        getEventTrigger().fireResumeByClientRequestEvent();
	        break;
	      }
	      break;

	    case TERMINATE:
	      if (terminateAction != null) {
	        terminateAction.run();
	      }
	      break;
		
		case RESTART_FRAME:
			if(suspended) {
				int frameId = (int) message.getPayload();
				assert frameId >= 0 && frameId < evaluator.getCurrentStack().size(): "Frame id out of bounds: " + frameId;
				// Set flag for the evaluated thread to handle the restart
				if (restartFrameId.compareAndSet(-1, frameId)) {
					// Unsuspend to let the evaluated thread continue and hit the restart exception
					setSuspendRequested(true);
					setSuspended(false);
				}
			}
		  break;
		}
	    break;
	  }
	}

	public void setTerminateAction(Runnable terminateAction) {
	  this.terminateAction = terminateAction;
	}

	protected synchronized boolean isSuspended() {
	  return suspended;
	}

	protected synchronized void setSuspended(boolean suspended) {
	  this.suspendThreadId = Thread.currentThread().getId();
	  this.suspended = suspended;
	}

	protected DebugStepMode getStepMode() {
	  return stepMode;
	}

	protected void setStepMode(DebugStepMode stepMode) {
	  this.stepMode = stepMode;
	}

	public AbstractInterpreterEventTrigger getEventTrigger() {
	  return eventTrigger;
	}

	public void setEventTrigger(AbstractInterpreterEventTrigger eventTrigger) {
	  this.eventTrigger = eventTrigger;
	}	

}
