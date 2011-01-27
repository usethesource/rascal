package org.rascalmpl.semantics.dynamic;

public abstract class Comprehension extends org.rascalmpl.ast.Comprehension {


public Comprehension (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class List extends org.rascalmpl.ast.Comprehension.List {


public List (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Expression> __param2,java.util.List<org.rascalmpl.ast.Expression> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalComprehension(
				this.getGenerators(),
				__eval.new ListComprehensionWriter(this.getResults(), __eval));
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Comprehension.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Comprehension> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Map extends org.rascalmpl.ast.Comprehension.Map {


public Map (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Expression __param3,java.util.List<org.rascalmpl.ast.Expression> __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.util.List<org.rascalmpl.ast.Expression> resultExprs = new java.util.ArrayList<org.rascalmpl.ast.Expression>();
		resultExprs.add(this.getFrom());
		resultExprs.add(this.getTo());
		return __eval.evalComprehension(
				this.getGenerators(),
				__eval.new MapComprehensionWriter(resultExprs, __eval));
	
}

}
static public class Set extends org.rascalmpl.ast.Comprehension.Set {


public Set (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Expression> __param2,java.util.List<org.rascalmpl.ast.Expression> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return __eval.evalComprehension(
				this.getGenerators(),
				__eval.new SetComprehensionWriter(this.getResults(), __eval));
	
}

}
}