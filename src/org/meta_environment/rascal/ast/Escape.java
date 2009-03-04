package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Escape extends AbstractAST { 
static public class Lexical extends Escape {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitEscapeLexical(this);
  	}
}
static public class Ambiguity extends Escape {
  private final java.util.List<org.meta_environment.rascal.ast.Escape> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Escape> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Escape> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitEscapeAmbiguity(this);
  }
}
}