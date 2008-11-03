package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Assignable extends AbstractAST {
	static public class Ambiguity extends Assignable {
		private final java.util.List<org.meta_environment.rascal.ast.Assignable> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Assignable> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Assignable> getAlternatives() {
			return alternatives;
		}
	}

	static public class Annotation extends Assignable {
		private org.meta_environment.rascal.ast.Expression annotation;
		private org.meta_environment.rascal.ast.Assignable receiver;

		/*
		 * receiver:Assignable "@" annotation:Expression -> Assignable
		 * {cons("Annotation")}
		 */
		private Annotation() {
		}

		/* package */Annotation(ITree tree,
				org.meta_environment.rascal.ast.Assignable receiver,
				org.meta_environment.rascal.ast.Expression annotation) {
			this.tree = tree;
			this.receiver = receiver;
			this.annotation = annotation;
		}

		private void $setAnnotation(org.meta_environment.rascal.ast.Expression x) {
			this.annotation = x;
		}

		private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) {
			this.receiver = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitAssignableAnnotation(this);
		}

		public org.meta_environment.rascal.ast.Expression getAnnotation() {
			return annotation;
		}

		public org.meta_environment.rascal.ast.Assignable getReceiver() {
			return receiver;
		}

		public Annotation setAnnotation(
				org.meta_environment.rascal.ast.Expression x) {
			Annotation z = new Annotation();
			z.$setAnnotation(x);
			return z;
		}

		public Annotation setReceiver(
				org.meta_environment.rascal.ast.Assignable x) {
			Annotation z = new Annotation();
			z.$setReceiver(x);
			return z;
		}
	}

	static public class Constructor extends Assignable {
		private java.util.List<org.meta_environment.rascal.ast.Assignable> arguments;
		private org.meta_environment.rascal.ast.Name name;

		/*
		 * name:Name "(" arguments:{Assignable ","}+ ")" -> Assignable
		 * {cons("Constructor")}
		 */
		private Constructor() {
		}

		/* package */Constructor(
				ITree tree,
				org.meta_environment.rascal.ast.Name name,
				java.util.List<org.meta_environment.rascal.ast.Assignable> arguments) {
			this.tree = tree;
			this.name = name;
			this.arguments = arguments;
		}

		private void $setArguments(
				java.util.List<org.meta_environment.rascal.ast.Assignable> x) {
			this.arguments = x;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitAssignableConstructor(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Assignable> getArguments() {
			return arguments;
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public Constructor setArguments(
				java.util.List<org.meta_environment.rascal.ast.Assignable> x) {
			Constructor z = new Constructor();
			z.$setArguments(x);
			return z;
		}

		public Constructor setName(org.meta_environment.rascal.ast.Name x) {
			Constructor z = new Constructor();
			z.$setName(x);
			return z;
		}
	}

	static public class FieldAccess extends Assignable {
		private org.meta_environment.rascal.ast.Name field;
		private org.meta_environment.rascal.ast.Assignable receiver;

		/*
		 * receiver:Assignable "." field:Name -> Assignable
		 * {cons("FieldAccess")}
		 */
		private FieldAccess() {
		}

		/* package */FieldAccess(ITree tree,
				org.meta_environment.rascal.ast.Assignable receiver,
				org.meta_environment.rascal.ast.Name field) {
			this.tree = tree;
			this.receiver = receiver;
			this.field = field;
		}

		private void $setField(org.meta_environment.rascal.ast.Name x) {
			this.field = x;
		}

		private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) {
			this.receiver = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitAssignableFieldAccess(this);
		}

		public org.meta_environment.rascal.ast.Name getField() {
			return field;
		}

		public org.meta_environment.rascal.ast.Assignable getReceiver() {
			return receiver;
		}

		public FieldAccess setField(org.meta_environment.rascal.ast.Name x) {
			FieldAccess z = new FieldAccess();
			z.$setField(x);
			return z;
		}

		public FieldAccess setReceiver(
				org.meta_environment.rascal.ast.Assignable x) {
			FieldAccess z = new FieldAccess();
			z.$setReceiver(x);
			return z;
		}
	}

	static public class IfDefined extends Assignable {
		private org.meta_environment.rascal.ast.Expression condition;
		private org.meta_environment.rascal.ast.Assignable receiver;

		/*
		 * receiver:Assignable "?" condition:Expression -> Assignable
		 * {cons("IfDefined")}
		 */
		private IfDefined() {
		}

		/* package */IfDefined(ITree tree,
				org.meta_environment.rascal.ast.Assignable receiver,
				org.meta_environment.rascal.ast.Expression condition) {
			this.tree = tree;
			this.receiver = receiver;
			this.condition = condition;
		}

		private void $setCondition(org.meta_environment.rascal.ast.Expression x) {
			this.condition = x;
		}

		private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) {
			this.receiver = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitAssignableIfDefined(this);
		}

		public org.meta_environment.rascal.ast.Expression getCondition() {
			return condition;
		}

		public org.meta_environment.rascal.ast.Assignable getReceiver() {
			return receiver;
		}

		public IfDefined setCondition(
				org.meta_environment.rascal.ast.Expression x) {
			IfDefined z = new IfDefined();
			z.$setCondition(x);
			return z;
		}

		public IfDefined setReceiver(
				org.meta_environment.rascal.ast.Assignable x) {
			IfDefined z = new IfDefined();
			z.$setReceiver(x);
			return z;
		}
	}

	static public class Subscript extends Assignable {
		private org.meta_environment.rascal.ast.Assignable receiver;
		private org.meta_environment.rascal.ast.Expression subscript;

		/*
		 * receiver:Assignable "[" subscript:Expression "]" -> Assignable
		 * {cons("Subscript")}
		 */
		private Subscript() {
		}

		/* package */Subscript(ITree tree,
				org.meta_environment.rascal.ast.Assignable receiver,
				org.meta_environment.rascal.ast.Expression subscript) {
			this.tree = tree;
			this.receiver = receiver;
			this.subscript = subscript;
		}

		private void $setReceiver(org.meta_environment.rascal.ast.Assignable x) {
			this.receiver = x;
		}

		private void $setSubscript(org.meta_environment.rascal.ast.Expression x) {
			this.subscript = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitAssignableSubscript(this);
		}

		public org.meta_environment.rascal.ast.Assignable getReceiver() {
			return receiver;
		}

		public org.meta_environment.rascal.ast.Expression getSubscript() {
			return subscript;
		}

		public Subscript setReceiver(
				org.meta_environment.rascal.ast.Assignable x) {
			Subscript z = new Subscript();
			z.$setReceiver(x);
			return z;
		}

		public Subscript setSubscript(
				org.meta_environment.rascal.ast.Expression x) {
			Subscript z = new Subscript();
			z.$setSubscript(x);
			return z;
		}
	}

	static public class Tuple extends Assignable {
		private org.meta_environment.rascal.ast.Assignable first;
		private java.util.List<org.meta_environment.rascal.ast.Assignable> rest;

		/*
		 * "<" first:Assignable "," rest:{Assignable ","}+ ">" -> Assignable
		 * {cons("Tuple")}
		 */
		private Tuple() {
		}

		/* package */Tuple(ITree tree,
				org.meta_environment.rascal.ast.Assignable first,
				java.util.List<org.meta_environment.rascal.ast.Assignable> rest) {
			this.tree = tree;
			this.first = first;
			this.rest = rest;
		}

		private void $setFirst(org.meta_environment.rascal.ast.Assignable x) {
			this.first = x;
		}

		private void $setRest(
				java.util.List<org.meta_environment.rascal.ast.Assignable> x) {
			this.rest = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitAssignableTuple(this);
		}

		public org.meta_environment.rascal.ast.Assignable getFirst() {
			return first;
		}

		public java.util.List<org.meta_environment.rascal.ast.Assignable> getRest() {
			return rest;
		}

		public Tuple setFirst(org.meta_environment.rascal.ast.Assignable x) {
			Tuple z = new Tuple();
			z.$setFirst(x);
			return z;
		}

		public Tuple setRest(
				java.util.List<org.meta_environment.rascal.ast.Assignable> x) {
			Tuple z = new Tuple();
			z.$setRest(x);
			return z;
		}
	}

	static public class Variable extends Assignable {
		private org.meta_environment.rascal.ast.QualifiedName qualifiedName;

		/* qualifiedName:QualifiedName -> Assignable {cons("Variable")} */
		private Variable() {
		}

		/* package */Variable(ITree tree,
				org.meta_environment.rascal.ast.QualifiedName qualifiedName) {
			this.tree = tree;
			this.qualifiedName = qualifiedName;
		}

		private void $setQualifiedName(
				org.meta_environment.rascal.ast.QualifiedName x) {
			this.qualifiedName = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitAssignableVariable(this);
		}

		public org.meta_environment.rascal.ast.QualifiedName getQualifiedName() {
			return qualifiedName;
		}

		public Variable setQualifiedName(
				org.meta_environment.rascal.ast.QualifiedName x) {
			Variable z = new Variable();
			z.$setQualifiedName(x);
			return z;
		}
	}
}
