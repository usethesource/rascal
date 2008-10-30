package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
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
    public Sort getsort ()
    {
      return sort;
    }
    private void $setsort (Sort x)
    {
      this.sort = x;
    }
    public Sort setsort (Sort x)
    {
      Sort z = new Sort ();
      z.$setsort (x);
      return z;
    }
  }
  public class Ambiguity extends Symbol
  {
    private final List < Symbol > alternatives;
    public Ambiguity (List < Symbol > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Symbol > getAlternatives ()
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
				    List < Symbol > parameters)
    {
      this.tree = tree;
      this.sort = sort;
      this.parameters = parameters;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolParameterizedSort (this);
    }
    private Sort sort;
    public Sort getsort ()
    {
      return sort;
    }
    private void $setsort (Sort x)
    {
      this.sort = x;
    }
    public ParameterizedSort setsort (Sort x)
    {
      ParameterizedSort z = new ParameterizedSort ();
      z.$setsort (x);
      return z;
    }
    private List < Symbol > parameters;
    public List < Symbol > getparameters ()
    {
      return parameters;
    }
    private void $setparameters (List < Symbol > x)
    {
      this.parameters = x;
    }
    public ParameterizedSort setparameters (List < Symbol > x)
    {
      ParameterizedSort z = new ParameterizedSort ();
      z.$setparameters (x);
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
    /*package */ Sequence (ITree tree, Symbol head, List < Symbol > tail)
    {
      this.tree = tree;
      this.head = head;
      this.tail = tail;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSymbolSequence (this);
    }
    private Symbol head;
    public Symbol gethead ()
    {
      return head;
    }
    private void $sethead (Symbol x)
    {
      this.head = x;
    }
    public Sequence sethead (Symbol x)
    {
      Sequence z = new Sequence ();
      z.$sethead (x);
      return z;
    }
    private List < Symbol > tail;
    public List < Symbol > gettail ()
    {
      return tail;
    }
    private void $settail (List < Symbol > x)
    {
      this.tail = x;
    }
    public Sequence settail (List < Symbol > x)
    {
      Sequence z = new Sequence ();
      z.$settail (x);
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
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void $setsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public Optional setsymbol (Symbol x)
    {
      Optional z = new Optional ();
      z.$setsymbol (x);
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
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void $setsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public Iter setsymbol (Symbol x)
    {
      Iter z = new Iter ();
      z.$setsymbol (x);
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
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void $setsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public IterStar setsymbol (Symbol x)
    {
      IterStar z = new IterStar ();
      z.$setsymbol (x);
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
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void $setsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public IterSep setsymbol (Symbol x)
    {
      IterSep z = new IterSep ();
      z.$setsymbol (x);
      return z;
    }
    private StrCon sep;
    public StrCon getsep ()
    {
      return sep;
    }
    private void $setsep (StrCon x)
    {
      this.sep = x;
    }
    public IterSep setsep (StrCon x)
    {
      IterSep z = new IterSep ();
      z.$setsep (x);
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
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void $setsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public IterStarSep setsymbol (Symbol x)
    {
      IterStarSep z = new IterStarSep ();
      z.$setsymbol (x);
      return z;
    }
    private StrCon sep;
    public StrCon getsep ()
    {
      return sep;
    }
    private void $setsep (StrCon x)
    {
      this.sep = x;
    }
    public IterStarSep setsep (StrCon x)
    {
      IterStarSep z = new IterStarSep ();
      z.$setsep (x);
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
    public Symbol getlhs ()
    {
      return lhs;
    }
    private void $setlhs (Symbol x)
    {
      this.lhs = x;
    }
    public Alternative setlhs (Symbol x)
    {
      Alternative z = new Alternative ();
      z.$setlhs (x);
      return z;
    }
    private Symbol rhs;
    public Symbol getrhs ()
    {
      return rhs;
    }
    private void $setrhs (Symbol x)
    {
      this.rhs = x;
    }
    public Alternative setrhs (Symbol x)
    {
      Alternative z = new Alternative ();
      z.$setrhs (x);
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
    public CharClass getcharClass ()
    {
      return charClass;
    }
    private void $setcharClass (CharClass x)
    {
      this.charClass = x;
    }
    public CharacterClass setcharClass (CharClass x)
    {
      CharacterClass z = new CharacterClass ();
      z.$setcharClass (x);
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
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void $setsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public LiftedSymbol setsymbol (Symbol x)
    {
      LiftedSymbol z = new LiftedSymbol ();
      z.$setsymbol (x);
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
    public StrCon getstring ()
    {
      return string;
    }
    private void $setstring (StrCon x)
    {
      this.string = x;
    }
    public Literal setstring (StrCon x)
    {
      Literal z = new Literal ();
      z.$setstring (x);
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
    public SingleQuotedStrCon getsingelQuotedString ()
    {
      return singelQuotedString;
    }
    private void $setsingelQuotedString (SingleQuotedStrCon x)
    {
      this.singelQuotedString = x;
    }
    public CaseInsensitiveLiteral setsingelQuotedString (SingleQuotedStrCon x)
    {
      CaseInsensitiveLiteral z = new CaseInsensitiveLiteral ();
      z.$setsingelQuotedString (x);
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
