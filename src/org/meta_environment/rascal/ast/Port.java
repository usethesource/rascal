package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Port extends AbstractAST { 
static public class Lexical extends Port {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitPortLexical(this);
  	}
}
static public class Ambiguity extends Port {
  private final java.util.List<org.meta_environment.rascal.ast.Port> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Port> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Port> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitPortAmbiguity(this);
  }
}
}