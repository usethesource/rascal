package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode;
public abstract class Header extends AbstractAST { 
  public org.meta_environment.rascal.ast.QualifiedName getName() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Tags getTags() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Import> getImports() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean hasTags() { return false; } public boolean hasImports() { return false; } public boolean isDefault() { return false; }
static public class Default extends Header {
/** "module" name:QualifiedName tags:Tags imports:Import* -> Header {cons("Default")} */
	private Default() {
		super();
	}
	public Default(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Import> imports) {
		this.node = node;
		this.name = name;
		this.tags = tags;
		this.imports = imports;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitHeaderDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasName() { return true; }
	public boolean hasTags() { return true; }
	public boolean hasImports() { return true; }

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public Default setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		Default z = new Default();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Default setTags(org.meta_environment.rascal.ast.Tags x) { 
		Default z = new Default();
 		z.$setTags(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Import> imports;
	public java.util.List<org.meta_environment.rascal.ast.Import> getImports() { return imports; }
	private void $setImports(java.util.List<org.meta_environment.rascal.ast.Import> x) { this.imports = x; }
	public Default setImports(java.util.List<org.meta_environment.rascal.ast.Import> x) { 
		Default z = new Default();
 		z.$setImports(x);
		return z;
	}	
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
/** "module" name:QualifiedName params:ModuleParameters tags:Tags imports:Import* -> Header {cons("Parameters")} */
	private Parameters() {
		super();
	}
	public Parameters(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleParameters params, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Import> imports) {
		this.node = node;
		this.name = name;
		this.params = params;
		this.tags = tags;
		this.imports = imports;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitHeaderParameters(this);
	}

	public boolean isParameters() { return true; }

	public boolean hasName() { return true; }
	public boolean hasParams() { return true; }
	public boolean hasTags() { return true; }
	public boolean hasImports() { return true; }

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public Parameters setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		Parameters z = new Parameters();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.ModuleParameters params;
	public org.meta_environment.rascal.ast.ModuleParameters getParams() { return params; }
	private void $setParams(org.meta_environment.rascal.ast.ModuleParameters x) { this.params = x; }
	public Parameters setParams(org.meta_environment.rascal.ast.ModuleParameters x) { 
		Parameters z = new Parameters();
 		z.$setParams(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Parameters setTags(org.meta_environment.rascal.ast.Tags x) { 
		Parameters z = new Parameters();
 		z.$setTags(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Import> imports;
	public java.util.List<org.meta_environment.rascal.ast.Import> getImports() { return imports; }
	private void $setImports(java.util.List<org.meta_environment.rascal.ast.Import> x) { this.imports = x; }
	public Parameters setImports(java.util.List<org.meta_environment.rascal.ast.Import> x) { 
		Parameters z = new Parameters();
 		z.$setImports(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}