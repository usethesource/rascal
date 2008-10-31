package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class OctalIntegerLiteral extends AbstractAST
{
  static public class Lexical extends OctalIntegerLiteral
  {
    /* [0] [0-7]+ -> OctalIntegerLiteral  */
  }
  static public class Ambiguity extends OctalIntegerLiteral
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.OctalIntegerLiteral > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.OctalIntegerLiteral >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List <
      org.meta_environment.rascal.ast.OctalIntegerLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
