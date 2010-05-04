package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Sym extends AbstractAST { 
  public boolean isParametrized() { return false; }
static public class Parametrized extends Sym {
/** Nonterminal "[" {Sym ","}+ "]" -> Sym {cons("Parametrized")} */
	public Parametrized(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymParametrized(this);
	}

	@Override
	public boolean isParametrized() { return true; }	
}
static public class Ambiguity extends Sym {
  private final java.util.List<org.rascalmpl.ast.Sym> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Sym> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Sym> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitSymAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Nonterminal getNonterminal() { throw new UnsupportedOperationException(); }
public boolean hasNonterminal() { return false; }
public boolean isNonterminal() { return false; }
static public class Nonterminal extends Sym {
/** nonterminal:Nonterminal -> Sym {cons("Nonterminal")} */
	public Nonterminal(INode node, org.rascalmpl.ast.Nonterminal nonterminal) {
		this.node = node;
		this.nonterminal = nonterminal;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymNonterminal(this);
	}

	@Override
	public boolean isNonterminal() { return true; }

	@Override
	public boolean hasNonterminal() { return true; }

private final org.rascalmpl.ast.Nonterminal nonterminal;
	@Override
	public org.rascalmpl.ast.Nonterminal getNonterminal() { return nonterminal; }	
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.Sym getSymbol() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.NonterminalLabel getLabel() { throw new UnsupportedOperationException(); } public boolean hasSymbol() { return false; } public boolean hasLabel() { return false; }
public boolean isLabeled() { return false; }
static public class Labeled extends Sym {
/** symbol:Sym label:NonterminalLabel -> Sym {cons("Labeled")} */
	public Labeled(INode node, org.rascalmpl.ast.Sym symbol, org.rascalmpl.ast.NonterminalLabel label) {
		this.node = node;
		this.symbol = symbol;
		this.label = label;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymLabeled(this);
	}

	@Override
	public boolean isLabeled() { return true; }

	@Override
	public boolean hasSymbol() { return true; }
	@Override
	public boolean hasLabel() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }
	private final org.rascalmpl.ast.NonterminalLabel label;
	@Override
	public org.rascalmpl.ast.NonterminalLabel getLabel() { return label; }	
} public boolean isOptional() { return false; }
static public class Optional extends Sym {
/** symbol:Sym "?" -> Sym {cons("Optional")} */
	public Optional(INode node, org.rascalmpl.ast.Sym symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymOptional(this);
	}

	@Override
	public boolean isOptional() { return true; }

	@Override
	public boolean hasSymbol() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }	
} public boolean isNonEagerOptional() { return false; }
static public class NonEagerOptional extends Sym {
/** symbol:Sym "??" -> Sym {cons("NonEagerOptional")} */
	public NonEagerOptional(INode node, org.rascalmpl.ast.Sym symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymNonEagerOptional(this);
	}

	@Override
	public boolean isNonEagerOptional() { return true; }

	@Override
	public boolean hasSymbol() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }	
} public boolean isIter() { return false; }
static public class Iter extends Sym {
/** symbol:Sym "+" -> Sym {cons("Iter")} */
	public Iter(INode node, org.rascalmpl.ast.Sym symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymIter(this);
	}

	@Override
	public boolean isIter() { return true; }

	@Override
	public boolean hasSymbol() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }	
} public boolean isIterStar() { return false; }
static public class IterStar extends Sym {
/** symbol:Sym "*" -> Sym {cons("IterStar")} */
	public IterStar(INode node, org.rascalmpl.ast.Sym symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymIterStar(this);
	}

	@Override
	public boolean isIterStar() { return true; }

	@Override
	public boolean hasSymbol() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }	
} public boolean isNonEagerIter() { return false; }
static public class NonEagerIter extends Sym {
/** symbol:Sym "+?" -> Sym {cons("NonEagerIter")} */
	public NonEagerIter(INode node, org.rascalmpl.ast.Sym symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymNonEagerIter(this);
	}

