package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class FunctionName extends AbstractAST
{
  public class Name extends FunctionName
  {
/* name:Name -> FunctionName {cons("Name")} */
    private Name ()
    {
    }
    /*package */ Name (ITree tree, Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionNameName (this);
    }
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public Name setName (Name x)
    {
      Name z = new Name ();
      z.$setName (x);
      return z;
    }
  }
  public class Ambiguity extends FunctionName
  {
    private final java.util.List < FunctionName > alternatives;
    public Ambiguity (java.util.List < FunctionName > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < FunctionName > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Operator extends FunctionName
  {
/* operator:StandardOperator -> FunctionName {cons("Operator")} */
    private Operator ()
    {
    }
    /*package */ Operator (ITree tree, StandardOperator operator)
    {
      this.tree = tree;
      this.operator = operator;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionNameOperator (this);
    }
    private StandardOperator operator;
    public StandardOperator getOperator ()
    {
      return operator;
    }
    private void $setOperator (StandardOperator x)
    {
      this.operator = x;
    }
    public Operator setOperator (StandardOperator x)
    {
      Operator z = new Operator ();
      z.$setOperator (x);
      return z;
    }
  }
}
