package org.rascalmpl.semantics.dynamic;

public abstract class Sym extends org.rascalmpl.ast.Sym {


public Sym (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.Sym.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Sym> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class StartOfLine extends org.rascalmpl.ast.Sym.StartOfLine {


public StartOfLine (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class CharacterClass extends org.rascalmpl.ast.Sym.CharacterClass {


public CharacterClass (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Class __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Literal extends org.rascalmpl.ast.Sym.Literal {


public Literal (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.StringConstant __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class CaseInsensitiveLiteral extends org.rascalmpl.ast.Sym.CaseInsensitiveLiteral {


public CaseInsensitiveLiteral (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.CaseInsensitiveStringConstant __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Parameter extends org.rascalmpl.ast.Sym.Parameter {


public Parameter (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Nonterminal __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Nonterminal extends org.rascalmpl.ast.Sym.Nonterminal {


public Nonterminal (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Nonterminal __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Iter extends org.rascalmpl.ast.Sym.Iter {


public Iter (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Sym __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class IterStar extends org.rascalmpl.ast.Sym.IterStar {


public IterStar (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Sym __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class IterStarSep extends org.rascalmpl.ast.Sym.IterStarSep {


public IterStarSep (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Sym __param2,org.rascalmpl.ast.StringConstant __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class EndOfLine extends org.rascalmpl.ast.Sym.EndOfLine {


public EndOfLine (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Column extends org.rascalmpl.ast.Sym.Column {


public Column (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.IntegerLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Parametrized extends org.rascalmpl.ast.Sym.Parametrized {


public Parametrized (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.ParameterizedNonterminal __param2,java.util.List<org.rascalmpl.ast.Sym> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class IterSep extends org.rascalmpl.ast.Sym.IterSep {


public IterSep (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Sym __param2,org.rascalmpl.ast.StringConstant __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Optional extends org.rascalmpl.ast.Sym.Optional {


public Optional (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Sym __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Labeled extends org.rascalmpl.ast.Sym.Labeled {


public Labeled (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Sym __param2,org.rascalmpl.ast.NonterminalLabel __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}