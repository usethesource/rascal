package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NameCompleter;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.TreeAdapter;

public class CommandExecutor {
	
	private PrintWriter stdout;
	private PrintWriter stderr;
	private final IValueFactory vf;
	private ISourceLocation compilerBinaryLocation;
	private String consoleInputName = "ConsoleInput";
	private ISourceLocation consoleInputLocation;
	private RVMExecutable rvmConsoleExecutable;
	private RVMExecutable lastRvmConsoleExecutable;
	private final Prelude prelude;
	private RVM rvmCompiler;
	private final Function compileAndLinkIncremental;
	
	private DebugREPLFrameObserver debugObserver;
	
	boolean debug;
	boolean debugRVM;
	boolean testsuite;
	boolean profile;
	boolean trackCalls;
	boolean coverage;
	boolean useJVM;
	boolean verbose;
	boolean serialize;
	
	private ArrayList<String> imports;
	private ArrayList<String> syntaxDefinitions;
	private ArrayList<String> declarations;	
	
	private HashMap<String, Variable> variables = new HashMap<String, Variable>();
	
	private final String shellModuleName = "CompiledRascalShell";

	private IValue[] compileArgs;
	
	public CommandExecutor(PrintWriter stdout, PrintWriter stderr) {
		this.stdout = stdout;
		this.stderr = stderr; 
		vf = ValueFactoryFactory.getValueFactory();
		prelude = new Prelude(vf);
		try {
			compilerBinaryLocation = vf.sourceLocation("compressed+boot", "", "Kernel.rvm.ser.gz");
			//compilerBinaryLocation = vf.sourceLocation("compressed+home", "", "/bin/rascal/src/org/rascalmpl/library/lang/rascal/boot/Kernel.rvm.ser.gz");
			consoleInputLocation = vf.sourceLocation("test-modules", "", consoleInputName + ".rsc");
			
		} catch (URISyntaxException e) {
			throw new RuntimeException("Cannot initialize: " + e.getMessage());
		}
		debug = false;
		debugRVM = false;
		testsuite = false;
		profile = false;
		trackCalls = false;
		coverage = false;
		useJVM = false;
		verbose = false;
		serialize = false;
		
		IMapWriter w = vf.mapWriter();
		w.put(vf.string("bootstrapParser"), vf.string(""));
		IMap CompiledRascalShellModuleTags = w.done();
		
		w = vf.mapWriter();
		w.put(vf.string(shellModuleName), CompiledRascalShellModuleTags);
		IMap moduleTags = w.done();
		
		RascalExecutionContext rex = new RascalExecutionContext(vf, this.stdout, this.stderr, moduleTags, null, null, false, false, false, /*profile*/false, false, false, false, null, null, null);
		rex.setCurrentModuleName(shellModuleName);
		
		try {
			rvmCompiler = RVM.readFromFileAndInitialize(compilerBinaryLocation, rex);
		} catch (IOException e) {
			throw new RuntimeException("Cannot initialize: " + e.getMessage());
		}
		
		TypeFactory tf = TypeFactory.getInstance();
		compileAndLinkIncremental = rvmCompiler.getFunction("compileAndLinkIncremental", 
															tf.abstractDataType(new TypeStore(), "RVMProgram"), 
															tf.tupleType(tf.stringType(), 
																		 tf.boolType()
																		 //tf.abstractDataType(new TypeStore(), "PathConfig")
																		 ));
		if(compileAndLinkIncremental == null){
			throw new RuntimeException("Cannot find compileAndLinkIncremental function");
		}
		
		
		
		compileArgs = new IValue[] {vf.string(consoleInputName), vf.bool(true)};
		
		imports = new ArrayList<String>();
		syntaxDefinitions = new ArrayList<String>();
		declarations = new ArrayList<String>();
	}
	
	public void setDebugObserver(DebugREPLFrameObserver observer){
		this.debugObserver = observer;
	}
	
	private Map<String, IValue> makeCompileKwParams(){
		HashMap<String, IValue> w = new HashMap<>();
		w.put("verbose", vf.bool(false));
		return w;
	}
	
