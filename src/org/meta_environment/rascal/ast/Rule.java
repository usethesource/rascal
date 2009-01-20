package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Rule extends AbstractAST {
	static public class Ambiguity extends Rule {
		private final java.util.List<org.meta_environment.rascal.ast.Rule> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Rule> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitRuleAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Rule> getAlternatives() {
			return alternatives;
		}
	}

	static public class Arbitrary extends Rule {
		private org.meta_environment.rascal.ast.Expression pattern;
		private org.meta_environment.rascal.ast.Statement statement;

		/*
		 * pattern:Expression ":" statement:Statement -> Rule
		 * {cons("Arbitrary")}
		 */
		private Arbitrary() {
		}

		/* package */Arbitrary(ITree tree,
				org.meta_environment.rascal.ast.Expression pattern,
				org.meta_environment.rascal.ast.Statement statement) {
			this.tree = tree;
			this.pattern = pattern;
			this.statement = statement;
		}

		private void $setPattern(org.meta_environment.rascal.ast.Expression x) {
			this.pattern = x;
		}

		private void $setStatement(org.meta_environment.rascal.ast.Statement x) {
			this.statement = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitRuleArbitrary(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
		}

		@Override
		public org.meta_environment.rascal.ast.Statement getStatement() {
			return statement;
		}

		@Override
		public boolean hasPattern() {
			return true;
		}

		@Override
		public boolean hasStatement() {
			return true;
		}

		@Override
		public boolean isArbitrary() {
			return true;
		}

		public Arbitrary setPattern(org.meta_environment.rascal.ast.Expression x) {
			final Arbitrary z = new Arbitrary();
			z.$setPattern(x);
			return z;
		}

		public Arbitrary setStatement(
				org.meta_environment.rascal.ast.Statement x) {
			final Arbitrary z = new Arbitrary();
			z.$setStatement(x);
			return z;
		}
	}

	static public class Guarded extends Rule {
		private org.meta_environment.rascal.ast.Type type;
		private org.meta_environment.rascal.ast.Rule rule;

		/* "[" type:Type "]" rule:Rule -> Rule {non-assoc, cons("Guarded")} */
		private Guarded() {
		}

		/* package */Guarded(ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Rule rule) {
			this.tree = tree;
			this.type = type;
			this.rule = rule;
		}

		private void $setRule(org.meta_environment.rascal.ast.Rule x) {
			this.rule = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitRuleGuarded(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Rule getRule() {
			return rule;
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public boolean hasRule() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean isGuarded() {
			return true;
		}

		public Guarded setRule(org.meta_environment.rascal.ast.Rule x) {
			final Guarded z = new Guarded();
			z.$setRule(x);
			return z;
		}

		public Guarded setType(org.meta_environment.rascal.ast.Type x) {
			final Guarded z = new Guarded();
			z.$setType(x);
			return z;
		}
	}

	static public class Replacing extends Rule {
		private org.meta_environment.rascal.ast.Expression pattern;
		private org.meta_environment.rascal.ast.Replacement replacement;

		/*
		 * pattern:Expression "=>" replacement:Replacement -> Rule
		 * {cons("Replacing")}
		 */
		private Replacing() {
		}

		/* package */Replacing(ITree tree,
				org.meta_environment.rascal.ast.Expression pattern,
				org.meta_environment.rascal.ast.Replacement replacement) {
			this.tree = tree;
			this.pattern = pattern;
			this.replacement = replacement;
		}

		private void $setPattern(org.meta_environment.rascal.ast.Expression x) {
			this.pattern = x;
		}

		private void $setReplacement(
				org.meta_environment.rascal.ast.Replacement x) {
			this.replacement = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitRuleReplacing(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
		}

		@Override
		public org.meta_environment.rascal.ast.Replacement getReplacement() {
			return replacement;
		}

		@Override
		public boolean hasPattern() {
			return true;
		}

		@Override
		public boolean hasReplacement() {
			return true;
		}

		@Override
		public boolean isReplacing() {
			return true;
		}

		public Replacing setPattern(org.meta_environment.rascal.ast.Expression x) {
			final Replacing z = new Replacing();
			z.$setPattern(x);
			return z;
		}

		public Replacing setReplacement(
				org.meta_environment.rascal.ast.Replacement x) {
			final Replacing z = new Replacing();
			z.$setReplacement(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Expression getPattern() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Replacement getReplacement() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Rule getRule() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Statement getStatement() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public boolean hasPattern() {
		return false;
	}

	public boolean hasReplacement() {
		return false;
	}

	public boolean hasRule() {
		return false;
	}

	public boolean hasStatement() {
		return false;
	}

	public boolean hasType() {
		return false;
	}

	public boolean isArbitrary() {
		return false;
	}

	public boolean isGuarded() {
		return false;
	}

	public boolean isReplacing() {
		return false;
	}
}