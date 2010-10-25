package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class CharRanges extends AbstractAST { 
  public org.rascalmpl.ast.CharRange getRange() { throw new UnsupportedOperationException(); }
public boolean hasRange() { return false; }
public boolean isRange() { return false; }
static public class Range extends CharRanges {
/** range:CharRange -> CharRanges {cons("Range")} */
	protected Range(INode node, org.rascalmpl.ast.CharRange range) {
		this.node = node;
		this.range = range;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharRangesRange(this);
	}

	public boolean isRange() { return true; }

	public boolean hasRange() { return true; }

private final org.rascalmpl.ast.CharRange range;
	public org.rascalmpl.ast.CharRange getRange() { return range; }	
}
static public class Ambiguity extends CharRanges {
  private final java.util.List<org.rascalmpl.ast.CharRanges> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CharRanges> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.CharRanges> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharRangesAmbiguity(this);
  }
} 
public org.rascalmpl.ast.CharRanges getLhs() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.CharRanges getRhs() { throw new UnsupportedOperationException(); }
public boolean hasLhs() { return false; }
	public boolean hasRhs() { return false; }
public boolean isConcatenate() { return false; }
static public class Concatenate extends CharRanges {
/** lhs:CharRanges rhs:CharRanges -> CharRanges {cons("Concatenate"), right, memo} */
	protected Concatenate(INode node, org.rascalmpl.ast.CharRanges lhs, org.rascalmpl.ast.CharRanges rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharRangesConcatenate(this);
	}

	public boolean isConcatenate() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.CharRanges lhs;
	public org.rascalmpl.ast.CharRanges getLhs() { return lhs; }
	private final org.rascalmpl.ast.CharRanges rhs;
	public org.rascalmpl.ast.CharRanges getRhs() { return rhs; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.CharRanges getRanges() { throw new UnsupportedOperationException(); }
public boolean hasRanges() { return false; }
public boolean isBracket() { return false; }
static public class Bracket extends CharRanges {
/** "(" ranges:CharRanges ")" -> CharRanges {bracket, cons("Bracket")} */
	protected Bracket(INode node, org.rascalmpl.ast.CharRanges ranges) {
		this.node = node;
		this.ranges = ranges;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharRangesBracket(this);
	}

	public boolean isBracket() { return true; }

	public boolean hasRanges() { return true; }

private final org.rascalmpl.ast.CharRanges ranges;
	public org.rascalmpl.ast.CharRanges getRanges() { return ranges; }	
}
}