package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class ShortChar extends AbstractAST
{
  public class Lexical extends ShortChar
  {
    /* character:[a-zA-Z0-9] -> ShortChar  */
  }
  public class Ambiguity extends ShortChar
  {
    private final java.util.List < ShortChar > alternatives;
    public Ambiguity (java.util.List < ShortChar > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < ShortChar > getAlternatives ()
    {
      return alternatives;
    }
  }
}
