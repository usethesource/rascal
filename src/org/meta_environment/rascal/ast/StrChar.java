package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class StrChar extends AbstractAST { 
  public boolean isnewline() { return false; }
static public class newline extends StrChar {
/* "\\n" -> StrChar {cons("newline")} */
	private newline() { }
	/*package*/ newline(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStrCharnewline(this);
	}

	public boolean isnewline() { return true; }	
}
static public class Ambiguity extends StrChar {
  private final java.util.List<org.meta_environment.rascal.ast.StrChar> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.StrChar> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.StrChar> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStrCharAmbiguity(this);
  }
} static public class Lexical extends StrChar {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitStrCharLexical(this);
  	}
} public abstract <T> T accept(IASTVisitor<T> visitor);
}