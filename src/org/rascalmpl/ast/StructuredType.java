package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class StructuredType extends AbstractAST { 
public org.rascalmpl.ast.BasicType getBasicType() { throw new UnsupportedOperationException(); }
	public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() { throw new UnsupportedOperationException(); }
public boolean hasBasicType() { return false; }
	public boolean hasArguments() { return false; }
public boolean isDefault() { return false; }
static public class Default extends StructuredType {
/** basicType:BasicType "[" arguments:{TypeArg ","}+ "]" -> StructuredType {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.BasicType basicType, java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
		this.node = node;
		this.basicType = basicType;
		this.arguments = arguments;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStructuredTypeDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasBasicType() { return true; }
	@Override
	public boolean hasArguments() { return true; }

private final org.rascalmpl.ast.BasicType basicType;
	@Override
	public org.rascalmpl.ast.BasicType getBasicType() { return basicType; }
	private final java.util.List<org.rascalmpl.ast.TypeArg> arguments;
	@Override
	public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() { return arguments; }	
}
static public class Ambiguity extends StructuredType {
  private final java.util.List<org.rascalmpl.ast.StructuredType> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.StructuredType> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.StructuredType> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitStructuredTypeAmbiguity(this);
  }
}
}