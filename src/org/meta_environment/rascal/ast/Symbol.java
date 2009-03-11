package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Symbol extends AbstractAST {
	public boolean isEmpty() {
		return false;
	}

	static public class Empty extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Empty() {
		}

		/* package */Empty(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolEmpty(this);
		}

		@Override
		public boolean isEmpty() {
			return true;
		}
	}

	static public class Ambiguity extends Symbol {
		private final java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Symbol> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitSymbolAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.Symbol getHead() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Symbol> getTail() {
		throw new UnsupportedOperationException();
	}

	public boolean hasHead() {
		return false;
	}

	public boolean hasTail() {
		return false;
	}

	public boolean isSequence() {
		return false;
	}

	static public class Sequence extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Sequence() {
		}

		/* package */Sequence(INode node,
				org.meta_environment.rascal.ast.Symbol head,
				java.util.List<org.meta_environment.rascal.ast.Symbol> tail) {
			this.node = node;
			this.head = head;
			this.tail = tail;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolSequence(this);
		}

		@Override
		public boolean isSequence() {
			return true;
		}

		@Override
		public boolean hasHead() {
			return true;
		}

		@Override
		public boolean hasTail() {
			return true;
		}

		private org.meta_environment.rascal.ast.Symbol head;

		@Override
		public org.meta_environment.rascal.ast.Symbol getHead() {
			return head;
		}

		private void $setHead(org.meta_environment.rascal.ast.Symbol x) {
			this.head = x;
		}

		public Sequence setHead(org.meta_environment.rascal.ast.Symbol x) {
			Sequence z = new Sequence();
			z.$setHead(x);
			return z;
		}

		private java.util.List<org.meta_environment.rascal.ast.Symbol> tail;

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Symbol> getTail() {
			return tail;
		}

		private void $setTail(
				java.util.List<org.meta_environment.rascal.ast.Symbol> x) {
			this.tail = x;
		}

		public Sequence setTail(
				java.util.List<org.meta_environment.rascal.ast.Symbol> x) {
			Sequence z = new Sequence();
			z.$setTail(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Symbol getSymbol() {
		throw new UnsupportedOperationException();
	}

	public boolean hasSymbol() {
		return false;
	}

	public boolean isOptional() {
		return false;
	}

	static public class Optional extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Optional() {
		}

		/* package */Optional(INode node,
				org.meta_environment.rascal.ast.Symbol symbol) {
			this.node = node;
			this.symbol = symbol;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolOptional(this);
		}

		@Override
		public boolean isOptional() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		private org.meta_environment.rascal.ast.Symbol symbol;

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		public Optional setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			Optional z = new Optional();
			z.$setSymbol(x);
			return z;
		}
	}

	public boolean isIter() {
		return false;
	}

	static public class Iter extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Iter() {
		}

		/* package */Iter(INode node,
				org.meta_environment.rascal.ast.Symbol symbol) {
			this.node = node;
			this.symbol = symbol;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolIter(this);
		}

		@Override
		public boolean isIter() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		private org.meta_environment.rascal.ast.Symbol symbol;

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		public Iter setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			Iter z = new Iter();
			z.$setSymbol(x);
			return z;
		}
	}

	public boolean isIterStar() {
		return false;
	}

	static public class IterStar extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private IterStar() {
		}

		/* package */IterStar(INode node,
				org.meta_environment.rascal.ast.Symbol symbol) {
			this.node = node;
			this.symbol = symbol;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolIterStar(this);
		}

		@Override
		public boolean isIterStar() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		private org.meta_environment.rascal.ast.Symbol symbol;

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		public IterStar setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			IterStar z = new IterStar();
			z.$setSymbol(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.StrCon getSep() {
		throw new UnsupportedOperationException();
	}

	public boolean hasSep() {
		return false;
	}

	public boolean isIterSep() {
		return false;
	}

	static public class IterSep extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private IterSep() {
		}

		/* package */IterSep(INode node,
				org.meta_environment.rascal.ast.Symbol symbol,
				org.meta_environment.rascal.ast.StrCon sep) {
			this.node = node;
			this.symbol = symbol;
			this.sep = sep;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolIterSep(this);
		}

		@Override
		public boolean isIterSep() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		@Override
		public boolean hasSep() {
			return true;
		}

		private org.meta_environment.rascal.ast.Symbol symbol;

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		public IterSep setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			IterSep z = new IterSep();
			z.$setSymbol(x);
			return z;
		}

		private org.meta_environment.rascal.ast.StrCon sep;

		@Override
		public org.meta_environment.rascal.ast.StrCon getSep() {
			return sep;
		}

		private void $setSep(org.meta_environment.rascal.ast.StrCon x) {
			this.sep = x;
		}

		public IterSep setSep(org.meta_environment.rascal.ast.StrCon x) {
			IterSep z = new IterSep();
			z.$setSep(x);
			return z;
		}
	}

	public boolean isIterStarSep() {
		return false;
	}

	static public class IterStarSep extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private IterStarSep() {
		}

		/* package */IterStarSep(INode node,
				org.meta_environment.rascal.ast.Symbol symbol,
				org.meta_environment.rascal.ast.StrCon sep) {
			this.node = node;
			this.symbol = symbol;
			this.sep = sep;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolIterStarSep(this);
		}

		@Override
		public boolean isIterStarSep() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		@Override
		public boolean hasSep() {
			return true;
		}

		private org.meta_environment.rascal.ast.Symbol symbol;

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		public IterStarSep setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			IterStarSep z = new IterStarSep();
			z.$setSymbol(x);
			return z;
		}

		private org.meta_environment.rascal.ast.StrCon sep;

		@Override
		public org.meta_environment.rascal.ast.StrCon getSep() {
			return sep;
		}

		private void $setSep(org.meta_environment.rascal.ast.StrCon x) {
			this.sep = x;
		}

		public IterStarSep setSep(org.meta_environment.rascal.ast.StrCon x) {
			IterStarSep z = new IterStarSep();
			z.$setSep(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Symbol getLhs() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Symbol getRhs() {
		throw new UnsupportedOperationException();
	}

	public boolean hasLhs() {
		return false;
	}

	public boolean hasRhs() {
		return false;
	}

	public boolean isAlternative() {
		return false;
	}

	static public class Alternative extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Alternative() {
		}

		/* package */Alternative(INode node,
				org.meta_environment.rascal.ast.Symbol lhs,
				org.meta_environment.rascal.ast.Symbol rhs) {
			this.node = node;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolAlternative(this);
		}

		@Override
		public boolean isAlternative() {
			return true;
		}

		@Override
		public boolean hasLhs() {
			return true;
		}

		@Override
		public boolean hasRhs() {
			return true;
		}

		private org.meta_environment.rascal.ast.Symbol lhs;

		@Override
		public org.meta_environment.rascal.ast.Symbol getLhs() {
			return lhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Symbol x) {
			this.lhs = x;
		}

		public Alternative setLhs(org.meta_environment.rascal.ast.Symbol x) {
			Alternative z = new Alternative();
			z.$setLhs(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Symbol rhs;

		@Override
		public org.meta_environment.rascal.ast.Symbol getRhs() {
			return rhs;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Symbol x) {
			this.rhs = x;
		}

		public Alternative setRhs(org.meta_environment.rascal.ast.Symbol x) {
			Alternative z = new Alternative();
			z.$setRhs(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.CharClass getCharClass() {
		throw new UnsupportedOperationException();
	}

	public boolean hasCharClass() {
		return false;
	}

	public boolean isCharacterClass() {
		return false;
	}

	static public class CharacterClass extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private CharacterClass() {
		}

		/* package */CharacterClass(INode node,
				org.meta_environment.rascal.ast.CharClass charClass) {
			this.node = node;
			this.charClass = charClass;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolCharacterClass(this);
		}

		@Override
		public boolean isCharacterClass() {
			return true;
		}

		@Override
		public boolean hasCharClass() {
			return true;
		}

		private org.meta_environment.rascal.ast.CharClass charClass;

		@Override
		public org.meta_environment.rascal.ast.CharClass getCharClass() {
			return charClass;
		}

		private void $setCharClass(org.meta_environment.rascal.ast.CharClass x) {
			this.charClass = x;
		}

		public CharacterClass setCharClass(
				org.meta_environment.rascal.ast.CharClass x) {
			CharacterClass z = new CharacterClass();
			z.$setCharClass(x);
			return z;
		}
	}

	public boolean isLiftedSymbol() {
		return false;
	}

	static public class LiftedSymbol extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private LiftedSymbol() {
		}

		/* package */LiftedSymbol(INode node,
				org.meta_environment.rascal.ast.Symbol symbol) {
			this.node = node;
			this.symbol = symbol;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolLiftedSymbol(this);
		}

		@Override
		public boolean isLiftedSymbol() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		private org.meta_environment.rascal.ast.Symbol symbol;

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		public LiftedSymbol setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			LiftedSymbol z = new LiftedSymbol();
			z.$setSymbol(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.StrCon getString() {
		throw new UnsupportedOperationException();
	}

	public boolean hasString() {
		return false;
	}

	public boolean isLiteral() {
		return false;
	}

	static public class Literal extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Literal() {
		}

		/* package */Literal(INode node,
				org.meta_environment.rascal.ast.StrCon string) {
			this.node = node;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolLiteral(this);
		}

		@Override
		public boolean isLiteral() {
			return true;
		}

		@Override
		public boolean hasString() {
			return true;
		}

		private org.meta_environment.rascal.ast.StrCon string;

		@Override
		public org.meta_environment.rascal.ast.StrCon getString() {
			return string;
		}

		private void $setString(org.meta_environment.rascal.ast.StrCon x) {
			this.string = x;
		}

		public Literal setString(org.meta_environment.rascal.ast.StrCon x) {
			Literal z = new Literal();
			z.$setString(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.SingleQuotedStrCon getSingelQuotedString() {
		throw new UnsupportedOperationException();
	}

	public boolean hasSingelQuotedString() {
		return false;
	}

	public boolean isCaseInsensitiveLiteral() {
		return false;
	}

	static public class CaseInsensitiveLiteral extends Symbol {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private CaseInsensitiveLiteral() {
		}

		/* package */CaseInsensitiveLiteral(
				INode node,
				org.meta_environment.rascal.ast.SingleQuotedStrCon singelQuotedString) {
			this.node = node;
			this.singelQuotedString = singelQuotedString;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolCaseInsensitiveLiteral(this);
		}

		@Override
		public boolean isCaseInsensitiveLiteral() {
			return true;
		}

		@Override
		public boolean hasSingelQuotedString() {
			return true;
		}

		private org.meta_environment.rascal.ast.SingleQuotedStrCon singelQuotedString;

		@Override
		public org.meta_environment.rascal.ast.SingleQuotedStrCon getSingelQuotedString() {
			return singelQuotedString;
		}

		private void $setSingelQuotedString(
				org.meta_environment.rascal.ast.SingleQuotedStrCon x) {
			this.singelQuotedString = x;
		}

		public CaseInsensitiveLiteral setSingelQuotedString(
				org.meta_environment.rascal.ast.SingleQuotedStrCon x) {
			CaseInsensitiveLiteral z = new CaseInsensitiveLiteral();
			z.$setSingelQuotedString(x);
			return z;
		}
	}
}