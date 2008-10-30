package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class ModuleName extends AbstractAST
{
  public class Lexical extends ModuleName
  {
    /* ModuleWord -> ModuleName  */
  }
  public class Ambiguity extends ModuleName
  {
    private final List < ModuleName > alternatives;
    public Ambiguity (List < ModuleName > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < ModuleName > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Lexical extends ModuleName
  {
    /* sep:"/" basename:ModuleName -> ModuleName  */
  }
  public class Lexical extends ModuleName
  {
    /* dirname:ModuleWord sep:"/" basename:ModuleName -> ModuleName  */
  }
}
