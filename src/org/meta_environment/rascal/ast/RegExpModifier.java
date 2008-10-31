package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class RegExpModifier extends AbstractAST
{
  public class Lexical extends RegExpModifier
  {
    /* [imsd] -> RegExpModifier  */
  }
  public class Ambiguity extends RegExpModifier
  {
    private final java.util.List < RegExpModifier > alternatives;
    public Ambiguity (java.util.List < RegExpModifier > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < RegExpModifier > getAlternatives ()
    {
      return alternatives;
    }
  }
}
