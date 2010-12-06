package org.rascalmpl.semantics.dynamic;

public abstract class ImportedModule extends org.rascalmpl.ast.ImportedModule {


public ImportedModule (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Default extends org.rascalmpl.ast.ImportedModule.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class ActualsRenaming extends org.rascalmpl.ast.ImportedModule.ActualsRenaming {


public ActualsRenaming (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2,org.rascalmpl.ast.ModuleActuals __param3,org.rascalmpl.ast.Renamings __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Actuals extends org.rascalmpl.ast.ImportedModule.Actuals {


public Actuals (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2,org.rascalmpl.ast.ModuleActuals __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Renamings extends org.rascalmpl.ast.ImportedModule.Renamings {


public Renamings (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2,org.rascalmpl.ast.Renamings __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.ImportedModule.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.ImportedModule> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}