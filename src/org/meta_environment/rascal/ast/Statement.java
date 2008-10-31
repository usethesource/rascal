package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Statement extends AbstractAST {
	static public class All extends Statement {
		private org.meta_environment.rascal.ast.Statement body;
		private java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
		private org.meta_environment.rascal.ast.Label label;

		/*
		 * label:Label "all" "(" conditions:{Expression ","}+ ")" body:Statement
		 * -> Statement {cons("All")}
		 */
		private All() {
		}

		/* package */All(
				ITree tree,
				org.meta_environment.rascal.ast.Label label,
				java.util.List<org.meta_environment.rascal.ast.Expression> conditions,
				org.meta_environment.rascal.ast.Statement body) {
			this.tree = tree;
			this.label = label;
			this.conditions = conditions;
			this.body = body;
		}

		private void $setBody(org.meta_environment.rascal.ast.Statement x) {
			this.body = x;
		}

		private void $setConditions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.conditions = x;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Label x) {
			this.label = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementAll(this);
		}

		public org.meta_environment.rascal.ast.Statement getBody() {
			return body;
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() {
			return conditions;
		}

		public org.meta_environment.rascal.ast.Label getLabel() {
			return label;
		}

		public All setBody(org.meta_environment.rascal.ast.Statement x) {
			All z = new All();
			z.$setBody(x);
			return z;
		}

		public All setConditions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			All z = new All();
			z.$setConditions(x);
			return z;
		}

		public All setLabel(org.meta_environment.rascal.ast.Label x) {
			All z = new All();
			z.$setLabel(x);
			return z;
		}
	}

	static public class Ambiguity extends Statement {
		private final java.util.List<org.meta_environment.rascal.ast.Statement> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Statement> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Statement> getAlternatives() {
			return alternatives;
		}
	}

	static public class Assert extends Statement {
		private org.meta_environment.rascal.ast.Expression expression;
		private org.meta_environment.rascal.ast.StringLiteral label;

		/*
		 * "assert" label:StringLiteral ":" expression:Expression ";" ->
		 * Statement {cons("Assert")}
		 */
		private Assert() {
		}

		/* package */Assert(ITree tree,
				org.meta_environment.rascal.ast.StringLiteral label,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.label = label;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setLabel(org.meta_environment.rascal.ast.StringLiteral x) {
			this.label = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementAssert(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.StringLiteral getLabel() {
			return label;
		}

		public Assert setExpression(org.meta_environment.rascal.ast.Expression x) {
			Assert z = new Assert();
			z.$setExpression(x);
			return z;
		}

		public Assert setLabel(org.meta_environment.rascal.ast.StringLiteral x) {
			Assert z = new Assert();
			z.$setLabel(x);
			return z;
		}
	}

	static public class Assignment extends Statement {
		private java.util.List<org.meta_environment.rascal.ast.Assignable> assignables;
		private java.util.List<org.meta_environment.rascal.ast.Expression> expressions;
		private org.meta_environment.rascal.ast.Assignment operator;

		/*
		 * assignables:{Assignable ","}+ operator:Assignment
		 * expressions:{Expression ","}+ ";" -> Statement {cons("Assignment")}
		 */
		private Assignment() {
		}

		/* package */Assignment(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Assignable> assignables,
				org.meta_environment.rascal.ast.Assignment operator,
				java.util.List<org.meta_environment.rascal.ast.Expression> expressions) {
			this.tree = tree;
			this.assignables = assignables;
			this.operator = operator;
			this.expressions = expressions;
		}

		private void $setAssignables(
				java.util.List<org.meta_environment.rascal.ast.Assignable> x) {
			this.assignables = x;
		}

		private void $setExpressions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.expressions = x;
		}

		private void $setOperator(org.meta_environment.rascal.ast.Assignment x) {
			this.operator = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementAssignment(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Assignable> getAssignables() {
			return assignables;
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getExpressions() {
			return expressions;
		}

		public org.meta_environment.rascal.ast.Assignment getOperator() {
			return operator;
		}

		public Assignment setAssignables(
				java.util.List<org.meta_environment.rascal.ast.Assignable> x) {
			Assignment z = new Assignment();
			z.$setAssignables(x);
			return z;
		}

		public Assignment setExpressions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			Assignment z = new Assignment();
			z.$setExpressions(x);
			return z;
		}

		public Assignment setOperator(
				org.meta_environment.rascal.ast.Assignment x) {
			Assignment z = new Assignment();
			z.$setOperator(x);
			return z;
		}
	}

	static public class Block extends Statement {
		private org.meta_environment.rascal.ast.Label label;
		private java.util.List<org.meta_environment.rascal.ast.Statement> statements;

		/* label:Label "{" statements:Statement "}" -> Statement {cons("Block")} */
		private Block() {
		}

		/* package */Block(
				ITree tree,
				org.meta_environment.rascal.ast.Label label,
				java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
			this.tree = tree;
			this.label = label;
			this.statements = statements;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Label x) {
			this.label = x;
		}

		private void $setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			this.statements = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementBlock(this);
		}

		public org.meta_environment.rascal.ast.Label getLabel() {
			return label;
		}

		public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() {
			return statements;
		}

		public Block setLabel(org.meta_environment.rascal.ast.Label x) {
			Block z = new Block();
			z.$setLabel(x);
			return z;
		}

		public Block setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			Block z = new Block();
			z.$setStatements(x);
			return z;
		}
	}

	static public class Break extends Statement {
		/* package */Break(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementBreak(this);
		}
	}

	static public class Continue extends Statement {
		/* package */Continue(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementContinue(this);
		}
	}

	static public class DoWhile extends Statement {
		private org.meta_environment.rascal.ast.Statement body;
		private org.meta_environment.rascal.ast.Expression condition;
		private org.meta_environment.rascal.ast.Label label;

		/*
		 * label:Label "do" body:Statement "while" "(" condition:Expression ")"
		 * ";" -> Statement {cons("DoWhile")}
		 */
		private DoWhile() {
		}

		/* package */DoWhile(ITree tree,
				org.meta_environment.rascal.ast.Label label,
				org.meta_environment.rascal.ast.Statement body,
				org.meta_environment.rascal.ast.Expression condition) {
			this.tree = tree;
			this.label = label;
			this.body = body;
			this.condition = condition;
		}

		private void $setBody(org.meta_environment.rascal.ast.Statement x) {
			this.body = x;
		}

		private void $setCondition(org.meta_environment.rascal.ast.Expression x) {
			this.condition = x;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Label x) {
			this.label = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementDoWhile(this);
		}

		public org.meta_environment.rascal.ast.Statement getBody() {
			return body;
		}

		public org.meta_environment.rascal.ast.Expression getCondition() {
			return condition;
		}

		public org.meta_environment.rascal.ast.Label getLabel() {
			return label;
		}

		public DoWhile setBody(org.meta_environment.rascal.ast.Statement x) {
			DoWhile z = new DoWhile();
			z.$setBody(x);
			return z;
		}

		public DoWhile setCondition(org.meta_environment.rascal.ast.Expression x) {
			DoWhile z = new DoWhile();
			z.$setCondition(x);
			return z;
		}

		public DoWhile setLabel(org.meta_environment.rascal.ast.Label x) {
			DoWhile z = new DoWhile();
			z.$setLabel(x);
			return z;
		}
	}

	static public class Expression extends Statement {
		private org.meta_environment.rascal.ast.Expression expression;

		/* expression:Expression ";" -> Statement {cons("Expression")} */
		private Expression() {
		}

		/* package */Expression(ITree tree,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementExpression(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public Expression setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			Expression z = new Expression();
			z.$setExpression(x);
			return z;
		}
	}

	static public class Fail extends Statement {
		/* package */Fail(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementFail(this);
		}
	}

	static public class First extends Statement {
		private org.meta_environment.rascal.ast.Statement body;
		private java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
		private org.meta_environment.rascal.ast.Label label;

		/*
		 * label:Label "first" "(" conditions:{Expression ","}+ ")"
		 * body:Statement -> Statement {cons("First")}
		 */
		private First() {
		}

		/* package */First(
				ITree tree,
				org.meta_environment.rascal.ast.Label label,
				java.util.List<org.meta_environment.rascal.ast.Expression> conditions,
				org.meta_environment.rascal.ast.Statement body) {
			this.tree = tree;
			this.label = label;
			this.conditions = conditions;
			this.body = body;
		}

		private void $setBody(org.meta_environment.rascal.ast.Statement x) {
			this.body = x;
		}

		private void $setConditions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.conditions = x;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Label x) {
			this.label = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementFirst(this);
		}

		public org.meta_environment.rascal.ast.Statement getBody() {
			return body;
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() {
			return conditions;
		}

		public org.meta_environment.rascal.ast.Label getLabel() {
			return label;
		}

		public First setBody(org.meta_environment.rascal.ast.Statement x) {
			First z = new First();
			z.$setBody(x);
			return z;
		}

		public First setConditions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			First z = new First();
			z.$setConditions(x);
			return z;
		}

		public First setLabel(org.meta_environment.rascal.ast.Label x) {
			First z = new First();
			z.$setLabel(x);
			return z;
		}
	}

	static public class For extends Statement {
		private org.meta_environment.rascal.ast.Statement body;
		private java.util.List<org.meta_environment.rascal.ast.Generator> generators;
		private org.meta_environment.rascal.ast.Label label;

		/*
		 * label:Label "for" "(" generators:{Generator ","}+ ")" body:Statement
		 * -> Statement {cons("For")}
		 */
		private For() {
		}

		/* package */For(
				ITree tree,
				org.meta_environment.rascal.ast.Label label,
				java.util.List<org.meta_environment.rascal.ast.Generator> generators,
				org.meta_environment.rascal.ast.Statement body) {
			this.tree = tree;
			this.label = label;
			this.generators = generators;
			this.body = body;
		}

		private void $setBody(org.meta_environment.rascal.ast.Statement x) {
			this.body = x;
		}

		private void $setGenerators(
				java.util.List<org.meta_environment.rascal.ast.Generator> x) {
			this.generators = x;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Label x) {
			this.label = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementFor(this);
		}

		public org.meta_environment.rascal.ast.Statement getBody() {
			return body;
		}

		public java.util.List<org.meta_environment.rascal.ast.Generator> getGenerators() {
			return generators;
		}

		public org.meta_environment.rascal.ast.Label getLabel() {
			return label;
		}

		public For setBody(org.meta_environment.rascal.ast.Statement x) {
			For z = new For();
			z.$setBody(x);
			return z;
		}

		public For setGenerators(
				java.util.List<org.meta_environment.rascal.ast.Generator> x) {
			For z = new For();
			z.$setGenerators(x);
			return z;
		}

		public For setLabel(org.meta_environment.rascal.ast.Label x) {
			For z = new For();
			z.$setLabel(x);
			return z;
		}
	}

	static public class FunctionDeclaration extends Statement {
		private org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration;

		/*
		 * functionDeclaration:FunctionDeclaration -> Statement
		 * {cons("FunctionDeclaration")}
		 */
		private FunctionDeclaration() {
		}

		/* package */FunctionDeclaration(
				ITree tree,
				org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) {
			this.tree = tree;
			this.functionDeclaration = functionDeclaration;
		}

		private void $setFunctionDeclaration(
				org.meta_environment.rascal.ast.FunctionDeclaration x) {
			this.functionDeclaration = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementFunctionDeclaration(this);
		}

		public org.meta_environment.rascal.ast.FunctionDeclaration getFunctionDeclaration() {
			return functionDeclaration;
		}

		public FunctionDeclaration setFunctionDeclaration(
				org.meta_environment.rascal.ast.FunctionDeclaration x) {
			FunctionDeclaration z = new FunctionDeclaration();
			z.$setFunctionDeclaration(x);
			return z;
		}
	}

	static public class GlobalDirective extends Statement {
		private java.util.List<org.meta_environment.rascal.ast.QualifiedName> names;
		private org.meta_environment.rascal.ast.Type type;

		/*
		 * "global" type:Type names:{QualifiedName ","}+ ";" -> Statement
		 * {cons("GlobalDirective")}
		 */
		private GlobalDirective() {
		}

		/* package */GlobalDirective(
				ITree tree,
				org.meta_environment.rascal.ast.Type type,
				java.util.List<org.meta_environment.rascal.ast.QualifiedName> names) {
			this.tree = tree;
			this.type = type;
			this.names = names;
		}

		private void $setNames(
				java.util.List<org.meta_environment.rascal.ast.QualifiedName> x) {
			this.names = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementGlobalDirective(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getNames() {
			return names;
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public GlobalDirective setNames(
				java.util.List<org.meta_environment.rascal.ast.QualifiedName> x) {
			GlobalDirective z = new GlobalDirective();
			z.$setNames(x);
			return z;
		}

		public GlobalDirective setType(org.meta_environment.rascal.ast.Type x) {
			GlobalDirective z = new GlobalDirective();
			z.$setType(x);
			return z;
		}
	}

	static public class IfThen extends Statement {
		private java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
		private org.meta_environment.rascal.ast.Label label;
		private org.meta_environment.rascal.ast.Statement thenStatement;

		/*
		 * label:Label "if" "(" conditions:{Expression ","}+ ")"
		 * thenStatement:Statement NoElseMayFollow -> Statement {cons("IfThen")}
		 */
		private IfThen() {
		}

		/* package */IfThen(
				ITree tree,
				org.meta_environment.rascal.ast.Label label,
				java.util.List<org.meta_environment.rascal.ast.Expression> conditions,
				org.meta_environment.rascal.ast.Statement thenStatement) {
			this.tree = tree;
			this.label = label;
			this.conditions = conditions;
			this.thenStatement = thenStatement;
		}

		private void $setConditions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.conditions = x;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Label x) {
			this.label = x;
		}

		private void $setThenStatement(
				org.meta_environment.rascal.ast.Statement x) {
			this.thenStatement = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementIfThen(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() {
			return conditions;
		}

		public org.meta_environment.rascal.ast.Label getLabel() {
			return label;
		}

		public org.meta_environment.rascal.ast.Statement getThenStatement() {
			return thenStatement;
		}

		public IfThen setConditions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			IfThen z = new IfThen();
			z.$setConditions(x);
			return z;
		}

		public IfThen setLabel(org.meta_environment.rascal.ast.Label x) {
			IfThen z = new IfThen();
			z.$setLabel(x);
			return z;
		}

		public IfThen setThenStatement(
				org.meta_environment.rascal.ast.Statement x) {
			IfThen z = new IfThen();
			z.$setThenStatement(x);
			return z;
		}
	}

	static public class IfThenElse extends Statement {
		private java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
		private org.meta_environment.rascal.ast.Statement elseStatement;
		private org.meta_environment.rascal.ast.Label label;
		private org.meta_environment.rascal.ast.Statement thenStatement;

		/*
		 * label:Label "if" "(" conditions:{Expression ","}+ ")"
		 * thenStatement:Statement "else" elseStatement:Statement -> Statement
		 * {cons("IfThenElse")}
		 */
		private IfThenElse() {
		}

		/* package */IfThenElse(
				ITree tree,
				org.meta_environment.rascal.ast.Label label,
				java.util.List<org.meta_environment.rascal.ast.Expression> conditions,
				org.meta_environment.rascal.ast.Statement thenStatement,
				org.meta_environment.rascal.ast.Statement elseStatement) {
			this.tree = tree;
			this.label = label;
			this.conditions = conditions;
			this.thenStatement = thenStatement;
			this.elseStatement = elseStatement;
		}

		private void $setConditions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.conditions = x;
		}

		private void $setElseStatement(
				org.meta_environment.rascal.ast.Statement x) {
			this.elseStatement = x;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Label x) {
			this.label = x;
		}

		private void $setThenStatement(
				org.meta_environment.rascal.ast.Statement x) {
			this.thenStatement = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementIfThenElse(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() {
			return conditions;
		}

		public org.meta_environment.rascal.ast.Statement getElseStatement() {
			return elseStatement;
		}

		public org.meta_environment.rascal.ast.Label getLabel() {
			return label;
		}

		public org.meta_environment.rascal.ast.Statement getThenStatement() {
			return thenStatement;
		}

		public IfThenElse setConditions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			IfThenElse z = new IfThenElse();
			z.$setConditions(x);
			return z;
		}

		public IfThenElse setElseStatement(
				org.meta_environment.rascal.ast.Statement x) {
			IfThenElse z = new IfThenElse();
			z.$setElseStatement(x);
			return z;
		}

		public IfThenElse setLabel(org.meta_environment.rascal.ast.Label x) {
			IfThenElse z = new IfThenElse();
			z.$setLabel(x);
			return z;
		}

		public IfThenElse setThenStatement(
				org.meta_environment.rascal.ast.Statement x) {
			IfThenElse z = new IfThenElse();
			z.$setThenStatement(x);
			return z;
		}
	}

	static public class Insert extends Statement {
		private org.meta_environment.rascal.ast.Expression expression;

		/* "insert" expression:Expression ";" -> Statement {cons("Insert")} */
		private Insert() {
		}

		/* package */Insert(ITree tree,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementInsert(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public Insert setExpression(org.meta_environment.rascal.ast.Expression x) {
			Insert z = new Insert();
			z.$setExpression(x);
			return z;
		}
	}

	static public class Return extends Statement {
		/* package */Return(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementReturn(this);
		}
	}

	static public class Solve extends Statement {
		private org.meta_environment.rascal.ast.Statement body;
		private java.util.List<org.meta_environment.rascal.ast.Declarator> declarations;

		/*
		 * "with" declarations:{Declarator ";"}+ ";" "solve" body:Statement ->
		 * Statement {cons("Solve")}
		 */
		private Solve() {
		}

		/* package */Solve(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Declarator> declarations,
				org.meta_environment.rascal.ast.Statement body) {
			this.tree = tree;
			this.declarations = declarations;
			this.body = body;
		}

		private void $setBody(org.meta_environment.rascal.ast.Statement x) {
			this.body = x;
		}

		private void $setDeclarations(
				java.util.List<org.meta_environment.rascal.ast.Declarator> x) {
			this.declarations = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementSolve(this);
		}

		public org.meta_environment.rascal.ast.Statement getBody() {
			return body;
		}

		public java.util.List<org.meta_environment.rascal.ast.Declarator> getDeclarations() {
			return declarations;
		}

		public Solve setBody(org.meta_environment.rascal.ast.Statement x) {
			Solve z = new Solve();
			z.$setBody(x);
			return z;
		}

		public Solve setDeclarations(
				java.util.List<org.meta_environment.rascal.ast.Declarator> x) {
			Solve z = new Solve();
			z.$setDeclarations(x);
			return z;
		}
	}

	static public class Switch extends Statement {
		private java.util.List<org.meta_environment.rascal.ast.Case> cases;
		private org.meta_environment.rascal.ast.Expression expression;
		private org.meta_environment.rascal.ast.Label label;

		/*
		 * label:Label "switch" "(" expression:Expression ")" "{" cases:Case+
		 * "}" -> Statement {cons("Switch")}
		 */
		private Switch() {
		}

		/* package */Switch(ITree tree,
				org.meta_environment.rascal.ast.Label label,
				org.meta_environment.rascal.ast.Expression expression,
				java.util.List<org.meta_environment.rascal.ast.Case> cases) {
			this.tree = tree;
			this.label = label;
			this.expression = expression;
			this.cases = cases;
		}

		private void $setCases(
				java.util.List<org.meta_environment.rascal.ast.Case> x) {
			this.cases = x;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Label x) {
			this.label = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementSwitch(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Case> getCases() {
			return cases;
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.Label getLabel() {
			return label;
		}

		public Switch setCases(
				java.util.List<org.meta_environment.rascal.ast.Case> x) {
			Switch z = new Switch();
			z.$setCases(x);
			return z;
		}

		public Switch setExpression(org.meta_environment.rascal.ast.Expression x) {
			Switch z = new Switch();
			z.$setExpression(x);
			return z;
		}

		public Switch setLabel(org.meta_environment.rascal.ast.Label x) {
			Switch z = new Switch();
			z.$setLabel(x);
			return z;
		}
	}

	static public class Throw extends Statement {
		private org.meta_environment.rascal.ast.Expression expression;

		/* "throw" expression:Expression ";" -> Statement {cons("Throw")} */
		private Throw() {
		}

		/* package */Throw(ITree tree,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementThrow(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public Throw setExpression(org.meta_environment.rascal.ast.Expression x) {
			Throw z = new Throw();
			z.$setExpression(x);
			return z;
		}
	}

	static public class Try extends Statement {
		private org.meta_environment.rascal.ast.Statement body;
		private java.util.List<org.meta_environment.rascal.ast.Catch> handlers;

		/*
		 * "try" body:Statement handlers:Catch+ -> Statement {non-assoc,
		 * cons("Try")}
		 */
		private Try() {
		}

		/* package */Try(ITree tree,
				org.meta_environment.rascal.ast.Statement body,
				java.util.List<org.meta_environment.rascal.ast.Catch> handlers) {
			this.tree = tree;
			this.body = body;
			this.handlers = handlers;
		}

		private void $setBody(org.meta_environment.rascal.ast.Statement x) {
			this.body = x;
		}

		private void $setHandlers(
				java.util.List<org.meta_environment.rascal.ast.Catch> x) {
			this.handlers = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementTry(this);
		}

		public org.meta_environment.rascal.ast.Statement getBody() {
			return body;
		}

		public java.util.List<org.meta_environment.rascal.ast.Catch> getHandlers() {
			return handlers;
		}

		public Try setBody(org.meta_environment.rascal.ast.Statement x) {
			Try z = new Try();
			z.$setBody(x);
			return z;
		}

		public Try setHandlers(
				java.util.List<org.meta_environment.rascal.ast.Catch> x) {
			Try z = new Try();
			z.$setHandlers(x);
			return z;
		}
	}

	static public class TryFinally extends Statement {
		private org.meta_environment.rascal.ast.Statement body;
		private org.meta_environment.rascal.ast.Statement finallyBody;
		private java.util.List<org.meta_environment.rascal.ast.Catch> handlers;

		/*
		 * "try" body:Statement handlers:Catch+ "finally" finallyBody:Statement
		 * -> Statement {cons("TryFinally")}
		 */
		private TryFinally() {
		}

		/* package */TryFinally(ITree tree,
				org.meta_environment.rascal.ast.Statement body,
				java.util.List<org.meta_environment.rascal.ast.Catch> handlers,
				org.meta_environment.rascal.ast.Statement finallyBody) {
			this.tree = tree;
			this.body = body;
			this.handlers = handlers;
			this.finallyBody = finallyBody;
		}

		private void $setBody(org.meta_environment.rascal.ast.Statement x) {
			this.body = x;
		}

		private void $setFinallyBody(org.meta_environment.rascal.ast.Statement x) {
			this.finallyBody = x;
		}

		private void $setHandlers(
				java.util.List<org.meta_environment.rascal.ast.Catch> x) {
			this.handlers = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementTryFinally(this);
		}

		public org.meta_environment.rascal.ast.Statement getBody() {
			return body;
		}

		public org.meta_environment.rascal.ast.Statement getFinallyBody() {
			return finallyBody;
		}

		public java.util.List<org.meta_environment.rascal.ast.Catch> getHandlers() {
			return handlers;
		}

		public TryFinally setBody(org.meta_environment.rascal.ast.Statement x) {
			TryFinally z = new TryFinally();
			z.$setBody(x);
			return z;
		}

		public TryFinally setFinallyBody(
				org.meta_environment.rascal.ast.Statement x) {
			TryFinally z = new TryFinally();
			z.$setFinallyBody(x);
			return z;
		}

		public TryFinally setHandlers(
				java.util.List<org.meta_environment.rascal.ast.Catch> x) {
			TryFinally z = new TryFinally();
			z.$setHandlers(x);
			return z;
		}
	}

	static public class VariableDeclaration extends Statement {
		private org.meta_environment.rascal.ast.LocalVariableDeclaration declaration;

		/*
		 * declaration:LocalVariableDeclaration ";" -> Statement
		 * {cons("VariableDeclaration")}
		 */
		private VariableDeclaration() {
		}

		/* package */VariableDeclaration(
				ITree tree,
				org.meta_environment.rascal.ast.LocalVariableDeclaration declaration) {
			this.tree = tree;
			this.declaration = declaration;
		}

		private void $setDeclaration(
				org.meta_environment.rascal.ast.LocalVariableDeclaration x) {
			this.declaration = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementVariableDeclaration(this);
		}

		public org.meta_environment.rascal.ast.LocalVariableDeclaration getDeclaration() {
			return declaration;
		}

		public VariableDeclaration setDeclaration(
				org.meta_environment.rascal.ast.LocalVariableDeclaration x) {
			VariableDeclaration z = new VariableDeclaration();
			z.$setDeclaration(x);
			return z;
		}
	}

	static public class Visit extends Statement {
		private org.meta_environment.rascal.ast.Visit visit;

		/* visit:Visit -> Statement {cons("Visit")} */
		private Visit() {
		}

		/* package */Visit(ITree tree,
				org.meta_environment.rascal.ast.Visit visit) {
			this.tree = tree;
			this.visit = visit;
		}

		private void $setVisit(org.meta_environment.rascal.ast.Visit x) {
			this.visit = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementVisit(this);
		}

		public org.meta_environment.rascal.ast.Visit getVisit() {
			return visit;
		}

		public Visit setVisit(org.meta_environment.rascal.ast.Visit x) {
			Visit z = new Visit();
			z.$setVisit(x);
			return z;
		}
	}

	static public class While extends Statement {
		private org.meta_environment.rascal.ast.Statement body;
		private org.meta_environment.rascal.ast.Expression condition;
		private org.meta_environment.rascal.ast.Label label;

		/*
		 * label:Label "while" "(" condition:Expression ")" body:Statement ->
		 * Statement {cons("While")}
		 */
		private While() {
		}

		/* package */While(ITree tree,
				org.meta_environment.rascal.ast.Label label,
				org.meta_environment.rascal.ast.Expression condition,
				org.meta_environment.rascal.ast.Statement body) {
			this.tree = tree;
			this.label = label;
			this.condition = condition;
			this.body = body;
		}

		private void $setBody(org.meta_environment.rascal.ast.Statement x) {
			this.body = x;
		}

		private void $setCondition(org.meta_environment.rascal.ast.Expression x) {
			this.condition = x;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Label x) {
			this.label = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStatementWhile(this);
		}

		public org.meta_environment.rascal.ast.Statement getBody() {
			return body;
		}

		public org.meta_environment.rascal.ast.Expression getCondition() {
			return condition;
		}

		public org.meta_environment.rascal.ast.Label getLabel() {
			return label;
		}

		public While setBody(org.meta_environment.rascal.ast.Statement x) {
			While z = new While();
			z.$setBody(x);
			return z;
		}

		public While setCondition(org.meta_environment.rascal.ast.Expression x) {
			While z = new While();
			z.$setCondition(x);
			return z;
		}

		public While setLabel(org.meta_environment.rascal.ast.Label x) {
			While z = new While();
			z.$setLabel(x);
			return z;
		}
	}
}
