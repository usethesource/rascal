package org.meta_environment.rascal.ast;
public abstract class CharClass extends AbstractAST
{
  public class SimpleCharclass extends CharClass
  {
    private OptCharRanges optionalCharRanges;

    private SimpleCharclass ()
    {
    }
    /*package */ SimpleCharclass (ITree tree,
				  OptCharRanges optionalCharRanges)
    {
      this.tree = tree;
      this.optionalCharRanges = optionalCharRanges;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSimpleCharclassCharClass (this);
    }
    private final OptCharRanges optionalCharRanges;
    public OptCharRanges getoptionalCharRanges ()
    {
      return optionalCharRanges;
    }
    private void privateSetoptionalCharRanges (OptCharRanges x)
    {
      this.optionalCharRanges = x;
    }
    public SimpleCharclass setoptionalCharRanges (OptCharRanges x)
    {
      z = new SimpleCharclass ();
      z.privateSetoptionalCharRanges (x);
      return z;
    }
  }
  public class Complement extends CharClass
  {
    private CharClass charClass;

    private Complement ()
    {
    }
    /*package */ Complement (ITree tree, CharClass charClass)
    {
      this.tree = tree;
      this.charClass = charClass;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitComplementCharClass (this);
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
    public Complement setcharClass (CharClass x)
    {
      z = new Complement ();
      z.privateSetcharClass (x);
      return z;
    }
  }
  public class Difference extends CharClass
  {
    private CharClass lhs;
    private CharClass rhs;

    private Difference ()
    {
    }
    /*package */ Difference (ITree tree, CharClass lhs, CharClass rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDifferenceCharClass (this);
    }
    private final CharClass lhs;
    public CharClass getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (CharClass x)
    {
      this.lhs = x;
    }
    public Difference setlhs (CharClass x)
    {
      z = new Difference ();
      z.privateSetlhs (x);
      return z;
    }
    private final CharClass rhs;
    public CharClass getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (CharClass x)
    {
      this.rhs = x;
    }
    public Difference setrhs (CharClass x)
    {
      z = new Difference ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class Intersection extends CharClass
  {
    private CharClass lhs;
    private CharClass rhs;

    private Intersection ()
    {
    }
    /*package */ Intersection (ITree tree, CharClass lhs, CharClass rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIntersectionCharClass (this);
    }
    private final CharClass lhs;
    public CharClass getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (CharClass x)
    {
      this.lhs = x;
    }
    public Intersection setlhs (CharClass x)
    {
      z = new Intersection ();
      z.privateSetlhs (x);
      return z;
    }
    private final CharClass rhs;
    public CharClass getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (CharClass x)
    {
      this.rhs = x;
    }
    public Intersection setrhs (CharClass x)
    {
      z = new Intersection ();
      z.privateSetrhs (x);
      return z;
    }
  }
  public class Union extends CharClass
  {
    private CharClass lhs;
    private CharClass rhs;

    private Union ()
    {
    }
    /*package */ Union (ITree tree, CharClass lhs, CharClass rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitUnionCharClass (this);
    }
    private final CharClass lhs;
    public CharClass getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (CharClass x)
    {
      this.lhs = x;
    }
    public Union setlhs (CharClass x)
    {
      z = new Union ();
      z.privateSetlhs (x);
      return z;
    }
    private final CharClass rhs;
    public CharClass getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (CharClass x)
    {
      this.rhs = x;
    }
    public Union setrhs (CharClass x)
    {
      z = new Union ();
      z.privateSetrhs (x);
      return z;
    }
  }
  prod2class ("(" CharClass ")"->CharClass
	      {
	      bracket, avoid}
)prod2class ("~" CharClass->CharClass)
    prod2class (CharClass "/" CharClass->CharClass)
    prod2class (CharClass "/\\" CharClass->CharClass)
    prod2class (CharClass "\\/" CharClass->CharClass)}
