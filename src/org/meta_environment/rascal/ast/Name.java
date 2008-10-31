package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Name extends AbstractAST
{
  static public class Lexical extends Name
  {
    /* [\\]? [A-Za-z\_] [A-Za-z0-9\_\-]* -> Name  */
  }
  static public class Ambiguity extends Name
  {
    public Name.Ambiguity makeNameAmbiguity (java.util.List < Name >
					     alternatives)
    {
      Name.Ambiguity amb = new Name.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Name.Ambiguity) table.get (amb);
    }
    private final java.util.List < Name > alternatives;
    public Ambiguity (java.util.List < Name > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Name > getAlternatives ()
    {
      return alternatives;
    }
  }
}
