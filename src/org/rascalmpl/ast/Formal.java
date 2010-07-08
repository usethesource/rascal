package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Formal extends AbstractAST { 
public org.rascalmpl.ast.Type getType() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); }
public boolean hasType() { return false; }
	public boolean hasName() { return false; }
public boolean isTypeName() { return false; }
static public class TypeName extends Formal {
/** type:Type name:Name -> Formal {cons("TypeName")} */
	public TypeName(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.Name name) {
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

private final org.rascalmpl.ast.Type type;
	public org.rascalmpl.ast.Type getType() { return type; }
	private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }	
}
static public class Ambiguity extends Formal {
  private final java.util.List<org.rascalmpl.ast.Formal> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Formal> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Formal> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFormalAmbiguity(this);
  }
}
}