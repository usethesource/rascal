package org.meta_environment.rascal.ast;
public abstract class Strategy extends AbstractAST
{
  public class TopDown extends Strategy
  {
/* "top-down" -> Strategy {cons("TopDown")} */
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
/* "top-down-break" -> Strategy {cons("TopDownBreak")} */
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
/* "bottom-up" -> Strategy {cons("BottomUp")} */
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
/* "bottom-up-break" -> Strategy {cons("BottomUpBreak")} */
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
/* "outermost" -> Strategy {cons("Outermost")} */
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
/* "innermost" -> Strategy {cons("Innermost")} */
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
