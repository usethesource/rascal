package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Rule extends AbstractAST {
	public org.meta_environment.rascal.ast.Expression getPattern() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Replacement getReplacement() {
		throw new UnsupportedOperationException();
	}

	public boolean hasPattern() {
		return false;
	}

	public boolean hasReplacement() {
		return false;
	}

	public boolean isReplacing() {
		return false;
	}

	static public class Replacing extends Rule {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Replacing() {
		}

		/* package */Replacing(INode node,
				org.meta_environment.rascal.ast.Expression pattern,
				org.meta_environment.rascal.ast.Replacement replacement) {
			this.node = node;
			this.pattern = pattern;
			this.replacement = replacement;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitRuleReplacing(this);
		}

		@Override
		public boolean isReplacing() {
			return true;
		}

		@Override
		public boolean hasPattern() {
			return true;
		}

		@Override
		public boolean hasReplacement() {
			return true;
		}

		private org.meta_environment.rascal.ast.Expression pattern;

		@Override
		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
		}

		private void $setPattern(org.meta_environment.rascal.ast.Expression x) {
			this.pattern = x;
		}

		public Replacing setPattern(org.meta_environment.rascal.ast.Expression x) {
			Replacing z = new Replacing();
			z.$setPattern(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Replacement replacement;

		@Override
		public org.meta_environment.rascal.ast.Replacement getReplacement() {
			return replacement;
		}

		private void $setReplacement(
				org.meta_environment.rascal.ast.Replacement x) {
			this.replacement = x;
		}

		public Replacing setReplacement(
				org.meta_environment.rascal.ast.Replacement x) {
			Replacing z = new Replacing();
			z.$setReplacement(x);
			return z;
		}
	}

	static public class Ambiguity extends Rule {
		private final java.util.List<org.meta_environment.rascal.ast.Rule> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Rule> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Rule> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitRuleAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.Statement getStatement() {
		throw new UnsupportedOperationException();
	}

	public boolean hasStatement() {
		return false;
	}

	public boolean isArbitrary() {
		return false;
	}

	static public class Arbitrary extends Rule {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Arbitrary() {
		}

		/* package */Arbitrary(INode node,
				org.meta_environment.rascal.ast.Expression pattern,
				org.meta_environment.rascal.ast.Statement statement) {
			this.node = node;
			this.pattern = pattern;
			this.statement = statement;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitRuleArbitrary(this);
		}

		@Override
		public boolean isArbitrary() {
			return true;
		}

		@Override
		public boolean hasPattern() {
			return true;
		}

		@Override
		public boolean hasStatement() {
			return true;
		}

		private org.meta_environment.rascal.ast.Expression pattern;

		@Override
		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
		}

		private void $setPattern(org.meta_environment.rascal.ast.Expression x) {
			this.pattern = x;
		}

		public Arbitrary setPattern(org.meta_environment.rascal.ast.Expression x) {
			Arbitrary z = new Arbitrary();
			z.$setPattern(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Statement statement;

		@Override
		public org.meta_environment.rascal.ast.Statement getStatement() {
			return statement;
		}

		private void $setStatement(org.meta_environment.rascal.ast.Statement x) {
			this.statement = x;
		}

		public Arbitrary setStatement(
				org.meta_environment.rascal.ast.Statement x) {
			Arbitrary z = new Arbitrary();
			z.$setStatement(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Rule getRule() {
		throw new UnsupportedOperationException();
	}

	public boolean hasType() {
		return false;
	}

	public boolean hasRule() {
		return false;
	}

	public boolean isGuarded() {
		return false;
	}

	static public class Guarded extends Rule {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Guarded() {
		}

		/* package */Guarded(INode node,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Rule rule) {
			this.node = node;
			this.type = type;
			this.rule = rule;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitRuleGuarded(this);
		}

		@Override
		public boolean isGuarded() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean hasRule() {
			return true;
		}

		private org.meta_environment.rascal.ast.Type type;

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public Guarded setType(org.meta_environment.rascal.ast.Type x) {
			Guarded z = new Guarded();
			z.$setType(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Rule rule;

		@Override
		public org.meta_environment.rascal.ast.Rule getRule() {
			return rule;
		}

		private void $setRule(org.meta_environment.rascal.ast.Rule x) {
			this.rule = x;
		}

		public Guarded setRule(org.meta_environment.rascal.ast.Rule x) {
			Guarded z = new Guarded();
			z.$setRule(x);
			return z;
		}
	}
}