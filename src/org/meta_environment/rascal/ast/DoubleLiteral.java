package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class DoubleLiteral extends AbstractAST
{
  public class Lexical extends DoubleLiteral
  {
    /* [0-9]+ "." [0-9]* ( [eE] [\+\-]? [0-9]+ )? [dD]? -> DoubleLiteral  */
  }
  public class Ambiguity extends DoubleLiteral
  {
    private final List < DoubleLiteral > alternatives;
    public Ambiguity (List < DoubleLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < DoubleLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends DoubleLiteral
  {
    /* "." [0-9]+ ( [eE] [\+\-]? [0-9]+ )? [dD]? -> DoubleLiteral  */
  }
  public class Lexical extends DoubleLiteral
  {
    /* [0-9]+ [eE] [\+\-]? [0-9]+ [dD]? -> DoubleLiteral  */
  }
  public class Lexical extends DoubleLiteral
  {
    /* [0-9]+ ( [eE] [\+\-]? [0-9]+ )? [dD] -> DoubleLiteral  */
  }
}
