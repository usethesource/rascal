package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class HostPort extends AbstractAST { 
static public class Lexical extends HostPort {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitHostPortLexical(this);
  	}
}
static public class Ambiguity extends HostPort {
  private final java.util.List<org.meta_environment.rascal.ast.HostPort> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.HostPort> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.HostPort> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitHostPortAmbiguity(this);
  }
}
}