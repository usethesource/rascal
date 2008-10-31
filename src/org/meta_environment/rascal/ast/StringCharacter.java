package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StringCharacter extends AbstractAST
{
  public class Lexical extends StringCharacter
  {
    /* UnicodeEscape -> StringCharacter  */
  }
  public class Ambiguity extends StringCharacter
  {
    private final java.util.List < StringCharacter > alternatives;
    public Ambiguity (java.util.List < StringCharacter > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < StringCharacter > getAlternatives ()
    {
      return alternatives;
    }
  }
}
