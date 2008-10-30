package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class SingleCharacter extends AbstractAST
{
  public class Lexical extends SingleCharacter
  {
    /* UnicodeEscape -> SingleCharacter  */
  }
  public class Ambiguity extends SingleCharacter
  {
    private final List < SingleCharacter > alternatives;
    public Ambiguity (List < SingleCharacter > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < SingleCharacter > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends SingleCharacter
  {
    /* ~[\'\\] -> SingleCharacter  */
  }
}
