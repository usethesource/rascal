package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Statement extends AbstractAST { 
  public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getVariables() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Bound getBound() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Statement getBody() { throw new UnsupportedOperationException(); } public boolean hasVariables() { return false; }
	public boolean hasBound() { return false; } public boolean hasBody() { return false; } public boolean isSolve() { return false; }
static public class Solve extends Statement {
/** "solve" "(" variables:{QualifiedName ","}+ bound:Bound ")" body:Statement -> Statement {cons("Solve")} */
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

private final java.util.List<org.meta_environment.rascal.ast.QualifiedName> variables;
	public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getVariables() { return variables; }
	private final org.meta_environment.rascal.ast.Bound bound;
	public org.meta_environment.rascal.ast.Bound getBound() { return bound; }
	private final org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }	
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

private final org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private final java.util.List<org.meta_environment.rascal.ast.Expression> generators;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getGenerators() { return generators; }
	private final org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { throw new UnsupportedOperationException(); } public boolean hasConditions() { return false; } public boolean isWhile() { return false; }
static public class While extends Statement {
/** label:Label "while" "(" conditions:{Expression ","}+ ")" body:Statement -> Statement {cons("While")} */
	public While(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.Statement body) {
		this.node = node;
		this.label = label;
		this.conditions = conditions;
		this.body = body;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementWhile(this);
	}

	public boolean isWhile() { return true; }

	public boolean hasLabel() { return true; }
	public boolean hasConditions() { return true; }
	public boolean hasBody() { return true; }

private final org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private final java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private final org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }	
} public org.meta_environment.rascal.ast.Expression getCondition() { throw new UnsupportedOperationException(); } public boolean hasCondition() { return false; }
public boolean isDoWhile() { return false; }
static public class DoWhile extends Statement {
/** label:Label "do" body:Statement "while" "(" condition:Expression ")" ";" -> Statement {cons("DoWhile")} */
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

private final org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private final org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private final org.meta_environment.rascal.ast.Expression condition;
	public org.meta_environment.rascal.ast.Expression getCondition() { return condition; }	
} public org.meta_environment.rascal.ast.Statement getThenStatement() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Statement getElseStatement() { throw new UnsupportedOperationException(); } public boolean hasThenStatement() { return false; } public boolean hasElseStatement() { return false; }
public boolean isIfThenElse() { return false; }
static public class IfThenElse extends Statement {
/** label:Label "if" "(" conditions:{Expression ","}+ ")" thenStatement:Statement "else" elseStatement:Statement -> Statement {cons("IfThenElse")} */
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

private final org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private final java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private final org.meta_environment.rascal.ast.Statement thenStatement;
	public org.meta_environment.rascal.ast.Statement getThenStatement() { return thenStatement; }
	private final org.meta_environment.rascal.ast.Statement elseStatement;
	public org.meta_environment.rascal.ast.Statement getElseStatement() { return elseStatement; }	
} public org.meta_environment.rascal.ast.NoElseMayFollow getNoElseMayFollow() { throw new UnsupportedOperationException(); } public boolean hasNoElseMayFollow() { return false; }
public boolean isIfThen() { return false; }
static public class IfThen extends Statement {
/** label:Label "if" "(" conditions:{Expression ","}+ ")" thenStatement:Statement noElseMayFollow:NoElseMayFollow -> Statement {cons("IfThen")} */
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

private final org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private final java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private final org.meta_environment.rascal.ast.Statement thenStatement;
	public org.meta_environment.rascal.ast.Statement getThenStatement() { return thenStatement; }
	private final org.meta_environment.rascal.ast.NoElseMayFollow noElseMayFollow;
	public org.meta_environment.rascal.ast.NoElseMayFollow getNoElseMayFollow() { return noElseMayFollow; }	
} public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Case> getCases() { throw new UnsupportedOperationException(); } public boolean hasExpression() { return false; } public boolean hasCases() { return false; }
public boolean isSwitch() { return false; }
static public class Switch extends Statement {
/** label:Label "switch" "(" expression:Expression ")" "{" cases:Case+ "}" -> Statement {cons("Switch")} */
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

private final org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private final org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private final java.util.List<org.meta_environment.rascal.ast.Case> cases;
	public java.util.List<org.meta_environment.rascal.ast.Case> getCases() { return cases; }	
} public org.meta_environment.rascal.ast.Visit getVisit() { throw new UnsupportedOperationException(); } public boolean hasVisit() { return false; }
public boolean isVisit() { return false; }
static public class Visit extends Statement {
/** label:Label visit:Visit -> Statement {cons("Visit")} */
	public Visit(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Visit visit) {
		this.node = node;
		this.label = label;
		this.visit = visit;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementVisit(this);
	}

