package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class DoubleLiteral extends AbstractAST
{
  static public class Lexical extends DoubleLiteral
  {
    /* [0-9]+ "." [0-9]* ( [eE] [\+\-]? [0-9]+ )? [dD]? -> DoubleLiteral  */
  } static public class Ambiguity extends DoubleLiteral
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.DoubleLiteral > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.DoubleLiteral >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.DoubleLiteral >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
