package org.rascalmpl.semantics.dynamic;

public abstract class Declaration extends org.rascalmpl.ast.Declaration {


public Declaration (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class View extends org.rascalmpl.ast.Declaration.View {


public View (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Visibility __param3,org.rascalmpl.ast.Name __param4,org.rascalmpl.ast.Name __param5,java.util.List<org.rascalmpl.ast.Alternative> __param6) {
	super(__param1,__param2,__param3,__param4,__param5,__param6);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		// TODO implement
		throw new org.rascalmpl.interpreter.asserts.NotYetImplemented("Views");
	
}

}
static public class Test extends org.rascalmpl.ast.Declaration.Test {


public Test (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Test __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getTest().interpret(__eval);
	
}

}
static public class Alias extends org.rascalmpl.ast.Declaration.Alias {


public Alias (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Visibility __param3,org.rascalmpl.ast.UserType __param4,org.rascalmpl.ast.Type __param5) {
	super(__param1,__param2,__param3,__param4,__param5);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.__getTypeDeclarator().declareAlias(this, __eval.getCurrentEnvt());
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Annotation extends org.rascalmpl.ast.Declaration.Annotation {


public Annotation (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Visibility __param3,org.rascalmpl.ast.Type __param4,org.rascalmpl.ast.Type __param5,org.rascalmpl.ast.Name __param6) {
	super(__param1,__param2,__param3,__param4,__param5,__param6);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.eclipse.imp.pdb.facts.type.Type annoType = new org.rascalmpl.interpreter.TypeEvaluator(__eval.getCurrentModuleEnvironment(), __eval.__getHeap()).eval(this.getAnnoType());
		java.lang.String name = org.rascalmpl.interpreter.utils.Names.name(this.getName());

		org.eclipse.imp.pdb.facts.type.Type onType = new org.rascalmpl.interpreter.TypeEvaluator(__eval.getCurrentModuleEnvironment(), __eval.__getHeap()).eval(this.getOnType());
		__eval.getCurrentModuleEnvironment().declareAnnotation(onType, name, annoType);	

		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.Declaration.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Declaration> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class DataAbstract extends org.rascalmpl.ast.Declaration.DataAbstract {


public DataAbstract (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Visibility __param3,org.rascalmpl.ast.UserType __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.__getTypeDeclarator().declareAbstractADT(this, __eval.getCurrentEnvt());
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Variable extends org.rascalmpl.ast.Declaration.Variable {


public Variable (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Visibility __param3,org.rascalmpl.ast.Type __param4,java.util.List<org.rascalmpl.ast.Variable> __param5) {
	super(__param1,__param2,__param3,__param4,__param5);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> r = org.rascalmpl.interpreter.result.ResultFactory.nothing();
		__eval.setCurrentAST(this);

		for (org.rascalmpl.ast.Variable var : this.getVariables()) {
			org.eclipse.imp.pdb.facts.type.Type declaredType = new org.rascalmpl.interpreter.TypeEvaluator(__eval.getCurrentModuleEnvironment(), __eval.__getHeap()).eval(this.getType());

			if (var.isInitialized()) {  
				org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> v = var.getInitial().interpret(__eval);

				if (!__eval.getCurrentEnvt().declareVariable(declaredType, var.getName())) {
					throw new org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError(org.rascalmpl.interpreter.utils.Names.name(var.getName()), var);
				}

				if(v.getType().isSubtypeOf(declaredType)){
					// TODO: do we actually want to instantiate the locally bound type parameters?
					java.util.Map<org.eclipse.imp.pdb.facts.type.Type,org.eclipse.imp.pdb.facts.type.Type> bindings = new java.util.HashMap<org.eclipse.imp.pdb.facts.type.Type,org.eclipse.imp.pdb.facts.type.Type>();
					declaredType.match(v.getType(), bindings);
					declaredType = declaredType.instantiate(bindings);
					r = org.rascalmpl.interpreter.result.ResultFactory.makeResult(declaredType, v.getValue(), __eval);
					__eval.getCurrentModuleEnvironment().storeVariable(var.getName(), r);
				} else {
					throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(declaredType, v.getType(), var);
				}
			}
			else {
				throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(org.rascalmpl.interpreter.utils.Names.name(var.getName()), var);
			}
		}

		r.setPublic(this.getVisibility().isPublic());
		return r;
	
}

}
static public class Rule extends org.rascalmpl.ast.Declaration.Rule {


public Rule (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Name __param3,org.rascalmpl.ast.PatternWithAction __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getPatternAction().interpret(__eval);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Tag extends org.rascalmpl.ast.Declaration.Tag {


public Tag (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Visibility __param3,org.rascalmpl.ast.Kind __param4,org.rascalmpl.ast.Name __param5,java.util.List<org.rascalmpl.ast.Type> __param6) {
	super(__param1,__param2,__param3,__param4,__param5,__param6);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.NotYetImplemented("tags");
	
}

}
static public class Data extends org.rascalmpl.ast.Declaration.Data {


public Data (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Visibility __param3,org.rascalmpl.ast.UserType __param4,java.util.List<org.rascalmpl.ast.Variant> __param5) {
	super(__param1,__param2,__param3,__param4,__param5);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.__getTypeDeclarator().declareConstructor(this, __eval.getCurrentEnvt());
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
static public class Function extends org.rascalmpl.ast.Declaration.Function {


public Function (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.FunctionDeclaration __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getFunctionDeclaration().interpret(__eval);
	
}

}
}