	@Override
	public boolean isNonEagerIter() { return true; }

	@Override
	public boolean hasSymbol() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }	
} public boolean isNonEagerIterStar() { return false; }
static public class NonEagerIterStar extends Sym {
/** symbol:Sym "*?" -> Sym {cons("NonEagerIterStar")} */
	public NonEagerIterStar(INode node, org.rascalmpl.ast.Sym symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymNonEagerIterStar(this);
	}

	@Override
	public boolean isNonEagerIterStar() { return true; }

	@Override
	public boolean hasSymbol() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }	
} public org.rascalmpl.ast.StringConstant getSep() { throw new UnsupportedOperationException(); } public boolean hasSep() { return false; } public boolean isIterSep() { return false; }
static public class IterSep extends Sym {
/** "{" symbol:Sym sep:StringConstant "}" "+" -> Sym {cons("IterSep")} */
	public IterSep(INode node, org.rascalmpl.ast.Sym symbol, org.rascalmpl.ast.StringConstant sep) {
		this.node = node;
		this.symbol = symbol;
		this.sep = sep;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymIterSep(this);
	}

	@Override
	public boolean isIterSep() { return true; }

	@Override
	public boolean hasSymbol() { return true; }
	@Override
	public boolean hasSep() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }
	private final org.rascalmpl.ast.StringConstant sep;
	@Override
	public org.rascalmpl.ast.StringConstant getSep() { return sep; }	
} public boolean isIterStarSep() { return false; }
static public class IterStarSep extends Sym {
/** "{" symbol:Sym sep:StringConstant "}" "*" -> Sym {cons("IterStarSep")} */
	public IterStarSep(INode node, org.rascalmpl.ast.Sym symbol, org.rascalmpl.ast.StringConstant sep) {
		this.node = node;
		this.symbol = symbol;
		this.sep = sep;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymIterStarSep(this);
	}

	@Override
	public boolean isIterStarSep() { return true; }

	@Override
	public boolean hasSymbol() { return true; }
	@Override
	public boolean hasSep() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }
	private final org.rascalmpl.ast.StringConstant sep;
	@Override
	public org.rascalmpl.ast.StringConstant getSep() { return sep; }	
} public boolean isNonEagerIterSep() { return false; }
static public class NonEagerIterSep extends Sym {
/** "{" symbol:Sym sep:StringConstant "}" "+?" -> Sym {cons("NonEagerIterSep")} */
	public NonEagerIterSep(INode node, org.rascalmpl.ast.Sym symbol, org.rascalmpl.ast.StringConstant sep) {
		this.node = node;
		this.symbol = symbol;
		this.sep = sep;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymNonEagerIterSep(this);
	}

	@Override
	public boolean isNonEagerIterSep() { return true; }

	@Override
	public boolean hasSymbol() { return true; }
	@Override
	public boolean hasSep() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }
	private final org.rascalmpl.ast.StringConstant sep;
	@Override
	public org.rascalmpl.ast.StringConstant getSep() { return sep; }	
} public boolean isNonEagerIterStarSep() { return false; }
static public class NonEagerIterStarSep extends Sym {
/** "{" symbol:Sym sep:StringConstant "}" "*?" -> Sym {cons("NonEagerIterStarSep")} */
	public NonEagerIterStarSep(INode node, org.rascalmpl.ast.Sym symbol, org.rascalmpl.ast.StringConstant sep) {
		this.node = node;
		this.symbol = symbol;
		this.sep = sep;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymNonEagerIterStarSep(this);
	}

	@Override
	public boolean isNonEagerIterStarSep() { return true; }

