package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Variable extends AbstractAST {
	static public class Ambiguity extends Variable {
		private final java.util.List<org.meta_environment.rascal.ast.Variable> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Variable> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitVariableAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Variable> getAlternatives() {
			return alternatives;
		}
	}

	static public class Initialized extends Variable {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Tags tags;
		private org.meta_environment.rascal.ast.Expression initial;

		/*
		 * name:Name tags:Tags "=" initial:Expression -> Variable
		 * {cons("Initialized")}
		 */
		private Initialized() {
		}

		/* package */Initialized(ITree tree,
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitVariableInitialized(this);
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

		@Override
		public boolean hasInitial() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean isInitialized() {
			return true;
		}

		public Initialized setInitial(
				org.meta_environment.rascal.ast.Expression x) {
			final Initialized z = new Initialized();
			z.$setInitial(x);
			return z;
		}

		public Initialized setName(org.meta_environment.rascal.ast.Name x) {
			final Initialized z = new Initialized();
			z.$setName(x);
			return z;
		}

		public Initialized setTags(org.meta_environment.rascal.ast.Tags x) {
			final Initialized z = new Initialized();
			z.$setTags(x);
			return z;
		}
	}

	static public class UnInitialized extends Variable {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Tags tags;

		/* name:Name tags:Tags -> Variable {cons("UnInitialized")} */
		private UnInitialized() {
		}

		/* package */UnInitialized(ITree tree,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Tags tags) {
			this.tree = tree;
			this.name = name;
			this.tags = tags;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitVariableUnInitialized(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean isUnInitialized() {
			return true;
		}

		public UnInitialized setName(org.meta_environment.rascal.ast.Name x) {
			final UnInitialized z = new UnInitialized();
			z.$setName(x);
			return z;
		}

		public UnInitialized setTags(org.meta_environment.rascal.ast.Tags x) {
			final UnInitialized z = new UnInitialized();
			z.$setTags(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Expression getInitial() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Tags getTags() {
		throw new UnsupportedOperationException();
	}

	public boolean hasInitial() {
		return false;
	}

	public boolean hasName() {
		return false;
	}

	public boolean hasTags() {
		return false;
	}

	public boolean isInitialized() {
		return false;
	}

	public boolean isUnInitialized() {
		return false;
	}
}