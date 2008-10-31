package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class SingleCharacter extends AbstractAST
{
  public class Lexical extends SingleCharacter
  {
    /* UnicodeEscape -> SingleCharacter  */
  }
  public class Ambiguity extends SingleCharacter
  {
    private final java.util.List < SingleCharacter > alternatives;
    public Ambiguity (java.util.List < SingleCharacter > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < SingleCharacter > getAlternatives ()
    {
      return alternatives;
    }
  }
}
