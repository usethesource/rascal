package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.interpreter.TraversalEvaluator.DIRECTION;
import org.rascalmpl.interpreter.TraversalEvaluator.FIXEDPOINT;
import org.rascalmpl.interpreter.TraversalEvaluator.PROGRESS;

public abstract class Visit extends org.rascalmpl.ast.Visit {


public Visit (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class DefaultStrategy extends org.rascalmpl.ast.Visit.DefaultStrategy {


public DefaultStrategy (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,java.util.List<org.rascalmpl.ast.Case> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	

		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> subject = this.getSubject().interpret(__eval);
		java.util.List<org.rascalmpl.ast.Case> cases = this.getCases();
		org.rascalmpl.interpreter.TraversalEvaluator te = new org.rascalmpl.interpreter.TraversalEvaluator(__eval);

		org.rascalmpl.interpreter.TraverseResult tr = te.traverse(subject.getValue(), te.new CasesOrRules(cases), 
				DIRECTION.BottomUp,
				PROGRESS.Continuing,
				FIXEDPOINT.No);
		org.eclipse.imp.pdb.facts.type.Type t = tr.value.getType();
		org.eclipse.imp.pdb.facts.IValue val = tr.value;
		org.rascalmpl.interpreter.TraverseResultFactory.freeTraverseResult(tr);
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(t, val, __eval);
	
}

}
static public class GivenStrategy extends org.rascalmpl.ast.Visit.GivenStrategy {


public GivenStrategy (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Strategy __param2,org.rascalmpl.ast.Expression __param3,java.util.List<org.rascalmpl.ast.Case> __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> subject = this.getSubject().interpret(__eval);

		// TODO: warning switched to static type here, but not sure if that's correct...
		org.eclipse.imp.pdb.facts.type.Type subjectType = subject.getType();

		if(subjectType.isConstructorType()){
			subjectType = subjectType.getAbstractDataType();
		}

		java.util.List<org.rascalmpl.ast.Case> cases = this.getCases();
		org.rascalmpl.ast.Strategy s = this.getStrategy();

		org.rascalmpl.interpreter.TraversalEvaluator.DIRECTION direction = DIRECTION.BottomUp;
		org.rascalmpl.interpreter.TraversalEvaluator.PROGRESS progress = PROGRESS.Continuing;
		org.rascalmpl.interpreter.TraversalEvaluator.FIXEDPOINT fixedpoint = FIXEDPOINT.No;

		if(s.isBottomUp()){
			direction = DIRECTION.BottomUp;
		} else if(s.isBottomUpBreak()){
			direction = DIRECTION.BottomUp;
			progress = PROGRESS.Breaking;
		} else if(s.isInnermost()){
			direction = DIRECTION.BottomUp;
			fixedpoint = FIXEDPOINT.Yes;
		} else if(s.isTopDown()){
			direction = DIRECTION.TopDown;
		} else if(s.isTopDownBreak()){
			direction = DIRECTION.TopDown;
			progress = PROGRESS.Breaking;
		} else if(s.isOutermost()){
			direction = DIRECTION.TopDown;
			fixedpoint = FIXEDPOINT.Yes;
		} else {
			throw new org.rascalmpl.interpreter.asserts.ImplementationError("Unknown strategy " + s);
		}

		org.rascalmpl.interpreter.TraversalEvaluator te = new org.rascalmpl.interpreter.TraversalEvaluator(__eval);
		org.rascalmpl.interpreter.TraverseResult tr = te.traverse(subject.getValue(), te.new CasesOrRules(cases), direction, progress, fixedpoint);
		org.eclipse.imp.pdb.facts.type.Type t = tr.value.getType();
		org.eclipse.imp.pdb.facts.IValue val = tr.value;
		org.rascalmpl.interpreter.TraverseResultFactory.freeTraverseResult(tr);
		return org.rascalmpl.interpreter.result.ResultFactory.makeResult(t, val, __eval);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Visit.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Visit> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}