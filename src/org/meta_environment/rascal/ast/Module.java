package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Module extends AbstractAST
{
  static public class Default extends Module
  {
/* header:Header body:Body -> Module {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.Header header,
			  org.meta_environment.rascal.ast.Body body)
    {
      this.tree = tree;
      this.header = header;
      this.body = body;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitModuleDefault (this);
    }
    private org.meta_environment.rascal.ast.Header header;
    public org.meta_environment.rascal.ast.Header getHeader ()
    {
      return header;
    }
    private void $setHeader (org.meta_environment.rascal.ast.Header x)
    {
      this.header = x;
    }
    public Default setHeader (org.meta_environment.rascal.ast.Header x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setHeader (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Body body;
    public org.meta_environment.rascal.ast.Body getBody ()
    {
      return body;
    }
    private void $setBody (org.meta_environment.rascal.ast.Body x)
    {
      this.body = x;
    }
    public Default setBody (org.meta_environment.rascal.ast.Body x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setBody (x);
      return z;
    }
  }
  static public class Ambiguity extends Module
  {
    public Module.Ambiguity makeModuleAmbiguity (java.util.List < Module >
						 alternatives)
    {
      Module.Ambiguity amb = new Module.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Module.Ambiguity) table.get (amb);
    }
    private final java.util.List < Module > alternatives;
    public Ambiguity (java.util.List < Module > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Module > getAlternatives ()
    {
      return alternatives;
    }
  }
}
