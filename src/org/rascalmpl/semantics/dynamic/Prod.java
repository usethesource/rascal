package org.rascalmpl.semantics.dynamic;

public abstract class Prod extends org.rascalmpl.ast.Prod {


public Prod (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.Prod.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Prod> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Reject extends org.rascalmpl.ast.Prod.Reject {


public Reject (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Prod __param2,org.rascalmpl.ast.Prod __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Reference extends org.rascalmpl.ast.Prod.Reference {


public Reference (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Unlabeled extends org.rascalmpl.ast.Prod.Unlabeled {


public Unlabeled (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.ProdModifier> __param2,java.util.List<org.rascalmpl.ast.Sym> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class AssociativityGroup extends org.rascalmpl.ast.Prod.AssociativityGroup {


public AssociativityGroup (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Assoc __param2,org.rascalmpl.ast.Prod __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Action extends org.rascalmpl.ast.Prod.Action {


public Action (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Prod __param2,org.rascalmpl.ast.LanguageAction __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Follow extends org.rascalmpl.ast.Prod.Follow {


public Follow (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Prod __param2,org.rascalmpl.ast.Prod __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class All extends org.rascalmpl.ast.Prod.All {


public All (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Prod __param2,org.rascalmpl.ast.Prod __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class First extends org.rascalmpl.ast.Prod.First {


public First (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Prod __param2,org.rascalmpl.ast.Prod __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Labeled extends org.rascalmpl.ast.Prod.Labeled {


public Labeled (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.ProdModifier> __param2,org.rascalmpl.ast.Name __param3,java.util.List<org.rascalmpl.ast.Sym> __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Others extends org.rascalmpl.ast.Prod.Others {


public Others (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}