package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class UnicodeEscape extends AbstractAST { 
static public class Lexical extends UnicodeEscape {
	private String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitUnicodeEscapeLexical(this);
  	}
}
static public class Ambiguity extends UnicodeEscape {
  private final java.util.List<org.meta_environment.rascal.ast.UnicodeEscape> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.UnicodeEscape> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.UnicodeEscape> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitUnicodeEscapeAmbiguity(this);
  }
}
}