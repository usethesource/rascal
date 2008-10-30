package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class FloatingPointLiteral extends AbstractAST
{
  public class Lexical extends FloatingPointLiteral
  {
    /* [0-9]+ "." [0-9]* ( [eE] [\+\-]? [0-9]+ )? [fF] -> FloatingPointLiteral  */
  }
  public class Ambiguity extends FloatingPointLiteral
  {
    private final List < FloatingPointLiteral > alternatives;
    public Ambiguity (List < FloatingPointLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < FloatingPointLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends FloatingPointLiteral
  {
    /* "." [0-9]+ ( [eE] [\+\-]? [0-9]+ )? [fF] -> FloatingPointLiteral  */
  }
  public class Lexical extends FloatingPointLiteral
  {
    /* [0-9]+ [eE] [\+\-]? [0-9]+ [fF] -> FloatingPointLiteral  */
  }
  public class Lexical extends FloatingPointLiteral
  {
    /* [0-9]+ ( [eE] [\+\-]? [0-9]+ )? [fF] -> FloatingPointLiteral  */
  }
}
