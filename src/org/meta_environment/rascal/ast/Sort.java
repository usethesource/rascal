package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Sort extends AbstractAST
{
  public class one - char extends Sort
  {
/* head:[A-Z] -> Sort {cons("one-char")} */
    private one - char ()
    {
    }
    /*package */ one - char (ITree tree,
			     List < get - sort - from - symbol ([A - Z]) >
			     head)
    {
      this.tree = tree;
      this.head = head;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSortone - char (this);
    }
    private List < get - sort - from - symbol ([A - Z]) > head;
    public List < get - sort - from - symbol ([A - Z]) > gethead ()
    {
      return head;
    }
    private void privateSethead (List < get - sort - from - symbol ([A - Z]) >
				 x)
    {
      this.head = x;
    }
    public one - char sethead (List < get - sort - from - symbol ([A - Z]) >
			       x)
    {
      one - char z = new one - char ();
      z.privateSethead (x);
      return z;
    }
  }
  public class Ambiguity extends Sort
  {
    private final List < Sort > alternatives;
    public Ambiguity (List < Sort > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < Sort > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class more - chars extends Sort
  {
/* head:[A-Z] middle:[A-Za-z0-9\-]* last:[A-Za-z0-9] -> Sort {cons("more-chars")} */
    private more - chars ()
    {
    }
    /*package */ more - chars (ITree tree,
			       List < get - sort - from - symbol ([A - Z]) >
			       head,
			       List <
			       get - sort - from -
			       symbol ([A - Za - z0 - 9 \ -] *) > middle,
			       List <
			       get - sort - from -
			       symbol ([A - Za - z0 - 9]) > last)
    {
      this.tree = tree;
      this.head = head;
      this.middle = middle;
      this.last = last;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSortmore - chars (this);
    }
    private List < get - sort - from - symbol ([A - Z]) > head;
    public List < get - sort - from - symbol ([A - Z]) > gethead ()
    {
      return head;
    }
    private void privateSethead (List < get - sort - from - symbol ([A - Z]) >
				 x)
    {
      this.head = x;
    }
    public more - chars sethead (List < get - sort - from - symbol ([A - Z]) >
				 x)
    {
      more - chars z = new more - chars ();
      z.privateSethead (x);
      return z;
    }
    private List < get - sort - from - symbol ([A - Za - z0 - 9 \ -] *) >
      middle;
    public List < get - sort - from - symbol ([A - Za - z0 - 9 \ -] *) >
      getmiddle ()
    {
      return middle;
    }
    private void privateSetmiddle (List <
				   get - sort - from -
				   symbol ([A - Za - z0 - 9 \ -] *) > x)
    {
      this.middle = x;
    }
    public more - chars setmiddle (List <
				   get - sort - from -
				   symbol ([A - Za - z0 - 9 \ -] *) > x)
    {
      more - chars z = new more - chars ();
      z.privateSetmiddle (x);
      return z;
    }
    private List < get - sort - from - symbol ([A - Za - z0 - 9]) > last;
    public List < get - sort - from - symbol ([A - Za - z0 - 9]) > getlast ()
    {
      return last;
    }
    private void privateSetlast (List <
				 get - sort - from -
				 symbol ([A - Za - z0 - 9]) > x)
    {
      this.last = x;
    }
    public more - chars setlast (List <
				 get - sort - from -
				 symbol ([A - Za - z0 - 9]) > x)
    {
      more - chars z = new more - chars ();
      z.privateSetlast (x);
      return z;
    }
  }
}
