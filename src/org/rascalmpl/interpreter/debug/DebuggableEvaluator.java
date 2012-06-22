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

import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Command;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;

/**
 * Extension of {@link Evaluator} that enables debugging.
 * 
 * TODO: remove this class an; capture debugging state in a separate class that
 * becomes a field in {@link Evaluator};
 */
public class DebuggableEvaluator extends Evaluator {
	protected final IDebugger debugger;

	//when there is a suspend request from the debugger (caused by the pause button)
	private boolean suspendRequest;

	private DebugStepMode stepMode = DebugStepMode.NO_STEP;
	
	private AbstractAST referenceAST = null;
	private Integer referenceEnvironmentStackSize = null;

	public DebuggableEvaluator(IValueFactory vf, PrintWriter stderr, PrintWriter stdout,
			ModuleEnvironment moduleEnvironment, IDebugger debugger, GlobalEnvironment heap) {
		super(vf, stderr, stdout, moduleEnvironment, heap);
		this.debugger = debugger;
	}

	protected void updateSteppingState(AbstractAST currentAST) {
		setCurrentAST(currentAST); 
		referenceAST = currentAST;
		referenceEnvironmentStackSize = this.getCallStack().size();		
	}
	
	/* (non-Javadoc)
	 * @see org.rascalmpl.interpreter.IEvaluator#suspend(org.rascalmpl.ast.AbstractAST)
	 */
	@Override
	public void suspend(AbstractAST currentAST) {
		
		if (debugger.isTerminated()) {
			//can happen when we evaluating a loop for example and the debugger is stopped
			throw new QuitException();
		}
		
		if(suspendRequest) {
			
			updateSteppingState(currentAST);			
			debugger.notifySuspend(DebugSuspendMode.CLIENT_REQUEST);
			
			suspendRequest = false;
			
		} else if (debugger.isStepping()) {
			switch (stepMode) {
			
			case STEP_INTO:
				updateSteppingState(currentAST);
				debugger.notifySuspend(DebugSuspendMode.STEP_END);
				break;
				
			case STEP_OVER:
				int currentEnvironmentStackSize = this.getCallStack().size();

				/*
				 * Stepping over implies:
				 * * either there is a next statement in the same environment stack frame
				 * * or there is no next statement in the same stack frame and thus the stack frame 
				 *   eventually gets popped from the stack. As long the calls in deeper nesting levels 
				 *   are executed, no action needs to be taken.
				 */	
				if (currentEnvironmentStackSize <= referenceEnvironmentStackSize) {
					
					/*
					 * For the case that we are still within the same stack frame, positions are compared to
					 * ensure that the statement was finished executing.
					 */					
					int referenceStart = referenceAST.getLocation().getOffset();
					int referenceEnd = referenceAST.getLocation().getOffset() + referenceAST.getLocation().getLength();
					int currentStart = currentAST.getLocation().getOffset();
					
					if (! (referenceStart <= currentStart && currentStart <= referenceEnd)) {
						updateSteppingState(currentAST);
						debugger.notifySuspend(DebugSuspendMode.STEP_END);
					}
				}
				break;
				
			default:
				break;
			
			}
		} else if (debugger.hasEnabledBreakpoint(getCurrentAST().getLocation())) {
			updateSteppingState(currentAST);
			debugger.notifySuspend(DebugSuspendMode.BREAKPOINT);
		}
	}

	/** 
	 * this method is called when the debugger send a suspend request 
	 * correspond to a suspend event from the client
	 * */
	public void suspendRequest() {
		// the evaluator will suspend itself at the next call of suspend or suspend Expression
		suspendRequest = true;
	}

	public void setStepMode(DebugStepMode mode) {
		stepMode = mode;
	}

	public IDebugger getDebugger() {
		return debugger;
	}

	public IConstructor parseCommand(IRascalMonitor monitor, String command){
		return parseCommand(monitor, command, URI.create("debug:///"));
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.interpreter.Evaluator#eval(org.rascalmpl.ast.Statement)
	 */
	@Override
	public Result<IValue> eval(Statement stat) {
		Result<IValue> result = super.eval(stat);
		
		setStepMode(DebugStepMode.NO_STEP);
		debugger.stopStepping();
		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.interpreter.Evaluator#eval(org.rascalmpl.interpreter.IRascalMonitor, java.lang.String, java.net.URI)
	 */
	@Override
	public Result<IValue> eval(IRascalMonitor monitor, String command,
			URI location) {
		Result<IValue> result = super.eval(monitor, command, location);
		
		setStepMode(DebugStepMode.NO_STEP);
		debugger.stopStepping();
		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.interpreter.Evaluator#eval(org.rascalmpl.interpreter.IRascalMonitor, org.rascalmpl.ast.Command)
	 */
	@Override
	public Result<IValue> eval(IRascalMonitor monitor, Command command) {
		Result<IValue> result = super.eval(monitor, command);

		setStepMode(DebugStepMode.NO_STEP);
		debugger.stopStepping();
		
		return result;	
	}

}
