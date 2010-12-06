package org.rascalmpl.semantics.dynamic;

public abstract class Declarator extends org.rascalmpl.ast.Declarator {


public Declarator (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Default extends org.rascalmpl.ast.Declarator.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2,java.util.List<org.rascalmpl.ast.Variable> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r = org.rascalmpl.interpreter.result.ResultFactory.nothing();

		for (org.rascalmpl.ast.Variable var : this.getVariables()) {
			java.lang.String varAsString = org.rascalmpl.interpreter.utils.Names.name(var.getName());

			if (var.isInitialized()) {  // variable declaration without initialization
				// first evaluate the initialization, in case the left hand side will shadow something
				// that is used on the right hand side.
				org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> v = var.getInitial().__evaluate(__eval);

				org.eclipse.imp.pdb.facts.type.Type declaredType = __eval.evalType(this.getType());

				if (!__eval.getCurrentEnvt().declareVariable(declaredType, var.getName())) {
					throw new org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError(varAsString, var);
				}

				if(v.getType().isSubtypeOf(declaredType)){
					// TODO: do we actually want to instantiate the locally bound type parameters?
					java.util.Map<org.eclipse.imp.pdb.facts.type.Type,org.eclipse.imp.pdb.facts.type.Type> bindings = new java.util.HashMap<org.eclipse.imp.pdb.facts.type.Type,org.eclipse.imp.pdb.facts.type.Type>();
					declaredType.match(v.getType(), bindings);
					declaredType = declaredType.instantiate(bindings);
					// Was: r = makeResult(declaredType, applyRules(v.getValue()));
					r = org.rascalmpl.interpreter.result.ResultFactory.makeResult(declaredType, v.getValue(), __eval);
					__eval.getCurrentEnvt().storeVariable(var.getName(), r);
				} else {
					throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(declaredType, v.getType(), var);
				}
			}
			else {
				org.eclipse.imp.pdb.facts.type.Type declaredType = __eval.evalType(this.getType());

				if (!__eval.getCurrentEnvt().declareVariable(declaredType, var.getName())) {
					throw new org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError(varAsString, var);
				}
			}
		}

		return r;
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.Declarator.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Declarator> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}