	public boolean isVisit() { return true; }

	public boolean hasLabel() { return true; }
	public boolean hasVisit() { return true; }

private final org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private final org.meta_environment.rascal.ast.Visit visit;
	public org.meta_environment.rascal.ast.Visit getVisit() { return visit; }	
} 
public boolean isEmptyStatement() { return false; }
static public class EmptyStatement extends Statement {
/** ";" -> Statement {cons("EmptyStatement")} */
	public EmptyStatement(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementEmptyStatement(this);
	}

	public boolean isEmptyStatement() { return true; }	
} public boolean isExpression() { return false; } static public class Expression extends Statement {
/** expression:Expression ";" -> Statement {cons("Expression")} */
	public Expression(INode node, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementExpression(this);
	}

	public boolean isExpression() { return true; }

	public boolean hasExpression() { return true; }

private final org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }	
} public org.meta_environment.rascal.ast.Assignable getAssignable() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Assignment getOperator() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Statement getStatement() { throw new UnsupportedOperationException(); } public boolean hasAssignable() { return false; } public boolean hasOperator() { return false; } public boolean hasStatement() { return false; } public boolean isAssignment() { return false; } static public class Assignment extends Statement {
/** assignable:Assignable operator:Assignment statement:Statement -> Statement {cons("Assignment")} */
	public Assignment(INode node, org.meta_environment.rascal.ast.Assignable assignable, org.meta_environment.rascal.ast.Assignment operator, org.meta_environment.rascal.ast.Statement statement) {
		this.node = node;
		this.assignable = assignable;
		this.operator = operator;
		this.statement = statement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementAssignment(this);
	}

	public boolean isAssignment() { return true; }

	public boolean hasAssignable() { return true; }
	public boolean hasOperator() { return true; }
	public boolean hasStatement() { return true; }

private final org.meta_environment.rascal.ast.Assignable assignable;
	public org.meta_environment.rascal.ast.Assignable getAssignable() { return assignable; }
	private final org.meta_environment.rascal.ast.Assignment operator;
	public org.meta_environment.rascal.ast.Assignment getOperator() { return operator; }
	private final org.meta_environment.rascal.ast.Statement statement;
	public org.meta_environment.rascal.ast.Statement getStatement() { return statement; }	
} public boolean isAssert() { return false; }
static public class Assert extends Statement {
/** "assert" expression:Expression ";" -> Statement {cons("Assert")} */
	public Assert(INode node, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementAssert(this);
	}

	public boolean isAssert() { return true; }

