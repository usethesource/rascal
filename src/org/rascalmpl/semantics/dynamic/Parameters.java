package org.rascalmpl.semantics.dynamic;

public abstract class Parameters extends org.rascalmpl.ast.Parameters {


public Parameters (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class VarArgs extends org.rascalmpl.ast.Parameters.VarArgs {


public VarArgs (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Formals __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			org.eclipse.imp.pdb.facts.type.Type formals = this.getFormals().__evaluate(__eval);
			int arity = formals.getArity();

			if (arity == 0) {
				// TODO is __eval sensible or should we restrict the syntax?
				return org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(org.rascalmpl.interpreter.TypeEvaluator.__getTf().listType(org.rascalmpl.interpreter.TypeEvaluator.__getTf().valueType()), "args");
			}
			
			org.eclipse.imp.pdb.facts.type.Type[] types = new org.eclipse.imp.pdb.facts.type.Type[arity];
			java.lang.String[] labels = new java.lang.String[arity];
			int i;

			for (i = 0; i < arity - 1; i++) {
				types[i] = formals.getFieldType(i);
				labels[i] = formals.getFieldName(i);
			}

			types[i] = org.rascalmpl.interpreter.TypeEvaluator.__getTf().listType(formals.getFieldType(i));
			labels[i] = formals.getFieldName(i);

			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(types, labels);
		
}

}
static public class Default extends org.rascalmpl.ast.Parameters.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Formals __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return this.getFormals().__evaluate(__eval);
		
}

}
static public class Ambiguity extends org.rascalmpl.ast.Parameters.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Parameters> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}