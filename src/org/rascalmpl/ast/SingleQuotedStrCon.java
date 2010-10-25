package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class SingleQuotedStrCon extends AbstractAST { 
static public class Lexical extends SingleQuotedStrCon {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitSingleQuotedStrConLexical(this);
  	}
}
static public class Ambiguity extends SingleQuotedStrCon {
  private final java.util.List<org.rascalmpl.ast.SingleQuotedStrCon> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.SingleQuotedStrCon> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.SingleQuotedStrCon> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitSingleQuotedStrConAmbiguity(this);
  }
}
}