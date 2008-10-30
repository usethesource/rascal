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
    private final List < RegExpModifier > alternatives;
    public Ambiguity (List < RegExpModifier > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < RegExpModifier > getAlternatives ()
    {
      return alternatives;
    }
  }
}
