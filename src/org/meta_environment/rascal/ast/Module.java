package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
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
    public Header getheader ()
    {
      return header;
    }
    private void $setheader (Header x)
    {
      this.header = x;
    }
    public Default setheader (Header x)
    {
      Default z = new Default ();
      z.$setheader (x);
      return z;
    }
    private Body body;
    public Body getbody ()
    {
      return body;
    }
    private void $setbody (Body x)
    {
      this.body = x;
    }
    public Default setbody (Body x)
    {
      Default z = new Default ();
      z.$setbody (x);
      return z;
    }
  }
  public class Ambiguity extends Module
  {
    private final List < Module > alternatives;
    public Ambiguity (List < Module > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Module > getAlternatives ()
    {
      return alternatives;
    }
  }
}
