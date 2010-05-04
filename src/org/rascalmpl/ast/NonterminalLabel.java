package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class NonterminalLabel extends AbstractAST { 
static public class Lexical extends NonterminalLabel {
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
     		return v.visitNonterminalLabelLexical(this);
  	}
}
static public class Ambiguity extends NonterminalLabel {
  private final java.util.List<org.rascalmpl.ast.NonterminalLabel> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.NonterminalLabel> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.NonterminalLabel> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitNonterminalLabelAmbiguity(this);
  }
}
}