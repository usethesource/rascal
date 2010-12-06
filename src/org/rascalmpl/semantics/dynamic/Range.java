package org.rascalmpl.semantics.dynamic;

public abstract class Range extends org.rascalmpl.ast.Range {


public Range (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Character extends org.rascalmpl.ast.Range.Character {


public Character (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Char __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Range.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Range> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class FromTo extends org.rascalmpl.ast.Range.FromTo {


public FromTo (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Char __param2,org.rascalmpl.ast.Char __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}