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
import java.util.function.IntSupplier;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Case;
import org.rascalmpl.debug.IDebugMessage.Detail;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.rascal.RascalValuePrinter;
import org.rascalmpl.values.functions.IFunction;
import java.util.function.Function;
import org.rascalmpl.semantics.dynamic.Statement.For;
import org.rascalmpl.semantics.dynamic.Statement.Switch;
import org.rascalmpl.semantics.dynamic.Statement.Visit;
import org.rascalmpl.semantics.dynamic.Statement.While;
import org.rascalmpl.exceptions.RascalStackOverflowError;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages;
import io.usethesource.vallang.io.StandardTextWriter;
import java.io.StringWriter;
import java.io.PrintWriter;
import org.rascalmpl.repl.output.impl.PrinterErrorCommandOutput;

import io.usethesource.vallang.ISourceLocation;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.IValue;

public final class DebugHandler implements IDebugHandler, IRascalRuntimeEvaluation {

	private AbstractInterpreterEventTrigger eventTrigger;

	private static final ISourceLocation DEBUGGER_PROMPT_LOCATION = URIUtil.rootLocation("debugger");

	private final Set<ISourceLocation> breakpoints = new java.util.HashSet<>();
			
	/**
	 * Indicates a manual suspend request from the debugger, e.g. caused by a pause action in the GUI.
	 */
	private boolean suspendRequested;

	/**
	 * Indicates that the evaluator is suspended. Also used for suspending / blocking the evaluator.
	 */
	private boolean suspended;
	
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
	 * Create a new debug handler with its own interpreter event trigger.
	 */
	public DebugHandler() {
		setEventTrigger(newNullEventTrigger());
	}
	
	private boolean hasBreakpoint(ISourceLocation b) {
		return breakpoints.contains(b);
	}
	
	private void addBreakpoint(ISourceLocation breakpointLocation) {
		breakpoints.add(breakpointLocation);
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
	    if (isSuspendRequested()) {
	        updateSuspensionState(getCallStackSize.getAsInt(), currentAST);
	        getEventTrigger().fireSuspendByClientRequestEvent();			
	        setSuspendRequested(false);
	    } 
	    else {
	        AbstractAST location = currentAST;
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
	                int referenceStart = getReferenceAST().getLocation().getOffset();
	                int referenceAfter = getReferenceAST().getLocation().getOffset() + getReferenceAST().getLocation().getLength();
	                int currentStart = location.getLocation().getOffset();
	                int currentAfter = location.getLocation().getOffset() + location.getLocation().getLength();

					// Special handling for For, While and Switch statements to step over inside their body
					if(getReferenceAST() instanceof For){
						For forStmt = (For) getReferenceAST();
						referenceAfter = forStmt.getBody().getLocation().getOffset();
					}
					if(getReferenceAST() instanceof While){
						While whileStmt = (While) getReferenceAST();
						referenceAfter = whileStmt.getBody().getLocation().getOffset();
					}
					if(getReferenceAST() instanceof Switch){
						Switch switchStmt = (Switch) getReferenceAST();
						if (switchStmt.getCases().size() > 0) {
							Case lastCase = switchStmt.getCases().get(0);
							referenceAfter = lastCase.getLocation().getOffset();
						}
					}
					if(getReferenceAST() instanceof Visit){
						Visit visitStmt = (Visit) getReferenceAST();
						org.rascalmpl.ast.Visit visit = visitStmt.getVisit();
						if (visit.getCases().size() > 0) {
							Case lastCase = visit.getCases().get(0);
							referenceAfter = lastCase.getLocation().getOffset();
						}
					}

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
	            if (hasBreakpoint(location.getLocation())) {
	                updateSuspensionState(getCallStackSize.getAsInt(), currentAST);
	                getEventTrigger().fireSuspendByBreakpointEvent(location.getLocation());
	            }
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
		String expr = command.endsWith(";") ? command : command + ";";

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

		synchronized (evaluator) {
			// Save old state
			AbstractInterpreterEventTrigger oldTrigger = evaluator.getEventTrigger();
			Environment oldEnvironment = evaluator.getCurrentEnvt();
			try {
				// disable suspend triggers while evaluating expressions from the debugger
				evaluator.removeSuspendTriggerListener(this);
				evaluator.setEventTrigger(AbstractInterpreterEventTrigger.newNullEventTrigger());
				evaluator.setCurrentEnvt(evalEnv);

				Result<IValue> result = evaluator.eval(evaluator.getMonitor(), expr, DEBUGGER_PROMPT_LOCATION);

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
	    ISourceLocation breakpointLocation = (ISourceLocation) message.getPayload();
  
	    switch (message.getAction()) {
	    case SET:
	      addBreakpoint(breakpointLocation);
	      break;

	    case DELETE:
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
