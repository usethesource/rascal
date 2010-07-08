package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Toplevel extends AbstractAST { 
public org.rascalmpl.ast.Declaration getDeclaration() { throw new UnsupportedOperationException(); }
public boolean hasDeclaration() { return false; }
public boolean isGivenVisibility() { return false; }
static public class GivenVisibility extends Toplevel {
/** declaration:Declaration -> Toplevel {cons("GivenVisibility")} */
	public GivenVisibility(INode node, org.rascalmpl.ast.Declaration declaration) {
		this.node = node;
		this.declaration = declaration;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitToplevelGivenVisibility(this);
	}

	public boolean isGivenVisibility() { return true; }

	public boolean hasDeclaration() { return true; }

private final org.rascalmpl.ast.Declaration declaration;
	public org.rascalmpl.ast.Declaration getDeclaration() { return declaration; }	
}
static public class Ambiguity extends Toplevel {
  private final java.util.List<org.rascalmpl.ast.Toplevel> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Toplevel> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Toplevel> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitToplevelAmbiguity(this);
  }
}
}