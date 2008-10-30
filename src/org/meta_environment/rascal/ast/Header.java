package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Header extends AbstractAST
{
  public class Default extends Header
  {
/* "module" name:ModuleName tags:Tags  imports:Import* -> Header {cons("Default")} */
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitHeaderDefault (this);
    }
    private ModuleName name;
    public ModuleName getname ()
    {
      return name;
    }
    private void $setname (ModuleName x)
    {
      this.name = x;
    }
    public Default setname (ModuleName x)
    {
      Default z = new Default ();
      z.$setname (x);
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
    private List < Import > imports;
    public List < Import > getimports ()
    {
      return imports;
    }
    private void $setimports (List < Import > x)
    {
      this.imports = x;
    }
    public Default setimports (List < Import > x)
    {
      Default z = new Default ();
      z.$setimports (x);
      return z;
    }
  }
  public class Ambiguity extends Header
  {
    private final List < Header > alternatives;
    public Ambiguity (List < Header > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Header > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Parameters extends Header
  {
/* "module" name:ModuleName params:ModuleParameters tags:Tags imports:Import* -> Header {cons("Parameters")} */
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitHeaderParameters (this);
    }
    private ModuleName name;
    public ModuleName getname ()
    {
      return name;
    }
    private void $setname (ModuleName x)
    {
      this.name = x;
    }
    public Parameters setname (ModuleName x)
    {
      Parameters z = new Parameters ();
      z.$setname (x);
      return z;
    }
    private ModuleParameters params;
    public ModuleParameters getparams ()
    {
      return params;
    }
    private void $setparams (ModuleParameters x)
    {
      this.params = x;
    }
    public Parameters setparams (ModuleParameters x)
    {
      Parameters z = new Parameters ();
      z.$setparams (x);
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
    public Parameters settags (Tags x)
    {
      Parameters z = new Parameters ();
      z.$settags (x);
      return z;
    }
    private List < Import > imports;
    public List < Import > getimports ()
    {
      return imports;
    }
    private void $setimports (List < Import > x)
    {
      this.imports = x;
    }
    public Parameters setimports (List < Import > x)
    {
      Parameters z = new Parameters ();
      z.$setimports (x);
      return z;
    }
  }
}
