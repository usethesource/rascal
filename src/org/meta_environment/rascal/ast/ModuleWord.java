package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class ModuleWord extends AbstractAST
{
  public class Lexical extends ModuleWord
  {
    /* letters:[A-Za-z0-9\_\-]+ -> ModuleWord  */
  }
  public class Ambiguity extends ModuleWord
  {
    private final List < ModuleWord > alternatives;
    public Ambiguity (List < ModuleWord > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < ModuleWord > getAlternatives ()
    {
      return alternatives;
    }
  }
}
