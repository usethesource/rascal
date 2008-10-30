package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Visibility extends AbstractAST
{
  public class Public extends Visibility
  {
/* "public" -> Visibility {cons("Public")} */
    private Public ()
    {
    }
    /*package */ Public (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitVisibilityPublic (this);
    }
  }
  public class Ambiguity extends Visibility
  {
    private final List < Visibility > alternatives;
    public Ambiguity (List < Visibility > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Visibility > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Private extends Visibility
  {
/* "private" -> Visibility {cons("Private")} */
    private Private ()
    {
    }
    /*package */ Private (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitVisibilityPrivate (this);
    }
  }
}
