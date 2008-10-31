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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolAlternative(this);
		}

		public org.meta_environment.rascal.ast.Symbol getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.Symbol getRhs() {
			return rhs;
		}

		public Alternative setLhs(org.meta_environment.rascal.ast.Symbol x) {
			Alternative z = new Alternative();
			z.$setLhs(x);
			return z;
		}

		public Alternative setRhs(org.meta_environment.rascal.ast.Symbol x) {
			Alternative z = new Alternative();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Ambiguity extends Symbol {
		private final java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolCaseInsensitiveLiteral(this);
		}

		public org.meta_environment.rascal.ast.SingleQuotedStrCon getSingelQuotedString() {
			return singelQuotedString;
		}

		public CaseInsensitiveLiteral setSingelQuotedString(
				org.meta_environment.rascal.ast.SingleQuotedStrCon x) {
			CaseInsensitiveLiteral z = new CaseInsensitiveLiteral();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolCharacterClass(this);
		}

		public org.meta_environment.rascal.ast.CharClass getCharClass() {
			return charClass;
		}

		public CharacterClass setCharClass(
				org.meta_environment.rascal.ast.CharClass x) {
			CharacterClass z = new CharacterClass();
			z.$setCharClass(x);
			return z;
		}
	}

	static public class Empty extends Symbol {
		/* package */Empty(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolEmpty(this);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolIter(this);
		}

		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		public Iter setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			Iter z = new Iter();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class IterSep extends Symbol {
		private org.meta_environment.rascal.ast.StrCon sep;
		private org.meta_environment.rascal.ast.Symbol symbol;

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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolIterSep(this);
		}

		public org.meta_environment.rascal.ast.StrCon getSep() {
			return sep;
		}

		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		public IterSep setSep(org.meta_environment.rascal.ast.StrCon x) {
			IterSep z = new IterSep();
			z.$setSep(x);
			return z;
		}

		public IterSep setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			IterSep z = new IterSep();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolIterStar(this);
		}

		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		public IterStar setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			IterStar z = new IterStar();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class IterStarSep extends Symbol {
		private org.meta_environment.rascal.ast.StrCon sep;
		private org.meta_environment.rascal.ast.Symbol symbol;

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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolIterStarSep(this);
		}

		public org.meta_environment.rascal.ast.StrCon getSep() {
			return sep;
		}

		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		public IterStarSep setSep(org.meta_environment.rascal.ast.StrCon x) {
			IterStarSep z = new IterStarSep();
			z.$setSep(x);
			return z;
		}

		public IterStarSep setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			IterStarSep z = new IterStarSep();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class Lexical extends Symbol {
		/* Symbol "?" -> Symbol */
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolLiftedSymbol(this);
		}

		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		public LiftedSymbol setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			LiftedSymbol z = new LiftedSymbol();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolLiteral(this);
		}

		public org.meta_environment.rascal.ast.StrCon getString() {
			return string;
		}

		public Literal setString(org.meta_environment.rascal.ast.StrCon x) {
			Literal z = new Literal();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolOptional(this);
		}

		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		public Optional setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			Optional z = new Optional();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class ParameterizedSort extends Symbol {
		private java.util.List<org.meta_environment.rascal.ast.Symbol> parameters;
		private org.meta_environment.rascal.ast.Sort sort;

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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolParameterizedSort(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Symbol> getParameters() {
			return parameters;
		}

		public org.meta_environment.rascal.ast.Sort getSort() {
			return sort;
		}

		public ParameterizedSort setParameters(
				java.util.List<org.meta_environment.rascal.ast.Symbol> x) {
			ParameterizedSort z = new ParameterizedSort();
			z.$setParameters(x);
			return z;
		}

		public ParameterizedSort setSort(org.meta_environment.rascal.ast.Sort x) {
			ParameterizedSort z = new ParameterizedSort();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolSequence(this);
		}

		public org.meta_environment.rascal.ast.Symbol getHead() {
			return head;
		}

		public java.util.List<org.meta_environment.rascal.ast.Symbol> getTail() {
			return tail;
		}

		public Sequence setHead(org.meta_environment.rascal.ast.Symbol x) {
			Sequence z = new Sequence();
			z.$setHead(x);
			return z;
		}

		public Sequence setTail(
				java.util.List<org.meta_environment.rascal.ast.Symbol> x) {
			Sequence z = new Sequence();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitSymbolSort(this);
		}

		public org.meta_environment.rascal.ast.Sort getSort() {
			return sort;
		}

		public Sort setSort(org.meta_environment.rascal.ast.Sort x) {
			Sort z = new Sort();
			z.$setSort(x);
			return z;
		}
	}
}
