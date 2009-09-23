package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class StringConstant extends AbstractAST { 
static public class Lexical extends StringConstant {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitStringConstantLexical(this);
  	}
}
static public class Ambiguity extends StringConstant {
  private final java.util.List<org.meta_environment.rascal.ast.StringConstant> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringConstant> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.StringConstant> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStringConstantAmbiguity(this);
  }
}
}