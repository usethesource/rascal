package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class QualifiedName extends AbstractAST { 
public java.util.List<org.rascalmpl.ast.Name> getNames() { throw new UnsupportedOperationException(); }
public boolean hasNames() { return false; }
public boolean isDefault() { return false; }
static public class Default extends QualifiedName {
/** names:{Name "::"}+ -> QualifiedName {cons("Default")} */
	public Default(INode node, java.util.List<org.rascalmpl.ast.Name> names) {
		this.node = node;
		this.names = names;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitQualifiedNameDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasNames() { return true; }

private final java.util.List<org.rascalmpl.ast.Name> names;
	@Override
	public java.util.List<org.rascalmpl.ast.Name> getNames() { return names; }	
}
static public class Ambiguity extends QualifiedName {
  private final java.util.List<org.rascalmpl.ast.QualifiedName> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.QualifiedName> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.QualifiedName> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitQualifiedNameAmbiguity(this);
  }
}
}