package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
      return visitor.visitFunctionTypeTypeArguments (this);
    }
    private Type type;
    public Type gettype ()
    {
      return type;
    }
    private void $settype (Type x)
    {
      this.type = x;
    }
    public TypeArguments settype (Type x)
    {
      TypeArguments z = new TypeArguments ();
      z.$settype (x);
      return z;
    }
    private List < TypeArg > arguments;
    public List < TypeArg > getarguments ()
    {
      return arguments;
    }
    private void $setarguments (List < TypeArg > x)
    {
      this.arguments = x;
    }
    public TypeArguments setarguments (List < TypeArg > x)
    {
      TypeArguments z = new TypeArguments ();
      z.$setarguments (x);
      return z;
    }
  }
  public class Ambiguity extends FunctionType
  {
    private final List < FunctionType > alternatives;
    public Ambiguity (List < FunctionType > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < FunctionType > getAlternatives ()
    {
      return alternatives;
    }
  }
}