	public boolean hasExpression() { return true; }

private final org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }	
} public org.meta_environment.rascal.ast.Expression getMessage() { throw new UnsupportedOperationException(); } public boolean hasMessage() { return false; }
public boolean isAssertWithMessage() { return false; }
static public class AssertWithMessage extends Statement {
/** "assert" expression:Expression ":" message:Expression ";" -> Statement {cons("AssertWithMessage")} */
	public AssertWithMessage(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Expression message) {
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

private final org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private final org.meta_environment.rascal.ast.Expression message;
	public org.meta_environment.rascal.ast.Expression getMessage() { return message; }	
} public boolean isReturn() { return false; } static public class Return extends Statement {
/** "return" statement:Statement -> Statement {cons("Return")} */
	public Return(INode node, org.meta_environment.rascal.ast.Statement statement) {
		this.node = node;
		this.statement = statement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementReturn(this);
	}

	public boolean isReturn() { return true; }

	public boolean hasStatement() { return true; }

private final org.meta_environment.rascal.ast.Statement statement;
	public org.meta_environment.rascal.ast.Statement getStatement() { return statement; }	
} public boolean isThrow() { return false; } static public class Throw extends Statement {
/** "throw" statement:Statement -> Statement {cons("Throw")} */
	public Throw(INode node, org.meta_environment.rascal.ast.Statement statement) {
		this.node = node;
		this.statement = statement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementThrow(this);
	}

	public boolean isThrow() { return true; }

	public boolean hasStatement() { return true; }

private final org.meta_environment.rascal.ast.Statement statement;
	public org.meta_environment.rascal.ast.Statement getStatement() { return statement; }	
} public org.meta_environment.rascal.ast.DataTarget getDataTarget() { throw new UnsupportedOperationException(); } public boolean hasDataTarget() { return false; } public boolean isInsert() { return false; } static public class Insert extends Statement {
/** "insert" dataTarget:DataTarget statement:Statement -> Statement {non-assoc, cons("Insert")} */
	public Insert(INode node, org.meta_environment.rascal.ast.DataTarget dataTarget, org.meta_environment.rascal.ast.Statement statement) {
		this.node = node;
		this.dataTarget = dataTarget;
		this.statement = statement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementInsert(this);
	}

	public boolean isInsert() { return true; }

	public boolean hasDataTarget() { return true; }
	public boolean hasStatement() { return true; }

private final org.meta_environment.rascal.ast.DataTarget dataTarget;
	public org.meta_environment.rascal.ast.DataTarget getDataTarget() { return dataTarget; }
	private final org.meta_environment.rascal.ast.Statement statement;
	public org.meta_environment.rascal.ast.Statement getStatement() { return statement; }	
} public boolean isAppend() { return false; } static public class Append extends Statement {
/** "append" dataTarget:DataTarget statement:Statement -> Statement {non-assoc, cons("Append")} */
	public Append(INode node, org.meta_environment.rascal.ast.DataTarget dataTarget, org.meta_environment.rascal.ast.Statement statement) {
		this.node = node;
		this.dataTarget = dataTarget;
		this.statement = statement;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementAppend(this);
	}

	public boolean isAppend() { return true; }

	public boolean hasDataTarget() { return true; }
	public boolean hasStatement() { return true; }

private final org.meta_environment.rascal.ast.DataTarget dataTarget;
	public org.meta_environment.rascal.ast.DataTarget getDataTarget() { return dataTarget; }
	private final org.meta_environment.rascal.ast.Statement statement;
	public org.meta_environment.rascal.ast.Statement getStatement() { return statement; }	
} public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() { throw new UnsupportedOperationException(); } public boolean hasFunctionDeclaration() { return false; } public boolean isFunctionDeclaration() { return false; } static public class FunctionDeclaration extends Statement {
/** functionDeclaration:FunctionDeclaration -> Statement {cons("FunctionDeclaration")} */
	public FunctionDeclaration(INode node, org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) {
		this.node = node;
		this.functionDeclaration = functionDeclaration;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementFunctionDeclaration(this);
	}

	public boolean isFunctionDeclaration() { return true; }

	public boolean hasFunctionDeclaration() { return true; }

private final org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration;
	public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() { return functionDeclaration; }	
} public org.meta_environment.rascal.ast.LocalVariableDeclaration getDeclaration() { throw new UnsupportedOperationException(); } public boolean hasDeclaration() { return false; } public boolean isVariableDeclaration() { return false; } static public class VariableDeclaration extends Statement {
/** declaration:LocalVariableDeclaration ";" -> Statement {cons("VariableDeclaration")} */
	public VariableDeclaration(INode node, org.meta_environment.rascal.ast.LocalVariableDeclaration declaration) {
		this.node = node;
		this.declaration = declaration;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementVariableDeclaration(this);
	}

	public boolean isVariableDeclaration() { return true; }

	public boolean hasDeclaration() { return true; }

private final org.meta_environment.rascal.ast.LocalVariableDeclaration declaration;
	public org.meta_environment.rascal.ast.LocalVariableDeclaration getDeclaration() { return declaration; }	
} public org.meta_environment.rascal.ast.Target getTarget() { throw new UnsupportedOperationException(); } public boolean hasTarget() { return false; } public boolean isBreak() { return false; }
static public class Break extends Statement {
/** "break" target:Target ";" -> Statement {cons("Break")} */
	public Break(INode node, org.meta_environment.rascal.ast.Target target) {
		this.node = node;
		this.target = target;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementBreak(this);
	}

