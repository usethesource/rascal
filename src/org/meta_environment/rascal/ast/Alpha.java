package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Alpha extends AbstractAST { 
static public class Lexical extends Alpha {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitAlphaLexical(this);
  	}
}
static public class Ambiguity extends Alpha {
  private final java.util.List<org.meta_environment.rascal.ast.Alpha> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Alpha> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Alpha> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitAlphaAmbiguity(this);
  }
}
}