package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class FunctionModifiers extends AbstractAST { 
public java.util.List<org.rascalmpl.ast.FunctionModifier> getModifiers() { throw new UnsupportedOperationException(); }
public boolean hasModifiers() { return false; }
public boolean isList() { return false; }
static public class List extends FunctionModifiers {
/** modifiers:FunctionModifier* -> FunctionModifiers {cons("List")} */
	public List(INode node, java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers) {
		this.node = node;
		this.modifiers = modifiers;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionModifiersList(this);
	}

	@Override
	public boolean isList() { return true; }

	@Override
	public boolean hasModifiers() { return true; }

private final java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers;
	@Override
	public java.util.List<org.rascalmpl.ast.FunctionModifier> getModifiers() { return modifiers; }	
}
static public class Ambiguity extends FunctionModifiers {
  private final java.util.List<org.rascalmpl.ast.FunctionModifiers> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionModifiers> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.FunctionModifiers> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionModifiersAmbiguity(this);
  }
}
}