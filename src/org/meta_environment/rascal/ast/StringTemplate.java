package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class StringTemplate extends AbstractAST { 
  public java.util.List<org.meta_environment.rascal.ast.Expression> getGenerators() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.StringMiddle getBody() { throw new UnsupportedOperationException(); } public boolean hasGenerators() { return false; } public boolean hasBody() { return false; } public boolean isFor() { return false; }
static public class For extends StringTemplate {
/** "for" "(" generators:{Expression ","}+ ")" "{" body:StringMiddle "}" -> StringTemplate {cons("For")} */
	public For(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> generators, org.meta_environment.rascal.ast.StringMiddle body) {
		this.node = node;
		this.generators = generators;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTemplateFor(this);
	}

	public boolean isFor() { return true; }

	public boolean hasGenerators() { return true; }
	public boolean hasBody() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Expression> generators;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getGenerators() { return generators; }
	private final org.meta_environment.rascal.ast.StringMiddle body;
	public org.meta_environment.rascal.ast.StringMiddle getBody() { return body; }	
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
/** "if" "(" conditions:{Expression ","}+ ")" "{" body:StringMiddle "}" -> StringTemplate {cons("IfThen")} */
	public IfThen(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.StringMiddle body) {
		this.node = node;
		this.conditions = conditions;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTemplateIfThen(this);
	}

	public boolean isIfThen() { return true; }

	public boolean hasConditions() { return true; }
	public boolean hasBody() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private final org.meta_environment.rascal.ast.StringMiddle body;
	public org.meta_environment.rascal.ast.StringMiddle getBody() { return body; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.StringMiddle getThenString() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.StringMiddle getElseString() { throw new UnsupportedOperationException(); } public boolean hasThenString() { return false; }
	public boolean hasElseString() { return false; }
public boolean isIfThenElse() { return false; }
static public class IfThenElse extends StringTemplate {
/** "if" "(" conditions:{Expression ","}+ ")" "{" thenString:StringMiddle "}" "else" "{" elseString:StringMiddle "}" -> StringTemplate {cons("IfThenElse")} */
	public IfThenElse(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.StringMiddle thenString, org.meta_environment.rascal.ast.StringMiddle elseString) {
		this.node = node;
		this.conditions = conditions;
		this.thenString = thenString;
		this.elseString = elseString;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTemplateIfThenElse(this);
	}

	public boolean isIfThenElse() { return true; }

	public boolean hasConditions() { return true; }
	public boolean hasThenString() { return true; }
	public boolean hasElseString() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private final org.meta_environment.rascal.ast.StringMiddle thenString;
	public org.meta_environment.rascal.ast.StringMiddle getThenString() { return thenString; }
	private final org.meta_environment.rascal.ast.StringMiddle elseString;
	public org.meta_environment.rascal.ast.StringMiddle getElseString() { return elseString; }	
} public org.meta_environment.rascal.ast.Expression getCondition() { throw new UnsupportedOperationException(); } public boolean hasCondition() { return false; } public boolean isWhile() { return false; }
static public class While extends StringTemplate {
/** "while" "(" condition:Expression ")" "{" body:StringMiddle "}" -> StringTemplate {cons("While")} */
	public While(INode node, org.meta_environment.rascal.ast.Expression condition, org.meta_environment.rascal.ast.StringMiddle body) {
		this.node = node;
		this.condition = condition;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTemplateWhile(this);
	}

	public boolean isWhile() { return true; }

	public boolean hasCondition() { return true; }
	public boolean hasBody() { return true; }

private final org.meta_environment.rascal.ast.Expression condition;
	public org.meta_environment.rascal.ast.Expression getCondition() { return condition; }
	private final org.meta_environment.rascal.ast.StringMiddle body;
	public org.meta_environment.rascal.ast.StringMiddle getBody() { return body; }	
} public boolean isDoWhile() { return false; }
static public class DoWhile extends StringTemplate {
/** "do" "{" body:StringMiddle  "}" "while" "(" condition:Expression ")" -> StringTemplate {cons("DoWhile")} */
	public DoWhile(INode node, org.meta_environment.rascal.ast.StringMiddle body, org.meta_environment.rascal.ast.Expression condition) {
		this.node = node;
		this.body = body;
		this.condition = condition;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTemplateDoWhile(this);
	}

	public boolean isDoWhile() { return true; }

	public boolean hasBody() { return true; }
	public boolean hasCondition() { return true; }

private final org.meta_environment.rascal.ast.StringMiddle body;
	public org.meta_environment.rascal.ast.StringMiddle getBody() { return body; }
	private final org.meta_environment.rascal.ast.Expression condition;
	public org.meta_environment.rascal.ast.Expression getCondition() { return condition; }	
}
}