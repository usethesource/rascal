package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class FunctionModifier extends AbstractAST { 
public boolean isJava() { return false; }
static public class Java extends FunctionModifier {
/** "java" -> FunctionModifier {cons("Java")} */
	public Java(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionModifierJava(this);
	}

	public boolean isJava() { return true; }	
}
static public class Ambiguity extends FunctionModifier {
  private final java.util.List<org.rascalmpl.ast.FunctionModifier> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionModifier> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.FunctionModifier> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionModifierAmbiguity(this);
  }
}
}