package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class TagString extends AbstractAST { 
static public class Lexical extends TagString {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitTagStringLexical(this);
  	}
}
static public class Ambiguity extends TagString {
  private final java.util.List<org.rascalmpl.ast.TagString> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.TagString> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.TagString> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTagStringAmbiguity(this);
  }
}
}