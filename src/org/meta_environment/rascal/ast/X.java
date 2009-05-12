package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class X extends AbstractAST { 
public org.meta_environment.rascal.ast.QualifiedName getName() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
public boolean isMetaVariable() { return false; }
static public class MetaVariable extends X {
/* "<" name:QualifiedName ">" -> X {cons("MetaVariable"), category("MetaVariable")} */
	private MetaVariable() {
		super();
	}
	/*package*/ MetaVariable(INode node, org.meta_environment.rascal.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitXMetaVariable(this);
	}

	public boolean isMetaVariable() { return true; }

	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public MetaVariable setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		MetaVariable z = new MetaVariable();
 		z.$setName(x);
		return z;
	}	
}
static public class Ambiguity extends X {
  private final java.util.List<org.meta_environment.rascal.ast.X> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.X> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.X> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitXAmbiguity(this);
  }
}
}