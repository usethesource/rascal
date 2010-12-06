package org.rascalmpl.semantics.dynamic;

public abstract class UserType extends org.rascalmpl.ast.UserType {


public UserType (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Name extends org.rascalmpl.ast.UserType.Name {


public Name (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			org.rascalmpl.interpreter.env.Environment theEnv = __eval.getEnvironmentForName(this.getName());
			java.lang.String name = org.rascalmpl.interpreter.utils.Names.typeName(this.getName());

			if (theEnv != null) {
				org.eclipse.imp.pdb.facts.type.Type type = theEnv.lookupAlias(name);

				if (type != null) {
					return type;
				}
				
				org.eclipse.imp.pdb.facts.type.Type tree = theEnv.lookupAbstractDataType(name);

				if (tree != null) {
					return tree;
				}
				
				org.eclipse.imp.pdb.facts.type.Type symbol = theEnv.lookupConcreteSyntaxType(name);
				
				if (symbol != null) {
					return symbol;
				}
			}
			
			throw new org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError(name, this);
		
}

}
static public class Ambiguity extends org.rascalmpl.ast.UserType.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.UserType> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Parametric extends org.rascalmpl.ast.UserType.Parametric {


public Parametric (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2,java.util.List<org.rascalmpl.ast.Type> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			java.lang.String name;
			org.eclipse.imp.pdb.facts.type.Type type = null;
			org.rascalmpl.interpreter.env.Environment theEnv = __eval.getEnvironmentForName(this.getName());

			name = org.rascalmpl.interpreter.utils.Names.typeName(this.getName());

			if (theEnv != null) {
				type = theEnv.lookupAlias(name);

				if (type == null) {
					type = theEnv.lookupAbstractDataType(name);
				}
			}

			if (type != null) {
				java.util.Map<org.eclipse.imp.pdb.facts.type.Type, org.eclipse.imp.pdb.facts.type.Type> bindings = new java.util.HashMap<org.eclipse.imp.pdb.facts.type.Type,org.eclipse.imp.pdb.facts.type.Type>();
				org.eclipse.imp.pdb.facts.type.Type[] params = new org.eclipse.imp.pdb.facts.type.Type[this.getParameters().size()];

				int i = 0;
				for (org.rascalmpl.ast.Type param : this.getParameters()) {
					params[i++] = param.__evaluate(__eval);
				}

				// __eval has side-effects that we might need?
				type.getTypeParameters().match(org.rascalmpl.interpreter.TypeEvaluator.__getTf().tupleType(params), bindings);
				
				// Note that instantiation use type variables from the current context, not the declaring context
				org.eclipse.imp.pdb.facts.type.Type outerInstance = type.instantiate(__eval.__getEnv().getTypeBindings());
				return outerInstance.instantiate(bindings);
			}
			
			throw new org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError(name, this);
		
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}