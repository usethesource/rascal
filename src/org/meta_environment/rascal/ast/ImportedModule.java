package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class ImportedModule extends AbstractAST
{
  public class ActualsRenaming extends ImportedModule
  {
/* name:ModuleName actuals:ModuleActuals renamings:Renamings -> ImportedModule {cons("ActualsRenaming")} */
    private ActualsRenaming ()
    {
    }
    /*package */ ActualsRenaming (ITree tree, ModuleName name,
				  ModuleActuals actuals, Renamings renamings)
    {
      this.tree = tree;
      this.name = name;
      this.actuals = actuals;
      this.renamings = renamings;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitActualsRenamingImportedModule (this);
    }
    private ModuleName name;
    public ModuleName getname ()
    {
      return name;
    }
    private void privateSetname (ModuleName x)
    {
      this.name = x;
    }
    public ActualsRenaming setname (ModuleName x)
    {
      z = new ActualsRenaming ();
      z.privateSetname (x);
      return z;
    }
    private ModuleActuals actuals;
    public ModuleActuals getactuals ()
    {
      return actuals;
    }
    private void privateSetactuals (ModuleActuals x)
    {
      this.actuals = x;
    }
    public ActualsRenaming setactuals (ModuleActuals x)
    {
      z = new ActualsRenaming ();
      z.privateSetactuals (x);
      return z;
    }
    private Renamings renamings;
    public Renamings getrenamings ()
    {
      return renamings;
    }
    private void privateSetrenamings (Renamings x)
    {
      this.renamings = x;
    }
    public ActualsRenaming setrenamings (Renamings x)
    {
      z = new ActualsRenaming ();
      z.privateSetrenamings (x);
      return z;
    }
  }
  public class Actuals extends ImportedModule
  {
/* name:ModuleName actuals:ModuleActuals -> ImportedModule {cons("Actuals")} */
    private Actuals ()
    {
    }
    /*package */ Actuals (ITree tree, ModuleName name, ModuleActuals actuals)
    {
      this.tree = tree;
      this.name = name;
      this.actuals = actuals;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitActualsImportedModule (this);
    }
    private ModuleName name;
    public ModuleName getname ()
    {
      return name;
    }
    private void privateSetname (ModuleName x)
    {
      this.name = x;
    }
    public Actuals setname (ModuleName x)
    {
      z = new Actuals ();
      z.privateSetname (x);
      return z;
    }
    private ModuleActuals actuals;
    public ModuleActuals getactuals ()
    {
      return actuals;
    }
    private void privateSetactuals (ModuleActuals x)
    {
      this.actuals = x;
    }
    public Actuals setactuals (ModuleActuals x)
    {
      z = new Actuals ();
      z.privateSetactuals (x);
      return z;
    }
  }
  public class Renamings extends ImportedModule
  {
/* name:ModuleName renamings:Renamings -> ImportedModule {cons("Renamings")} */
    private Renamings ()
    {
    }
    /*package */ Renamings (ITree tree, ModuleName name, Renamings renamings)
    {
      this.tree = tree;
      this.name = name;
      this.renamings = renamings;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitRenamingsImportedModule (this);
    }
    private ModuleName name;
    public ModuleName getname ()
    {
      return name;
    }
    private void privateSetname (ModuleName x)
    {
      this.name = x;
    }
    public Renamings setname (ModuleName x)
    {
      z = new Renamings ();
      z.privateSetname (x);
      return z;
    }
    private Renamings renamings;
    public Renamings getrenamings ()
    {
      return renamings;
    }
    private void privateSetrenamings (Renamings x)
    {
      this.renamings = x;
    }
    public Renamings setrenamings (Renamings x)
    {
      z = new Renamings ();
      z.privateSetrenamings (x);
      return z;
    }
  }
  public class Default extends ImportedModule
  {
/* name:ModuleName -> ImportedModule {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, ModuleName name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDefaultImportedModule (this);
    }
    private ModuleName name;
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
  }
}
