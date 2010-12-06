package org.rascalmpl.semantics.dynamic;

public abstract class IntegerLiteral extends org.rascalmpl.ast.IntegerLiteral {


public IntegerLiteral (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class DecimalIntegerLiteral extends org.rascalmpl.ast.IntegerLiteral.DecimalIntegerLiteral {


public DecimalIntegerLiteral (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.DecimalIntegerLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.lang.String str = ((org.rascalmpl.ast.DecimalIntegerLiteral.Lexical) this.getDecimal()).getString();
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().integerType(), __eval.__getVf().integer(str), __eval);
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.IntegerLiteral.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.IntegerLiteral> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class OctalIntegerLiteral extends org.rascalmpl.ast.IntegerLiteral.OctalIntegerLiteral {


public OctalIntegerLiteral (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.OctalIntegerLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getOctal().__evaluate(__eval);
	
}

}
static public class HexIntegerLiteral extends org.rascalmpl.ast.IntegerLiteral.HexIntegerLiteral {


public HexIntegerLiteral (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.HexIntegerLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getHex().__evaluate(__eval);
	
}

}
}