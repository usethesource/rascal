package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class FunctionModifier extends AbstractAST { 
public boolean isJava() { return false; }
static public class Java extends FunctionModifier {
/* "java" -> FunctionModifier {cons("Java")} */
	private Java() { }
	/*package*/ Java(ITree tree) {
		this.tree = tree;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionModifierJava(this);
	}

	@Override
	public boolean isJava() { return true; }	
}
static public class Ambiguity extends FunctionModifier {
  private final java.util.List<org.meta_environment.rascal.ast.FunctionModifier> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.FunctionModifier> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.FunctionModifier> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionModifierAmbiguity(this);
  }
}
}