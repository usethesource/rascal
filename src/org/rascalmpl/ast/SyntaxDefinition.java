package org.rascalmpl.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class SyntaxDefinition extends AbstractAST {
	public org.rascalmpl.ast.Start getStart() {
		throw new UnsupportedOperationException();
	}

	public org.rascalmpl.ast.UserType getUser() {
		throw new UnsupportedOperationException();
	}

	public org.rascalmpl.ast.Prod getProduction() {
		throw new UnsupportedOperationException();
	}

	
	public boolean hasStart() {
		return false;
	}

	public boolean hasUser() {
		return false;
	}

	public boolean isLanguage() {
		return false;
	}
	
	public boolean hasProduction() {
		return false;
	}

	static public class Language extends SyntaxDefinition {
		private final Prod production;

		/**
		 * start:Start "syntax" user:UserType "=" Prod ";" -> SyntaxDefinition
		 * {cons("Language")}
		 */
		public Language(INode node, org.rascalmpl.ast.Start start,
				org.rascalmpl.ast.UserType user,
				org.rascalmpl.ast.Prod production) {
			this.node = node;
			this.start = start;
			this.user = user;
			this.production = production;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSyntaxDefinitionLanguage(this);
		}

		@Override
		public boolean isLanguage() {
			return true;
		}

		@Override
		public boolean hasStart() {
			return true;
		}
		
		@Override
		public boolean hasProduction() {
			return true;
		}

		@Override
		public boolean hasUser() {
			return true;
		}

		private final org.rascalmpl.ast.Start start;

		@Override
		public org.rascalmpl.ast.Start getStart() {
			return start;
		}

		private final org.rascalmpl.ast.UserType user;

		@Override
		public org.rascalmpl.ast.UserType getUser() {
			return user;
		}
		
		public Prod getProduction() {
			return production;
		}
	}

	static public class Ambiguity extends SyntaxDefinition {
		private final java.util.List<org.rascalmpl.ast.SyntaxDefinition> alternatives;

		public Ambiguity(INode node,
				java.util.List<org.rascalmpl.ast.SyntaxDefinition> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.rascalmpl.ast.SyntaxDefinition> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitSyntaxDefinitionAmbiguity(this);
		}
	}

	public boolean isLayout() {
		return false;
	}

	static public class Layout extends SyntaxDefinition {
		private final Prod production;

		/**
		 * "layout" user:UserType "=" Prod ";" -> SyntaxDefinition
		 * {cons("Layout")}
		 */
		public Layout(INode node, org.rascalmpl.ast.UserType user, org.rascalmpl.ast.Prod production) {
			this.node = node;
			this.user = user;
			this.production = production;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSyntaxDefinitionLayout(this);
		}

		@Override
		public boolean isLayout() {
			return true;
		}

		@Override
		public boolean hasUser() {
			return true;
		}
		
		@Override
		public boolean hasProduction() {
			return true;
		}
		
		public Prod getProduction() {
			return production;
		}

		private final org.rascalmpl.ast.UserType user;

		@Override
		public org.rascalmpl.ast.UserType getUser() {
			return user;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}