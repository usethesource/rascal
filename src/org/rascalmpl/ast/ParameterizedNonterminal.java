package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ParameterizedNonterminal extends AbstractAST { 
static public class Lexical extends ParameterizedNonterminal {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitParameterizedNonterminalLexical(this);
  	}
}
static public class Ambiguity extends ParameterizedNonterminal {
  private final java.util.List<org.rascalmpl.ast.ParameterizedNonterminal> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ParameterizedNonterminal> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ParameterizedNonterminal> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitParameterizedNonterminalAmbiguity(this);
  }
}
}