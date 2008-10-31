package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Variant extends AbstractAST
{
  static public class Type extends Variant
  {
/* type:Type name:Name -> Variant {cons("Type")} */
    private Type ()
    {
    }
    /*package */ Type (ITree tree, org.meta_environment.rascal.ast.Type type,
		       org.meta_environment.rascal.ast.Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitVariantType (this);
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
    public Type setType (org.meta_environment.rascal.ast.Type x)
    {
      org.meta_environment.rascal.ast.Type z = new Type ();
      z.$setType (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public Type setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Type z = new Type ();
      z.$setName (x);
      return z;
    }
  }
  static public class Ambiguity extends Variant
  {
    public Variant.Ambiguity makeVariantAmbiguity (java.util.List < Variant >
						   alternatives)
    {
      Variant.Ambiguity amb = new Variant.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Variant.Ambiguity) table.get (amb);
    }
    private final java.util.List < Variant > alternatives;
    public Ambiguity (java.util.List < Variant > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Variant > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class NAryConstructor extends Variant
  {
/* name:Name "(" arguments:{TypeArg ","}+ ")" -> Variant {cons("NAryConstructor")} */
    private NAryConstructor ()
    {
    }
    /*package */ NAryConstructor (ITree tree,
				  org.meta_environment.rascal.ast.Name name,
				  java.util.List < TypeArg > arguments)
    {
      this.tree = tree;
      this.name = name;
      this.arguments = arguments;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitVariantNAryConstructor (this);
    }
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public NAryConstructor setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.NAryConstructor z =
	new NAryConstructor ();
      z.$setName (x);
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
    public NAryConstructor setArguments (java.util.List <
					 org.meta_environment.rascal.ast.
					 TypeArg > x)
    {
      org.meta_environment.rascal.ast.NAryConstructor z =
	new NAryConstructor ();
      z.$setArguments (x);
      return z;
    }
  }
  static public class NillaryConstructor extends Variant
  {
/* name:Name -> Variant {cons("NillaryConstructor")} */
    private NillaryConstructor ()
    {
    }
    /*package */ NillaryConstructor (ITree tree,
				     org.meta_environment.rascal.ast.
				     Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitVariantNillaryConstructor (this);
    }
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public NillaryConstructor setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.NillaryConstructor z =
	new NillaryConstructor ();
      z.$setName (x);
      return z;
    }
  }
}
