package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class CaseInsensitiveStringConstant extends AbstractAST { 
static public class Lexical extends CaseInsensitiveStringConstant {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitCaseInsensitiveStringConstantLexical(this);
  	}
}
static public class Ambiguity extends CaseInsensitiveStringConstant {
  private final java.util.List<org.rascalmpl.ast.CaseInsensitiveStringConstant> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CaseInsensitiveStringConstant> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.CaseInsensitiveStringConstant> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCaseInsensitiveStringConstantAmbiguity(this);
  }
}
}