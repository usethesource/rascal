package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Replacement extends AbstractAST { 
  public org.meta_environment.rascal.ast.Expression getReplacement() { throw new UnsupportedOperationException(); } public boolean hasReplacement() { return false; } public boolean isConditional() { return false; }
static public class Conditional extends Replacement {
/* replacement:Expression -> Replacement {cons("Conditional")} */
	private Conditional() { }
	/*package*/ Conditional(ITree tree, org.meta_environment.rascal.ast.Expression replacement) {
		this.tree = tree;
		this.replacement = replacement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitReplacementConditional(this);
	}

	public boolean isConditional() { return true; }

	public boolean hasReplacement() { return true; }

private org.meta_environment.rascal.ast.Expression replacement;
	public org.meta_environment.rascal.ast.Expression getReplacement() { return replacement; }
	private void $setReplacement(org.meta_environment.rascal.ast.Expression x) { this.replacement = x; }
	public Conditional setReplacement(org.meta_environment.rascal.ast.Expression x) { 
		Conditional z = new Conditional();
 		z.$setReplacement(x);
		return z;
	}	
}
static public class Ambiguity extends Replacement {
  private final java.util.List<org.meta_environment.rascal.ast.Replacement> alternatives;
  public Ambiguity(ITree tree, java.util.List<org.meta_environment.rascal.ast.Replacement> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.tree = tree;
  }
  public java.util.List<org.meta_environment.rascal.ast.Replacement> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitReplacementAmbiguity(this);
  }
} public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { throw new UnsupportedOperationException(); } public boolean hasConditions() { return false; }
public boolean isUnconditional() { return false; }
static public class Unconditional extends Replacement {
/* replacement:Expression "when" conditions:{Expression ","}+ -> Replacement {cons("Unconditional")} */
	private Unconditional() { }
	/*package*/ Unconditional(ITree tree, org.meta_environment.rascal.ast.Expression replacement, java.util.List<org.meta_environment.rascal.ast.Expression> conditions) {
		this.tree = tree;
		this.replacement = replacement;
		this.conditions = conditions;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitReplacementUnconditional(this);
	}

	public boolean isUnconditional() { return true; }

	public boolean hasReplacement() { return true; }
	public boolean hasConditions() { return true; }

private org.meta_environment.rascal.ast.Expression replacement;
	public org.meta_environment.rascal.ast.Expression getReplacement() { return replacement; }
	private void $setReplacement(org.meta_environment.rascal.ast.Expression x) { this.replacement = x; }
	public Unconditional setReplacement(org.meta_environment.rascal.ast.Expression x) { 
		Unconditional z = new Unconditional();
 		z.$setReplacement(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private void $setConditions(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.conditions = x; }
	public Unconditional setConditions(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		Unconditional z = new Unconditional();
 		z.$setConditions(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}