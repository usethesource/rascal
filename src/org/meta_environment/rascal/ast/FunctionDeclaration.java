package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class FunctionDeclaration extends AbstractAST
{
  public class Default extends FunctionDeclaration
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
    public Signature getsignature ()
    {
      return signature;
    }
    private void $setsignature (Signature x)
    {
      this.signature = x;
    }
    public Default setsignature (Signature x)
    {
      Default z = new Default ();
      z.$setsignature (x);
      return z;
    }
    private Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void $settags (Tags x)
    {
      this.tags = x;
    }
    public Default settags (Tags x)
    {
      Default z = new Default ();
      z.$settags (x);
      return z;
    }
    private FunctionBody body;
    public FunctionBody getbody ()
    {
      return body;
    }
    private void $setbody (FunctionBody x)
    {
      this.body = x;
    }
    public Default setbody (FunctionBody x)
    {
      Default z = new Default ();
      z.$setbody (x);
      return z;
    }
  }
  public class Ambiguity extends FunctionDeclaration
  {
    private final List < FunctionDeclaration > alternatives;
    public Ambiguity (List < FunctionDeclaration > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < FunctionDeclaration > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Abstract extends FunctionDeclaration
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
    public Signature getsignature ()
    {
      return signature;
    }
    private void $setsignature (Signature x)
    {
      this.signature = x;
    }
    public Abstract setsignature (Signature x)
    {
      Abstract z = new Abstract ();
      z.$setsignature (x);
      return z;
    }
    private Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void $settags (Tags x)
    {
      this.tags = x;
    }
    public Abstract settags (Tags x)
    {
      Abstract z = new Abstract ();
      z.$settags (x);
      return z;
    }
  }
}
