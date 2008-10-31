package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FloatingPointLiteral extends AbstractAST
{
  static public class Lexical extends FloatingPointLiteral
  {
    /* [0-9]+ "." [0-9]* ( [eE] [\+\-]? [0-9]+ )? [fF] -> FloatingPointLiteral  */
  } static public class Ambiguity extends FloatingPointLiteral
  {
    public FloatingPointLiteral.
      Ambiguity makeFloatingPointLiteralAmbiguity (java.util.List <
						   FloatingPointLiteral >
						   alternatives)
    {
      FloatingPointLiteral.Ambiguity amb =
	new FloatingPointLiteral.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (FloatingPointLiteral.Ambiguity) table.get (amb);
    }
    private final java.util.List < FloatingPointLiteral > alternatives;
    public Ambiguity (java.util.List < FloatingPointLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < FloatingPointLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
}
