package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Class extends AbstractAST { 
  public java.util.List<org.rascalmpl.ast.Range> getRanges() { throw new UnsupportedOperationException(); }
public boolean hasRanges() { return false; }
public boolean isSimpleCharclass() { return false; }
static public class SimpleCharclass extends Class {
/** "[" ranges:Range* "]" -> Class {cons("SimpleCharclass")} */
	public SimpleCharclass(INode node, java.util.List<org.rascalmpl.ast.Range> ranges) {
		this.node = node;
		this.ranges = ranges;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitClassSimpleCharclass(this);
	}

	@Override
	public boolean isSimpleCharclass() { return true; }

	@Override
	public boolean hasRanges() { return true; }

private final java.util.List<org.rascalmpl.ast.Range> ranges;
	@Override
	public java.util.List<org.rascalmpl.ast.Range> getRanges() { return ranges; }	
}
static public class Ambiguity extends Class {
  private final java.util.List<org.rascalmpl.ast.Class> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Class> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Class> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitClassAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Class getCharclass() { throw new UnsupportedOperationException(); }
public boolean hasCharclass() { return false; }
public boolean isBracket() { return false; }
static public class Bracket extends Class {
/** "(" charclass:Class ")" -> Class {cons("Bracket"), bracket} */
	public Bracket(INode node, org.rascalmpl.ast.Class charclass) {
		this.node = node;
		this.charclass = charclass;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitClassBracket(this);
	}

	@Override
	public boolean isBracket() { return true; }

	@Override
	public boolean hasCharclass() { return true; }

private final org.rascalmpl.ast.Class charclass;
	@Override
	public org.rascalmpl.ast.Class getCharclass() { return charclass; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.Class getCharClass() { throw new UnsupportedOperationException(); }
public boolean hasCharClass() { return false; }
public boolean isComplement() { return false; }
static public class Complement extends Class {
/** "!" charClass:Class -> Class {cons("Complement")} */
	public Complement(INode node, org.rascalmpl.ast.Class charClass) {
		this.node = node;
		this.charClass = charClass;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitClassComplement(this);
	}

	@Override
	public boolean isComplement() { return true; }

	@Override
	public boolean hasCharClass() { return true; }

private final org.rascalmpl.ast.Class charClass;
	@Override
	public org.rascalmpl.ast.Class getCharClass() { return charClass; }	
} public org.rascalmpl.ast.Class getLhs() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Class getRhs() { throw new UnsupportedOperationException(); } public boolean hasLhs() { return false; } public boolean hasRhs() { return false; } public boolean isDifference() { return false; }
static public class Difference extends Class {
/** lhs:Class "-" rhs:Class -> Class {cons("Difference"), left} */
	public Difference(INode node, org.rascalmpl.ast.Class lhs, org.rascalmpl.ast.Class rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitClassDifference(this);
	}

	@Override
	public boolean isDifference() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Class lhs;
	@Override
	public org.rascalmpl.ast.Class getLhs() { return lhs; }
	private final org.rascalmpl.ast.Class rhs;
	@Override
	public org.rascalmpl.ast.Class getRhs() { return rhs; }	
} public boolean isIntersection() { return false; }
static public class Intersection extends Class {
/** lhs:Class "&" rhs:Class -> Class {cons("Intersection"), left} */
	public Intersection(INode node, org.rascalmpl.ast.Class lhs, org.rascalmpl.ast.Class rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitClassIntersection(this);
	}

	@Override
	public boolean isIntersection() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Class lhs;
	@Override
	public org.rascalmpl.ast.Class getLhs() { return lhs; }
	private final org.rascalmpl.ast.Class rhs;
	@Override
	public org.rascalmpl.ast.Class getRhs() { return rhs; }	
} public boolean isUnion() { return false; }
static public class Union extends Class {
/** lhs:Class "+" rhs:Class -> Class {cons("Union"), left} */
	public Union(INode node, org.rascalmpl.ast.Class lhs, org.rascalmpl.ast.Class rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitClassUnion(this);
	}

	@Override
	public boolean isUnion() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Class lhs;
	@Override
	public org.rascalmpl.ast.Class getLhs() { return lhs; }
	private final org.rascalmpl.ast.Class rhs;
	@Override
	public org.rascalmpl.ast.Class getRhs() { return rhs; }	
}
}