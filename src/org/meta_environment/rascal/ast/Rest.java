package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Rest extends AbstractAST { 
  static public class Lexical extends Rest {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitRestLexical(this);
  	}
} static public class Ambiguity extends Rest {
  private final java.util.List<org.meta_environment.rascal.ast.Rest> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Rest> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Rest> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitRestAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}