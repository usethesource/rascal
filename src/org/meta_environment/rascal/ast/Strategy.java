package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Strategy extends AbstractAST
{
  static public class TopDown extends Strategy
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
  static public class Ambiguity extends Strategy
  {
    public Strategy.Ambiguity makeStrategyAmbiguity (java.util.List <
						     Strategy > alternatives)
    {
      Strategy.Ambiguity amb = new Strategy.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Strategy.Ambiguity) table.get (amb);
    }
    private final java.util.List < Strategy > alternatives;
    public Ambiguity (java.util.List < Strategy > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Strategy > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class TopDownBreak extends Strategy
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
  static public class BottomUp extends Strategy
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
  static public class BottomUpBreak extends Strategy
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
  static public class Outermost extends Strategy
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
  static public class Innermost extends Strategy
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
