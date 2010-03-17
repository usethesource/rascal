package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class PathChars extends AbstractAST { 
static public class Lexical extends PathChars {
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
     		return v.visitPathCharsLexical(this);
  	}
}
static public class Ambiguity extends PathChars {
  private final java.util.List<org.rascalmpl.ast.PathChars> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PathChars> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.PathChars> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitPathCharsAmbiguity(this);
  }
}
}