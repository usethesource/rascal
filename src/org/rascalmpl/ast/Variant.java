package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Variant extends AbstractAST { 
public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); }
	public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
	public boolean hasArguments() { return false; }
public boolean isNAryConstructor() { return false; }
static public class NAryConstructor extends Variant {
/** name:Name "(" arguments:{TypeArg ","}* ")" -> Variant {cons("NAryConstructor")} */
	protected NAryConstructor(INode node, org.rascalmpl.ast.Name name, java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
		this.node = node;
		this.name = name;
		this.arguments = arguments;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVariantNAryConstructor(this);
	}

	public boolean isNAryConstructor() { return true; }

	public boolean hasName() { return true; }
	public boolean hasArguments() { return true; }

private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }
	private final java.util.List<org.rascalmpl.ast.TypeArg> arguments;
	public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() { return arguments; }	
}
static public class Ambiguity extends Variant {
  private final java.util.List<org.rascalmpl.ast.Variant> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Variant> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Variant> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitVariantAmbiguity(this);
  }
}
}