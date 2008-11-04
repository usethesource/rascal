package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class TypeArg extends AbstractAST
{
  public org.meta_environment.rascal.ast.Type getType ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasType ()
  {
    return false;
  }
  public boolean isDefault ()
  {
    return false;
  }
  static public class Default extends TypeArg
  {
/* type:Type -> TypeArg {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.Type type)
    {
      this.tree = tree;
      this.type = type;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItTypeArgDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }

    public boolean hasType ()
    {
      return true;
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
    public Default setType (org.meta_environment.rascal.ast.Type x)
    {
      Default z = new Default ();
      z.$setType (x);
      return z;
    }
  }
  static public class Ambiguity extends TypeArg
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.TypeArg >
      alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.TypeArg > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.TypeArg >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public org.meta_environment.rascal.ast.Name getName ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasName ()
  {
    return false;
  }
  public boolean isNamed ()
  {
    return false;
  }
  static public class Named extends TypeArg
  {
/* type:Type name:Name -> TypeArg {cons("Named")} */
    private Named ()
    {
    }
    /*package */ Named (ITree tree, org.meta_environment.rascal.ast.Type type,
			org.meta_environment.rascal.ast.Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItTypeArgNamed (this);
    }

    public boolean isNamed ()
    {
      return true;
    }

    public boolean hasType ()
    {
      return true;
    }
    public boolean hasName ()
    {
      return true;
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
    public Named setType (org.meta_environment.rascal.ast.Type x)
    {
      Named z = new Named ();
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
    public Named setName (org.meta_environment.rascal.ast.Name x)
    {
      Named z = new Named ();
      z.$setName (x);
      return z;
    }
  }
}
