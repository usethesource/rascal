package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Header extends AbstractAST
{
  public org.meta_environment.rascal.ast.ModuleName getName ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Tags getTags ()
  {
    throw new UnsupportedOperationException ();
  }
  public java.util.List < org.meta_environment.rascal.ast.Import >
    getImports ()
  {
    throw new UnsupportedOperationException ();
  }
  static public class Default extends Header
  {
/* "module" name:ModuleName tags:Tags imports:Import* -> Header {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.ModuleName name,
			  org.meta_environment.rascal.ast.Tags tags,
			  java.util.List <
			  org.meta_environment.rascal.ast.Import > imports)
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
    private org.meta_environment.rascal.ast.ModuleName name;
    public org.meta_environment.rascal.ast.ModuleName getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.ModuleName x)
    {
      this.name = x;
    }
    public Default setName (org.meta_environment.rascal.ast.ModuleName x)
    {
      Default z = new Default ();
      z.$setName (x);
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
      Default z = new Default ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Import > imports;
    public java.util.List < org.meta_environment.rascal.ast.Import >
      getImports ()
    {
      return imports;
    }
    private void $setImports (java.util.List <
			      org.meta_environment.rascal.ast.Import > x)
    {
      this.imports = x;
    }
    public Default setImports (java.util.List <
			       org.meta_environment.rascal.ast.Import > x)
    {
      Default z = new Default ();
      z.$setImports (x);
      return z;
    }
  }
  static public class Ambiguity extends Header
  {
    private final java.util.List < org.meta_environment.rascal.ast.Header >
      alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.Header > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Header >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public org.meta_environment.rascal.ast.ModuleParameters getParams ()
  {
    throw new UnsupportedOperationException ();
  }
  static public class Parameters extends Header
  {
/* "module" name:ModuleName params:ModuleParameters tags:Tags imports:Import* -> Header {cons("Parameters")} */
    private Parameters ()
    {
    }
    /*package */ Parameters (ITree tree,
			     org.meta_environment.rascal.ast.ModuleName name,
			     org.meta_environment.rascal.ast.
			     ModuleParameters params,
			     org.meta_environment.rascal.ast.Tags tags,
			     java.util.List <
			     org.meta_environment.rascal.ast.Import > imports)
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
    private org.meta_environment.rascal.ast.ModuleName name;
    public org.meta_environment.rascal.ast.ModuleName getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.ModuleName x)
    {
      this.name = x;
    }
    public Parameters setName (org.meta_environment.rascal.ast.ModuleName x)
    {
      Parameters z = new Parameters ();
      z.$setName (x);
      return z;
    }
    private org.meta_environment.rascal.ast.ModuleParameters params;
    public org.meta_environment.rascal.ast.ModuleParameters getParams ()
    {
      return params;
    }
    private void $setParams (org.meta_environment.rascal.ast.
			     ModuleParameters x)
    {
      this.params = x;
    }
    public Parameters setParams (org.meta_environment.rascal.ast.
				 ModuleParameters x)
    {
      Parameters z = new Parameters ();
      z.$setParams (x);
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
    public Parameters setTags (org.meta_environment.rascal.ast.Tags x)
    {
      Parameters z = new Parameters ();
      z.$setTags (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Import > imports;
    public java.util.List < org.meta_environment.rascal.ast.Import >
      getImports ()
    {
      return imports;
    }
    private void $setImports (java.util.List <
			      org.meta_environment.rascal.ast.Import > x)
    {
      this.imports = x;
    }
    public Parameters setImports (java.util.List <
				  org.meta_environment.rascal.ast.Import > x)
    {
      Parameters z = new Parameters ();
      z.$setImports (x);
      return z;
    }
  }
}
