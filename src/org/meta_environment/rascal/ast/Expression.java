package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Expression extends AbstractAST {
	static public class Addition extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "+" rhs:Expression -> Expression {cons("Addition"),
		 * left}
		 */
		private Addition() {
		}

		/* package */Addition(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionAddition(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isAddition() {
			return true;
		}

		public Addition setLhs(org.meta_environment.rascal.ast.Expression x) {
			Addition z = new Addition();
			z.$setLhs(x);
			return z;
		}

		public Addition setRhs(org.meta_environment.rascal.ast.Expression x) {
			Addition z = new Addition();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Ambiguity extends Expression {
		private final java.util.List<org.meta_environment.rascal.ast.Expression> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Expression> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getAlternatives() {
			return alternatives;
		}
	}

	static public class And extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/* lhs:Expression "&&" rhs:Expression -> Expression {left, cons("And")} */
		private And() {
		}

		/* package */And(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionAnd(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isAnd() {
			return true;
		}

		public And setLhs(org.meta_environment.rascal.ast.Expression x) {
			And z = new And();
			z.$setLhs(x);
			return z;
		}

		public And setRhs(org.meta_environment.rascal.ast.Expression x) {
			And z = new And();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Annotation extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;
		private org.meta_environment.rascal.ast.Name name;

		/*
		 * expression:Expression "@" name:Name -> Expression
		 * {cons("Annotation")}
		 */
		private Annotation() {
		}

		/* package */Annotation(ITree tree,
				org.meta_environment.rascal.ast.Expression expression,
				org.meta_environment.rascal.ast.Name name) {
			this.tree = tree;
			this.expression = expression;
			this.name = name;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionAnnotation(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean isAnnotation() {
			return true;
		}

		public Annotation setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			Annotation z = new Annotation();
			z.$setExpression(x);
			return z;
		}

		public Annotation setName(org.meta_environment.rascal.ast.Name x) {
			Annotation z = new Annotation();
			z.$setName(x);
			return z;
		}
	}

	static public class Area extends Expression {
		private org.meta_environment.rascal.ast.Area area;

		/* area:Area -> Expression {cons("Area")} */
		private Area() {
		}

		/* package */Area(ITree tree, org.meta_environment.rascal.ast.Area area) {
			this.tree = tree;
			this.area = area;
		}

		private void $setArea(org.meta_environment.rascal.ast.Area x) {
			this.area = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionArea(this);
		}

		public org.meta_environment.rascal.ast.Area getArea() {
			return area;
		}

		public boolean hasArea() {
			return true;
		}

		public boolean isArea() {
			return true;
		}

		public Area setArea(org.meta_environment.rascal.ast.Area x) {
			Area z = new Area();
			z.$setArea(x);
			return z;
		}
	}

	static public class AreaInFileLocation extends Expression {
		private org.meta_environment.rascal.ast.Expression areaExpression;
		private org.meta_environment.rascal.ast.Expression filename;

		/*
		 * "area-in-file" "(" filename:Expression "," areaExpression:Expression
		 * ")" -> Expression {cons("AreaInFileLocation")}
		 */
		private AreaInFileLocation() {
		}

		/* package */AreaInFileLocation(ITree tree,
				org.meta_environment.rascal.ast.Expression filename,
				org.meta_environment.rascal.ast.Expression areaExpression) {
			this.tree = tree;
			this.filename = filename;
			this.areaExpression = areaExpression;
		}

		private void $setAreaExpression(
				org.meta_environment.rascal.ast.Expression x) {
			this.areaExpression = x;
		}

		private void $setFilename(org.meta_environment.rascal.ast.Expression x) {
			this.filename = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionAreaInFileLocation(this);
		}

		public org.meta_environment.rascal.ast.Expression getAreaExpression() {
			return areaExpression;
		}

		public org.meta_environment.rascal.ast.Expression getFilename() {
			return filename;
		}

		public boolean hasAreaExpression() {
			return true;
		}

		public boolean hasFilename() {
			return true;
		}

		public boolean isAreaInFileLocation() {
			return true;
		}

		public AreaInFileLocation setAreaExpression(
				org.meta_environment.rascal.ast.Expression x) {
			AreaInFileLocation z = new AreaInFileLocation();
			z.$setAreaExpression(x);
			return z;
		}

		public AreaInFileLocation setFilename(
				org.meta_environment.rascal.ast.Expression x) {
			AreaInFileLocation z = new AreaInFileLocation();
			z.$setFilename(x);
			return z;
		}
	}

	static public class AreaLocation extends Expression {
		private org.meta_environment.rascal.ast.Expression areaExpression;

		/*
		 * "area" "(" areaExpression:Expression ")" -> Expression
		 * {cons("AreaLocation")}
		 */
		private AreaLocation() {
		}

		/* package */AreaLocation(ITree tree,
				org.meta_environment.rascal.ast.Expression areaExpression) {
			this.tree = tree;
			this.areaExpression = areaExpression;
		}

		private void $setAreaExpression(
				org.meta_environment.rascal.ast.Expression x) {
			this.areaExpression = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionAreaLocation(this);
		}

		public org.meta_environment.rascal.ast.Expression getAreaExpression() {
			return areaExpression;
		}

		public boolean hasAreaExpression() {
			return true;
		}

		public boolean isAreaLocation() {
			return true;
		}

		public AreaLocation setAreaExpression(
				org.meta_environment.rascal.ast.Expression x) {
			AreaLocation z = new AreaLocation();
			z.$setAreaExpression(x);
			return z;
		}
	}

	static public class Bracket extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;

		/*
		 * "(" expression:Expression ")" -> Expression {cons("Bracket"),
		 * bracket}
		 */
		private Bracket() {
		}

		/* package */Bracket(ITree tree,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionBracket(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean isBracket() {
			return true;
		}

		public Bracket setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			Bracket z = new Bracket();
			z.$setExpression(x);
			return z;
		}
	}

	static public class CallOrTree extends Expression {
		private java.util.List<org.meta_environment.rascal.ast.Expression> arguments;
		private org.meta_environment.rascal.ast.Name name;

		/*
		 * name:Name "(" arguments:{Expression ","} ")" -> Expression
		 * {cons("CallOrTree")}
		 */
		private CallOrTree() {
		}

		/* package */CallOrTree(
				ITree tree,
				org.meta_environment.rascal.ast.Name name,
				java.util.List<org.meta_environment.rascal.ast.Expression> arguments) {
			this.tree = tree;
			this.name = name;
			this.arguments = arguments;
		}

		private void $setArguments(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.arguments = x;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionCallOrTree(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getArguments() {
			return arguments;
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public boolean hasArguments() {
			return true;
		}

		public boolean hasName() {
			return true;
		}

		public boolean isCallOrTree() {
			return true;
		}

		public CallOrTree setArguments(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			CallOrTree z = new CallOrTree();
			z.$setArguments(x);
			return z;
		}

		public CallOrTree setName(org.meta_environment.rascal.ast.Name x) {
			CallOrTree z = new CallOrTree();
			z.$setName(x);
			return z;
		}
	}

	static public class Closure extends Expression {
		private org.meta_environment.rascal.ast.Parameters parameters;
		private java.util.List<org.meta_environment.rascal.ast.Statement> statements;
		private org.meta_environment.rascal.ast.Type type;

		/*
		 * type:Type parameters:Parameters "{" statements:Statement "}" ->
		 * Expression {cons("Closure")}
		 */
		private Closure() {
		}

		/* package */Closure(
				ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Parameters parameters,
				java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
			this.tree = tree;
			this.type = type;
			this.parameters = parameters;
			this.statements = statements;
		}

		private void $setParameters(org.meta_environment.rascal.ast.Parameters x) {
			this.parameters = x;
		}

		private void $setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			this.statements = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionClosure(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Parameters getParameters() {
			return parameters;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() {
			return statements;
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public boolean hasParameters() {
			return true;
		}

		@Override
		public boolean hasStatements() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean isClosure() {
			return true;
		}

		public Closure setParameters(
				org.meta_environment.rascal.ast.Parameters x) {
			Closure z = new Closure();
			z.$setParameters(x);
			return z;
		}

		public Closure setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			Closure z = new Closure();
			z.$setStatements(x);
			return z;
		}

		public Closure setType(org.meta_environment.rascal.ast.Type x) {
			Closure z = new Closure();
			z.$setType(x);
			return z;
		}
	}

	static public class ClosureCall extends Expression {
		private java.util.List<org.meta_environment.rascal.ast.Expression> arguments;
		private org.meta_environment.rascal.ast.ClosureAsFunction closure;

		/*
		 * closure:ClosureAsFunction "(" arguments:{Expression ","} ")" ->
		 * Expression {cons("ClosureCall")}
		 */
		private ClosureCall() {
		}

		/* package */ClosureCall(
				ITree tree,
				org.meta_environment.rascal.ast.ClosureAsFunction closure,
				java.util.List<org.meta_environment.rascal.ast.Expression> arguments) {
			this.tree = tree;
			this.closure = closure;
			this.arguments = arguments;
		}

		private void $setArguments(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.arguments = x;
		}

		private void $setClosure(
				org.meta_environment.rascal.ast.ClosureAsFunction x) {
			this.closure = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionClosureCall(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Expression> getArguments() {
			return arguments;
		}

		@Override
		public org.meta_environment.rascal.ast.ClosureAsFunction getClosure() {
			return closure;
		}

		@Override
		public boolean hasArguments() {
			return true;
		}

		@Override
		public boolean hasClosure() {
			return true;
		}

		@Override
		public boolean isClosureCall() {
			return true;
		}

		public ClosureCall setArguments(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			ClosureCall z = new ClosureCall();
			z.$setArguments(x);
			return z;
		}

		public ClosureCall setClosure(
				org.meta_environment.rascal.ast.ClosureAsFunction x) {
			ClosureCall z = new ClosureCall();
			z.$setClosure(x);
			return z;
		}
	}

	static public class Composition extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "o" rhs:Expression -> Expression {cons("Composition"),
		 * left}
		 */
		private Composition() {
		}

		/* package */Composition(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionComposition(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		@Override
		public boolean hasLhs() {
			return true;
		}

		@Override
		public boolean hasRhs() {
			return true;
		}

		@Override
		public boolean isComposition() {
			return true;
		}

		public Composition setLhs(org.meta_environment.rascal.ast.Expression x) {
			Composition z = new Composition();
			z.$setLhs(x);
			return z;
		}

		public Composition setRhs(org.meta_environment.rascal.ast.Expression x) {
			Composition z = new Composition();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Comprehension extends Expression {
		private org.meta_environment.rascal.ast.Comprehension comprehension;

		/* comprehension:Comprehension -> Expression {cons("Comprehension")} */
		private Comprehension() {
		}

		/* package */Comprehension(ITree tree,
				org.meta_environment.rascal.ast.Comprehension comprehension) {
			this.tree = tree;
			this.comprehension = comprehension;
		}

		private void $setComprehension(
				org.meta_environment.rascal.ast.Comprehension x) {
			this.comprehension = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionComprehension(this);
		}

		public org.meta_environment.rascal.ast.Comprehension getComprehension() {
			return comprehension;
		}

		public boolean hasComprehension() {
			return true;
		}

		public boolean isComprehension() {
			return true;
		}

		public Comprehension setComprehension(
				org.meta_environment.rascal.ast.Comprehension x) {
			Comprehension z = new Comprehension();
			z.$setComprehension(x);
			return z;
		}
	}

	static public class Division extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "/" rhs:Expression -> Expression {cons("Division"),
		 * left}
		 */
		private Division() {
		}

		/* package */Division(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionDivision(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		@Override
		public boolean hasLhs() {
			return true;
		}

		@Override
		public boolean hasRhs() {
			return true;
		}

		@Override
		public boolean isDivision() {
			return true;
		}

		public Division setLhs(org.meta_environment.rascal.ast.Expression x) {
			Division z = new Division();
			z.$setLhs(x);
			return z;
		}

		public Division setRhs(org.meta_environment.rascal.ast.Expression x) {
			Division z = new Division();
			z.$setRhs(x);
			return z;
		}
	}

	static public class EmptySetOrBlock extends Expression {
		/* "{" "}" -> Expression {cons("EmptySetOrBlock")} */

		/* package */EmptySetOrBlock(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionEmptySetOrBlock(this);
		}

		public boolean isEmptySetOrBlock() {
			return true;
		}
	}

	static public class Equals extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/**
		 * lhs:Expression "==" rhs:Expression -> Expression {left,
		 * cons("Equals")}
		 */
		private Equals() {
		}

		/* package */Equals(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionEquals(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isEquals() {
			return true;
		}

		public Equals setLhs(org.meta_environment.rascal.ast.Expression x) {
			Equals z = new Equals();
			z.$setLhs(x);
			return z;
		}

		public Equals setRhs(org.meta_environment.rascal.ast.Expression x) {
			Equals z = new Equals();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Equivalence extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "<==>" rhs:Expression -> Expression {right,
		 * cons("Equivalence")}
		 */
		private Equivalence() {
		}

		/* package */Equivalence(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionEquivalence(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isEquivalence() {
			return true;
		}

		public Equivalence setLhs(org.meta_environment.rascal.ast.Expression x) {
			Equivalence z = new Equivalence();
			z.$setLhs(x);
			return z;
		}

		public Equivalence setRhs(org.meta_environment.rascal.ast.Expression x) {
			Equivalence z = new Equivalence();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Exists extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;
		private org.meta_environment.rascal.ast.ValueProducer producer;

		/*
		 * "exists" "(" producer:ValueProducer "|" expression:Expression ")" ->
		 * Expression {cons("Exists")}
		 */
		private Exists() {
		}

		/* package */Exists(ITree tree,
				org.meta_environment.rascal.ast.ValueProducer producer,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.producer = producer;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setProducer(
				org.meta_environment.rascal.ast.ValueProducer x) {
			this.producer = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionExists(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.ValueProducer getProducer() {
			return producer;
		}

		public boolean hasExpression() {
			return true;
		}

		public boolean hasProducer() {
			return true;
		}

		public boolean isExists() {
			return true;
		}

		public Exists setExpression(org.meta_environment.rascal.ast.Expression x) {
			Exists z = new Exists();
			z.$setExpression(x);
			return z;
		}

		public Exists setProducer(
				org.meta_environment.rascal.ast.ValueProducer x) {
			Exists z = new Exists();
			z.$setProducer(x);
			return z;
		}
	}

	static public class FieldAccess extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;
		private org.meta_environment.rascal.ast.Name field;

		/*
		 * expression:Expression "." field:Name -> Expression
		 * {cons("FieldAccess")}
		 */
		private FieldAccess() {
		}

		/* package */FieldAccess(ITree tree,
				org.meta_environment.rascal.ast.Expression expression,
				org.meta_environment.rascal.ast.Name field) {
			this.tree = tree;
			this.expression = expression;
			this.field = field;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setField(org.meta_environment.rascal.ast.Name x) {
			this.field = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionFieldAccess(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getField() {
			return field;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasField() {
			return true;
		}

		@Override
		public boolean isFieldAccess() {
			return true;
		}

		public FieldAccess setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			FieldAccess z = new FieldAccess();
			z.$setExpression(x);
			return z;
		}

		public FieldAccess setField(org.meta_environment.rascal.ast.Name x) {
			FieldAccess z = new FieldAccess();
			z.$setField(x);
			return z;
		}
	}

	static public class FieldUpdate extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;
		private org.meta_environment.rascal.ast.Name key;
		private org.meta_environment.rascal.ast.Expression replacement;

		/*
		 * expression:Expression "[" key:Name "->" replacement:Expression "]" ->
		 * Expression {cons("FieldUpdate")}
		 */
		private FieldUpdate() {
		}

		/* package */FieldUpdate(ITree tree,
				org.meta_environment.rascal.ast.Expression expression,
				org.meta_environment.rascal.ast.Name key,
				org.meta_environment.rascal.ast.Expression replacement) {
			this.tree = tree;
			this.expression = expression;
			this.key = key;
			this.replacement = replacement;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setKey(org.meta_environment.rascal.ast.Name x) {
			this.key = x;
		}

		private void $setReplacement(
				org.meta_environment.rascal.ast.Expression x) {
			this.replacement = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionFieldUpdate(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getKey() {
			return key;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getReplacement() {
			return replacement;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasKey() {
			return true;
		}

		@Override
		public boolean hasReplacement() {
			return true;
		}

		@Override
		public boolean isFieldUpdate() {
			return true;
		}

		public FieldUpdate setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			FieldUpdate z = new FieldUpdate();
			z.$setExpression(x);
			return z;
		}

		public FieldUpdate setKey(org.meta_environment.rascal.ast.Name x) {
			FieldUpdate z = new FieldUpdate();
			z.$setKey(x);
			return z;
		}

		public FieldUpdate setReplacement(
				org.meta_environment.rascal.ast.Expression x) {
			FieldUpdate z = new FieldUpdate();
			z.$setReplacement(x);
			return z;
		}
	}

	static public class FileLocation extends Expression {
		private org.meta_environment.rascal.ast.Expression filename;

		/*
		 * "file" "(" filename:Expression ")" -> Expression
		 * {cons("FileLocation")}
		 */
		private FileLocation() {
		}

		/* package */FileLocation(ITree tree,
				org.meta_environment.rascal.ast.Expression filename) {
			this.tree = tree;
			this.filename = filename;
		}

		private void $setFilename(org.meta_environment.rascal.ast.Expression x) {
			this.filename = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionFileLocation(this);
		}

		public org.meta_environment.rascal.ast.Expression getFilename() {
			return filename;
		}

		public boolean hasFilename() {
			return true;
		}

		public boolean isFileLocation() {
			return true;
		}

		public FileLocation setFilename(
				org.meta_environment.rascal.ast.Expression x) {
			FileLocation z = new FileLocation();
			z.$setFilename(x);
			return z;
		}
	}

	static public class ForAll extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;
		private org.meta_environment.rascal.ast.ValueProducer producer;

		/*
		 * "forall" "(" producer:ValueProducer "|" expression:Expression ")" ->
		 * Expression {cons("ForAll")}
		 */
		private ForAll() {
		}

		/* package */ForAll(ITree tree,
				org.meta_environment.rascal.ast.ValueProducer producer,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.producer = producer;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setProducer(
				org.meta_environment.rascal.ast.ValueProducer x) {
			this.producer = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionForAll(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.ValueProducer getProducer() {
			return producer;
		}

		public boolean hasExpression() {
			return true;
		}

		public boolean hasProducer() {
			return true;
		}

		public boolean isForAll() {
			return true;
		}

		public ForAll setExpression(org.meta_environment.rascal.ast.Expression x) {
			ForAll z = new ForAll();
			z.$setExpression(x);
			return z;
		}

		public ForAll setProducer(
				org.meta_environment.rascal.ast.ValueProducer x) {
			ForAll z = new ForAll();
			z.$setProducer(x);
			return z;
		}
	}

	static public class FunctionAsValue extends Expression {
		private org.meta_environment.rascal.ast.FunctionAsValue function;

		/* function:FunctionAsValue -> Expression {cons("FunctionAsValue")} */
		private FunctionAsValue() {
		}

		/* package */FunctionAsValue(ITree tree,
				org.meta_environment.rascal.ast.FunctionAsValue function) {
			this.tree = tree;
			this.function = function;
		}

		private void $setFunction(
				org.meta_environment.rascal.ast.FunctionAsValue x) {
			this.function = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionFunctionAsValue(this);
		}

		@Override
		public org.meta_environment.rascal.ast.FunctionAsValue getFunction() {
			return function;
		}

		@Override
		public boolean hasFunction() {
			return true;
		}

		@Override
		public boolean isFunctionAsValue() {
			return true;
		}

		public FunctionAsValue setFunction(
				org.meta_environment.rascal.ast.FunctionAsValue x) {
			FunctionAsValue z = new FunctionAsValue();
			z.$setFunction(x);
			return z;
		}
	}

	static public class GreaterThan extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression ">" rhs:Expression -> Expression {non-assoc,
		 * cons("GreaterThan")}
		 */
		private GreaterThan() {
		}

		/* package */GreaterThan(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionGreaterThan(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isGreaterThan() {
			return true;
		}

		public GreaterThan setLhs(org.meta_environment.rascal.ast.Expression x) {
			GreaterThan z = new GreaterThan();
			z.$setLhs(x);
			return z;
		}

		public GreaterThan setRhs(org.meta_environment.rascal.ast.Expression x) {
			GreaterThan z = new GreaterThan();
			z.$setRhs(x);
			return z;
		}
	}

	static public class GreaterThanOrEq extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression ">=" rhs:Expression -> Expression {non-assoc,
		 * cons("GreaterThanOrEq")}
		 */
		private GreaterThanOrEq() {
		}

		/* package */GreaterThanOrEq(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionGreaterThanOrEq(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isGreaterThanOrEq() {
			return true;
		}

		public GreaterThanOrEq setLhs(
				org.meta_environment.rascal.ast.Expression x) {
			GreaterThanOrEq z = new GreaterThanOrEq();
			z.$setLhs(x);
			return z;
		}

		public GreaterThanOrEq setRhs(
				org.meta_environment.rascal.ast.Expression x) {
			GreaterThanOrEq z = new GreaterThanOrEq();
			z.$setRhs(x);
			return z;
		}
	}

	static public class IfDefined extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "?" rhs:Expression -> Expression {left,
		 * cons("IfDefined")}
		 */
		private IfDefined() {
		}

		/* package */IfDefined(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionIfDefined(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isIfDefined() {
			return true;
		}

		public IfDefined setLhs(org.meta_environment.rascal.ast.Expression x) {
			IfDefined z = new IfDefined();
			z.$setLhs(x);
			return z;
		}

		public IfDefined setRhs(org.meta_environment.rascal.ast.Expression x) {
			IfDefined z = new IfDefined();
			z.$setRhs(x);
			return z;
		}
	}

	static public class IfThenElse extends Expression {
		private org.meta_environment.rascal.ast.Expression condition;
		private org.meta_environment.rascal.ast.Expression elseExp;
		private org.meta_environment.rascal.ast.Expression thenExp;

		/*
		 * condition:Expression "?" thenExp:Expression ":" elseExp:Expression ->
		 * Expression {right, cons("IfThenElse")}
		 */
		private IfThenElse() {
		}

		/* package */IfThenElse(ITree tree,
				org.meta_environment.rascal.ast.Expression condition,
				org.meta_environment.rascal.ast.Expression thenExp,
				org.meta_environment.rascal.ast.Expression elseExp) {
			this.tree = tree;
			this.condition = condition;
			this.thenExp = thenExp;
			this.elseExp = elseExp;
		}

		private void $setCondition(org.meta_environment.rascal.ast.Expression x) {
			this.condition = x;
		}

		private void $setElseExp(org.meta_environment.rascal.ast.Expression x) {
			this.elseExp = x;
		}

		private void $setThenExp(org.meta_environment.rascal.ast.Expression x) {
			this.thenExp = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionIfThenElse(this);
		}

		public org.meta_environment.rascal.ast.Expression getCondition() {
			return condition;
		}

		public org.meta_environment.rascal.ast.Expression getElseExp() {
			return elseExp;
		}

		public org.meta_environment.rascal.ast.Expression getThenExp() {
			return thenExp;
		}

		public boolean hasCondition() {
			return true;
		}

		public boolean hasElseExp() {
			return true;
		}

		public boolean hasThenExp() {
			return true;
		}

		public boolean isIfThenElse() {
			return true;
		}

		public IfThenElse setCondition(
				org.meta_environment.rascal.ast.Expression x) {
			IfThenElse z = new IfThenElse();
			z.$setCondition(x);
			return z;
		}

		public IfThenElse setElseExp(
				org.meta_environment.rascal.ast.Expression x) {
			IfThenElse z = new IfThenElse();
			z.$setElseExp(x);
			return z;
		}

		public IfThenElse setThenExp(
				org.meta_environment.rascal.ast.Expression x) {
			IfThenElse z = new IfThenElse();
			z.$setThenExp(x);
			return z;
		}
	}

	static public class Implication extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "==>" rhs:Expression -> Expression {right,
		 * cons("Implication")}
		 */
		private Implication() {
		}

		/* package */Implication(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionImplication(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isImplication() {
			return true;
		}

		public Implication setLhs(org.meta_environment.rascal.ast.Expression x) {
			Implication z = new Implication();
			z.$setLhs(x);
			return z;
		}

		public Implication setRhs(org.meta_environment.rascal.ast.Expression x) {
			Implication z = new Implication();
			z.$setRhs(x);
			return z;
		}
	}

	static public class In extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "in" rhs:Expression -> Expression {non-assoc,
		 * cons("In")}
		 */
		private In() {
		}

		/* package */In(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionIn(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isIn() {
			return true;
		}

		public In setLhs(org.meta_environment.rascal.ast.Expression x) {
			In z = new In();
			z.$setLhs(x);
			return z;
		}

		public In setRhs(org.meta_environment.rascal.ast.Expression x) {
			In z = new In();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Intersection extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "&" rhs:Expression -> Expression
		 * {cons("Intersection"), left}
		 */
		private Intersection() {
		}

		/* package */Intersection(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionIntersection(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		@Override
		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		@Override
		public boolean isIntersection() {
			return true;
		}

		public Intersection setLhs(org.meta_environment.rascal.ast.Expression x) {
			Intersection z = new Intersection();
			z.$setLhs(x);
			return z;
		}

		public Intersection setRhs(org.meta_environment.rascal.ast.Expression x) {
			Intersection z = new Intersection();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Inverse extends Expression {
		private org.meta_environment.rascal.ast.Expression argument;

		/* "~" argument:Expression -> Expression {cons("Inverse")} */
		private Inverse() {
		}

		/* package */Inverse(ITree tree,
				org.meta_environment.rascal.ast.Expression argument) {
			this.tree = tree;
			this.argument = argument;
		}

		private void $setArgument(org.meta_environment.rascal.ast.Expression x) {
			this.argument = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionInverse(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getArgument() {
			return argument;
		}

		@Override
		public boolean hasArgument() {
			return true;
		}

		@Override
		public boolean isInverse() {
			return true;
		}

		public Inverse setArgument(org.meta_environment.rascal.ast.Expression x) {
			Inverse z = new Inverse();
			z.$setArgument(x);
			return z;
		}
	}

	static public class LessThan extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "<" rhs:Expression -> Expression {non-assoc,
		 * cons("LessThan")}
		 */
		private LessThan() {
		}

		/* package */LessThan(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionLessThan(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isLessThan() {
			return true;
		}

		public LessThan setLhs(org.meta_environment.rascal.ast.Expression x) {
			LessThan z = new LessThan();
			z.$setLhs(x);
			return z;
		}

		public LessThan setRhs(org.meta_environment.rascal.ast.Expression x) {
			LessThan z = new LessThan();
			z.$setRhs(x);
			return z;
		}
	}

	static public class LessThanOrEq extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "<=" rhs:Expression -> Expression {non-assoc,
		 * cons("LessThanOrEq")}
		 */
		private LessThanOrEq() {
		}

		/* package */LessThanOrEq(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionLessThanOrEq(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isLessThanOrEq() {
			return true;
		}

		public LessThanOrEq setLhs(org.meta_environment.rascal.ast.Expression x) {
			LessThanOrEq z = new LessThanOrEq();
			z.$setLhs(x);
			return z;
		}

		public LessThanOrEq setRhs(org.meta_environment.rascal.ast.Expression x) {
			LessThanOrEq z = new LessThanOrEq();
			z.$setRhs(x);
			return z;
		}
	}

	static public class List extends Expression {
		private java.util.List<org.meta_environment.rascal.ast.Expression> elements;

		/* "[" elements:{Expression ","} "]" -> Expression {cons("List")} */
		private List() {
		}

		/* package */List(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Expression> elements) {
			this.tree = tree;
			this.elements = elements;
		}

		private void $setElements(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.elements = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionList(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() {
			return elements;
		}

		public boolean hasElements() {
			return true;
		}

		public boolean isList() {
			return true;
		}

		public List setElements(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			List z = new List();
			z.$setElements(x);
			return z;
		}
	}

	static public class Literal extends Expression {
		private org.meta_environment.rascal.ast.Literal literal;

		/* literal:Literal -> Expression {cons("Literal")} */
		private Literal() {
		}

		/* package */Literal(ITree tree,
				org.meta_environment.rascal.ast.Literal literal) {
			this.tree = tree;
			this.literal = literal;
		}

		private void $setLiteral(org.meta_environment.rascal.ast.Literal x) {
			this.literal = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionLiteral(this);
		}

		public org.meta_environment.rascal.ast.Literal getLiteral() {
			return literal;
		}

		public boolean hasLiteral() {
			return true;
		}

		public boolean isLiteral() {
			return true;
		}

		public Literal setLiteral(org.meta_environment.rascal.ast.Literal x) {
			Literal z = new Literal();
			z.$setLiteral(x);
			return z;
		}
	}

	static public class Map extends Expression {
		/* "(" {Mapping[[Expression]] ","} ")" -> Expression {cons("Map")} */

		/* package */Map(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionMap(this);
		}

		public boolean isMap() {
			return true;
		}
	}

	static public class Match extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;
		private org.meta_environment.rascal.ast.Expression pattern;

		/*
		 * pattern:Expression ":=" expression:Expression -> Expression
		 * {cons("Match")}
		 */
		private Match() {
		}

		/* package */Match(ITree tree,
				org.meta_environment.rascal.ast.Expression pattern,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.pattern = pattern;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setPattern(org.meta_environment.rascal.ast.Expression x) {
			this.pattern = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionMatch(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
		}

		public boolean hasExpression() {
			return true;
		}

		public boolean hasPattern() {
			return true;
		}

		public boolean isMatch() {
			return true;
		}

		public Match setExpression(org.meta_environment.rascal.ast.Expression x) {
			Match z = new Match();
			z.$setExpression(x);
			return z;
		}

		public Match setPattern(org.meta_environment.rascal.ast.Expression x) {
			Match z = new Match();
			z.$setPattern(x);
			return z;
		}
	}

	static public class Modulo extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "%" rhs:Expression -> Expression {cons("Modulo"),
		 * left}
		 */
		private Modulo() {
		}

		/* package */Modulo(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionModulo(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		@Override
		public boolean hasLhs() {
			return true;
		}

		@Override
		public boolean hasRhs() {
			return true;
		}

		@Override
		public boolean isModulo() {
			return true;
		}

		public Modulo setLhs(org.meta_environment.rascal.ast.Expression x) {
			Modulo z = new Modulo();
			z.$setLhs(x);
			return z;
		}

		public Modulo setRhs(org.meta_environment.rascal.ast.Expression x) {
			Modulo z = new Modulo();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Negation extends Expression {
		private org.meta_environment.rascal.ast.Expression argument;

		/* "!" argument:Expression -> Expression {cons("Negation")} */
		private Negation() {
		}

		/* package */Negation(ITree tree,
				org.meta_environment.rascal.ast.Expression argument) {
			this.tree = tree;
			this.argument = argument;
		}

		private void $setArgument(org.meta_environment.rascal.ast.Expression x) {
			this.argument = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionNegation(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getArgument() {
			return argument;
		}

		@Override
		public boolean hasArgument() {
			return true;
		}

		@Override
		public boolean isNegation() {
			return true;
		}

		public Negation setArgument(org.meta_environment.rascal.ast.Expression x) {
			Negation z = new Negation();
			z.$setArgument(x);
			return z;
		}
	}

	static public class Negative extends Expression {
		private org.meta_environment.rascal.ast.Expression argument;

		/* "-" argument:Expression -> Expression {cons("Negative")} */
		private Negative() {
		}

		/* package */Negative(ITree tree,
				org.meta_environment.rascal.ast.Expression argument) {
			this.tree = tree;
			this.argument = argument;
		}

		private void $setArgument(org.meta_environment.rascal.ast.Expression x) {
			this.argument = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionNegative(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getArgument() {
			return argument;
		}

		@Override
		public boolean hasArgument() {
			return true;
		}

		@Override
		public boolean isNegative() {
			return true;
		}

		public Negative setArgument(org.meta_environment.rascal.ast.Expression x) {
			Negative z = new Negative();
			z.$setArgument(x);
			return z;
		}
	}

	static public class NoMatch extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;
		private org.meta_environment.rascal.ast.Expression pattern;

		/*
		 * pattern:Expression "!:=" expression:Expression -> Expression
		 * {cons("NoMatch")}
		 */
		private NoMatch() {
		}

		/* package */NoMatch(ITree tree,
				org.meta_environment.rascal.ast.Expression pattern,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.pattern = pattern;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setPattern(org.meta_environment.rascal.ast.Expression x) {
			this.pattern = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionNoMatch(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
		}

		public boolean hasExpression() {
			return true;
		}

		public boolean hasPattern() {
			return true;
		}

		public boolean isNoMatch() {
			return true;
		}

		public NoMatch setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			NoMatch z = new NoMatch();
			z.$setExpression(x);
			return z;
		}

		public NoMatch setPattern(org.meta_environment.rascal.ast.Expression x) {
			NoMatch z = new NoMatch();
			z.$setPattern(x);
			return z;
		}
	}

	static public class NonEmptyBlock extends Expression {
		private java.util.List<org.meta_environment.rascal.ast.Statement> statements;

		/* "{" statements:Statement+ "}" -> Expression {cons("NonEmptyBlock")} */
		private NonEmptyBlock() {
		}

		/* package */NonEmptyBlock(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
			this.tree = tree;
			this.statements = statements;
		}

		private void $setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			this.statements = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionNonEmptyBlock(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() {
			return statements;
		}

		@Override
		public boolean hasStatements() {
			return true;
		}

		@Override
		public boolean isNonEmptyBlock() {
			return true;
		}

		public NonEmptyBlock setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			NonEmptyBlock z = new NonEmptyBlock();
			z.$setStatements(x);
			return z;
		}
	}

	static public class NonEmptySet extends Expression {
		private java.util.List<org.meta_environment.rascal.ast.Expression> elements;

		/*
		 * "{" elements:{Expression ","}+ "}" -> Expression
		 * {cons("NonEmptySet")}
		 */
		private NonEmptySet() {
		}

		/* package */NonEmptySet(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Expression> elements) {
			this.tree = tree;
			this.elements = elements;
		}

		private void $setElements(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.elements = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionNonEmptySet(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() {
			return elements;
		}

		public boolean hasElements() {
			return true;
		}

		public boolean isNonEmptySet() {
			return true;
		}

		public NonEmptySet setElements(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			NonEmptySet z = new NonEmptySet();
			z.$setElements(x);
			return z;
		}
	}

	static public class NonEquals extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "!=" rhs:Expression -> Expression {left,
		 * cons("NonEquals")}
		 */
		private NonEquals() {
		}

		/* package */NonEquals(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionNonEquals(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isNonEquals() {
			return true;
		}

		public NonEquals setLhs(org.meta_environment.rascal.ast.Expression x) {
			NonEquals z = new NonEquals();
			z.$setLhs(x);
			return z;
		}

		public NonEquals setRhs(org.meta_environment.rascal.ast.Expression x) {
			NonEquals z = new NonEquals();
			z.$setRhs(x);
			return z;
		}
	}

	static public class NotIn extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "notin" rhs:Expression -> Expression {non-assoc,
		 * cons("NotIn")}
		 */
		private NotIn() {
		}

		/* package */NotIn(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionNotIn(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isNotIn() {
			return true;
		}

		public NotIn setLhs(org.meta_environment.rascal.ast.Expression x) {
			NotIn z = new NotIn();
			z.$setLhs(x);
			return z;
		}

		public NotIn setRhs(org.meta_environment.rascal.ast.Expression x) {
			NotIn z = new NotIn();
			z.$setRhs(x);
			return z;
		}
	}

	static public class OperatorAsValue extends Expression {
		private org.meta_environment.rascal.ast.OperatorAsValue operator;

		/* operator:OperatorAsValue -> Expression {cons("OperatorAsValue")} */
		private OperatorAsValue() {
		}

		/* package */OperatorAsValue(ITree tree,
				org.meta_environment.rascal.ast.OperatorAsValue operator) {
			this.tree = tree;
			this.operator = operator;
		}

		private void $setOperator(
				org.meta_environment.rascal.ast.OperatorAsValue x) {
			this.operator = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionOperatorAsValue(this);
		}

		@Override
		public org.meta_environment.rascal.ast.OperatorAsValue getOperator() {
			return operator;
		}

		@Override
		public boolean hasOperator() {
			return true;
		}

		@Override
		public boolean isOperatorAsValue() {
			return true;
		}

		public OperatorAsValue setOperator(
				org.meta_environment.rascal.ast.OperatorAsValue x) {
			OperatorAsValue z = new OperatorAsValue();
			z.$setOperator(x);
			return z;
		}
	}

	static public class Or extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/* lhs:Expression "||" rhs:Expression -> Expression {left, cons("Or")} */
		private Or() {
		}

		/* package */Or(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionOr(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isOr() {
			return true;
		}

		public Or setLhs(org.meta_environment.rascal.ast.Expression x) {
			Or z = new Or();
			z.$setLhs(x);
			return z;
		}

		public Or setRhs(org.meta_environment.rascal.ast.Expression x) {
			Or z = new Or();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Product extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "*" rhs:Expression -> Expression {cons("Product"),
		 * left}
		 */
		private Product() {
		}

		/* package */Product(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionProduct(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		@Override
		public boolean hasLhs() {
			return true;
		}

		@Override
		public boolean hasRhs() {
			return true;
		}

		@Override
		public boolean isProduct() {
			return true;
		}

		public Product setLhs(org.meta_environment.rascal.ast.Expression x) {
			Product z = new Product();
			z.$setLhs(x);
			return z;
		}

		public Product setRhs(org.meta_environment.rascal.ast.Expression x) {
			Product z = new Product();
			z.$setRhs(x);
			return z;
		}
	}

	static public class QualifiedName extends Expression {
		private org.meta_environment.rascal.ast.QualifiedName qualifiedName;

		/* qualifiedName:QualifiedName -> Expression {cons("QualifiedName")} */
		private QualifiedName() {
		}

		/* package */QualifiedName(ITree tree,
				org.meta_environment.rascal.ast.QualifiedName qualifiedName) {
			this.tree = tree;
			this.qualifiedName = qualifiedName;
		}

		private void $setQualifiedName(
				org.meta_environment.rascal.ast.QualifiedName x) {
			this.qualifiedName = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionQualifiedName(this);
		}

		public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() {
			return qualifiedName;
		}

		public boolean hasQualifiedName() {
			return true;
		}

		public boolean isQualifiedName() {
			return true;
		}

		public QualifiedName setQualifiedName(
				org.meta_environment.rascal.ast.QualifiedName x) {
			QualifiedName z = new QualifiedName();
			z.$setQualifiedName(x);
			return z;
		}
	}

	static public class Range extends Expression {
		private org.meta_environment.rascal.ast.Expression first;
		private org.meta_environment.rascal.ast.Expression last;

		/*
		 * "[" first:Expression ".." last:Expression "]" -> Expression
		 * {cons("Range")}
		 */
		private Range() {
		}

		/* package */Range(ITree tree,
				org.meta_environment.rascal.ast.Expression first,
				org.meta_environment.rascal.ast.Expression last) {
			this.tree = tree;
			this.first = first;
			this.last = last;
		}

		private void $setFirst(org.meta_environment.rascal.ast.Expression x) {
			this.first = x;
		}

		private void $setLast(org.meta_environment.rascal.ast.Expression x) {
			this.last = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionRange(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getFirst() {
			return first;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getLast() {
			return last;
		}

		@Override
		public boolean hasFirst() {
			return true;
		}

		@Override
		public boolean hasLast() {
			return true;
		}

		@Override
		public boolean isRange() {
			return true;
		}

		public Range setFirst(org.meta_environment.rascal.ast.Expression x) {
			Range z = new Range();
			z.$setFirst(x);
			return z;
		}

		public Range setLast(org.meta_environment.rascal.ast.Expression x) {
			Range z = new Range();
			z.$setLast(x);
			return z;
		}
	}

	static public class RegExpMatch extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "=~" rhs:Expression -> Expression {non-assoc,
		 * cons("RegExpMatch")}
		 */
		private RegExpMatch() {
		}

		/* package */RegExpMatch(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionRegExpMatch(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isRegExpMatch() {
			return true;
		}

		public RegExpMatch setLhs(org.meta_environment.rascal.ast.Expression x) {
			RegExpMatch z = new RegExpMatch();
			z.$setLhs(x);
			return z;
		}

		public RegExpMatch setRhs(org.meta_environment.rascal.ast.Expression x) {
			RegExpMatch z = new RegExpMatch();
			z.$setRhs(x);
			return z;
		}
	}

	static public class RegExpNoMatch extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "!~" rhs:Expression -> Expression {non-assoc,
		 * cons("RegExpNoMatch")}
		 */
		private RegExpNoMatch() {
		}

		/* package */RegExpNoMatch(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionRegExpNoMatch(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isRegExpNoMatch() {
			return true;
		}

		public RegExpNoMatch setLhs(org.meta_environment.rascal.ast.Expression x) {
			RegExpNoMatch z = new RegExpNoMatch();
			z.$setLhs(x);
			return z;
		}

		public RegExpNoMatch setRhs(org.meta_environment.rascal.ast.Expression x) {
			RegExpNoMatch z = new RegExpNoMatch();
			z.$setRhs(x);
			return z;
		}
	}

	static public class StepRange extends Expression {
		private org.meta_environment.rascal.ast.Expression first;
		private org.meta_environment.rascal.ast.Expression last;
		private org.meta_environment.rascal.ast.Expression second;

		/*
		 * "[" first:Expression "," second:Expression "," ".." last:Expression
		 * "]" -> Expression {cons("StepRange")}
		 */
		private StepRange() {
		}

		/* package */StepRange(ITree tree,
				org.meta_environment.rascal.ast.Expression first,
				org.meta_environment.rascal.ast.Expression second,
				org.meta_environment.rascal.ast.Expression last) {
			this.tree = tree;
			this.first = first;
			this.second = second;
			this.last = last;
		}

		private void $setFirst(org.meta_environment.rascal.ast.Expression x) {
			this.first = x;
		}

		private void $setLast(org.meta_environment.rascal.ast.Expression x) {
			this.last = x;
		}

		private void $setSecond(org.meta_environment.rascal.ast.Expression x) {
			this.second = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionStepRange(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getFirst() {
			return first;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getLast() {
			return last;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getSecond() {
			return second;
		}

		@Override
		public boolean hasFirst() {
			return true;
		}

		@Override
		public boolean hasLast() {
			return true;
		}

		@Override
		public boolean hasSecond() {
			return true;
		}

		@Override
		public boolean isStepRange() {
			return true;
		}

		public StepRange setFirst(org.meta_environment.rascal.ast.Expression x) {
			StepRange z = new StepRange();
			z.$setFirst(x);
			return z;
		}

		public StepRange setLast(org.meta_environment.rascal.ast.Expression x) {
			StepRange z = new StepRange();
			z.$setLast(x);
			return z;
		}

		public StepRange setSecond(org.meta_environment.rascal.ast.Expression x) {
			StepRange z = new StepRange();
			z.$setSecond(x);
			return z;
		}
	}

	static public class Subscript extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;
		private org.meta_environment.rascal.ast.Expression subscript;

		/*
		 * expression:Expression "[" subscript:Expression "]" -> Expression
		 * {cons("Subscript")}
		 */
		private Subscript() {
		}

		/* package */Subscript(ITree tree,
				org.meta_environment.rascal.ast.Expression expression,
				org.meta_environment.rascal.ast.Expression subscript) {
			this.tree = tree;
			this.expression = expression;
			this.subscript = subscript;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setSubscript(org.meta_environment.rascal.ast.Expression x) {
			this.subscript = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionSubscript(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getSubscript() {
			return subscript;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasSubscript() {
			return true;
		}

		@Override
		public boolean isSubscript() {
			return true;
		}

		public Subscript setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			Subscript z = new Subscript();
			z.$setExpression(x);
			return z;
		}

		public Subscript setSubscript(
				org.meta_environment.rascal.ast.Expression x) {
			Subscript z = new Subscript();
			z.$setSubscript(x);
			return z;
		}
	}

	static public class Substraction extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "-" rhs:Expression -> Expression
		 * {cons("Substraction"), left}
		 */
		private Substraction() {
		}

		/* package */Substraction(ITree tree,
				org.meta_environment.rascal.ast.Expression lhs,
				org.meta_environment.rascal.ast.Expression rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Expression x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Expression x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionSubstraction(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
		}

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isSubstraction() {
			return true;
		}

		public Substraction setLhs(org.meta_environment.rascal.ast.Expression x) {
			Substraction z = new Substraction();
			z.$setLhs(x);
			return z;
		}

		public Substraction setRhs(org.meta_environment.rascal.ast.Expression x) {
			Substraction z = new Substraction();
			z.$setRhs(x);
			return z;
		}
	}

	static public class TransitiveClosure extends Expression {
		private org.meta_environment.rascal.ast.Expression argument;

		/* argument:Expression "+" -> Expression {cons("TransitiveClosure")} */
		private TransitiveClosure() {
		}

		/* package */TransitiveClosure(ITree tree,
				org.meta_environment.rascal.ast.Expression argument) {
			this.tree = tree;
			this.argument = argument;
		}

		private void $setArgument(org.meta_environment.rascal.ast.Expression x) {
			this.argument = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionTransitiveClosure(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getArgument() {
			return argument;
		}

		@Override
		public boolean hasArgument() {
			return true;
		}

		@Override
		public boolean isTransitiveClosure() {
			return true;
		}

		public TransitiveClosure setArgument(
				org.meta_environment.rascal.ast.Expression x) {
			TransitiveClosure z = new TransitiveClosure();
			z.$setArgument(x);
			return z;
		}
	}

	static public class TransitiveReflexiveClosure extends Expression {
		private org.meta_environment.rascal.ast.Expression argument;

		/*
		 * argument:Expression "*" -> Expression
		 * {cons("TransitiveReflexiveClosure")}
		 */
		private TransitiveReflexiveClosure() {
		}

		/* package */TransitiveReflexiveClosure(ITree tree,
				org.meta_environment.rascal.ast.Expression argument) {
			this.tree = tree;
			this.argument = argument;
		}

		private void $setArgument(org.meta_environment.rascal.ast.Expression x) {
			this.argument = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionTransitiveReflexiveClosure(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getArgument() {
			return argument;
		}

		@Override
		public boolean hasArgument() {
			return true;
		}

		@Override
		public boolean isTransitiveReflexiveClosure() {
			return true;
		}

		public TransitiveReflexiveClosure setArgument(
				org.meta_environment.rascal.ast.Expression x) {
			TransitiveReflexiveClosure z = new TransitiveReflexiveClosure();
			z.$setArgument(x);
			return z;
		}
	}

	static public class Tuple extends Expression {
		private org.meta_environment.rascal.ast.Expression first;
		private java.util.List<org.meta_environment.rascal.ast.Expression> rest;

		/*
		 * "<" first:Expression "," rest:{Expression ","}+ ">" -> Expression
		 * {cons("Tuple")}
		 */
		private Tuple() {
		}

		/* package */Tuple(ITree tree,
				org.meta_environment.rascal.ast.Expression first,
				java.util.List<org.meta_environment.rascal.ast.Expression> rest) {
			this.tree = tree;
			this.first = first;
			this.rest = rest;
		}

		private void $setFirst(org.meta_environment.rascal.ast.Expression x) {
			this.first = x;
		}

		private void $setRest(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.rest = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionTuple(this);
		}

		public org.meta_environment.rascal.ast.Expression getFirst() {
			return first;
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getRest() {
			return rest;
		}

		public boolean hasFirst() {
			return true;
		}

		public boolean hasRest() {
			return true;
		}

		public boolean isTuple() {
			return true;
		}

		public Tuple setFirst(org.meta_environment.rascal.ast.Expression x) {
			Tuple z = new Tuple();
			z.$setFirst(x);
			return z;
		}

		public Tuple setRest(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			Tuple z = new Tuple();
			z.$setRest(x);
			return z;
		}
	}

	static public class TypedVariable extends Expression {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Type type;

		/* type:Type name:Name -> Expression {cons("TypedVariable")} */
		private TypedVariable() {
		}

		/* package */TypedVariable(ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Name name) {
			this.tree = tree;
			this.type = type;
			this.name = name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionTypedVariable(this);
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public boolean hasName() {
			return true;
		}

		public boolean hasType() {
			return true;
		}

		public boolean isTypedVariable() {
			return true;
		}

		public TypedVariable setName(org.meta_environment.rascal.ast.Name x) {
			TypedVariable z = new TypedVariable();
			z.$setName(x);
			return z;
		}

		public TypedVariable setType(org.meta_environment.rascal.ast.Type x) {
			TypedVariable z = new TypedVariable();
			z.$setType(x);
			return z;
		}
	}

	static public class Visit extends Expression {
		private org.meta_environment.rascal.ast.Visit visit;

		/* visit:Visit -> Expression {cons("Visit")} */
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

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionVisit(this);
		}

		public org.meta_environment.rascal.ast.Visit getVisit() {
			return visit;
		}

		public boolean hasVisit() {
			return true;
		}

		public boolean isVisit() {
			return true;
		}

		public Visit setVisit(org.meta_environment.rascal.ast.Visit x) {
			Visit z = new Visit();
			z.$setVisit(x);
			return z;
		}
	}

	static public class VoidClosure extends Expression {
		private org.meta_environment.rascal.ast.Parameters parameters;
		private java.util.List<org.meta_environment.rascal.ast.Statement> statements;

		/*
		 * parameters:Parameters "{" statements:Statement "}" -> Expression
		 * {cons("VoidClosure")}
		 */
		private VoidClosure() {
		}

		/* package */VoidClosure(
				ITree tree,
				org.meta_environment.rascal.ast.Parameters parameters,
				java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
			this.tree = tree;
			this.parameters = parameters;
			this.statements = statements;
		}

		private void $setParameters(org.meta_environment.rascal.ast.Parameters x) {
			this.parameters = x;
		}

		private void $setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			this.statements = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionVoidClosure(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Parameters getParameters() {
			return parameters;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() {
			return statements;
		}

		@Override
		public boolean hasParameters() {
			return true;
		}

		@Override
		public boolean hasStatements() {
			return true;
		}

		@Override
		public boolean isVoidClosure() {
			return true;
		}

		public VoidClosure setParameters(
				org.meta_environment.rascal.ast.Parameters x) {
			VoidClosure z = new VoidClosure();
			z.$setParameters(x);
			return z;
		}

		public VoidClosure setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			VoidClosure z = new VoidClosure();
			z.$setStatements(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Area getArea() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getAreaExpression() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getArgument() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Expression> getArguments() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.ClosureAsFunction getClosure() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Comprehension getComprehension() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getCondition() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getElseExp() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getExpression() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getField() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getFilename() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getFirst() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.FunctionAsValue getFunction() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getKey() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getLast() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getLhs() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Literal getLiteral() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.OperatorAsValue getOperator() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Parameters getParameters() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getPattern() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.ValueProducer getProducer() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getReplacement() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Expression> getRest() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getRhs() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getSecond() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getSubscript() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getThenExp() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Visit getVisit() {
		throw new UnsupportedOperationException();
	}

	public boolean hasArea() {
		return false;
	}

	public boolean hasAreaExpression() {
		return false;
	}

	public boolean hasArgument() {
		return false;
	}

	public boolean hasArguments() {
		return false;
	}

	public boolean hasClosure() {
		return false;
	}

	public boolean hasComprehension() {
		return false;
	}

	public boolean hasCondition() {
		return false;
	}

	public boolean hasElements() {
		return false;
	}

	public boolean hasElseExp() {
		return false;
	}

	public boolean hasExpression() {
		return false;
	}

	public boolean hasField() {
		return false;
	}

	public boolean hasFilename() {
		return false;
	}

	public boolean hasFirst() {
		return false;
	}

	public boolean hasFunction() {
		return false;
	}

	public boolean hasKey() {
		return false;
	}

	public boolean hasLast() {
		return false;
	}

	public boolean hasLhs() {
		return false;
	}

	public boolean hasLiteral() {
		return false;
	}

	public boolean hasName() {
		return false;
	}

	public boolean hasOperator() {
		return false;
	}

	public boolean hasParameters() {
		return false;
	}

	public boolean hasPattern() {
		return false;
	}

	public boolean hasProducer() {
		return false;
	}

	public boolean hasQualifiedName() {
		return false;
	}

	public boolean hasReplacement() {
		return false;
	}

	public boolean hasRest() {
		return false;
	}

	public boolean hasRhs() {
		return false;
	}

	public boolean hasSecond() {
		return false;
	}

	public boolean hasStatements() {
		return false;
	}

	public boolean hasSubscript() {
		return false;
	}

	public boolean hasThenExp() {
		return false;
	}

	public boolean hasType() {
		return false;
	}

	public boolean hasVisit() {
		return false;
	}

	public boolean isAddition() {
		return false;
	}

	public boolean isAnd() {
		return false;
	}

	public boolean isAnnotation() {
		return false;
	}

	public boolean isArea() {
		return false;
	}

	public boolean isAreaInFileLocation() {
		return false;
	}

	public boolean isAreaLocation() {
		return false;
	}

	public boolean isBracket() {
		return false;
	}

	public boolean isCallOrTree() {
		return false;
	}

	public boolean isClosure() {
		return false;
	}

	public boolean isClosureCall() {
		return false;
	}

	public boolean isComposition() {
		return false;
	}

	public boolean isComprehension() {
		return false;
	}

	public boolean isDivision() {
		return false;
	}

	public boolean isEmptySetOrBlock() {
		return false;
	}

	public boolean isEquals() {
		return false;
	}

	public boolean isEquivalence() {
		return false;
	}

	public boolean isExists() {
		return false;
	}

	public boolean isFieldAccess() {
		return false;
	}

	public boolean isFieldUpdate() {
		return false;
	}

	public boolean isFileLocation() {
		return false;
	}

	public boolean isForAll() {
		return false;
	}

	public boolean isFunctionAsValue() {
		return false;
	}

	public boolean isGreaterThan() {
		return false;
	}

	public boolean isGreaterThanOrEq() {
		return false;
	}

	public boolean isIfDefined() {
		return false;
	}

	public boolean isIfThenElse() {
		return false;
	}

	public boolean isImplication() {
		return false;
	}

	public boolean isIn() {
		return false;
	}

	public boolean isIntersection() {
		return false;
	}

	public boolean isInverse() {
		return false;
	}

	public boolean isLessThan() {
		return false;
	}

	public boolean isLessThanOrEq() {
		return false;
	}

	public boolean isList() {
		return false;
	}

	public boolean isLiteral() {
		return false;
	}

	public boolean isMap() {
		return false;
	}

	public boolean isMatch() {
		return false;
	}

	public boolean isModulo() {
		return false;
	}

	public boolean isNegation() {
		return false;
	}

	public boolean isNegative() {
		return false;
	}

	public boolean isNoMatch() {
		return false;
	}

	public boolean isNonEmptyBlock() {
		return false;
	}

	public boolean isNonEmptySet() {
		return false;
	}

	public boolean isNonEquals() {
		return false;
	}

	public boolean isNotIn() {
		return false;
	}

	public boolean isOperatorAsValue() {
		return false;
	}

	public boolean isOr() {
		return false;
	}

	public boolean isProduct() {
		return false;
	}

	public boolean isQualifiedName() {
		return false;
	}

	public boolean isRange() {
		return false;
	}

	public boolean isRegExpMatch() {
		return false;
	}

	public boolean isRegExpNoMatch() {
		return false;
	}

	public boolean isStepRange() {
		return false;
	}

	public boolean isSubscript() {
		return false;
	}

	public boolean isSubstraction() {
		return false;
	}

	public boolean isTransitiveClosure() {
		return false;
	}

	public boolean isTransitiveReflexiveClosure() {
		return false;
	}

	public boolean isTuple() {
		return false;
	}

	public boolean isTypedVariable() {
		return false;
	}

	public boolean isVisit() {
		return false;
	}

	public boolean isVoidClosure() {
		return false;
	}
}
