package org.meta_environment.rascal.ast;
public abstract class Character extends AbstractAST
{
  public class Numeric extends Character
  {
    private NumChar numeric;

    private Numeric ()
    {
    }
    /*package */ Numeric (ITree tree, NumChar numeric)
    {
      this.tree = tree;
      this.numeric = numeric;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNumericCharacter (this);
    }
    private final NumChar numeric;
    public NumChar getnumeric ()
    {
      return numeric;
    }
    private void privateSetnumeric (NumChar x)
    {
      this.numeric = x;
    }
    public Numeric setnumeric (NumChar x)
    {
      z = new Numeric ();
      z.privateSetnumeric (x);
      return z;
    }
  }
  public class Short extends Character
  {
    private ShortChar short;

    private Short ()
    {
    }
    /*package */ Short (ITree tree, ShortChar short)
    {
      this.tree = tree;
      this.short = short;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitShortCharacter (this);
    }
    private final ShortChar short;
    public ShortChar getshort ()
    {
      return short;
    }
    private void privateSetshort (ShortChar x)
    {
      this.short = x;
    }
    public Short setshort (ShortChar x)
    {
      z = new Short ();
      z.privateSetshort (x);
      return z;
    }
  }
  public class Top extends Character
  {
    private Top ()
    {
    }
    /*package */ Top (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTopCharacter (this);
    }
  }
  public class EOF extends Character
  {
    private EOF ()
    {
    }
    /*package */ EOF (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitEOFCharacter (this);
    }
  }
  public class Bottom extends Character
  {
    private Bottom ()
    {
    }
    /*package */ Bottom (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitBottomCharacter (this);
    }
  }
  public class LabelStart extends Character
  {
    private LabelStart ()
    {
    }
    /*package */ LabelStart (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitLabelStartCharacter (this);
    }
  }
}
