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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

public class Eval {
	private final IValueFactory values;
		
	private final IInteger duration;
	private Evaluator eval;
	private int evalCount = 0;

	private final TypeReifier tr;
	private final TypeFactory tf = TypeFactory.getInstance();
	private final TypeStore store = new TypeStore();
	private final Type param = tf.parameterType("T");
	public final Type Result = tf.abstractDataType(store, "Result", param);
	public final Type Result_void = tf.constructor(store, Result, "ok");
	public final Type Result_value = tf.constructor(store, Result, "result", param, "val");
	public final Type Exception = tf.abstractDataType(store, "Exception");
	public final Type Exception_StaticError = tf.constructor(store, Exception, "StaticError", tf.stringType(), "messages", tf.sourceLocationType(), "location");
			
	public Eval(IValueFactory values){
		super();
		this.values = values;
		this.tr = new TypeReifier(values);
		duration = values.integer(1000*100); // default duration for eval
	}

	private ModuleEnvironment getUniqueModuleEnvironment(IEvaluatorContext ctx) {
		ModuleEnvironment mod = new ModuleEnvironment("___EVAL_INSTANCE___" + evalCount++ , ctx.getHeap());
		return mod;
	}

	private Evaluator getSharedEvaluator(IEvaluatorContext ctx) {
		if (this.eval == null) {
			GlobalEnvironment heap = new GlobalEnvironment();
			ModuleEnvironment root = new ModuleEnvironment("___EVAL___", heap);
			this.eval = new Evaluator(ctx.getValueFactory(), ctx.getStdErr(), ctx.getStdOut(), root, heap, ctx.getEvaluator().getClassLoaders(), ctx.getEvaluator().getRascalResolver());
		}
		
		return this.eval;
	}
	
	
	public IValue eval (IValue typ, IString input, IInteger duration, IEvaluatorContext ctx) {
		Result<IValue> result = doEval(typ, ValueFactoryFactory.getValueFactory().list(input), duration,  getSharedEvaluator(ctx), true);
		
		if (result.getType().isSubtypeOf(TypeFactory.getInstance().voidType())) {
			return Result_void.make(values);
		}
		else {
			Map<Type,Type> bindings = new HashMap<Type,Type>();
			bindings.put(param, result.getType());
			IValue res = Result_value.instantiate(bindings).make(values, result.getValue());
			return res;
		}
	}
	
	public IValue eval (IValue typ, IString input, IEvaluatorContext ctx) {
		return eval(typ, input, duration,  getSharedEvaluator(ctx));
	}

	public IValue eval (IValue typ, IList commands, IInteger duration, IEvaluatorContext ctx) {
		Result<IValue> result = doEval(typ, commands, duration,  getSharedEvaluator(ctx), true);
		
		if (result.getType().isSubtypeOf(TypeFactory.getInstance().voidType())) {
			return Result_void.make(values);
		}
		else {
			Map<Type,Type> bindings = new HashMap<Type,Type>();
			bindings.put(param, result.getType());
			return Result_value.instantiate(bindings).make(values, result.getValue());
		}
	}
	
	public IValue eval (IValue typ, IList commands, IEvaluatorContext ctx) {
		return eval(typ, commands, duration,  getSharedEvaluator(ctx));
	}
	
	public IValue evalType (IString input, IInteger duration, IEvaluatorContext ctx) {
		Result<IValue> result =  doEval(null, values.list(input), duration,  getSharedEvaluator(ctx), true);
		// Make sure redundant spaces are removed from the type.
		return values.string(result.getType().toString().replaceAll(" ", ""));
	}
	
	public IValue evalType (IString input, IEvaluatorContext ctx) {
		return evalType(input, duration,  getSharedEvaluator(ctx));
	}
	
	public IValue evalType (IList commands, IInteger duration, IEvaluatorContext ctx) {
		Result<IValue> result = doEval(null, commands, duration,  getSharedEvaluator(ctx), true);
		return values.string(result.getType().toString().replaceAll(" ", ""));
	}
	
	public IValue evalType (IList commands, IEvaluatorContext ctx) {
		return evalType(commands, duration, getSharedEvaluator(ctx));
	}
	
	public Result<IValue> doEval (IValue expected, IList commands, IInteger duration, IEvaluatorContext ctx, boolean forRascal) {
		IEvaluator<Result<IValue>> evaluator = ctx.getEvaluator();
		EvalTimer timer = new EvalTimer(evaluator, duration.intValue());

		Result<IValue> result = null;
		Environment old = evaluator.getCurrentEnvt();
		ModuleEnvironment env = getUniqueModuleEnvironment(evaluator);
		
		try {
			timer.start();
			
			evaluator.setCurrentEnvt(env);
			if(!timer.hasExpired() && commands.length() > 0){
				for(IValue command : commands){
					URI commandLocation = URIUtil.create("eval", "", "/","command=" + ((IString)command).getValue(), null);
					result = evaluator.evalMore(null, ((IString) command).getValue(), commandLocation);
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
		catch (ParseError e) {
			if (forRascal) 
				throw RuntimeExceptionFactory.parseError(values.sourceLocation(e.getLocation(), e.getOffset(), e.getLength(), e.getBeginLine(), e.getEndLine(), e.getBeginColumn(), e.getEndColumn()), null, null);
			throw e;
		}
		catch (StaticError e) {
			if (forRascal)
				throw new Throw(Exception_StaticError.make(values, values.string(e.getMessage()), e.getLocation()), (ISourceLocation) null, (String) null);
			throw e;
		} catch (URISyntaxException e) {
			// this should never happen
			if (forRascal)
				throw RuntimeExceptionFactory.illegalArgument(commands, null, null);
			throw new RuntimeException(e.getMessage(), e);
		}
		finally {
			evaluator.getHeap().removeModule(env);
			evaluator.setCurrentEnvt(old);
		}
		
		if (forRascal) 
			throw RuntimeExceptionFactory.illegalArgument(commands, null, null);
		
		throw new IllegalArgumentException();
	}
	

	public static class Timer extends Thread {
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

	public static class EvalTimer extends Timer {
		private IEvaluator<Result<IValue>> eval;

		public EvalTimer(IEvaluator<Result<IValue>> eval, int timeout) {
			super(timeout);
			this.eval = eval;
		}
		
		public void timeout(){
			eval.interrupt();
		}
	}
}

