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
			final Addition z = new Addition();
			z.$setLhs(x);
			return z;
		}

		public Addition setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Addition z = new Addition();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Ambiguity extends Expression {
		private final java.util.List<org.meta_environment.rascal.ast.Expression> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Expression> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitExpressionAmbiguity(this);
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
			final And z = new And();
			z.$setLhs(x);
			return z;
		}

		public And setRhs(org.meta_environment.rascal.ast.Expression x) {
			final And z = new And();
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

		@Override
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
			final Annotation z = new Annotation();
			z.$setExpression(x);
			return z;
		}

		public Annotation setName(org.meta_environment.rascal.ast.Name x) {
			final Annotation z = new Annotation();
			z.$setName(x);
			return z;
		}
	}

	static public class Area extends Expression {
		private org.meta_environment.rascal.ast.Expression beginLine;
		private org.meta_environment.rascal.ast.Expression beginColumn;
		private org.meta_environment.rascal.ast.Expression endLine;

		private org.meta_environment.rascal.ast.Expression endColumn;

		private org.meta_environment.rascal.ast.Expression offset;
		private org.meta_environment.rascal.ast.Expression length;

		/*
		 * "area" "(" beginLine:Expression "," beginColumn:Expression ","
		 * endLine:Expression "," endColumn:Expression "," offset:Expression ","
		 * length:Expression ")" -> Expression {cons("Area")}
		 */
		private Area() {
		}

		/* package */Area(ITree tree,
				org.meta_environment.rascal.ast.Expression beginLine,
				org.meta_environment.rascal.ast.Expression beginColumn,
				org.meta_environment.rascal.ast.Expression endLine,
				org.meta_environment.rascal.ast.Expression endColumn,
				org.meta_environment.rascal.ast.Expression offset,
				org.meta_environment.rascal.ast.Expression length) {
			this.tree = tree;
			this.beginLine = beginLine;
			this.beginColumn = beginColumn;
			this.endLine = endLine;
			this.endColumn = endColumn;
			this.offset = offset;
			this.length = length;
		}

		private void $setBeginColumn(
				org.meta_environment.rascal.ast.Expression x) {
			this.beginColumn = x;
		}

		private void $setBeginLine(org.meta_environment.rascal.ast.Expression x) {
			this.beginLine = x;
		}

		private void $setEndColumn(org.meta_environment.rascal.ast.Expression x) {
			this.endColumn = x;
		}

		private void $setEndLine(org.meta_environment.rascal.ast.Expression x) {
			this.endLine = x;
		}

		private void $setLength(org.meta_environment.rascal.ast.Expression x) {
			this.length = x;
		}

		private void $setOffset(org.meta_environment.rascal.ast.Expression x) {
			this.offset = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionArea(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getBeginColumn() {
			return beginColumn;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getBeginLine() {
			return beginLine;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getEndColumn() {
			return endColumn;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getEndLine() {
			return endLine;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getLength() {
			return length;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getOffset() {
			return offset;
		}

		@Override
		public boolean hasBeginColumn() {
			return true;
		}

		@Override
		public boolean hasBeginLine() {
			return true;
		}

		@Override
		public boolean hasEndColumn() {
			return true;
		}

		@Override
		public boolean hasEndLine() {
			return true;
		}

		@Override
		public boolean hasLength() {
			return true;
		}

		@Override
		public boolean hasOffset() {
			return true;
		}

		@Override
		public boolean isArea() {
			return true;
		}

		public Area setBeginColumn(org.meta_environment.rascal.ast.Expression x) {
			final Area z = new Area();
			z.$setBeginColumn(x);
			return z;
		}

		public Area setBeginLine(org.meta_environment.rascal.ast.Expression x) {
			final Area z = new Area();
			z.$setBeginLine(x);
			return z;
		}

		public Area setEndColumn(org.meta_environment.rascal.ast.Expression x) {
			final Area z = new Area();
			z.$setEndColumn(x);
			return z;
		}

		public Area setEndLine(org.meta_environment.rascal.ast.Expression x) {
			final Area z = new Area();
			z.$setEndLine(x);
			return z;
		}

		public Area setLength(org.meta_environment.rascal.ast.Expression x) {
			final Area z = new Area();
			z.$setLength(x);
			return z;
		}

		public Area setOffset(org.meta_environment.rascal.ast.Expression x) {
			final Area z = new Area();
			z.$setOffset(x);
			return z;
		}
	}

	static public class AreaInFileLocation extends Expression {
		private org.meta_environment.rascal.ast.Expression filename;
		private org.meta_environment.rascal.ast.Expression areaExpression;

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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionAreaInFileLocation(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getAreaExpression() {
			return areaExpression;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getFilename() {
			return filename;
		}

		@Override
		public boolean hasAreaExpression() {
			return true;
		}

		@Override
		public boolean hasFilename() {
			return true;
		}

		@Override
		public boolean isAreaInFileLocation() {
			return true;
		}

		public AreaInFileLocation setAreaExpression(
				org.meta_environment.rascal.ast.Expression x) {
			final AreaInFileLocation z = new AreaInFileLocation();
			z.$setAreaExpression(x);
			return z;
		}

		public AreaInFileLocation setFilename(
				org.meta_environment.rascal.ast.Expression x) {
			final AreaInFileLocation z = new AreaInFileLocation();
			z.$setFilename(x);
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

		@Override
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
			final Bracket z = new Bracket();
			z.$setExpression(x);
			return z;
		}
	}

	static public class CallOrTree extends Expression {
		private org.meta_environment.rascal.ast.QualifiedName qualifiedName;
		private java.util.List<org.meta_environment.rascal.ast.Expression> arguments;

		/*
		 * qualifiedName:QualifiedName "(" arguments:{Expression ","} ")" ->
		 * Expression {cons("CallOrTree")}
		 */
		private CallOrTree() {
		}

		/* package */CallOrTree(
				ITree tree,
				org.meta_environment.rascal.ast.QualifiedName qualifiedName,
				java.util.List<org.meta_environment.rascal.ast.Expression> arguments) {
			this.tree = tree;
			this.qualifiedName = qualifiedName;
			this.arguments = arguments;
		}

		private void $setArguments(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.arguments = x;
		}

		private void $setQualifiedName(
				org.meta_environment.rascal.ast.QualifiedName x) {
			this.qualifiedName = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionCallOrTree(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Expression> getArguments() {
			return arguments;
		}

		@Override
		public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() {
			return qualifiedName;
		}

		@Override
		public boolean hasArguments() {
			return true;
		}

		@Override
		public boolean hasQualifiedName() {
			return true;
		}

		@Override
		public boolean isCallOrTree() {
			return true;
		}

		public CallOrTree setArguments(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			final CallOrTree z = new CallOrTree();
			z.$setArguments(x);
			return z;
		}

		public CallOrTree setQualifiedName(
				org.meta_environment.rascal.ast.QualifiedName x) {
			final CallOrTree z = new CallOrTree();
			z.$setQualifiedName(x);
			return z;
		}
	}

	static public class Closure extends Expression {
		private org.meta_environment.rascal.ast.Type type;
		private org.meta_environment.rascal.ast.Parameters parameters;
		private java.util.List<org.meta_environment.rascal.ast.Statement> statements;

		/*
		 * type:Type parameters:Parameters "{" statements:Statement+ "}" ->
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

		@Override
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
			final Closure z = new Closure();
			z.$setParameters(x);
			return z;
		}

		public Closure setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			final Closure z = new Closure();
			z.$setStatements(x);
			return z;
		}

		public Closure setType(org.meta_environment.rascal.ast.Type x) {
			final Closure z = new Closure();
			z.$setType(x);
			return z;
		}
	}

	static public class ClosureCall extends Expression {
		private org.meta_environment.rascal.ast.ClosureAsFunction closure;
		private java.util.List<org.meta_environment.rascal.ast.Expression> arguments;

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

		@Override
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
			final ClosureCall z = new ClosureCall();
			z.$setArguments(x);
			return z;
		}

		public ClosureCall setClosure(
				org.meta_environment.rascal.ast.ClosureAsFunction x) {
			final ClosureCall z = new ClosureCall();
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

		public boolean isComposition() {
			return true;
		}

		public Composition setLhs(org.meta_environment.rascal.ast.Expression x) {
			final Composition z = new Composition();
			z.$setLhs(x);
			return z;
		}

		public Composition setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Composition z = new Composition();
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionComprehension(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Comprehension getComprehension() {
			return comprehension;
		}

		@Override
		public boolean hasComprehension() {
			return true;
		}

		@Override
		public boolean isComprehension() {
			return true;
		}

		public Comprehension setComprehension(
				org.meta_environment.rascal.ast.Comprehension x) {
			final Comprehension z = new Comprehension();
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

		public boolean isDivision() {
			return true;
		}

		public Division setLhs(org.meta_environment.rascal.ast.Expression x) {
			final Division z = new Division();
			z.$setLhs(x);
			return z;
		}

		public Division setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Division z = new Division();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Equals extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
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
			final Equals z = new Equals();
			z.$setLhs(x);
			return z;
		}

		public Equals setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Equals z = new Equals();
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
			final Equivalence z = new Equivalence();
			z.$setLhs(x);
			return z;
		}

		public Equivalence setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Equivalence z = new Equivalence();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Exists extends Expression {
		private org.meta_environment.rascal.ast.ValueProducer producer;
		private org.meta_environment.rascal.ast.Expression expression;

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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionExists(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public org.meta_environment.rascal.ast.ValueProducer getProducer() {
			return producer;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasProducer() {
			return true;
		}

		@Override
		public boolean isExists() {
			return true;
		}

		public Exists setExpression(org.meta_environment.rascal.ast.Expression x) {
			final Exists z = new Exists();
			z.$setExpression(x);
			return z;
		}

		public Exists setProducer(
				org.meta_environment.rascal.ast.ValueProducer x) {
			final Exists z = new Exists();
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

		@Override
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
			final FieldAccess z = new FieldAccess();
			z.$setExpression(x);
			return z;
		}

		public FieldAccess setField(org.meta_environment.rascal.ast.Name x) {
			final FieldAccess z = new FieldAccess();
			z.$setField(x);
			return z;
		}
	}

	static public class FieldProject extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;
		private java.util.List<org.meta_environment.rascal.ast.Field> fields;

		/*
		 * expression:Expression "<" fields:{Field ","}+ ">" -> Expression
		 * {cons("FieldProject")}
		 */
		private FieldProject() {
		}

		/* package */FieldProject(ITree tree,
				org.meta_environment.rascal.ast.Expression expression,
				java.util.List<org.meta_environment.rascal.ast.Field> fields) {
			this.tree = tree;
			this.expression = expression;
			this.fields = fields;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setFields(
				java.util.List<org.meta_environment.rascal.ast.Field> x) {
			this.fields = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionFieldProject(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Field> getFields() {
			return fields;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasFields() {
			return true;
		}

		@Override
		public boolean isFieldProject() {
			return true;
		}

		public FieldProject setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			final FieldProject z = new FieldProject();
			z.$setExpression(x);
			return z;
		}

		public FieldProject setFields(
				java.util.List<org.meta_environment.rascal.ast.Field> x) {
			final FieldProject z = new FieldProject();
			z.$setFields(x);
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

		@Override
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
			final FieldUpdate z = new FieldUpdate();
			z.$setExpression(x);
			return z;
		}

		public FieldUpdate setKey(org.meta_environment.rascal.ast.Name x) {
			final FieldUpdate z = new FieldUpdate();
			z.$setKey(x);
			return z;
		}

		public FieldUpdate setReplacement(
				org.meta_environment.rascal.ast.Expression x) {
			final FieldUpdate z = new FieldUpdate();
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionFileLocation(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getFilename() {
			return filename;
		}

		@Override
		public boolean hasFilename() {
			return true;
		}

		@Override
		public boolean isFileLocation() {
			return true;
		}

		public FileLocation setFilename(
				org.meta_environment.rascal.ast.Expression x) {
			final FileLocation z = new FileLocation();
			z.$setFilename(x);
			return z;
		}
	}

	static public class ForAll extends Expression {
		private org.meta_environment.rascal.ast.ValueProducer producer;
		private org.meta_environment.rascal.ast.Expression expression;

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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionForAll(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public org.meta_environment.rascal.ast.ValueProducer getProducer() {
			return producer;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasProducer() {
			return true;
		}

		@Override
		public boolean isForAll() {
			return true;
		}

		public ForAll setExpression(org.meta_environment.rascal.ast.Expression x) {
			final ForAll z = new ForAll();
			z.$setExpression(x);
			return z;
		}

		public ForAll setProducer(
				org.meta_environment.rascal.ast.ValueProducer x) {
			final ForAll z = new ForAll();
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

		@Override
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
			final FunctionAsValue z = new FunctionAsValue();
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
			final GreaterThan z = new GreaterThan();
			z.$setLhs(x);
			return z;
		}

		public GreaterThan setRhs(org.meta_environment.rascal.ast.Expression x) {
			final GreaterThan z = new GreaterThan();
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
			final GreaterThanOrEq z = new GreaterThanOrEq();
			z.$setLhs(x);
			return z;
		}

		public GreaterThanOrEq setRhs(
				org.meta_environment.rascal.ast.Expression x) {
			final GreaterThanOrEq z = new GreaterThanOrEq();
			z.$setRhs(x);
			return z;
		}
	}

	static public class IfDefined extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "=?" rhs:Expression -> Expression {left,
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
			final IfDefined z = new IfDefined();
			z.$setLhs(x);
			return z;
		}

		public IfDefined setRhs(org.meta_environment.rascal.ast.Expression x) {
			final IfDefined z = new IfDefined();
			z.$setRhs(x);
			return z;
		}
	}

	static public class IfThenElse extends Expression {
		private org.meta_environment.rascal.ast.Expression condition;
		private org.meta_environment.rascal.ast.Expression thenExp;
		private org.meta_environment.rascal.ast.Expression elseExp;

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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionIfThenElse(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getCondition() {
			return condition;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getElseExp() {
			return elseExp;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getThenExp() {
			return thenExp;
		}

		@Override
		public boolean hasCondition() {
			return true;
		}

		@Override
		public boolean hasElseExp() {
			return true;
		}

		@Override
		public boolean hasThenExp() {
			return true;
		}

		@Override
		public boolean isIfThenElse() {
			return true;
		}

		public IfThenElse setCondition(
				org.meta_environment.rascal.ast.Expression x) {
			final IfThenElse z = new IfThenElse();
			z.$setCondition(x);
			return z;
		}

		public IfThenElse setElseExp(
				org.meta_environment.rascal.ast.Expression x) {
			final IfThenElse z = new IfThenElse();
			z.$setElseExp(x);
			return z;
		}

		public IfThenElse setThenExp(
				org.meta_environment.rascal.ast.Expression x) {
			final IfThenElse z = new IfThenElse();
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
			final Implication z = new Implication();
			z.$setLhs(x);
			return z;
		}

		public Implication setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Implication z = new Implication();
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
			final In z = new In();
			z.$setLhs(x);
			return z;
		}

		public In setRhs(org.meta_environment.rascal.ast.Expression x) {
			final In z = new In();
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

		public boolean hasLhs() {
			return true;
		}

		public boolean hasRhs() {
			return true;
		}

		public boolean isIntersection() {
			return true;
		}

		public Intersection setLhs(org.meta_environment.rascal.ast.Expression x) {
			final Intersection z = new Intersection();
			z.$setLhs(x);
			return z;
		}

		public Intersection setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Intersection z = new Intersection();
			z.$setRhs(x);
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
			final LessThan z = new LessThan();
			z.$setLhs(x);
			return z;
		}

		public LessThan setRhs(org.meta_environment.rascal.ast.Expression x) {
			final LessThan z = new LessThan();
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
			final LessThanOrEq z = new LessThanOrEq();
			z.$setLhs(x);
			return z;
		}

		public LessThanOrEq setRhs(org.meta_environment.rascal.ast.Expression x) {
			final LessThanOrEq z = new LessThanOrEq();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Lexical extends Expression {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		public <T> T accept(IASTVisitor<T> v) {
			return v.visitExpressionLexical(this);
		}

		public String getString() {
			return string;
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionList(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() {
			return elements;
		}

		@Override
		public boolean hasElements() {
			return true;
		}

		@Override
		public boolean isList() {
			return true;
		}

		public List setElements(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			final List z = new List();
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionLiteral(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Literal getLiteral() {
			return literal;
		}

		@Override
		public boolean hasLiteral() {
			return true;
		}

		@Override
		public boolean isLiteral() {
			return true;
		}

		public Literal setLiteral(org.meta_environment.rascal.ast.Literal x) {
			final Literal z = new Literal();
			z.$setLiteral(x);
			return z;
		}
	}

	static public class Map extends Expression {
		private java.util.List<org.meta_environment.rascal.ast.Mapping> mappings;

		/* "(" mappings:{Mapping ","} ")" -> Expression {cons("Map")} */
		private Map() {
		}

		/* package */Map(ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Mapping> mappings) {
			this.tree = tree;
			this.mappings = mappings;
		}

		private void $setMappings(
				java.util.List<org.meta_environment.rascal.ast.Mapping> x) {
			this.mappings = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionMap(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Mapping> getMappings() {
			return mappings;
		}

		@Override
		public boolean hasMappings() {
			return true;
		}

		@Override
		public boolean isMap() {
			return true;
		}

		public Map setMappings(
				java.util.List<org.meta_environment.rascal.ast.Mapping> x) {
			final Map z = new Map();
			z.$setMappings(x);
			return z;
		}
	}

	static public class Match extends Expression {
		private org.meta_environment.rascal.ast.Expression pattern;
		private org.meta_environment.rascal.ast.Expression expression;

		/*
		 * pattern:Expression "~=" expression:Expression -> Expression
		 * {non-assoc, cons("Match")}
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionMatch(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasPattern() {
			return true;
		}

		@Override
		public boolean isMatch() {
			return true;
		}

		public Match setExpression(org.meta_environment.rascal.ast.Expression x) {
			final Match z = new Match();
			z.$setExpression(x);
			return z;
		}

		public Match setPattern(org.meta_environment.rascal.ast.Expression x) {
			final Match z = new Match();
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

		public boolean isModulo() {
			return true;
		}

		public Modulo setLhs(org.meta_environment.rascal.ast.Expression x) {
			final Modulo z = new Modulo();
			z.$setLhs(x);
			return z;
		}

		public Modulo setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Modulo z = new Modulo();
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

		@Override
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
			final Negation z = new Negation();
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

		@Override
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
			final Negative z = new Negative();
			z.$setArgument(x);
			return z;
		}
	}

	static public class NoMatch extends Expression {
		private org.meta_environment.rascal.ast.Expression pattern;
		private org.meta_environment.rascal.ast.Expression expression;

		/*
		 * pattern:Expression "~!" expression:Expression -> Expression
		 * {non-assoc, cons("NoMatch")}
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionNoMatch(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasPattern() {
			return true;
		}

		@Override
		public boolean isNoMatch() {
			return true;
		}

		public NoMatch setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			final NoMatch z = new NoMatch();
			z.$setExpression(x);
			return z;
		}

		public NoMatch setPattern(org.meta_environment.rascal.ast.Expression x) {
			final NoMatch z = new NoMatch();
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

		@Override
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
			final NonEmptyBlock z = new NonEmptyBlock();
			z.$setStatements(x);
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
			final NonEquals z = new NonEquals();
			z.$setLhs(x);
			return z;
		}

		public NonEquals setRhs(org.meta_environment.rascal.ast.Expression x) {
			final NonEquals z = new NonEquals();
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
			final NotIn z = new NotIn();
			z.$setLhs(x);
			return z;
		}

		public NotIn setRhs(org.meta_environment.rascal.ast.Expression x) {
			final NotIn z = new NotIn();
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

		@Override
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
			final OperatorAsValue z = new OperatorAsValue();
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
			final Or z = new Or();
			z.$setLhs(x);
			return z;
		}

		public Or setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Or z = new Or();
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

		public boolean isProduct() {
			return true;
		}

		public Product setLhs(org.meta_environment.rascal.ast.Expression x) {
			final Product z = new Product();
			z.$setLhs(x);
			return z;
		}

		public Product setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Product z = new Product();
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionQualifiedName(this);
		}

		@Override
		public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() {
			return qualifiedName;
		}

		@Override
		public boolean hasQualifiedName() {
			return true;
		}

		@Override
		public boolean isQualifiedName() {
			return true;
		}

		public QualifiedName setQualifiedName(
				org.meta_environment.rascal.ast.QualifiedName x) {
			final QualifiedName z = new QualifiedName();
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

		@Override
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
			final Range z = new Range();
			z.$setFirst(x);
			return z;
		}

		public Range setLast(org.meta_environment.rascal.ast.Expression x) {
			final Range z = new Range();
			z.$setLast(x);
			return z;
		}
	}

	static public class Set extends Expression {
		private java.util.List<org.meta_environment.rascal.ast.Expression> elements;

		/* "{" elements:{Expression ","} "}" -> Expression {cons("Set")} */
		private Set() {
		}

		/* package */Set(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Expression> elements) {
			this.tree = tree;
			this.elements = elements;
		}

		private void $setElements(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.elements = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionSet(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() {
			return elements;
		}

		@Override
		public boolean hasElements() {
			return true;
		}

		@Override
		public boolean isSet() {
			return true;
		}

		public Set setElements(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			final Set z = new Set();
			z.$setElements(x);
			return z;
		}
	}

	static public class StepRange extends Expression {
		private org.meta_environment.rascal.ast.Expression first;
		private org.meta_environment.rascal.ast.Expression second;
		private org.meta_environment.rascal.ast.Expression last;

		/*
		 * "[" first:Expression "," second:Expression ".." last:Expression "]"
		 * -> Expression {cons("StepRange")}
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

		@Override
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
			final StepRange z = new StepRange();
			z.$setFirst(x);
			return z;
		}

		public StepRange setLast(org.meta_environment.rascal.ast.Expression x) {
			final StepRange z = new StepRange();
			z.$setLast(x);
			return z;
		}

		public StepRange setSecond(org.meta_environment.rascal.ast.Expression x) {
			final StepRange z = new StepRange();
			z.$setSecond(x);
			return z;
		}
	}

	static public class Subscript extends Expression {
		private org.meta_environment.rascal.ast.Expression expression;
		private java.util.List<org.meta_environment.rascal.ast.Expression> subscripts;

		/*
		 * expression:Expression "[" subscripts: {Expression ","}+"]" ->
		 * Expression {cons("Subscript")}
		 */
		private Subscript() {
		}

		/* package */Subscript(
				ITree tree,
				org.meta_environment.rascal.ast.Expression expression,
				java.util.List<org.meta_environment.rascal.ast.Expression> subscripts) {
			this.tree = tree;
			this.expression = expression;
			this.subscripts = subscripts;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setSubscripts(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.subscripts = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionSubscript(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Expression> getSubscripts() {
			return subscripts;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasSubscripts() {
			return true;
		}

		@Override
		public boolean isSubscript() {
			return true;
		}

		public Subscript setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			final Subscript z = new Subscript();
			z.$setExpression(x);
			return z;
		}

		public Subscript setSubscripts(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			final Subscript z = new Subscript();
			z.$setSubscripts(x);
			return z;
		}
	}

	static public class Subtraction extends Expression {
		private org.meta_environment.rascal.ast.Expression lhs;
		private org.meta_environment.rascal.ast.Expression rhs;

		/*
		 * lhs:Expression "-" rhs:Expression -> Expression {cons("Subtraction"),
		 * left}
		 */
		private Subtraction() {
		}

		/* package */Subtraction(ITree tree,
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
			return visitor.visitExpressionSubtraction(this);
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

		public boolean isSubtraction() {
			return true;
		}

		public Subtraction setLhs(org.meta_environment.rascal.ast.Expression x) {
			final Subtraction z = new Subtraction();
			z.$setLhs(x);
			return z;
		}

		public Subtraction setRhs(org.meta_environment.rascal.ast.Expression x) {
			final Subtraction z = new Subtraction();
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

		@Override
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
			final TransitiveClosure z = new TransitiveClosure();
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

		@Override
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
			final TransitiveReflexiveClosure z = new TransitiveReflexiveClosure();
			z.$setArgument(x);
			return z;
		}
	}

	static public class Tuple extends Expression {
		private java.util.List<org.meta_environment.rascal.ast.Expression> elements;

		/* "<" elements:{Expression ","}+ ">" -> Expression {cons("Tuple")} */
		private Tuple() {
		}

		/* package */Tuple(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Expression> elements) {
			this.tree = tree;
			this.elements = elements;
		}

		private void $setElements(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.elements = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionTuple(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() {
			return elements;
		}

		@Override
		public boolean hasElements() {
			return true;
		}

		@Override
		public boolean isTuple() {
			return true;
		}

		public Tuple setElements(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			final Tuple z = new Tuple();
			z.$setElements(x);
			return z;
		}
	}

	static public class TypedVariable extends Expression {
		private org.meta_environment.rascal.ast.Type type;
		private org.meta_environment.rascal.ast.Name name;

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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionTypedVariable(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean isTypedVariable() {
			return true;
		}

		public TypedVariable setName(org.meta_environment.rascal.ast.Name x) {
			final TypedVariable z = new TypedVariable();
			z.$setName(x);
			return z;
		}

		public TypedVariable setType(org.meta_environment.rascal.ast.Type x) {
			final TypedVariable z = new TypedVariable();
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitExpressionVisit(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Visit getVisit() {
			return visit;
		}

		@Override
		public boolean hasVisit() {
			return true;
		}

		@Override
		public boolean isVisit() {
			return true;
		}

		public Visit setVisit(org.meta_environment.rascal.ast.Visit x) {
			final Visit z = new Visit();
			z.$setVisit(x);
			return z;
		}
	}

	static public class VoidClosure extends Expression {
		private org.meta_environment.rascal.ast.Parameters parameters;
		private java.util.List<org.meta_environment.rascal.ast.Statement> statements;

		/*
		 * parameters:Parameters "{" statements:Statement+ "}" -> Expression
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

		@Override
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
			final VoidClosure z = new VoidClosure();
			z.$setParameters(x);
			return z;
		}

		public VoidClosure setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			final VoidClosure z = new VoidClosure();
			z.$setStatements(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Expression getAreaExpression() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getArgument() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Expression> getArguments() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getBeginColumn() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getBeginLine() {
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

	public org.meta_environment.rascal.ast.Expression getEndColumn() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getEndLine() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getExpression() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getField() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Field> getFields() {
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

	public org.meta_environment.rascal.ast.Expression getLength() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getLhs() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Literal getLiteral() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Mapping> getMappings() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getOffset() {
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

	public org.meta_environment.rascal.ast.Expression getRhs() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getSecond() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Expression> getSubscripts() {
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

	public boolean hasAreaExpression() {
		return false;
	}

	public boolean hasArgument() {
		return false;
	}

	public boolean hasArguments() {
		return false;
	}

	public boolean hasBeginColumn() {
		return false;
	}

	public boolean hasBeginLine() {
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

	public boolean hasEndColumn() {
		return false;
	}

	public boolean hasEndLine() {
		return false;
	}

	public boolean hasExpression() {
		return false;
	}

	public boolean hasField() {
		return false;
	}

	public boolean hasFields() {
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

	public boolean hasLength() {
		return false;
	}

	public boolean hasLhs() {
		return false;
	}

	public boolean hasLiteral() {
		return false;
	}

	public boolean hasMappings() {
		return false;
	}

	public boolean hasName() {
		return false;
	}

	public boolean hasOffset() {
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

	public boolean hasRhs() {
		return false;
	}

	public boolean hasSecond() {
		return false;
	}

	public boolean hasStatements() {
		return false;
	}

	public boolean hasSubscripts() {
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

	public boolean isFieldProject() {
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

	public boolean isSet() {
		return false;
	}

	public boolean isStepRange() {
		return false;
	}

	public boolean isSubscript() {
		return false;
	}

	public boolean isSubtraction() {
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