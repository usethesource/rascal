package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class FunctionDeclaration extends AbstractAST
{
  static public class Default extends FunctionDeclaration
  {
/* signature:Signature tags:Tags body:FunctionBody -> FunctionDeclaration {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.Signature signature,
			  org.meta_environment.rascal.ast.Tags tags,
			  org.meta_environment.rascal.ast.FunctionBody body)
    {
      this.tree = tree;
      this.signature = signature;
      this.tags = tags;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionDeclarationDefault (this);
    }
    private org.meta_environment.rascal.ast.Signature signature;
    public org.meta_environment.rascal.ast.Signature getSignature ()
    {
      return signature;
    }
    private void $setSignature (org.meta_environment.rascal.ast.Signature x)
    {
      this.signature = x;
    }
    public Default setSignature (org.meta_environment.rascal.ast.Signature x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setSignature (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Tags tags;
    public org.meta_environment.rascal.ast.Tags getTags ()
    {
      return tags;
    }
    private void $setTags (org.meta_environment.rascal.ast.Tags x)
    {
      this.tags = x;
    }
    public Default setTags (org.meta_environment.rascal.ast.Tags x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setTags (x);
      return z;
    }
    private org.meta_environment.rascal.ast.FunctionBody body;
    public org.meta_environment.rascal.ast.FunctionBody getBody ()
    {
      return body;
    }
    private void $setBody (org.meta_environment.rascal.ast.FunctionBody x)
    {
      this.body = x;
    }
    public Default setBody (org.meta_environment.rascal.ast.FunctionBody x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setBody (x);
      return z;
    }
  }
  static public class Ambiguity extends FunctionDeclaration
  {
    public FunctionDeclaration.
      Ambiguity makeFunctionDeclarationAmbiguity (java.util.List <
						  FunctionDeclaration >
						  alternatives)
    {
      FunctionDeclaration.Ambiguity amb =
	new FunctionDeclaration.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (FunctionDeclaration.Ambiguity) table.get (amb);
    }
    private final java.util.List < FunctionDeclaration > alternatives;
    public Ambiguity (java.util.List < FunctionDeclaration > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < FunctionDeclaration > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Abstract extends FunctionDeclaration
  {
/* signature:Signature tags:Tags -> FunctionDeclaration {cons("Abstract")} */
    private Abstract ()
    {
    }
    /*package */ Abstract (ITree tree,
			   org.meta_environment.rascal.ast.
			   Signature signature,
			   org.meta_environment.rascal.ast.Tags tags)
    {
      this.tree = tree;
      this.signature = signature;
      this.tags = tags;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionDeclarationAbstract (this);
    }
    private org.meta_environment.rascal.ast.Signature signature;
    public org.meta_environment.rascal.ast.Signature getSignature ()
    {
      return signature;
    }
    private void $setSignature (org.meta_environment.rascal.ast.Signature x)
    {
      this.signature = x;
    }
    public Abstract setSignature (org.meta_environment.rascal.ast.Signature x)
    {
      org.meta_environment.rascal.ast.Abstract z = new Abstract ();
      z.$setSignature (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Tags tags;
    public org.meta_environment.rascal.ast.Tags getTags ()
    {
      return tags;
    }
    private void $setTags (org.meta_environment.rascal.ast.Tags x)
    {
      this.tags = x;
    }
    public Abstract setTags (org.meta_environment.rascal.ast.Tags x)
    {
      org.meta_environment.rascal.ast.Abstract z = new Abstract ();
      z.$setTags (x);
      return z;
    }
  }
}
