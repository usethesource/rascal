package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Signature extends AbstractAST { 
  public org.meta_environment.rascal.ast.Type getType() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.FunctionModifiers getModifiers() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Parameters getParameters() { throw new UnsupportedOperationException(); } public boolean hasType() { return false; } public boolean hasModifiers() { return false; } public boolean hasName() { return false; } public boolean hasParameters() { return false; } public boolean isNoThrows() { return false; }
static public class NoThrows extends Signature {
/* type:Type modifiers:FunctionModifiers name:Name parameters:Parameters -> Signature {cons("NoThrows")} */
	private NoThrows() { }
	/*package*/ NoThrows(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.FunctionModifiers modifiers, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Parameters parameters) {
		this.node = node;
		this.type = type;
		this.modifiers = modifiers;
		this.name = name;
		this.parameters = parameters;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSignatureNoThrows(this);
	}

	public boolean isNoThrows() { return true; }

	public boolean hasType() { return true; }
	public boolean hasModifiers() { return true; }
	public boolean hasName() { return true; }
	public boolean hasParameters() { return true; }

private org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }
	private void $setType(org.meta_environment.rascal.ast.Type x) { this.type = x; }
	public NoThrows setType(org.meta_environment.rascal.ast.Type x) { 
		NoThrows z = new NoThrows();
 		z.$setType(x);
		return z;
	}
	private org.meta_environment.rascal.ast.FunctionModifiers modifiers;
	public org.meta_environment.rascal.ast.FunctionModifiers getModifiers() { return modifiers; }
	private void $setModifiers(org.meta_environment.rascal.ast.FunctionModifiers x) { this.modifiers = x; }
	public NoThrows setModifiers(org.meta_environment.rascal.ast.FunctionModifiers x) { 
		NoThrows z = new NoThrows();
 		z.$setModifiers(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public NoThrows setName(org.meta_environment.rascal.ast.Name x) { 
		NoThrows z = new NoThrows();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Parameters parameters;
	public org.meta_environment.rascal.ast.Parameters getParameters() { return parameters; }
	private void $setParameters(org.meta_environment.rascal.ast.Parameters x) { this.parameters = x; }
	public NoThrows setParameters(org.meta_environment.rascal.ast.Parameters x) { 
		NoThrows z = new NoThrows();
 		z.$setParameters(x);
		return z;
	}	
}
static public class Ambiguity extends Signature {
  private final java.util.List<org.meta_environment.rascal.ast.Signature> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Signature> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Signature> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitSignatureAmbiguity(this);
  }
} public java.util.List<org.meta_environment.rascal.ast.Type> getExceptions() { throw new UnsupportedOperationException(); } public boolean hasExceptions() { return false; }
public boolean isWithThrows() { return false; }
static public class WithThrows extends Signature {
/* type:Type modifiers:FunctionModifiers name:Name parameters:Parameters "throws" exceptions:{Type ","}+ -> Signature {cons("WithThrows")} */
	private WithThrows() { }
	/*package*/ WithThrows(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.FunctionModifiers modifiers, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Type> exceptions) {
		this.node = node;
		this.type = type;
		this.modifiers = modifiers;
		this.name = name;
		this.parameters = parameters;
		this.exceptions = exceptions;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSignatureWithThrows(this);
	}

	public boolean isWithThrows() { return true; }

	public boolean hasType() { return true; }
	public boolean hasModifiers() { return true; }
	public boolean hasName() { return true; }
	public boolean hasParameters() { return true; }
	public boolean hasExceptions() { return true; }

private org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }
	private void $setType(org.meta_environment.rascal.ast.Type x) { this.type = x; }
	public WithThrows setType(org.meta_environment.rascal.ast.Type x) { 
		WithThrows z = new WithThrows();
 		z.$setType(x);
		return z;
	}
	private org.meta_environment.rascal.ast.FunctionModifiers modifiers;
	public org.meta_environment.rascal.ast.FunctionModifiers getModifiers() { return modifiers; }
	private void $setModifiers(org.meta_environment.rascal.ast.FunctionModifiers x) { this.modifiers = x; }
	public WithThrows setModifiers(org.meta_environment.rascal.ast.FunctionModifiers x) { 
		WithThrows z = new WithThrows();
 		z.$setModifiers(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public WithThrows setName(org.meta_environment.rascal.ast.Name x) { 
		WithThrows z = new WithThrows();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Parameters parameters;
	public org.meta_environment.rascal.ast.Parameters getParameters() { return parameters; }
	private void $setParameters(org.meta_environment.rascal.ast.Parameters x) { this.parameters = x; }
	public WithThrows setParameters(org.meta_environment.rascal.ast.Parameters x) { 
		WithThrows z = new WithThrows();
 		z.$setParameters(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Type> exceptions;
	public java.util.List<org.meta_environment.rascal.ast.Type> getExceptions() { return exceptions; }
	private void $setExceptions(java.util.List<org.meta_environment.rascal.ast.Type> x) { this.exceptions = x; }
	public WithThrows setExceptions(java.util.List<org.meta_environment.rascal.ast.Type> x) { 
		WithThrows z = new WithThrows();
 		z.$setExceptions(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}