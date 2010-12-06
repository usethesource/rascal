package org.rascalmpl.semantics.dynamic;

public abstract class PathTail extends org.rascalmpl.ast.PathTail {


public PathTail (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Mid extends org.rascalmpl.ast.PathTail.Mid {


public Mid (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.MidPathChars __param2,org.rascalmpl.ast.Expression __param3,org.rascalmpl.ast.PathTail __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> mid = this.getMid().__evaluate(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> expr = this.getExpression().__evaluate(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> tail = this.getTail().__evaluate(__eval);
		java.lang.StringBuilder result = new java.lang.StringBuilder();

		result.append(((org.eclipse.imp.pdb.facts.IString) mid.getValue()).getValue());
		__eval.appendToString(expr.getValue(), result);
		result.append(((org.eclipse.imp.pdb.facts.IString) tail.getValue()).getValue());

		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), __eval.__getVf().string(result.toString()), __eval);
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.PathTail.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.PathTail> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Post extends org.rascalmpl.ast.PathTail.Post {


public Post (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.PostPathChars __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getPost().__evaluate(__eval);
	
}

}
}