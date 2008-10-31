package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
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
    public OptCharRanges getOptionalCharRanges ()
    {
      return optionalCharRanges;
    }
    private void $setOptionalCharRanges (OptCharRanges x)
    {
      this.optionalCharRanges = x;
    }
    public SimpleCharclass setOptionalCharRanges (OptCharRanges x)
    {
      SimpleCharclass z = new SimpleCharclass ();
      z.$setOptionalCharRanges (x);
      return z;
    }
  }
  public class Ambiguity extends CharClass
  {
    private final java.util.List < CharClass > alternatives;
    public Ambiguity (java.util.List < CharClass > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < CharClass > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Bracket extends CharClass
  {
/* "(" CharClass ")" -> CharClass {bracket, avoid} */
    private Bracket ()
    {
    }
    /*package */ Bracket (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassBracket (this);
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
    public CharClass getCharClass ()
    {
      return charClass;
    }
    private void $setCharClass (CharClass x)
    {
      this.charClass = x;
    }
    public Complement setCharClass (CharClass x)
    {
      Complement z = new Complement ();
      z.$setCharClass (x);
      return z;
    }
  }
  public class Difference extends CharClass
  {
/* lhs:CharClass "/" rhs:CharClass -> CharClass {cons("Difference"), left, memo} */
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
    public CharClass getLhs ()
    {
      return lhs;
    }
    private void $setLhs (CharClass x)
    {
      this.lhs = x;
    }
    public Difference setLhs (CharClass x)
    {
      Difference z = new Difference ();
      z.$setLhs (x);
      return z;
    }
    private CharClass rhs;
    public CharClass getRhs ()
    {
      return rhs;
    }
    private void $setRhs (CharClass x)
    {
      this.rhs = x;
    }
    public Difference setRhs (CharClass x)
    {
      Difference z = new Difference ();
      z.$setRhs (x);
      return z;
    }
  }
  public class Intersection extends CharClass
  {
/* lhs:CharClass "/\\" rhs:CharClass -> CharClass {cons("Intersection"), left, memo} */
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
    public CharClass getLhs ()
    {
      return lhs;
    }
    private void $setLhs (CharClass x)
    {
      this.lhs = x;
    }
    public Intersection setLhs (CharClass x)
    {
      Intersection z = new Intersection ();
      z.$setLhs (x);
      return z;
    }
    private CharClass rhs;
    public CharClass getRhs ()
    {
      return rhs;
    }
    private void $setRhs (CharClass x)
    {
      this.rhs = x;
    }
    public Intersection setRhs (CharClass x)
    {
      Intersection z = new Intersection ();
      z.$setRhs (x);
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
    public CharClass getLhs ()
    {
      return lhs;
    }
    private void $setLhs (CharClass x)
    {
      this.lhs = x;
    }
    public Union setLhs (CharClass x)
    {
      Union z = new Union ();
      z.$setLhs (x);
      return z;
    }
    private CharClass rhs;
    public CharClass getRhs ()
    {
      return rhs;
    }
    private void $setRhs (CharClass x)
    {
      this.rhs = x;
    }
    public Union setRhs (CharClass x)
    {
      Union z = new Union ();
      z.$setRhs (x);
      return z;
    }
  }
}
