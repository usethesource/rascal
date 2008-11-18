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

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitComprehensionList(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Generator> getGenerators() {
			return generators;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getResult() {
			return result;
		}

		@Override
		public boolean hasGenerators() {
			return true;
		}

		@Override
		public boolean hasResult() {
			return true;
		}

		@Override
		public boolean isList() {
			return true;
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

	static public class Map extends Comprehension {
		private org.meta_environment.rascal.ast.Expression from;
		private java.util.List<org.meta_environment.rascal.ast.Generator> generators;
		private org.meta_environment.rascal.ast.Expression to;

		/*
		 * "(" from:Expression ":" to:Expression "|" generators:{Generator ","}+
		 * ")" -> Comprehension {cons("Map")}
		 */
		private Map() {
		}

		/* package */Map(
				ITree tree,
				org.meta_environment.rascal.ast.Expression from,
				org.meta_environment.rascal.ast.Expression to,
				java.util.List<org.meta_environment.rascal.ast.Generator> generators) {
			this.tree = tree;
			this.from = from;
			this.to = to;
			this.generators = generators;
		}

		private void $setFrom(org.meta_environment.rascal.ast.Expression x) {
			this.from = x;
		}

		private void $setGenerators(
				java.util.List<org.meta_environment.rascal.ast.Generator> x) {
			this.generators = x;
		}

		private void $setTo(org.meta_environment.rascal.ast.Expression x) {
			this.to = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitComprehensionMap(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getFrom() {
			return from;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Generator> getGenerators() {
			return generators;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getTo() {
			return to;
		}

		@Override
		public boolean hasFrom() {
			return true;
		}

		@Override
		public boolean hasGenerators() {
			return true;
		}

		@Override
		public boolean hasTo() {
			return true;
		}

		@Override
		public boolean isMap() {
			return true;
		}

		public Map setFrom(org.meta_environment.rascal.ast.Expression x) {
			Map z = new Map();
			z.$setFrom(x);
			return z;
		}

		public Map setGenerators(
				java.util.List<org.meta_environment.rascal.ast.Generator> x) {
			Map z = new Map();
			z.$setGenerators(x);
			return z;
		}

		public Map setTo(org.meta_environment.rascal.ast.Expression x) {
			Map z = new Map();
			z.$setTo(x);
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

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitComprehensionSet(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Generator> getGenerators() {
			return generators;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getResult() {
			return result;
		}

		@Override
		public boolean hasGenerators() {
			return true;
		}

		@Override
		public boolean hasResult() {
			return true;
		}

		@Override
		public boolean isSet() {
			return true;
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

	public org.meta_environment.rascal.ast.Expression getFrom() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Generator> getGenerators() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getResult() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getTo() {
		throw new UnsupportedOperationException();
	}

	public boolean hasFrom() {
		return false;
	}

	public boolean hasGenerators() {
		return false;
	}

	public boolean hasResult() {
		return false;
	}

	public boolean hasTo() {
		return false;
	}

	public boolean isList() {
		return false;
	}

	public boolean isMap() {
		return false;
	}

	public boolean isSet() {
		return false;
	}
}
