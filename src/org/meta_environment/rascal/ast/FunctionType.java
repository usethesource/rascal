package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FunctionType extends AbstractAST
{
  public class TypeArguments extends FunctionType
  {
/* type:Type "(" arguments:{TypeArg ","}* ")" -> FunctionType {cons("TypeArguments")} */
    private TypeArguments ()
    {
    }
    /*package */ TypeArguments (ITree tree, Type type,
				List < TypeArg > arguments)
    {
      this.tree = tree;
      this.type = type;
      this.arguments = arguments;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeArgumentsFunctionType (this);
    }
    private Type type;
    public Type gettype ()
    {
      return type;
    }
    private void privateSettype (Type x)
    {
      this.type = x;
    }
    public TypeArguments settype (Type x)
    {
      z = new TypeArguments ();
      z.privateSettype (x);
      return z;
    }
    private List < TypeArg > arguments;
    public List < TypeArg > getarguments ()
    {
      return arguments;
    }
    private void privateSetarguments (List < TypeArg > x)
    {
      this.arguments = x;
    }
    public TypeArguments setarguments (List < TypeArg > x)
    {
      TypeArguments z = new TypeArguments ();
      z.privateSetarguments (x);
      return z;
    }
  }
}
