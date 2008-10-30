package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class OctalIntegerLiteral extends AbstractAST
{
  public class Lexical extends OctalIntegerLiteral
  {
    /* [0] [0-7]+ -> OctalIntegerLiteral  */
  }
  public class Ambiguity extends OctalIntegerLiteral
  {
    private final List < OctalIntegerLiteral > alternatives;
    public Ambiguity (List < OctalIntegerLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < OctalIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
