package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Scope extends AbstractAST
{
  public class Global extends Scope
  {
/* "global" -> Scope {cons("Global")} */
    private Global ()
    {
    }
    /*package */ Global (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitGlobalScope (this);
    }
  }
  public class Dynamic extends Scope
  {
/* "dynamic" -> Scope {cons("Dynamic")} */
    private Dynamic ()
    {
    }
    /*package */ Dynamic (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDynamicScope (this);
    }
  }
}
