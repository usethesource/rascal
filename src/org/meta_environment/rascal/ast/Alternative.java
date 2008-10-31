package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Alternative extends AbstractAST
{
  static public class NamedType extends Alternative
  {
/* name:Name type:Type -> Alternative {cons("NamedType")} */
    private NamedType ()
    {
    }
    /*package */ NamedType (ITree tree,
			    org.meta_environment.rascal.ast.Name name,
			    org.meta_environment.rascal.ast.Type type)
    {
      this.tree = tree;
      this.name = name;
      this.type = type;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAlternativeNamedType (this);
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
    public NamedType setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.NamedType z = new NamedType ();
      z.$setName (x);
      return z;
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
    public NamedType setType (org.meta_environment.rascal.ast.Type x)
    {
      org.meta_environment.rascal.ast.NamedType z = new NamedType ();
      z.$setType (x);
      return z;
    }
  }
  static public class Ambiguity extends Alternative
  {
    public Alternative.Ambiguity makeAlternativeAmbiguity (java.util.List <
							   Alternative >
							   alternatives)
    {
      Alternative.Ambiguity amb = new Alternative.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Alternative.Ambiguity) table.get (amb);
    }
    private final java.util.List < Alternative > alternatives;
    public Ambiguity (java.util.List < Alternative > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Alternative > getAlternatives ()
    {
      return alternatives;
    }
  }
}
