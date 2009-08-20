package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode;
public abstract class Formal extends AbstractAST { 
public org.meta_environment.rascal.ast.Type getType() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); }
public boolean hasType() { return false; }
	public boolean hasName() { return false; }
public boolean isTypeName() { return false; }
static public class TypeName extends Formal {
/** type:Type name:Name -> Formal {cons("TypeName")} */
	private TypeName() {
		super();
	}
	public TypeName(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) {
		this.node = node;
		this.type = type;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFormalTypeName(this);
	}

	public boolean isTypeName() { return true; }

	public boolean hasType() { return true; }
	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }
	private void $setType(org.meta_environment.rascal.ast.Type x) { this.type = x; }
	public TypeName setType(org.meta_environment.rascal.ast.Type x) { 
		TypeName z = new TypeName();
 		z.$setType(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public TypeName setName(org.meta_environment.rascal.ast.Name x) { 
		TypeName z = new TypeName();
 		z.$setName(x);
		return z;
	}	
}
static public class Ambiguity extends Formal {
  private final java.util.List<org.meta_environment.rascal.ast.Formal> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Formal> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Formal> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFormalAmbiguity(this);
  }
}
}