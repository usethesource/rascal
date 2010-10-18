package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Signature extends AbstractAST { 
  public org.rascalmpl.ast.Type getType() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.FunctionModifiers getModifiers() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Parameters getParameters() { throw new UnsupportedOperationException(); } public boolean hasType() { return false; } public boolean hasModifiers() { return false; } public boolean hasName() { return false; } public boolean hasParameters() { return false; } public boolean isNoThrows() { return false; }
static public class NoThrows extends Signature {
/** type:Type modifiers:FunctionModifiers name:Name parameters:Parameters -> Signature {cons("NoThrows")} */
	protected NoThrows(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.FunctionModifiers modifiers, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Parameters parameters) {
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

private final org.rascalmpl.ast.Type type;
	public org.rascalmpl.ast.Type getType() { return type; }
	private final org.rascalmpl.ast.FunctionModifiers modifiers;
	public org.rascalmpl.ast.FunctionModifiers getModifiers() { return modifiers; }
	private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.Parameters parameters;
	public org.rascalmpl.ast.Parameters getParameters() { return parameters; }	
}
static public class Ambiguity extends Signature {
  private final java.util.List<org.rascalmpl.ast.Signature> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Signature> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Signature> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitSignatureAmbiguity(this);
  }
} public java.util.List<org.rascalmpl.ast.Type> getExceptions() { throw new UnsupportedOperationException(); } public boolean hasExceptions() { return false; }
public boolean isWithThrows() { return false; }
static public class WithThrows extends Signature {
/** type:Type modifiers:FunctionModifiers name:Name parameters:Parameters 
            "throws" exceptions:{Type ","}+ -> Signature {cons("WithThrows")} */
	protected WithThrows(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.FunctionModifiers modifiers, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Parameters parameters, java.util.List<org.rascalmpl.ast.Type> exceptions) {
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

private final org.rascalmpl.ast.Type type;
	public org.rascalmpl.ast.Type getType() { return type; }
	private final org.rascalmpl.ast.FunctionModifiers modifiers;
	public org.rascalmpl.ast.FunctionModifiers getModifiers() { return modifiers; }
	private final org.rascalmpl.ast.Name name;
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.Parameters parameters;
	public org.rascalmpl.ast.Parameters getParameters() { return parameters; }
	private final java.util.List<org.rascalmpl.ast.Type> exceptions;
	public java.util.List<org.rascalmpl.ast.Type> getExceptions() { return exceptions; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}