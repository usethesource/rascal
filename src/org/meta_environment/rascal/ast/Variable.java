package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Variable extends AbstractAST {
	static public class Ambiguity extends Variable {
		private final java.util.List<org.meta_environment.rascal.ast.Variable> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Variable> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Variable> getAlternatives() {
			return alternatives;
		}
	}

	static public class GivenInitialization extends Variable {
		private org.meta_environment.rascal.ast.Expression initial;
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Tags tags;

		/*
		 * name:Name tags:Tags "=" initial:Expression -> Variable
		 * {cons("GivenInitialization")}
		 */
		private GivenInitialization() {
		}

		/* package */GivenInitialization(ITree tree,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Tags tags,
				org.meta_environment.rascal.ast.Expression initial) {
			this.tree = tree;
			this.name = name;
			this.tags = tags;
			this.initial = initial;
		}

		private void $setInitial(org.meta_environment.rascal.ast.Expression x) {
			this.initial = x;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitVariableGivenInitialization(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getInitial() {
			return initial;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public GivenInitialization setInitial(
				org.meta_environment.rascal.ast.Expression x) {
			GivenInitialization z = new GivenInitialization();
			z.$setInitial(x);
			return z;
		}

		public GivenInitialization setName(
				org.meta_environment.rascal.ast.Name x) {
			GivenInitialization z = new GivenInitialization();
			z.$setName(x);
			return z;
		}

		public GivenInitialization setTags(
				org.meta_environment.rascal.ast.Tags x) {
			GivenInitialization z = new GivenInitialization();
			z.$setTags(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Expression getInitial() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Tags getTags() {
		throw new UnsupportedOperationException();
	}
}
