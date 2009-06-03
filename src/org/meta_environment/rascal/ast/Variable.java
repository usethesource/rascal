package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Variable extends AbstractAST { 
  public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Tags getTags() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean hasTags() { return false; } public boolean isUnInitialized() { return false; }
static public class UnInitialized extends Variable {
/** name:Name tags:Tags -> Variable {cons("UnInitialized")} */
	private UnInitialized() {
		super();
	}
	public UnInitialized(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags) {
		this.node = node;
		this.name = name;
		this.tags = tags;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVariableUnInitialized(this);
	}

	public boolean isUnInitialized() { return true; }

	public boolean hasName() { return true; }
	public boolean hasTags() { return true; }

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public UnInitialized setName(org.meta_environment.rascal.ast.Name x) { 
		UnInitialized z = new UnInitialized();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public UnInitialized setTags(org.meta_environment.rascal.ast.Tags x) { 
		UnInitialized z = new UnInitialized();
 		z.$setTags(x);
		return z;
	}	
}
static public class Ambiguity extends Variable {
  private final java.util.List<org.meta_environment.rascal.ast.Variable> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Variable> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Variable> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitVariableAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.Expression getInitial() { throw new UnsupportedOperationException(); } public boolean hasInitial() { return false; }
public boolean isInitialized() { return false; }
static public class Initialized extends Variable {
/** name:Name tags:Tags "=" initial:Expression -> Variable {cons("Initialized")} */
	private Initialized() {
		super();
	}
	public Initialized(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Expression initial) {
		this.node = node;
		this.name = name;
		this.tags = tags;
		this.initial = initial;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVariableInitialized(this);
	}

	public boolean isInitialized() { return true; }

	public boolean hasName() { return true; }
	public boolean hasTags() { return true; }
	public boolean hasInitial() { return true; }

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Initialized setName(org.meta_environment.rascal.ast.Name x) { 
		Initialized z = new Initialized();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Initialized setTags(org.meta_environment.rascal.ast.Tags x) { 
		Initialized z = new Initialized();
 		z.$setTags(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression initial;
	public org.meta_environment.rascal.ast.Expression getInitial() { return initial; }
	private void $setInitial(org.meta_environment.rascal.ast.Expression x) { this.initial = x; }
	public Initialized setInitial(org.meta_environment.rascal.ast.Expression x) { 
		Initialized z = new Initialized();
 		z.$setInitial(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}