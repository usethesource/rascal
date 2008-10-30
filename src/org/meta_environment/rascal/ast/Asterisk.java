package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Asterisk extends AbstractAST
{
  public class Lexical extends Asterisk
  {
    /* [\*] -> Asterisk  */
  }
  public class Ambiguity extends Asterisk
  {
    private final List < Asterisk > alternatives;
    public Ambiguity (List < Asterisk > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Asterisk > getAlternatives ()
    {
      return alternatives;
    }
  }
}
