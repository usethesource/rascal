package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class NamedRegExp extends AbstractAST { 
  static public class Lexical extends NamedRegExp {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitNamedRegExpLexical(this);
  	}
} static public class Ambiguity extends NamedRegExp {
  private final java.util.List<org.rascalmpl.ast.NamedRegExp> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.NamedRegExp> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.NamedRegExp> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitNamedRegExpAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}