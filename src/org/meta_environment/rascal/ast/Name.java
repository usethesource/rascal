package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Name extends AbstractAST
{
  static public class Lexical extends Name
  {
    /* [\\]? [A-Za-z\_] [A-Za-z0-9\_\-]* -> Name  */
  }
  public class Ambiguity extends Name
  {
    private final java.util.List < Name > alternatives;
    public Ambiguity (java.util.List < Name > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Name > getAlternatives ()
    {
      return alternatives;
    }
  }
}
