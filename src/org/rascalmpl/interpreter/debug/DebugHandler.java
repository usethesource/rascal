/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
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
package org.rascalmpl.interpreter.debug;

import java.util.Set;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.AbstractInterpreterEventTrigger;
import org.rascalmpl.interpreter.debug.IDebugMessage.Detail;

import static org.rascalmpl.interpreter.AbstractInterpreterEventTrigger.*;

public final class DebugHandler implements IDebugHandler {

	private AbstractInterpreterEventTrigger eventTrigger;

	private final Set<String> breakpoints = new java.util.HashSet<String>();
			
	/**
	 * Indicates a manual suspend request from the debugger, e.g. caused by a pause action in the GUI.
	 */
	private boolean suspendRequested;

	/**
	 * Indicates that the evaluator is suspended. Also used for suspending / blocking the evaluator.
	 */
	private boolean suspended;
	
	private enum DebugStepMode {
		NO_STEP, STEP_INTO, STEP_OVER 
	};
	
	private DebugStepMode stepMode = DebugStepMode.NO_STEP;
	
	/**
	 * Referring to {@link AbstractAST} responsible for last suspension.
	 */
	private AbstractAST referenceAST = null;	

	/**
	 * Referring to the {@link Environment} stack depth at last suspension suspension.
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
	
	private boolean hasBreakpoint(ISourceLocation breakpointLocation) {
		return breakpoints.contains(breakpointLocation.toString());
	}
	
	private void addBreakpoint(ISourceLocation breakpointLocation) {
		breakpoints.add(breakpointLocation.toString());
	}

	private void removeBreakpoint(ISourceLocation breakpointLocation) {
		breakpoints.remove(breakpointLocation.toString());
	}
	
	protected void clearSuspensionState() {
		setReferenceAST(null);
		setReferenceEnvironmentStackSize(null);
		setSuspended(false);
	}	
	
	protected void updateSuspensionState(IEvaluator<?> evaluator, AbstractAST currentAST) {
		setReferenceAST(currentAST);
		
		// TODO: remove cast to {@link Evaluator} and rework {@link IEvaluator}.
		setReferenceEnvironmentStackSize(((Evaluator) evaluator).getCallStack().size());
		setSuspended(true);
	}
	
	@Override
	public void suspended(IEvaluator<?> evaluator, AbstractAST currentAST) {
		
		if(isSuspendRequested()) {
			
			updateSuspensionState(evaluator, currentAST);
			getEventTrigger().fireSuspendByClientRequestEvent();			
		
			setSuspendRequested(false);
			
		} else {

			switch (getStepMode()) {
			
			case STEP_INTO:
				updateSuspensionState(evaluator, currentAST);
				getEventTrigger().fireSuspendByStepEndEvent();
				break;
				
			case STEP_OVER:
				// TODO: remove cast to {@link Evaluator} and rework {@link IEvaluator}.
				int currentEnvironmentStackSize = ((Evaluator) evaluator).getCallStack().size();

				/*
				 * Stepping over implies:
				 * * either there is a next statement in the same environment stack frame
				 * * or there is no next statement in the same stack frame and thus the stack frame 
				 *   eventually gets popped from the stack. As long the calls in deeper nesting levels 
				 *   are executed, no action needs to be taken.
				 */	
				if (currentEnvironmentStackSize <= getReferenceEnvironmentStackSize()) {
					
					/*
					 * For the case that we are still within the same stack frame, positions are compared to
					 * ensure that the statement was finished executing.
					 */					
					int referenceStart = getReferenceAST().getLocation().getOffset();
					int referenceEnd   = getReferenceAST().getLocation().getOffset() + getReferenceAST().getLocation().getLength();
					int currentStart   = currentAST.getLocation().getOffset();
					
					if (! (referenceStart <= currentStart && currentStart < referenceEnd)) {
						updateSuspensionState(evaluator, currentAST);
						getEventTrigger().fireSuspendByStepEndEvent();
					}
				}
				break;

			case NO_STEP:
				if (hasBreakpoint(currentAST.getLocation())) {
					updateSuspensionState(evaluator, currentAST);
					getEventTrigger().fireSuspendByBreakpointEvent(currentAST.getLocation());
				}
				break;
				
			}
		}
	
		/*
		 * Waiting until GUI triggers end of suspension.
		 */
		while (isSuspended()) {
			try {
				evaluator.wait(50);
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
