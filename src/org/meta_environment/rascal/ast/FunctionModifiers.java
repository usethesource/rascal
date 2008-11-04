package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class FunctionModifiers extends AbstractAST { 
public java.util.List<org.meta_environment.rascal.ast.FunctionModifier> getModifiers() { throw new UnsupportedOperationException(); }
public boolean hasModifiers() { return false; }
public boolean isList() { return false; }
static public class List extends FunctionModifiers {
/* modifiers:FunctionModifier* -> FunctionModifiers {cons("List")} */
	private List() { }
	/*package*/ List(ITree tree, java.util.List<org.meta_environment.rascal.ast.FunctionModifier> modifiers) {
		this.tree = tree;
		this.modifiers = modifiers;
	}
	public IVisitable accept(IASTVisitor visitor) {
		return visitor.visitFunctionModifiersList(this);
	}

	public boolean isList() { return true; }

	public boolean hasModifiers() { return true; }

private java.util.List<org.meta_environment.rascal.ast.FunctionModifier> modifiers;
	public java.util.List<org.meta_environment.rascal.ast.FunctionModifier> getModifiers() { return modifiers; }
	private void $setModifiers(java.util.List<org.meta_environment.rascal.ast.FunctionModifier> x) { this.modifiers = x; }
	public List setModifiers(java.util.List<org.meta_environment.rascal.ast.FunctionModifier> x) { 
		List z = new List();
 		z.$setModifiers(x);
		return z;
	}	
}
static public class Ambiguity extends FunctionModifiers {
  private final java.util.List<org.meta_environment.rascal.ast.FunctionModifiers> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.FunctionModifiers> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.FunctionModifiers> getAlternatives() {
	return alternatives;
  }
}
}