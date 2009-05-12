package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class FunctionModifier extends AbstractAST { 
public boolean isJava() { return false; }
static public class Java extends FunctionModifier {
/* "java" -> FunctionModifier {cons("Java")} */
	private Java() {
		super();
	}
	/*package*/ Java(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionModifierJava(this);
	}

	public boolean isJava() { return true; }	
}
static public class Ambiguity extends FunctionModifier {
  private final java.util.List<org.meta_environment.rascal.ast.FunctionModifier> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionModifier> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.FunctionModifier> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionModifierAmbiguity(this);
  }
}
}