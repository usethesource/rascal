package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Formal extends AbstractAST
{
  static public class TypeName extends Formal
  {
/* type:Type name:Name -> Formal {cons("TypeName")} */
    private TypeName ()
    {
    }
    /*package */ TypeName (ITree tree,
			   org.meta_environment.rascal.ast.Type type,
			   org.meta_environment.rascal.ast.Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFormalTypeName (this);
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
    public TypeName setType (org.meta_environment.rascal.ast.Type x)
    {
      org.meta_environment.rascal.ast.TypeName z = new TypeName ();
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
    public TypeName setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.TypeName z = new TypeName ();
      z.$setName (x);
      return z;
    }
  }
  static public class Ambiguity extends Formal
  {
    private final java.util.List < org.meta_environment.rascal.ast.Formal >
      alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.Formal > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Formal >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
