package org.rascalmpl.semantics.dynamic;

public abstract class ProtocolTail extends org.rascalmpl.ast.ProtocolTail {


public ProtocolTail (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Post extends org.rascalmpl.ast.ProtocolTail.Post {


public Post (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.PostProtocolChars __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getPost().interpret(__eval);
	
}

}
static public class Mid extends org.rascalmpl.ast.ProtocolTail.Mid {


public Mid (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.MidProtocolChars __param2,org.rascalmpl.ast.Expression __param3,org.rascalmpl.ast.ProtocolTail __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> pre = this.getMid().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> expr = this.getExpression().interpret(__eval);
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> tail = this.getTail().interpret(__eval);
		java.lang.StringBuilder result = new java.lang.StringBuilder();

		result.append(((org.eclipse.imp.pdb.facts.IString) pre.getValue()).getValue());
		__eval.appendToString(expr.getValue(), result);
		result.append(((org.eclipse.imp.pdb.facts.IString) tail.getValue()).getValue());

		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), __eval.__getVf().string(result.toString()), __eval);
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.ProtocolTail.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.ProtocolTail> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}