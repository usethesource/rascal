package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class JustTime extends AbstractAST { 
static public class Lexical extends JustTime {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitJustTimeLexical(this);
  	}
}
static public class Ambiguity extends JustTime {
  private final java.util.List<org.rascalmpl.ast.JustTime> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.JustTime> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.JustTime> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitJustTimeAmbiguity(this);
  }
}
}