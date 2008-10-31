package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StrChar extends AbstractAST
{
  public class newline extends StrChar
  {
/* "\\n" -> StrChar {cons("newline")} */
    private newline ()
    {
    }
    /*package */ newline (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitStrCharnewline (this);
    }
  }
  public class Ambiguity extends StrChar
  {
    private final java.util.List < StrChar > alternatives;
    public Ambiguity (java.util.List < StrChar > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < StrChar > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends StrChar
  {
    /* "\\t" -> StrChar  */
  }
}
