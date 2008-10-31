package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Comment extends AbstractAST
{
  public class Lexical extends Comment
  {
    /* "//" ~[\n]* [\n] -> Comment {category("Comment")} */
  }
  public class Ambiguity extends Comment
  {
    private final java.util.List < Comment > alternatives;
    public Ambiguity (java.util.List < Comment > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Comment > getAlternatives ()
    {
      return alternatives;
    }
  }
}
