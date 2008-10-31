package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class LocalVariableDeclaration extends AbstractAST
{
  static public class Default extends LocalVariableDeclaration
  {
/* declarator:Declarator -> LocalVariableDeclaration {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.
			  Declarator declarator)
    {
      this.tree = tree;
      this.declarator = declarator;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLocalVariableDeclarationDefault (this);
    }
    private org.meta_environment.rascal.ast.Declarator declarator;
    public org.meta_environment.rascal.ast.Declarator getDeclarator ()
    {
      return declarator;
    }
    private void $setDeclarator (org.meta_environment.rascal.ast.Declarator x)
    {
      this.declarator = x;
    }
    public org.meta_environment.rascal.ast.Default setDeclarator (org.
								  meta_environment.
								  rascal.ast.
								  Declarator
								  x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setDeclarator (x);
      return z;
    }
  }
  static public class Ambiguity extends LocalVariableDeclaration
  {
    private final java.util.List < LocalVariableDeclaration > alternatives;
    public Ambiguity (java.util.List < LocalVariableDeclaration >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < LocalVariableDeclaration > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Dynamic extends LocalVariableDeclaration
  {
/* "dynamic" declarator:Declarator -> LocalVariableDeclaration {cons("Dynamic")} */
    private Dynamic ()
    {
    }
    /*package */ Dynamic (ITree tree,
			  org.meta_environment.rascal.ast.
			  Declarator declarator)
    {
      this.tree = tree;
      this.declarator = declarator;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLocalVariableDeclarationDynamic (this);
    }
    private org.meta_environment.rascal.ast.Declarator declarator;
    public org.meta_environment.rascal.ast.Declarator getDeclarator ()
    {
      return declarator;
    }
    private void $setDeclarator (org.meta_environment.rascal.ast.Declarator x)
    {
      this.declarator = x;
    }
    public org.meta_environment.rascal.ast.Dynamic setDeclarator (org.
								  meta_environment.
								  rascal.ast.
								  Declarator
								  x)
    {
      org.meta_environment.rascal.ast.Dynamic z = new Dynamic ();
      z.$setDeclarator (x);
      return z;
    }
  }
}
