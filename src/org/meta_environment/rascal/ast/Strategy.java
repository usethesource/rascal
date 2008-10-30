package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStrategyTopDown (this);
    }
  }
  public class Ambiguity extends Strategy
  {
    private final List < Strategy > alternatives;
    public Ambiguity (List < Strategy > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Strategy > getAlternatives ()
    {
      return alternatives;
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStrategyTopDownBreak (this);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStrategyBottomUp (this);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStrategyBottomUpBreak (this);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStrategyOutermost (this);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStrategyInnermost (this);
    }
  }
}
