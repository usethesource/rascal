package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class PatternWithAction extends AbstractAST { 
  public org.rascalmpl.ast.Expression getPattern() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Replacement getReplacement() { throw new UnsupportedOperationException(); } public boolean hasPattern() { return false; } public boolean hasReplacement() { return false; }
public boolean isReplacing() { return false; }
static public class Replacing extends PatternWithAction {
/** pattern:Expression "=>" replacement:Replacement -> PatternWithAction {cons("Replacing")} */
	public Replacing(INode node, org.rascalmpl.ast.Expression pattern, org.rascalmpl.ast.Replacement replacement) {
		this.node = node;
		this.pattern = pattern;
		this.replacement = replacement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitPatternWithActionReplacing(this);
	}

	public boolean isReplacing() { return true; }

	public boolean hasPattern() { return true; }
	public boolean hasReplacement() { return true; }

private final org.rascalmpl.ast.Expression pattern;
	public org.rascalmpl.ast.Expression getPattern() { return pattern; }
	private final org.rascalmpl.ast.Replacement replacement;
	public org.rascalmpl.ast.Replacement getReplacement() { return replacement; }	
}
static public class Ambiguity extends PatternWithAction {
  private final java.util.List<org.rascalmpl.ast.PatternWithAction> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PatternWithAction> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.PatternWithAction> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitPatternWithActionAmbiguity(this);
  }
} public org.rascalmpl.ast.Statement getStatement() { throw new UnsupportedOperationException(); } public boolean hasStatement() { return false; }
public boolean isArbitrary() { return false; }
static public class Arbitrary extends PatternWithAction {
/** pattern:Expression ":" statement:Statement -> PatternWithAction {cons("Arbitrary")} */
	public Arbitrary(INode node, org.rascalmpl.ast.Expression pattern, org.rascalmpl.ast.Statement statement) {
		this.node = node;
		this.pattern = pattern;
		this.statement = statement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitPatternWithActionArbitrary(this);
	}

	public boolean isArbitrary() { return true; }

	public boolean hasPattern() { return true; }
	public boolean hasStatement() { return true; }

private final org.rascalmpl.ast.Expression pattern;
	public org.rascalmpl.ast.Expression getPattern() { return pattern; }
	private final org.rascalmpl.ast.Statement statement;
	public org.rascalmpl.ast.Statement getStatement() { return statement; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}