package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class TypeVar extends AbstractAST { 
public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean isFree() { return false; }
static public class Free extends TypeVar {
/* "&" name:Name -> TypeVar {cons("Free")} */
	private Free() { }
	/*package*/ Free(ITree tree, org.meta_environment.rascal.ast.Name name) {
		this.tree = tree;
		this.name = name;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitTypeVarFree(this);
	}

	public boolean isFree() { return true; }

	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Free setName(org.meta_environment.rascal.ast.Name x) { 
		Free z = new Free();
 		z.$setName(x);
		return z;
	}	
}
static public class Ambiguity extends TypeVar {
  private final java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.TypeVar> getAlternatives() {
	return alternatives;
  }
} public org.meta_environment.rascal.ast.Type getBound() { throw new UnsupportedOperationException(); } public boolean hasBound() { return false; }
public boolean isBounded() { return false; }
static public class Bounded extends TypeVar {
/* "&" name:Name "<:" bound:Type -> TypeVar {cons("Bounded")} */
	private Bounded() { }
	/*package*/ Bounded(ITree tree, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Type bound) {
		this.tree = tree;
		this.name = name;
		this.bound = bound;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitTypeVarBounded(this);
	}

	public boolean isBounded() { return true; }

	public boolean hasName() { return true; }
	public boolean hasBound() { return true; }

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Bounded setName(org.meta_environment.rascal.ast.Name x) { 
		Bounded z = new Bounded();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Type bound;
	public org.meta_environment.rascal.ast.Type getBound() { return bound; }
	private void $setBound(org.meta_environment.rascal.ast.Type x) { this.bound = x; }
	public Bounded setBound(org.meta_environment.rascal.ast.Type x) { 
		Bounded z = new Bounded();
 		z.$setBound(x);
		return z;
	}	
}
}