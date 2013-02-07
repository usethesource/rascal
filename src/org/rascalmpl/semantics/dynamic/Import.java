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
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.SyntaxDefinition;
import org.rascalmpl.ast.Tag;
import org.rascalmpl.ast.TagString.Lexical;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.result.SourceLocationResult;
import org.rascalmpl.interpreter.staticErrors.ModuleImport;
import org.rascalmpl.interpreter.staticErrors.ModuleNameMismatch;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModule;
import org.rascalmpl.interpreter.staticErrors.UndeclaredModuleProvider;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public abstract class Import {
	
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
				IValue module = importer.call(argTypes, argValues, null).getValue();
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
				
				importModule(Names.fullName(this.getName()), getLocation(), eval);
				return ResultFactory.nothing();
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
			extendCurrentModule(this.getLocation(), name, eval);
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}
	}

	static public class Default extends org.rascalmpl.ast.Import.Default {
		public Default(IConstructor __param1, ImportedModule __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			 importModule(Names.fullName(getModule().getName()), getLocation(), eval);
			 return ResultFactory.nothing();
		}
	}

	static public class Syntax extends org.rascalmpl.ast.Import.Syntax {
		public Syntax(IConstructor __param1, SyntaxDefinition __param2) {
			super(__param1, __param2);
		}

    @Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			String parseTreeModName = "ParseTree";
			if (!eval.__getHeap().existsModule(parseTreeModName)) {
				loadModule(getLocation(), parseTreeModName, eval);
			}
			addImportToCurrentModule(getLocation(), parseTreeModName, eval);

			getSyntax().interpret(eval);
			return nothing();
		}
	}

	protected static void importModule(String name, ISourceLocation src, IEvaluator<Result<IValue>> eval) {
		GlobalEnvironment heap = eval.__getHeap();
		
		if (!heap.existsModule(name)) {
			// deal with a fresh module that needs initialization
			heap.addModule(new ModuleEnvironment(name, heap));
			loadModule(src, name, eval);
		} 
		else if (eval.getCurrentEnvt() == eval.__getRootScope()) {
			// in the root scope we treat an import as a "reload"
			heap.resetModule(name);
			loadModule(src, name, eval);
		} 
		
		addImportToCurrentModule(src, name, eval);
		
		if (heap.getModule(name).isDeprecated()) {
			eval.getStdErr().println(src + ":" + name + " is deprecated, " + heap.getModule(name).getDeprecatedMessage());
		}
		
		return;
	}
	
	public static void extendCurrentModule(ISourceLocation x, String name, IEvaluator<Result<IValue>> eval) {
    eval.getStdErr().println("extending " + name);
    GlobalEnvironment heap = eval.__getHeap();
    ModuleEnvironment other = heap.getModule(name);;
    
    if (other == null) {
      // deal with a fresh module that needs initialization
      heap.addModule(new ModuleEnvironment(name, heap));
      other = loadModule(x, name, eval);
    } 
    else if (eval.getCurrentEnvt() == eval.__getRootScope()) {
      // in the root scope we treat an extend as a "reload"
      heap.resetModule(name);
      other = loadModule(x, name, eval);
    } 
    
    // now simply extend the current module
    eval.getCurrentModuleEnvironment().extend(heap.getModule(name));
  }
	
  public static ModuleEnvironment loadModule(ISourceLocation x, String name, IEvaluator<Result<IValue>> eval) {
    GlobalEnvironment heap = eval.getHeap();
    
    ModuleEnvironment env = heap.getModule(name);
    if (env == null) {
      env = new ModuleEnvironment(name, heap);
      heap.addModule(env);
    }
    
    try {
      Module module = buildModule(name, env, eval);

      if (isDeprecated(module)) {
        eval.getStdErr().println("WARNING: deprecated module " + name + ":" + getDeprecatedMessage(module));
      }
      
      if (module != null) {
        String internalName = org.rascalmpl.semantics.dynamic.Module.getModuleName(module);
        if (!internalName.equals(name)) {
          throw new ModuleNameMismatch(internalName, name, x);
        }
        heap.setModuleURI(name, module.getLocation().getURI());
        module.interpret(eval);
        
        return env;
      }
    } catch (StaticError e) {
      heap.removeModule(env);
      throw e;
    } catch (Throw e) {
      heap.removeModule(env);
      throw e;
    } catch (IOException e) {
      heap.removeModule(env);
      throw new ModuleImport(name, e.getMessage(), x);
    }

    heap.removeModule(env);
    throw new ImplementationError("Unexpected error while parsing module " + name + " and building an AST for it ", x);
  }
  
  private static boolean isDeprecated(Module preModule){
    for (Tag tag : preModule.getHeader().getTags().getTags()) {
      if (((Name.Lexical) tag.getName()).getString().equals("deprecated")) {
        return true;
      }
    }
    return false;
  }
  
  private static String getDeprecatedMessage(Module preModule){
    for (Tag tag : preModule.getHeader().getTags().getTags()) {
      if (((Name.Lexical) tag.getName()).getString().equals("deprecated")) {
        String contents = ((Lexical) tag.getContents()).getString();
        return contents.substring(1, contents.length() -1);
      }
    }
    return "";
  }
  
  private static Module buildModule(String name, ModuleEnvironment env,  IEvaluator<Result<IValue>> eval) throws IOException {
    IConstructor tree = eval.parseModule(eval, URIUtil.createRascalModule(name), env);
    return eval.getBuilder().buildModule(tree);
  }
  
  private static void addImportToCurrentModule(ISourceLocation src, String name, IEvaluator<Result<IValue>> eval) {
    ModuleEnvironment module = eval.getHeap().getModule(name);
    if (module == null) {
      throw new UndeclaredModule(name, src);
    }
    eval.getCurrentModuleEnvironment().addImport(name, module);
  }
}
