package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Header extends AbstractAST { 
  public org.meta_environment.rascal.ast.Tags getTags() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.QualifiedName getName() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Import> getImports() { throw new UnsupportedOperationException(); } public boolean hasTags() { return false; } public boolean hasName() { return false; } public boolean hasImports() { return false; } public boolean isDefault() { return false; }
static public class Default extends Header {
/** tags:Tags "module" name:QualifiedName imports:Import* -> Header {cons("Default")} */
	public Default(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.QualifiedName name, java.util.List<org.meta_environment.rascal.ast.Import> imports) {
		this.node = node;
		this.tags = tags;
		this.name = name;
		this.imports = imports;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitHeaderDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasName() { return true; }
	public boolean hasImports() { return true; }

private final org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private final org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private final java.util.List<org.meta_environment.rascal.ast.Import> imports;
	public java.util.List<org.meta_environment.rascal.ast.Import> getImports() { return imports; }	
}
static public class Ambiguity extends Header {
  private final java.util.List<org.meta_environment.rascal.ast.Header> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Header> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Header> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitHeaderAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.ModuleParameters getParams() { throw new UnsupportedOperationException(); } public boolean hasParams() { return false; } public boolean isParameters() { return false; }
static public class Parameters extends Header {
/** tags:Tags "module" name:QualifiedName params:ModuleParameters  imports:Import* -> Header {cons("Parameters")} */
	public Parameters(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleParameters params, java.util.List<org.meta_environment.rascal.ast.Import> imports) {
		this.node = node;
		this.tags = tags;
		this.name = name;
		this.params = params;
		this.imports = imports;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitHeaderParameters(this);
	}

	public boolean isParameters() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasName() { return true; }
	public boolean hasParams() { return true; }
	public boolean hasImports() { return true; }

private final org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private final org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private final org.meta_environment.rascal.ast.ModuleParameters params;
	public org.meta_environment.rascal.ast.ModuleParameters getParams() { return params; }
	private final java.util.List<org.meta_environment.rascal.ast.Import> imports;
	public java.util.List<org.meta_environment.rascal.ast.Import> getImports() { return imports; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}