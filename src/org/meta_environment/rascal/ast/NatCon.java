package org.meta_environment.rascal.ast;
import java.util.Collections;
import java.util.List;

import org.eclipse.imp.pdb.facts.ITree;
public abstract class NatCon extends AbstractAST
{
  public class digits extends NatCon
  {
    /*package */ digits (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitNatCondigits (this);
    }
  }
  public class Ambiguity extends NatCon
  {
    private final List < NatCon > alternatives;
    public Ambiguity (List < NatCon > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < NatCon > getAlternatives ()
    {
      return alternatives;
    }
  }
}
