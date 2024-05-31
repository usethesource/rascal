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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.ast.Expression.ReifiedType;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.Result;
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
import io.usethesource.vallang.IList;
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
	private final Type evalType = tf.functionType(Result_value, tf.tupleType(TypeTyp, tf.stringType()), tf.tupleEmpty());
	private final Type execConstructor;

	/* the following four fields are inherited by the configuration of nested evaluators */
	private final OutputStream stderr;
	private final OutputStream stdout;
	private final InputStream input;
	private final IDEServices services;
			
	public Eval(IRascalValueFactory values, OutputStream out, OutputStream err, InputStream in, ClassLoader loader, IDEServices services, TypeStore ts) {
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
				buildEvalFunction(runtime)
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

	private IFunction buildEvalFunction(RascalRuntime exec) {
		return values.function(evalType, (args, kwargs) -> {    
			var durInt = (IInteger) (kwargs.containsKey("duration") ? kwargs.get("duration") : values.integer(-1));

			EvaluatorInterruptTimer timer = new EvaluatorInterruptTimer(exec.eval, durInt.intValue());

			try {	
				IConstructor expected = (IConstructor) args[0];
				if (!(expected.getType().getName().equals("type"))) {
					throw new MatchFailed();
				}

				IString command = (IString) args[1];
				if (!command.getType().isString()) {
					throw new MatchFailed();
				}

				if (durInt.intValue() > 0) {
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
		
		public RascalRuntime(PathConfig pcfg, InputStream input, OutputStream stderr, OutputStream stdout, IDEServices services) throws IOException, URISyntaxException{
			GlobalEnvironment heap = new GlobalEnvironment();
			ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
			IValueFactory vf = ValueFactoryFactory.getValueFactory();
			this.eval = new Evaluator(vf, input, stderr, stdout, root, heap, services);

			eval.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
			eval.setMonitor(services);        
			eval.getConfiguration().setRascalJavaClassPathProperty(javaCompilerPathAsString(pcfg.getJavaCompilerPath()));
			eval.setMonitor(services);

			if (!pcfg.getSrcs().isEmpty()) {
				ISourceLocation projectRoot = inferProjectRoot((ISourceLocation) pcfg.getSrcs().get(0));
				String projectName = new RascalManifest().getProjectName(projectRoot);
				URIResolverRegistry reg = URIResolverRegistry.getInstance();
				reg.registerLogical(new ProjectURIResolver(projectRoot, projectName));
				reg.registerLogical(new TargetURIResolver(projectRoot, projectName));
			}

			for (IValue path : pcfg.getSrcs()) {
				eval.addRascalSearchPath((ISourceLocation) path); 
			}

			for (IValue path : pcfg.getLibs()) {
				eval.addRascalSearchPath((ISourceLocation) path);
			}

			ClassLoader cl = new SourceLocationClassLoader(pcfg.getClassloaders(), ShellEvaluatorFactory.class.getClassLoader());
			eval.addClassLoader(cl);
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
		
		private String javaCompilerPathAsString(IList javaCompilerPath) {
			StringBuilder b = new StringBuilder();

			for (IValue elem : javaCompilerPath) {
				ISourceLocation loc = (ISourceLocation) elem;

				if (b.length() != 0) {
					b.append(File.pathSeparatorChar);
				}

				// this is the precondition
				assert loc.getScheme().equals("file");

				// this is robustness in case of experimentation in pom.xml
				if ("file".equals(loc.getScheme())) {
					b.append(Paths.get(loc.getURI()).toAbsolutePath().toString());
				}
			}

			return b.toString();
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
		private int elapsed;
		private final int sample;
		private volatile boolean running;
		
		public EvaluatorInterruptTimer(Evaluator eval, int timeout) {
			super();
			this.eval = eval;
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
					eval.interrupt();
				}
			}
		}

		public void cancel() {
			running = false;
		}
	}
}

