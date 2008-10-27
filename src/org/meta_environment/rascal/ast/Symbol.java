package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
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
      return visitor.visitSortSymbol (this);
    }
    private Sort sort;
    public Sort getsort ()
    {
      return sort;
    }
    private void privateSetsort (Sort x)
    {
      this.sort = x;
    }
    public Sort setsort (Sort x)
    {
      Sort z = new Sort ();
      z.privateSetsort (x);
      return z;
    }
  }
  public class ParameterizedSort extends Symbol
  {
/* sort:Sort "[[" parameters:{Symbol ","}+ "]]" -> Symbol {cons("ParameterizedSort")} */
    private ParameterizedSort ()
    {
    }
    /*package */ ParameterizedSort (ITree tree, Sort sort,
				    List < Symbol > parameters)
    {
      this.tree = tree;
      this.sort = sort;
      this.parameters = parameters;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitParameterizedSortSymbol (this);
    }
    private Sort sort;
    public Sort getsort ()
    {
      return sort;
    }
    private void privateSetsort (Sort x)
    {
      this.sort = x;
    }
    public ParameterizedSort setsort (Sort x)
    {
      ParameterizedSort z = new ParameterizedSort ();
      z.privateSetsort (x);
      return z;
    }
    private List < Symbol > parameters;
    public List < Symbol > getparameters ()
    {
      return parameters;
    }
    private void privateSetparameters (List < Symbol > x)
    {
      this.parameters = x;
    }
    public ParameterizedSort setparameters (List < Symbol > x)
    {
      ParameterizedSort z = new ParameterizedSort ();
      z.privateSetparameters (x);
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
      return visitor.visitEmptySymbol (this);
    }
  }
  public class Sequence extends Symbol
  {
/* "(" head:Symbol tail:Symbol+ ")" -> Symbol {cons("Sequence")} */
    private Sequence ()
    {
    }
    /*package */ Sequence (ITree tree, Symbol head, List < Symbol > tail)
    {
      this.tree = tree;
      this.head = head;
      this.tail = tail;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSequenceSymbol (this);
    }
    private Symbol head;
    public Symbol gethead ()
    {
      return head;
    }
    private void privateSethead (Symbol x)
    {
      this.head = x;
    }
    public Sequence sethead (Symbol x)
    {
      Sequence z = new Sequence ();
      z.privateSethead (x);
      return z;
    }
    private List < Symbol > tail;
    public List < Symbol > gettail ()
    {
      return tail;
    }
    private void privateSettail (List < Symbol > x)
    {
      this.tail = x;
    }
    public Sequence settail (List < Symbol > x)
    {
      Sequence z = new Sequence ();
      z.privateSettail (x);
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
      return visitor.visitOptionalSymbol (this);
    }
    private Symbol symbol;
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void privateSetsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public Optional setsymbol (Symbol x)
    {
      Optional z = new Optional ();
      z.privateSetsymbol (x);
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
      return visitor.visitIterSymbol (this);
    }
    private Symbol symbol;
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void privateSetsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public Iter setsymbol (Symbol x)
    {
      Iter z = new Iter ();
      z.privateSetsymbol (x);
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
      return visitor.visitIterStarSymbol (this);
    }
    private Symbol symbol;
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void privateSetsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public IterStar setsymbol (Symbol x)
    {
      IterStar z = new IterStar ();
      z.privateSetsymbol (x);
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
      return visitor.visitIterSepSymbol (this);
    }
    private Symbol symbol;
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void privateSetsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public IterSep setsymbol (Symbol x)
    {
      IterSep z = new IterSep ();
      z.privateSetsymbol (x);
      return z;
    }
    private StrCon sep;
    public StrCon getsep ()
    {
      return sep;
    }
    private void privateSetsep (StrCon x)
    {
      this.sep = x;
    }
    public IterSep setsep (StrCon x)
    {
      IterSep z = new IterSep ();
      z.privateSetsep (x);
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
      return visitor.visitIterStarSepSymbol (this);
    }
    private Symbol symbol;
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void privateSetsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public IterStarSep setsymbol (Symbol x)
    {
      IterStarSep z = new IterStarSep ();
      z.privateSetsymbol (x);
      return z;
    }
    private StrCon sep;
    public StrCon getsep ()
    {
      return sep;
    }
    private void privateSetsep (StrCon x)
    {
      this.sep = x;
    }
    public IterStarSep setsep (StrCon x)
    {
      IterStarSep z = new IterStarSep ();
      z.privateSetsep (x);
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
      return visitor.visitAlternativeSymbol (this);
    }
    private Symbol lhs;
    public Symbol getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Symbol x)
    {
      this.lhs = x;
    }
    public Alternative setlhs (Symbol x)
    {
      Alternative z = new Alternative ();
      z.privateSetlhs (x);
      return z;
    }
    private Symbol rhs;
    public Symbol getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Symbol x)
    {
      this.rhs = x;
    }
    public Alternative setrhs (Symbol x)
    {
      Alternative z = new Alternative ();
      z.privateSetrhs (x);
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
      return visitor.visitCharacterClassSymbol (this);
    }
    private CharClass charClass;
    public CharClass getcharClass ()
    {
      return charClass;
    }
    private void privateSetcharClass (CharClass x)
    {
      this.charClass = x;
    }
    public CharacterClass setcharClass (CharClass x)
    {
      CharacterClass z = new CharacterClass ();
      z.privateSetcharClass (x);
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
      return visitor.visitLiftedSymbolSymbol (this);
    }
    private Symbol symbol;
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void privateSetsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public LiftedSymbol setsymbol (Symbol x)
    {
      LiftedSymbol z = new LiftedSymbol ();
      z.privateSetsymbol (x);
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
      return visitor.visitLiteralSymbol (this);
    }
    private StrCon string;
    public StrCon getstring ()
    {
      return string;
    }
    private void privateSetstring (StrCon x)
    {
      this.string = x;
    }
    public Literal setstring (StrCon x)
    {
      Literal z = new Literal ();
      z.privateSetstring (x);
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
      return visitor.visitCaseInsensitiveLiteralSymbol (this);
    }
    private SingleQuotedStrCon singelQuotedString;
    public SingleQuotedStrCon getsingelQuotedString ()
    {
      return singelQuotedString;
    }
    private void privateSetsingelQuotedString (SingleQuotedStrCon x)
    {
      this.singelQuotedString = x;
    }
    public CaseInsensitiveLiteral setsingelQuotedString (SingleQuotedStrCon x)
    {
      CaseInsensitiveLiteral z = new CaseInsensitiveLiteral ();
      z.privateSetsingelQuotedString (x);
      return z;
    }
  }
prod2class (Symbol "?"->Symbol)
    prod2class (Symbol "*"->Symbol)
    prod2class (Symbol "+"->Symbol) prod2class (Symbol "|" Symbol->Symbol)}
