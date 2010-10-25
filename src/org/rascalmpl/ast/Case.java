package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Case extends AbstractAST { 
  public org.rascalmpl.ast.PatternWithAction getPatternWithAction() { throw new UnsupportedOperationException(); }
public boolean hasPatternWithAction() { return false; }
public boolean isPatternWithAction() { return false; }
static public class PatternWithAction extends Case {
/** "case" patternWithAction:PatternWithAction -> Case {cons("PatternWithAction")} */
	protected PatternWithAction(INode node, org.rascalmpl.ast.PatternWithAction patternWithAction) {
		this.node = node;
		this.patternWithAction = patternWithAction;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCasePatternWithAction(this);
	}

	public boolean isPatternWithAction() { return true; }

	public boolean hasPatternWithAction() { return true; }

private final org.rascalmpl.ast.PatternWithAction patternWithAction;
	public org.rascalmpl.ast.PatternWithAction getPatternWithAction() { return patternWithAction; }	
}
static public class Ambiguity extends Case {
  private final java.util.List<org.rascalmpl.ast.Case> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Case> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Case> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCaseAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Statement getStatement() { throw new UnsupportedOperationException(); }
public boolean hasStatement() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Case {
/** "default" ":" statement:Statement -> Case {cons("Default")} */
	protected Default(INode node, org.rascalmpl.ast.Statement statement) {
		this.node = node;
		this.statement = statement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitCaseDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasStatement() { return true; }

private final org.rascalmpl.ast.Statement statement;
	public org.rascalmpl.ast.Statement getStatement() { return statement; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}