package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Assoc extends AbstractAST { 
  public boolean isLeft() { return false; }
static public class Left extends Assoc {
/** "left" -> Assoc {cons("Left")} */
	public Left(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssocLeft(this);
	}

	public boolean isLeft() { return true; }	
}
static public class Ambiguity extends Assoc {
  private final java.util.List<org.rascalmpl.ast.Assoc> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Assoc> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Assoc> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitAssocAmbiguity(this);
  }
} 
public boolean isRight() { return false; }
static public class Right extends Assoc {
/** "right" -> Assoc {cons("Right")} */
	public Right(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssocRight(this);
	}

	public boolean isRight() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isNonAssociative() { return false; }
static public class NonAssociative extends Assoc {
/** "non-assoc" -> Assoc {cons("NonAssociative")} */
	public NonAssociative(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssocNonAssociative(this);
	}

	public boolean isNonAssociative() { return true; }	
} 
public boolean isAssociative() { return false; }
static public class Associative extends Assoc {
/** "assoc" -> Assoc {cons("Associative")} */
	public Associative(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAssocAssociative(this);
	}

	public boolean isAssociative() { return true; }	
}
}