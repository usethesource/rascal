package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class CharClass extends AbstractAST
{
  public class SimpleCharclass extends CharClass
  {
/* "[" optionalCharRanges:OptCharRanges "]" -> CharClass {cons("SimpleCharclass")} */
    private SimpleCharclass ()
    {
    }
    /*package */ SimpleCharclass (ITree tree,
				  OptCharRanges optionalCharRanges)
    {
      this.tree = tree;
      this.optionalCharRanges = optionalCharRanges;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassSimpleCharclass (this);
    }
    private OptCharRanges optionalCharRanges;
    public OptCharRanges getoptionalCharRanges ()
    {
      return optionalCharRanges;
    }
    private void $setoptionalCharRanges (OptCharRanges x)
    {
      this.optionalCharRanges = x;
    }
    public SimpleCharclass setoptionalCharRanges (OptCharRanges x)
    {
      SimpleCharclass z = new SimpleCharclass ();
      z.$setoptionalCharRanges (x);
      return z;
    }
  }
  public class Ambiguity extends CharClass
  {
    private final List < CharClass > alternatives;
    public Ambiguity (List < CharClass > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < CharClass > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Complement extends CharClass
  {
/* "~" charClass:CharClass -> CharClass {cons("Complement")} */
    private Complement ()
    {
    }
    /*package */ Complement (ITree tree, CharClass charClass)
    {
      this.tree = tree;
      this.charClass = charClass;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassComplement (this);
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
    public Complement setcharClass (CharClass x)
    {
      Complement z = new Complement ();
      z.$setcharClass (x);
      return z;
    }
  }
  public class Difference extends CharClass
  {
/* lhs:CharClass "/" rhs:CharClass -> CharClass {cons("Difference"), left,memo} */
    private Difference ()
    {
    }
    /*package */ Difference (ITree tree, CharClass lhs, CharClass rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassDifference (this);
    }
    private CharClass lhs;
    public CharClass getlhs ()
    {
      return lhs;
    }
    private void $setlhs (CharClass x)
    {
      this.lhs = x;
    }
    public Difference setlhs (CharClass x)
    {
      Difference z = new Difference ();
      z.$setlhs (x);
      return z;
    }
    private CharClass rhs;
    public CharClass getrhs ()
    {
      return rhs;
    }
    private void $setrhs (CharClass x)
    {
      this.rhs = x;
    }
    public Difference setrhs (CharClass x)
    {
      Difference z = new Difference ();
      z.$setrhs (x);
      return z;
    }
  }
  public class Intersection extends CharClass
  {
/* lhs:CharClass "/\\" rhs:CharClass -> CharClass {cons("Intersection"), left,memo} */
    private Intersection ()
    {
    }
    /*package */ Intersection (ITree tree, CharClass lhs, CharClass rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassIntersection (this);
    }
    private CharClass lhs;
    public CharClass getlhs ()
    {
      return lhs;
    }
    private void $setlhs (CharClass x)
    {
      this.lhs = x;
    }
    public Intersection setlhs (CharClass x)
    {
      Intersection z = new Intersection ();
      z.$setlhs (x);
      return z;
    }
    private CharClass rhs;
    public CharClass getrhs ()
    {
      return rhs;
    }
    private void $setrhs (CharClass x)
    {
      this.rhs = x;
    }
    public Intersection setrhs (CharClass x)
    {
      Intersection z = new Intersection ();
      z.$setrhs (x);
      return z;
    }
  }
  public class Union extends CharClass
  {
/* lhs:CharClass "\\/" rhs:CharClass -> CharClass {cons("Union"), left} */
    private Union ()
    {
    }
    /*package */ Union (ITree tree, CharClass lhs, CharClass rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassUnion (this);
    }
    private CharClass lhs;
    public CharClass getlhs ()
    {
      return lhs;
    }
    private void $setlhs (CharClass x)
    {
      this.lhs = x;
    }
    public Union setlhs (CharClass x)
    {
      Union z = new Union ();
      z.$setlhs (x);
      return z;
    }
    private CharClass rhs;
    public CharClass getrhs ()
    {
      return rhs;
    }
    private void $setrhs (CharClass x)
    {
      this.rhs = x;
    }
    public Union setrhs (CharClass x)
    {
      Union z = new Union ();
      z.$setrhs (x);
      return z;
    }
  }
  public class Lexical extends CharClass
  {
    /* "(" CharClass ")" -> CharClass {bracket, avoid} */
  }
  public class Lexical extends CharClass
  {
    /* "~" CharClass -> CharClass  */
  }
  public class Lexical extends CharClass
  {
    /* CharClass "/" CharClass -> CharClass  */
  }
  public class Lexical extends CharClass
  {
    /* CharClass "/\\" CharClass -> CharClass  */
  }
  public class Lexical extends CharClass
  {
    /* CharClass "\\/" CharClass -> CharClass  */
  }
}
