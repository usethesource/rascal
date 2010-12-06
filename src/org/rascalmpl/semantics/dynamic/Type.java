package org.rascalmpl.semantics.dynamic;

public abstract class Type extends org.rascalmpl.ast.Type {


public Type (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Structured extends org.rascalmpl.ast.Type.Structured {


public Structured (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.StructuredType __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return this.getStructured().__evaluate(__eval);
		
}

}
static public class Selector extends org.rascalmpl.ast.Type.Selector {


public Selector (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.DataTypeSelector __param2) {
	super(__param1,__param2);
}
@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return this.getSelector().__evaluate(__eval);
		
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Basic extends org.rascalmpl.ast.Type.Basic {


public Basic (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.BasicType __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return this.getBasic().__evaluate(__eval);
		
}

}
static public class Ambiguity extends org.rascalmpl.ast.Type.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Type> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			throw new org.rascalmpl.interpreter.asserts.Ambiguous((org.eclipse.imp.pdb.facts.IConstructor) this.getTree());
		
}

}
static public class Variable extends org.rascalmpl.ast.Type.Variable {


public Variable (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.TypeVar __param2) {
	super(__param1,__param2);
}
@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			org.rascalmpl.ast.TypeVar var = this.getTypeVar();
			org.eclipse.imp.pdb.facts.type.Type param;

			if (var.isBounded()) {
				param = org.rascalmpl.interpreter.TypeEvaluator.__getTf().parameterType(org.rascalmpl.interpreter.utils.Names.name(var.getName()), var
						.getBound().__evaluate(__eval));
			} 
			else {
				param = org.rascalmpl.interpreter.TypeEvaluator.__getTf().parameterType(org.rascalmpl.interpreter.utils.Names.name(var.getName()));
			}
			if (__eval.__getEnv() != null) {
				return param.instantiate(__eval.__getEnv().getTypeBindings());
			}
			return param;
		
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class User extends org.rascalmpl.ast.Type.User {


public User (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.UserType __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return this.getUser().__evaluate(__eval);
		
}

}
static public class Bracket extends org.rascalmpl.ast.Type.Bracket {


public Bracket (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Type __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return this.getType().__evaluate(__eval);
		
}

}
static public class Function extends org.rascalmpl.ast.Type.Function {


public Function (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.FunctionType __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return this.getFunction().__evaluate(__eval);
		
}

}
static public class Symbol extends org.rascalmpl.ast.Type.Symbol {


public Symbol (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Sym __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			return org.rascalmpl.interpreter.types.RascalTypeFactory.getInstance().nonTerminalType(this);
		
}

}
}