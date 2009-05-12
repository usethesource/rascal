package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Variant extends AbstractAST { 
  public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.TypeArg> getArguments() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean hasArguments() { return false; }
public boolean isNAryConstructor() { return false; }
static public class NAryConstructor extends Variant {
/* name:Name "(" arguments:{TypeArg ","}* ")" -> Variant {cons("NAryConstructor")} */
	private NAryConstructor() {
		super();
	}
	/*package*/ NAryConstructor(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) {
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

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public NAryConstructor setName(org.meta_environment.rascal.ast.Name x) { 
		NAryConstructor z = new NAryConstructor();
 		z.$setName(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments;
	public java.util.List<org.meta_environment.rascal.ast.TypeArg> getArguments() { return arguments; }
	private void $setArguments(java.util.List<org.meta_environment.rascal.ast.TypeArg> x) { this.arguments = x; }
	public NAryConstructor setArguments(java.util.List<org.meta_environment.rascal.ast.TypeArg> x) { 
		NAryConstructor z = new NAryConstructor();
 		z.$setArguments(x);
		return z;
	}	
}
static public class Ambiguity extends Variant {
  private final java.util.List<org.meta_environment.rascal.ast.Variant> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Variant> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Variant> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitVariantAmbiguity(this);
  }
} public boolean isNillaryConstructor() { return false; }
static public class NillaryConstructor extends Variant {
/* name:Name -> Variant {cons("NillaryConstructor")} */
	private NillaryConstructor() {
		super();
	}
	/*package*/ NillaryConstructor(INode node, org.meta_environment.rascal.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVariantNillaryConstructor(this);
	}

	public boolean isNillaryConstructor() { return true; }

	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public NillaryConstructor setName(org.meta_environment.rascal.ast.Name x) { 
		NillaryConstructor z = new NillaryConstructor();
 		z.$setName(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}