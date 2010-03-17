package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class PreProtocolChars extends AbstractAST { 
static public class Lexical extends PreProtocolChars {
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
     		return v.visitPreProtocolCharsLexical(this);
  	}
}
static public class Ambiguity extends PreProtocolChars {
  private final java.util.List<org.rascalmpl.ast.PreProtocolChars> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PreProtocolChars> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.PreProtocolChars> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitPreProtocolCharsAmbiguity(this);
  }
}
}