package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Asterisk extends AbstractAST
{
  static public class Lexical extends Asterisk
  {
    /* [\*] -> Asterisk  */
  }
  static public class Ambiguity extends Asterisk
  {
    public Asterisk.Ambiguity makeAsteriskAmbiguity (java.util.List <
						     Asterisk > alternatives)
    {
      Asterisk.Ambiguity amb = new Asterisk.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Asterisk.Ambiguity) table.get (amb);
    }
    private final java.util.List < Asterisk > alternatives;
    public Ambiguity (java.util.List < Asterisk > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Asterisk > getAlternatives ()
    {
      return alternatives;
    }
  }
}
