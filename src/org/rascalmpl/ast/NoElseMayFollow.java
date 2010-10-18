package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class NoElseMayFollow extends AbstractAST { 
public boolean isDefault() { return false; }
static public class Default extends NoElseMayFollow {
/**  -> NoElseMayFollow {cons("Default")} */
	protected Default(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitNoElseMayFollowDefault(this);
	}

	public boolean isDefault() { return true; }	
}
static public class Ambiguity extends NoElseMayFollow {
  private final java.util.List<org.rascalmpl.ast.NoElseMayFollow> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.NoElseMayFollow> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.NoElseMayFollow> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitNoElseMayFollowAmbiguity(this);
  }
}
}