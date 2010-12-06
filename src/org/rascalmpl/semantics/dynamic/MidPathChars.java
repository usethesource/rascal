package org.rascalmpl.semantics.dynamic;

public abstract class MidPathChars extends org.rascalmpl.ast.MidPathChars {


public MidPathChars (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Lexical extends org.rascalmpl.ast.MidPathChars.Lexical {


public Lexical (org.eclipse.imp.pdb.facts.INode __param1,java.lang.String __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.lang.String s = this.getString();
		s = s.substring(1, s.length() - 1);
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), __eval.__getVf().string(s), __eval);
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.MidPathChars.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.MidPathChars> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}