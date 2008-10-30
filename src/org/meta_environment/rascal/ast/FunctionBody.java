package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class FunctionBody extends AbstractAST
{
  public class Default extends FunctionBody
  {
/* "{" statements:Statement* "}" -> FunctionBody {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, List < Statement > statements)
    {
      this.tree = tree;
      this.statements = statements;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionBodyDefault (this);
    }
    private List < Statement > statements;
    public List < Statement > getstatements ()
    {
      return statements;
    }
    private void $setstatements (List < Statement > x)
    {
      this.statements = x;
    }
    public Default setstatements (List < Statement > x)
    {
      Default z = new Default ();
      z.$setstatements (x);
      return z;
    }
  }
  public class Ambiguity extends FunctionBody
  {
    private final List < FunctionBody > alternatives;
    public Ambiguity (List < FunctionBody > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < FunctionBody > getAlternatives ()
    {
      return alternatives;
    }
  }
}
