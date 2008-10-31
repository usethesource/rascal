package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Asterisk extends AbstractAST
{
  public class Lexical extends Asterisk
  {
    /* [\*] -> Asterisk  */
  }
  public class Ambiguity extends Asterisk
  {
    private final java.util.List < Asterisk > alternatives;
    public Ambiguity (java.util.List < Asterisk > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Asterisk > getAlternatives ()
    {
      return alternatives;
    }
  }
}
