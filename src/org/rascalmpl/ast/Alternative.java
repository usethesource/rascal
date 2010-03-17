package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Alternative extends AbstractAST { 
public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Type getType() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
	public boolean hasType() { return false; }
public boolean isNamedType() { return false; }
static public class NamedType extends Alternative {
/** name:Name type:Type -> Alternative {cons("NamedType")} */
	public NamedType(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Type type) {
		this.node = node;
		this.name = name;
		this.type = type;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitAlternativeNamedType(this);
	}

	@Override
	public boolean isNamedType() { return true; }

	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasType() { return true; }

private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.Type type;
	@Override
	public org.rascalmpl.ast.Type getType() { return type; }	
}
static public class Ambiguity extends Alternative {
  private final java.util.List<org.rascalmpl.ast.Alternative> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Alternative> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Alternative> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitAlternativeAmbiguity(this);
  }
}
}