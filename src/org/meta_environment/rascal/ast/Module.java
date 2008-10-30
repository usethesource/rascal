package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Module extends AbstractAST
{
  public class Default extends Module
  {
/* header:Header body:Body -> Module {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, Header header, Body body)
    {
      this.tree = tree;
      this.header = header;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitModuleDefault (this);
    }
    private Header header;
    public Header getHeader ()
    {
      return header;
    }
    private void $setHeader (Header x)
    {
      this.header = x;
    }
    public Default setHeader (Header x)
    {
      Default z = new Default ();
      z.$setHeader (x);
      return z;
    }
    private Body body;
    public Body getBody ()
    {
      return body;
    }
    private void $setBody (Body x)
    {
      this.body = x;
    }
    public Default setBody (Body x)
    {
      Default z = new Default ();
      z.$setBody (x);
      return z;
    }
  }
  public class Ambiguity extends Module
  {
    private final java.util.List < Module > alternatives;
    public Ambiguity (java.util.List < Module > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Module > getAlternatives ()
    {
      return alternatives;
    }
  }
}
