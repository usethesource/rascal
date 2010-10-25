package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class PrePathChars extends AbstractAST { 
static public class Lexical extends PrePathChars {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitPrePathCharsLexical(this);
  	}
}
static public class Ambiguity extends PrePathChars {
  private final java.util.List<org.rascalmpl.ast.PrePathChars> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PrePathChars> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.PrePathChars> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitPrePathCharsAmbiguity(this);
  }
}
}