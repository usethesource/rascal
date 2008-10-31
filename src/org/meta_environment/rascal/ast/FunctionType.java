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
    /*package */ TypeArguments (ITree tree,
				org.meta_environment.rascal.ast.Type type,
				java.util.List <
				org.meta_environment.rascal.ast.TypeArg >
				arguments)
    {
      this.tree = tree;
      this.type = type;
      this.arguments = arguments;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionTypeTypeArguments (this);
    }
    private org.meta_environment.rascal.ast.Type type;
    public org.meta_environment.rascal.ast.Type getType ()
    {
      return type;
    }
    private void $setType (org.meta_environment.rascal.ast.Type x)
    {
      this.type = x;
    }
    public TypeArguments setType (org.meta_environment.rascal.ast.Type x)
    {
      org.meta_environment.rascal.ast.TypeArguments z = new TypeArguments ();
      z.$setType (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.TypeArg >
      arguments;
    public java.util.List < org.meta_environment.rascal.ast.TypeArg >
      getArguments ()
    {
      return arguments;
    }
    private void $setArguments (java.util.List <
				org.meta_environment.rascal.ast.TypeArg > x)
    {
      this.arguments = x;
    }
    public TypeArguments setArguments (java.util.List <
				       org.meta_environment.rascal.ast.
				       TypeArg > x)
    {
      org.meta_environment.rascal.ast.TypeArguments z = new TypeArguments ();
      z.$setArguments (x);
      return z;
    }
  }
  static public class Ambiguity extends FunctionType
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.FunctionType > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.FunctionType >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.FunctionType >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
