package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Symbol extends AbstractAST {
	static public class Alternative extends Symbol {
		private org.meta_environment.rascal.ast.Symbol lhs;
		private org.meta_environment.rascal.ast.Symbol rhs;

		/* lhs:Symbol "|" rhs:Symbol -> Symbol {right, cons("Alternative")} */
		private Alternative() {
		}

		/* package */Alternative(ITree tree,
				org.meta_environment.rascal.ast.Symbol lhs,
				org.meta_environment.rascal.ast.Symbol rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.Symbol x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.Symbol x) {
			this.rhs = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolAlternative(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Symbol getLhs() {
			return lhs;
		}

		@Override
		public org.meta_environment.rascal.ast.Symbol getRhs() {
			return rhs;
		}

		@Override
		public boolean hasLhs() {
			return true;
		}

		@Override
		public boolean hasRhs() {
			return true;
		}

		@Override
		public boolean isAlternative() {
			return true;
		}

		public Alternative setLhs(org.meta_environment.rascal.ast.Symbol x) {
			final Alternative z = new Alternative();
			z.$setLhs(x);
			return z;
		}

		public Alternative setRhs(org.meta_environment.rascal.ast.Symbol x) {
			final Alternative z = new Alternative();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Ambiguity extends Symbol {
		private final java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitSymbolAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Symbol> getAlternatives() {
			return alternatives;
		}
	}

	static public class CaseInsensitiveLiteral extends Symbol {
		private org.meta_environment.rascal.ast.SingleQuotedStrCon singelQuotedString;

		/*
		 * singelQuotedString:SingleQuotedStrCon -> Symbol
		 * {cons("CaseInsensitiveLiteral")}
		 */
		private CaseInsensitiveLiteral() {
		}

		/* package */CaseInsensitiveLiteral(
				ITree tree,
				org.meta_environment.rascal.ast.SingleQuotedStrCon singelQuotedString) {
			this.tree = tree;
			this.singelQuotedString = singelQuotedString;
		}

		private void $setSingelQuotedString(
				org.meta_environment.rascal.ast.SingleQuotedStrCon x) {
			this.singelQuotedString = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolCaseInsensitiveLiteral(this);
		}

		@Override
		public org.meta_environment.rascal.ast.SingleQuotedStrCon getSingelQuotedString() {
			return singelQuotedString;
		}

		@Override
		public boolean hasSingelQuotedString() {
			return true;
		}

		@Override
		public boolean isCaseInsensitiveLiteral() {
			return true;
		}

		public CaseInsensitiveLiteral setSingelQuotedString(
				org.meta_environment.rascal.ast.SingleQuotedStrCon x) {
			final CaseInsensitiveLiteral z = new CaseInsensitiveLiteral();
			z.$setSingelQuotedString(x);
			return z;
		}
	}

	static public class CharacterClass extends Symbol {
		private org.meta_environment.rascal.ast.CharClass charClass;

		/* charClass:CharClass -> Symbol {cons("CharacterClass")} */
		private CharacterClass() {
		}

		/* package */CharacterClass(ITree tree,
				org.meta_environment.rascal.ast.CharClass charClass) {
			this.tree = tree;
			this.charClass = charClass;
		}

		private void $setCharClass(org.meta_environment.rascal.ast.CharClass x) {
			this.charClass = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolCharacterClass(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharClass getCharClass() {
			return charClass;
		}

		@Override
		public boolean hasCharClass() {
			return true;
		}

		@Override
		public boolean isCharacterClass() {
			return true;
		}

		public CharacterClass setCharClass(
				org.meta_environment.rascal.ast.CharClass x) {
			final CharacterClass z = new CharacterClass();
			z.$setCharClass(x);
			return z;
		}
	}

	static public class Empty extends Symbol {
		/* package */Empty(ITree tree) {
			this.tree = tree;
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

	static public class Iter extends Symbol {
		private org.meta_environment.rascal.ast.Symbol symbol;

		/* symbol:Symbol "+" -> Symbol {cons("Iter")} */
		private Iter() {
		}

		/* package */Iter(ITree tree,
				org.meta_environment.rascal.ast.Symbol symbol) {
			this.tree = tree;
			this.symbol = symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolIter(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		@Override
		public boolean isIter() {
			return true;
		}

		public Iter setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			final Iter z = new Iter();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class IterSep extends Symbol {
		private org.meta_environment.rascal.ast.Symbol symbol;
		private org.meta_environment.rascal.ast.StrCon sep;

		/* "{" symbol:Symbol sep:StrCon "}" "+" -> Symbol {cons("IterSep")} */
		private IterSep() {
		}

		/* package */IterSep(ITree tree,
				org.meta_environment.rascal.ast.Symbol symbol,
				org.meta_environment.rascal.ast.StrCon sep) {
			this.tree = tree;
			this.symbol = symbol;
			this.sep = sep;
		}

		private void $setSep(org.meta_environment.rascal.ast.StrCon x) {
			this.sep = x;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolIterSep(this);
		}

		@Override
		public org.meta_environment.rascal.ast.StrCon getSep() {
			return sep;
		}

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		@Override
		public boolean hasSep() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		@Override
		public boolean isIterSep() {
			return true;
		}

		public IterSep setSep(org.meta_environment.rascal.ast.StrCon x) {
			final IterSep z = new IterSep();
			z.$setSep(x);
			return z;
		}

		public IterSep setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			final IterSep z = new IterSep();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class IterStar extends Symbol {
		private org.meta_environment.rascal.ast.Symbol symbol;

		/* symbol:Symbol "*" -> Symbol {cons("IterStar")} */
		private IterStar() {
		}

		/* package */IterStar(ITree tree,
				org.meta_environment.rascal.ast.Symbol symbol) {
			this.tree = tree;
			this.symbol = symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolIterStar(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		@Override
		public boolean isIterStar() {
			return true;
		}

		public IterStar setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			final IterStar z = new IterStar();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class IterStarSep extends Symbol {
		private org.meta_environment.rascal.ast.Symbol symbol;
		private org.meta_environment.rascal.ast.StrCon sep;

		/* "{" symbol:Symbol sep:StrCon "}" "*" -> Symbol {cons("IterStarSep")} */
		private IterStarSep() {
		}

		/* package */IterStarSep(ITree tree,
				org.meta_environment.rascal.ast.Symbol symbol,
				org.meta_environment.rascal.ast.StrCon sep) {
			this.tree = tree;
			this.symbol = symbol;
			this.sep = sep;
		}

		private void $setSep(org.meta_environment.rascal.ast.StrCon x) {
			this.sep = x;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolIterStarSep(this);
		}

		@Override
		public org.meta_environment.rascal.ast.StrCon getSep() {
			return sep;
		}

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		@Override
		public boolean hasSep() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		@Override
		public boolean isIterStarSep() {
			return true;
		}

		public IterStarSep setSep(org.meta_environment.rascal.ast.StrCon x) {
			final IterStarSep z = new IterStarSep();
			z.$setSep(x);
			return z;
		}

		public IterStarSep setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			final IterStarSep z = new IterStarSep();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class LiftedSymbol extends Symbol {
		private org.meta_environment.rascal.ast.Symbol symbol;

		/* "`" symbol:Symbol "`" -> Symbol {cons("LiftedSymbol")} */
		private LiftedSymbol() {
		}

		/* package */LiftedSymbol(ITree tree,
				org.meta_environment.rascal.ast.Symbol symbol) {
			this.tree = tree;
			this.symbol = symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolLiftedSymbol(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		@Override
		public boolean isLiftedSymbol() {
			return true;
		}

		public LiftedSymbol setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			final LiftedSymbol z = new LiftedSymbol();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class Literal extends Symbol {
		private org.meta_environment.rascal.ast.StrCon string;

		/* string:StrCon -> Symbol {cons("Literal")} */
		private Literal() {
		}

		/* package */Literal(ITree tree,
				org.meta_environment.rascal.ast.StrCon string) {
			this.tree = tree;
			this.string = string;
		}

		private void $setString(org.meta_environment.rascal.ast.StrCon x) {
			this.string = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolLiteral(this);
		}

		@Override
		public org.meta_environment.rascal.ast.StrCon getString() {
			return string;
		}

		@Override
		public boolean hasString() {
			return true;
		}

		@Override
		public boolean isLiteral() {
			return true;
		}

		public Literal setString(org.meta_environment.rascal.ast.StrCon x) {
			final Literal z = new Literal();
			z.$setString(x);
			return z;
		}
	}

	static public class Optional extends Symbol {
		private org.meta_environment.rascal.ast.Symbol symbol;

		/* symbol:Symbol "?" -> Symbol {cons("Optional")} */
		private Optional() {
		}

		/* package */Optional(ITree tree,
				org.meta_environment.rascal.ast.Symbol symbol) {
			this.tree = tree;
			this.symbol = symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolOptional(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		@Override
		public boolean isOptional() {
			return true;
		}

		public Optional setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			final Optional z = new Optional();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class ParameterizedSort extends Symbol {
		private org.meta_environment.rascal.ast.Sort sort;
		private java.util.List<org.meta_environment.rascal.ast.Symbol> parameters;

		/*
		 * sort:Sort "[[" parameters:{Symbol ","}+ "]]" -> Symbol
		 * {cons("ParameterizedSort")}
		 */
		private ParameterizedSort() {
		}

		/* package */ParameterizedSort(
				ITree tree,
				org.meta_environment.rascal.ast.Sort sort,
				java.util.List<org.meta_environment.rascal.ast.Symbol> parameters) {
			this.tree = tree;
			this.sort = sort;
			this.parameters = parameters;
		}

		private void $setParameters(
				java.util.List<org.meta_environment.rascal.ast.Symbol> x) {
			this.parameters = x;
		}

		private void $setSort(org.meta_environment.rascal.ast.Sort x) {
			this.sort = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolParameterizedSort(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Symbol> getParameters() {
			return parameters;
		}

		@Override
		public org.meta_environment.rascal.ast.Sort getSort() {
			return sort;
		}

		@Override
		public boolean hasParameters() {
			return true;
		}

		@Override
		public boolean hasSort() {
			return true;
		}

		@Override
		public boolean isParameterizedSort() {
			return true;
		}

		public ParameterizedSort setParameters(
				java.util.List<org.meta_environment.rascal.ast.Symbol> x) {
			final ParameterizedSort z = new ParameterizedSort();
			z.$setParameters(x);
			return z;
		}

		public ParameterizedSort setSort(org.meta_environment.rascal.ast.Sort x) {
			final ParameterizedSort z = new ParameterizedSort();
			z.$setSort(x);
			return z;
		}
	}

	static public class Sequence extends Symbol {
		private org.meta_environment.rascal.ast.Symbol head;
		private java.util.List<org.meta_environment.rascal.ast.Symbol> tail;

		/* "(" head:Symbol tail:Symbol+ ")" -> Symbol {cons("Sequence")} */
		private Sequence() {
		}

		/* package */Sequence(ITree tree,
				org.meta_environment.rascal.ast.Symbol head,
				java.util.List<org.meta_environment.rascal.ast.Symbol> tail) {
			this.tree = tree;
			this.head = head;
			this.tail = tail;
		}

		private void $setHead(org.meta_environment.rascal.ast.Symbol x) {
			this.head = x;
		}

		private void $setTail(
				java.util.List<org.meta_environment.rascal.ast.Symbol> x) {
			this.tail = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolSequence(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Symbol getHead() {
			return head;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Symbol> getTail() {
			return tail;
		}

		@Override
		public boolean hasHead() {
			return true;
		}

		@Override
		public boolean hasTail() {
			return true;
		}

		@Override
		public boolean isSequence() {
			return true;
		}

		public Sequence setHead(org.meta_environment.rascal.ast.Symbol x) {
			final Sequence z = new Sequence();
			z.$setHead(x);
			return z;
		}

		public Sequence setTail(
				java.util.List<org.meta_environment.rascal.ast.Symbol> x) {
			final Sequence z = new Sequence();
			z.$setTail(x);
			return z;
		}
	}

	static public class Sort extends Symbol {
		private org.meta_environment.rascal.ast.Sort sort;

		/* sort:Sort -> Symbol {cons("Sort")} */
		private Sort() {
		}

		/* package */Sort(ITree tree, org.meta_environment.rascal.ast.Sort sort) {
			this.tree = tree;
			this.sort = sort;
		}

		private void $setSort(org.meta_environment.rascal.ast.Sort x) {
			this.sort = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSymbolSort(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Sort getSort() {
			return sort;
		}

		@Override
		public boolean hasSort() {
			return true;
		}

		@Override
		public boolean isSort() {
			return true;
		}

		public Sort setSort(org.meta_environment.rascal.ast.Sort x) {
			final Sort z = new Sort();
			z.$setSort(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.CharClass getCharClass() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Symbol getHead() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Symbol getLhs() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Symbol> getParameters() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Symbol getRhs() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.StrCon getSep() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.SingleQuotedStrCon getSingelQuotedString() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Sort getSort() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.StrCon getString() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Symbol getSymbol() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Symbol> getTail() {
		throw new UnsupportedOperationException();
	}

	public boolean hasCharClass() {
		return false;
	}

	public boolean hasHead() {
		return false;
	}

	public boolean hasLhs() {
		return false;
	}

	public boolean hasParameters() {
		return false;
	}

	public boolean hasRhs() {
		return false;
	}

	public boolean hasSep() {
		return false;
	}

	public boolean hasSingelQuotedString() {
		return false;
	}

	public boolean hasSort() {
		return false;
	}

	public boolean hasString() {
		return false;
	}

	public boolean hasSymbol() {
		return false;
	}

	public boolean hasTail() {
		return false;
	}

	public boolean isAlternative() {
		return false;
	}

	public boolean isCaseInsensitiveLiteral() {
		return false;
	}

	public boolean isCharacterClass() {
		return false;
	}

	public boolean isEmpty() {
		return false;
	}

	public boolean isIter() {
		return false;
	}

	public boolean isIterSep() {
		return false;
	}

	public boolean isIterStar() {
		return false;
	}

	public boolean isIterStarSep() {
		return false;
	}

	public boolean isLiftedSymbol() {
		return false;
	}

	public boolean isLiteral() {
		return false;
	}

	public boolean isOptional() {
		return false;
	}

	public boolean isParameterizedSort() {
		return false;
	}

	public boolean isSequence() {
		return false;
	}

	public boolean isSort() {
		return false;
	}
}