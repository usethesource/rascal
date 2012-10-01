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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.ImportedModule;
import org.rascalmpl.ast.LocationLiteral;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.SyntaxDefinition;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.result.SourceLocationResult;
import org.rascalmpl.interpreter.staticErrors.ModuleLoadError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleProvider;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public abstract class Import extends org.rascalmpl.ast.Import {
	
	static public class External extends org.rascalmpl.ast.Import.External {

		public External(IConstructor node, QualifiedName name,
				LocationLiteral at) {
			super(node, name, at);
		}
		
	
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			// Compute the URI location, which contains the scheme (and other info we need later)
			ISourceLocation sl = (ISourceLocation)getAt().interpret(eval).getValue();
			
			// If we have a resource scheme, given as resource scheme + standard scheme, we
			// extract that out, e.g., jdbctable+mysql would give a resource scheme of
			// jdbctable and a standard URI scheme of mysql. If we do not have a separate
			// resource scheme, we just use the specified scheme, e.g., sdf would give
			// a resource scheme of sdf and a URI scheme of sdf.
			URI uri = sl.getURI();
			String resourceScheme = uri.getScheme();
			
			if (resourceScheme.contains("+")) {
				String uriScheme = resourceScheme.substring(resourceScheme.indexOf("+")+1); 
				resourceScheme = resourceScheme.substring(0,resourceScheme.indexOf("+"));
				try {
					uri = URIUtil.changeScheme(uri, uriScheme);
				} catch (URISyntaxException e) {
					throw RuntimeExceptionFactory.malformedURI(uri.toString().substring(uri.toString().indexOf("+")+1), null, null);
				}
				sl = ValueFactoryFactory.getValueFactory().sourceLocation(uri);
			}
			
			String moduleName = Names.fullName(this.getName());
			IString mn = this.VF.string(moduleName);
			
			// Using the scheme, get back the correct importer
			ICallableValue importer = getImporter(resourceScheme, eval.getCurrentEnvt());
			
			if (importer != null) {
				Type[] argTypes = new org.eclipse.imp.pdb.facts.type.Type[] {TF.stringType(), TF.sourceLocationType()};
				IValue[] argValues = new IValue[] { mn, sl };
				
				// Invoke the importer, which should generate the text of the module that we need
				// to actually import.
				IValue module = importer.call(argTypes, argValues).getValue();
				String moduleText = module.getType().isStringType() ? ((IString) module).getValue() : TreeAdapter.yield((IConstructor) module);
				
				moduleText = "@generated\n" + moduleText;
				
				try {
					URIResolverRegistry reg = eval.getResolverRegistry();
					String moduleEnvName = eval.getCurrentModuleEnvironment().getName();
					URI ur = null;
					if (moduleEnvName.equals(ModuleEnvironment.SHELL_MODULE)) {
						ur = URIUtil.rootScheme("cwd");
					} else {
						ur = eval.getRascalResolver().getRootForModule((URIUtil.createRascalModule(moduleEnvName)));
					}
					Result<?> loc = new SourceLocationResult(TF.sourceLocationType(), VF.sourceLocation(ur), eval);
					String modulePath = moduleName.replaceAll("::", "/");
					loc = loc.add(ResultFactory.makeResult(TF.stringType(), VF.string(modulePath), eval));
					loc = loc.fieldUpdate("extension", ResultFactory.makeResult(TF.stringType(), VF.string(".rsc"), eval), eval.getCurrentEnvt().getStore());
					
					OutputStream outputStream;
					try {
						outputStream = reg.getOutputStream(((ISourceLocation) loc.getValue()).getURI(), false);
					}
					catch (IOException e) {
						outputStream = reg.getOutputStream(URIUtil.rootScheme("cwd"), false);
					}
					
					if (outputStream == null) {
						outputStream = reg.getOutputStream(URIUtil.rootScheme("cwd"), false);
					}
					
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
					writer.write(moduleText);
					writer.close();
				}
				catch (IOException e) {
					throw RuntimeExceptionFactory.moduleNotFound(mn, eval.getCurrentAST(), eval.getStackTrace());
				}
				
				return importModule(this.getName(), eval);
			} else {
				throw new UndeclaredModuleProvider(resourceScheme, eval.getCurrentAST());
			}
		}
		
		@Override
		public String declareSyntax(IEvaluator<Result<IValue>> eval, boolean withImports) {
			// TODO: this means that external imports do not support syntax definition for now
			return Names.fullName(getName());
		}


		private ICallableValue getImporter(String s, Environment currentEnvt) {
			return currentEnvt.getHeap().getResourceImporter(s);
		}
	}
	
	static public class Extend extends org.rascalmpl.ast.Import.Extend {

		public Extend(IConstructor node, ImportedModule module) {
			super(node, module);
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			String name = Names.fullName(this.getModule().getName());
			eval.extendCurrentModule(this.getLocation(), name);
			
			GlobalEnvironment heap = eval.getHeap();
			if (heap.getModule(name).isDeprecated()) {
				eval.getStdErr().println(getLocation() + ":" + name + " is deprecated, " + heap.getModule(name).getDeprecatedMessage());
			}
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}
		
		@Override
		public String declareSyntax(IEvaluator<Result<IValue>> eval, boolean withImports) {
			String name = Names.fullName(this.getModule().getName());

			GlobalEnvironment heap = eval.__getHeap();
			if (!heap.existsModule(name)) {
				// deal with a fresh module that needs initialization
				heap.addModule(new ModuleEnvironment(name, heap));
			}
			
			ModuleEnvironment env = eval.getCurrentModuleEnvironment();
			
			if (env.getExtends().contains(name)) {
				return name;
			}

			try {
				eval.getCurrentModuleEnvironment().addExtend(name);

				if (withImports) {
					org.rascalmpl.ast.Module mod = eval.preParseModule(URIUtil.assumeCorrect("rascal", name, ""), this.getLocation());
					mod.declareSyntax(eval, true);
				}
				
				return name;
			}
			catch (ModuleLoadError e) {
				// when a module does not load, the import should not fail here, rather it will fail when we evaluate the module
				return null;
			}
		}
		
		
	}

	static public class Default extends org.rascalmpl.ast.Import.Default {

		public Default(IConstructor __param1, ImportedModule __param2) {
			super(__param1, __param2);
		}

		@Override
		public String declareSyntax(IEvaluator<Result<IValue>> eval, boolean withImports) {
			String name = Names.fullName(this.getModule().getName());

			GlobalEnvironment heap = eval.__getHeap();
			if (!heap.existsModule(name)) {
				// deal with a fresh module that needs initialization
				heap.addModule(new ModuleEnvironment(name, heap));
			}

			try {
				eval.addImportToCurrentModule(this, name);

				if (withImports) {
					org.rascalmpl.ast.Module mod = eval.preParseModule(URIUtil.assumeCorrect("rascal", name, ""), this.getLocation());
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
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			// TODO support for full complexity of import declarations
			return importModule(this.getModule().getName(), __eval);

		}

	}

	static public class Syntax extends org.rascalmpl.ast.Import.Syntax {

		public Syntax(IConstructor __param1, SyntaxDefinition __param2) {
			super(__param1, __param2);
		}

		@Override
		public String declareSyntax(IEvaluator<Result<IValue>> eval, boolean withImports) {
			return getSyntax().declareSyntax(eval, withImports);
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
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

	protected static Result<IValue> importModule(QualifiedName imported, IEvaluator<Result<IValue>> __eval) {
		String name = Names.fullName(imported);
		GlobalEnvironment heap = __eval.__getHeap();
		if (!heap.existsModule(name)) {
			// deal with a fresh module that needs initialization
			heap.addModule(new ModuleEnvironment(name, heap));
			__eval.evalRascalModule(imported, name);
			__eval.addImportToCurrentModule(imported, name);
		} else if (__eval.getCurrentEnvt() == __eval.__getRootScope()) {
			// in the root scope we treat an import as a "reload"
			heap.resetModule(name);
			__eval.evalRascalModule(imported, name);
			__eval.addImportToCurrentModule(imported, name);
		} else {
			// otherwise simply add the current imported name to the imports
			// of the current module
			if (!heap.getModule(name).isInitialized()) {
				__eval.evalRascalModule(imported, name);
			}
			__eval.addImportToCurrentModule(imported, name);
		}
	
		if (heap.getModule(name).isDeprecated()) {
			__eval.getStdErr().println(imported.getLocation() + ":" + name + " is deprecated, " + heap.getModule(name).getDeprecatedMessage());
		}
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	}
}