	IValue executeModule(String main, boolean onlyMainChanged){
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
		System.err.println("----------------------");
		System.err.println(modString);
		System.err.println("----------------------");
		try {
			prelude.writeFile(consoleInputLocation, vf.list(vf.string(modString)));
			compileArgs[1] = vf.bool(onlyMainChanged);
			IConstructor consoleRVMProgram = (IConstructor) rvmCompiler.executeFunction(compileAndLinkIncremental, compileArgs, makeCompileKwParams());
			rvmConsoleExecutable = ExecutionTools.loadProgram(consoleInputLocation, consoleRVMProgram, vf.bool(useJVM));
			
			RascalExecutionContext rex = new RascalExecutionContext(vf, stdout, stderr, null, null, null, debug, debugRVM, testsuite, profile, trackCalls, coverage, useJVM, null, debugObserver, null);
			rex.setCurrentModuleName(shellModuleName);
			IValue val = ExecutionTools.executeProgram(rvmConsoleExecutable, vf.mapWriter().done(), rex);
			lastRvmConsoleExecutable = rvmConsoleExecutable;
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
			return executeModule("\nvalue main() = " + src + ";\n", true);
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
				return executeModule("\nvalue main() = " + innerExp + ";\n", true);
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
					IValue val = executeModule("\nvalue main() { " + src + "}\n", true);
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
		
		return executeModule("\nvalue main() = true;\n", false);
	}
	
	public IValue evalImport(String src, ITree imp) throws FactTypeUseException, IOException{
		if(is(imp, "default")){
			String impName = unparse(get(get(imp, "module"), "name"));
			if(imports.contains(impName)){
				return null;
			}
			imports.add(impName);
			try {
				return executeModule("\nvalue main() = true;\n", false);
			} catch (Exception e){
				imports.remove(impName);
				return null;
			}
		}
		if(is(imp, "syntax")){
			syntaxDefinitions.add(src);
			try {
				return executeModule("\nvalue main() = true;\n", false);
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
					return executeModule("\nvalue main() = " + name + ";\n", false);
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
			return executeModule("\nvalue main() = true;\n", false);
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
				profile =  getBooleanValue(val);
				return report(name + " set to "  + profile);
				
			case "trace": case "tracing": case "trackCalls":
				trackCalls = getBooleanValue(val);
				return report(name + " set to "  + trackCalls);
				
			case "coverage":
				coverage = getBooleanValue(val);
				return report(name + " set to "  + coverage);
				
			case "debugRVM":
				debugRVM = getBooleanValue(val);
				return report(name + " set to "  + coverage);
				
			case "testsuite":
				testsuite = getBooleanValue(val);
				return report(name + " set to "  + coverage);
				
			default:
				return report("Unrecognized option : " + name);
			}
	
		case "help":
			String[] helpText = {
					"Help for the compiler-based Rascal command shell.",
					"",
					"RascalShell commands:",
					"    :help                    Prints this message",
					"    :quit or EOF             Quits this Rascal command shell",
					"    :declarations            Lists all declarations",
					//":edit <modulename>         Opens an editor for that module",
					"    :modules                 Lists all imported modules",
					//":test                      Runs all unit tests currently loaded",
					//":unimport <modulename>     Undo an import",
					//":undeclare <name>          Undeclares a variable or function introduced in the shell",
					//":history                   Print the command history",
					"    :clear                   Clears the console",
					"    :set <option> <expr>     Sets a RascalShell option to value",
					"    e.g. :set profiling true",
					"         :set tracing false",
					"",
					"Keyboard essentials:",
					"    <UP>                     Previous command in history",
					"    <DOWN>                   Next command in history",
					"    <CTRL>r                  Backward search in history",
					"    <CTRL>s                  Forward search in history",
					"    <TAB>                    Complete previous word",
					"    <CTRL>a                  Move cursor to begin of line",
					"    <CTRL>e                  Move cursor to end of line",
					"    <CTRL>k                  Kill remainder of line after cursor",
					"    <CTRL>l                  Clear screen",
					"",
					"Further help: XXX"
				};
			for(String line : helpText){
				stdout.println(line);;
			}
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
	
	public Collection<String> completePartialIdentifier(String qualifier, String term) {
		if(lastRvmConsoleExecutable != null){
			return lastRvmConsoleExecutable.completePartialIdentifier(new NameCompleter(), term).getResult();
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
