package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Tags extends AbstractAST { 
public java.util.List<org.meta_environment.rascal.ast.Tag> getAnnotations() { throw new UnsupportedOperationException(); }
public boolean hasAnnotations() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Tags {
/* annotations:Tag* -> Tags {cons("Default")} */
	private Default() {
		super();
	}
	/*package*/ Default(INode node, java.util.List<org.meta_environment.rascal.ast.Tag> annotations) {
		this.node = node;
		this.annotations = annotations;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTagsDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasAnnotations() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Tag> annotations;
	public java.util.List<org.meta_environment.rascal.ast.Tag> getAnnotations() { return annotations; }
	private void $setAnnotations(java.util.List<org.meta_environment.rascal.ast.Tag> x) { this.annotations = x; }
	public Default setAnnotations(java.util.List<org.meta_environment.rascal.ast.Tag> x) { 
		Default z = new Default();
 		z.$setAnnotations(x);
		return z;
	}	
}
static public class Ambiguity extends Tags {
  private final java.util.List<org.meta_environment.rascal.ast.Tags> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Tags> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Tags> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTagsAmbiguity(this);
  }
}
}