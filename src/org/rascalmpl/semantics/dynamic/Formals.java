package org.rascalmpl.semantics.dynamic;

public abstract class Formals extends org.rascalmpl.ast.Formals {


public Formals (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.Formals.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Formals> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Default extends org.rascalmpl.ast.Formals.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Formal> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			java.util.List<org.rascalmpl.ast.Formal> list = this.getFormals();
			java.lang.Object[] typesAndNames = new java.lang.Object[list.size() * 2];

			for (int formal = 0, index = 0; formal < list.size(); formal++, index++) {
				org.rascalmpl.ast.Formal f = list.get(formal);
				org.eclipse.imp.pdb.facts.type.Type type = f.__evaluate(__eval);
				
				if (type == null) {
					throw new org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError(f.getType().toString(), f);
				}
				typesAndNames[index++] = type;
				typesAndNames[index] = org.rascalmpl.interpreter.utils.Names.name(f.getName());
			}

			return org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(typesAndNames);
		
}

}
}