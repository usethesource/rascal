package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class TypeVar extends AbstractAST { 
public class Free extends TypeVar {
/* "&" name:Name -> TypeVar {cons("Free")} */
	private Free() { }
	/*package*/ Free(ITree tree, Name name) {
		this.tree = tree;
		this.name = name;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitFreeTypeVar(this);
	}
private Name name;
	public Name getname() { return name; }
	private void privateSetname(Name x) { this.name = x; }
	public Free setname(Name x) { 
		Free z = new Free();
 		z.privateSetname(x);
		return z;
	}	
}
public class Ambiguity extends TypeVar {
  private final List<TypeVar> alternatives;
  public Ambiguity(List<TypeVar> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<TypeVar> getAlternatives() {
	return alternatives;
  }
} 
public class Bounded extends TypeVar {
/* "&" name:Name "<:" bound:Type -> TypeVar {cons("Bounded")} */
	private Bounded() { }
	/*package*/ Bounded(ITree tree, Name name, Type bound) {
		this.tree = tree;
		this.name = name;
		this.bound = bound;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitBoundedTypeVar(this);
	}
private Name name;
	public Name getname() { return name; }
	private void privateSetname(Name x) { this.name = x; }
	public Bounded setname(Name x) { 
		Bounded z = new Bounded();
 		z.privateSetname(x);
		return z;
	}
	private Type bound;
	public Type getbound() { return bound; }
	private void privateSetbound(Type x) { this.bound = x; }
	public Bounded setbound(Type x) { 
		Bounded z = new Bounded();
 		z.privateSetbound(x);
		return z;
	}	
}
}