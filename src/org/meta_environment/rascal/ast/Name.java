package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Name extends AbstractAST { 
  static public class Lexical extends Name {
	private String string;
	/*package*/ public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitNameLexical(this);
  	}
} static public class Ambiguity extends Name {
  private final java.util.List<org.meta_environment.rascal.ast.Name> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Name> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Name> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitNameAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}