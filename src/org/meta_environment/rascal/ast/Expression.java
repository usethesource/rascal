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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionAddition(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionAnd(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionAnnotation(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
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
		/* package */Area(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionArea(this);
		}
	}

	static public class AreaInFileLocation extends Expression {
		private org.meta_environment.rascal.ast.Expression area;
		private org.meta_environment.rascal.ast.Expression filename;

		/*
		 * "area-in-file" "(" filename:Expression "," area:Expression ")" ->
		 * Expression {cons("AreaInFileLocation")}
		 */
		private AreaInFileLocation() {
		}

		/* package */AreaInFileLocation(ITree tree,
				org.meta_environment.rascal.ast.Expression filename,
				org.meta_environment.rascal.ast.Expression area) {
			this.tree = tree;
			this.filename = filename;
			this.area = area;
		}

		private void $setArea(org.meta_environment.rascal.ast.Expression x) {
			this.area = x;
		}

		private void $setFilename(org.meta_environment.rascal.ast.Expression x) {
			this.filename = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionAreaInFileLocation(this);
		}

		public org.meta_environment.rascal.ast.Expression getArea() {
			return area;
		}

		public org.meta_environment.rascal.ast.Expression getFilename() {
			return filename;
		}

		public AreaInFileLocation setArea(
				org.meta_environment.rascal.ast.Expression x) {
			AreaInFileLocation z = new AreaInFileLocation();
			z.$setArea(x);
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
		private org.meta_environment.rascal.ast.Expression area;

		/* "area" "(" area:Expression ")" -> Expression {cons("AreaLocation")} */
		private AreaLocation() {
		}

		/* package */AreaLocation(ITree tree,
				org.meta_environment.rascal.ast.Expression area) {
			this.tree = tree;
			this.area = area;
		}

		private void $setArea(org.meta_environment.rascal.ast.Expression x) {
			this.area = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionAreaLocation(this);
		}

		public org.meta_environment.rascal.ast.Expression getArea() {
			return area;
		}

		public AreaLocation setArea(org.meta_environment.rascal.ast.Expression x) {
			AreaLocation z = new AreaLocation();
			z.$setArea(x);
			return z;
		}
	}

	static public class Bracket extends Expression {
		/* package */Bracket(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionBracket(this);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionCallOrTree(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getArguments() {
			return arguments;
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
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
		private java.util.List<org.meta_environment.rascal.ast.Statement> statements;
		private org.meta_environment.rascal.ast.Type type;

		/*
		 * "fun" type:Type Parameters "{" statements:Statement "}" -> Expression
		 * {cons("Closure")}
		 */
		private Closure() {
		}

		/* package */Closure(
				ITree tree,
				org.meta_environment.rascal.ast.Type type,
				java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
			this.tree = tree;
			this.type = type;
			this.statements = statements;
		}

		private void $setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			this.statements = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionClosure(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() {
			return statements;
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
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
		private org.meta_environment.rascal.ast.Expression closure;

		/*
		 * "(" closure:Expression ")" "(" arguments:{Expression ","} ")" ->
		 * Expression {cons("ClosureCall")}
		 */
		private ClosureCall() {
		}

		/* package */ClosureCall(
				ITree tree,
				org.meta_environment.rascal.ast.Expression closure,
				java.util.List<org.meta_environment.rascal.ast.Expression> arguments) {
			this.tree = tree;
			this.closure = closure;
			this.arguments = arguments;
		}

		private void $setArguments(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.arguments = x;
		}

		private void $setClosure(org.meta_environment.rascal.ast.Expression x) {
			this.closure = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionClosureCall(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getArguments() {
			return arguments;
		}

		public org.meta_environment.rascal.ast.Expression getClosure() {
			return closure;
		}

		public ClosureCall setArguments(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			ClosureCall z = new ClosureCall();
			z.$setArguments(x);
			return z;
		}

		public ClosureCall setClosure(
				org.meta_environment.rascal.ast.Expression x) {
			ClosureCall z = new ClosureCall();
			z.$setClosure(x);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionComprehension(this);
		}

		public org.meta_environment.rascal.ast.Comprehension getComprehension() {
			return comprehension;
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
		 * non-assoc}
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionDivision(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionEquals(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionExists(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.ValueProducer getProducer() {
			return producer;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionFieldAccess(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.Name getField() {
			return field;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionFieldUpdate(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.Name getKey() {
			return key;
		}

		public org.meta_environment.rascal.ast.Expression getReplacement() {
			return replacement;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionFileLocation(this);
		}

		public org.meta_environment.rascal.ast.Expression getFilename() {
			return filename;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionForAll(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.ValueProducer getProducer() {
			return producer;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionGreaterThan(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionGreaterThanOrEq(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionIfDefined(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionIn(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionIntersection(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionLessThan(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionLessThanOrEq(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionList(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() {
			return elements;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionLiteral(this);
		}

		public org.meta_environment.rascal.ast.Literal getLiteral() {
			return literal;
		}

		public Literal setLiteral(org.meta_environment.rascal.ast.Literal x) {
			Literal z = new Literal();
			z.$setLiteral(x);
			return z;
		}
	}

	static public class Location extends Expression {
		/* package */Location(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionLocation(this);
		}
	}

	static public class MapTuple extends Expression {
		private org.meta_environment.rascal.ast.Expression from;
		private org.meta_environment.rascal.ast.Expression to;

		/*
		 * "<" from:Expression "->" to:Expression ">" -> Expression
		 * {cons("MapTuple")}
		 */
		private MapTuple() {
		}

		/* package */MapTuple(ITree tree,
				org.meta_environment.rascal.ast.Expression from,
				org.meta_environment.rascal.ast.Expression to) {
			this.tree = tree;
			this.from = from;
			this.to = to;
		}

		private void $setFrom(org.meta_environment.rascal.ast.Expression x) {
			this.from = x;
		}

		private void $setTo(org.meta_environment.rascal.ast.Expression x) {
			this.to = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionMapTuple(this);
		}

		public org.meta_environment.rascal.ast.Expression getFrom() {
			return from;
		}

		public org.meta_environment.rascal.ast.Expression getTo() {
			return to;
		}

		public MapTuple setFrom(org.meta_environment.rascal.ast.Expression x) {
			MapTuple z = new MapTuple();
			z.$setFrom(x);
			return z;
		}

		public MapTuple setTo(org.meta_environment.rascal.ast.Expression x) {
			MapTuple z = new MapTuple();
			z.$setTo(x);
			return z;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionMatch(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionNegation(this);
		}

		public org.meta_environment.rascal.ast.Expression getArgument() {
			return argument;
		}

		public Negation setArgument(org.meta_environment.rascal.ast.Expression x) {
			Negation z = new Negation();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionNoMatch(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionNonEquals(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionNotIn(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

	static public class Operator extends Expression {
		private org.meta_environment.rascal.ast.StandardOperator operator;

		/* operator:StandardOperator -> Expression {cons("Operator")} */
		private Operator() {
		}

		/* package */Operator(ITree tree,
				org.meta_environment.rascal.ast.StandardOperator operator) {
			this.tree = tree;
			this.operator = operator;
		}

		private void $setOperator(
				org.meta_environment.rascal.ast.StandardOperator x) {
			this.operator = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionOperator(this);
		}

		public org.meta_environment.rascal.ast.StandardOperator getOperator() {
			return operator;
		}

		public Operator setOperator(
				org.meta_environment.rascal.ast.StandardOperator x) {
			Operator z = new Operator();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionOr(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionProduct(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionQualifiedName(this);
		}

		public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() {
			return qualifiedName;
		}

		public QualifiedName setQualifiedName(
				org.meta_environment.rascal.ast.QualifiedName x) {
			QualifiedName z = new QualifiedName();
			z.$setQualifiedName(x);
			return z;
		}
	}

	static public class Range extends Expression {
		private org.meta_environment.rascal.ast.Expression from;
		private org.meta_environment.rascal.ast.Expression to;

		/*
		 * "[" from:Expression ".." to:Expression "]" -> Expression
		 * {cons("Range")}
		 */
		private Range() {
		}

		/* package */Range(ITree tree,
				org.meta_environment.rascal.ast.Expression from,
				org.meta_environment.rascal.ast.Expression to) {
			this.tree = tree;
			this.from = from;
			this.to = to;
		}

		private void $setFrom(org.meta_environment.rascal.ast.Expression x) {
			this.from = x;
		}

		private void $setTo(org.meta_environment.rascal.ast.Expression x) {
			this.to = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionRange(this);
		}

		public org.meta_environment.rascal.ast.Expression getFrom() {
			return from;
		}

		public org.meta_environment.rascal.ast.Expression getTo() {
			return to;
		}

		public Range setFrom(org.meta_environment.rascal.ast.Expression x) {
			Range z = new Range();
			z.$setFrom(x);
			return z;
		}

		public Range setTo(org.meta_environment.rascal.ast.Expression x) {
			Range z = new Range();
			z.$setTo(x);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionRegExpMatch(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionRegExpNoMatch(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionSet(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getElements() {
			return elements;
		}

		public Set setElements(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			Set z = new Set();
			z.$setElements(x);
			return z;
		}
	}

	static public class StepRange extends Expression {
		private org.meta_environment.rascal.ast.Expression by;
		private org.meta_environment.rascal.ast.Expression from;
		private org.meta_environment.rascal.ast.Expression to;

		/*
		 * "[" from:Expression "," by:Expression ",.." to:Expression "]" ->
		 * Expression {cons("StepRange")}
		 */
		private StepRange() {
		}

		/* package */StepRange(ITree tree,
				org.meta_environment.rascal.ast.Expression from,
				org.meta_environment.rascal.ast.Expression by,
				org.meta_environment.rascal.ast.Expression to) {
			this.tree = tree;
			this.from = from;
			this.by = by;
			this.to = to;
		}

		private void $setBy(org.meta_environment.rascal.ast.Expression x) {
			this.by = x;
		}

		private void $setFrom(org.meta_environment.rascal.ast.Expression x) {
			this.from = x;
		}

		private void $setTo(org.meta_environment.rascal.ast.Expression x) {
			this.to = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionStepRange(this);
		}

		public org.meta_environment.rascal.ast.Expression getBy() {
			return by;
		}

		public org.meta_environment.rascal.ast.Expression getFrom() {
			return from;
		}

		public org.meta_environment.rascal.ast.Expression getTo() {
			return to;
		}

		public StepRange setBy(org.meta_environment.rascal.ast.Expression x) {
			StepRange z = new StepRange();
			z.$setBy(x);
			return z;
		}

		public StepRange setFrom(org.meta_environment.rascal.ast.Expression x) {
			StepRange z = new StepRange();
			z.$setFrom(x);
			return z;
		}

		public StepRange setTo(org.meta_environment.rascal.ast.Expression x) {
			StepRange z = new StepRange();
			z.$setTo(x);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionSubscript(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public org.meta_environment.rascal.ast.Expression getSubscript() {
			return subscript;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionSubstraction(this);
		}

		public org.meta_environment.rascal.ast.Expression getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Expression getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionTransitiveClosure(this);
		}

		public org.meta_environment.rascal.ast.Expression getArgument() {
			return argument;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionTransitiveReflexiveClosure(this);
		}

		public org.meta_environment.rascal.ast.Expression getArgument() {
			return argument;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionTuple(this);
		}

		public org.meta_environment.rascal.ast.Expression getFirst() {
			return first;
		}

		public java.util.List<org.meta_environment.rascal.ast.Expression> getRest() {
			return rest;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionTypedVariable(this);
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitExpressionVisit(this);
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
}
