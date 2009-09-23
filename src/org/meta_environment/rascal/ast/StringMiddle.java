package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class StringMiddle extends AbstractAST { 
  static public class Lexical extends StringMiddle {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitStringMiddleLexical(this);
  	}
} static public class Ambiguity extends StringMiddle {
  private final java.util.List<org.meta_environment.rascal.ast.StringMiddle> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringMiddle> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.StringMiddle> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStringMiddleAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}