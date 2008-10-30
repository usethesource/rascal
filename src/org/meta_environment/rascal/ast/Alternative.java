package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Alternative extends AbstractAST
{
  public class NamedType extends Alternative
  {
/* name:Name type:Type -> Alternative {cons("NamedType")} */
    private NamedType ()
    {
    }
    /*package */ NamedType (ITree tree, Name name, Type type)
    {
      this.tree = tree;
      this.name = name;
      this.type = type;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAlternativeNamedType (this);
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
    public NamedType setName (Name x)
    {
      NamedType z = new NamedType ();
      z.$setName (x);
      return z;
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
    public NamedType setType (Type x)
    {
      NamedType z = new NamedType ();
      z.$setType (x);
      return z;
    }
  }
  public class Ambiguity extends Alternative
  {
    private final java.util.List < Alternative > alternatives;
    public Ambiguity (java.util.List < Alternative > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Alternative > getAlternatives ()
    {
      return alternatives;
    }
  }
}
