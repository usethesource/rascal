package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class TypeArg extends AbstractAST
{
  public class Default extends TypeArg
  {
/* type:Type -> TypeArg {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Type type)
    {
      this.tree = tree;
      this.type = type;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeArgDefault (this);
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
    public Default settype (Type x)
    {
      Default z = new Default ();
      z.$settype (x);
      return z;
    }
  }
  public class Ambiguity extends TypeArg
  {
    private final List < TypeArg > alternatives;
    public Ambiguity (List < TypeArg > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < TypeArg > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Named extends TypeArg
  {
/* type:Type name:Name -> TypeArg {cons("Named")} */
    private Named ()
    {
    }
    /*package */ Named (ITree tree, Type type, Name name)
    {
      this.tree = tree;
      this.type = type;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeArgNamed (this);
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
    public Named settype (Type x)
    {
      Named z = new Named ();
      z.$settype (x);
      return z;
    }
    private Name name;
    public Name getname ()
    {
      return name;
    }
    private void $setname (Name x)
    {
      this.name = x;
    }
    public Named setname (Name x)
    {
      Named z = new Named ();
      z.$setname (x);
      return z;
    }
  }
}
