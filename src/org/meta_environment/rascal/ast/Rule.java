package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Rule extends AbstractAST { 
  public org.meta_environment.rascal.ast.Expression getPattern() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Replacement getReplacement() { throw new UnsupportedOperationException(); } public boolean hasPattern() { return false; } public boolean hasReplacement() { return false; }
public boolean isReplacing() { return false; }
static public class Replacing extends Rule {
/* pattern:Expression "=>" replacement:Replacement -> Rule {cons("Replacing")} */
	private Replacing() { }
	/*package*/ Replacing(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Replacement replacement) {
		this.node = node;
		this.pattern = pattern;
		this.replacement = replacement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitRuleReplacing(this);
	}

	public boolean isReplacing() { return true; }

	public boolean hasPattern() { return true; }
	public boolean hasReplacement() { return true; }

private org.meta_environment.rascal.ast.Expression pattern;
	public org.meta_environment.rascal.ast.Expression getPattern() { return pattern; }
	private void $setPattern(org.meta_environment.rascal.ast.Expression x) { this.pattern = x; }
	public Replacing setPattern(org.meta_environment.rascal.ast.Expression x) { 
		Replacing z = new Replacing();
 		z.$setPattern(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Replacement replacement;
	public org.meta_environment.rascal.ast.Replacement getReplacement() { return replacement; }
	private void $setReplacement(org.meta_environment.rascal.ast.Replacement x) { this.replacement = x; }
	public Replacing setReplacement(org.meta_environment.rascal.ast.Replacement x) { 
		Replacing z = new Replacing();
 		z.$setReplacement(x);
		return z;
	}	
}
static public class Ambiguity extends Rule {
  private final java.util.List<org.meta_environment.rascal.ast.Rule> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Rule> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Rule> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitRuleAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.Statement getStatement() { throw new UnsupportedOperationException(); } public boolean hasStatement() { return false; }
public boolean isArbitrary() { return false; }
static public class Arbitrary extends Rule {
/* pattern:Expression ":" statement:Statement -> Rule {cons("Arbitrary")} */
	private Arbitrary() { }
	/*package*/ Arbitrary(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Statement statement) {
		this.node = node;
		this.pattern = pattern;
		this.statement = statement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitRuleArbitrary(this);
	}

	public boolean isArbitrary() { return true; }

	public boolean hasPattern() { return true; }
	public boolean hasStatement() { return true; }

private org.meta_environment.rascal.ast.Expression pattern;
	public org.meta_environment.rascal.ast.Expression getPattern() { return pattern; }
	private void $setPattern(org.meta_environment.rascal.ast.Expression x) { this.pattern = x; }
	public Arbitrary setPattern(org.meta_environment.rascal.ast.Expression x) { 
		Arbitrary z = new Arbitrary();
 		z.$setPattern(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Statement statement;
	public org.meta_environment.rascal.ast.Statement getStatement() { return statement; }
	private void $setStatement(org.meta_environment.rascal.ast.Statement x) { this.statement = x; }
	public Arbitrary setStatement(org.meta_environment.rascal.ast.Statement x) { 
		Arbitrary z = new Arbitrary();
 		z.$setStatement(x);
		return z;
	}	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.Type getType() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Rule getRule() { throw new UnsupportedOperationException(); }
public boolean hasType() { return false; }
	public boolean hasRule() { return false; }
public boolean isGuarded() { return false; }
static public class Guarded extends Rule {
/* "[" type:Type "]" rule:Rule -> Rule {non-assoc, cons("Guarded")} */
	private Guarded() { }
	/*package*/ Guarded(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Rule rule) {
		this.node = node;
		this.type = type;
		this.rule = rule;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitRuleGuarded(this);
	}

	public boolean isGuarded() { return true; }

	public boolean hasType() { return true; }
	public boolean hasRule() { return true; }

private org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }
	private void $setType(org.meta_environment.rascal.ast.Type x) { this.type = x; }
	public Guarded setType(org.meta_environment.rascal.ast.Type x) { 
		Guarded z = new Guarded();
 		z.$setType(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Rule rule;
	public org.meta_environment.rascal.ast.Rule getRule() { return rule; }
	private void $setRule(org.meta_environment.rascal.ast.Rule x) { this.rule = x; }
	public Guarded setRule(org.meta_environment.rascal.ast.Rule x) { 
		Guarded z = new Guarded();
 		z.$setRule(x);
		return z;
	}	
}
}