package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class RegExpModifier extends AbstractAST { 
static public class Lexical extends RegExpModifier {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitRegExpModifierLexical(this);
  	}
}
static public class Ambiguity extends RegExpModifier {
  private final java.util.List<org.rascalmpl.ast.RegExpModifier> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.RegExpModifier> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.RegExpModifier> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitRegExpModifierAmbiguity(this);
  }
}
}