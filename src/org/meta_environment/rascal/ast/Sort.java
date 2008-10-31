package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Sort extends AbstractAST
{
  static public class Lexical extends Sort
  {
    /* head:[A-Z] -> Sort  */
  } static public class Ambiguity extends Sort
  {
    public Sort.Ambiguity makeSortAmbiguity (java.util.List < Sort >
					     alternatives)
    {
      Sort.Ambiguity amb = new Sort.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Sort.Ambiguity) table.get (amb);
    }
    private final java.util.List < Sort > alternatives;
    public Ambiguity (java.util.List < Sort > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Sort > getAlternatives ()
    {
      return alternatives;
    }
  }
}
