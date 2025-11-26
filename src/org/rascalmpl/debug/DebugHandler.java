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
import org.rascalmpl.semantics.dynamic.Statement.For;
import org.rascalmpl.semantics.dynamic.Statement.Switch;
import org.rascalmpl.semantics.dynamic.Statement.Visit;
import org.rascalmpl.semantics.dynamic.Statement.While;

import io.usethesource.vallang.ISourceLocation;

public final class DebugHandler implements IDebugHandler {

	private AbstractInterpreterEventTrigger eventTrigger;

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
