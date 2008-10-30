package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
    public ModuleName getname ()
    {
      return name;
    }
    private void $setname (ModuleName x)
    {
      this.name = x;
    }
    public ActualsRenaming setname (ModuleName x)
    {
      ActualsRenaming z = new ActualsRenaming ();
      z.$setname (x);
      return z;
    }
    private ModuleActuals actuals;
    public ModuleActuals getactuals ()
    {
      return actuals;
    }
    private void $setactuals (ModuleActuals x)
    {
      this.actuals = x;
    }
    public ActualsRenaming setactuals (ModuleActuals x)
    {
      ActualsRenaming z = new ActualsRenaming ();
      z.$setactuals (x);
      return z;
    }
    private Renamings renamings;
    public Renamings getrenamings ()
    {
      return renamings;
    }
    private void $setrenamings (Renamings x)
    {
      this.renamings = x;
    }
    public ActualsRenaming setrenamings (Renamings x)
    {
      ActualsRenaming z = new ActualsRenaming ();
      z.$setrenamings (x);
      return z;
    }
  }
  public class Ambiguity extends ImportedModule
  {
    private final List < ImportedModule > alternatives;
    public Ambiguity (List < ImportedModule > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < ImportedModule > getAlternatives ()
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
    public ModuleName getname ()
    {
      return name;
    }
    private void $setname (ModuleName x)
    {
      this.name = x;
    }
    public Actuals setname (ModuleName x)
    {
      Actuals z = new Actuals ();
      z.$setname (x);
      return z;
    }
    private ModuleActuals actuals;
    public ModuleActuals getactuals ()
    {
      return actuals;
    }
    private void $setactuals (ModuleActuals x)
    {
      this.actuals = x;
    }
    public Actuals setactuals (ModuleActuals x)
    {
      Actuals z = new Actuals ();
      z.$setactuals (x);
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
    public ModuleName getname ()
    {
      return name;
    }
    private void $setname (ModuleName x)
    {
      this.name = x;
    }
    public Renamings setname (ModuleName x)
    {
      Renamings z = new Renamings ();
      z.$setname (x);
      return z;
    }
    private Renamings renamings;
    public Renamings getrenamings ()
    {
      return renamings;
    }
    private void $setrenamings (Renamings x)
    {
      this.renamings = x;
    }
    public Renamings setrenamings (Renamings x)
    {
      Renamings z = new Renamings ();
      z.$setrenamings (x);
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
  }
}
