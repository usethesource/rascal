package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StrChar extends AbstractAST
{
  public boolean isNewline ()
  {
    return false;
  }
  static public class newline extends StrChar
  {
/* "\\n" -> StrChar {cons("newline")} */
    private newline ()
    {
    }
    /*package */ newline (ITree tree)
    {
      this.tree = tree;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItStrCharnewline (this);
    }

    public boolean isNewline ()
    {
      return true;
    }
  }
  static public class Ambiguity extends StrChar
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.StrChar >
      alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.StrChar > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.StrChar >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Lexical extends StrChar
  {
    private String string;
    /*package */ Lexical (ITree tree, String string)
    {
      this.tree = tree;
      this.string = string;
    }
    public String getString ()
    {
      return string;
    }
  }
}
