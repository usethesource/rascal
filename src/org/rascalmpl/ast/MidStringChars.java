package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class MidStringChars extends AbstractAST { 
static public class Lexical extends MidStringChars {
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
     		return v.visitMidStringCharsLexical(this);
  	}
}
static public class Ambiguity extends MidStringChars {
  private final java.util.List<org.rascalmpl.ast.MidStringChars> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.MidStringChars> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.MidStringChars> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitMidStringCharsAmbiguity(this);
  }
}
}