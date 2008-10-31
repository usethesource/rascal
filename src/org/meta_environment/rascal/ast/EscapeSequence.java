package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class EscapeSequence extends AbstractAST
{
  public class Lexical extends EscapeSequence
  {
    /* "\\" [0-7] -> EscapeSequence  */
  }
  public class Ambiguity extends EscapeSequence
  {
    private final java.util.List < EscapeSequence > alternatives;
    public Ambiguity (java.util.List < EscapeSequence > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < EscapeSequence > getAlternatives ()
    {
      return alternatives;
    }
  }
}
