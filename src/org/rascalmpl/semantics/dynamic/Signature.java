package org.rascalmpl.semantics.dynamic;

public abstract class Signature extends org.rascalmpl.ast.Signature {


public Signature (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class WithThrows extends org.rascalmpl.ast.Signature.WithThrows {


public WithThrows (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2,org.rascalmpl.ast.FunctionModifiers __param3,org.rascalmpl.ast.Name __param4,org.rascalmpl.ast.Parameters __param5,java.util.List<org.rascalmpl.ast.Type> __param6) {
	super(__param1,__param2,__param3,__param4,__param5,__param6);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().functionType(this.getType().__evaluate(__eval), this.getParameters().__evaluate(__eval));
		
}

}
static public class NoThrows extends org.rascalmpl.ast.Signature.NoThrows {


public NoThrows (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2,org.rascalmpl.ast.FunctionModifiers __param3,org.rascalmpl.ast.Name __param4,org.rascalmpl.ast.Parameters __param5) {
	super(__param1,__param2,__param3,__param4,__param5);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().functionType(this.getType().__evaluate(__eval), this.getParameters().__evaluate(__eval));
		
}

}
static public class Ambiguity extends org.rascalmpl.ast.Signature.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Signature> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}