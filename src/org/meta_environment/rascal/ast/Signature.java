package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Signature extends AbstractAST {
	static public class Ambiguity extends Signature {
		private final java.util.List<org.meta_environment.rascal.ast.Signature> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Signature> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Signature> getAlternatives() {
			return alternatives;
		}
	}

	static public class NoThrows extends Signature {
		private org.meta_environment.rascal.ast.FunctionModifiers modifiers;
		private org.meta_environment.rascal.ast.FunctionName name;
		private org.meta_environment.rascal.ast.Parameters parameters;
		private org.meta_environment.rascal.ast.Type type;

		/*
		 * type:Type modifiers:FunctionModifiers name:FunctionName
		 * parameters:Parameters -> Signature {cons("NoThrows")}
		 */
		private NoThrows() {
		}

		/* package */NoThrows(ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.FunctionModifiers modifiers,
				org.meta_environment.rascal.ast.FunctionName name,
				org.meta_environment.rascal.ast.Parameters parameters) {
			this.tree = tree;
			this.type = type;
			this.modifiers = modifiers;
			this.name = name;
			this.parameters = parameters;
		}

		private void $setModifiers(
				org.meta_environment.rascal.ast.FunctionModifiers x) {
			this.modifiers = x;
		}

		private void $setName(org.meta_environment.rascal.ast.FunctionName x) {
			this.name = x;
		}

		private void $setParameters(org.meta_environment.rascal.ast.Parameters x) {
			this.parameters = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSignatureNoThrows(this);
		}

		public org.meta_environment.rascal.ast.FunctionModifiers getModifiers() {
			return modifiers;
		}

		public org.meta_environment.rascal.ast.FunctionName getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.Parameters getParameters() {
			return parameters;
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public NoThrows setModifiers(
				org.meta_environment.rascal.ast.FunctionModifiers x) {
			NoThrows z = new NoThrows();
			z.$setModifiers(x);
			return z;
		}

		public NoThrows setName(org.meta_environment.rascal.ast.FunctionName x) {
			NoThrows z = new NoThrows();
			z.$setName(x);
			return z;
		}

		public NoThrows setParameters(
				org.meta_environment.rascal.ast.Parameters x) {
			NoThrows z = new NoThrows();
			z.$setParameters(x);
			return z;
		}

		public NoThrows setType(org.meta_environment.rascal.ast.Type x) {
			NoThrows z = new NoThrows();
			z.$setType(x);
			return z;
		}
	}

	static public class WithThrows extends Signature {
		private java.util.List<org.meta_environment.rascal.ast.Type> exceptions;
		private org.meta_environment.rascal.ast.FunctionModifiers modifiers;
		private org.meta_environment.rascal.ast.FunctionName name;
		private org.meta_environment.rascal.ast.Parameters parameters;
		private org.meta_environment.rascal.ast.Type type;

		/*
		 * type:Type modifiers:FunctionModifiers name:FunctionName
		 * parameters:Parameters "throws" exceptions:{Type ","}+ -> Signature
		 * {cons("WithThrows")}
		 */
		private WithThrows() {
		}

		/* package */WithThrows(ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.FunctionModifiers modifiers,
				org.meta_environment.rascal.ast.FunctionName name,
				org.meta_environment.rascal.ast.Parameters parameters,
				java.util.List<org.meta_environment.rascal.ast.Type> exceptions) {
			this.tree = tree;
			this.type = type;
			this.modifiers = modifiers;
			this.name = name;
			this.parameters = parameters;
			this.exceptions = exceptions;
		}

		private void $setExceptions(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			this.exceptions = x;
		}

		private void $setModifiers(
				org.meta_environment.rascal.ast.FunctionModifiers x) {
			this.modifiers = x;
		}

		private void $setName(org.meta_environment.rascal.ast.FunctionName x) {
			this.name = x;
		}

		private void $setParameters(org.meta_environment.rascal.ast.Parameters x) {
			this.parameters = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSignatureWithThrows(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Type> getExceptions() {
			return exceptions;
		}

		public org.meta_environment.rascal.ast.FunctionModifiers getModifiers() {
			return modifiers;
		}

		public org.meta_environment.rascal.ast.FunctionName getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.Parameters getParameters() {
			return parameters;
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public WithThrows setExceptions(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			WithThrows z = new WithThrows();
			z.$setExceptions(x);
			return z;
		}

		public WithThrows setModifiers(
				org.meta_environment.rascal.ast.FunctionModifiers x) {
			WithThrows z = new WithThrows();
			z.$setModifiers(x);
			return z;
		}

		public WithThrows setName(org.meta_environment.rascal.ast.FunctionName x) {
			WithThrows z = new WithThrows();
			z.$setName(x);
			return z;
		}

		public WithThrows setParameters(
				org.meta_environment.rascal.ast.Parameters x) {
			WithThrows z = new WithThrows();
			z.$setParameters(x);
			return z;
		}

		public WithThrows setType(org.meta_environment.rascal.ast.Type x) {
			WithThrows z = new WithThrows();
			z.$setType(x);
			return z;
		}
	}
}
