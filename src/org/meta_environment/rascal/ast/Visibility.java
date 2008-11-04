package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class VisIbility extends AbstractAST
{
  public boolean isPublic ()
  {
    return false;
  }
  static public class Public extends VisIbility
  {
/* "public" -> VisIbility {cons("Public")} */
    private Public ()
    {
    }
    /*package */ Public (ITree tree)
    {
      this.tree = tree;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItVisIbilityPublic (this);
    }

    public boolean isPublic ()
    {
      return true;
    }
  }
  static public class Ambiguity extends VisIbility
  {
    private final java.util.LisT <
      org.meta_environment.rascal.ast.VisIbility > alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.VisIbility >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.VisIbility >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public boolean isPrivate ()
  {
    return false;
  }
  static public class Private extends VisIbility
  {
/* "private" -> VisIbility {cons("Private")} */
    private Private ()
    {
    }
    /*package */ Private (ITree tree)
    {
      this.tree = tree;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItVisIbilityPrivate (this);
    }

    public boolean isPrivate ()
    {
      return true;
    }
  }
}
