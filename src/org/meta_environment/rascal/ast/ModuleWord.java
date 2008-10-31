package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class ModuleWord extends AbstractAST
{
  static public class Lexical extends ModuleWord
  {
    /* letters:[A-Za-z0-9\_\-]+ -> ModuleWord  */
  }
  static public class Ambiguity extends ModuleWord
  {
    public ModuleWord.Ambiguity makeModuleWordAmbiguity (java.util.List <
							 ModuleWord >
							 alternatives)
    {
      ModuleWord.Ambiguity amb = new ModuleWord.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (ModuleWord.Ambiguity) table.get (amb);
    }
    private final java.util.List < ModuleWord > alternatives;
    public Ambiguity (java.util.List < ModuleWord > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < ModuleWord > getAlternatives ()
    {
      return alternatives;
    }
  }
}
