package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Symbol extends AbstractAST { 
  public boolean isEmpty() { return false; }
static public class Empty extends Symbol {
/** "(" ")" -> Symbol {cons("Empty")} */
	public Empty(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolEmpty(this);
	}

	@Override
	public boolean isEmpty() { return true; }	
}
static public class Ambiguity extends Symbol {
  private final java.util.List<org.rascalmpl.ast.Symbol> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Symbol> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Symbol> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitSymbolAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Symbol getHead() { throw new UnsupportedOperationException(); }
	public java.util.List<org.rascalmpl.ast.Symbol> getTail() { throw new UnsupportedOperationException(); }
public boolean hasHead() { return false; }
	public boolean hasTail() { return false; }
public boolean isSequence() { return false; }
static public class Sequence extends Symbol {
/** "(" head:Symbol tail:Symbol+ ")" -> Symbol {cons("Sequence")} */
	public Sequence(INode node, org.rascalmpl.ast.Symbol head, java.util.List<org.rascalmpl.ast.Symbol> tail) {
		this.node = node;
		this.head = head;
		this.tail = tail;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolSequence(this);
	}

	@Override
	public boolean isSequence() { return true; }

	@Override
	public boolean hasHead() { return true; }
	@Override
	public boolean hasTail() { return true; }

private final org.rascalmpl.ast.Symbol head;
	@Override
	public org.rascalmpl.ast.Symbol getHead() { return head; }
	private final java.util.List<org.rascalmpl.ast.Symbol> tail;
	@Override
	public java.util.List<org.rascalmpl.ast.Symbol> getTail() { return tail; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.Symbol getSymbol() { throw new UnsupportedOperationException(); } public boolean hasSymbol() { return false; } public boolean isOptional() { return false; } static public class Optional extends Symbol {
/** symbol:Symbol "?" -> Symbol {cons("Optional")} */
	public Optional(INode node, org.rascalmpl.ast.Symbol symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolOptional(this);
	}

	@Override
	public boolean isOptional() { return true; }

	@Override
	public boolean hasSymbol() { return true; }

private final org.rascalmpl.ast.Symbol symbol;
	@Override
	public org.rascalmpl.ast.Symbol getSymbol() { return symbol; }	
} public boolean isIter() { return false; } static public class Iter extends Symbol {
/** symbol:Symbol "+" -> Symbol {cons("Iter")} */
	public Iter(INode node, org.rascalmpl.ast.Symbol symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolIter(this);
	}

	@Override
	public boolean isIter() { return true; }

	@Override
	public boolean hasSymbol() { return true; }

private final org.rascalmpl.ast.Symbol symbol;
	@Override
	public org.rascalmpl.ast.Symbol getSymbol() { return symbol; }	
} public boolean isIterStar() { return false; } static public class IterStar extends Symbol {
/** symbol:Symbol "*" -> Symbol {cons("IterStar")} */
	public IterStar(INode node, org.rascalmpl.ast.Symbol symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolIterStar(this);
	}

	@Override
	public boolean isIterStar() { return true; }

	@Override
	public boolean hasSymbol() { return true; }

private final org.rascalmpl.ast.Symbol symbol;
	@Override
	public org.rascalmpl.ast.Symbol getSymbol() { return symbol; }	
} public org.rascalmpl.ast.StrCon getSep() { throw new UnsupportedOperationException(); } public boolean hasSep() { return false; } public boolean isIterSep() { return false; }
static public class IterSep extends Symbol {
/** "{" symbol:Symbol sep:StrCon "}" "+" -> Symbol {cons("IterSep")} */
	public IterSep(INode node, org.rascalmpl.ast.Symbol symbol, org.rascalmpl.ast.StrCon sep) {
		this.node = node;
		this.symbol = symbol;
		this.sep = sep;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolIterSep(this);
	}

	@Override
	public boolean isIterSep() { return true; }

	@Override
	public boolean hasSymbol() { return true; }
	@Override
	public boolean hasSep() { return true; }

private final org.rascalmpl.ast.Symbol symbol;
	@Override
	public org.rascalmpl.ast.Symbol getSymbol() { return symbol; }
	private final org.rascalmpl.ast.StrCon sep;
	@Override
	public org.rascalmpl.ast.StrCon getSep() { return sep; }	
} public boolean isIterStarSep() { return false; }
static public class IterStarSep extends Symbol {
/** "{" symbol:Symbol sep:StrCon "}" "*" -> Symbol {cons("IterStarSep")} */
	public IterStarSep(INode node, org.rascalmpl.ast.Symbol symbol, org.rascalmpl.ast.StrCon sep) {
		this.node = node;
		this.symbol = symbol;
		this.sep = sep;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolIterStarSep(this);
	}

	@Override
	public boolean isIterStarSep() { return true; }

	@Override
	public boolean hasSymbol() { return true; }
	@Override
	public boolean hasSep() { return true; }

private final org.rascalmpl.ast.Symbol symbol;
	@Override
	public org.rascalmpl.ast.Symbol getSymbol() { return symbol; }
	private final org.rascalmpl.ast.StrCon sep;
	@Override
	public org.rascalmpl.ast.StrCon getSep() { return sep; }	
} public org.rascalmpl.ast.Symbol getLhs() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Symbol getRhs() { throw new UnsupportedOperationException(); } public boolean hasLhs() { return false; } public boolean hasRhs() { return false; } public boolean isAlternative() { return false; } static public class Alternative extends Symbol {
/** lhs:Symbol "|" rhs:Symbol -> Symbol {right, cons("Alternative")} */
	public Alternative(INode node, org.rascalmpl.ast.Symbol lhs, org.rascalmpl.ast.Symbol rhs) {
		this.node = node;
		this.lhs = lhs;
		this.rhs = rhs;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolAlternative(this);
	}

	@Override
	public boolean isAlternative() { return true; }

	@Override
	public boolean hasLhs() { return true; }
	@Override
	public boolean hasRhs() { return true; }

private final org.rascalmpl.ast.Symbol lhs;
	@Override
	public org.rascalmpl.ast.Symbol getLhs() { return lhs; }
	private final org.rascalmpl.ast.Symbol rhs;
	@Override
	public org.rascalmpl.ast.Symbol getRhs() { return rhs; }	
} public org.rascalmpl.ast.CharClass getCharClass() { throw new UnsupportedOperationException(); }
public boolean hasCharClass() { return false; }
public boolean isCharacterClass() { return false; }
static public class CharacterClass extends Symbol {
/** charClass:CharClass -> Symbol {cons("CharacterClass")} */
	public CharacterClass(INode node, org.rascalmpl.ast.CharClass charClass) {
		this.node = node;
		this.charClass = charClass;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolCharacterClass(this);
	}

	@Override
	public boolean isCharacterClass() { return true; }

	@Override
	public boolean hasCharClass() { return true; }

private final org.rascalmpl.ast.CharClass charClass;
	@Override
	public org.rascalmpl.ast.CharClass getCharClass() { return charClass; }	
} 
public org.rascalmpl.ast.StrCon getString() { throw new UnsupportedOperationException(); }
public boolean hasString() { return false; }
public boolean isLiteral() { return false; }
static public class Literal extends Symbol {
/** string:StrCon -> Symbol {cons("Literal")} */
	public Literal(INode node, org.rascalmpl.ast.StrCon string) {
		this.node = node;
		this.string = string;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolLiteral(this);
	}

	@Override
	public boolean isLiteral() { return true; }

	@Override
	public boolean hasString() { return true; }

private final org.rascalmpl.ast.StrCon string;
	@Override
	public org.rascalmpl.ast.StrCon getString() { return string; }	
} 
public org.rascalmpl.ast.SingleQuotedStrCon getSingelQuotedString() { throw new UnsupportedOperationException(); }
public boolean hasSingelQuotedString() { return false; }
public boolean isCaseInsensitiveLiteral() { return false; }
static public class CaseInsensitiveLiteral extends Symbol {
/** singelQuotedString:SingleQuotedStrCon -> Symbol {cons("CaseInsensitiveLiteral")} */
	public CaseInsensitiveLiteral(INode node, org.rascalmpl.ast.SingleQuotedStrCon singelQuotedString) {
		this.node = node;
		this.singelQuotedString = singelQuotedString;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolCaseInsensitiveLiteral(this);
	}

	@Override
	public boolean isCaseInsensitiveLiteral() { return true; }

	@Override
	public boolean hasSingelQuotedString() { return true; }

private final org.rascalmpl.ast.SingleQuotedStrCon singelQuotedString;
	@Override
	public org.rascalmpl.ast.SingleQuotedStrCon getSingelQuotedString() { return singelQuotedString; }	
} 
public org.rascalmpl.ast.Name getName() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
public boolean isSort() { return false; }
static public class Sort extends Symbol {
/** name:Name -> Symbol {cons("Sort")} */
	public Sort(INode node, org.rascalmpl.ast.Name name) {
		this.node = node;
		this.name = name;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymbolSort(this);
	}

	@Override
	public boolean isSort() { return true; }

	@Override
	public boolean hasName() { return true; }

private final org.rascalmpl.ast.Name name;
	@Override
	public org.rascalmpl.ast.Name getName() { return name; }	
}
}