package org.meta_environment.rascal.ast;
public abstract class Header extends AbstractAST
{
  public class Default extends Header
  {
    private ModuleName name;
    private Tags tags;
    private List < Import > imports;

    private Default ()
    {
    }
    /*package */ Default (ITree tree, ModuleName name, Tags tags,
			  List < Import > imports)
    {
      this.tree = tree;
      this.name = name;
      this.tags = tags;
      this.imports = imports;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultHeader (this);
    }
    private final ModuleName name;
    public ModuleName getname ()
    {
      return name;
    }
    private void privateSetname (ModuleName x)
    {
      this.name = x;
    }
    public Default setname (ModuleName x)
    {
      z = new Default ();
      z.privateSetname (x);
      return z;
    }
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public Default settags (Tags x)
    {
      z = new Default ();
      z.privateSettags (x);
      return z;
    }
    private final List < Import > imports;
    public List < Import > getimports ()
    {
      return imports;
    }
    private void privateSetimports (List < Import > x)
    {
      this.imports = x;
    }
    public Default setimports (List < Import > x)
    {
      z = new Default ();
      z.privateSetimports (x);
      return z;
    }
  }
  public class Parameters extends Header
  {
    private ModuleName name;
    private ModuleParameters params;
    private Tags tags;
    private List < Import > imports;

    private Parameters ()
    {
    }
    /*package */ Parameters (ITree tree, ModuleName name,
			     ModuleParameters params, Tags tags,
			     List < Import > imports)
    {
      this.tree = tree;
      this.name = name;
      this.params = params;
      this.tags = tags;
      this.imports = imports;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitParametersHeader (this);
    }
    private final ModuleName name;
    public ModuleName getname ()
    {
      return name;
    }
    private void privateSetname (ModuleName x)
    {
      this.name = x;
    }
    public Parameters setname (ModuleName x)
    {
      z = new Parameters ();
      z.privateSetname (x);
      return z;
    }
    private final ModuleParameters params;
    public ModuleParameters getparams ()
    {
      return params;
    }
    private void privateSetparams (ModuleParameters x)
    {
      this.params = x;
    }
    public Parameters setparams (ModuleParameters x)
    {
      z = new Parameters ();
      z.privateSetparams (x);
      return z;
    }
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public Parameters settags (Tags x)
    {
      z = new Parameters ();
      z.privateSettags (x);
      return z;
    }
    private final List < Import > imports;
    public List < Import > getimports ()
    {
      return imports;
    }
    private void privateSetimports (List < Import > x)
    {
      this.imports = x;
    }
    public Parameters setimports (List < Import > x)
    {
      z = new Parameters ();
      z.privateSetimports (x);
      return z;
    }
  }
}
