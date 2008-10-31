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
      return visitor.visitImportedModuleActualsRenaming (this);
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
    public ActualsRenaming setName (ModuleName x)
    {
      ActualsRenaming z = new ActualsRenaming ();
      z.$setName (x);
      return z;
    }
    private ModuleActuals actuals;
    public ModuleActuals getActuals ()
    {
      return actuals;
    }
    private void $setActuals (ModuleActuals x)
    {
      this.actuals = x;
    }
    public ActualsRenaming setActuals (ModuleActuals x)
    {
      ActualsRenaming z = new ActualsRenaming ();
      z.$setActuals (x);
      return z;
    }
    private Renamings renamings;
    public Renamings getRenamings ()
    {
      return renamings;
    }
    private void $setRenamings (Renamings x)
    {
      this.renamings = x;
    }
    public ActualsRenaming setRenamings (Renamings x)
    {
      ActualsRenaming z = new ActualsRenaming ();
      z.$setRenamings (x);
      return z;
    }
  }
  public class Ambiguity extends ImportedModule
  {
    private final java.util.List < ImportedModule > alternatives;
    public Ambiguity (java.util.List < ImportedModule > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < ImportedModule > getAlternatives ()
    {
      return alternatives;
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
      return visitor.visitImportedModuleActuals (this);
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
    public Actuals setName (ModuleName x)
    {
      Actuals z = new Actuals ();
      z.$setName (x);
      return z;
    }
    private ModuleActuals actuals;
    public ModuleActuals getActuals ()
    {
      return actuals;
    }
    private void $setActuals (ModuleActuals x)
    {
      this.actuals = x;
    }
    public Actuals setActuals (ModuleActuals x)
    {
      Actuals z = new Actuals ();
      z.$setActuals (x);
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
      return visitor.visitImportedModuleRenamings (this);
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
    public Renamings setName (ModuleName x)
    {
      Renamings z = new Renamings ();
      z.$setName (x);
      return z;
    }
    private Renamings renamings;
    public Renamings getRenamings ()
    {
      return renamings;
    }
    private void $setRenamings (Renamings x)
    {
      this.renamings = x;
    }
    public Renamings setRenamings (Renamings x)
    {
      Renamings z = new Renamings ();
      z.$setRenamings (x);
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
      return visitor.visitImportedModuleDefault (this);
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
  }
}
