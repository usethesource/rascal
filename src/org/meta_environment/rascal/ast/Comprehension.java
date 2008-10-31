package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Comprehension extends AbstractAST
{
  public class Set extends Comprehension
  {
/* "{" result:Expression "|" generators:{Generator ","}+ "}" -> Comprehension {cons("Set")} */
    private Set ()
    {
    }
    /*package */ Set (ITree tree, Expression result,
		      java.util.List < Generator > generators)
    {
      this.tree = tree;
      this.result = result;
      this.generators = generators;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitComprehensionSet (this);
    }
    private Expression result;
    public Expression getResult ()
    {
      return result;
    }
    private void $setResult (Expression x)
    {
      this.result = x;
    }
    public Set setResult (Expression x)
    {
      Set z = new Set ();
      z.$setResult (x);
      return z;
    }
    private java.util.List < Generator > generators;
    public java.util.List < Generator > getGenerators ()
    {
      return generators;
    }
    private void $setGenerators (java.util.List < Generator > x)
    {
      this.generators = x;
    }
    public Set setGenerators (java.util.List < Generator > x)
    {
      Set z = new Set ();
      z.$setGenerators (x);
      return z;
    }
  }
  public class Ambiguity extends Comprehension
  {
    private final java.util.List < Comprehension > alternatives;
    public Ambiguity (java.util.List < Comprehension > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Comprehension > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class List extends Comprehension
  {
/* "[" result:Expression "|" generators:{Generator ","}+ "]" -> Comprehension {cons("List")} */
    private List ()
    {
    }
    /*package */ List (ITree tree, Expression result,
		       java.util.List < Generator > generators)
    {
      this.tree = tree;
      this.result = result;
      this.generators = generators;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitComprehensionList (this);
    }
    private Expression result;
    public Expression getResult ()
    {
      return result;
    }
    private void $setResult (Expression x)
    {
      this.result = x;
    }
    public List setResult (Expression x)
    {
      List z = new List ();
      z.$setResult (x);
      return z;
    }
    private java.util.List < Generator > generators;
    public java.util.List < Generator > getGenerators ()
    {
      return generators;
    }
    private void $setGenerators (java.util.List < Generator > x)
    {
      this.generators = x;
    }
    public List setGenerators (java.util.List < Generator > x)
    {
      List z = new List ();
      z.$setGenerators (x);
      return z;
    }
  }
}
