package org.rascalmpl.semantics.dynamic;

public abstract class Strategy extends org.rascalmpl.ast.Strategy {


public Strategy (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class TopDown extends org.rascalmpl.ast.Strategy.TopDown {


public TopDown (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class BottomUp extends org.rascalmpl.ast.Strategy.BottomUp {


public BottomUp (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Strategy.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Strategy> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Outermost extends org.rascalmpl.ast.Strategy.Outermost {


public Outermost (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class BottomUpBreak extends org.rascalmpl.ast.Strategy.BottomUpBreak {


public BottomUpBreak (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Innermost extends org.rascalmpl.ast.Strategy.Innermost {


public Innermost (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class TopDownBreak extends org.rascalmpl.ast.Strategy.TopDownBreak {


public TopDownBreak (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}