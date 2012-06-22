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
	
	protected final DebuggingHandler debuggingHandler;
	
	public DebuggableEvaluator(IValueFactory vf, PrintWriter stderr, PrintWriter stdout,
			ModuleEnvironment moduleEnvironment, IDebugger debugger, GlobalEnvironment heap) {
		super(vf, stderr, stdout, moduleEnvironment, heap);
		this.debuggingHandler = new DebuggingHandler(debugger);
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.interpreter.IEvaluator#suspend(org.rascalmpl.ast.AbstractAST)
	 * 
	 * TODO: Remove indirection and delegation.
	 */
	@Override
	public void suspend(AbstractAST currentAST) {		
		debuggingHandler.suspended(this, currentAST);
	}

	/* 
	 * this method is called when the debugger send a suspend request 
	 * correspond to a suspend event from the client
	 * 
	 * TODO: Remove indirection and delegation.
	 */
	public void suspendRequest() {
		// the evaluator will suspend itself at the next call of suspend or suspend Expression
		debuggingHandler.requestSuspend();
	}

	/*
	 * TODO: Remove indirection and delegation.
	 */
	public void setStepMode(DebugStepMode mode) {
		debuggingHandler.setStepMode(mode);
	}

	/*
	 * TODO: Remove indirection and delegation.
	 */
	public IDebugger getDebugger() {
		return debuggingHandler.getDebugger();
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
		
		debuggingHandler.stopStepping();
		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.interpreter.Evaluator#eval(org.rascalmpl.interpreter.IRascalMonitor, java.lang.String, java.net.URI)
	 */
	@Override
	public Result<IValue> eval(IRascalMonitor monitor, String command,
			URI location) {
		Result<IValue> result = super.eval(monitor, command, location);
		
		debuggingHandler.stopStepping();
		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.interpreter.Evaluator#eval(org.rascalmpl.interpreter.IRascalMonitor, org.rascalmpl.ast.Command)
	 */
	@Override
	public Result<IValue> eval(IRascalMonitor monitor, Command command) {
		Result<IValue> result = super.eval(monitor, command);

		debuggingHandler.stopStepping();
		
		return result;	
	}

}
