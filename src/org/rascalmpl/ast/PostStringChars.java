package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class PostStringChars extends AbstractAST { 
static public class Lexical extends PostStringChars {
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
     		return v.visitPostStringCharsLexical(this);
  	}
}
static public class Ambiguity extends PostStringChars {
  private final java.util.List<org.rascalmpl.ast.PostStringChars> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PostStringChars> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.PostStringChars> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitPostStringCharsAmbiguity(this);
  }
}
}