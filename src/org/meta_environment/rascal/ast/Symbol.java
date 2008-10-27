package org.meta_environment.rascal.ast;
public abstract class Symbol extends AbstractAST
{
  public class Sort extends Symbol
  {
    private Sort sort;

    private Sort ()
    {
    }
    /*package */ Sort (ITree tree, Sort sort)
    {
      this.tree = tree;
      this.sort = sort;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSortSymbol (this);
    }
    private final Sort sort;
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
      z = new Sort ();
      z.privateSetsort (x);
      return z;
    }
  }
  public class ParameterizedSort extends Symbol
  {
    private Sort sort;
    private List < Symbol > parameters;

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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitParameterizedSortSymbol (this);
    }
    private final Sort sort;
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
      z = new ParameterizedSort ();
      z.privateSetsort (x);
      return z;
    }
    private final List < Symbol > parameters;
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
      z = new ParameterizedSort ();
      z.privateSetparameters (x);
      return z;
    }
  }
  public class Empty extends Symbol
  {
    private Empty ()
    {
    }
    /*package */ Empty (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitEmptySymbol (this);
    }
  }
  public class Sequence extends Symbol
  {
    private Symbol head;
    private List < Symbol > tail;

    private Sequence ()
    {
    }
    /*package */ Sequence (ITree tree, Symbol head, List < Symbol > tail)
    {
      this.tree = tree;
      this.head = head;
      this.tail = tail;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSequenceSymbol (this);
    }
    private final Symbol head;
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
      z = new Sequence ();
      z.privateSethead (x);
      return z;
    }
    private final List < Symbol > tail;
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
      z = new Sequence ();
      z.privateSettail (x);
      return z;
    }
  }
  public class Optional extends Symbol
  {
    private Symbol symbol;

    private Optional ()
    {
    }
    /*package */ Optional (ITree tree, Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitOptionalSymbol (this);
    }
    private final Symbol symbol;
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
      z = new Optional ();
      z.privateSetsymbol (x);
      return z;
    }
  }
  public class Iter extends Symbol
  {
    private Symbol symbol;

    private Iter ()
    {
    }
    /*package */ Iter (ITree tree, Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIterSymbol (this);
    }
    private final Symbol symbol;
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
      z = new Iter ();
      z.privateSetsymbol (x);
      return z;
    }
  }
  public class IterStar extends Symbol
  {
    private Symbol symbol;

    private IterStar ()
    {
    }
    /*package */ IterStar (ITree tree, Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIterStarSymbol (this);
    }
    private final Symbol symbol;
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
      z = new IterStar ();
      z.privateSetsymbol (x);
      return z;
    }
  }
  public class IterSep extends Symbol
  {
    private Symbol symbol;
    private StrCon sep;

    private IterSep ()
    {
    }
    /*package */ IterSep (ITree tree, Symbol symbol, StrCon sep)
    {
      this.tree = tree;
      this.symbol = symbol;
      this.sep = sep;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIterSepSymbol (this);
    }
    private final Symbol symbol;
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
      z = new IterSep ();
      z.privateSetsymbol (x);
      return z;
    }
    private final StrCon sep;
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
      z = new IterSep ();
      z.privateSetsep (x);
      return z;
    }
  }
  public class IterStarSep extends Symbol
  {
    private Symbol symbol;
    private StrCon sep;

    private IterStarSep ()
    {
    }
    /*package */ IterStarSep (ITree tree, Symbol symbol, StrCon sep)
    {
      this.tree = tree;
      this.symbol = symbol;
      this.sep = sep;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIterStarSepSymbol (this);
    }
    private final Symbol symbol;
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
      z = new IterStarSep ();
      z.privateSetsymbol (x);
      return z;
    }
    private final StrCon sep;
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
      z = new IterStarSep ();
      z.privateSetsep (x);
      return z;
    }
  }
  public class Alternative extends Symbol
  {
    private Symbol lhs;
    private Symbol rhs;

    private Alternative ()
    {
    }
    /*package */ Alternative (ITree tree, Symbol lhs, Symbol rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAlternativeSymbol (this);
    }
    private final Symbol lhs;
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
      z = new Alternative ();
      z.privateSetlhs (x);
      return z;
    }
    private final Symbol rhs;
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
      z = new Alternative ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class CharacterClass extends Symbol
  {
    private CharClass charClass;

    private CharacterClass ()
    {
    }
    /*package */ CharacterClass (ITree tree, CharClass charClass)
    {
      this.tree = tree;
      this.charClass = charClass;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitCharacterClassSymbol (this);
    }
    private final CharClass charClass;
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
      z = new CharacterClass ();
      z.privateSetcharClass (x);
      return z;
    }
  }
  public class LiftedSymbol extends Symbol
  {
    private Symbol symbol;

    private LiftedSymbol ()
    {
    }
    /*package */ LiftedSymbol (ITree tree, Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitLiftedSymbolSymbol (this);
    }
    private final Symbol symbol;
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
      z = new LiftedSymbol ();
      z.privateSetsymbol (x);
      return z;
    }
  }
  public class Literal extends Symbol
  {
    private StrCon string;

    private Literal ()
    {
    }
    /*package */ Literal (ITree tree, StrCon string)
    {
      this.tree = tree;
      this.string = string;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitLiteralSymbol (this);
    }
    private final StrCon string;
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
      z = new Literal ();
      z.privateSetstring (x);
      return z;
    }
  }
  public class CaseInsensitiveLiteral extends Symbol
  {
    private SingleQuotedStrCon singelQuotedString;

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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitCaseInsensitiveLiteralSymbol (this);
    }
    private final SingleQuotedStrCon singelQuotedString;
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
      z = new CaseInsensitiveLiteral ();
      z.privateSetsingelQuotedString (x);
      return z;
    }
  }
prod2class (Symbol "?"->Symbol)
    prod2class (Symbol "*"->Symbol)
    prod2class (Symbol "+"->Symbol) prod2class (Symbol "|" Symbol->Symbol)}
