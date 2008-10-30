package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Comprehension extends AbstractAST
{
  public class Set extends Comprehension
  {
/* "{" result:Expression "|" generators:{Generator ","}+ "}" -> Comprehension {cons("Set")} */
    private Set ()
    {
    }
    /*package */ Set (ITree tree, Expression result,
		      List < Generator > generators)
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
    public Expression getresult ()
    {
      return result;
    }
    private void $setresult (Expression x)
    {
      this.result = x;
    }
    public Set setresult (Expression x)
    {
      Set z = new Set ();
      z.$setresult (x);
      return z;
    }
    private List < Generator > generators;
    public List < Generator > getgenerators ()
    {
      return generators;
    }
    private void $setgenerators (List < Generator > x)
    {
      this.generators = x;
    }
    public Set setgenerators (List < Generator > x)
    {
      Set z = new Set ();
      z.$setgenerators (x);
      return z;
    }
  }
  public class Ambiguity extends Comprehension
  {
    private final List < Comprehension > alternatives;
    public Ambiguity (List < Comprehension > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Comprehension > getAlternatives ()
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
		       List < Generator > generators)
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
    public Expression getresult ()
    {
      return result;
    }
    private void $setresult (Expression x)
    {
      this.result = x;
    }
    public List setresult (Expression x)
    {
      List z = new List ();
      z.$setresult (x);
      return z;
    }
    private List < Generator > generators;
    public List < Generator > getgenerators ()
    {
      return generators;
    }
    private void $setgenerators (List < Generator > x)
    {
      this.generators = x;
    }
    public List setgenerators (List < Generator > x)
    {
      List z = new List ();
      z.$setgenerators (x);
      return z;
    }
  }
}
