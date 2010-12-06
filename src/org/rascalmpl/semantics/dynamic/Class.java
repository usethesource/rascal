package org.rascalmpl.semantics.dynamic;

public abstract class Class extends org.rascalmpl.ast.Class {


public Class (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class SimpleCharclass extends org.rascalmpl.ast.Class.SimpleCharclass {


public SimpleCharclass (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Range> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Complement extends org.rascalmpl.ast.Class.Complement {


public Complement (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Class __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Intersection extends org.rascalmpl.ast.Class.Intersection {


public Intersection (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Class __param2,org.rascalmpl.ast.Class __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Bracket extends org.rascalmpl.ast.Class.Bracket {


public Bracket (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Class __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Union extends org.rascalmpl.ast.Class.Union {


public Union (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Class __param2,org.rascalmpl.ast.Class __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Class.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Class> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Difference extends org.rascalmpl.ast.Class.Difference {


public Difference (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Class __param2,org.rascalmpl.ast.Class __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}