package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class CharClass extends AbstractAST { 
  public org.rascalmpl.ast.OptCharRanges getOptionalCharRanges() { throw new UnsupportedOperationException(); }
public boolean hasOptionalCharRanges() { return false; }
public boolean isSimpleCharclass() { return false; }
static public class SimpleCharclass extends CharClass {
/** "[" optionalCharRanges:OptCharRanges "]" -> CharClass {cons("SimpleCharclass")} */
	public SimpleCharclass(INode node, org.rascalmpl.ast.OptCharRanges optionalCharRanges) {
		this.node = node;
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

private final org.rascalmpl.ast.OptCharRanges optionalCharRanges;
	@Override
	public org.rascalmpl.ast.OptCharRanges getOptionalCharRanges() { return optionalCharRanges; }	
}
static public class Ambiguity extends CharClass {
  private final java.util.List<org.rascalmpl.ast.CharClass> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CharClass> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.CharClass> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharClassAmbiguity(this);
  }
} public org.rascalmpl.ast.CharClass getCharClass() { throw new UnsupportedOperationException(); } public boolean hasCharClass() { return false; } public boolean isBracket() { return false; }
static public class Bracket extends CharClass {
/** "(" charClass:CharClass ")" -> CharClass {bracket, cons("Bracket"), avoid} */
	public Bracket(INode node, org.rascalmpl.ast.CharClass charClass) {
		this.node = node;
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

private final org.rascalmpl.ast.CharClass charClass;
	@Override
	public org.rascalmpl.ast.CharClass getCharClass() { return charClass; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isComplement() { return false; }
static public class Complement extends CharClass {
/** "~" charClass:CharClass -> CharClass {cons("Complement")} */
	public Complement(INode node, org.rascalmpl.ast.CharClass charClass) {
		this.node = node;
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

private final org.rascalmpl.ast.CharClass charClass;
	@Override
	public org.rascalmpl.ast.CharClass getCharClass() { return charClass; }	
} public org.rascalmpl.ast.CharClass getLhs() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.CharClass getRhs() { throw new UnsupportedOperationException(); } public boolean hasLhs() { return false; } public boolean hasRhs() { return false; } public boolean isDifference() { return false; }
static public class Difference extends CharClass {
/** lhs:CharClass "/" rhs:CharClass -> CharClass {cons("Difference"), left, memo} */
	public Difference(INode node, org.rascalmpl.ast.CharClass lhs, org.rascalmpl.ast.CharClass rhs) {
		this.node = node;
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

private final org.rascalmpl.ast.CharClass lhs;
	@Override
	public org.rascalmpl.ast.CharClass getLhs() { return lhs; }
	private final org.rascalmpl.ast.CharClass rhs;
	@Override
	public org.rascalmpl.ast.CharClass getRhs() { return rhs; }	
} public boolean isIntersection() { return false; }
static public class Intersection extends CharClass {
/** lhs:CharClass "/\\" rhs:CharClass -> CharClass {cons("Intersection"), left, memo} */
	public Intersection(INode node, org.rascalmpl.ast.CharClass lhs, org.rascalmpl.ast.CharClass rhs) {
		this.node = node;
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

private final org.rascalmpl.ast.CharClass lhs;
	@Override
	public org.rascalmpl.ast.CharClass getLhs() { return lhs; }
	private final org.rascalmpl.ast.CharClass rhs;
	@Override
	public org.rascalmpl.ast.CharClass getRhs() { return rhs; }	
} public boolean isUnion() { return false; }
static public class Union extends CharClass {
/** lhs:CharClass "\\/" rhs:CharClass -> CharClass {cons("Union"), left} */
	public Union(INode node, org.rascalmpl.ast.CharClass lhs, org.rascalmpl.ast.CharClass rhs) {
		this.node = node;
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

private final org.rascalmpl.ast.CharClass lhs;
	@Override
	public org.rascalmpl.ast.CharClass getLhs() { return lhs; }
	private final org.rascalmpl.ast.CharClass rhs;
	@Override
	public org.rascalmpl.ast.CharClass getRhs() { return rhs; }	
}
}