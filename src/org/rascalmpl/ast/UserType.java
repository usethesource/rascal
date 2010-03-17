package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class UserType extends AbstractAST { 
  public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean isName() { return false; }
static public class Name extends UserType {
/** name:Name -> UserType {cons("Name")} */
	public Name(INode node, org.rascalmpl.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitUserTypeName(this);
	}

	@Override
	public boolean isName() { return true; }

	@Override
	public boolean hasName() { return true; }

private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }	
}
static public class Ambiguity extends UserType {
  private final java.util.List<org.rascalmpl.ast.UserType> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.UserType> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.UserType> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitUserTypeAmbiguity(this);
  }
} public java.util.List<org.rascalmpl.ast.Type> getParameters() { throw new UnsupportedOperationException(); } public boolean hasParameters() { return false; }
public boolean isParametric() { return false; }
static public class Parametric extends UserType {
/** name:Name "[" parameters:{Type ","}+ "]" -> UserType {cons("Parametric")} */
	public Parametric(INode node, org.rascalmpl.ast.Name name, java.util.List<org.rascalmpl.ast.Type> parameters) {
		this.node = node;
		this.name = name;
		this.parameters = parameters;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitUserTypeParametric(this);
	}

	@Override
	public boolean isParametric() { return true; }

	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasParameters() { return true; }

private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }
	private final java.util.List<org.rascalmpl.ast.Type> parameters;
	@Override
	public java.util.List<org.rascalmpl.ast.Type> getParameters() { return parameters; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}