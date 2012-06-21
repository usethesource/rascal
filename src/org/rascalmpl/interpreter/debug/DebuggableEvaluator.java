/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;

public class DebuggableEvaluator extends Evaluator {
	protected final IDebugger debugger;

	//when there is a suspend request from the debugger (caused by the pause button)
	private boolean suspendRequest;

	private DebugStepMode stepMode = DebugStepMode.NO_STEP;

	public DebuggableEvaluator(IValueFactory vf, PrintWriter stderr, PrintWriter stdout,
			ModuleEnvironment moduleEnvironment, IDebugger debugger, GlobalEnvironment heap) {
		super(vf, stderr, stdout, moduleEnvironment, heap);
		this.debugger = debugger;
	}


//	@Override
//	public Result<IValue> visitExpressionCallOrTree(CallOrTree x) {
//		suspend(x);
//		if (stepMode.equals(DebugStepMode.STEP_OVER)) {
//			/* desactivate the stepping mode when evaluating the call */
//			setStepMode(DebugStepMode.NO_STEP);
//			Result<IValue> res = super.visitExpressionCallOrTree(x);
//			setStepMode(DebugStepMode.STEP_OVER);
//			// suspend when returning to the calling statement
//			suspend(x);
//			return res;
//		}
//		
//		Result<IValue> res = super.visitExpressionCallOrTree(x);
//		// suspend when returning to the calling statement
//		suspend(x);
//		return res;
//	}
//
////	@Override
////	public Result<IValue> visitExpressionEnumeratorWithStrategy(
////			EnumeratorWithStrategy x) {
////		suspend(x);
////		return super.visitExpressionEnumeratorWithStrategy(x);
////	}
//	
//	@Override
//	public Result<IValue> visitStatementNonEmptyBlock(Statement.NonEmptyBlock x) {
//		/* no need to supend on a block */
//		//suspend(x);
//		return super.visitStatementNonEmptyBlock(x);
//	}
//	
//	@Override
//	public Result<IValue> visitExpressionNonEmptyBlock(NonEmptyBlock x) {
//		/* no need to supend on a block */
//		//suspend(x);
//		return super.visitExpressionNonEmptyBlock(x);
//	}
//
//	@Override
//	public Result<IValue> visitStatementExpression(Expression x) {
//		/**		
//		 //should avoid to stop twice when stepping into
//		if (! stepMode.equals(DebugStepMode.STEP_INTO)) {
//			suspend(x);
//		}
//		*/
//		return super.visitStatementExpression(x);
//	}
	

	private void suspend(AbstractAST x) {
		if (debugger.isTerminated()) {
			//can happen when we evaluating a loop for example and the debugger is stopped
			throw new QuitException();
		}
		if(suspendRequest) {
			setCurrentAST(x);
			debugger.notifySuspend(DebugSuspendMode.CLIENT_REQUEST);
			suspendRequest = false;
		} else if (debugger.isStepping()) {
			switch (stepMode) {
			case STEP_INTO:
				setCurrentAST(x);
				debugger.notifySuspend(DebugSuspendMode.STEP_END);
				break;
			case STEP_OVER:
				if (x instanceof Statement) {
					setCurrentAST(x);
					debugger.notifySuspend(DebugSuspendMode.STEP_END);
				}
			default:
				break;
			}
		} else if (debugger.hasEnabledBreakpoint(getCurrentAST().getLocation())) {
			setCurrentAST(x);
			debugger.notifySuspend(DebugSuspendMode.BREAKPOINT);
		}
	}

	/** 
	 * this method is called when the debugger send a suspend request 
	 * correspond to a suspend event from the client
	 * 
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

}
