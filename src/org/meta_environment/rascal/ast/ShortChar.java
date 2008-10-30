package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class ShortChar extends AbstractAST
{
  public class Lexical extends ShortChar
  {
    /* character:[a-zA-Z0-9] -> ShortChar  */
  }
  public class Ambiguity extends ShortChar
  {
    private final List < ShortChar > alternatives;
    public Ambiguity (List < ShortChar > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < ShortChar > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends ShortChar
  {
    /* [\\] escape:~[\0-\31A-Za-mo-qsu-z0-9] -> ShortChar  */
  }
}
