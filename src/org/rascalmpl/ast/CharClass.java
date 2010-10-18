package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class CharClass extends AbstractAST { 
  public org.rascalmpl.ast.OptCharRanges getOptionalCharRanges() { throw new UnsupportedOperationException(); }
public boolean hasOptionalCharRanges() { return false; }
public boolean isSimpleCharclass() { return false; }
static public class SimpleCharclass extends CharClass {
/** "[" optionalCharRanges:OptCharRanges "]" -> CharClass {cons("SimpleCharclass")} */
	protected SimpleCharclass(INode node, org.rascalmpl.ast.OptCharRanges optionalCharRanges) {
		this.node = node;
		this.optionalCharRanges = optionalCharRanges;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassSimpleCharclass(this);
	}

	public boolean isSimpleCharclass() { return true; }

	public boolean hasOptionalCharRanges() { return true; }

private final org.rascalmpl.ast.OptCharRanges optionalCharRanges;
	public org.rascalmpl.ast.OptCharRanges getOptionalCharRanges() { return optionalCharRanges; }	
}
static public class Ambiguity extends CharClass {
  private final java.util.List<org.rascalmpl.ast.CharClass> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CharClass> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.CharClass> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharClassAmbiguity(this);
  }
} public org.rascalmpl.ast.CharClass getCharClass() { throw new UnsupportedOperationException(); } public boolean hasCharClass() { return false; } public boolean isBracket() { return false; }
static public class Bracket extends CharClass {
/** "(" charClass:CharClass ")" -> CharClass {bracket, cons("Bracket"), avoid} */
	protected Bracket(INode node, org.rascalmpl.ast.CharClass charClass) {
		this.node = node;
		this.charClass = charClass;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassBracket(this);
	}

	public boolean isBracket() { return true; }

	public boolean hasCharClass() { return true; }

private final org.rascalmpl.ast.CharClass charClass;
	public org.rascalmpl.ast.CharClass getCharClass() { return charClass; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isComplement() { return false; }
static public class Complement extends CharClass {
/** "~" charClass:CharClass -> CharClass {cons("Complement")} */
	protected Complement(INode node, org.rascalmpl.ast.CharClass charClass) {
		this.node = node;
		this.charClass = charClass;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassComplement(this);
	}

	public boolean isComplement() { return true; }

	public boolean hasCharClass() { return true; }

private final org.rascalmpl.ast.CharClass charClass;
	public org.rascalmpl.ast.CharClass getCharClass() { return charClass; }	
} public org.rascalmpl.ast.CharClass getLhs() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.CharClass getRhs() { throw new UnsupportedOperationException(); } public boolean hasLhs() { return false; } public boolean hasRhs() { return false; } public boolean isDifference() { return false; }
static public class Difference extends CharClass {
/** lhs:CharClass "/" rhs:CharClass -> CharClass {cons("Difference"), left, memo} */
	protected Difference(INode node, org.rascalmpl.ast.CharClass lhs, org.rascalmpl.ast.CharClass rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassDifference(this);
	}

	public boolean isDifference() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.CharClass lhs;
	public org.rascalmpl.ast.CharClass getLhs() { return lhs; }
	private final org.rascalmpl.ast.CharClass rhs;
	public org.rascalmpl.ast.CharClass getRhs() { return rhs; }	
} public boolean isIntersection() { return false; }
static public class Intersection extends CharClass {
/** lhs:CharClass "/\\" rhs:CharClass -> CharClass {cons("Intersection"), left, memo} */
	protected Intersection(INode node, org.rascalmpl.ast.CharClass lhs, org.rascalmpl.ast.CharClass rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassIntersection(this);
	}

	public boolean isIntersection() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.CharClass lhs;
	public org.rascalmpl.ast.CharClass getLhs() { return lhs; }
	private final org.rascalmpl.ast.CharClass rhs;
	public org.rascalmpl.ast.CharClass getRhs() { return rhs; }	
} public boolean isUnion() { return false; }
static public class Union extends CharClass {
/** lhs:CharClass "\\/" rhs:CharClass -> CharClass {cons("Union"), left} */
	protected Union(INode node, org.rascalmpl.ast.CharClass lhs, org.rascalmpl.ast.CharClass rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharClassUnion(this);
	}

	public boolean isUnion() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.CharClass lhs;
	public org.rascalmpl.ast.CharClass getLhs() { return lhs; }
	private final org.rascalmpl.ast.CharClass rhs;
	public org.rascalmpl.ast.CharClass getRhs() { return rhs; }	
}
}