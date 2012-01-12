/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Eval {
	private final IValueFactory values;
		
	private final IInteger duration;
	private Evaluator eval;
	private int evalCount = 0;

	private final TypeReifier tr;
	
	public Eval(IValueFactory values){
		super();
		this.values = values;
		this.tr = new TypeReifier(values);
		duration = values.integer(1000*100); // default duration for eval
	}

	private ModuleEnvironment getUniqueModuleEnvironment(Evaluator eval) {
		ModuleEnvironment mod = new ModuleEnvironment("eval" + evalCount , eval.getHeap());
		mod.addImport("Prelude", eval.getHeap().getModule("Prelude"));
		return mod;
	}

	private Evaluator getSharedEvaluator(IEvaluatorContext ctx) {
		if (this.eval == null) {
			GlobalEnvironment heap = new GlobalEnvironment();
			ModuleEnvironment root = new ModuleEnvironment("***eval***", heap);
			this.eval = new Evaluator(ctx.getValueFactory(), ctx.getStdErr(), ctx.getStdOut(), root, heap, ctx.getEvaluator().getClassLoaders(), ctx.getEvaluator().getRascalResolver());
			eval.doImport(null, "Prelude");
		}
		
		return this.eval;
	}
	
	
	public IValue eval (IValue typ, IString input, IInteger duration, IEvaluatorContext ctx) {
		return doEval(typ, ValueFactoryFactory.getValueFactory().list(input), duration, ctx).getValue();
	}
	
	public IValue eval (IValue typ, IString input, IEvaluatorContext ctx) {
		return eval(typ, input, duration, ctx);
	}

	public IValue eval (IValue typ, IList commands, IInteger duration, IEvaluatorContext ctx) {
		return doEval(typ, commands, duration, ctx).getValue();
	}
	
	public IValue eval (IValue typ, IList commands, IEvaluatorContext ctx) {
		return eval(typ, commands, duration, ctx);
	}
	
	public IValue evalType (IString input, IInteger duration, IEvaluatorContext ctx) {
		Result<IValue> result =  doEval(null, values.list(input), duration, ctx);
		// Make sure redundant spaces are removed from the type.
		return values.string(result.getType().toString().replaceAll(" ", ""));
	}
	
	public IValue evalType (IString input, IEvaluatorContext ctx) {
		return evalType(input, duration, ctx);
	}
	
	public IValue evalType (IList commands, IInteger duration, IEvaluatorContext ctx) {
		Result<IValue> result = doEval(null, commands, duration, ctx);
		return values.string(result.getType().toString().replaceAll(" ", ""));
	}
	
	public IValue evalType (IList commands, IEvaluatorContext ctx) {
		return evalType(commands, duration, ctx);
	}
	
	public Result<IValue> doEval (IValue expected, IList commands, IInteger duration, IEvaluatorContext ctx) {
		Evaluator evaluator = getSharedEvaluator(ctx);
		EvalTimer timer = new EvalTimer(evaluator, duration.intValue());

		Result<IValue> result = null;
		Environment old = evaluator.getCurrentEnvt();
		ModuleEnvironment env = getUniqueModuleEnvironment(evaluator);
		
		try {
			timer.start();
			
			if(!timer.hasExpired() && commands.length() > 0){
				for(IValue command : commands){
					result = evaluator.eval(null, ((IString) command).getValue(), URI.create("eval:///?command=" + URLEncoder.encode(((IString) command).getValue(), "UTF8")));
				}
				timer.cancel();
				if (timer.hasExpired()) {
					throw RuntimeExceptionFactory.timeout(null, null);
				}
				
				if (expected != null) {
					Type typ = tr.valueToType((IConstructor) expected);
					if (!result.getType().isSubtypeOf(typ)) {
						throw new UnexpectedTypeError(typ, result.getType(), ctx.getCurrentAST());
					}
				}
				return result;
			}
		}
		catch (UnsupportedEncodingException e) {
			// this should never happen
			throw RuntimeExceptionFactory.illegalArgument(commands, null, null);
		}
		finally {
			evaluator.getHeap().removeModule(env);
			evaluator.setCurrentEnvt(old);
		}
		
		throw RuntimeExceptionFactory.illegalArgument(commands, null, null);
	}
	

	class Timer extends Thread {
		private final int timeout;
		private int elapsed;
		private final int sample;
		private volatile boolean running;

		public Timer(int timeout) {
			super();
			this.elapsed = 0;
			this.timeout = timeout;
			this.sample = timeout / 10;
			running = true;
		}

		public void run() {
			running = true;
			elapsed = 0;
			while (running) {
				try {
					sleep(sample);
				} catch (InterruptedException e) {
					// ignore
				}
				
				elapsed += sample;
				if (elapsed > timeout && running) {
					running = false;
					timeout();
				}
			}
		}

		public void cancel() {
			running = false;
		}

		public boolean hasExpired() {
			return elapsed > timeout;
		}

		public void timeout(){
			System.err.println("Timeout!");
		}
	}

	private class EvalTimer extends Timer {
		private Evaluator eval;

		public EvalTimer(Evaluator eval, int timeout) {
			super(timeout);
			this.eval = eval;
		}
		
		public void timeout(){
			eval.interrupt();
		}
	}
}

