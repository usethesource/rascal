package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Toplevel extends AbstractAST { 
public org.meta_environment.rascal.ast.Declaration getDeclaration() { throw new UnsupportedOperationException(); }
public boolean hasDeclaration() { return false; }
public boolean isGivenVisibility() { return false; }
static public class GivenVisibility extends Toplevel {
/** declaration:Declaration -> Toplevel {cons("GivenVisibility")} */
	private GivenVisibility() {
		super();
	}
	public GivenVisibility(INode node, org.meta_environment.rascal.ast.Declaration declaration) {
		this.node = node;
		this.declaration = declaration;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitToplevelGivenVisibility(this);
	}

	public boolean isGivenVisibility() { return true; }

	public boolean hasDeclaration() { return true; }

private org.meta_environment.rascal.ast.Declaration declaration;
	public org.meta_environment.rascal.ast.Declaration getDeclaration() { return declaration; }
	private void $setDeclaration(org.meta_environment.rascal.ast.Declaration x) { this.declaration = x; }
	public GivenVisibility setDeclaration(org.meta_environment.rascal.ast.Declaration x) { 
		GivenVisibility z = new GivenVisibility();
 		z.$setDeclaration(x);
		return z;
	}	
}
static public class Ambiguity extends Toplevel {
  private final java.util.List<org.meta_environment.rascal.ast.Toplevel> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Toplevel> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Toplevel> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitToplevelAmbiguity(this);
  }
}
}