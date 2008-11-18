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

	static public class Initialized extends Variable {
		private org.meta_environment.rascal.ast.Expression initial;
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Tags tags;

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
			Initialized z = new Initialized();
			z.$setInitial(x);
			return z;
		}

		public Initialized setName(org.meta_environment.rascal.ast.Name x) {
			Initialized z = new Initialized();
			z.$setName(x);
			return z;
		}

		public Initialized setTags(org.meta_environment.rascal.ast.Tags x) {
			Initialized z = new Initialized();
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
			UnInitialized z = new UnInitialized();
			z.$setName(x);
			return z;
		}

		public UnInitialized setTags(org.meta_environment.rascal.ast.Tags x) {
			UnInitialized z = new UnInitialized();
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
