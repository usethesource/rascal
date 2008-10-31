package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Header extends AbstractAST
{
  static public class Default extends Header
  {
/* "module" name:ModuleName tags:Tags imports:Import* -> Header {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, ModuleName name, Tags tags,
			  java.util.List < Import > imports)
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
    public ModuleName getName ()
    {
      return name;
    }
    private void $setName (ModuleName x)
    {
      this.name = x;
    }
    public Default setName (ModuleName x)
    {
      Default z = new Default ();
      z.$setName (x);
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
    private java.util.List < Import > imports;
    public java.util.List < Import > getImports ()
    {
      return imports;
    }
    private void $setImports (java.util.List < Import > x)
    {
      this.imports = x;
    }
    public Default setImports (java.util.List < Import > x)
    {
      Default z = new Default ();
      z.$setImports (x);
      return z;
    }
  }
  public class Ambiguity extends Header
  {
    private final java.util.List < Header > alternatives;
    public Ambiguity (java.util.List < Header > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Header > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Parameters extends Header
  {
/* "module" name:ModuleName params:ModuleParameters tags:Tags imports:Import* -> Header {cons("Parameters")} */
    private Parameters ()
    {
    }
    /*package */ Parameters (ITree tree, ModuleName name,
			     ModuleParameters params, Tags tags,
			     java.util.List < Import > imports)
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
    public ModuleName getName ()
    {
      return name;
    }
    private void $setName (ModuleName x)
    {
      this.name = x;
    }
    public Parameters setName (ModuleName x)
    {
      Parameters z = new Parameters ();
      z.$setName (x);
      return z;
    }
    private ModuleParameters params;
    public ModuleParameters getParams ()
    {
      return params;
    }
    private void $setParams (ModuleParameters x)
    {
      this.params = x;
    }
    public Parameters setParams (ModuleParameters x)
    {
      Parameters z = new Parameters ();
      z.$setParams (x);
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
    public Parameters setTags (Tags x)
    {
      Parameters z = new Parameters ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < Import > imports;
    public java.util.List < Import > getImports ()
    {
      return imports;
    }
    private void $setImports (java.util.List < Import > x)
    {
      this.imports = x;
    }
    public Parameters setImports (java.util.List < Import > x)
    {
      Parameters z = new Parameters ();
      z.$setImports (x);
      return z;
    }
  }
}