	@Override
	public boolean hasSymbol() { return true; }
	@Override
	public boolean hasSep() { return true; }

private final org.rascalmpl.ast.Sym symbol;
	@Override
	public org.rascalmpl.ast.Sym getSymbol() { return symbol; }
	private final org.rascalmpl.ast.StringConstant sep;
	@Override
	public org.rascalmpl.ast.StringConstant getSep() { return sep; }	
} 
public boolean isStartOfLine() { return false; }
static public class StartOfLine extends Sym {
/** "^" -> Sym {cons("StartOfLine")} */
	public StartOfLine(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymStartOfLine(this);
	}

	@Override
	public boolean isStartOfLine() { return true; }	
} 
public boolean isEndOfLine() { return false; }
static public class EndOfLine extends Sym {
/** "$" -> Sym {cons("EndOfLine")} */
	public EndOfLine(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymEndOfLine(this);
	}

	@Override
	public boolean isEndOfLine() { return true; }	
} 
public org.rascalmpl.ast.IntegerLiteral getColumn() { throw new UnsupportedOperationException(); }
public boolean hasColumn() { return false; }
public boolean isColumn() { return false; }
static public class Column extends Sym {
/** "@" column:IntegerLiteral -> Sym {cons("Column")} */
	public Column(INode node, org.rascalmpl.ast.IntegerLiteral column) {
		this.node = node;
		this.column = column;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymColumn(this);
	}

	@Override
	public boolean isColumn() { return true; }

	@Override
	public boolean hasColumn() { return true; }

private final org.rascalmpl.ast.IntegerLiteral column;
	@Override
	public org.rascalmpl.ast.IntegerLiteral getColumn() { return column; }	
} 
public org.rascalmpl.ast.Class getCharClass() { throw new UnsupportedOperationException(); }
public boolean hasCharClass() { return false; }
public boolean isCharacterClass() { return false; }
static public class CharacterClass extends Sym {
/** charClass:Class -> Sym {cons("CharacterClass")} */
	public CharacterClass(INode node, org.rascalmpl.ast.Class charClass) {
		this.node = node;
		this.charClass = charClass;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymCharacterClass(this);
	}

	@Override
	public boolean isCharacterClass() { return true; }

	@Override
	public boolean hasCharClass() { return true; }

private final org.rascalmpl.ast.Class charClass;
	@Override
	public org.rascalmpl.ast.Class getCharClass() { return charClass; }	
} 
public org.rascalmpl.ast.StringConstant getString() { throw new UnsupportedOperationException(); }
public boolean hasString() { return false; }
public boolean isLiteral() { return false; }
static public class Literal extends Sym {
/** string:StringConstant -> Sym {cons("Literal")} */
	public Literal(INode node, org.rascalmpl.ast.StringConstant string) {
		this.node = node;
		this.string = string;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymLiteral(this);
	}

	@Override
	public boolean isLiteral() { return true; }

	@Override
	public boolean hasString() { return true; }

private final org.rascalmpl.ast.StringConstant string;
	@Override
	public org.rascalmpl.ast.StringConstant getString() { return string; }	
} 
public org.rascalmpl.ast.CaseInsensitiveStringConstant getCistring() { throw new UnsupportedOperationException(); }
public boolean hasCistring() { return false; }
public boolean isCaseInsensitiveLiteral() { return false; }
static public class CaseInsensitiveLiteral extends Sym {
/** cistring:CaseInsensitiveStringConstant -> Sym {cons("CaseInsensitiveLiteral")} */
	public CaseInsensitiveLiteral(INode node, org.rascalmpl.ast.CaseInsensitiveStringConstant cistring) {
		this.node = node;
		this.cistring = cistring;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitSymCaseInsensitiveLiteral(this);
	}

	@Override
	public boolean isCaseInsensitiveLiteral() { return true; }

	@Override
	public boolean hasCistring() { return true; }

private final org.rascalmpl.ast.CaseInsensitiveStringConstant cistring;
	@Override
	public org.rascalmpl.ast.CaseInsensitiveStringConstant getCistring() { return cistring; }	
}
}