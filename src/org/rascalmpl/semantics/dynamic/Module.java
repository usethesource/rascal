package org.rascalmpl.semantics.dynamic;

import java.lang.String;
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

public abstract class Module extends org.rascalmpl.ast.Module {

	public Module(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.Module.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Module> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Default extends org.rascalmpl.ast.Module.Default {

		public Default(INode __param1, Header __param2, Body __param3) {
			super(__param1, __param2, __param3);
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
				Environment oldEnv = __eval.getCurrentEnvt();
				__eval.setCurrentEnvt(env); // such that declarations end up in
											// the module scope
				try {
					this.getHeader().interpret(__eval);

					List<Toplevel> decls = this.getBody().getToplevels();
					__eval.__getTypeDeclarator().evaluateSyntaxDefinitions(this.getHeader().getImports(), __eval.getCurrentEnvt());
					__eval.__getTypeDeclarator().evaluateDeclarations(decls, __eval.getCurrentEnvt());

					for (Toplevel l : decls) {
						l.interpret(__eval);
					}

					// only after everything was successful mark the module
					// initialized
					env.setInitialized();
				} finally {
					__eval.setCurrentEnvt(oldEnv);
				}
			}

			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), __eval.__getVf().string(name), __eval);

		}


	}
}
