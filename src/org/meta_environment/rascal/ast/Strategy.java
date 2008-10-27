package org.meta_environment.rascal.ast;
public abstract class Strategy extends AbstractAST
{
  public class TopDown extends Strategy
  {
    private TopDown ()
    {
    }
    /*package */ TopDown (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTopDownStrategy (this);
    }
  }
  public class TopDownBreak extends Strategy
  {
    private TopDownBreak ()
    {
    }
    /*package */ TopDownBreak (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitTopDownBreakStrategy (this);
    }
  }
  public class BottomUp extends Strategy
  {
    private BottomUp ()
    {
    }
    /*package */ BottomUp (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitBottomUpStrategy (this);
    }
  }
  public class BottomUpBreak extends Strategy
  {
    private BottomUpBreak ()
    {
    }
    /*package */ BottomUpBreak (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitBottomUpBreakStrategy (this);
    }
  }
  public class Outermost extends Strategy
  {
    private Outermost ()
    {
    }
    /*package */ Outermost (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitOutermostStrategy (this);
    }
  }
  public class Innermost extends Strategy
  {
    private Innermost ()
    {
    }
    /*package */ Innermost (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitInnermostStrategy (this);
    }
  }
}
