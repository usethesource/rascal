/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import org.rascalmpl.ast.Body;
import org.rascalmpl.ast.Header;
import org.rascalmpl.ast.Toplevel;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.utils.Names;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

public abstract class Module {

	static public class Default extends org.rascalmpl.ast.Module.Default {

		public Default(ISourceLocation __param1, IConstructor tree, Header __param2, Body __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			String name = getModuleName(this);
			GlobalEnvironment heap = eval.__getHeap();
			ModuleEnvironment env = heap.getModule(name);

			if (env == null) { 
				env = new ModuleEnvironment(name, heap);
				heap.addModule(env);
			}

			Environment oldEnv = eval.getCurrentEnvt();
			eval.setCurrentEnvt(env); // such that declarations end up in
			// the module scope
			try {
			  // the header is already evaluated at parse time, 
			  // including imports and extends and syntax definitions
			  List<Toplevel> decls = this.getBody().getToplevels();
			  eval.__getTypeDeclarator().evaluateDeclarations(decls, eval.getCurrentEnvt(), false);

			  for (Toplevel l : decls) {
				l.interpret(eval);
			  }
			}
			catch (RuntimeException e) {
			  env.setInitialized(false);
			  throw e;
			}
			finally {
			  eval.setCurrentEnvt(oldEnv);
			}

			return ResultFactory.nothing();
		}
	}

	public static String getModuleName(org.rascalmpl.ast.Module module) {
    return Names.fullName(module.getHeader().getName());
  }
}
