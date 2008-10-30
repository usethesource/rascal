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
    private final List < StringCharacter > alternatives;
    public Ambiguity (List < StringCharacter > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < StringCharacter > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends StringCharacter
  {
    /* ~[\"\\\<] -> StringCharacter  */
  }
  public class Lexical extends StringCharacter
  {
    /* EscapeSequence -> StringCharacter  */
  }
}
