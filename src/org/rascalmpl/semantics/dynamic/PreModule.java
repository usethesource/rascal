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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.ast.Header;
import org.rascalmpl.ast.Rest;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.utils.Names;

public abstract class PreModule extends org.rascalmpl.ast.PreModule {

	static public class Default extends org.rascalmpl.ast.PreModule.Default {

		public Default(IConstructor node, Header header, Rest rest) {
			super(node, header, rest);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String declareSyntax(Evaluator eval, boolean withImports) {
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
		
	}

	public PreModule(IConstructor __param1) {
		super(__param1);
	}
}
