package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FunctionType extends AbstractAST
{
  static public class TypeArguments extends FunctionType
  {
/* type:Type "(" arguments:{TypeArg ","}* ")" -> FunctionType {cons("TypeArguments")} */
    private TypeArguments ()
    {
    }
    /*package */ TypeArguments (ITree tree, Type type,
				java.util.List < TypeArg > arguments)
    {
      this.tree = tree;
      this.type = type;
      this.arguments = arguments;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionTypeTypeArguments (this);
    }
    private Type type;
    public Type getType ()
    {
      return type;
    }
    private void $setType (Type x)
    {
      this.type = x;
    }
    public TypeArguments setType (Type x)
    {
      TypeArguments z = new TypeArguments ();
      z.$setType (x);
      return z;
    }
    private java.util.List < TypeArg > arguments;
    public java.util.List < TypeArg > getArguments ()
    {
      return arguments;
    }
    private void $setArguments (java.util.List < TypeArg > x)
    {
      this.arguments = x;
    }
    public TypeArguments setArguments (java.util.List < TypeArg > x)
    {
      TypeArguments z = new TypeArguments ();
      z.$setArguments (x);
      return z;
    }
  }
  public class Ambiguity extends FunctionType
  {
    private final java.util.List < FunctionType > alternatives;
    public Ambiguity (java.util.List < FunctionType > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < FunctionType > getAlternatives ()
    {
      return alternatives;
    }
  }
}
