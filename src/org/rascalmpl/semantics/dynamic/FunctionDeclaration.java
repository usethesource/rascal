package org.rascalmpl.semantics.dynamic;

public abstract class FunctionDeclaration extends org.rascalmpl.ast.FunctionDeclaration {


public FunctionDeclaration (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Abstract extends org.rascalmpl.ast.FunctionDeclaration.Abstract {


public Abstract (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Visibility __param3,org.rascalmpl.ast.Signature __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		boolean varArgs = this.getSignature().getParameters().isVarArgs();

		if (!__eval.hasJavaModifier(this)) {
			throw new org.rascalmpl.interpreter.staticErrors.MissingModifierError("java", this);
		}

		org.rascalmpl.interpreter.result.AbstractFunction lambda = new org.rascalmpl.interpreter.result.JavaMethod(__eval, this, varArgs, __eval.getCurrentEnvt(), __eval.__getJavaBridge());
		java.lang.String name = org.rascalmpl.interpreter.utils.Names.name(this.getSignature().getName());
		__eval.getCurrentEnvt().storeFunction(name, lambda);
		__eval.getCurrentEnvt().markNameFinal(lambda.getName());
		__eval.getCurrentEnvt().markNameOverloadable(lambda.getName());

		lambda.setPublic(this.getVisibility().isPublic());
		return lambda;
	
}

}
static public class Default extends org.rascalmpl.ast.FunctionDeclaration.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Visibility __param3,org.rascalmpl.ast.Signature __param4,org.rascalmpl.ast.FunctionBody __param5) {
	super(__param1,__param2,__param3,__param4,__param5);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.AbstractFunction lambda;
		boolean varArgs = this.getSignature().getParameters().isVarArgs();

		if (__eval.hasJavaModifier(this)) {
			throw new org.rascalmpl.interpreter.staticErrors.JavaMethodLinkError("may not use java modifier with a function that has a body", null,  this);
		}
		
		if (!this.getBody().isDefault()) {
			throw new org.rascalmpl.interpreter.staticErrors.MissingModifierError("java", this);
		}

		lambda = new org.rascalmpl.interpreter.result.RascalFunction(__eval, this, varArgs, __eval.getCurrentEnvt(), __eval.__getAccumulators());

		__eval.getCurrentEnvt().storeFunction(lambda.getName(), lambda);
		__eval.getCurrentEnvt().markNameFinal(lambda.getName());
		__eval.getCurrentEnvt().markNameOverloadable(lambda.getName());

		lambda.setPublic(this.getVisibility().isPublic());
		return lambda;
	
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return this.getSignature().__evaluate(__eval);
		
}

}
static public class Ambiguity extends org.rascalmpl.ast.FunctionDeclaration.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.FunctionDeclaration> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}