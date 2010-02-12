package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class TypeVar extends AbstractAST { 
  public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean isFree() { return false; }
static public class Free extends TypeVar {
/** "&" name:Name -> TypeVar {cons("Free")} */
	public Free(INode node, org.rascalmpl.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTypeVarFree(this);
	}

	public boolean isFree() { return true; }

	public boolean hasName() { return true; }

private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }	
}
static public class Ambiguity extends TypeVar {
  private final java.util.List<org.rascalmpl.ast.TypeVar> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.TypeVar> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.TypeVar> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTypeVarAmbiguity(this);
  }
} public org.rascalmpl.ast.Type getBound() { throw new UnsupportedOperationException(); } public boolean hasBound() { return false; }
public boolean isBounded() { return false; }
static public class Bounded extends TypeVar {
/** "&" name:Name "<:" bound:Type -> TypeVar {cons("Bounded")} */
	public Bounded(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Type bound) {
		this.node = node;
		this.name = name;
		this.bound = bound;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTypeVarBounded(this);
	}

	public boolean isBounded() { return true; }

	public boolean hasName() { return true; }
	public boolean hasBound() { return true; }

private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.Type bound;
	public org.rascalmpl.ast.Type getBound() { return bound; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}