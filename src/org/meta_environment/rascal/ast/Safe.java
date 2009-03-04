package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Safe extends AbstractAST { 
static public class Lexical extends Safe {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitSafeLexical(this);
  	}
}
static public class Ambiguity extends Safe {
  private final java.util.List<org.meta_environment.rascal.ast.Safe> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Safe> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Safe> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitSafeAmbiguity(this);
  }
}
}