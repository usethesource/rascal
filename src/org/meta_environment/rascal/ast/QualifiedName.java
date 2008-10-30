package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class QualifiedName extends AbstractAST
{
  public class Default extends QualifiedName
  {
/* names:{Name "::"}+ -> QualifiedName {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, List < Name > names)
    {
      this.tree = tree;
      this.names = names;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitQualifiedNameDefault (this);
    }
    private List < Name > names;
    public List < Name > getnames ()
    {
      return names;
    }
    private void privateSetnames (List < Name > x)
    {
      this.names = x;
    }
    public Default setnames (List < Name > x)
    {
      Default z = new Default ();
      z.privateSetnames (x);
      return z;
    }
  }
  public class Ambiguity extends QualifiedName
  {
    private final List < QualifiedName > alternatives;
    public Ambiguity (List < QualifiedName > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < QualifiedName > getAlternatives ()
    {
      return alternatives;
    }
  }
}
