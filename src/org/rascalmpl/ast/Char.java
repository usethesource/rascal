package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Char extends AbstractAST { 
	static public class Lexical extends Char {
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
	     		return v.visitCharLexical(this);
	  	}
	}

	static public class Ambiguity extends Char {
  private final java.util.List<org.rascalmpl.ast.Char> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Char> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Char> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharAmbiguity(this);
  }
} 
}