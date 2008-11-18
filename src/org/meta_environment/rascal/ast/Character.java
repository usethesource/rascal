package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Character extends AbstractAST {
	static public class Ambiguity extends Character {
		private final java.util.List<org.meta_environment.rascal.ast.Character> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Character> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Character> getAlternatives() {
			return alternatives;
		}
	}

	static public class Bottom extends Character {
		/* package */Bottom(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharacterBottom(this);
		}

		@Override
		public boolean isBottom() {
			return true;
		}
	}

	static public class EOF extends Character {
		/* package */EOF(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharacterEOF(this);
		}

		@Override
		public boolean isEOF() {
			return true;
		}
	}

	static public class LabelStart extends Character {
		/* package */LabelStart(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharacterLabelStart(this);
		}

		@Override
		public boolean isLabelStart() {
			return true;
		}
	}

	static public class Numeric extends Character {
		private org.meta_environment.rascal.ast.NumChar numChar;

		/* numChar:NumChar -> Character {cons("Numeric")} */
		private Numeric() {
		}

		/* package */Numeric(ITree tree,
				org.meta_environment.rascal.ast.NumChar numChar) {
			this.tree = tree;
			this.numChar = numChar;
		}

		private void $setNumChar(org.meta_environment.rascal.ast.NumChar x) {
			this.numChar = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharacterNumeric(this);
		}

		@Override
		public org.meta_environment.rascal.ast.NumChar getNumChar() {
			return numChar;
		}

		@Override
		public boolean hasNumChar() {
			return true;
		}

		@Override
		public boolean isNumeric() {
			return true;
		}

		public Numeric setNumChar(org.meta_environment.rascal.ast.NumChar x) {
			Numeric z = new Numeric();
			z.$setNumChar(x);
			return z;
		}
	}

	static public class Short extends Character {
		private org.meta_environment.rascal.ast.ShortChar shortChar;

		/* shortChar:ShortChar -> Character {cons("Short")} */
		private Short() {
		}

		/* package */Short(ITree tree,
				org.meta_environment.rascal.ast.ShortChar shortChar) {
			this.tree = tree;
			this.shortChar = shortChar;
		}

		private void $setShortChar(org.meta_environment.rascal.ast.ShortChar x) {
			this.shortChar = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharacterShort(this);
		}

		@Override
		public org.meta_environment.rascal.ast.ShortChar getShortChar() {
			return shortChar;
		}

		@Override
		public boolean hasShortChar() {
			return true;
		}

		@Override
		public boolean isShort() {
			return true;
		}

		public Short setShortChar(org.meta_environment.rascal.ast.ShortChar x) {
			Short z = new Short();
			z.$setShortChar(x);
			return z;
		}
	}

	static public class Top extends Character {
		/* package */Top(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharacterTop(this);
		}

		@Override
		public boolean isTop() {
			return true;
		}
	}

	public org.meta_environment.rascal.ast.NumChar getNumChar() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.ShortChar getShortChar() {
		throw new UnsupportedOperationException();
	}

	public boolean hasNumChar() {
		return false;
	}

	public boolean hasShortChar() {
		return false;
	}

	public boolean isBottom() {
		return false;
	}

	public boolean isEOF() {
		return false;
	}

	public boolean isLabelStart() {
		return false;
	}

	public boolean isNumeric() {
		return false;
	}

	public boolean isShort() {
		return false;
	}

	public boolean isTop() {
		return false;
	}
}
