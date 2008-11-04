package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class CharClass extends AbstractAST { 
public org.meta_environment.rascal.ast.OptCharRanges getOptionalCharRanges() { throw new UnsupportedOperationException(); }
public boolean hasOptionalCharRanges() { return false; }
public boolean isSimpleCharclass() { return false; }
static public class SimpleCharclass extends CharClass {
/* "[" optionalCharRanges:OptCharRanges "]" -> CharClass {cons("SimpleCharclass")} */
	private SimpleCharclass() { }
	/*package*/ SimpleCharclass(ITree tree, org.meta_environment.rascal.ast.OptCharRanges optionalCharRanges) {
		this.tree = tree;
		this.optionalCharRanges = optionalCharRanges;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitCharClassSimpleCharclass(this);
	}

	public boolean isSimpleCharclass() { return true; }

	public boolean hasOptionalCharRanges() { return true; }

private org.meta_environment.rascal.ast.OptCharRanges optionalCharRanges;
	public org.meta_environment.rascal.ast.OptCharRanges getOptionalCharRanges() { return optionalCharRanges; }
	private void $setOptionalCharRanges(org.meta_environment.rascal.ast.OptCharRanges x) { this.optionalCharRanges = x; }
	public SimpleCharclass setOptionalCharRanges(org.meta_environment.rascal.ast.OptCharRanges x) { 
		SimpleCharclass z = new SimpleCharclass();
 		z.$setOptionalCharRanges(x);
		return z;
	}	
}
static public class Ambiguity extends CharClass {
  private final java.util.List<org.meta_environment.rascal.ast.CharClass> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.CharClass> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.CharClass> getAlternatives() {
	return alternatives;
  }
} public org.meta_environment.rascal.ast.CharClass getCharClass() { throw new UnsupportedOperationException(); } public boolean hasCharClass() { return false; } public boolean isBracket() { return false; }
static public class Bracket extends CharClass {
/* "(" charClass:CharClass ")" -> CharClass {bracket, cons("Bracket"), avoid} */
	private Bracket() { }
	/*package*/ Bracket(ITree tree, org.meta_environment.rascal.ast.CharClass charClass) {
		this.tree = tree;
		this.charClass = charClass;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitCharClassBracket(this);
	}

	public boolean isBracket() { return true; }

	public boolean hasCharClass() { return true; }

private org.meta_environment.rascal.ast.CharClass charClass;
	public org.meta_environment.rascal.ast.CharClass getCharClass() { return charClass; }
	private void $setCharClass(org.meta_environment.rascal.ast.CharClass x) { this.charClass = x; }
	public Bracket setCharClass(org.meta_environment.rascal.ast.CharClass x) { 
		Bracket z = new Bracket();
 		z.$setCharClass(x);
		return z;
	}	
} public boolean isComplement() { return false; }
static public class Complement extends CharClass {
/* "~" charClass:CharClass -> CharClass {cons("Complement")} */
	private Complement() { }
	/*package*/ Complement(ITree tree, org.meta_environment.rascal.ast.CharClass charClass) {
		this.tree = tree;
		this.charClass = charClass;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitCharClassComplement(this);
	}

	public boolean isComplement() { return true; }

	public boolean hasCharClass() { return true; }

private org.meta_environment.rascal.ast.CharClass charClass;
	public org.meta_environment.rascal.ast.CharClass getCharClass() { return charClass; }
	private void $setCharClass(org.meta_environment.rascal.ast.CharClass x) { this.charClass = x; }
	public Complement setCharClass(org.meta_environment.rascal.ast.CharClass x) { 
		Complement z = new Complement();
 		z.$setCharClass(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.CharClass getLhs() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.CharClass getRhs() { throw new UnsupportedOperationException(); } public boolean hasLhs() { return false; } public boolean hasRhs() { return false; } public boolean isDifference() { return false; }
static public class Difference extends CharClass {
/* lhs:CharClass "/" rhs:CharClass -> CharClass {cons("Difference"), left, memo} */
	private Difference() { }
	/*package*/ Difference(ITree tree, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) {
		this.tree = tree;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitCharClassDifference(this);
	}

	public boolean isDifference() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.CharClass lhs;
	public org.meta_environment.rascal.ast.CharClass getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.CharClass x) { this.lhs = x; }
	public Difference setLhs(org.meta_environment.rascal.ast.CharClass x) { 
		Difference z = new Difference();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.CharClass rhs;
	public org.meta_environment.rascal.ast.CharClass getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.CharClass x) { this.rhs = x; }
	public Difference setRhs(org.meta_environment.rascal.ast.CharClass x) { 
		Difference z = new Difference();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isIntersection() { return false; }
static public class Intersection extends CharClass {
/* lhs:CharClass "/\\" rhs:CharClass -> CharClass {cons("Intersection"), left, memo} */
	private Intersection() { }
	/*package*/ Intersection(ITree tree, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) {
		this.tree = tree;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitCharClassIntersection(this);
	}

	public boolean isIntersection() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.CharClass lhs;
	public org.meta_environment.rascal.ast.CharClass getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.CharClass x) { this.lhs = x; }
	public Intersection setLhs(org.meta_environment.rascal.ast.CharClass x) { 
		Intersection z = new Intersection();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.CharClass rhs;
	public org.meta_environment.rascal.ast.CharClass getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.CharClass x) { this.rhs = x; }
	public Intersection setRhs(org.meta_environment.rascal.ast.CharClass x) { 
		Intersection z = new Intersection();
 		z.$setRhs(x);
		return z;
	}	
} public boolean isUnion() { return false; }
static public class Union extends CharClass {
/* lhs:CharClass "\\/" rhs:CharClass -> CharClass {cons("Union"), left} */
	private Union() { }
	/*package*/ Union(ITree tree, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) {
		this.tree = tree;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitCharClassUnion(this);
	}

	public boolean isUnion() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.CharClass lhs;
	public org.meta_environment.rascal.ast.CharClass getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.CharClass x) { this.lhs = x; }
	public Union setLhs(org.meta_environment.rascal.ast.CharClass x) { 
		Union z = new Union();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.CharClass rhs;
	public org.meta_environment.rascal.ast.CharClass getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.CharClass x) { this.rhs = x; }
	public Union setRhs(org.meta_environment.rascal.ast.CharClass x) { 
		Union z = new Union();
 		z.$setRhs(x);
		return z;
	}	
}
}