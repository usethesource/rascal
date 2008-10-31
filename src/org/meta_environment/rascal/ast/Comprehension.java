package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Comprehension extends AbstractAST {
	static public class Ambiguity extends Comprehension {
		private final java.util.List<org.meta_environment.rascal.ast.Comprehension> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Comprehension> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Comprehension> getAlternatives() {
			return alternatives;
		}
	}

	static public class List extends Comprehension {
		private java.util.List<org.meta_environment.rascal.ast.Generator> generators;
		private org.meta_environment.rascal.ast.Expression result;

		/*
		 * "[" result:Expression "|" generators:{Generator ","}+ "]" ->
		 * Comprehension {cons("List")}
		 */
		private List() {
		}

		/* package */List(
				ITree tree,
				org.meta_environment.rascal.ast.Expression result,
				java.util.List<org.meta_environment.rascal.ast.Generator> generators) {
			this.tree = tree;
			this.result = result;
			this.generators = generators;
		}

		private void $setGenerators(
				java.util.List<org.meta_environment.rascal.ast.Generator> x) {
			this.generators = x;
		}

		private void $setResult(org.meta_environment.rascal.ast.Expression x) {
			this.result = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitComprehensionList(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Generator> getGenerators() {
			return generators;
		}

		public org.meta_environment.rascal.ast.Expression getResult() {
			return result;
		}

		public List setGenerators(
				java.util.List<org.meta_environment.rascal.ast.Generator> x) {
			List z = new List();
			z.$setGenerators(x);
			return z;
		}

		public List setResult(org.meta_environment.rascal.ast.Expression x) {
			List z = new List();
			z.$setResult(x);
			return z;
		}
	}

	static public class Set extends Comprehension {
		private java.util.List<org.meta_environment.rascal.ast.Generator> generators;
		private org.meta_environment.rascal.ast.Expression result;

		/*
		 * "{" result:Expression "|" generators:{Generator ","}+ "}" ->
		 * Comprehension {cons("Set")}
		 */
		private Set() {
		}

		/* package */Set(
				ITree tree,
				org.meta_environment.rascal.ast.Expression result,
				java.util.List<org.meta_environment.rascal.ast.Generator> generators) {
			this.tree = tree;
			this.result = result;
			this.generators = generators;
		}

		private void $setGenerators(
				java.util.List<org.meta_environment.rascal.ast.Generator> x) {
			this.generators = x;
		}

		private void $setResult(org.meta_environment.rascal.ast.Expression x) {
			this.result = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitComprehensionSet(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Generator> getGenerators() {
			return generators;
		}

		public org.meta_environment.rascal.ast.Expression getResult() {
			return result;
		}

		public Set setGenerators(
				java.util.List<org.meta_environment.rascal.ast.Generator> x) {
			Set z = new Set();
			z.$setGenerators(x);
			return z;
		}

		public Set setResult(org.meta_environment.rascal.ast.Expression x) {
			Set z = new Set();
			z.$setResult(x);
			return z;
		}
	}
}
