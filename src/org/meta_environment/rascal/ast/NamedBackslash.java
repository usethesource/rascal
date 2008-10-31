package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class NamedBackslash extends AbstractAST
{
  public class Lexical extends NamedBackslash
  {
    /* [\\] -> NamedBackslash  */
  }
  public class Ambiguity extends NamedBackslash
  {
    private final java.util.List < NamedBackslash > alternatives;
    public Ambiguity (java.util.List < NamedBackslash > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < NamedBackslash > getAlternatives ()
    {
      return alternatives;
    }
  }
}
