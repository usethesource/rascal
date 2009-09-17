package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class SingleQuotedStrChar extends AbstractAST { 
  static public class Lexical extends SingleQuotedStrChar {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitSingleQuotedStrCharLexical(this);
  	}
} static public class Ambiguity extends SingleQuotedStrChar {
  private final java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitSingleQuotedStrCharAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}