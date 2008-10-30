package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < RegExpModifier > getAlternatives ()
    {
      return alternatives;
    }
  }
}
