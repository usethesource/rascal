package org.rascalmpl.semantics.dynamic;

public abstract class DataTypeSelector extends org.rascalmpl.ast.DataTypeSelector {


public DataTypeSelector (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.DataTypeSelector.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.DataTypeSelector> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Selector extends org.rascalmpl.ast.DataTypeSelector.Selector {


public Selector (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2,org.rascalmpl.ast.Name __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
			org.eclipse.imp.pdb.facts.type.Type adt;
			org.rascalmpl.ast.QualifiedName sort = this.getSort();
			java.lang.String name = org.rascalmpl.interpreter.utils.Names.typeName(sort);
			
			if (org.rascalmpl.interpreter.utils.Names.isQualified(sort)) {
				org.rascalmpl.interpreter.env.ModuleEnvironment mod = __eval.__getHeap().getModule(org.rascalmpl.interpreter.utils.Names.moduleName(sort));
				
				if (mod == null) {
					throw new org.rascalmpl.interpreter.staticErrors.UndeclaredModuleError(org.rascalmpl.interpreter.utils.Names.moduleName(sort), sort);
				}
				
				adt = mod.lookupAbstractDataType(name);
			}
			else {
				adt = __eval.__getEnv().lookupAbstractDataType(name);
			}
			
			if (adt == null) {
				throw new org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError(name, this);
			}
			
			java.lang.String constructor = org.rascalmpl.interpreter.utils.Names.name(this.getProduction());
			java.util.Set<org.eclipse.imp.pdb.facts.type.Type> constructors = __eval.__getEnv().lookupConstructor(adt, constructor);
			
		    if (constructors.size() == 0) {
		    	throw new org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError(name + "." + constructor, this);
		    }
		    else if (constructors.size() > 1) {
		    	throw new org.rascalmpl.interpreter.staticErrors.AmbiguousFunctionReferenceError(name + "." + constructor, this);
		    }
		    else {
		    	return constructors.iterator().next();
		    }
		
}

}
}