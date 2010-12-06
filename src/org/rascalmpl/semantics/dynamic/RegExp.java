package org.rascalmpl.semantics.dynamic;

public abstract class RegExp extends org.rascalmpl.ast.RegExp {


public RegExp (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Lexical extends org.rascalmpl.ast.RegExp.Lexical {


public Lexical (org.eclipse.imp.pdb.facts.INode __param1,java.lang.String __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		if(__eval.__getDebug())System.err.println("visitRegExpLexical: " + this.getString());
		return new org.rascalmpl.interpreter.matching.RegExpPatternValue(__eval.__getCtx(), this, this.getString(), java.util.Collections.<java.lang.String>emptyList());
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.RegExp.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.RegExp> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}