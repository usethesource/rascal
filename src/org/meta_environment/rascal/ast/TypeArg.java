package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class TypeArg extends AbstractAST { 
public class Default extends TypeArg {
/* type:Type -> TypeArg {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree, Type type) {
		this.tree = tree;
		this.type = type;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitDefaultTypeArg(this);
	}
private Type type;
	public Type gettype() { return type; }
	private void privateSettype(Type x) { this.type = x; }
	public Default settype(Type x) { 
		Default z = new Default();
 		z.privateSettype(x);
		return z;
	}	
}
public class Ambiguity extends TypeArg {
  private final List<TypeArg> alternatives;
  public Ambiguity(List<TypeArg> alternatives) {
	this.alternatives = Collections.immutableList(alternatives);
  }
  public List<TypeArg> getAlternatives() {
	return alternatives;
  }
} 
public class Named extends TypeArg {
/* type:Type name:Name -> TypeArg {cons("Named")} */
	private Named() { }
	/*package*/ Named(ITree tree, Type type, Name name) {
		this.tree = tree;
		this.type = type;
		this.name = name;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitNamedTypeArg(this);
	}
private Type type;
	public Type gettype() { return type; }
	private void privateSettype(Type x) { this.type = x; }
	public Named settype(Type x) { 
		Named z = new Named();
 		z.privateSettype(x);
		return z;
	}
	private Name name;
	public Name getname() { return name; }
	private void privateSetname(Name x) { this.name = x; }
	public Named setname(Name x) { 
		Named z = new Named();
 		z.privateSetname(x);
		return z;
	}	
}
}