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
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class Executor {
	
	private PrintWriter stdout;
	private PrintWriter stderr;
	private final IValueFactory vf;
	private ISourceLocation executeBinaryLocation;
	private ISourceLocation tmpSource;
	private final RVMExecutable rvmExecutable;
	private final Prelude prelude;
	private final ExecuteProgram execute;
	private final RascalExecutionContext rex;
	private RVM rvm;
	
	IBool debug;
	IBool testsuite;
	IBool profile;
	IBool trackCalls;
	IBool coverage;
	IBool useJVM;
	IBool verbose;
	private final String executeFunId;
	private final IValue[] executeArgs;
	
	private ArrayList<String> imports;
	private ArrayList<String> declarations;
	
	private HashMap<String, String> variables = new HashMap<String, String>();
	
	public Executor(PrintWriter stdout, PrintWriter stderr) {
		
		this.stdout = stdout;
		this.stderr = stderr; 
		vf = ValueFactoryFactory.getValueFactory();
		prelude = new Prelude(vf);
		execute = new ExecuteProgram(vf);
		try {
			executeBinaryLocation = vf.sourceLocation("compressed+home", "", "/bin/rascal/src/org/rascalmpl/library/experiments/Compiler/Execute.rvm.ser.gz");
			tmpSource = vf.sourceLocation("home", "", "TMP.rsc");
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
		verbose = vf.bool(false);
		
		IMapWriter w = vf.mapWriter();
		w.put(vf.string("bootstrapParser"), vf.string(""));
		IMap CompiledRascalShellModuleTags = w.done();
		
		w = vf.mapWriter();
		w.put(vf.string("CompiledRascalShell"), CompiledRascalShellModuleTags);
		IMap moduleTags = w.done();
		
		rex = new RascalExecutionContext(vf,stdout,stderr, moduleTags, null, null, false, false, false, false, false, false, null);
		rex.setCurrentModuleName("CompiledRascalShell");
		rvmExecutable = RVMExecutable.read(executeBinaryLocation);
		rvm = execute.initializedRVM(rvmExecutable, rex);
		RascalPrimitive.init(rvm, rex);
		
		executeFunId = "experiments::Compiler::Execute/execute(\\loc();list(value());)#283";
		executeArgs = new IValue[] {tmpSource, vf.list()};
		
		imports = new ArrayList<String>();
		declarations = new ArrayList<String>();
	}
	
	private IMap makeKwParams(){
		IMapWriter w = vf.mapWriter();
		w.put(vf.string("debug"), debug);
		w.put(vf.string("testsuite"), testsuite);
		w.put(vf.string("profile"), profile);
		w.put(vf.string("trackCalls"), trackCalls);
		w.put(vf.string("coverage"), coverage);
		w.put(vf.string("useJVM"), useJVM);
		w.put(vf.string("verbose"), verbose);
		return w.done();
	}
	
	IValue executeModule(String main){
		StringWriter w = new StringWriter();
		w.append("@bootstrapParser module TMP\n");
		for(String imp : imports){
			w.append(imp);
		}
		for(String decl : declarations){
			w.append(decl);
		}
		for(String name : variables.keySet()){
			w.append(variables.get(name)).append(" ").append(name).append(";\n");
		}
		w.append(main);
		prelude.writeFile(tmpSource, vf.list(vf.string(w.toString())));
		return rvm.executeFunction(executeFunId, executeArgs, makeKwParams());
	}
	
	private boolean is(ITree tree, String consName){
		return TreeAdapter.getConstructorName(tree).equals(consName);
	}
	
	private ITree get(ITree tree, String fieldName){
		return TreeAdapter.getArg(tree, fieldName);
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
		}
		if(is(cmd, "import")){
			return evalImport(statement, get(cmd, "imported"));
		}
		if(is(cmd, "declaration")){
			return evalDeclaration(statement, get(cmd, "declaration"));
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
		} catch (Exception e){
			return null;
		}
	}
	
	void declareVar(String name, String type){
		variables.put(name,  type);
	}
	
	public IValue evalStatement(String src, ITree stat) throws FactTypeUseException, IOException{
		
		String consName = TreeAdapter.getConstructorName(stat);
		switch(consName){
		case "assignment":
			
		case "variableDeclaration":
			ITree declarator = get(get(stat, "default"), "declarator");
			ITree type = get(declarator, "type");
			ITree variables = get(declarator, "variables");
			IList vars = TreeAdapter.getListASTArgs(variables);
			for(int i = 0; i < vars.length(); i++){
				ITree var = (ITree) vars.get(i);
				declareVar(unparse(get(var, "name")), unparse(type));
			}
		}
		//| @breakable assignment: Assignable assignable Assignment operator Statement!functionDeclaration!variableDeclaration statement
		// Statement:  @breakable variableDeclaration: LocalVariableDeclaration declaration ";"
		//syntax LocalVariableDeclaration
		//= \default: Declarator declarator 
		//syntax Declarator
		//= \default: Type type {Variable ","}+ variables ;
		//	syntax Variable
		//	= initialized: Name name "=" Expression initial 
		//	| unInitialized: Name name ;
		
		return executeModule("\nvalue main(list[value] args) = { " + src + "};\n");
	}
	
	public IValue evalImport(String src, ITree exp){
		imports.add(src);
		try {
			return executeModule("\nvalue main(list[value] args) = true;\n");
		} catch (Exception e){
			imports.remove(src);
			return null;
		}
	}
	
	public IValue evalDeclaration(String src, ITree exp){
		
		//Declaration: variable    : Tags tags Visibility visibility Type type {Variable ","}+ variables ";" 
		declarations.add(src);
		return executeModule("\nvalue main(list[value] args) = true;\n");
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
				stdout.println(name + " set to "  + profile.getValue());
				break;
			case "trace": case "tracing": case "trackCalls":
				trackCalls = vf.bool(getBooleanValue(val));
				stdout.println(name + " set to "  + trackCalls.getValue());
			case "coverage":
				coverage = vf.bool(getBooleanValue(val));
				stdout.println(name + " set to "  + coverage.getValue());
				break;
			default:
				stdout.println("Unrecognized option : " + name);
			}
			break;
	
		case "help":
			stdout.println("Welcome to the compiler-based Rascal command shell.");
			stdout.println();
			stdout.println("Shell commands:");
			stdout.println(":help                      Prints this message");
			stdout.println(":quit or EOF               Quits the shell");
			stdout.println(":declarations              Lists all visible rules, functions and variables");
			//stdout.println(":set <option> <expression> Sets an option");
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
			stdout.println("Example rascal statements and declarations:");
			stdout.println("1 + 1;                     Expressions simply print their stdoutput and type");
			stdout.println("int a;                     Declarations allocate a name in the current scope");
			stdout.println("a = 1;                     Assignments store a value in a (optionally previously declared) variable");
			stdout.println("int a = 1;                 Declaration with initialization");
			stdout.println("import IO;                 Importing a module makes its public members available");
			stdout.println("println(\"Hello World\")     Function calling");
			stdout.println();
			stdout.println("Please read the manual for further information");
			break;
			
		case "listDeclarations":
			for(String decl : declarations){
				System.err.println(decl);
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
		// TODO Auto-generated method stub
		return null;
	}
}
