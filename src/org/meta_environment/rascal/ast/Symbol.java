package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Symbol extends AbstractAST { 
  public boolean isEmpty() { return false; }
static public class Empty extends Symbol {
/* "(" ")" -> Symbol {cons("Empty")} */
	private Empty() { }
	/*package*/ Empty(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolEmpty(this);
	}

	public boolean isEmpty() { return true; }	
}
static public class Ambiguity extends Symbol {
  private final java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Symbol> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitSymbolAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.Symbol getHead() { throw new UnsupportedOperationException(); }
	public java.util.List<org.meta_environment.rascal.ast.Symbol> getTail() { throw new UnsupportedOperationException(); }
public boolean hasHead() { return false; }
	public boolean hasTail() { return false; }
public boolean isSequence() { return false; }
static public class Sequence extends Symbol {
/* "(" head:Symbol tail:Symbol+ ")" -> Symbol {cons("Sequence")} */
	private Sequence() { }
	/*package*/ Sequence(INode node, org.meta_environment.rascal.ast.Symbol head, java.util.List<org.meta_environment.rascal.ast.Symbol> tail) {
		this.node = node;
		this.head = head;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolSequence(this);
	}

	public boolean isSequence() { return true; }

	public boolean hasHead() { return true; }
	public boolean hasTail() { return true; }

private org.meta_environment.rascal.ast.Symbol head;
	public org.meta_environment.rascal.ast.Symbol getHead() { return head; }
	private void $setHead(org.meta_environment.rascal.ast.Symbol x) { this.head = x; }
	public Sequence setHead(org.meta_environment.rascal.ast.Symbol x) { 
		Sequence z = new Sequence();
 		z.$setHead(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Symbol> tail;
	public java.util.List<org.meta_environment.rascal.ast.Symbol> getTail() { return tail; }
	private void $setTail(java.util.List<org.meta_environment.rascal.ast.Symbol> x) { this.tail = x; }
	public Sequence setTail(java.util.List<org.meta_environment.rascal.ast.Symbol> x) { 
		Sequence z = new Sequence();
 		z.$setTail(x);
		return z;
	}	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.Symbol getSymbol() { throw new UnsupportedOperationException(); } public boolean hasSymbol() { return false; } public boolean isOptional() { return false; } static public class Optional extends Symbol {
/* symbol:Symbol "?" -> Symbol {cons("Optional")} */
	private Optional() { }
	/*package*/ Optional(INode node, org.meta_environment.rascal.ast.Symbol symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolOptional(this);
	}

	public boolean isOptional() { return true; }

	public boolean hasSymbol() { return true; }

private org.meta_environment.rascal.ast.Symbol symbol;
	public org.meta_environment.rascal.ast.Symbol getSymbol() { return symbol; }
	private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) { this.symbol = x; }
	public Optional setSymbol(org.meta_environment.rascal.ast.Symbol x) { 
		Optional z = new Optional();
 		z.$setSymbol(x);
		return z;
	}	
} public boolean isIter() { return false; } static public class Iter extends Symbol {
/* symbol:Symbol "+" -> Symbol {cons("Iter")} */
	private Iter() { }
	/*package*/ Iter(INode node, org.meta_environment.rascal.ast.Symbol symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolIter(this);
	}

	public boolean isIter() { return true; }

	public boolean hasSymbol() { return true; }

private org.meta_environment.rascal.ast.Symbol symbol;
	public org.meta_environment.rascal.ast.Symbol getSymbol() { return symbol; }
	private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) { this.symbol = x; }
	public Iter setSymbol(org.meta_environment.rascal.ast.Symbol x) { 
		Iter z = new Iter();
 		z.$setSymbol(x);
		return z;
	}	
} public boolean isIterStar() { return false; } static public class IterStar extends Symbol {
/* symbol:Symbol "*" -> Symbol {cons("IterStar")} */
	private IterStar() { }
	/*package*/ IterStar(INode node, org.meta_environment.rascal.ast.Symbol symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolIterStar(this);
	}

	public boolean isIterStar() { return true; }

	public boolean hasSymbol() { return true; }

private org.meta_environment.rascal.ast.Symbol symbol;
	public org.meta_environment.rascal.ast.Symbol getSymbol() { return symbol; }
	private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) { this.symbol = x; }
	public IterStar setSymbol(org.meta_environment.rascal.ast.Symbol x) { 
		IterStar z = new IterStar();
 		z.$setSymbol(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.StrCon getSep() { throw new UnsupportedOperationException(); } public boolean hasSep() { return false; } public boolean isIterSep() { return false; }
static public class IterSep extends Symbol {
/* "{" symbol:Symbol sep:StrCon "}" "+" -> Symbol {cons("IterSep")} */
	private IterSep() { }
	/*package*/ IterSep(INode node, org.meta_environment.rascal.ast.Symbol symbol, org.meta_environment.rascal.ast.StrCon sep) {
		this.node = node;
		this.symbol = symbol;
		this.sep = sep;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolIterSep(this);
	}

	public boolean isIterSep() { return true; }

	public boolean hasSymbol() { return true; }
	public boolean hasSep() { return true; }

private org.meta_environment.rascal.ast.Symbol symbol;
	public org.meta_environment.rascal.ast.Symbol getSymbol() { return symbol; }
	private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) { this.symbol = x; }
	public IterSep setSymbol(org.meta_environment.rascal.ast.Symbol x) { 
		IterSep z = new IterSep();
 		z.$setSymbol(x);
		return z;
	}
	private org.meta_environment.rascal.ast.StrCon sep;
	public org.meta_environment.rascal.ast.StrCon getSep() { return sep; }
	private void $setSep(org.meta_environment.rascal.ast.StrCon x) { this.sep = x; }
	public IterSep setSep(org.meta_environment.rascal.ast.StrCon x) { 
		IterSep z = new IterSep();
 		z.$setSep(x);
		return z;
	}	
} public boolean isIterStarSep() { return false; }
static public class IterStarSep extends Symbol {
/* "{" symbol:Symbol sep:StrCon "}" "*" -> Symbol {cons("IterStarSep")} */
	private IterStarSep() { }
	/*package*/ IterStarSep(INode node, org.meta_environment.rascal.ast.Symbol symbol, org.meta_environment.rascal.ast.StrCon sep) {
		this.node = node;
		this.symbol = symbol;
		this.sep = sep;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolIterStarSep(this);
	}

	public boolean isIterStarSep() { return true; }

	public boolean hasSymbol() { return true; }
	public boolean hasSep() { return true; }

private org.meta_environment.rascal.ast.Symbol symbol;
	public org.meta_environment.rascal.ast.Symbol getSymbol() { return symbol; }
	private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) { this.symbol = x; }
	public IterStarSep setSymbol(org.meta_environment.rascal.ast.Symbol x) { 
		IterStarSep z = new IterStarSep();
 		z.$setSymbol(x);
		return z;
	}
	private org.meta_environment.rascal.ast.StrCon sep;
	public org.meta_environment.rascal.ast.StrCon getSep() { return sep; }
	private void $setSep(org.meta_environment.rascal.ast.StrCon x) { this.sep = x; }
	public IterStarSep setSep(org.meta_environment.rascal.ast.StrCon x) { 
		IterStarSep z = new IterStarSep();
 		z.$setSep(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.Symbol getLhs() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Symbol getRhs() { throw new UnsupportedOperationException(); } public boolean hasLhs() { return false; } public boolean hasRhs() { return false; } public boolean isAlternative() { return false; } static public class Alternative extends Symbol {
/* lhs:Symbol "|" rhs:Symbol -> Symbol {right, cons("Alternative")} */
	private Alternative() { }
	/*package*/ Alternative(INode node, org.meta_environment.rascal.ast.Symbol lhs, org.meta_environment.rascal.ast.Symbol rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolAlternative(this);
	}

	public boolean isAlternative() { return true; }

	public boolean hasLhs() { return true; }
	public boolean hasRhs() { return true; }

private org.meta_environment.rascal.ast.Symbol lhs;
	public org.meta_environment.rascal.ast.Symbol getLhs() { return lhs; }
	private void $setLhs(org.meta_environment.rascal.ast.Symbol x) { this.lhs = x; }
	public Alternative setLhs(org.meta_environment.rascal.ast.Symbol x) { 
		Alternative z = new Alternative();
 		z.$setLhs(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Symbol rhs;
	public org.meta_environment.rascal.ast.Symbol getRhs() { return rhs; }
	private void $setRhs(org.meta_environment.rascal.ast.Symbol x) { this.rhs = x; }
	public Alternative setRhs(org.meta_environment.rascal.ast.Symbol x) { 
		Alternative z = new Alternative();
 		z.$setRhs(x);
		return z;
	}	
} public org.meta_environment.rascal.ast.CharClass getCharClass() { throw new UnsupportedOperationException(); }
public boolean hasCharClass() { return false; }
public boolean isCharacterClass() { return false; }
static public class CharacterClass extends Symbol {
/* charClass:CharClass -> Symbol {cons("CharacterClass")} */
	private CharacterClass() { }
	/*package*/ CharacterClass(INode node, org.meta_environment.rascal.ast.CharClass charClass) {
		this.node = node;
		this.charClass = charClass;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolCharacterClass(this);
	}

	public boolean isCharacterClass() { return true; }

	public boolean hasCharClass() { return true; }

private org.meta_environment.rascal.ast.CharClass charClass;
	public org.meta_environment.rascal.ast.CharClass getCharClass() { return charClass; }
	private void $setCharClass(org.meta_environment.rascal.ast.CharClass x) { this.charClass = x; }
	public CharacterClass setCharClass(org.meta_environment.rascal.ast.CharClass x) { 
		CharacterClass z = new CharacterClass();
 		z.$setCharClass(x);
		return z;
	}	
} public boolean isLiftedSymbol() { return false; }
static public class LiftedSymbol extends Symbol {
/* "140" symbol:Symbol "140" -> Symbol {cons("LiftedSymbol")} */
	private LiftedSymbol() { }
	/*package*/ LiftedSymbol(INode node, org.meta_environment.rascal.ast.Symbol symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolLiftedSymbol(this);
	}

	public boolean isLiftedSymbol() { return true; }

	public boolean hasSymbol() { return true; }

private org.meta_environment.rascal.ast.Symbol symbol;
	public org.meta_environment.rascal.ast.Symbol getSymbol() { return symbol; }
	private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) { this.symbol = x; }
	public LiftedSymbol setSymbol(org.meta_environment.rascal.ast.Symbol x) { 
		LiftedSymbol z = new LiftedSymbol();
 		z.$setSymbol(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.StrCon getString() { throw new UnsupportedOperationException(); }
public boolean hasString() { return false; }
public boolean isLiteral() { return false; }
static public class Literal extends Symbol {
/* string:StrCon -> Symbol {cons("Literal")} */
	private Literal() { }
	/*package*/ Literal(INode node, org.meta_environment.rascal.ast.StrCon string) {
		this.node = node;
		this.string = string;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolLiteral(this);
	}

	public boolean isLiteral() { return true; }

	public boolean hasString() { return true; }

private org.meta_environment.rascal.ast.StrCon string;
	public org.meta_environment.rascal.ast.StrCon getString() { return string; }
	private void $setString(org.meta_environment.rascal.ast.StrCon x) { this.string = x; }
	public Literal setString(org.meta_environment.rascal.ast.StrCon x) { 
		Literal z = new Literal();
 		z.$setString(x);
		return z;
	}	
} 
public org.meta_environment.rascal.ast.SingleQuotedStrCon getSingelQuotedString() { throw new UnsupportedOperationException(); }
public boolean hasSingelQuotedString() { return false; }
public boolean isCaseInsensitiveLiteral() { return false; }
static public class CaseInsensitiveLiteral extends Symbol {
/* singelQuotedString:SingleQuotedStrCon -> Symbol {cons("CaseInsensitiveLiteral")} */
	private CaseInsensitiveLiteral() { }
	/*package*/ CaseInsensitiveLiteral(INode node, org.meta_environment.rascal.ast.SingleQuotedStrCon singelQuotedString) {
		this.node = node;
		this.singelQuotedString = singelQuotedString;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolCaseInsensitiveLiteral(this);
	}

	public boolean isCaseInsensitiveLiteral() { return true; }

	public boolean hasSingelQuotedString() { return true; }

private org.meta_environment.rascal.ast.SingleQuotedStrCon singelQuotedString;
	public org.meta_environment.rascal.ast.SingleQuotedStrCon getSingelQuotedString() { return singelQuotedString; }
	private void $setSingelQuotedString(org.meta_environment.rascal.ast.SingleQuotedStrCon x) { this.singelQuotedString = x; }
	public CaseInsensitiveLiteral setSingelQuotedString(org.meta_environment.rascal.ast.SingleQuotedStrCon x) { 
		CaseInsensitiveLiteral z = new CaseInsensitiveLiteral();
 		z.$setSingelQuotedString(x);
		return z;
	}	
}
}