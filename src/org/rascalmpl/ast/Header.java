package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Header extends AbstractAST { 
  public org.rascalmpl.ast.Tags getTags() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.QualifiedName getName() { throw new UnsupportedOperationException(); } public java.util.List<org.rascalmpl.ast.Import> getImports() { throw new UnsupportedOperationException(); } public boolean hasTags() { return false; } public boolean hasName() { return false; } public boolean hasImports() { return false; } public boolean isDefault() { return false; }
static public class Default extends Header {
/** tags:Tags "module" name:QualifiedName imports:Import* -> Header {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.QualifiedName name, java.util.List<org.rascalmpl.ast.Import> imports) {
		this.node = node;
		this.tags = tags;
		this.name = name;
		this.imports = imports;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitHeaderDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasImports() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.QualifiedName name;
	@Override
	public org.rascalmpl.ast.QualifiedName getName() { return name; }
	private final java.util.List<org.rascalmpl.ast.Import> imports;
	@Override
	public java.util.List<org.rascalmpl.ast.Import> getImports() { return imports; }	
}
static public class Ambiguity extends Header {
  private final java.util.List<org.rascalmpl.ast.Header> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Header> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Header> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitHeaderAmbiguity(this);
  }
} public org.rascalmpl.ast.ModuleParameters getParams() { throw new UnsupportedOperationException(); } public boolean hasParams() { return false; } public boolean isParameters() { return false; }
static public class Parameters extends Header {
/** tags:Tags "module" name:QualifiedName params:ModuleParameters  imports:Import* -> Header {cons("Parameters")} */
	public Parameters(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.ModuleParameters params, java.util.List<org.rascalmpl.ast.Import> imports) {
		this.node = node;
		this.tags = tags;
		this.name = name;
		this.params = params;
		this.imports = imports;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitHeaderParameters(this);
	}

	@Override
	public boolean isParameters() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasName() { return true; }
	@Override
	public boolean hasParams() { return true; }
	@Override
	public boolean hasImports() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.QualifiedName name;
	@Override
	public org.rascalmpl.ast.QualifiedName getName() { return name; }
	private final org.rascalmpl.ast.ModuleParameters params;
	@Override
	public org.rascalmpl.ast.ModuleParameters getParams() { return params; }
	private final java.util.List<org.rascalmpl.ast.Import> imports;
	@Override
	public java.util.List<org.rascalmpl.ast.Import> getImports() { return imports; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}