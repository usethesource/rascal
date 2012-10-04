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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Body;
import org.rascalmpl.ast.Header;
import org.rascalmpl.ast.Toplevel;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;

public abstract class Module extends org.rascalmpl.ast.Module {

	static public class Default extends org.rascalmpl.ast.Module.Default {

		public Default(IConstructor __param1, Header __param2, Body __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public String declareSyntax(IEvaluator<Result<IValue>> eval, boolean withImports) {
			String name = eval.getModuleName(this);

			GlobalEnvironment heap = eval.__getHeap();
			ModuleEnvironment env = heap.getModule(name);

			if (env == null) {
				env = new ModuleEnvironment(name, heap);
				heap.addModule(env);
			}

			if (!env.isSyntaxDefined()) {
				env.setBootstrap(eval.needBootstrapParser(this));
				env.setCachedParser(eval.getCachedParser(this));
				Environment oldEnv = eval.getCurrentEnvt();
				eval.setCurrentEnvt(env);
				env.resetProductions();
				env.setSyntaxDefined(true);
				

				try {
					this.getHeader().declareSyntax(eval, withImports);
				}
				catch (RuntimeException e) {
					env.setSyntaxDefined(false);
					throw e;
				}
				finally {
					eval.setCurrentEnvt(oldEnv);
				}
			}

			return Names.fullName(getHeader().getName());
		}
		
		public Result<IValue> interpretInCurrentEnv(Evaluator __eval) {
			String name = __eval.getModuleName(this);
			Environment env = __eval.getCurrentModuleEnvironment();

			// ????
			//env.setBootstrap(__eval.needBootstrapParser(this));
			
			Environment oldEnv = __eval.getCurrentEnvt();
			__eval.setCurrentEnvt(env); // such that declarations end up in
			// the module scope
			try {
				this.getHeader().interpret(__eval);

				List<Toplevel> decls = this.getBody().getToplevels();
				__eval.__getTypeDeclarator().evaluateDeclarations(decls,
						__eval.getCurrentEnvt());

				for (Toplevel l : decls) {
					l.interpret(__eval);
				}
			}
			catch (RuntimeException e) {
				throw e;
			}
			finally {
				__eval.setCurrentEnvt(oldEnv);
			}

			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(
					org.rascalmpl.interpreter.Evaluator.__getTf().stringType(),
					__eval.__getVf().string(name), __eval);

		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			String name = __eval.getModuleName(this);

			GlobalEnvironment heap = __eval.__getHeap();
			ModuleEnvironment env = heap.getModule(name);

			if (env == null) {
				env = new ModuleEnvironment(name, heap);
				heap.addModule(env);
			}

			env.setBootstrap(__eval.needBootstrapParser(this));
			
			if (!env.isInitialized()) {
				env.setInitialized(true);
				Environment oldEnv = __eval.getCurrentEnvt();
				__eval.setCurrentEnvt(env); // such that declarations end up in
				// the module scope
				try {
					this.getHeader().interpret(__eval);

					List<Toplevel> decls = this.getBody().getToplevels();
					__eval.__getTypeDeclarator().evaluateDeclarations(decls,
							__eval.getCurrentEnvt());
					
					for (Toplevel l : decls) {
						l.interpret(__eval);
					}
				}
				catch (RuntimeException e) {
					env.setInitialized(false);
					throw e;
				}
				finally {
					__eval.setCurrentEnvt(oldEnv);
				}
			}

			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(
					org.rascalmpl.interpreter.Evaluator.__getTf().stringType(),
					__eval.__getVf().string(name), __eval);

		}

	}

	public Module(IConstructor __param1) {
		super(__param1);
	}
}
