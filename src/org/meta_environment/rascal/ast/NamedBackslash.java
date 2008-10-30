package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class NamedBackslash extends AbstractAST
{
  public class Lexical extends NamedBackslash
  {
    /* [\\] -> NamedBackslash  */
  }
  public class Ambiguity extends NamedBackslash
  {
    private final List < NamedBackslash > alternatives;
    public Ambiguity (List < NamedBackslash > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < NamedBackslash > getAlternatives ()
    {
      return alternatives;
    }
  }
}
