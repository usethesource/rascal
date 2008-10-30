package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class QualifiedName extends AbstractAST
{
  public class Default extends QualifiedName
  {
/* names:{Name "::"}+ -> QualifiedName {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, java.util.List < Name > names)
    {
      this.tree = tree;
      this.names = names;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitQualifiedNameDefault (this);
    }
    private java.util.List < Name > names;
    public java.util.List < Name > getNames ()
    {
      return names;
    }
    private void $setNames (java.util.List < Name > x)
    {
      this.names = x;
    }
    public Default setNames (java.util.List < Name > x)
    {
      Default z = new Default ();
      z.$setNames (x);
      return z;
    }
  }
  public class Ambiguity extends QualifiedName
  {
    private final java.util.List < QualifiedName > alternatives;
    public Ambiguity (java.util.List < QualifiedName > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < QualifiedName > getAlternatives ()
    {
      return alternatives;
    }
  }
}
