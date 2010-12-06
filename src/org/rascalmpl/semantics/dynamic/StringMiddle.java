package org.rascalmpl.semantics.dynamic;

public abstract class StringMiddle extends org.rascalmpl.ast.StringMiddle {


public StringMiddle (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.StringMiddle.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.StringMiddle> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Template extends org.rascalmpl.ast.StringMiddle.Template {


public Template (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.MidStringChars __param2,org.rascalmpl.ast.StringTemplate __param3,org.rascalmpl.ast.StringMiddle __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Mid extends org.rascalmpl.ast.StringMiddle.Mid {


public Mid (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.MidStringChars __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Interpolated extends org.rascalmpl.ast.StringMiddle.Interpolated {


public Interpolated (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.MidStringChars __param2,org.rascalmpl.ast.Expression __param3,org.rascalmpl.ast.StringMiddle __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}