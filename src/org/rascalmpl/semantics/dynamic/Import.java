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
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.ImportedModule;
import org.rascalmpl.ast.SyntaxDefinition;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.ModuleLoadError;
import org.rascalmpl.interpreter.utils.Names;

public abstract class Import extends org.rascalmpl.ast.Import {
	
	static public class Extend extends org.rascalmpl.ast.Import.Extend {

		public Extend(IConstructor node, ImportedModule module) {
			super(node, module);
		}
		
		@Override
		public Result<IValue> interpret(Evaluator eval) {
			String name = Names.fullName(this.getModule().getName());
			eval.extendCurrentModule(this, name);
			
			GlobalEnvironment heap = eval.getHeap();
			if (heap.getModule(name).isDeprecated()) {
				eval.getStdErr().println(getLocation() + ":" + name + " is deprecated, " + heap.getModule(name).getDeprecatedMessage());
			}
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}
		
		@Override
		public String declareSyntax(Evaluator eval, boolean withImports) {
			String name = Names.fullName(this.getModule().getName());

			ModuleEnvironment env = eval.getHeap().getModule(name);
			if (env != null && env.isSyntaxDefined()) {
				// so modules that have been initialized already dont get parsed again and again
				return name;
			}
			
			org.rascalmpl.ast.Module mod = eval.preParseModule(java.net.URI.create("rascal:///" + name), this.getLocation());  
			mod.declareSyntax(eval, withImports);

			return null;
		}
	}

	static public class Default extends org.rascalmpl.ast.Import.Default {

		public Default(IConstructor __param1, ImportedModule __param2) {
			super(__param1, __param2);
		}

		@Override
		public String declareSyntax(Evaluator eval, boolean withImports) {
			String name = Names.fullName(this.getModule().getName());

			GlobalEnvironment heap = eval.__getHeap();
			if (!heap.existsModule(name)) {
				// deal with a fresh module that needs initialization
				heap.addModule(new ModuleEnvironment(name, heap));
			}

			try {
				eval.addImportToCurrentModule(this, name);

				if (withImports) {
					org.rascalmpl.ast.Module mod = eval.preParseModule(java.net.URI.create("rascal:///" + name), this.getLocation());
					Environment old = eval.getCurrentEnvt();
					try {
						eval.setCurrentEnvt(heap.getModule(name));
						mod.declareSyntax(eval, false);
					}
					finally {
						eval.setCurrentEnvt(old);
					}
				}
			}
			catch (ModuleLoadError e) {
				// when a module does not load, the import should not fail here, rather it will fail when we evaluate the module
				return null;
			}

			return null;
		}
		
		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			// TODO support for full complexity of import declarations
			String name = Names.fullName(this.getModule().getName());
			GlobalEnvironment heap = __eval.__getHeap();
			if (!heap.existsModule(name)) {
				// deal with a fresh module that needs initialization
				heap.addModule(new ModuleEnvironment(name, heap));
				__eval.evalRascalModule(this, name);
				__eval.addImportToCurrentModule(this, name);
			} else if (__eval.getCurrentEnvt() == __eval.__getRootScope()) {
				// in the root scope we treat an import as a "reload"
				heap.resetModule(name);
				__eval.evalRascalModule(this, name);
				__eval.addImportToCurrentModule(this, name);
			} else {
				// otherwise simply add the current imported name to the imports
				// of the current module
				if (!heap.getModule(name).isInitialized()) {
					__eval.evalRascalModule(this, name);
				}
				__eval.addImportToCurrentModule(this, name);
			}

			if (heap.getModule(name).isDeprecated()) {
				__eval.getStdErr().println(getLocation() + ":" + name + " is deprecated, " + heap.getModule(name).getDeprecatedMessage());
			}
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class Syntax extends org.rascalmpl.ast.Import.Syntax {

		public Syntax(IConstructor __param1, SyntaxDefinition __param2) {
			super(__param1, __param2);
		}

		@Override
		public String declareSyntax(Evaluator eval, boolean withImports) {
			return getSyntax().declareSyntax(eval, withImports);
		}
		
		@Override
		public Result<IValue> interpret(Evaluator eval) {
			String parseTreeModName = "ParseTree";
			if (!eval.__getHeap().existsModule(parseTreeModName)) {
				eval.evalRascalModule(this, parseTreeModName);
			}
			eval.addImportToCurrentModule(this, parseTreeModName);

			declareSyntax(eval, false);
			return nothing();
		}

	}

	public Import(IConstructor __param1) {
		super(__param1);
	}
}
