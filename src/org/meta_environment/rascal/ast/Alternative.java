package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Alternative extends AbstractAST { 
public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Type getType() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
	public boolean hasType() { return false; }
public boolean isNamedType() { return false; }
static public class NamedType extends Alternative {
/** name:Name type:Type -> Alternative {cons("NamedType")} */
	public NamedType(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Type type) {
		this.node = node;
		this.name = name;
		this.type = type;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAlternativeNamedType(this);
	}

	public boolean isNamedType() { return true; }

	public boolean hasName() { return true; }
	public boolean hasType() { return true; }

private final org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private final org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }	
}
static public class Ambiguity extends Alternative {
  private final java.util.List<org.meta_environment.rascal.ast.Alternative> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Alternative> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Alternative> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitAlternativeAmbiguity(this);
  }
}
}