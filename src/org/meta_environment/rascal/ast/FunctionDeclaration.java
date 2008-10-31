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
    /*package */ Default (ITree tree, Signature signature, Tags tags,
			  FunctionBody body)
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
    private Signature signature;
    public Signature getSignature ()
    {
      return signature;
    }
    private void $setSignature (Signature x)
    {
      this.signature = x;
    }
    public Default setSignature (Signature x)
    {
      Default z = new Default ();
      z.$setSignature (x);
      return z;
    }
    private Tags tags;
    public Tags getTags ()
    {
      return tags;
    }
    private void $setTags (Tags x)
    {
      this.tags = x;
    }
    public Default setTags (Tags x)
    {
      Default z = new Default ();
      z.$setTags (x);
      return z;
    }
    private FunctionBody body;
    public FunctionBody getBody ()
    {
      return body;
    }
    private void $setBody (FunctionBody x)
    {
      this.body = x;
    }
    public Default setBody (FunctionBody x)
    {
      Default z = new Default ();
      z.$setBody (x);
      return z;
    }
  }
  static public class Ambiguity extends FunctionDeclaration
  {
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
    /*package */ Abstract (ITree tree, Signature signature, Tags tags)
    {
      this.tree = tree;
      this.signature = signature;
      this.tags = tags;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFunctionDeclarationAbstract (this);
    }
    private Signature signature;
    public Signature getSignature ()
    {
      return signature;
    }
    private void $setSignature (Signature x)
    {
      this.signature = x;
    }
    public Abstract setSignature (Signature x)
    {
      Abstract z = new Abstract ();
      z.$setSignature (x);
      return z;
    }
    private Tags tags;
    public Tags getTags ()
    {
      return tags;
    }
    private void $setTags (Tags x)
    {
      this.tags = x;
    }
    public Abstract setTags (Tags x)
    {
      Abstract z = new Abstract ();
      z.$setTags (x);
      return z;
    }
  }
}
