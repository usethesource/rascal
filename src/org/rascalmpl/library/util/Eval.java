/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class Eval {
	private final IValueFactory values;
		
	private final IInteger duration;
	private final Evaluator eval;
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
 

			
	public Eval(IValueFactory values, OutputStream out, OutputStream err, InputStream in, ClassLoader loader) {
		super();
		this.values = values;
		this.tr = new TypeReifier(values);
		this.duration = values.integer(1000*100); // default duration for eval
		
		GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment root = new ModuleEnvironment("$eval$", heap);
        this.eval = new Evaluator(values, in, err, out, root, heap);
        
        // TODO: this is to fix the course Question compiler with a workaround.
        // it would be better to parameterize eval with a PathConfig.
        this.eval.addRascalSearchPath(URIUtil.rootLocation("std"));
        //          this.eval.getConfiguration().setRascalJavaClassPathProperty(ctx.getConfiguration().getRascalJavaClassPathProperty());
	}

	private ModuleEnvironment getUniqueModuleEnvironment() {
		return new ModuleEnvironment("$evalinstance$" + evalCount++ , eval.getHeap());
	}

	public IValue eval (IValue typ, IString input, IInteger duration) {
		Result<IValue> result = doEval(typ, ValueFactoryFactory.getValueFactory().list(input), duration, true);
		
		if(result.getStaticType().isBottom()){
		  return values.constructor(Result_void);
		}
		else {
			Map<Type,Type> bindings = new HashMap<Type,Type>();
			bindings.put(param, result.getStaticType());
			return values.constructor(Result_value.instantiate(bindings), result.getValue());
		}
	}
	
	public IValue eval (IValue typ, IString input) {
		return eval(typ, input, duration);
	}

	public IValue eval (IValue typ, IList commands, IInteger duration) {
		Result<IValue> result = doEval(typ, commands, duration, true);
		
		if(result.getStaticType().isBottom()){
		//if (result.getType().isSubtypeOf(TypeFactory.getInstance().voidType())) {
			return values.constructor(Result_void);
		}
		else {
			Map<Type,Type> bindings = new HashMap<Type,Type>();
			bindings.put(param, result.getStaticType());
			return values.constructor(Result_value.instantiate(bindings), result.getValue());
		}
	}
	
	public IValue eval (IValue typ, IList commands) {
		return eval(typ, commands, duration);
	}
	
	public IValue evalType (IString input, IInteger duration) {
		Result<IValue> result =  doEval(null, values.list(input), duration, true);
		// Make sure redundant spaces are removed from the type.
		return values.string(result.getStaticType().toString().replaceAll(" ", ""));
	}
	
	public IValue evalType (IString input) {
		return evalType(input, duration);
	}
	
	public IValue evalType (IList commands, IInteger duration) {
		Result<IValue> result = doEval(null, commands, duration, true);
		return values.string(result.getStaticType().toString().replaceAll(" ", ""));
	}
	
	public IValue evalType (IList commands) {
		return evalType(commands, duration);
	}
	
	public Result<IValue> doEval (IValue expected, IList commands, IInteger duration, boolean forRascal) {
		IEvaluator<Result<IValue>> evaluator = eval;
		EvalTimer timer = new EvalTimer(evaluator, duration.intValue());

		Result<IValue> result = null;
		Environment old = evaluator.getCurrentEnvt();
		ModuleEnvironment env = getUniqueModuleEnvironment();
		
		try {
			timer.start();
			
			evaluator.setCurrentEnvt(env);
			if(!timer.hasExpired() && commands.length() > 0){
				for(IValue command : commands){
					ISourceLocation commandLocation = eval.getValueFactory().sourceLocation("eval", "", "/","command=" + ((IString)command).getValue(), null);
					result = evaluator.evalMore(null, ((IString) command).getValue(), commandLocation);
				}
				timer.cancel();
				if (timer.hasExpired()) {
					throw RuntimeExceptionFactory.timeout(null, null);
				}
				
				if (expected != null) {
					Type typ = tr.valueToType((IConstructor) expected);
					if (!result.getStaticType().isSubtypeOf(typ)) {
						throw new UnexpectedType(typ, result.getStaticType(), URIUtil.rootLocation("eval"));
					}
				}
				return result;
			}
		}
		catch (ParseError e) {
			if (forRascal) 
				throw RuntimeExceptionFactory.parseError(e.getLocation(), null, null);
			throw e;
		}
		catch (StaticError e) {
			if (forRascal) {
			  throw new Throw(values.constructor(Exception_StaticError, values.string(e.getMessage()), e.getLocation()), (ISourceLocation) null, null);
			}
			throw e;
		} 
		catch (URISyntaxException e) {
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

