package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class NumChar extends AbstractAST
{
  static public class Lexical extends NumChar
  {
    /* [\\] number:[0-9]+ -> NumChar  */
  }
  static public class Ambiguity extends NumChar
  {
    public NumChar.Ambiguity makeNumCharAmbiguity (java.util.List < NumChar >
						   alternatives)
    {
      NumChar.Ambiguity amb = new NumChar.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (NumChar.Ambiguity) table.get (amb);
    }
    private final java.util.List < NumChar > alternatives;
    public Ambiguity (java.util.List < NumChar > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < NumChar > getAlternatives ()
    {
      return alternatives;
    }
  }
}
