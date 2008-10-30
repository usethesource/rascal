package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class NumChar extends AbstractAST
{
  public class Lexical extends NumChar
  {
    /* [\\] number:[0-9]+ -> NumChar  */
  }
  public class Ambiguity extends NumChar
  {
    private final List < NumChar > alternatives;
    public Ambiguity (List < NumChar > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < NumChar > getAlternatives ()
    {
      return alternatives;
    }
  }
}
