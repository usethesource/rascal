package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Symbol extends AbstractAST
{
  public class Sort extends Symbol
  {
/* sort:Sort -> Symbol {cons("Sort")} */
    private Sort ()
    {
    }
    /*package */ Sort (ITree tree, Sort sort)
    {
      this.tree = tree;
      this.sort = sort;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolSort (this);
    }
    private Sort sort;
    public Sort getSort ()
    {
      return sort;
    }
    private void $setSort (Sort x)
    {
      this.sort = x;
    }
    public Sort setSort (Sort x)
    {
      Sort z = new Sort ();
      z.$setSort (x);
      return z;
    }
  }
  public class Ambiguity extends Symbol
  {
    private final java.util.List < Symbol > alternatives;
    public Ambiguity (java.util.List < Symbol > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Symbol > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class ParameterizedSort extends Symbol
  {
/* sort:Sort "[[" parameters:{Symbol ","}+ "]]" -> Symbol {cons("ParameterizedSort")} */
    private ParameterizedSort ()
    {
    }
    /*package */ ParameterizedSort (ITree tree, Sort sort,
				    java.util.List < Symbol > parameters)
    {
      this.tree = tree;
      this.sort = sort;
    params2statements (java.util.List < Symbol > parameters)}
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolParameterizedSort (this);
    }
    private Sort sort;
    public Sort getSort ()
    {
      return sort;
    }
    private void $setSort (Sort x)
    {
      this.sort = x;
    }
    public ParameterizedSort setSort (Sort x)
    {
      ParameterizedSort z = new ParameterizedSort ();
      z.$setSort (x);
      return z;
    }
    private java.util.List < Symbol > parameters;
    public java.util.List < Symbol > getParameters ()
    {
      return parameters;
    }
    private void $setParameters (java.util.List < Symbol > x)
    {
      this.parameters = x;
    }
    public ParameterizedSort setParameters (java.util.List < Symbol > x)
    {
      ParameterizedSort z = new ParameterizedSort ();
      z.$setParameters (x);
      return z;
    }
  }
  public class Empty extends Symbol
  {
/* "(" ")" -> Symbol {cons("Empty")} */
    private Empty ()
    {
    }
    /*package */ Empty (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolEmpty (this);
    }
  }
  public class Sequence extends Symbol
  {
/* "(" head:Symbol tail:Symbol+ ")" -> Symbol {cons("Sequence")} */
    private Sequence ()
    {
    }
    /*package */ Sequence (ITree tree, Symbol head,
			   java.util.List < Symbol > tail)
    {
      this.tree = tree;
      this.head = head;
    params2statements (java.util.List < Symbol > tail)}
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolSequence (this);
    }
    private Symbol head;
    public Symbol getHead ()
    {
      return head;
    }
    private void $setHead (Symbol x)
    {
      this.head = x;
    }
    public Sequence setHead (Symbol x)
    {
      Sequence z = new Sequence ();
      z.$setHead (x);
      return z;
    }
    private java.util.List < Symbol > tail;
    public java.util.List < Symbol > getTail ()
    {
      return tail;
    }
    private void $setTail (java.util.List < Symbol > x)
    {
      this.tail = x;
    }
    public Sequence setTail (java.util.List < Symbol > x)
    {
      Sequence z = new Sequence ();
      z.$setTail (x);
      return z;
    }
  }
  public class Optional extends Symbol
  {
/* symbol:Symbol "?" -> Symbol {cons("Optional")} */
    private Optional ()
    {
    }
    /*package */ Optional (ITree tree, Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolOptional (this);
    }
    private Symbol symbol;
    public Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (Symbol x)
    {
      this.symbol = x;
    }
    public Optional setSymbol (Symbol x)
    {
      Optional z = new Optional ();
      z.$setSymbol (x);
      return z;
    }
  }
  public class Iter extends Symbol
  {
/* symbol:Symbol "+" -> Symbol {cons("Iter")} */
    private Iter ()
    {
    }
    /*package */ Iter (ITree tree, Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolIter (this);
    }
    private Symbol symbol;
    public Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (Symbol x)
    {
      this.symbol = x;
    }
    public Iter setSymbol (Symbol x)
    {
      Iter z = new Iter ();
      z.$setSymbol (x);
      return z;
    }
  }
  public class IterStar extends Symbol
  {
/* symbol:Symbol "*" -> Symbol {cons("IterStar")} */
    private IterStar ()
    {
    }
    /*package */ IterStar (ITree tree, Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolIterStar (this);
    }
    private Symbol symbol;
    public Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (Symbol x)
    {
      this.symbol = x;
    }
    public IterStar setSymbol (Symbol x)
    {
      IterStar z = new IterStar ();
      z.$setSymbol (x);
      return z;
    }
  }
  public class IterSep extends Symbol
  {
/* "{" symbol:Symbol sep:StrCon "}" "+" -> Symbol {cons("IterSep")} */
    private IterSep ()
    {
    }
    /*package */ IterSep (ITree tree, Symbol symbol, StrCon sep)
    {
      this.tree = tree;
      this.symbol = symbol;
      this.sep = sep;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolIterSep (this);
    }
    private Symbol symbol;
    public Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (Symbol x)
    {
      this.symbol = x;
    }
    public IterSep setSymbol (Symbol x)
    {
      IterSep z = new IterSep ();
      z.$setSymbol (x);
      return z;
    }
    private StrCon sep;
    public StrCon getSep ()
    {
      return sep;
    }
    private void $setSep (StrCon x)
    {
      this.sep = x;
    }
    public IterSep setSep (StrCon x)
    {
      IterSep z = new IterSep ();
      z.$setSep (x);
      return z;
    }
  }
  public class IterStarSep extends Symbol
  {
/* "{" symbol:Symbol sep:StrCon "}" "*" -> Symbol {cons("IterStarSep")} */
    private IterStarSep ()
    {
    }
    /*package */ IterStarSep (ITree tree, Symbol symbol, StrCon sep)
    {
      this.tree = tree;
      this.symbol = symbol;
      this.sep = sep;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolIterStarSep (this);
    }
    private Symbol symbol;
    public Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (Symbol x)
    {
      this.symbol = x;
    }
    public IterStarSep setSymbol (Symbol x)
    {
      IterStarSep z = new IterStarSep ();
      z.$setSymbol (x);
      return z;
    }
    private StrCon sep;
    public StrCon getSep ()
    {
      return sep;
    }
    private void $setSep (StrCon x)
    {
      this.sep = x;
    }
    public IterStarSep setSep (StrCon x)
    {
      IterStarSep z = new IterStarSep ();
      z.$setSep (x);
      return z;
    }
  }
  public class Alternative extends Symbol
  {
/* lhs:Symbol "|" rhs:Symbol -> Symbol {right, cons("Alternative")} */
    private Alternative ()
    {
    }
    /*package */ Alternative (ITree tree, Symbol lhs, Symbol rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolAlternative (this);
    }
    private Symbol lhs;
    public Symbol getLhs ()
    {
      return lhs;
    }
    private void $setLhs (Symbol x)
    {
      this.lhs = x;
    }
    public Alternative setLhs (Symbol x)
    {
      Alternative z = new Alternative ();
      z.$setLhs (x);
      return z;
    }
    private Symbol rhs;
    public Symbol getRhs ()
    {
      return rhs;
    }
    private void $setRhs (Symbol x)
    {
      this.rhs = x;
    }
    public Alternative setRhs (Symbol x)
    {
      Alternative z = new Alternative ();
      z.$setRhs (x);
      return z;
    }
  }
  public class CharacterClass extends Symbol
  {
/* charClass:CharClass -> Symbol {cons("CharacterClass")} */
    private CharacterClass ()
    {
    }
    /*package */ CharacterClass (ITree tree, CharClass charClass)
    {
      this.tree = tree;
      this.charClass = charClass;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolCharacterClass (this);
    }
    private CharClass charClass;
    public CharClass getCharClass ()
    {
      return charClass;
    }
    private void $setCharClass (CharClass x)
    {
      this.charClass = x;
    }
    public CharacterClass setCharClass (CharClass x)
    {
      CharacterClass z = new CharacterClass ();
      z.$setCharClass (x);
      return z;
    }
  }
  public class LiftedSymbol extends Symbol
  {
/* "`" symbol:Symbol "`" -> Symbol {cons("LiftedSymbol")} */
    private LiftedSymbol ()
    {
    }
    /*package */ LiftedSymbol (ITree tree, Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolLiftedSymbol (this);
    }
    private Symbol symbol;
    public Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (Symbol x)
    {
      this.symbol = x;
    }
    public LiftedSymbol setSymbol (Symbol x)
    {
      LiftedSymbol z = new LiftedSymbol ();
      z.$setSymbol (x);
      return z;
    }
  }
  public class Literal extends Symbol
  {
/* string:StrCon -> Symbol {cons("Literal")} */
    private Literal ()
    {
    }
    /*package */ Literal (ITree tree, StrCon string)
    {
      this.tree = tree;
      this.string = string;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolLiteral (this);
    }
    private StrCon string;
    public StrCon getString ()
    {
      return string;
    }
    private void $setString (StrCon x)
    {
      this.string = x;
    }
    public Literal setString (StrCon x)
    {
      Literal z = new Literal ();
      z.$setString (x);
      return z;
    }
  }
  public class CaseInsensitiveLiteral extends Symbol
  {
/* singelQuotedString:SingleQuotedStrCon -> Symbol {cons("CaseInsensitiveLiteral")} */
    private CaseInsensitiveLiteral ()
    {
    }
    /*package */ CaseInsensitiveLiteral (ITree tree,
					 SingleQuotedStrCon
					 singelQuotedString)
    {
      this.tree = tree;
      this.singelQuotedString = singelQuotedString;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolCaseInsensitiveLiteral (this);
    }
    private SingleQuotedStrCon singelQuotedString;
    public SingleQuotedStrCon getSingelQuotedString ()
    {
      return singelQuotedString;
    }
    private void $setSingelQuotedString (SingleQuotedStrCon x)
    {
      this.singelQuotedString = x;
    }
    public CaseInsensitiveLiteral setSingelQuotedString (SingleQuotedStrCon x)
    {
      CaseInsensitiveLiteral z = new CaseInsensitiveLiteral ();
      z.$setSingelQuotedString (x);
      return z;
    }
  }
  public class Lexical extends Symbol
  {
    /* Symbol "?" -> Symbol  */
  }
  public class Lexical extends Symbol
  {
    /* Symbol "*" -> Symbol  */
  }
  public class Lexical extends Symbol
  {
    /* Symbol "+" -> Symbol  */
  }
  public class Lexical extends Symbol
  {
    /* Symbol "|" Symbol -> Symbol  */
  }
}
