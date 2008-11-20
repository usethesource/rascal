package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Visibility extends AbstractAST { 
  public boolean isPublic() { return false; }
static public class Public extends Visibility {
/* "public" -> Visibility {cons("Public")} */
	private Public() { }
	/*package*/ Public(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVisibilityPublic(this);
	}

	public boolean isPublic() { return true; }	
}
static public class Ambiguity extends Visibility {
  private final java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Visibility> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitVisibilityAmbiguity(this);
  }
} 
public boolean isPrivate() { return false; }
static public class Private extends Visibility {
/* "private" -> Visibility {cons("Private")} */
	private Private() { }
	/*package*/ Private(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVisibilityPrivate(this);
	}

	public boolean isPrivate() { return true; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}