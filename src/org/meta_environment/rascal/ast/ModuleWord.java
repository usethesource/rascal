package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class ModuleWord extends AbstractAST
{
  public class Word extends ModuleWord
  {
/* letters:[A-Za-z0-9\_\-]+ -> ModuleWord {cons("Word")} */
    private Word ()
    {
    }
    /*package */ Word (ITree tree,
		       List <
		       get - sort - from -
		       symbol ([A - Za - z0 - 9 \ _ \ -] +) > letters)
    {
      this.tree = tree;
      this.letters = letters;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitModuleWordWord (this);
    }
    private List < get - sort - from - symbol ([A - Za - z0 - 9 \ _ \ -] +) >
      letters;
    public List < get - sort - from - symbol ([A - Za - z0 - 9 \ _ \ -] +) >
      getletters ()
    {
      return letters;
    }
    private void privateSetletters (List <
				    get - sort - from -
				    symbol ([A - Za - z0 - 9 \ _ \ -] +) > x)
    {
      this.letters = x;
    }
    public Word setletters (List <
			    get - sort - from -
			    symbol ([A - Za - z0 - 9 \ _ \ -] +) > x)
    {
      Word z = new Word ();
      z.privateSetletters (x);
      return z;
    }
  }
  public class Ambiguity extends ModuleWord
  {
    private final List < ModuleWord > alternatives;
    public Ambiguity (List < ModuleWord > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < ModuleWord > getAlternatives ()
    {
      return alternatives;
    }
  }
}
