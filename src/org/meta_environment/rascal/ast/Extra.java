package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Extra extends AbstractAST { 
static public class Lexical extends Extra {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitExtraLexical(this);
  	}
}
static public class Ambiguity extends Extra {
  private final java.util.List<org.meta_environment.rascal.ast.Extra> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Extra> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Extra> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitExtraAmbiguity(this);
  }
}
}