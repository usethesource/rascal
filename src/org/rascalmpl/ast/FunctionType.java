package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class FunctionType extends AbstractAST { 
public org.rascalmpl.ast.Type getType() { throw new UnsupportedOperationException(); }
	public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() { throw new UnsupportedOperationException(); }
public boolean hasType() { return false; }
	public boolean hasArguments() { return false; }
public boolean isTypeArguments() { return false; }
static public class TypeArguments extends FunctionType {
/** type:Type "(" arguments:{TypeArg ","}* ")" -> FunctionType {cons("TypeArguments")} */
	protected TypeArguments(INode node, org.rascalmpl.ast.Type type, java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
		this.node = node;
		this.type = type;
		this.arguments = arguments;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFunctionTypeTypeArguments(this);
	}

	public boolean isTypeArguments() { return true; }

	public boolean hasType() { return true; }
	public boolean hasArguments() { return true; }

private final org.rascalmpl.ast.Type type;
	public org.rascalmpl.ast.Type getType() { return type; }
	private final java.util.List<org.rascalmpl.ast.TypeArg> arguments;
	public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() { return arguments; }	
}
static public class Ambiguity extends FunctionType {
  private final java.util.List<org.rascalmpl.ast.FunctionType> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionType> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.FunctionType> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFunctionTypeAmbiguity(this);
  }
}
}