package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class HostNumber extends AbstractAST { 
static public class Lexical extends HostNumber {
	private String string;
	/*package*/ Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitHostNumberLexical(this);
  	}
}
static public class Ambiguity extends HostNumber {
  private final java.util.List<org.meta_environment.rascal.ast.HostNumber> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.HostNumber> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.HostNumber> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitHostNumberAmbiguity(this);
  }
}
}