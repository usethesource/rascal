package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class SingleQuotedStrChar extends AbstractAST
{
  public class Lexical extends SingleQuotedStrChar
  {
    /* "\\n" -> SingleQuotedStrChar  */
  }
  public class Ambiguity extends SingleQuotedStrChar
  {
    private final java.util.List < SingleQuotedStrChar > alternatives;
    public Ambiguity (java.util.List < SingleQuotedStrChar > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < SingleQuotedStrChar > getAlternatives ()
    {
      return alternatives;
    }
  }
}
