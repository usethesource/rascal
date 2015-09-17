package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CoverageLocationCollector;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NameCompleter;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ProfileLocationCollector;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.TreeAdapter;

public class CommandExecutor {
	
	private PrintWriter stdout;
	private PrintWriter stderr;
	private final IValueFactory vf;
	private ISourceLocation compilerBinaryLocation;
	private ISourceLocation consoleInputLocation;
	private final RVMExecutable rvmCompilerExecutable;
	private RVMExecutable rvmConsoleExecutable;
	private final Prelude prelude;
	private final ExecuteProgram execute;
	private final RascalExecutionContext rex;
	private RVM rvmCompiler;
	
	IBool debug;
	IBool testsuite;
	IBool profile;
	IBool trackCalls;
	IBool coverage;
	IBool useJVM;
	IBool verbose;
	IBool serialize;
	private final IList executeArgs;
	
	private ArrayList<String> imports;
	private ArrayList<String> syntaxDefinitions;
	private ArrayList<String> declarations;	
	
	private HashMap<String, Variable> variables = new HashMap<String, Variable>();
	
	private final String shellModuleName = "CompiledRascalShell";

	private IValue[] compileArgs;
	private String compileFunId;
	
	public CommandExecutor(PrintWriter stdout, PrintWriter stderr) {
		this.stdout = stdout;
		this.stderr = stderr; 
		vf = ValueFactoryFactory.getValueFactory();
		prelude = new Prelude(vf);
		execute = new ExecuteProgram(vf);
		try {
			compilerBinaryLocation = vf.sourceLocation("compressed+home", "", "/bin/rascal/src/org/rascalmpl/library/experiments/Compiler/Execute.rvm.ser.gz");
			consoleInputLocation = vf.sourceLocation("test-modules", "", "/ConsoleInput.rsc");
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		debug = vf.bool(false);
		testsuite = vf.bool(false);
		profile = vf.bool(false);
		trackCalls = vf.bool(false);
		coverage = vf.bool(false);
		useJVM = vf.bool(false);
		verbose = vf.bool(true);
		
		serialize = vf.bool(false);
		
		IMapWriter w = vf.mapWriter();
		w.put(vf.string("bootstrapParser"), vf.string(""));
		IMap CompiledRascalShellModuleTags = w.done();
		
		w = vf.mapWriter();
		w.put(vf.string(shellModuleName), CompiledRascalShellModuleTags);
		IMap moduleTags = w.done();
		
		rex = new RascalExecutionContext(vf, stdout, stderr, moduleTags, null, null, false, false, false, false, false, false, null);
		rex.setCurrentModuleName(shellModuleName);
		rvmCompilerExecutable = RVMExecutable.read(compilerBinaryLocation);
		rvmCompiler = execute.initializedRVM(rvmCompilerExecutable, rex);
		RascalPrimitive.init(rvmCompiler, rex);
		
		compileFunId = "experiments::Compiler::Execute/compileAndLink(\\loc();)#318";
		compileArgs = new IValue[] {consoleInputLocation};
		
		executeArgs =  vf.list();
		
		imports = new ArrayList<String>();
		syntaxDefinitions = new ArrayList<String>();
		declarations = new ArrayList<String>();
	}
	
	private IMap makeCompileKwParams(){
		IMapWriter w = vf.mapWriter();
		w.put(vf.string("verbose"), vf.bool(true));
		return w.done();
	}
	
	private IMap makeExecuteKwParams(){
		IMapWriter w = vf.mapWriter();
		w.put(vf.string("debug"), debug);
		w.put(vf.string("testsuite"), testsuite);
		w.put(vf.string("profile"), profile);
		w.put(vf.string("trackCalls"), trackCalls);
		w.put(vf.string("coverage"), coverage);
		w.put(vf.string("useJVM"), useJVM);
		w.put(vf.string("verbose"), verbose);
		w.put(vf.string("serialize"), serialize);
		return w.done();
	}
	
	IValue executeModule(String main){
		StringWriter w = new StringWriter();
		w.append("@bootstrapParser module ConsoleInput\n");
		for(String imp : imports){
			w.append("import ").append(imp).append(";\n");
		}
		for(String syn : syntaxDefinitions){
			w.append(syn);
		}
		for(String decl : declarations){
			w.append(decl);
		}
		for(String name : variables.keySet()){
			Variable var = variables.get(name);
			w.append(var.type).append(" ").append(name).append(" = ").append(var.value.toString()).append(";\n");
		}
		w.append(main);
		String modString = w.toString();
		stdout.println(modString);
		try {
			prelude.writeFile(consoleInputLocation, vf.list(vf.string(modString)));
			stdout.println("ConsoleInput.rsc: " + prelude.lastModified(consoleInputLocation));
			stdout.flush();
			IConstructor consoleRVMProgram = (IConstructor) rvmCompiler.executeFunction(compileFunId, compileArgs, makeCompileKwParams());
			
			rvmConsoleExecutable = execute.loadProgram(consoleInputLocation, consoleRVMProgram, useJVM);
			
			IValue val = execute.executeProgram(rvmConsoleExecutable, vf.list(), rex);

			if(profile.getValue()){
				((ProfileLocationCollector) rvmCompiler.getLocationCollector()).report(rvmCompiler.getStdOut());
			} else if(coverage.getValue()){
				((CoverageLocationCollector) rvmCompiler.getLocationCollector()).report(rvmCompiler.getStdOut());
			}
			return val;
		} catch (Exception e){
			stderr.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean is(ITree tree, String consName){
		return TreeAdapter.getConstructorName(tree).equals(consName);
	}
	
	private ITree get(ITree tree, String fieldName){
		return TreeAdapter.getArg(tree, fieldName);
	}
	
	private boolean has(ITree tree, String fieldName){
		try {
			TreeAdapter.getArg(tree, fieldName);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	public IValue eval(Object object, String statement, ISourceLocation rootLocation) {
		ITree cmd = parseCommand(statement, rootLocation);
		
		cmd = TreeAdapter.getStartTop(cmd);
		
		if(is(cmd, "expression")){
			return evalExpression(statement, get(cmd, "expression"));
		}
		if(is(cmd, "statement")){
			try {
				return evalStatement(statement, get(cmd, "statement"));
			} catch (FactTypeUseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		if(is(cmd, "import")){
			try {
				return evalImport(statement, get(cmd, "imported"));
			} catch (FactTypeUseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		if(is(cmd, "declaration")){
			try {
				return evalDeclaration(statement, get(cmd, "declaration"));
			} catch (FactTypeUseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(is(cmd, "shell")){
			try {
				return evalShellCommand(statement, get(cmd, "command"));
			} catch (FactTypeUseException | IOException e) {
			}
		}
		return null;
	}
	
	public IValue evalExpression(String src, ITree exp){
		try {
			return executeModule("\nvalue main(list[value] args) = " + src + ";\n");
		} catch (Thrown e){
			return null;
		}
	}
	
	void declareVar(String type, String name, String val){
		variables.put(name,  new Variable(type, name, val));
	}
	
	IValue report(String msg){
		stdout.println(msg);
		stdout.flush();
		return null;
	}
	
	String getBaseVar(ITree assignable) throws FactTypeUseException, IOException{
		if(is(assignable, "variable")){
			return unparse(get(assignable, "qualifiedName"));
		}
		if(has(assignable, "receiver")){
			return getBaseVar(get(assignable, "receiver"));
		}
		return null;
	}
	
	public IValue evalStatement(String src, ITree stat) throws FactTypeUseException, IOException{
		
		String consName = TreeAdapter.getConstructorName(stat);
		switch(consName){
		
		case "expression":
			String innerExp = unparse(get(stat, "expression"));
			try {
				return executeModule("\nvalue main(list[value] args) = " + innerExp + ";\n");
			} catch (Exception e){
				return null;
			}
			
		case "assignment":
			//assignment: Assignable assignable Assignment operator Statement!functionDeclaration!variableDeclaration statement
			
			ITree assignable = get(stat, "assignable");
			String name = getBaseVar(assignable);
			
			if(name != null){
				Variable var = variables.get(name);
				if(var != null){
					IValue val = executeModule("\nvalue main(list[value] args) { " + src + "}\n");
					var.value = val.toString();
					return val;
				} else {
					return report("Variable " + name + " should be declared first");
				}
			}
			return report("Assignable is not supported supported");
			
		case "variableDeclaration":
			ITree declarator = get(get(stat, "variableDeclaration"), "declarator");
			ITree type = get(declarator, "type");
			ITree variables = get(declarator, "variables");
			IList vars = TreeAdapter.getListASTArgs(variables);
			if(vars.length() != 1){
				return report("Multiple names in variable declaration are not supported");
			}
			ITree var = (ITree) vars.get(0);
			if(is(var, "initialized")){
				String name1 = unparse(get(var, "name"));
				String initial = unparse(get(var, "initial"));
				if(variables.get(name1) != null){
					report("Redeclaring variable " + name1);
				}
				declareVar(unparse(type), name1, initial);
			} else {
				return report("Initialization required in variable declaration");
			}
		}
		
		return executeModule("\nvalue main(list[value] args) = true;\n");
	}
	
	public IValue evalImport(String src, ITree imp) throws FactTypeUseException, IOException{
		if(is(imp, "default")){
			String impName = unparse(get(get(imp, "module"), "name"));
			if(imports.contains(impName)){
				return null;
			}
			imports.add(impName);
			try {
				return executeModule("\nvalue main(list[value] args) = true;\n");
			} catch (Exception e){
				imports.remove(impName);
				return null;
			}
		}
		if(is(imp, "syntax")){
			syntaxDefinitions.add(src);
			try {
				return executeModule("\nvalue main(list[value] args) = true;\n");
			} catch (Exception e){
				syntaxDefinitions.remove(src);
				return null;
			}
		}
		
		return null;
	}
	
	public IValue evalDeclaration(String src, ITree cmd) throws FactTypeUseException, IOException{
		
		if(is(cmd, "variable")){
			ITree type = get(cmd, "type");
			ITree variables = get(cmd, "variables");
			IList vars = TreeAdapter.getListASTArgs(variables);
			if(vars.length() != 1){
				return report("Multiple names in variable declaration not supported");
			}
			ITree var = (ITree) vars.get(0);
			if(is(var, "initialized")){
				String name = unparse(get(var, "name"));
				String initial = unparse(get(var, "initial"));
				declareVar(unparse(type), name, initial);
				try {
					return executeModule("\nvalue main(list[value] args) = " + name + ";\n");
				} catch (Exception e){
					this.variables.remove(name);
					return null;
				}
			} else {
				return report("Initialization required in variable declaration");
			}
		}
		
		declarations.add(src);
	
		try {
			return executeModule("\nvalue main(list[value] args) = true;\n");
		} catch (Exception e){
			declarations.remove(src);
			return null;
		}
	}
	
	private boolean getBooleanValue(String val){
		switch(val){
		case "true": return true;
		case "false": return false;
		default:
			stdout.println("'" + val + "' is not a boolean value");
			return false;
		}
	}
	
	String unparse(ITree tree) throws FactTypeUseException, IOException{
		StringWriter w = new StringWriter();
		TreeAdapter.unparse(tree, w);
		return w.toString();
	}
	
	public IValue evalShellCommand(String src, ITree exp) throws FactTypeUseException, IOException{
		String cmd = TreeAdapter.getConstructorName(exp);
		switch(cmd){
		
		case "setOption":
			String name = unparse(get(exp, "name"));
			String val = unparse(get(exp, "expression"));
			
			switch(name){
			case "profile": case "profiling":
				profile =  vf.bool(getBooleanValue(val));
				return report(name + " set to "  + profile.getValue());
				
			case "trace": case "tracing": case "trackCalls":
				trackCalls = vf.bool(getBooleanValue(val));
				return report(name + " set to "  + trackCalls.getValue());
				
			case "coverage":
				coverage = vf.bool(getBooleanValue(val));
				return report(name + " set to "  + coverage.getValue());
				
			case "debug":
				debug = vf.bool(getBooleanValue(val));
				return report(name + " set to "  + coverage.getValue());
				
			case "testsuite":
				testsuite = vf.bool(getBooleanValue(val));
				return report(name + " set to "  + coverage.getValue());
				
			default:
				return report("Unrecognized option : " + name);
			}
	
		case "help":
			stdout.println("Welcome to the compiler-based Rascal command shell.");
			stdout.println();
			stdout.println("Shell commands:");
			stdout.println(":help                      Prints this message");
			stdout.println(":quit or EOF               Quits the shell");
			stdout.println(":declarations              Lists all visible rules, functions and variables");
			stdout.println(":set <option> <expression> Sets an option");
			stdout.println("e.g. profiling    true/false");
			stdout.println("     tracing      true/false");
			//stdout.println(":edit <modulename>         Opens an editor for that module");
			stdout.println(":modules                   Lists all imported modules");
			//stdout.println(":test                      Runs all unit tests currently loaded");
			//stdout.println(":unimport <modulename>     Undo an import");
			//stdout.println(":undeclare <name>          Undeclares a variable or function introduced in the shell");
			//stdout.println(":history                   Print the command history");
			stdout.println(":clear                     Clears the console");			
			stdout.println();
//			stdout.println("Example rascal statements and declarations:");
//			stdout.println("1 + 1;                     Expressions simply print their stdoutput and type");
//			stdout.println("int a;                     Declarations allocate a name in the current scope");
//			stdout.println("a = 1;                     Assignments store a value in a (optionally previously declared) variable");
//			stdout.println("int a = 1;                 Declaration with initialization");
//			stdout.println("import IO;                 Importing a module makes its public members available");
//			stdout.println("println(\"Hello World\")     Function calling");
//			stdout.println();
			stdout.println("Please read the manual for further information");
			break;
			
		case "listDeclarations":
			
			for(String syn : syntaxDefinitions){
				System.err.println(syn);
			}
			
			for(String decl : declarations){
				System.err.println(decl);
			}
			
			for(String varName : variables.keySet()){
				Variable var = variables.get(varName);
				System.err.println(var.type + " " + varName + ";");
			}
			
			break;
			
		case "listModules":
			for(String imp : imports){
				System.err.println(imp);
			}
			break;
			
		case "clear":
			imports = new ArrayList<String>();
			declarations = new ArrayList<String>();
			break;
		}
		stdout.flush();
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
		if(rvmConsoleExecutable != null){
			NameCompleter completer = new NameCompleter();
			String[] commandKeywords = {":set", "undeclare", ":help", ":unimport", 
					":declarations", ":quit", ":test", ":modules", ":clear"};
			for(String kw : commandKeywords){
				completer.add(kw, term);
			}
			return rvmConsoleExecutable.completePartialIdentifier(completer, term).getResult();
		}
		return null;
	}
}

class Variable {
	String name;
	String type;
	String value;
	
	Variable(String type, String name, String value){
		this.name = name;
		this.type = type;
		this.value = value;
	}
}
