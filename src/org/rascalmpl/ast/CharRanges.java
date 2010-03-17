package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class CharRanges extends AbstractAST { 
  public org.rascalmpl.ast.CharRange getRange() { throw new UnsupportedOperationException(); }
public boolean hasRange() { return false; }
public boolean isRange() { return false; }
static public class Range extends CharRanges {
/** range:CharRange -> CharRanges {cons("Range")} */
	public Range(INode node, org.rascalmpl.ast.CharRange range) {
		this.node = node;
		this.range = range;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharRangesRange(this);
	}

	@Override
	public boolean isRange() { return true; }

	@Override
	public boolean hasRange() { return true; }

private final org.rascalmpl.ast.CharRange range;
	@Override
	public org.rascalmpl.ast.CharRange getRange() { return range; }	
}
static public class Ambiguity extends CharRanges {
  private final java.util.List<org.rascalmpl.ast.CharRanges> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CharRanges> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.CharRanges> getAlternatives() {
	return alternatives;
  }
  
  @Override
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
	public Concatenate(INode node, org.rascalmpl.ast.CharRanges lhs, org.rascalmpl.ast.CharRanges rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharRangesConcatenate(this);
	}

	@Override
	public boolean isConcatenate() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.CharRanges lhs;
	@Override
	public org.rascalmpl.ast.CharRanges getLhs() { return lhs; }
	private final org.rascalmpl.ast.CharRanges rhs;
	@Override
	public org.rascalmpl.ast.CharRanges getRhs() { return rhs; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.CharRanges getRanges() { throw new UnsupportedOperationException(); }
public boolean hasRanges() { return false; }
public boolean isBracket() { return false; }
static public class Bracket extends CharRanges {
/** "(" ranges:CharRanges ")" -> CharRanges {bracket, cons("Bracket")} */
	public Bracket(INode node, org.rascalmpl.ast.CharRanges ranges) {
		this.node = node;
		this.ranges = ranges;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCharRangesBracket(this);
	}

	@Override
	public boolean isBracket() { return true; }

	@Override
	public boolean hasRanges() { return true; }

private final org.rascalmpl.ast.CharRanges ranges;
	@Override
	public org.rascalmpl.ast.CharRanges getRanges() { return ranges; }	
}
}