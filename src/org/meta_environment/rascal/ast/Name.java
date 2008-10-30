package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Name extends AbstractAST
{
  public class Lexical extends Name
  {
    /* [\\]? [A-Za-z\_] [A-Za-z0-9\_\-]* -> Name  */
  }
  public class Ambiguity extends Name
  {
    private final List < Name > alternatives;
    public Ambiguity (List < Name > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Name > getAlternatives ()
    {
      return alternatives;
    }
  }
}
