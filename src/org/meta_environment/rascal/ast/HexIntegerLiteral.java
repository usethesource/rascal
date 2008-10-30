package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class HexIntegerLiteral extends AbstractAST
{
  public class Lexical extends HexIntegerLiteral
  {
    /* [0] [xX] [0-9a-fA-F]+ -> HexIntegerLiteral  */
  }
  public class Ambiguity extends HexIntegerLiteral
  {
    private final List < HexIntegerLiteral > alternatives;
    public Ambiguity (List < HexIntegerLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < HexIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