	public boolean isBreak() { return true; }

	public boolean hasTarget() { return true; }

private final org.meta_environment.rascal.ast.Target target;
	public org.meta_environment.rascal.ast.Target getTarget() { return target; }	
} public boolean isFail() { return false; }
static public class Fail extends Statement {
/** "fail" target:Target  ";" -> Statement {cons("Fail")} */
	public Fail(INode node, org.meta_environment.rascal.ast.Target target) {
		this.node = node;
		this.target = target;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementFail(this);
	}

	public boolean isFail() { return true; }

	public boolean hasTarget() { return true; }

private final org.meta_environment.rascal.ast.Target target;
	public org.meta_environment.rascal.ast.Target getTarget() { return target; }	
} public boolean isContinue() { return false; }
static public class Continue extends Statement {
/** "continue" target:Target ";" -> Statement {cons("Continue")} */
	public Continue(INode node, org.meta_environment.rascal.ast.Target target) {
		this.node = node;
		this.target = target;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementContinue(this);
	}

	public boolean isContinue() { return true; }

	public boolean hasTarget() { return true; }

private final org.meta_environment.rascal.ast.Target target;
	public org.meta_environment.rascal.ast.Target getTarget() { return target; }	
} public java.util.List<org.meta_environment.rascal.ast.Catch> getHandlers() { throw new UnsupportedOperationException(); } public boolean hasHandlers() { return false; } public boolean isTry() { return false; }
static public class Try extends Statement {
/** "try" body:Statement handlers:Catch+ -> Statement {non-assoc, cons("Try")} */
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

private final org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private final java.util.List<org.meta_environment.rascal.ast.Catch> handlers;
	public java.util.List<org.meta_environment.rascal.ast.Catch> getHandlers() { return handlers; }	
} public org.meta_environment.rascal.ast.Statement getFinallyBody() { throw new UnsupportedOperationException(); } public boolean hasFinallyBody() { return false; }
public boolean isTryFinally() { return false; }
static public class TryFinally extends Statement {
/** "try" body:Statement handlers:Catch+ "finally" finallyBody:Statement -> Statement {cons("TryFinally")} */
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

private final org.meta_environment.rascal.ast.Statement body;
	public org.meta_environment.rascal.ast.Statement getBody() { return body; }
	private final java.util.List<org.meta_environment.rascal.ast.Catch> handlers;
	public java.util.List<org.meta_environment.rascal.ast.Catch> getHandlers() { return handlers; }
	private final org.meta_environment.rascal.ast.Statement finallyBody;
	public org.meta_environment.rascal.ast.Statement getFinallyBody() { return finallyBody; }	
} public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() { throw new UnsupportedOperationException(); } public boolean hasStatements() { return false; }
public boolean isNonEmptyBlock() { return false; }
static public class NonEmptyBlock extends Statement {
/** label:Label "{" statements:Statement+ "}" -> Statement {cons("NonEmptyBlock")} */
	public NonEmptyBlock(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
		this.node = node;
		this.label = label;
		this.statements = statements;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStatementNonEmptyBlock(this);
	}

	public boolean isNonEmptyBlock() { return true; }

	public boolean hasLabel() { return true; }
	public boolean hasStatements() { return true; }

private final org.meta_environment.rascal.ast.Label label;
	public org.meta_environment.rascal.ast.Label getLabel() { return label; }
	private final java.util.List<org.meta_environment.rascal.ast.Statement> statements;
	public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() { return statements; }	
} 
public org.meta_environment.rascal.ast.Type getType() { throw new UnsupportedOperationException(); }
	public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getNames() { throw new UnsupportedOperationException(); }
public boolean hasType() { return false; }
	public boolean hasNames() { return false; }
public boolean isGlobalDirective() { return false; }
static public class GlobalDirective extends Statement {
/** "global" type:Type names:{QualifiedName ","}+ ";" -> Statement {cons("GlobalDirective")} */
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

private final org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }
	private final java.util.List<org.meta_environment.rascal.ast.QualifiedName> names;
	public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getNames() { return names; }	
}
}