package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class ShellCommand extends AbstractAST { 
  public boolean isHelp() { return false; }
static public class Help extends ShellCommand {
/* "help" -> ShellCommand {cons("Help")} */
	private Help() { }
	/*package*/ Help(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandHelp(this);
	}

	public boolean isHelp() { return true; }	
}
static public class Ambiguity extends ShellCommand {
  private final java.util.List<org.meta_environment.rascal.ast.ShellCommand> alternatives;
  public Ambiguity(ITree tree, java.util.List<org.meta_environment.rascal.ast.ShellCommand> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.tree = tree;
  }
  public java.util.List<org.meta_environment.rascal.ast.ShellCommand> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitShellCommandAmbiguity(this);
  }
} 
public boolean isQuit() { return false; }
static public class Quit extends ShellCommand {
/* "quit" -> ShellCommand {cons("Quit")} */
	private Quit() { }
	/*package*/ Quit(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandQuit(this);
	}

	public boolean isQuit() { return true; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.Name getName() { throw new UnsupportedOperationException(); }
public boolean hasName() { return false; }
public boolean isEdit() { return false; }
static public class Edit extends ShellCommand {
/* "edit" name:Name -> ShellCommand {cons("Edit")} */
	private Edit() { }
	/*package*/ Edit(ITree tree, org.meta_environment.rascal.ast.Name name) {
		this.tree = tree;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandEdit(this);
	}

	public boolean isEdit() { return true; }

	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.Name name;
	public org.meta_environment.rascal.ast.Name getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.Name x) { this.name = x; }
	public Edit setName(org.meta_environment.rascal.ast.Name x) { 
		Edit z = new Edit();
 		z.$setName(x);
		return z;
	}	
} 
public boolean isHistory() { return false; }
static public class History extends ShellCommand {
/* "history" -> ShellCommand {cons("History")} */
	private History() { }
	/*package*/ History(ITree tree) {
		this.tree = tree;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitShellCommandHistory(this);
	}

	public boolean isHistory() { return true; }	
}
}