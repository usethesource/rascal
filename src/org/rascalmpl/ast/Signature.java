package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Signature extends AbstractAST { 
  public org.rascalmpl.ast.Type getType() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.FunctionModifiers getModifiers() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Parameters getParameters() { throw new UnsupportedOperationException(); } public boolean hasType() { return false; } public boolean hasModifiers() { return false; } public boolean hasName() { return false; } public boolean hasParameters() { return false; } public boolean isNoThrows() { return false; }
static public class NoThrows extends Signature {
/** type:Type modifiers:FunctionModifiers name:Name parameters:Parameters -> Signature {cons("NoThrows")} */
	public NoThrows(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.FunctionModifiers modifiers, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Parameters parameters) {
		this.node = node;
		this.type = type;
		this.modifiers = modifiers;
		this.name = name;
		this.parameters = parameters;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSignatureNoThrows(this);
	}

	@Override
	public boolean isNoThrows() { return true; }

	@Override
	public boolean hasType() { return true; }
	@Override
	public boolean hasModifiers() { return true; }
	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasParameters() { return true; }

private final org.rascalmpl.ast.Type type;
	@Override
	public org.rascalmpl.ast.Type getType() { return type; }
	private final org.rascalmpl.ast.FunctionModifiers modifiers;
	@Override
	public org.rascalmpl.ast.FunctionModifiers getModifiers() { return modifiers; }
	private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.Parameters parameters;
	@Override
	public org.rascalmpl.ast.Parameters getParameters() { return parameters; }	
}
static public class Ambiguity extends Signature {
  private final java.util.List<org.rascalmpl.ast.Signature> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Signature> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Signature> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitSignatureAmbiguity(this);
  }
} public java.util.List<org.rascalmpl.ast.Type> getExceptions() { throw new UnsupportedOperationException(); } public boolean hasExceptions() { return false; }
public boolean isWithThrows() { return false; }
static public class WithThrows extends Signature {
/** type:Type modifiers:FunctionModifiers name:Name parameters:Parameters 
            "throws" exceptions:{Type ","}+ -> Signature {cons("WithThrows")} */
	public WithThrows(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.FunctionModifiers modifiers, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Parameters parameters, java.util.List<org.rascalmpl.ast.Type> exceptions) {
		this.node = node;
		this.type = type;
		this.modifiers = modifiers;
		this.name = name;
		this.parameters = parameters;
		this.exceptions = exceptions;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSignatureWithThrows(this);
	}

	@Override
	public boolean isWithThrows() { return true; }

	@Override
	public boolean hasType() { return true; }
	@Override
	public boolean hasModifiers() { return true; }
	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasParameters() { return true; }
	@Override
	public boolean hasExceptions() { return true; }

private final org.rascalmpl.ast.Type type;
	@Override
	public org.rascalmpl.ast.Type getType() { return type; }
	private final org.rascalmpl.ast.FunctionModifiers modifiers;
	@Override
	public org.rascalmpl.ast.FunctionModifiers getModifiers() { return modifiers; }
	private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }
	private final org.rascalmpl.ast.Parameters parameters;
	@Override
	public org.rascalmpl.ast.Parameters getParameters() { return parameters; }
	private final java.util.List<org.rascalmpl.ast.Type> exceptions;
	@Override
	public java.util.List<org.rascalmpl.ast.Type> getExceptions() { return exceptions; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}