package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class NumChar extends AbstractAST
{
  public class Digits extends NumChar
  {
/* [\\] number:[0-9]+ -> NumChar {cons("Digits")} */
    private Digits ()
    {
    }
    /*package */ Digits (ITree tree,
			 List < get - sort - from - symbol ([0 - 9] +) >
			 number)
    {
      this.tree = tree;
      this.number = number;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitNumCharDigits (this);
    }
    private List < get - sort - from - symbol ([0 - 9] +) > number;
    public List < get - sort - from - symbol ([0 - 9] +) > getnumber ()
    {
      return number;
    }
    private void privateSetnumber (List <
				   get - sort - from - symbol ([0 - 9] +) > x)
    {
      this.number = x;
    }
    public Digits setnumber (List < get - sort - from - symbol ([0 - 9] +) >
			     x)
    {
      Digits z = new Digits ();
      z.privateSetnumber (x);
      return z;
    }
  }
  public class Ambiguity extends NumChar
  {
    private final List < NumChar > alternatives;
    public Ambiguity (List < NumChar > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < NumChar > getAlternatives ()
    {
      return alternatives;
    }
  }
}
