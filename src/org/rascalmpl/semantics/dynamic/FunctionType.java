package org.rascalmpl.semantics.dynamic;

public abstract class FunctionType extends org.rascalmpl.ast.FunctionType {


public FunctionType (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.FunctionType.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.FunctionType> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class TypeArguments extends org.rascalmpl.ast.FunctionType.TypeArguments {


public TypeArguments (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2,java.util.List<org.rascalmpl.ast.TypeArg> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			org.eclipse.imp.pdb.facts.type.Type returnType = this.getType().__evaluate(__eval);
			org.eclipse.imp.pdb.facts.type.Type argTypes = __eval.getArgumentTypes(this.getArguments());
			return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().functionType(returnType, argTypes);
		
}

}
}