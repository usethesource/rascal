package org.rascalmpl.semantics.dynamic;

public abstract class Module extends org.rascalmpl.ast.Module {


public Module (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.Module.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Module> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Default extends org.rascalmpl.ast.Module.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Header __param2,org.rascalmpl.ast.Body __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.lang.String name = __eval.getModuleName(this);

		org.rascalmpl.interpreter.env.ModuleEnvironment env = __eval.__getHeap().getModule(name);

		if (env == null) {
			env = new org.rascalmpl.interpreter.env.ModuleEnvironment(name);
			__eval.__getHeap().addModule(env);
		}
		
		env.setBootstrap(__eval.needBootstrapParser(this));

		if (!env.isInitialized()) {
			org.rascalmpl.interpreter.env.Environment oldEnv = __eval.getCurrentEnvt();
			__eval.setCurrentEnvt(env); // such that declarations end up in the module scope
			try {
				this.getHeader().__evaluate(__eval);

				java.util.List<org.rascalmpl.ast.Toplevel> decls = this.getBody().getToplevels();
				__eval.__getTypeDeclarator().evaluateSyntaxDefinitions(this.getHeader().getImports(), __eval.getCurrentEnvt());
				__eval.__getTypeDeclarator().evaluateDeclarations(decls, __eval.getCurrentEnvt());

				for (org.rascalmpl.ast.Toplevel l : decls) {
					l.__evaluate(__eval);
				}

				// only after everything was successful mark the module initialized
				env.setInitialized();
			}
			finally {
				__eval.setCurrentEnvt(oldEnv);
			}
		}
		
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), __eval.__getVf().string(name), __eval);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}