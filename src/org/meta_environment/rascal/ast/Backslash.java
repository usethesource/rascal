package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Backslash extends AbstractAST
{
  static public class Lexical extends Backslash
  {
    /* [\\] -> Backslash  */
  }
  public class Ambiguity extends Backslash
  {
    private final java.util.List < Backslash > alternatives;
    public Ambiguity (java.util.List < Backslash > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Backslash > getAlternatives ()
    {
      return alternatives;
    }
  }
}
