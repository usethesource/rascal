package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Backslash extends AbstractAST { 
static public class Lexical extends Backslash {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitBackslashLexical(this);
  	}
}
static public class Ambiguity extends Backslash {
  private final java.util.List<org.meta_environment.rascal.ast.Backslash> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Backslash> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Backslash> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitBackslashAmbiguity(this);
  }
}
}