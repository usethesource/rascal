package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Body;
import org.rascalmpl.ast.Header;
import org.rascalmpl.ast.Toplevel;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;

public abstract class Module extends org.rascalmpl.ast.Module {

	static public class Default extends org.rascalmpl.ast.Module.Default {

		public Default(INode __param1, Header __param2, Body __param3) {
			super(__param1, __param2, __param3);
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

			env.setBootstrap(eval.needBootstrapParser(this));
			if(!env.getSyntaxDefined()) {
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
		public Result<IValue> interpret(Evaluator __eval) {

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

	public Module(INode __param1) {
		super(__param1);
	}
}
