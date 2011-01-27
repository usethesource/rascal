package org.rascalmpl.semantics.dynamic;

public abstract class DateTimeLiteral extends org.rascalmpl.ast.DateTimeLiteral {


public DateTimeLiteral (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class DateAndTimeLiteral extends org.rascalmpl.ast.DateTimeLiteral.DateAndTimeLiteral {


public DateAndTimeLiteral (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.DateAndTime __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getDateAndTime().interpret(__eval);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.DateTimeLiteral.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.DateTimeLiteral> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class TimeLiteral extends org.rascalmpl.ast.DateTimeLiteral.TimeLiteral {


public TimeLiteral (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.JustTime __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getTime().interpret(__eval);
	
}

}
static public class DateLiteral extends org.rascalmpl.ast.DateTimeLiteral.DateLiteral {


public DateLiteral (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.JustDate __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return this.getDate().interpret(__eval);
	
}

}
}