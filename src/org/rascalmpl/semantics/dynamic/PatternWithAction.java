package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.values.uptr.Factory;

public abstract class PatternWithAction extends org.rascalmpl.ast.PatternWithAction {


public PatternWithAction (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.PatternWithAction.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.PatternWithAction> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Arbitrary extends org.rascalmpl.ast.PatternWithAction.Arbitrary {


public Arbitrary (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Statement __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.matching.IMatchingResult pv = this.getPattern().__evaluate((org.rascalmpl.interpreter.PatternEvaluator)__eval.__getPatternEvaluator());
		
		org.eclipse.imp.pdb.facts.type.Type pt = pv.getType(__eval.getCurrentEnvt());
		
		if (pv instanceof org.rascalmpl.interpreter.matching.NodePattern) {
			pt = ((org.rascalmpl.interpreter.matching.NodePattern) pv).getConstructorType(__eval.getCurrentEnvt());
		}
		
		// TODO store rules for concrete syntax on production rule and
		// create Lambda's for production rules to speed up matching and
		// rewrite rule look up
		if (pt instanceof org.rascalmpl.interpreter.types.NonTerminalType) {
			pt = Factory.Tree_Appl;
		}
		
		if(!(pt.isAbstractDataType() || pt.isConstructorType() || pt.isNodeType()))
			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().nodeType(), pt, this);
		
		__eval.__getHeap().storeRule(pt, this, __eval.getCurrentModuleEnvironment());
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
static public class Replacing extends org.rascalmpl.ast.PatternWithAction.Replacing {


public Replacing (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Replacement __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.matching.IMatchingResult pv = this.getPattern().__evaluate((org.rascalmpl.interpreter.PatternEvaluator)__eval.__getPatternEvaluator());
		org.eclipse.imp.pdb.facts.type.Type pt = pv.getType(__eval.getCurrentEnvt());

		if (pv instanceof org.rascalmpl.interpreter.matching.NodePattern) {
			pt = ((org.rascalmpl.interpreter.matching.NodePattern) pv).getConstructorType(__eval.getCurrentEnvt());
		}
		
		if (pt instanceof org.rascalmpl.interpreter.types.NonTerminalType) {
			pt = Factory.Tree_Appl;
		}

		if(!(pt.isAbstractDataType() || pt.isConstructorType() || pt.isNodeType()))
			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().nodeType(), pt, this);
		
		__eval.__getHeap().storeRule(pt, this, __eval.getCurrentModuleEnvironment());
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
}