package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class RegExp extends AbstractAST { 
  static public class Lexical extends RegExp {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitRegExpLexical(this);
  	}
} static public class Ambiguity extends RegExp {
  private final java.util.List<org.rascalmpl.ast.RegExp> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.RegExp> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.RegExp> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitRegExpAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}