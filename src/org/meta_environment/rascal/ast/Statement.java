package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Statement extends AbstractAST { 
  public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getVariables() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Bound getBound() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Statement getBody() { throw new UnsupportedOperationException(); } public boolean hasVariables() { return false; }
	public boolean hasBound() { return false; } public boolean hasBody() { return false; } public boolean isSolve() { return false; }
static public class Solve extends Statement {
/** "solve" "(" variables:{QualifiedName ","}+ bound:Bound ")" body:Statement -> Statement {cons("Solve")} */
	private Solve() {
		super();
	}
	public Solve(INode node, java.util.List<org.meta_environment.rascal.ast.QualifiedName> variables, org.meta_environment.rascal.ast.Bound bound, org.meta_environment.rascal.ast.Statement body) {
		this.node = node;
		this.variables = variables;
		this.bound = bound;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementSolve(this);
	}

	public boolean isSolve() { return true; }

	public boolean hasVariables() { return true; }
	public boolean hasBound() { return true; }
	public boolean hasBody() { return true; }

private java.util.List<org.meta_environment.rascal.ast.QualifiedName> variables;
	public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getVariables() { return variables; }
	private void $setVariables(java.util.List<org.meta_environment.rascal.ast.QualifiedName> x) { this.variables = x; }
	public Solve setVariables(java.util.List<org.meta_environment.rascal.ast.QualifiedName> x) { 
		Solve z = new Solve();
 		z.$setVariables(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Bound bound;
	public org.meta_environment.rascal.ast.Bound getBound() { return bound; }
	private void $setBound(org.meta_environment.rascal.ast.Bound x) { this.bound = x; }
	public Solve setBound(org.meta_environment.rascal.ast.Bound x) { 
		Solve z = new Solve();
 		z.$setBound(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private void $setBody(org.meta_environment.rascal.ast.Statement x) { this.body = x; }
	public Solve setBody(org.meta_environment.rascal.ast.Statement x) { 
		Solve z = new Solve();
 		z.$setBody(x);
		return z;
	}	
}
static public class Ambiguity extends Statement {
  private final java.util.List<org.meta_environment.rascal.ast.Statement> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Statement> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStatementAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.Label getLabel() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Expression> getGenerators() { throw new UnsupportedOperationException(); } public boolean hasLabel() { return false; } public boolean hasGenerators() { return false; } public boolean isFor() { return false; }
static public class For extends Statement {
/** label:Label "for" "(" generators:{Expression ","}+ ")" body:Statement -> Statement {cons("For")} */
	private For() {
		super();
	}
	public For(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> generators, org.meta_environment.rascal.ast.Statement body) {
		this.node = node;
		this.label = label;
		this.generators = generators;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementFor(this);
	}

	public boolean isFor() { return true; }

	public boolean hasLabel() { return true; }
	public boolean hasGenerators() { return true; }
	public boolean hasBody() { return true; }

private org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private void $setLabel(org.meta_environment.rascal.ast.Label x) { this.label = x; }
	public For setLabel(org.meta_environment.rascal.ast.Label x) { 
		For z = new For();
 		z.$setLabel(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Expression> generators;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getGenerators() { return generators; }
	private void $setGenerators(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.generators = x; }
	public For setGenerators(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		For z = new For();
 		z.$setGenerators(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private void $setBody(org.meta_environment.rascal.ast.Statement x) { this.body = x; }
	public For setBody(org.meta_environment.rascal.ast.Statement x) { 
		For z = new For();
 		z.$setBody(x);
		return z;
	}	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.Expression getCondition() { throw new UnsupportedOperationException(); } public boolean hasCondition() { return false; } public boolean isWhile() { return false; }
static public class While extends Statement {
/** label:Label "while" "(" condition:Expression ")" body:Statement -> Statement {cons("While")} */
	private While() {
		super();
	}
	public While(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Expression condition, org.meta_environment.rascal.ast.Statement body) {
		this.node = node;
		this.label = label;
		this.condition = condition;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementWhile(this);
	}

	public boolean isWhile() { return true; }

	public boolean hasLabel() { return true; }
	public boolean hasCondition() { return true; }
	public boolean hasBody() { return true; }

private org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private void $setLabel(org.meta_environment.rascal.ast.Label x) { this.label = x; }
	public While setLabel(org.meta_environment.rascal.ast.Label x) { 
		While z = new While();
 		z.$setLabel(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression condition;
	public org.meta_environment.rascal.ast.Expression getCondition() { return condition; }
	private void $setCondition(org.meta_environment.rascal.ast.Expression x) { this.condition = x; }
	public While setCondition(org.meta_environment.rascal.ast.Expression x) { 
		While z = new While();
 		z.$setCondition(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private void $setBody(org.meta_environment.rascal.ast.Statement x) { this.body = x; }
	public While setBody(org.meta_environment.rascal.ast.Statement x) { 
		While z = new While();
 		z.$setBody(x);
		return z;
	}	
} public boolean isDoWhile() { return false; }
static public class DoWhile extends Statement {
/** label:Label "do" body:Statement "while" "(" condition:Expression ")" ";" -> Statement {cons("DoWhile")} */
	private DoWhile() {
		super();
	}
	public DoWhile(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Statement body, org.meta_environment.rascal.ast.Expression condition) {
		this.node = node;
		this.label = label;
		this.body = body;
		this.condition = condition;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementDoWhile(this);
	}

	public boolean isDoWhile() { return true; }

	public boolean hasLabel() { return true; }
	public boolean hasBody() { return true; }
	public boolean hasCondition() { return true; }

private org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private void $setLabel(org.meta_environment.rascal.ast.Label x) { this.label = x; }
	public DoWhile setLabel(org.meta_environment.rascal.ast.Label x) { 
		DoWhile z = new DoWhile();
 		z.$setLabel(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private void $setBody(org.meta_environment.rascal.ast.Statement x) { this.body = x; }
	public DoWhile setBody(org.meta_environment.rascal.ast.Statement x) { 
		DoWhile z = new DoWhile();
 		z.$setBody(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression condition;
	public org.meta_environment.rascal.ast.Expression getCondition() { return condition; }
	private void $setCondition(org.meta_environment.rascal.ast.Expression x) { this.condition = x; }
	public DoWhile setCondition(org.meta_environment.rascal.ast.Expression x) { 
		DoWhile z = new DoWhile();
 		z.$setCondition(x);
		return z;
	}	
} public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Statement getThenStatement() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Statement getElseStatement() { throw new UnsupportedOperationException(); } public boolean hasConditions() { return false; } public boolean hasThenStatement() { return false; } public boolean hasElseStatement() { return false; }
public boolean isIfThenElse() { return false; }
static public class IfThenElse extends Statement {
/** label:Label "if" "(" conditions:{Expression ","}+ ")" thenStatement:Statement "else" elseStatement:Statement -> Statement {cons("IfThenElse")} */
	private IfThenElse() {
		super();
	}
	public IfThenElse(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.Statement thenStatement, org.meta_environment.rascal.ast.Statement elseStatement) {
		this.node = node;
		this.label = label;
		this.conditions = conditions;
		this.thenStatement = thenStatement;
		this.elseStatement = elseStatement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementIfThenElse(this);
	}

	public boolean isIfThenElse() { return true; }

	public boolean hasLabel() { return true; }
	public boolean hasConditions() { return true; }
	public boolean hasThenStatement() { return true; }
	public boolean hasElseStatement() { return true; }

private org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private void $setLabel(org.meta_environment.rascal.ast.Label x) { this.label = x; }
	public IfThenElse setLabel(org.meta_environment.rascal.ast.Label x) { 
		IfThenElse z = new IfThenElse();
 		z.$setLabel(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private void $setConditions(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.conditions = x; }
	public IfThenElse setConditions(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		IfThenElse z = new IfThenElse();
 		z.$setConditions(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Statement thenStatement;
	public org.meta_environment.rascal.ast.Statement getThenStatement() { return thenStatement; }
	private void $setThenStatement(org.meta_environment.rascal.ast.Statement x) { this.thenStatement = x; }
	public IfThenElse setThenStatement(org.meta_environment.rascal.ast.Statement x) { 
		IfThenElse z = new IfThenElse();
 		z.$setThenStatement(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Statement elseStatement;
	public org.meta_environment.rascal.ast.Statement getElseStatement() { return elseStatement; }
	private void $setElseStatement(org.meta_environment.rascal.ast.Statement x) { this.elseStatement = x; }
	public IfThenElse setElseStatement(org.meta_environment.rascal.ast.Statement x) { 
		IfThenElse z = new IfThenElse();
 		z.$setElseStatement(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.NoElseMayFollow getNoElseMayFollow() { throw new UnsupportedOperationException(); } public boolean hasNoElseMayFollow() { return false; }
public boolean isIfThen() { return false; }
static public class IfThen extends Statement {
/** label:Label "if" "(" conditions:{Expression ","}+ ")" thenStatement:Statement noElseMayFollow:NoElseMayFollow -> Statement {cons("IfThen")} */
	private IfThen() {
		super();
	}
	public IfThen(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.Statement thenStatement, org.meta_environment.rascal.ast.NoElseMayFollow noElseMayFollow) {
		this.node = node;
		this.label = label;
		this.conditions = conditions;
		this.thenStatement = thenStatement;
		this.noElseMayFollow = noElseMayFollow;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementIfThen(this);
	}

	public boolean isIfThen() { return true; }

	public boolean hasLabel() { return true; }
	public boolean hasConditions() { return true; }
	public boolean hasThenStatement() { return true; }
	public boolean hasNoElseMayFollow() { return true; }

private org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private void $setLabel(org.meta_environment.rascal.ast.Label x) { this.label = x; }
	public IfThen setLabel(org.meta_environment.rascal.ast.Label x) { 
		IfThen z = new IfThen();
 		z.$setLabel(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private void $setConditions(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.conditions = x; }
	public IfThen setConditions(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		IfThen z = new IfThen();
 		z.$setConditions(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Statement thenStatement;
	public org.meta_environment.rascal.ast.Statement getThenStatement() { return thenStatement; }
	private void $setThenStatement(org.meta_environment.rascal.ast.Statement x) { this.thenStatement = x; }
	public IfThen setThenStatement(org.meta_environment.rascal.ast.Statement x) { 
		IfThen z = new IfThen();
 		z.$setThenStatement(x);
		return z;
	}
	private org.meta_environment.rascal.ast.NoElseMayFollow noElseMayFollow;
	public org.meta_environment.rascal.ast.NoElseMayFollow getNoElseMayFollow() { return noElseMayFollow; }
	private void $setNoElseMayFollow(org.meta_environment.rascal.ast.NoElseMayFollow x) { this.noElseMayFollow = x; }
	public IfThen setNoElseMayFollow(org.meta_environment.rascal.ast.NoElseMayFollow x) { 
		IfThen z = new IfThen();
 		z.$setNoElseMayFollow(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Case> getCases() { throw new UnsupportedOperationException(); } public boolean hasExpression() { return false; } public boolean hasCases() { return false; }
public boolean isSwitch() { return false; }
static public class Switch extends Statement {
/** label:Label "switch" "(" expression:Expression ")" "{" cases:Case+ "}" -> Statement {cons("Switch")} */
	private Switch() {
		super();
	}
	public Switch(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Case> cases) {
		this.node = node;
		this.label = label;
		this.expression = expression;
		this.cases = cases;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementSwitch(this);
	}

	public boolean isSwitch() { return true; }

	public boolean hasLabel() { return true; }
	public boolean hasExpression() { return true; }
	public boolean hasCases() { return true; }

private org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private void $setLabel(org.meta_environment.rascal.ast.Label x) { this.label = x; }
	public Switch setLabel(org.meta_environment.rascal.ast.Label x) { 
		Switch z = new Switch();
 		z.$setLabel(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Switch setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Switch z = new Switch();
 		z.$setExpression(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Case> cases;
	public java.util.List<org.meta_environment.rascal.ast.Case> getCases() { return cases; }
	private void $setCases(java.util.List<org.meta_environment.rascal.ast.Case> x) { this.cases = x; }
	public Switch setCases(java.util.List<org.meta_environment.rascal.ast.Case> x) { 
		Switch z = new Switch();
 		z.$setCases(x);
		return z;
	}	
} 
public boolean isEmptyStatement() { return false; }
static public class EmptyStatement extends Statement {
/** ";" -> Statement {cons("EmptyStatement")} */
	private EmptyStatement() {
		super();
	}
	public EmptyStatement(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementEmptyStatement(this);
	}

	public boolean isEmptyStatement() { return true; }	
} public boolean isExpression() { return false; } static public class Expression extends Statement {
/** expression:Expression ";" -> Statement {cons("Expression")} */
	private Expression() {
		super();
	}
	public Expression(INode node, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementExpression(this);
	}

	public boolean isExpression() { return true; }

	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Expression setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Expression z = new Expression();
 		z.$setExpression(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Visit getVisit() { throw new UnsupportedOperationException(); }
public boolean hasVisit() { return false; }
public boolean isVisit() { return false; }
static public class Visit extends Statement {
/** visit:Visit -> Statement {cons("Visit")} */
	private Visit() {
		super();
	}
	public Visit(INode node, org.meta_environment.rascal.ast.Visit visit) {
		this.node = node;
		this.visit = visit;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementVisit(this);
	}

	public boolean isVisit() { return true; }

	public boolean hasVisit() { return true; }

private org.meta_environment.rascal.ast.Visit visit;
	public org.meta_environment.rascal.ast.Visit getVisit() { return visit; }
	private void $setVisit(org.meta_environment.rascal.ast.Visit x) { this.visit = x; }
	public Visit setVisit(org.meta_environment.rascal.ast.Visit x) { 
		Visit z = new Visit();
 		z.$setVisit(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Assignable getAssignable() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Assignment getOperator() { throw new UnsupportedOperationException(); } public boolean hasAssignable() { return false; }
	public boolean hasOperator() { return false; } public boolean isAssignment() { return false; }
static public class Assignment extends Statement {
/** assignable:Assignable operator:Assignment expression:Expression ";" -> Statement {cons("Assignment")} */
	private Assignment() {
		super();
	}
	public Assignment(INode node, org.meta_environment.rascal.ast.Assignable assignable, org.meta_environment.rascal.ast.Assignment operator, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.assignable = assignable;
		this.operator = operator;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementAssignment(this);
	}

	public boolean isAssignment() { return true; }

	public boolean hasAssignable() { return true; }
	public boolean hasOperator() { return true; }
	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Assignable assignable;
	public org.meta_environment.rascal.ast.Assignable getAssignable() { return assignable; }
	private void $setAssignable(org.meta_environment.rascal.ast.Assignable x) { this.assignable = x; }
	public Assignment setAssignable(org.meta_environment.rascal.ast.Assignable x) { 
		Assignment z = new Assignment();
 		z.$setAssignable(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Assignment operator;
	public org.meta_environment.rascal.ast.Assignment getOperator() { return operator; }
	private void $setOperator(org.meta_environment.rascal.ast.Assignment x) { this.operator = x; }
	public Assignment setOperator(org.meta_environment.rascal.ast.Assignment x) { 
		Assignment z = new Assignment();
 		z.$setOperator(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Assignment setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Assignment z = new Assignment();
 		z.$setExpression(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Break getBrk() { throw new UnsupportedOperationException(); }
public boolean hasBrk() { return false; }
public boolean isBreak() { return false; }
static public class Break extends Statement {
/** brk:Break -> Statement {cons("Break")} */
	private Break() {
		super();
	}
	public Break(INode node, org.meta_environment.rascal.ast.Break brk) {
		this.node = node;
		this.brk = brk;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementBreak(this);
	}

	public boolean isBreak() { return true; }

	public boolean hasBrk() { return true; }

private org.meta_environment.rascal.ast.Break brk;
	public org.meta_environment.rascal.ast.Break getBrk() { return brk; }
	private void $setBrk(org.meta_environment.rascal.ast.Break x) { this.brk = x; }
	public Break setBrk(org.meta_environment.rascal.ast.Break x) { 
		Break z = new Break();
 		z.$setBrk(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Fail getFail() { throw new UnsupportedOperationException(); }
public boolean hasFail() { return false; }
public boolean isFail() { return false; }
static public class Fail extends Statement {
/** fail:Fail -> Statement {cons("Fail")} */
	private Fail() {
		super();
	}
	public Fail(INode node, org.meta_environment.rascal.ast.Fail fail) {
		this.node = node;
		this.fail = fail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementFail(this);
	}

	public boolean isFail() { return true; }

	public boolean hasFail() { return true; }

private org.meta_environment.rascal.ast.Fail fail;
	public org.meta_environment.rascal.ast.Fail getFail() { return fail; }
	private void $setFail(org.meta_environment.rascal.ast.Fail x) { this.fail = x; }
	public Fail setFail(org.meta_environment.rascal.ast.Fail x) { 
		Fail z = new Fail();
 		z.$setFail(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Return getRet() { throw new UnsupportedOperationException(); }
public boolean hasRet() { return false; }
public boolean isReturn() { return false; }
static public class Return extends Statement {
/** ret:Return -> Statement {cons("Return")} */
	private Return() {
		super();
	}
	public Return(INode node, org.meta_environment.rascal.ast.Return ret) {
		this.node = node;
		this.ret = ret;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementReturn(this);
	}

	public boolean isReturn() { return true; }

	public boolean hasRet() { return true; }

private org.meta_environment.rascal.ast.Return ret;
	public org.meta_environment.rascal.ast.Return getRet() { return ret; }
	private void $setRet(org.meta_environment.rascal.ast.Return x) { this.ret = x; }
	public Return setRet(org.meta_environment.rascal.ast.Return x) { 
		Return z = new Return();
 		z.$setRet(x);
		return z;
	}	
} 
public boolean isContinue() { return false; }
static public class Continue extends Statement {
/** "continue" ";" -> Statement {cons("Continue")} */
	private Continue() {
		super();
	}
	public Continue(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementContinue(this);
	}

	public boolean isContinue() { return true; }	
} public boolean isAssert() { return false; }
static public class Assert extends Statement {
/** "assert" expression:Expression ";" -> Statement {cons("Assert")} */
	private Assert() {
		super();
	}
	public Assert(INode node, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementAssert(this);
	}

	public boolean isAssert() { return true; }

	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Assert setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Assert z = new Assert();
 		z.$setExpression(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.StringLiteral getMessage() { throw new UnsupportedOperationException(); } public boolean hasMessage() { return false; }
public boolean isAssertWithMessage() { return false; }
static public class AssertWithMessage extends Statement {
/** "assert" expression:Expression ":" message:StringLiteral ";" -> Statement {cons("AssertWithMessage")} */
	private AssertWithMessage() {
		super();
	}
	public AssertWithMessage(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.StringLiteral message) {
		this.node = node;
		this.expression = expression;
		this.message = message;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementAssertWithMessage(this);
	}

	public boolean isAssertWithMessage() { return true; }

	public boolean hasExpression() { return true; }
	public boolean hasMessage() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public AssertWithMessage setExpression(org.meta_environment.rascal.ast.Expression x) { 
		AssertWithMessage z = new AssertWithMessage();
 		z.$setExpression(x);
		return z;
	}
	private org.meta_environment.rascal.ast.StringLiteral message;
	public org.meta_environment.rascal.ast.StringLiteral getMessage() { return message; }
	private void $setMessage(org.meta_environment.rascal.ast.StringLiteral x) { this.message = x; }
	public AssertWithMessage setMessage(org.meta_environment.rascal.ast.StringLiteral x) { 
		AssertWithMessage z = new AssertWithMessage();
 		z.$setMessage(x);
		return z;
	}	
} public boolean isInsert() { return false; }
static public class Insert extends Statement {
/** "insert" expression:Expression ";" -> Statement {cons("Insert")} */
	private Insert() {
		super();
	}
	public Insert(INode node, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementInsert(this);
	}

	public boolean isInsert() { return true; }

	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Insert setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Insert z = new Insert();
 		z.$setExpression(x);
		return z;
	}	
} public boolean isThrow() { return false; }
static public class Throw extends Statement {
/** "throw" expression:Expression ";" -> Statement {cons("Throw")} */
	private Throw() {
		super();
	}
	public Throw(INode node, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementThrow(this);
	}

	public boolean isThrow() { return true; }

	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Throw setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Throw z = new Throw();
 		z.$setExpression(x);
		return z;
	}	
} public java.util.List<org.meta_environment.rascal.ast.Catch> getHandlers() { throw new UnsupportedOperationException(); } public boolean hasHandlers() { return false; } public boolean isTry() { return false; }
static public class Try extends Statement {
/** "try" body:Statement handlers:Catch+ -> Statement {non-assoc, cons("Try")} */
	private Try() {
		super();
	}
	public Try(INode node, org.meta_environment.rascal.ast.Statement body, java.util.List<org.meta_environment.rascal.ast.Catch> handlers) {
		this.node = node;
		this.body = body;
		this.handlers = handlers;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementTry(this);
	}

	public boolean isTry() { return true; }

	public boolean hasBody() { return true; }
	public boolean hasHandlers() { return true; }

private org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private void $setBody(org.meta_environment.rascal.ast.Statement x) { this.body = x; }
	public Try setBody(org.meta_environment.rascal.ast.Statement x) { 
		Try z = new Try();
 		z.$setBody(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Catch> handlers;
	public java.util.List<org.meta_environment.rascal.ast.Catch> getHandlers() { return handlers; }
	private void $setHandlers(java.util.List<org.meta_environment.rascal.ast.Catch> x) { this.handlers = x; }
	public Try setHandlers(java.util.List<org.meta_environment.rascal.ast.Catch> x) { 
		Try z = new Try();
 		z.$setHandlers(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Statement getFinallyBody() { throw new UnsupportedOperationException(); } public boolean hasFinallyBody() { return false; }
public boolean isTryFinally() { return false; }
static public class TryFinally extends Statement {
/** "try" body:Statement handlers:Catch+ "finally" finallyBody:Statement -> Statement {cons("TryFinally")} */
	private TryFinally() {
		super();
	}
	public TryFinally(INode node, org.meta_environment.rascal.ast.Statement body, java.util.List<org.meta_environment.rascal.ast.Catch> handlers, org.meta_environment.rascal.ast.Statement finallyBody) {
		this.node = node;
		this.body = body;
		this.handlers = handlers;
		this.finallyBody = finallyBody;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementTryFinally(this);
	}

	public boolean isTryFinally() { return true; }

	public boolean hasBody() { return true; }
	public boolean hasHandlers() { return true; }
	public boolean hasFinallyBody() { return true; }

private org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private void $setBody(org.meta_environment.rascal.ast.Statement x) { this.body = x; }
	public TryFinally setBody(org.meta_environment.rascal.ast.Statement x) { 
		TryFinally z = new TryFinally();
 		z.$setBody(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Catch> handlers;
	public java.util.List<org.meta_environment.rascal.ast.Catch> getHandlers() { return handlers; }
	private void $setHandlers(java.util.List<org.meta_environment.rascal.ast.Catch> x) { this.handlers = x; }
	public TryFinally setHandlers(java.util.List<org.meta_environment.rascal.ast.Catch> x) { 
		TryFinally z = new TryFinally();
 		z.$setHandlers(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Statement finallyBody;
	public org.meta_environment.rascal.ast.Statement getFinallyBody() { return finallyBody; }
	private void $setFinallyBody(org.meta_environment.rascal.ast.Statement x) { this.finallyBody = x; }
	public TryFinally setFinallyBody(org.meta_environment.rascal.ast.Statement x) { 
		TryFinally z = new TryFinally();
 		z.$setFinallyBody(x);
		return z;
	}	
} public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() { throw new UnsupportedOperationException(); } public boolean hasStatements() { return false; }
public boolean isBlock() { return false; }
static public class Block extends Statement {
/** label:Label "{" statements:Statement+ "}" -> Statement {cons("Block")} */
	private Block() {
		super();
	}
	public Block(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
		this.node = node;
		this.label = label;
		this.statements = statements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementBlock(this);
	}

	public boolean isBlock() { return true; }

	public boolean hasLabel() { return true; }
	public boolean hasStatements() { return true; }

private org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private void $setLabel(org.meta_environment.rascal.ast.Label x) { this.label = x; }
	public Block setLabel(org.meta_environment.rascal.ast.Label x) { 
		Block z = new Block();
 		z.$setLabel(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Statement> statements;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() { return statements; }
	private void $setStatements(java.util.List<org.meta_environment.rascal.ast.Statement> x) { this.statements = x; }
	public Block setStatements(java.util.List<org.meta_environment.rascal.ast.Statement> x) { 
		Block z = new Block();
 		z.$setStatements(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() { throw new UnsupportedOperationException(); }
public boolean hasFunctionDeclaration() { return false; }
public boolean isFunctionDeclaration() { return false; }
static public class FunctionDeclaration extends Statement {
/** functionDeclaration:FunctionDeclaration -> Statement {cons("FunctionDeclaration")} */
	private FunctionDeclaration() {
		super();
	}
	public FunctionDeclaration(INode node, org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) {
		this.node = node;
		this.functionDeclaration = functionDeclaration;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementFunctionDeclaration(this);
	}

	public boolean isFunctionDeclaration() { return true; }

	public boolean hasFunctionDeclaration() { return true; }

private org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration;
	public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() { return functionDeclaration; }
	private void $setFunctionDeclaration(org.meta_environment.rascal.ast.FunctionDeclaration x) { this.functionDeclaration = x; }
	public FunctionDeclaration setFunctionDeclaration(org.meta_environment.rascal.ast.FunctionDeclaration x) { 
		FunctionDeclaration z = new FunctionDeclaration();
 		z.$setFunctionDeclaration(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.LocalVariableDeclaration getDeclaration() { throw new UnsupportedOperationException(); }
public boolean hasDeclaration() { return false; }
public boolean isVariableDeclaration() { return false; }
static public class VariableDeclaration extends Statement {
/** declaration:LocalVariableDeclaration ";" -> Statement {cons("VariableDeclaration")} */
	private VariableDeclaration() {
		super();
	}
	public VariableDeclaration(INode node, org.meta_environment.rascal.ast.LocalVariableDeclaration declaration) {
		this.node = node;
		this.declaration = declaration;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementVariableDeclaration(this);
	}

	public boolean isVariableDeclaration() { return true; }

	public boolean hasDeclaration() { return true; }

private org.meta_environment.rascal.ast.LocalVariableDeclaration declaration;
	public org.meta_environment.rascal.ast.LocalVariableDeclaration getDeclaration() { return declaration; }
	private void $setDeclaration(org.meta_environment.rascal.ast.LocalVariableDeclaration x) { this.declaration = x; }
	public VariableDeclaration setDeclaration(org.meta_environment.rascal.ast.LocalVariableDeclaration x) { 
		VariableDeclaration z = new VariableDeclaration();
 		z.$setDeclaration(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.Type getType() { throw new UnsupportedOperationException(); }
	public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getNames() { throw new UnsupportedOperationException(); }
public boolean hasType() { return false; }
	public boolean hasNames() { return false; }
public boolean isGlobalDirective() { return false; }
static public class GlobalDirective extends Statement {
/** "global" type:Type names:{QualifiedName ","}+ ";" -> Statement {cons("GlobalDirective")} */
	private GlobalDirective() {
		super();
	}
	public GlobalDirective(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.QualifiedName> names) {
		this.node = node;
		this.type = type;
		this.names = names;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementGlobalDirective(this);
	}

	public boolean isGlobalDirective() { return true; }

	public boolean hasType() { return true; }
	public boolean hasNames() { return true; }

private org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }
	private void $setType(org.meta_environment.rascal.ast.Type x) { this.type = x; }
	public GlobalDirective setType(org.meta_environment.rascal.ast.Type x) { 
		GlobalDirective z = new GlobalDirective();
 		z.$setType(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.QualifiedName> names;
	public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getNames() { return names; }
	private void $setNames(java.util.List<org.meta_environment.rascal.ast.QualifiedName> x) { this.names = x; }
	public GlobalDirective setNames(java.util.List<org.meta_environment.rascal.ast.QualifiedName> x) { 
		GlobalDirective z = new GlobalDirective();
 		z.$setNames(x);
		return z;
	}	
}
}