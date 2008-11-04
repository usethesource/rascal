package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Symbol extends AbstractAST
{
  public org.meta_environment.rascal.ast.Sort getSort ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasSort ()
  {
    return false;
  }
  public boolean isSort ()
  {
    return false;
  }
  static public class Sort extends Symbol
  {
/* sort:Sort -> Symbol {cons("Sort")} */
    private Sort ()
    {
    }
    /*package */ Sort (ITree tree, org.meta_environment.rascal.ast.Sort sort)
    {
      this.tree = tree;
      this.sort = sort;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolSort (this);
    }

    public boolean isSort ()
    {
      return true;
    }

    public boolean hasSort ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Sort sort;
    public org.meta_environment.rascal.ast.Sort getSort ()
    {
      return sort;
    }
    private void $setSort (org.meta_environment.rascal.ast.Sort x)
    {
      this.sort = x;
    }
    public Sort setSort (org.meta_environment.rascal.ast.Sort x)
    {
      Sort z = new Sort ();
      z.$setSort (x);
      return z;
    }
  }
  static public class Ambiguity extends Symbol
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.Symbol >
      alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.Symbol > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.Symbol >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public java.util.LisT < org.meta_environment.rascal.ast.Symbol >
    getParameters ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasParameters ()
  {
    return false;
  }
  public boolean isParameterizedSort ()
  {
    return false;
  }
  static public class ParameterizedSort extends Symbol
  {
/* sort:Sort "[[" parameters:{Symbol ","}+ "]]" -> Symbol {cons("ParameterizedSort")} */
    private ParameterizedSort ()
    {
    }
    /*package */ ParameterizedSort (ITree tree,
				    org.meta_environment.rascal.ast.Sort sort,
				    java.util.LisT <
				    org.meta_environment.rascal.ast.Symbol >
				    parameters)
    {
      this.tree = tree;
      this.sort = sort;
      this.parameters = parameters;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolParameterizedSort (this);
    }

    public boolean isParameterizedSort ()
    {
      return true;
    }

    public boolean hasSort ()
    {
      return true;
    }
    public boolean hasParameters ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Sort sort;
    public org.meta_environment.rascal.ast.Sort getSort ()
    {
      return sort;
    }
    private void $setSort (org.meta_environment.rascal.ast.Sort x)
    {
      this.sort = x;
    }
    public ParameterizedSort setSort (org.meta_environment.rascal.ast.Sort x)
    {
      ParameterizedSort z = new ParameterizedSort ();
      z.$setSort (x);
      return z;
    }
    private java.util.LisT < org.meta_environment.rascal.ast.Symbol >
      parameters;
    public java.util.LisT < org.meta_environment.rascal.ast.Symbol >
      getParameters ()
    {
      return parameters;
    }
    private void $setParameters (java.util.LisT <
				 org.meta_environment.rascal.ast.Symbol > x)
    {
      this.parameters = x;
    }
    public ParameterizedSort setParameters (java.util.LisT <
					    org.meta_environment.rascal.ast.
					    Symbol > x)
    {
      ParameterizedSort z = new ParameterizedSort ();
      z.$setParameters (x);
      return z;
    }
  }
  public boolean isEmpty ()
  {
    return false;
  }
  static public class Empty extends Symbol
  {
/* "(" ")" -> Symbol {cons("Empty")} */
    private Empty ()
    {
    }
    /*package */ Empty (ITree tree)
    {
      this.tree = tree;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolEmpty (this);
    }

    public boolean isEmpty ()
    {
      return true;
    }
  }
  public org.meta_environment.rascal.ast.Symbol getHead ()
  {
    throw new UnsupportedOperationException ();
  }
  public java.util.LisT < org.meta_environment.rascal.ast.Symbol > getTail ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasHead ()
  {
    return false;
  }
  public boolean hasTail ()
  {
    return false;
  }
  public boolean isSequence ()
  {
    return false;
  }
  static public class Sequence extends Symbol
  {
/* "(" head:Symbol tail:Symbol+ ")" -> Symbol {cons("Sequence")} */
    private Sequence ()
    {
    }
    /*package */ Sequence (ITree tree,
			   org.meta_environment.rascal.ast.Symbol head,
			   java.util.LisT <
			   org.meta_environment.rascal.ast.Symbol > tail)
    {
      this.tree = tree;
      this.head = head;
      this.tail = tail;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolSequence (this);
    }

    public boolean isSequence ()
    {
      return true;
    }

    public boolean hasHead ()
    {
      return true;
    }
    public boolean hasTail ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Symbol head;
    public org.meta_environment.rascal.ast.Symbol getHead ()
    {
      return head;
    }
    private void $setHead (org.meta_environment.rascal.ast.Symbol x)
    {
      this.head = x;
    }
    public Sequence setHead (org.meta_environment.rascal.ast.Symbol x)
    {
      Sequence z = new Sequence ();
      z.$setHead (x);
      return z;
    }
    private java.util.LisT < org.meta_environment.rascal.ast.Symbol > tail;
    public java.util.LisT < org.meta_environment.rascal.ast.Symbol >
      getTail ()
    {
      return tail;
    }
    private void $setTail (java.util.LisT <
			   org.meta_environment.rascal.ast.Symbol > x)
    {
      this.tail = x;
    }
    public Sequence setTail (java.util.LisT <
			     org.meta_environment.rascal.ast.Symbol > x)
    {
      Sequence z = new Sequence ();
      z.$setTail (x);
      return z;
    }
  }
  public org.meta_environment.rascal.ast.Symbol getSymbol ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasSymbol ()
  {
    return false;
  }
  public boolean isOptional ()
  {
    return false;
  }
  static public class Optional extends Symbol
  {
/* symbol:Symbol "?" -> Symbol {cons("Optional")} */
    private Optional ()
    {
    }
    /*package */ Optional (ITree tree,
			   org.meta_environment.rascal.ast.Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolOptional (this);
    }

    public boolean isOptional ()
    {
      return true;
    }

    public boolean hasSymbol ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Symbol symbol;
    public org.meta_environment.rascal.ast.Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      this.symbol = x;
    }
    public Optional setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      Optional z = new Optional ();
      z.$setSymbol (x);
      return z;
    }
  }
  public boolean isIter ()
  {
    return false;
  }
  static public class Iter extends Symbol
  {
/* symbol:Symbol "+" -> Symbol {cons("Iter")} */
    private Iter ()
    {
    }
    /*package */ Iter (ITree tree,
		       org.meta_environment.rascal.ast.Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolIter (this);
    }

    public boolean isIter ()
    {
      return true;
    }

    public boolean hasSymbol ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Symbol symbol;
    public org.meta_environment.rascal.ast.Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      this.symbol = x;
    }
    public Iter setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      Iter z = new Iter ();
      z.$setSymbol (x);
      return z;
    }
  }
  public boolean isIterStar ()
  {
    return false;
  }
  static public class IterStar extends Symbol
  {
/* symbol:Symbol "*" -> Symbol {cons("IterStar")} */
    private IterStar ()
    {
    }
    /*package */ IterStar (ITree tree,
			   org.meta_environment.rascal.ast.Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolIterStar (this);
    }

    public boolean isIterStar ()
    {
      return true;
    }

    public boolean hasSymbol ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Symbol symbol;
    public org.meta_environment.rascal.ast.Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      this.symbol = x;
    }
    public IterStar setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      IterStar z = new IterStar ();
      z.$setSymbol (x);
      return z;
    }
  }
  public org.meta_environment.rascal.ast.StrCon getSep ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasSep ()
  {
    return false;
  }
  public boolean isIterSep ()
  {
    return false;
  }
  static public class IterSep extends Symbol
  {
/* "{" symbol:Symbol sep:StrCon "}" "+" -> Symbol {cons("IterSep")} */
    private IterSep ()
    {
    }
    /*package */ IterSep (ITree tree,
			  org.meta_environment.rascal.ast.Symbol symbol,
			  org.meta_environment.rascal.ast.StrCon sep)
    {
      this.tree = tree;
      this.symbol = symbol;
      this.sep = sep;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolIterSep (this);
    }

    public boolean isIterSep ()
    {
      return true;
    }

    public boolean hasSymbol ()
    {
      return true;
    }
    public boolean hasSep ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Symbol symbol;
    public org.meta_environment.rascal.ast.Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      this.symbol = x;
    }
    public IterSep setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      IterSep z = new IterSep ();
      z.$setSymbol (x);
      return z;
    }
    private org.meta_environment.rascal.ast.StrCon sep;
    public org.meta_environment.rascal.ast.StrCon getSep ()
    {
      return sep;
    }
    private void $setSep (org.meta_environment.rascal.ast.StrCon x)
    {
      this.sep = x;
    }
    public IterSep setSep (org.meta_environment.rascal.ast.StrCon x)
    {
      IterSep z = new IterSep ();
      z.$setSep (x);
      return z;
    }
  }
  public boolean isIterStarSep ()
  {
    return false;
  }
  static public class IterStarSep extends Symbol
  {
/* "{" symbol:Symbol sep:StrCon "}" "*" -> Symbol {cons("IterStarSep")} */
    private IterStarSep ()
    {
    }
    /*package */ IterStarSep (ITree tree,
			      org.meta_environment.rascal.ast.Symbol symbol,
			      org.meta_environment.rascal.ast.StrCon sep)
    {
      this.tree = tree;
      this.symbol = symbol;
      this.sep = sep;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolIterStarSep (this);
    }

    public boolean isIterStarSep ()
    {
      return true;
    }

    public boolean hasSymbol ()
    {
      return true;
    }
    public boolean hasSep ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Symbol symbol;
    public org.meta_environment.rascal.ast.Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      this.symbol = x;
    }
    public IterStarSep setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      IterStarSep z = new IterStarSep ();
      z.$setSymbol (x);
      return z;
    }
    private org.meta_environment.rascal.ast.StrCon sep;
    public org.meta_environment.rascal.ast.StrCon getSep ()
    {
      return sep;
    }
    private void $setSep (org.meta_environment.rascal.ast.StrCon x)
    {
      this.sep = x;
    }
    public IterStarSep setSep (org.meta_environment.rascal.ast.StrCon x)
    {
      IterStarSep z = new IterStarSep ();
      z.$setSep (x);
      return z;
    }
  }
  public org.meta_environment.rascal.ast.Symbol getLhs ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Symbol getRhs ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasLhs ()
  {
    return false;
  }
  public boolean hasRhs ()
  {
    return false;
  }
  public boolean isAlternative ()
  {
    return false;
  }
  static public class Alternative extends Symbol
  {
/* lhs:Symbol "|" rhs:Symbol -> Symbol {right, cons("Alternative")} */
    private Alternative ()
    {
    }
    /*package */ Alternative (ITree tree,
			      org.meta_environment.rascal.ast.Symbol lhs,
			      org.meta_environment.rascal.ast.Symbol rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolAlternative (this);
    }

    public boolean isAlternative ()
    {
      return true;
    }

    public boolean hasLhs ()
    {
      return true;
    }
    public boolean hasRhs ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Symbol lhs;
    public org.meta_environment.rascal.ast.Symbol getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.Symbol x)
    {
      this.lhs = x;
    }
    public Alternative setLhs (org.meta_environment.rascal.ast.Symbol x)
    {
      Alternative z = new Alternative ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Symbol rhs;
    public org.meta_environment.rascal.ast.Symbol getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.Symbol x)
    {
      this.rhs = x;
    }
    public Alternative setRhs (org.meta_environment.rascal.ast.Symbol x)
    {
      Alternative z = new Alternative ();
      z.$setRhs (x);
      return z;
    }
  }
  public org.meta_environment.rascal.ast.CharClass getCharClass ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasCharClass ()
  {
    return false;
  }
  public boolean isCharacterClass ()
  {
    return false;
  }
  static public class CharacterClass extends Symbol
  {
/* charClass:CharClass -> Symbol {cons("CharacterClass")} */
    private CharacterClass ()
    {
    }
    /*package */ CharacterClass (ITree tree,
				 org.meta_environment.rascal.ast.
				 CharClass charClass)
    {
      this.tree = tree;
      this.charClass = charClass;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolCharacterClass (this);
    }

    public boolean isCharacterClass ()
    {
      return true;
    }

    public boolean hasCharClass ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.CharClass charClass;
    public org.meta_environment.rascal.ast.CharClass getCharClass ()
    {
      return charClass;
    }
    private void $setCharClass (org.meta_environment.rascal.ast.CharClass x)
    {
      this.charClass = x;
    }
    public CharacterClass setCharClass (org.meta_environment.rascal.ast.
					CharClass x)
    {
      CharacterClass z = new CharacterClass ();
      z.$setCharClass (x);
      return z;
    }
  }
  public boolean isLiftedSymbol ()
  {
    return false;
  }
  static public class LiftedSymbol extends Symbol
  {
/* "`" symbol:Symbol "`" -> Symbol {cons("LiftedSymbol")} */
    private LiftedSymbol ()
    {
    }
    /*package */ LiftedSymbol (ITree tree,
			       org.meta_environment.rascal.ast.Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolLiftedSymbol (this);
    }

    public boolean isLiftedSymbol ()
    {
      return true;
    }

    public boolean hasSymbol ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Symbol symbol;
    public org.meta_environment.rascal.ast.Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      this.symbol = x;
    }
    public LiftedSymbol setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      LiftedSymbol z = new LiftedSymbol ();
      z.$setSymbol (x);
      return z;
    }
  }
  public org.meta_environment.rascal.ast.StrCon getString ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasString ()
  {
    return false;
  }
  public boolean isLiteral ()
  {
    return false;
  }
  static public class Literal extends Symbol
  {
/* string:StrCon -> Symbol {cons("Literal")} */
    private Literal ()
    {
    }
    /*package */ Literal (ITree tree,
			  org.meta_environment.rascal.ast.StrCon string)
    {
      this.tree = tree;
      this.string = string;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolLiteral (this);
    }

    public boolean isLiteral ()
    {
      return true;
    }

    public boolean hasString ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.StrCon string;
    public org.meta_environment.rascal.ast.StrCon getString ()
    {
      return string;
    }
    private void $setString (org.meta_environment.rascal.ast.StrCon x)
    {
      this.string = x;
    }
    public Literal setString (org.meta_environment.rascal.ast.StrCon x)
    {
      Literal z = new Literal ();
      z.$setString (x);
      return z;
    }
  }
  public org.meta_environment.rascal.ast.
    SingleQuotedStrCon getSingelQuotedString ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasSingelQuotedString ()
  {
    return false;
  }
  public boolean isCaseInsensitiveLiteral ()
  {
    return false;
  }
  static public class CaseInsensitiveLiteral extends Symbol
  {
/* singelQuotedString:SingleQuotedStrCon -> Symbol {cons("CaseInsensitiveLiteral")} */
    private CaseInsensitiveLiteral ()
    {
    }
    /*package */ CaseInsensitiveLiteral (ITree tree,
					 org.meta_environment.rascal.ast.
					 SingleQuotedStrCon
					 singelQuotedString)
    {
      this.tree = tree;
      this.singelQuotedString = singelQuotedString;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItSymbolCaseInsensitiveLiteral (this);
    }

    public boolean isCaseInsensitiveLiteral ()
    {
      return true;
    }

    public boolean hasSingelQuotedString ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.
      SingleQuotedStrCon singelQuotedString;
    public org.meta_environment.rascal.ast.
      SingleQuotedStrCon getSingelQuotedString ()
    {
      return singelQuotedString;
    }
    private void $setSingelQuotedString (org.meta_environment.rascal.ast.
					 SingleQuotedStrCon x)
    {
      this.singelQuotedString = x;
    }
    public CaseInsensitiveLiteral setSingelQuotedString (org.meta_environment.
							 rascal.ast.
							 SingleQuotedStrCon x)
    {
      CaseInsensitiveLiteral z = new CaseInsensitiveLiteral ();
      z.$setSingelQuotedString (x);
      return z;
    }
  }
}
