package org.rascalmpl.semantics.dynamic;

public abstract class Formal extends org.rascalmpl.ast.Formal {


public Formal (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.Formal.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Formal> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class TypeName extends org.rascalmpl.ast.Formal.TypeName {


public TypeName (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2,org.rascalmpl.ast.Name __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return this.getType().__evaluate(__eval);
		
}

}
}