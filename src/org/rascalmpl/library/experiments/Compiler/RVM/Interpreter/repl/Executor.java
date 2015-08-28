package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Collection;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;

public class Executor {
	
	private PrintWriter stdout;
	private PrintWriter stderr;
	IValueFactory vf;
	private ISourceLocation executeSource;
	private RVMExecutable executeRVM;
	
	public Executor(PrintWriter stdout, PrintWriter stderr) {
		
		this.stdout = stdout;
		this.stderr = stderr; 
		vf = ValueFactoryFactory.getValueFactory();
		try {
			executeSource = vf.sourceLocation("home", "", "/bin/rascal/src/org/rascalmpl/library/experiments/Compiler/Execute.rvm.ser.gz)");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//RascalExecutionContext rex = new RascalExecutionContext(vf,stdout,stderr, null,null,null, false, false, false, false, false, null);
		//executeRVM = RVMExecutable.read(executeSource);
	}
	
	public IValue eval(Object object, String statement, ISourceLocation rootLocation) {
		ITree cmd = parseCommand(statement, rootLocation);
		String m = "module TMP\nvalue main(list[value] args) = " + cmd.toString() + ";\n";
		
		return null;
	}
	
	public ITree parseCommand(IRascalMonitor monitor, String command, ISourceLocation location) {
		//IRascalMonitor old = setMonitor(monitor);
		try {
			return parseCommand(command, location);
		}
		finally {
			//setMonitor(old);
		}
	}	
	
	private ITree parseCommand(String command, ISourceLocation location) {
		//__setInterrupt(false);
    IActionExecutor<ITree> actionExecutor =  new NoActionExecutor();
    ITree tree =  new RascalParser().parse(Parser.START_COMMAND, location.getURI(), command.toCharArray(), actionExecutor, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory());

//		if (!noBacktickOutsideStringConstant(command)) {
//		  tree = org.rascalmpl.semantics.dynamic.Import.parseFragments(this, tree, location, getCurrentModuleEnvironment());
//		}
		
		return tree;
	}
	public PrintWriter getStdErr() {
		return stderr;
	}
	public PrintWriter getStdOut() {
		return stdout;
	}
	public Collection<String> completePartialIdentifier(String term) {
		// TODO Auto-generated method stub
		return null;
	}
}
