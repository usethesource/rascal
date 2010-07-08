package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class NumChar extends AbstractAST { 
static public class Lexical extends NumChar {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitNumCharLexical(this);
  	}
}
static public class Ambiguity extends NumChar {
  private final java.util.List<org.rascalmpl.ast.NumChar> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.NumChar> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.NumChar> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitNumCharAmbiguity(this);
  }
}
}