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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;
import org.rascalmpl.uri.project.ProjectURIResolver;
import org.rascalmpl.uri.project.TargetURIResolver;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class Eval {
	private final IRascalValueFactory values;

	private final TypeReifier tr;
	private final TypeFactory tf = TypeFactory.getInstance();
	private final TypeStore store = new TypeStore();
	private final Type param = tf.parameterType("T");
	public final Type Result = tf.abstractDataType(store, "Result", param);
	public final Type TypeTyp = RascalTypeFactory.getInstance().reifiedType(param);
	public final Type Result_void = tf.constructor(store, Result, "ok");
	public final Type Result_value = tf.constructor(store, Result, "result", param, "val");
	public final Type Exception = tf.abstractDataType(store, "Exception");
	public final Type Exception_StaticError = tf.constructor(store, Exception, "StaticError", tf.stringType(), "messages", tf.sourceLocationType(), "location");
	private final Type resetType = tf.functionType(tf.voidType(), tf.tupleEmpty(), tf.tupleEmpty());
	private final Type setTimeoutType = tf.functionType(tf.voidType(), tf.tupleType(tf.integerType()), tf.tupleEmpty());
	private final Type evalType = tf.functionType(Result_value, tf.tupleType(TypeTyp, tf.stringType()), tf.tupleEmpty());
	private final Type staticTypeOfType = tf.functionType(TypeTyp.instantiate(Map.of(param, tf.valueType())), tf.tupleType(tf.stringType()), tf.tupleEmpty());
	private final Type execConstructor;

	/* the following four fields are inherited by the configuration of nested evaluators */
	private final PrintWriter stderr;
	private final PrintWriter stdout;
	private final Reader input;
	private final IDEServices services;
			
	public Eval(IRascalValueFactory values, PrintWriter out, PrintWriter err, Reader in, ClassLoader loader, IDEServices services, TypeStore ts) {
		super();
		this.values = values;
		this.tr = new TypeReifier(values);
		this.stderr = err;
		this.stdout = out;
		this.input = in;
		this.services = services;
		execConstructor = ts.lookupConstructor(ts.lookupAbstractDataType("RascalRuntime"), "evaluator").iterator().next();
	}
	
	public IConstructor createRascalRuntime(IConstructor pathConfigCons) {
		try {
			PathConfig pcfg = new PathConfig(pathConfigCons);
			RascalRuntime runtime = new RascalRuntime(pcfg, input, stderr, stdout, services);

			return values.constructor(execConstructor,
				pathConfigCons,
				buildResetFunction(runtime),
				buildEvalFunction(runtime),
				buildStaticTypeOfFunction(runtime),
				buildSetTimeOutFunction(runtime)
			);
		}
		catch (IOException | URISyntaxException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()));
		}
	}

	private IFunction buildResetFunction(RascalRuntime exec) {
		return values.function(resetType, (args, kwargs) -> {
			exec.reset();
			return null;
		});
	}

	private IFunction buildSetTimeOutFunction(RascalRuntime exec) {
		return values.function(setTimeoutType, (args, kwargs) -> {
			exec.setTimeout(((IInteger) args[0]).intValue());
			return null;
		});
	}

	private IFunction buildStaticTypeOfFunction(RascalRuntime exec) {
		return values.function(staticTypeOfType, (args, kwargs) -> {
			var duration = exec.getTimeoutDuration();

			EvaluatorInterruptTimer timer = new EvaluatorInterruptTimer(exec.eval, duration);

			IString command = (IString) args[0];
			if (!command.getType().isString()) {
				throw new MatchFailed();
			}

			try {	
				if (duration > 0) {
					timer.start();
				}
				
				return exec.staticTypeOf(command.getValue());
			}
			catch (StaticError e) {
				throw new Throw(values.constructor(Exception_StaticError, values.string(e.getMessage()), e.getLocation()), null, null);
			}
			catch (InterruptException e) {
				throw RuntimeExceptionFactory.timeout(null, null);
			}
			finally {
				// very necessary to clean up the timer thread
				timer.cancel();
			}
		});
	}

	private IFunction buildEvalFunction(RascalRuntime exec) {
		return values.function(evalType, (args, kwargs) -> {    
			var duration = exec.getTimeoutDuration();

			EvaluatorInterruptTimer timer = new EvaluatorInterruptTimer(exec.eval, duration);

			IConstructor expected = (IConstructor) args[0];
			if (!(expected.getType().getName().equals("type"))) {
				throw new MatchFailed();
			}

			IString command = (IString) args[1];
			if (!command.getType().isString()) {
				throw new MatchFailed();
			}

			try {	
				if (duration > 0) {
					timer.start();
				}
				Result<?> result = exec.eval(services, command.getValue());

				Type typ = tr.valueToType(expected);
				if (!result.getStaticType().isSubtypeOf(typ)) {
					throw new UnexpectedType(typ, result.getStaticType(), URIUtil.rootLocation("eval"));
				}

				if (result.getStaticType().isBottom()) {
					return values.constructor(Result_void);
				}
				else {
					Map<Type,Type> bindings = new HashMap<Type,Type>();
					bindings.put(param, result.getStaticType());
					return values.constructor(Result_value.instantiate(bindings), result.getValue());
				}
			}
			catch (StaticError e) {
				throw new Throw(values.constructor(Exception_StaticError, values.string(e.getMessage()), e.getLocation()), null, null);
			}
			catch (InterruptException | InterruptedException e) {
				throw RuntimeExceptionFactory.timeout(null, null);
			}
			catch (IOException e) {
				throw RuntimeExceptionFactory.io(values.string(e.getMessage()));
			}
			finally {
				// very necessary to clean up the timer thread
				timer.cancel();
			}
		});
	}

	/**
	 * A RascalRuntime is an evaluator that configures itself from a PathConfig instance,
	 * and then offers the `eval` method, and the `reset` method to interact it.
	 */
	private static class RascalRuntime {
		private final Evaluator eval;
		private int duration = -1;
		
		public RascalRuntime(PathConfig pcfg, Reader input, PrintWriter stderr, PrintWriter stdout, IDEServices services) throws IOException, URISyntaxException{
			GlobalEnvironment heap = new GlobalEnvironment();
			ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
			IValueFactory vf = ValueFactoryFactory.getValueFactory();
			this.eval = new Evaluator(vf, input, stderr, stdout, root, heap, services);

			eval.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
			eval.setMonitor(services);        
			eval.getConfiguration().setRascalJavaClassPathProperty(PathConfig.resolveCurrentRascalRuntimeJar().getPath());
			eval.setMonitor(services);

			if (!pcfg.getSrcs().isEmpty()) {
				ISourceLocation projectRoot = inferProjectRoot((ISourceLocation) pcfg.getSrcs().get(0));
				URIResolverRegistry reg = URIResolverRegistry.getInstance();
				reg.registerLogical(new ProjectURIResolver(projectRoot));
				reg.registerLogical(new TargetURIResolver(projectRoot));
			}

			for (IValue path : pcfg.getSrcs()) {
				eval.addRascalSearchPath((ISourceLocation) path); 
			}

			for (IValue path : pcfg.getLibs()) {
				eval.addRascalSearchPath((ISourceLocation) path);
			}

			ClassLoader cl = new SourceLocationClassLoader(pcfg.getLibsAndTarget(), ShellEvaluatorFactory.class.getClassLoader());
			eval.addClassLoader(cl);
		}

		public IValue staticTypeOf(String line) {
			var result = eval.eval(null, line, IRascalValueFactory.getInstance().sourceLocation(URIUtil.assumeCorrect("eval", "", "", "command=" + line)));
			var type = result.getStaticType();
			return new TypeReifier(eval.getValueFactory()).typeToValue(type, new TypeStore(), eval.getValueFactory().map());
		}

		public void setTimeout(int duration) {
			this.duration = duration;
		}
		
		public int getTimeoutDuration() {
			return duration;
		}

		private static ISourceLocation inferProjectRoot(ISourceLocation member) {
			ISourceLocation current = member;
			URIResolverRegistry reg = URIResolverRegistry.getInstance();
			while (current != null && reg.exists(current) && reg.isDirectory(current)) {
				if (reg.exists(URIUtil.getChildLocation(current, "META-INF/RASCAL.MF"))) {
					return current;
				}

				if (URIUtil.getParentLocation(current).equals(current)) {
					// we went all the way up to the root
					return reg.isDirectory(member) ? member : URIUtil.getParentLocation(member);
				}
				
				current = URIUtil.getParentLocation(current);
			}

			return current;
		}
	
		public void reset() {
			eval.getCurrentModuleEnvironment().reset();
			eval.getHeap().clear();
		}

		public Result<IValue> eval(IRascalMonitor monitor, String line) throws InterruptedException, IOException {
			return eval.eval(monitor, line, IRascalValueFactory.getInstance().sourceLocation(URIUtil.assumeCorrect("eval", "", "", "command=" + line)));
		}
	}

	/**
	 * This Thread class counts 10 steps towards overflowing the timeout,
	 * and then stops while informing the given Evaluator object to interrupt
	 * its running program.
	 */
	public static class EvaluatorInterruptTimer extends Thread {
		private final Evaluator eval;
		private final int timeout;
		private final int sample;
		private volatile boolean running;
		
		public EvaluatorInterruptTimer(Evaluator eval, int timeout) {
			super();
			setDaemon(true);
			this.eval = eval;
			// this.elapsed = 0;
			this.timeout = timeout;
			this.sample = java.lang.Math.max(timeout / 10, 1);
			running = true;
		}

		public void run() {
			running = true;
			var elapsed = 0;
			while (running) {
				try {
					sleep(sample);
				} catch (InterruptedException e) {
					// ignore
				}
				
				elapsed += sample;
				if (elapsed > timeout && running) {
					running = false;
					eval.interrupt();
				}
			}
		}

		public void cancel() {
			running = false;
		}
	}
}

