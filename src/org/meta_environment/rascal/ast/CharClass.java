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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassSimpleCharclass(this);
	}

	@Override
	public boolean isSimpleCharclass() { return true; }

	@Override
	public boolean hasOptionalCharRanges() { return true; }

private org.meta_environment.rascal.ast.OptCharRanges optionalCharRanges;
	@Override
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
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharClassAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.CharClass getCharClass() { throw new UnsupportedOperationException(); } public boolean hasCharClass() { return false; } public boolean isBracket() { return false; }
static public class Bracket extends CharClass {
/* "(" charClass:CharClass ")" -> CharClass {bracket, cons("Bracket"), avoid} */
	private Bracket() { }
	/*package*/ Bracket(ITree tree, org.meta_environment.rascal.ast.CharClass charClass) {
		this.tree = tree;
		this.charClass = charClass;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassBracket(this);
	}

	@Override
	public boolean isBracket() { return true; }

	@Override
	public boolean hasCharClass() { return true; }

private org.meta_environment.rascal.ast.CharClass charClass;
	@Override
	public org.meta_environment.rascal.ast.CharClass getCharClass() { return charClass; }
	private void $setCharClass(org.meta_environment.rascal.ast.CharClass x) { this.charClass = x; }
	public Bracket setCharClass(org.meta_environment.rascal.ast.CharClass x) { 
		Bracket z = new Bracket();
 		z.$setCharClass(x);
		return z;
	}	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isComplement() { return false; }
static public class Complement extends CharClass {
/* "~" charClass:CharClass -> CharClass {cons("Complement")} */
	private Complement() { }
	/*package*/ Complement(ITree tree, org.meta_environment.rascal.ast.CharClass charClass) {
		this.tree = tree;
		this.charClass = charClass;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassComplement(this);
	}

	@Override
	public boolean isComplement() { return true; }

	@Override
	public boolean hasCharClass() { return true; }

private org.meta_environment.rascal.ast.CharClass charClass;
	@Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassDifference(this);
	}

	@Override
	public boolean isDifference() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.CharClass lhs;
	@Override
	public org.meta_environment.rascal.ast.CharClass getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.CharClass x) { this.lhs = x; }
	public Difference setLhs(org.meta_environment.rascal.ast.CharClass x) { 
		Difference z = new Difference();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.CharClass rhs;
	@Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassIntersection(this);
	}

	@Override
	public boolean isIntersection() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.CharClass lhs;
	@Override
	public org.meta_environment.rascal.ast.CharClass getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.CharClass x) { this.lhs = x; }
	public Intersection setLhs(org.meta_environment.rascal.ast.CharClass x) { 
		Intersection z = new Intersection();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.CharClass rhs;
	@Override
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
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassUnion(this);
	}

	@Override
	public boolean isUnion() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.CharClass lhs;
	@Override
	public org.meta_environment.rascal.ast.CharClass getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.CharClass x) { this.lhs = x; }
	public Union setLhs(org.meta_environment.rascal.ast.CharClass x) { 
		Union z = new Union();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.CharClass rhs;
	@Override
	public org.meta_environment.rascal.ast.CharClass getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.CharClass x) { this.rhs = x; }
	public Union setRhs(org.meta_environment.rascal.ast.CharClass x) { 
		Union z = new Union();
 		z.$setRhs(x);
		return z;
	}	
}
}