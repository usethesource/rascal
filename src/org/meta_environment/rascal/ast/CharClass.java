package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class CharClass extends AbstractAST
{
  static public class SimpleCharclass extends CharClass
  {
/* "[" optionalCharRanges:OptCharRanges "]" -> CharClass {cons("SimpleCharclass")} */
    private SimpleCharclass ()
    {
    }
    /*package */ SimpleCharclass (ITree tree,
				  org.meta_environment.rascal.ast.
				  OptCharRanges optionalCharRanges)
    {
      this.tree = tree;
      this.optionalCharRanges = optionalCharRanges;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassSimpleCharclass (this);
    }
    private org.meta_environment.rascal.ast.OptCharRanges optionalCharRanges;
    public org.meta_environment.rascal.ast.
      OptCharRanges getOptionalCharRanges ()
    {
      return optionalCharRanges;
    }
    private void $setOptionalCharRanges (org.meta_environment.rascal.ast.
					 OptCharRanges x)
    {
      this.optionalCharRanges = x;
    }
    public SimpleCharclass setOptionalCharRanges (org.meta_environment.rascal.
						  ast.OptCharRanges x)
    {
      org.meta_environment.rascal.ast.SimpleCharclass z =
	new SimpleCharclass ();
      z.$setOptionalCharRanges (x);
      return z;
    }
  }
  static public class Ambiguity extends CharClass
  {
    public CharClass.Ambiguity makeCharClassAmbiguity (java.util.List <
						       CharClass >
						       alternatives)
    {
      CharClass.Ambiguity amb = new CharClass.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (CharClass.Ambiguity) table.get (amb);
    }
    private final java.util.List < CharClass > alternatives;
    public Ambiguity (java.util.List < CharClass > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < CharClass > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Bracket extends CharClass
  {
/* "(" CharClass ")" -> CharClass {bracket, avoid} */
    public CharClass.Bracket makeCharClassBracket (ITree tree)
    {
      CharClass.Bracket x = new CharClass.Bracket (tree);
      if (!table.containsKey (x))
	{
	  table.put (x, x);
	}
      return (CharClass.Bracket) table.get (x);
    }

    public CharClass visitCharClassBracket (CharClass.Bracket x);
    public CharClass visitCharClassBracket (CharClass.Bracket x)
    {
      return x;
    }

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
  static public class Complement extends CharClass
  {
/* "~" charClass:CharClass -> CharClass {cons("Complement")} */
    private Complement ()
    {
    }
    /*package */ Complement (ITree tree,
			     org.meta_environment.rascal.ast.
			     CharClass charClass)
    {
      this.tree = tree;
      this.charClass = charClass;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassComplement (this);
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
    public Complement setCharClass (org.meta_environment.rascal.ast.
				    CharClass x)
    {
      org.meta_environment.rascal.ast.Complement z = new Complement ();
      z.$setCharClass (x);
      return z;
    }
  }
  static public class Difference extends CharClass
  {
/* lhs:CharClass "/" rhs:CharClass -> CharClass {cons("Difference"), left, memo} */
    private Difference ()
    {
    }
    /*package */ Difference (ITree tree,
			     org.meta_environment.rascal.ast.CharClass lhs,
			     org.meta_environment.rascal.ast.CharClass rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassDifference (this);
    }
    private org.meta_environment.rascal.ast.CharClass lhs;
    public org.meta_environment.rascal.ast.CharClass getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.CharClass x)
    {
      this.lhs = x;
    }
    public Difference setLhs (org.meta_environment.rascal.ast.CharClass x)
    {
      org.meta_environment.rascal.ast.Difference z = new Difference ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.CharClass rhs;
    public org.meta_environment.rascal.ast.CharClass getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.CharClass x)
    {
      this.rhs = x;
    }
    public Difference setRhs (org.meta_environment.rascal.ast.CharClass x)
    {
      org.meta_environment.rascal.ast.Difference z = new Difference ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class Intersection extends CharClass
  {
/* lhs:CharClass "/\\" rhs:CharClass -> CharClass {cons("Intersection"), left, memo} */
    private Intersection ()
    {
    }
    /*package */ Intersection (ITree tree,
			       org.meta_environment.rascal.ast.CharClass lhs,
			       org.meta_environment.rascal.ast.CharClass rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassIntersection (this);
    }
    private org.meta_environment.rascal.ast.CharClass lhs;
    public org.meta_environment.rascal.ast.CharClass getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.CharClass x)
    {
      this.lhs = x;
    }
    public Intersection setLhs (org.meta_environment.rascal.ast.CharClass x)
    {
      org.meta_environment.rascal.ast.Intersection z = new Intersection ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.CharClass rhs;
    public org.meta_environment.rascal.ast.CharClass getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.CharClass x)
    {
      this.rhs = x;
    }
    public Intersection setRhs (org.meta_environment.rascal.ast.CharClass x)
    {
      org.meta_environment.rascal.ast.Intersection z = new Intersection ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class Union extends CharClass
  {
/* lhs:CharClass "\\/" rhs:CharClass -> CharClass {cons("Union"), left} */
    private Union ()
    {
    }
    /*package */ Union (ITree tree,
			org.meta_environment.rascal.ast.CharClass lhs,
			org.meta_environment.rascal.ast.CharClass rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharClassUnion (this);
    }
    private org.meta_environment.rascal.ast.CharClass lhs;
    public org.meta_environment.rascal.ast.CharClass getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.CharClass x)
    {
      this.lhs = x;
    }
    public Union setLhs (org.meta_environment.rascal.ast.CharClass x)
    {
      org.meta_environment.rascal.ast.Union z = new Union ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.CharClass rhs;
    public org.meta_environment.rascal.ast.CharClass getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.CharClass x)
    {
      this.rhs = x;
    }
    public Union setRhs (org.meta_environment.rascal.ast.CharClass x)
    {
      org.meta_environment.rascal.ast.Union z = new Union ();
      z.$setRhs (x);
      return z;
    }
  }
}
