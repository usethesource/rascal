package org.rascalmpl.semantics.dynamic;

public abstract class Literal extends org.rascalmpl.ast.Literal {


public Literal (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.Literal.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Literal> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Real extends org.rascalmpl.ast.Literal.Real {


public Real (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.RealLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.LiteralPattern(__eval.__getCtx(), this, this.interpret(__eval.__getCtx().getEvaluator()).getValue());
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.lang.String str = this.getRealLiteral().toString();
		if (str.toLowerCase().endsWith("d")) {
			str = str.substring(0, str.length() - 1);
		}
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().realType(), __eval.__getVf().real(str), __eval);
	
}

}
static public class Integer extends org.rascalmpl.ast.Literal.Integer {


public Integer (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.IntegerLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.LiteralPattern(__eval.__getCtx(), this, this.interpret(__eval.__getCtx().getEvaluator()).getValue());
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getIntegerLiteral().interpret(__eval);
	
}

}
static public class RegExp extends org.rascalmpl.ast.Literal.RegExp {


public RegExp (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.RegExpLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return this.getRegExpLiteral().__evaluate(__eval);
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.staticErrors.SyntaxError("regular expression. They are only allowed in a pattern (left of <- and := or in a case statement).", this.getLocation());
	
}

}
static public class Boolean extends org.rascalmpl.ast.Literal.Boolean {


public Boolean (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.BooleanLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.LiteralPattern(__eval.__getCtx(), this, this.interpret(__eval.__getCtx().getEvaluator()).getValue());
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.lang.String str = this.getBooleanLiteral().toString();
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), __eval.__getVf().bool(str.equals("true")), __eval);
	
}

}
static public class DateTime extends org.rascalmpl.ast.Literal.DateTime {


public DateTime (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.DateTimeLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getDateTimeLiteral().interpret(__eval);
	
}

}
static public class Location extends org.rascalmpl.ast.Literal.Location {


public Location (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.LocationLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getLocationLiteral().interpret(__eval);
	
}

}
static public class String extends org.rascalmpl.ast.Literal.String {


public String (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.StringLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.matching.IMatchingResult __evaluate(org.rascalmpl.interpreter.PatternEvaluator __eval) {
	
		return new org.rascalmpl.interpreter.matching.LiteralPattern(__eval.__getCtx(), this, this.interpret(__eval.__getCtx().getEvaluator()).getValue());
	
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.ast.StringLiteral lit = this.getStringLiteral();

		java.lang.StringBuilder result = new java.lang.StringBuilder();

		// To prevent infinite recursion detect non-interpolated strings
		// first. TODO: design flaw?
		if (lit.isNonInterpolated()) {
			java.lang.String str = org.rascalmpl.interpreter.utils.Utils.unescape(((org.rascalmpl.ast.StringConstant.Lexical)lit.getConstant()).getString());
			result.append(str);
		}
		else {
			org.rascalmpl.ast.Statement stat = org.rascalmpl.interpreter.StringTemplateConverter.convert(lit);
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> value = stat.interpret(__eval);
			if (!value.getType().isListType()) {
				throw new org.rascalmpl.interpreter.asserts.ImplementationError("template eval returns non-list");
			}
			org.eclipse.imp.pdb.facts.IList list = (org.eclipse.imp.pdb.facts.IList)value.getValue();
			for (org.eclipse.imp.pdb.facts.IValue elt: list) {
				__eval.appendToString(elt, result);
			}
		}

		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), __eval.__getVf().string(result.toString()), __eval);
	
}

}
}