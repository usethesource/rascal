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

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.QuitException;

public final class DebuggingHandler implements ISuspendable {

	private final IDebugger debugger;

	/**
	 * Indicates a manual suspend request from the debugger, e.g. caused by a pause action in the GUI.
	 */
	private boolean suspendRequested;

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

	public DebuggingHandler(IDebugger debugger) {
		this.debugger = debugger;
	}
	
	protected void clearSuspensionState() {
		setReferenceAST(null);
		setReferenceEnvironmentStackSize(null);
	}	
	
	protected void updateSuspensionState(IEvaluator<?> evaluator, AbstractAST currentAST) {
		setReferenceAST(currentAST);
		// TODO: remove cast to {@link Evaluator} and rework {@link IEvaluator}.
		setReferenceEnvironmentStackSize(((Evaluator) evaluator).getCallStack().size());
	}
	
	@Override
	public void suspend(IEvaluator<?> evaluator, AbstractAST currentAST) {
		// TODO: Remove exception throwing herein.
		if (debugger.isTerminated()) {
			//can happen when we evaluating a loop for example and the debugger is stopped
			throw new QuitException();
		}
		
		if(isSuspendRequested()) {
			
			updateSuspensionState(evaluator, currentAST);			
			debugger.notifySuspend(DebugSuspendMode.CLIENT_REQUEST);
			
			setSuspendRequested(false);
			
		} else if (debugger.isStepping()) {
			switch (stepMode) {
			
			case STEP_INTO:
				updateSuspensionState(evaluator, currentAST);
				debugger.notifySuspend(DebugSuspendMode.STEP_END);
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
					
					if (! (referenceStart <= currentStart && currentStart <= referenceEnd)) {
						updateSuspensionState(evaluator, currentAST);
						debugger.notifySuspend(DebugSuspendMode.STEP_END);
					}
				}
				break;
				
			default:
				break;
			
			}
		} else if (debugger.hasEnabledBreakpoint(currentAST.getLocation())) {
			updateSuspensionState(evaluator, currentAST);
			debugger.notifySuspend(DebugSuspendMode.BREAKPOINT);
		}
		
	}	
	
	/** 
	 * this method is called when the debugger send a suspend request 
	 * correspond to a suspend event from the client
	 * */
	public void requestSuspend() {
		// the evaluator will suspend itself at the next call of suspend or suspend Expression
		setSuspendRequested(true);
	}

	public void stopStepping() {
		setStepMode(DebugStepMode.NO_STEP);
		debugger.stopStepping();
	}
	
	public void setStepMode(DebugStepMode mode) {
		stepMode = mode;
	}
	
	/**
	 * @return the debugger associated with this handler object
	 */
	public IDebugger getDebugger() {
		return debugger;
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
		
}
