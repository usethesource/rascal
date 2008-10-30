package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Sort extends AbstractAST
{
  public class Lexical extends Sort
  {
    /* head:[A-Z] -> Sort  */
  }
  public class Ambiguity extends Sort
  {
    private final List < Sort > alternatives;
    public Ambiguity (List < Sort > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Sort > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends Sort
  {
    /* head:[A-Z] middle:[A-Za-z0-9\-]* last:[A-Za-z0-9] -> Sort  */
  }
}
