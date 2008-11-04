package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class QualifiedName extends AbstractAST { 
public java.util.List<org.meta_environment.rascal.ast.Name> getNames() { throw new UnsupportedOperationException(); }
public boolean hasNames() { return false; }
public boolean isDefault() { return false; }
static public class Default extends QualifiedName {
/* names:{Name "::"}+ -> QualifiedName {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree, java.util.List<org.meta_environment.rascal.ast.Name> names) {
		this.tree = tree;
		this.names = names;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitQualifiedNameDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasNames() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Name> names;
	public java.util.List<org.meta_environment.rascal.ast.Name> getNames() { return names; }
	private void $setNames(java.util.List<org.meta_environment.rascal.ast.Name> x) { this.names = x; }
	public Default setNames(java.util.List<org.meta_environment.rascal.ast.Name> x) { 
		Default z = new Default();
 		z.$setNames(x);
		return z;
	}	
}
static public class Ambiguity extends QualifiedName {
  private final java.util.List<org.meta_environment.rascal.ast.QualifiedName> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.QualifiedName> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getAlternatives() {
	return alternatives;
  }
}
}