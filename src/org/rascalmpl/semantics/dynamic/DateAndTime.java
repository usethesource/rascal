package org.rascalmpl.semantics.dynamic;

public abstract class DateAndTime extends org.rascalmpl.ast.DateAndTime {


public DateAndTime (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.DateAndTime.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.DateAndTime> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Lexical extends org.rascalmpl.ast.DateAndTime.Lexical {


public Lexical (org.eclipse.imp.pdb.facts.INode __param1,java.lang.String __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		// Split into date and time components; of the form $<date>T<time>
		java.lang.String dtPart = this.getString().substring(1); 
		java.lang.String datePart = dtPart.substring(0,dtPart.indexOf("T"));
		java.lang.String timePart = dtPart.substring(dtPart.indexOf("T")+1);
		
		return __eval.createVisitedDateTime(datePart, timePart, this);
		
	
}

}
}