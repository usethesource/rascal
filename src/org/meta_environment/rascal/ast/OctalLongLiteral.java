package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class OctalLongLiteral extends AbstractAST
{
  public class Lexical extends OctalLongLiteral
  {
    /* [0] [0-7]+ [lL] -> OctalLongLiteral  */
  }
  public class Ambiguity extends OctalLongLiteral
  {
    private final List < OctalLongLiteral > alternatives;
    public Ambiguity (List < OctalLongLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < OctalLongLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
