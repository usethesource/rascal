package org.rascalmpl.ast; 
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

 	@Override
	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitSingleQuotedStrCharLexical(this);
  	}
} static public class Ambiguity extends SingleQuotedStrChar {
  private final java.util.List<org.rascalmpl.ast.SingleQuotedStrChar> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.SingleQuotedStrChar> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.SingleQuotedStrChar> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitSingleQuotedStrCharAmbiguity(this);
  }
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}