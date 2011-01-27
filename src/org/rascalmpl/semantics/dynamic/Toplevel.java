package org.rascalmpl.semantics.dynamic;

public abstract class Toplevel extends org.rascalmpl.ast.Toplevel {


public Toplevel (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.Toplevel.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Toplevel> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class GivenVisibility extends org.rascalmpl.ast.Toplevel.GivenVisibility {


public GivenVisibility (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Declaration __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getDeclaration().interpret(__eval);
	
}

}
}