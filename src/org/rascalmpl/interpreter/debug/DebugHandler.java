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
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IInterpreterEventListener;
import org.rascalmpl.interpreter.AbstractInterpreterEventTrigger;

import static org.rascalmpl.interpreter.AbstractInterpreterEventTrigger.*;

public final class DebugHandler implements IDebugHandler {

	private AbstractInterpreterEventTrigger eventTrigger;

	private Set<String> breakpoints = new java.util.HashSet<String>();
			
	/**
	 * Indicates a manual suspend request from the debugger, e.g. caused by a pause action in the GUI.
	 */
	private boolean suspendRequested;

	/**
	 * Indicates that the evalutor is suspended. Also used for suspending / blocking the evaluator.
	 */
	private volatile boolean suspended;
	
	private enum DebugStepMode {
		STEP_INTO, STEP_OVER, STEP_RETURN, NO_STEP
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
	
	public DebugHandler() {
		setEventTrigger(newInterpreterEventTrigger(this,
				new CopyOnWriteArrayList<IInterpreterEventListener>()));
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
		suspended = false;
	}	
	
	protected void updateSuspensionState(IEvaluator<?> evaluator, AbstractAST currentAST) {
		setReferenceAST(currentAST);
		
		// TODO: remove cast to {@link Evaluator} and rework {@link IEvaluator}.
		setReferenceEnvironmentStackSize(((Evaluator) evaluator).getCallStack().size());
		suspended = true;
	}
	
	@Override
	public void suspended(IEvaluator<?> evaluator, AbstractAST currentAST) {
		
		if(isSuspendRequested()) {
			
			updateSuspensionState(evaluator, currentAST);
			getEventTrigger().fireSuspendByClientRequestEvent();			
		
			setSuspendRequested(false);
			
//		} else if (debugger.isStepping()) {
		} else if (stepMode != DebugStepMode.NO_STEP) {

			switch (stepMode) {
			
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
				
			default:
				break;
			
			}
		} else if (hasBreakpoint(currentAST.getLocation())) {
			updateSuspensionState(evaluator, currentAST);
			getEventTrigger().fireSuspendByBreakpointEvent(currentAST.getLocation());
		}
	
		/*
		 * Waiting until GUI triggers end of suspension.
		 */
		while (suspended) {
			try {
				evaluator.wait(50);
			} catch (InterruptedException e) {
				// Ignore
			}
		}		
		
	}

	/** 
	 * this method is called when the debugger send a suspend request 
	 * correspond to a suspend event from the client
	 * */
	@Deprecated
	public void requestSuspend() {
		// the evaluator will suspend itself at the next call of suspend or suspend Expression
		setSuspendRequested(true);
		
		// TODO: not the right place. used to stop suspension loop.
		suspended = false;
	}

	/*
	 * TODO: No direct API to say debugger to stop stepping. Message?
	 */
	@Override
	public void stopStepping() {
		setStepMode(DebugStepMode.NO_STEP);
		// debugger.stopStepping();
	}
	
	@Deprecated
	public void setStepMode(DebugStepMode mode) {
		stepMode = mode;

		// TODO: not the right place. used to stop suspension loop.		
		suspended = false;
	}
	
	/**
	 * @return the referenceAST
	 */
	protected AbstractAST getReferenceAST() {
		return referenceAST;
	}

	/**
	 * @param referenceAST the referenceAST to set
	 */
	protected void setReferenceAST(AbstractAST referenceAST) {
		this.referenceAST = referenceAST;
	}

	/**
	 * @return the referenceEnvironmentStackSize
	 */
	protected Integer getReferenceEnvironmentStackSize() {
		return referenceEnvironmentStackSize;
	}

	/**
	 * @param referenceEnvironmentStackSize the referenceEnvironmentStackSize to set
	 */
	protected void setReferenceEnvironmentStackSize(Integer referenceEnvironmentStackSize) {
		this.referenceEnvironmentStackSize = referenceEnvironmentStackSize;
	}

	/**
	 * @return the suspendRequested
	 */
	protected boolean isSuspendRequested() {
		return suspendRequested;
	}

	/**
	 * @param suspendRequested the suspendRequested to set
	 */
	protected void setSuspendRequested(boolean suspendRequested) {
		this.suspendRequested = suspendRequested;
	}

	@Override
	public void addInterpreterEventListener(IInterpreterEventListener listener) {
		getEventTrigger().addInterpreterEventListener(listener);
	}

	@Override
	public void removeInterpreterEventListener(
			IInterpreterEventListener listener) {
		getEventTrigger().removeInterpreterEventListener(listener);
	}

	@Override
	public void processMessage(IDebugMessage message) {
		// TODO Auto-generated method stub
		
	}
		
	/**
	 * @return the eventTrigger
	 */
	public AbstractInterpreterEventTrigger getEventTrigger() {
		return eventTrigger;
	}

	/**
	 * @param eventTrigger the eventTrigger to set
	 */
	public void setEventTrigger(AbstractInterpreterEventTrigger eventTrigger) {
		this.eventTrigger = eventTrigger;
	}
	
}
