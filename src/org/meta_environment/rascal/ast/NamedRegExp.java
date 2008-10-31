package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class NamedRegExp extends AbstractAST
{
  static public class Lexical extends NamedRegExp
  {
    /* ~[\>\\] -> NamedRegExp  */
  } static public class Ambiguity extends NamedRegExp
  {
    public NamedRegExp.Ambiguity makeNamedRegExpAmbiguity (java.util.List <
							   NamedRegExp >
							   alternatives)
    {
      NamedRegExp.Ambiguity amb = new NamedRegExp.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (NamedRegExp.Ambiguity) table.get (amb);
    }
    private final java.util.List < NamedRegExp > alternatives;
    public Ambiguity (java.util.List < NamedRegExp > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < NamedRegExp > getAlternatives ()
    {
      return alternatives;
    }
  }
}
