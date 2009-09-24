package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class StringTemplate extends AbstractAST { 
  public java.util.List<org.meta_environment.rascal.ast.Expression> getGenerators() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Statement> getPreStats() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.StringMiddle getBody() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Statement> getPostStats() { throw new UnsupportedOperationException(); } public boolean hasGenerators() { return false; } public boolean hasPreStats() { return false; } public boolean hasBody() { return false; } public boolean hasPostStats() { return false; } public boolean isFor() { return false; }
static public class For extends StringTemplate {
/** "for" "(" generators:{Expression ","}+ ")" "{" preStats:Statement* body:StringMiddle postStats:Statement* "}" -> StringTemplate {cons("For")} */
	public For(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> generators, java.util.List<org.meta_environment.rascal.ast.Statement> preStats, org.meta_environment.rascal.ast.StringMiddle body, java.util.List<org.meta_environment.rascal.ast.Statement> postStats) {
		this.node = node;
		this.generators = generators;
		this.preStats = preStats;
		this.body = body;
		this.postStats = postStats;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTemplateFor(this);
	}

	public boolean isFor() { return true; }

	public boolean hasGenerators() { return true; }
	public boolean hasPreStats() { return true; }
	public boolean hasBody() { return true; }
	public boolean hasPostStats() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Expression> generators;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getGenerators() { return generators; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> preStats;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPreStats() { return preStats; }
	private final org.meta_environment.rascal.ast.StringMiddle body;
	public org.meta_environment.rascal.ast.StringMiddle getBody() { return body; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> postStats;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPostStats() { return postStats; }	
}
static public class Ambiguity extends StringTemplate {
  private final java.util.List<org.meta_environment.rascal.ast.StringTemplate> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringTemplate> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.StringTemplate> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStringTemplateAmbiguity(this);
  }
} public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { throw new UnsupportedOperationException(); } public boolean hasConditions() { return false; } public boolean isIfThen() { return false; }
static public class IfThen extends StringTemplate {
/** "if" "(" conditions:{Expression ","}+ ")" "{" preStats:Statement* body:StringMiddle postStats:Statement* "}" -> StringTemplate {cons("IfThen")} */
	public IfThen(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, java.util.List<org.meta_environment.rascal.ast.Statement> preStats, org.meta_environment.rascal.ast.StringMiddle body, java.util.List<org.meta_environment.rascal.ast.Statement> postStats) {
		this.node = node;
		this.conditions = conditions;
		this.preStats = preStats;
		this.body = body;
		this.postStats = postStats;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTemplateIfThen(this);
	}

	public boolean isIfThen() { return true; }

	public boolean hasConditions() { return true; }
	public boolean hasPreStats() { return true; }
	public boolean hasBody() { return true; }
	public boolean hasPostStats() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> preStats;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPreStats() { return preStats; }
	private final org.meta_environment.rascal.ast.StringMiddle body;
	public org.meta_environment.rascal.ast.StringMiddle getBody() { return body; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> postStats;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPostStats() { return postStats; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public java.util.List<org.meta_environment.rascal.ast.Statement> getPreStatsThen() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.StringMiddle getThenString() { throw new UnsupportedOperationException(); }
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPostStatsThen() { throw new UnsupportedOperationException(); }
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPreStatsElse() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.StringMiddle getElseString() { throw new UnsupportedOperationException(); }
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPostStatsElse() { throw new UnsupportedOperationException(); } public boolean hasPreStatsThen() { return false; }
	public boolean hasThenString() { return false; }
	public boolean hasPostStatsThen() { return false; }
	public boolean hasPreStatsElse() { return false; }
	public boolean hasElseString() { return false; }
	public boolean hasPostStatsElse() { return false; }
public boolean isIfThenElse() { return false; }
static public class IfThenElse extends StringTemplate {
/** "if" "(" conditions:{Expression ","}+ ")" "{" preStatsThen:Statement* thenString:StringMiddle postStatsThen:Statement* "}" "else" "{" preStatsElse:Statement* elseString:StringMiddle postStatsElse:Statement*  "}" -> StringTemplate {cons("IfThenElse")} */
	public IfThenElse(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, java.util.List<org.meta_environment.rascal.ast.Statement> preStatsThen, org.meta_environment.rascal.ast.StringMiddle thenString, java.util.List<org.meta_environment.rascal.ast.Statement> postStatsThen, java.util.List<org.meta_environment.rascal.ast.Statement> preStatsElse, org.meta_environment.rascal.ast.StringMiddle elseString, java.util.List<org.meta_environment.rascal.ast.Statement> postStatsElse) {
		this.node = node;
		this.conditions = conditions;
		this.preStatsThen = preStatsThen;
		this.thenString = thenString;
		this.postStatsThen = postStatsThen;
		this.preStatsElse = preStatsElse;
		this.elseString = elseString;
		this.postStatsElse = postStatsElse;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTemplateIfThenElse(this);
	}

	public boolean isIfThenElse() { return true; }

	public boolean hasConditions() { return true; }
	public boolean hasPreStatsThen() { return true; }
	public boolean hasThenString() { return true; }
	public boolean hasPostStatsThen() { return true; }
	public boolean hasPreStatsElse() { return true; }
	public boolean hasElseString() { return true; }
	public boolean hasPostStatsElse() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> preStatsThen;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPreStatsThen() { return preStatsThen; }
	private final org.meta_environment.rascal.ast.StringMiddle thenString;
	public org.meta_environment.rascal.ast.StringMiddle getThenString() { return thenString; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> postStatsThen;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPostStatsThen() { return postStatsThen; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> preStatsElse;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPreStatsElse() { return preStatsElse; }
	private final org.meta_environment.rascal.ast.StringMiddle elseString;
	public org.meta_environment.rascal.ast.StringMiddle getElseString() { return elseString; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> postStatsElse;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPostStatsElse() { return postStatsElse; }	
} public org.meta_environment.rascal.ast.Expression getCondition() { throw new UnsupportedOperationException(); } public boolean hasCondition() { return false; } public boolean isWhile() { return false; }
static public class While extends StringTemplate {
/** "while" "(" condition:Expression ")" "{" preStats:Statement* body:StringMiddle postStats:Statement* "}" -> StringTemplate {cons("While")} */
	public While(INode node, org.meta_environment.rascal.ast.Expression condition, java.util.List<org.meta_environment.rascal.ast.Statement> preStats, org.meta_environment.rascal.ast.StringMiddle body, java.util.List<org.meta_environment.rascal.ast.Statement> postStats) {
		this.node = node;
		this.condition = condition;
		this.preStats = preStats;
		this.body = body;
		this.postStats = postStats;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTemplateWhile(this);
	}

	public boolean isWhile() { return true; }

	public boolean hasCondition() { return true; }
	public boolean hasPreStats() { return true; }
	public boolean hasBody() { return true; }
	public boolean hasPostStats() { return true; }

private final org.meta_environment.rascal.ast.Expression condition;
	public org.meta_environment.rascal.ast.Expression getCondition() { return condition; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> preStats;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPreStats() { return preStats; }
	private final org.meta_environment.rascal.ast.StringMiddle body;
	public org.meta_environment.rascal.ast.StringMiddle getBody() { return body; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> postStats;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPostStats() { return postStats; }	
} public boolean isDoWhile() { return false; }
static public class DoWhile extends StringTemplate {
/** "do" "{" preStats:Statement* body:StringMiddle  postStats:Statement* "}" "while" "(" condition:Expression ")" -> StringTemplate {cons("DoWhile")} */
	public DoWhile(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> preStats, org.meta_environment.rascal.ast.StringMiddle body, java.util.List<org.meta_environment.rascal.ast.Statement> postStats, org.meta_environment.rascal.ast.Expression condition) {
		this.node = node;
		this.preStats = preStats;
		this.body = body;
		this.postStats = postStats;
		this.condition = condition;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTemplateDoWhile(this);
	}

	public boolean isDoWhile() { return true; }

	public boolean hasPreStats() { return true; }
	public boolean hasBody() { return true; }
	public boolean hasPostStats() { return true; }
	public boolean hasCondition() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Statement> preStats;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPreStats() { return preStats; }
	private final org.meta_environment.rascal.ast.StringMiddle body;
	public org.meta_environment.rascal.ast.StringMiddle getBody() { return body; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> postStats;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getPostStats() { return postStats; }
	private final org.meta_environment.rascal.ast.Expression condition;
	public org.meta_environment.rascal.ast.Expression getCondition() { return condition; }	
}
}