package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.rascalmpl.interpreter.utils.LimitedResultWriter.IOLimitReachedException;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NameCompleter;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help.HelpManager;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug.DebugREPLFrameObserver;
import org.rascalmpl.library.lang.rascal.boot.Kernel;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.repl.LimitedLineWriter;
import org.rascalmpl.repl.LimitedWriter;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.io.StandardTextWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class CommandExecutor {
	
	 private final static int LINE_LIMIT = 75;
	 private final static int CHAR_LIMIT = LINE_LIMIT * 20;
	
	private PathConfig pcfg;
	private PrintWriter stdout;
	private PrintWriter stderr;
	private final IValueFactory vf;
	private String consoleInputName = "ConsoleInput";
	public static String consoleInputPath = "/ConsoleInput.rsc";
	private ISourceLocation consoleInputLocation;
	private RVMExecutable rvmConsoleExecutable;
	private RVMExecutable lastRvmConsoleExecutable;
	
	private final Prelude prelude;
	
	boolean semiColonAdded = false;
	
	private DebugREPLFrameObserver debugObserver;
	
	private HelpManager helpManager;
	
	boolean debug;
	boolean debugRVM;
	boolean testsuite;
	boolean profile;
	boolean trace;
	boolean coverage;
	boolean jvm;
	boolean verbose;
	boolean enableAsserts;
	
	private HashMap<String, Variable> variables ;
	private ArrayList<String> imports;
	private HashMap<String,List<String>> syntaxDefinitions;
	private HashMap<String,List<String>> functionDeclarations;
	private HashMap<String,List<String>> dataDeclarations;
	
	private Map<IValue, IValue> moduleVariables;
	
	private final String shellModuleName = "CompiledRascalShell";
	
	private boolean forceRecompilation = true;
	private IMap moduleTags;
	private Kernel kernel;
	private StandardTextWriter indentedPrettyPrinter;
	private boolean optimize;
	
	public CommandExecutor(PathConfig pcfg, PrintWriter stdout, PrintWriter stderr) throws IOException, NoSuchRascalFunction, URISyntaxException {
		
		vf = ValueFactoryFactory.getValueFactory();
		prelude = new Prelude(vf);
		
		this.pcfg = pcfg.addSourceLoc(vf.sourceLocation("test-modules", "", ""));
		this.stdout = stdout;
		this.stderr = stderr; 
		
		consoleInputLocation = vf.sourceLocation("test-modules", "", consoleInputName + ".rsc");
		
		debug = false;							// options per executed command
		debugRVM = false;
		testsuite = false;
		profile = false;
		trace = false;
		coverage = false;
		jvm = true;
		verbose = false;
		optimize = false;
		enableAsserts = true;
		
		IMapWriter w = vf.mapWriter();
		//w.put(vf.string("bootstrapParser"), vf.string(""));
		IMap CompiledRascalShellModuleTags = w.done();
		
		w = vf.mapWriter();
		w.put(vf.string(shellModuleName), CompiledRascalShellModuleTags);
		w.put(vf.string(consoleInputName), CompiledRascalShellModuleTags);
		moduleTags = w.done();
		
		RascalExecutionContext rex = 
				RascalExecutionContextBuilder.normalContext(vf, pcfg.getboot(), this.stdout, this.stderr)
					.withModuleTags(moduleTags)
					.forModule(shellModuleName)
					.setJVM(true)					// options for complete repl
					//.setProfiling(true)
					.build();

		kernel = new Kernel(vf, rex);
		
		variables = new HashMap<>();
		imports = new ArrayList<>();
		syntaxDefinitions = new HashMap<>();
		functionDeclarations = new HashMap<>();
		dataDeclarations = new HashMap<>();
		moduleVariables = new HashMap<>();
		
		// helpManager is only initialized on first help or apropos call
		
		indentedPrettyPrinter = new StandardTextWriter(true);
        //singleLinePrettyPrinter = new StandardTextWriter(false);
         
		stderr.println("Type 'help' for information or 'quit' to leave");
	}
	
	public void reset(){
		variables = new HashMap<>();
		imports = new ArrayList<>();
		syntaxDefinitions = new HashMap<>();
		dataDeclarations = new HashMap<>();
		functionDeclarations = new HashMap<>();
		moduleVariables = new HashMap<>();
		forceRecompilation = true;
	}
	
	public void setDebugObserver(DebugREPLFrameObserver observer){
		this.debugObserver = observer;
	}
	
	private IMap makeCompileKwParamsAsIMap(){
		IMapWriter w = vf.mapWriter();
		w.put(vf.string("verbose"), vf.bool(false));
		w.put(vf.string("optimize"), vf.bool(optimize));
		w.put(vf.string("enableAsserts"), vf.bool(enableAsserts));
		return w.done();
	}
	
	private boolean noErrors(RVMExecutable exec){
		return exec.getErrors().size() == 0;
	}
	
	private String capitalize(String s){
	  return s.substring(0,1).toUpperCase() + s.substring(1);
	}
	
	private String getErrors(String modString, RVMExecutable exec){
      ISet errors = exec.getErrors();
      if(errors.size() == 0){
          return "";
      }
      StringWriter sw = new StringWriter();
      for(IValue m : errors){
          IConstructor msg = (IConstructor) m;
          String msgKind = msg.getName();
          String txt = ((IString)msg.get("msg")).getValue();
          ISourceLocation src = (ISourceLocation)msg.get("at");
          String hint = " AT " + src.toString();
          if(src.getPath().equals(consoleInputPath)){
              int offset = src.getOffset();
              String subarea = modString.substring(offset, offset + src.getLength());
              hint = subarea.matches("[a-zA-Z0-9]+") ? "" : " IN '" + subarea + "'";
          }
          sw.append(capitalize(msgKind)).append(": ").append(txt).append(hint).append("\n");
      }
      return sw.toString();
  }
	
	private void executeTests(String[] words){
	  IListWriter w = vf.listWriter();
	  if(words.length > 1){
	    for(int i = 1; i < words.length; i++){
	      String mname = words[i];
	      for(int j = 0; j < imports.size(); j++){ // Support abbreviations of already imported modules
	        if(imports.get(j).contains(mname)){
	          mname = imports.get(j);
	          break;
	        }
            w.append(vf.string(mname));
          }
	    }
	  } else {
	    if(imports.size() > 0){
	      for(int i = 0; i < imports.size(); i++){
	        w.append(vf.string(imports.get(i)));
	      }
	    } else {
	      stderr.println("No tests to execute; import modules with tests or give list of modules with tests");
	      return;
	    }
	  }
	  IValue res = kernel.rascalTests(w.done(), 
	      pcfg.getSrcs(), 
	      pcfg.getLibs(), 
	      pcfg.getboot(), 
	      pcfg.getBin(), 
	      true,
	      makeCompileKwParamsAsIMap());
	  stderr.println("executeTests: " + res);
	}
	
	private IValue executeModule(String main, boolean onlyMainChanged) throws RascalShellExecutionException {
		StringWriter w = new StringWriter();
		
		w.append("module ConsoleInput\n");
		for(String imp : imports){
			w.append("import ").append(imp).append(";\n");
		}
		for(String syn : syntaxDefinitions.keySet()){
		    for(String alt : syntaxDefinitions.get(syn)){
		      w.append(alt).append("\n");
		    }
		}
		for(String d : dataDeclarations.keySet()){
		  for(String alt : dataDeclarations.get(d)){
		    w.append(alt).append("\n");
		  }
		}
		
		for(String f : functionDeclarations.keySet()){
          for(String alt : functionDeclarations.get(f)){
            w.append(alt).append("\n");
          }
        }
		
		for(String name : variables.keySet()){
			Variable var = variables.get(name);
			w.append(var.type).append(" ").append(name).append(";\n");
		}
		w.append(main);
		String modString = w.toString();
		//System.err.println(modString);
		try {
			prelude.writeFile(consoleInputLocation, vf.list(vf.string(modString)));
			IBool reuseConfig = vf.bool(onlyMainChanged && !forceRecompilation);
			forceRecompilation = true;
			rvmConsoleExecutable = kernel.compileAndMergeIncremental(vf.string(consoleInputName), 
																	reuseConfig, 
																	pcfg.getSrcs(), 
																	pcfg.getLibs(), 
																	pcfg.getboot(), 
																	pcfg.getBin(), 
																	makeCompileKwParamsAsIMap());
			
			if(noErrors(rvmConsoleExecutable)){
				RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(vf, pcfg.getboot(), stdout, stderr)
						.forModule(shellModuleName)
						.withModuleTags(rvmConsoleExecutable.getModuleTags())
						.withModuleVariables(moduleVariables)
						.setDebug(debug)
						.setDebugRVM(debugRVM)
						.setTestsuite(testsuite)
						.setProfile(profile)
						.setTrace(trace)
						.setCoverage(coverage)
						.setJVM(jvm)
						.setVerbose(verbose)
						.observedBy(debugObserver != null ? debugObserver.getObserverWhenActiveBreakpoints() : null)
						.build();
						
				IValue val = ExecutionTools.executeProgram(rvmConsoleExecutable, vf.mapWriter().done(), rex);
				lastRvmConsoleExecutable = rvmConsoleExecutable;
				updateModuleVariables(rex.getModuleVariables());
				forceRecompilation = false;
				return val;
			} else {
				throw new RascalShellExecutionException(getErrors(modString, rvmConsoleExecutable));
			}
		} catch (Thrown e){
		    IConstructor cons = (IConstructor) e.value;
		    StringWriter sw = new StringWriter();
		    sw.append("Error: ").append(cons.toString());
		    e.printStackTrace(new PrintWriter(sw));
			throw new RascalShellExecutionException(sw.toString());
		} catch (IOException e){
		  throw new RascalShellExecutionException("Error: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
		} catch (Exception e){
		  throw new RascalShellExecutionException("Error: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
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
	
//	public String evalPrint(String statement, ISourceLocation rootLocation) throws IOException, ExecutionException{
//		IValue result = eval(statement, rootLocation);
//		return resultToString(result);
//	}
	
	public IValue eval(String statement, ISourceLocation rootLocation) throws RascalShellExecutionException, IOException {
		ITree cmd = parseCommand(statement, rootLocation);
		
		cmd = TreeAdapter.getStartTop(cmd);
		
		if(is(cmd, "expression")){
			return evalExpression(statement, get(cmd, "expression"));
		}
		if(is(cmd, "statement")){
			return evalStatement(statement, get(cmd, "statement"));
		}
		if(is(cmd, "import")){
//			try {
				return evalImport(statement, get(cmd, "imported"));
//			} catch (FactTypeUseException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
		}
		if(is(cmd, "declaration")){
			//try {
				return evalDeclaration(statement, get(cmd, "declaration"));
//			} catch (FactTypeUseException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		return null;
	}
	
	private IValue evalExpression(String src, ITree exp) throws RascalShellExecutionException{
		return executeModule("\nvalue main() = " + src + ";\n", true);
	}
	
	private void declareVar(String type, String name){
		variables.put(name,  new Variable(type, name));
	}
	
	private void updateVar(String name, IValue val){
		 moduleVariables.put(vf.string("ConsoleInput:" + name), val);
	}
	
	private void annotateVar(String name, String annoName, IValue annoVal){
		IValue iname = vf.string("ConsoleInput:" + name);
		IValue oldVal = moduleVariables.get(iname);
		if(oldVal != null){
			moduleVariables.put(iname, oldVal.asAnnotatable().setAnnotation(annoName, annoVal));
		}
	}
	
	private void undeclareVar(String name){
		variables.remove(name);
		moduleVariables.remove("ConsoleInput:" + name);
	}
	
	private void updateModuleVariables(Map<IValue,IValue> newModuleVariables){
		for(IValue ivar : moduleVariables.keySet()){
			String name = ((IString) ivar).getValue();
			IValue newVal = newModuleVariables.get("ConsoleInput:" + name);
			if(newVal != null){
				moduleVariables.put(ivar,  newVal);
			}
		}
	}
	
	private IValue report(String msg) {
	  stdout.println(msg);
      stdout.flush();
      return null;
	}
	
	private IValue reportError(String msg) throws RascalShellExecutionException {
	  throw new RascalShellExecutionException("Error: " + msg);
	}

	private String getBaseVar(ITree assignable) throws FactTypeUseException, IOException{
		if(is(assignable, "variable")){
			return unparse(get(assignable, "qualifiedName"));
		}
		if(has(assignable, "receiver")){
			return getBaseVar(get(assignable, "receiver"));
		}
		return null;
	}
	
	private String makeMain(String stat){
	  return "\nvalue main() { try { return " + stat + "} catch e: return e;}\n";
	}
	
	private String makeMainOk(){
	  return "\nvalue main() = \"ok\";\n";
	}
	
	private IValue evalStatement(String src, ITree stat) throws IOException, RascalShellExecutionException {
		
		String consName = TreeAdapter.getConstructorName(stat);
		switch(consName){
		
		case "expression":
			String innerExp = unparse(get(stat, "expression"));
			return executeModule(makeMain(innerExp + ";"), true);
			
		case "assignment":
			//assignment: Assignable assignable Assignment operator Statement!functionDeclaration!variableDeclaration statement

			ITree assignable = get(stat, "assignable");
			String assignableName = TreeAdapter.getConstructorName(assignable);
			IValue val;

        switch(assignableName){
			case "variable": case "subscript": case "slice": case "sliceStep": case "fieldAccess": {
				String name = getBaseVar(assignable);

				Variable var = variables.get(name);
				if(var != null){
					val = executeModule("\nvalue main() { " + src + "}\n", true);
					updateVar(name, val);
					return val;
				} else {
					val = executeModule(makeMain(unparse(get(stat, "statement"))), true);
					declareVar(val.getType().toString(), name);
					updateVar(name, val);
					forceRecompilation = true;
					return val;
				}
			}
			case "annotation": {
				String annoName = unparse(get(assignable, "annotation"));
				String name = getBaseVar(assignable);
				Variable var = variables.get(name);
				val = executeModule(makeMain(unparse(get(stat, "statement"))), true);
				if(var != null){
				  annotateVar(name, annoName, val);
				}
				return val;
			}
				
			case "ifDefinedOrDefault": {
				String name = getBaseVar(assignable);
				Variable var = variables.get(name);
				val = executeModule(makeMain(src), true);
				if(var == null){
				  declareVar(val.getType().toString(), name);
				  updateVar(name, val);
				}
				return val;
			}
				
			case "tuple": {
				ITree elements = get(assignable, "elements");
				val = executeModule(makeMain(unparse(get(stat, "statement"))), true);

				ITuple tupleVal = (ITuple) val;
				IList elemList = TreeAdapter.getListASTArgs(elements);
				for(int i = 0; i < elemList.length(); i++){
				  ITree element = (ITree) elemList.get(i);
				  String elemName = getBaseVar((ITree)element);
				  if(elemName != null){
				    declareVar(val.getType().getFieldType(i).toString(), elemName);
				    updateVar(elemName, tupleVal.get(i));
				    forceRecompilation = true;
				  } else {
				    return reportError("Assignable is not supported: " + unparse((ITree)element));
				  }
				}
				return val;
			}
				
			case "constructor": {
				consName = unparse(get(assignable, "name"));
				ITree elements = get(assignable, "arguments");
				val = executeModule(makeMain(unparse(get(stat, "statement"))), true);
				if(val != null){
					IConstructor consVal = (IConstructor) val;
					if(consVal.getName().equals(consName)){
						IList elemList = TreeAdapter.getListASTArgs(elements);
						for(int i = 0; i < elemList.length(); i++){
							ITree element = (ITree) elemList.get(i);
							String elemName = getBaseVar((ITree)element);
							if(elemName != null){
								declareVar(consVal.getConstructorType().getFieldType(i).toString(), elemName);
								updateVar(elemName, consVal.get(i));
							} else {
								return reportError("Assignable is not supported: " + unparse((ITree)element));
							}
						}
					} else {
						return reportError("Name mismatch in assignment: " + consName  + " vs " + consVal.getName());
					}
				}
				forceRecompilation = true;
				return val;
			}
			
			default:
				return reportError("Assignable is not supported: " + src);
			}

		default:
			return executeModule(makeMain(unparse(stat)), true);
		}
	}
	
	private IValue evalDeclaration(String src, ITree cmd) throws FactTypeUseException, IOException, RascalShellExecutionException{
		IValue result = null;
		if(is(cmd, "variable")){
			ITree type = get(cmd, "type");
			ITree variables = get(cmd, "variables");
			IList vars = TreeAdapter.getListASTArgs(variables);

			for(IValue ivar : vars){
			  ITree var = (ITree) ivar;
			  String name = unparse(get(var, "name"));
			  if(is(var, "initialized")){
			    String initial = unparse(get(var, "initial"));
			    declareVar(unparse(type), name);
			    try {
			      forceRecompilation = true;
			      result = executeModule(makeMain(name + " = " + initial + ";"), false);
			      updateVar(name, result);
			    } catch (RascalShellExecutionException e){
			      undeclareVar(name);
			      return null;
			    }
			  } else {
			    declareVar(unparse(type), name);
			    try {
			      forceRecompilation = true;
			      result = executeModule(makeMain("true;"), false);
			    } catch (RascalShellExecutionException e){
			      undeclareVar(name);
			    }
			    return null;
			  }
			}
			return result;
		} else if(is(cmd, "dataAbstract") || is(cmd, "data")){
		  ITree userType = get(cmd, "user");
		  String name = unparse(get(userType, "name"));
		  List<String> alts = dataDeclarations.containsKey(name) 
		                        ? dataDeclarations.get(name)
		                        : new ArrayList<String>();
		  alts.add(src);
		  dataDeclarations.put(name,  alts);
		  try {
		    result = executeModule(makeMain("true;"), false);
		    return null;
		  } catch (RascalShellExecutionException e) {
		    alts.remove(src);
		    dataDeclarations.put(name,  alts);
		    throw e;
		  }
		} else if(is(cmd, "function")){
		  ITree functionDeclaration = get(cmd, "functionDeclaration");
		  ITree signature = get(functionDeclaration, "signature");
		  String name = unparse(get(signature, "name"));
		  List<String> alts = functionDeclarations.containsKey(name) 
		      ? functionDeclarations.get(name)
		          : new ArrayList<String>();
		      alts.add(src);
		      functionDeclarations.put(name,  alts);
		      try {
		        result = executeModule(makeMain("true;"), false);
		        return null;
		      } catch (RascalShellExecutionException e) {
		        alts.remove(src);
		        functionDeclarations.put(name,  alts);
		        throw e;
		      }
		}
		return reportError("Not supported construct: " + src);
	}
	
	private IValue evalImport(String src, ITree imp) throws FactTypeUseException, IOException, RascalShellExecutionException{
		IValue result;
		if(is(imp, "default")){
			String impName = unparse(get(get(imp, "module"), "name"));
			if(imports.contains(impName)){
				return null;
			}
			imports.add(impName);
			try {
				forceRecompilation = true;
				result = executeModule(makeMainOk(), false);
				return null;
			} catch (RascalShellExecutionException e){
				imports.remove(impName);
				throw e;
			}
		}
		if(is(imp, "syntax")){
			StringWriter w = new StringWriter();
			TreeAdapter.unparse(get(get(imp, "syntax"), "defined"), w);
			String name = w.toString();
			List<String> alts = syntaxDefinitions.containsKey(name) 
			                          ? syntaxDefinitions.get(name)
			                          : new ArrayList<String>();
			alts.add(src);
            syntaxDefinitions.put(name, alts);
			try {
				result = executeModule(makeMainOk(), false);
				return null;
			} catch (RascalShellExecutionException e){
			    alts.remove(src);
				syntaxDefinitions.put(name, alts);
				throw e;
			}
		}
		
		return null;
	}
	
	private boolean getBooleanValue(String val) throws RascalShellExecutionException{
		switch(val){
		case "true": return true;
		case "false": return false;
		default:
			throw new RascalShellExecutionException("Error: '" + val + "' is not a boolean value");
		}
	}
	
	private String unparse(ITree tree) throws FactTypeUseException, IOException{
		StringWriter w = new StringWriter();
		TreeAdapter.unparse(tree, w);
		return w.toString();
	}
	
	//TODO merge with BaseRascalRepl
	
	public String resultToString(IValue value) throws IOException {
		StringWriter out = new StringWriter();
		if (value == null) {
			out.append("ok\n");
			return out.toString();
		}
		Type type = value.getType();

		if (type.isAbstractData() && type.isStrictSubtypeOf(RascalValueFactory.Tree)) {
			out.append(type.toString());
			out.append(": ");
			// we unparse the tree
			out.append("(" + type.toString() +") `");
			TreeAdapter.yield((IConstructor)value, false, out);
			out.append("`");
		} else if(type.isAbstractData() && type.getName().equals("RuntimeException")){
		    out.append("Error: ");
		    out.append(value.toString());
		} else {
			out.append(type.toString());
			out.append(": ");
			 try (Writer wrt = new LimitedWriter(new LimitedLineWriter(out, LINE_LIMIT), CHAR_LIMIT)) {
	                indentedPrettyPrinter.write(value, wrt);
	            }
	            catch (IOLimitReachedException e) {
	                // ignore since this is what we wanted
	            }
		}
		out.append("\n");
		return out.toString();
	}

	private IValue showOptions(){
		StringBuilder sb = new StringBuilder();
		return report(
				sb.append("profile:  ").append(profile).append("\n")
				  .append("trace:    ").append(trace).append("\n")
				  .append("coverage: ").append(coverage).toString());
	}
	
	public IValue evalShellCommand(String[] words) throws RascalShellExecutionException {
		switch(words[0]){
		
		case "set":
			if(words.length == 1){
				return showOptions();
			}
			if(words.length != 3){
				return reportError("set requires two arguments");
			}
			String name = words[1];
			String val = words[2];
			
			switch(name){
			case "profile":
				profile =  getBooleanValue(val);
				if(profile && (trace || coverage)){
					trace = coverage = false;
					return showOptions();
				}
				return report(name + " set to "  + profile);
				
			case "trace":
				trace = getBooleanValue(val);
				if(trace && (profile || coverage)){
					profile = coverage = false;
					return showOptions();
				}
				return report(name + " set to "  + trace);
				
			case "coverage":
				coverage = getBooleanValue(val);
				if(coverage && (profile || trace)){
					profile = trace = false;
					return showOptions();
				}
				return report(name + " set to "  + coverage);
			case "optimize":
				optimize = getBooleanValue(val);
				return report(name + " set to "  + optimize);
								
			default:
				return reportError("Unrecognized option: " + name);
			}
	
		case "help": case "apropos":
			if(helpManager == null){
				helpManager = new HelpManager(pcfg.getboot(), stdout, stderr);
			}
			
			helpManager.handleHelp(words);
			break;
			
		case "declarations":
		  if(syntaxDefinitions.isEmpty() 
		      && dataDeclarations.isEmpty() 
		      &&  functionDeclarations.isEmpty() 
		      && variables.isEmpty()){
		    stderr.println("No declarations");
		  } else {
		    for(String synName : syntaxDefinitions.keySet()){
		      for(String alt : syntaxDefinitions.get(synName)){
		        stderr.println(alt);
		      }
		    }
		    for(String dataName : dataDeclarations.keySet()){
		      for(String alt : dataDeclarations.get(dataName)){
		        stderr.println(alt);
		      }
		    }
		    for(String funName : functionDeclarations.keySet()){
		      for(String alt : functionDeclarations.get(funName)){
		        stderr.println(alt);
		      }
		    }

		    for(String varName : variables.keySet()){
		      Variable var = variables.get(varName);
		      stderr.println(var.type + " " + varName + ";");
		    }
		  }

		  break;
			
		case "modules":
			if(imports.isEmpty()){
				stderr.println("No imported modules");
			} else {
				for(String imp : imports){
					stderr.println(imp);
				}
			}
			break;
			
		case "unimport":
			if(words.length > 1){
				for(int i = 1; i <words.length; i++){
					imports.remove(words[i]);
				}
			} else {
				imports = new ArrayList<String>();
			}
			break;
			
		case "undeclare":
			if(words.length > 1){
				for(int i = 1; i <words.length; i++){
					undeclareVar(words[i]);
					syntaxDefinitions.remove(words[i]);
					functionDeclarations.remove(words[i]);
					dataDeclarations.remove(words[i]);
				}
			} else {
				variables =  new HashMap<>();
				syntaxDefinitions = new HashMap<>();
				functionDeclarations = new HashMap<>();
				dataDeclarations = new HashMap<>();
				}
			break;
		
		case "break":
			debugObserver.getBreakPointManager().breakDirective(words);
			break;
		
		case "clear":
			debugObserver.getBreakPointManager().clearDirective(words);
			break;
		
		case "ignore":
			debugObserver.getBreakPointManager().ignoreDirective(words);
			break;
			
		case "enable":
			debugObserver.getBreakPointManager().enableDirective(words);
			break;
		
		case "disable":
			debugObserver.getBreakPointManager().disableDirective(words);
			break;
			
		case "test":
		  executeTests(words);
		  break;
		}
		stdout.flush();
		return null;
	}	
	
	private boolean endsNonEmpty(ITree stat, String consName, String partName){
		return is(stat, consName) && !is(get(stat, partName), "emptyStatement");
	}
	
	private boolean mayComplete(ITree tree){
		ITree cmd = TreeAdapter.getStartTop(tree);
		if(is(cmd, "statement")){
			ITree stat = get(cmd, "statement");
			return endsNonEmpty(stat, "ifThen", "thenStatement")
				   || endsNonEmpty(stat, "ifThenElse", "elseStatement") 
				   || endsNonEmpty(stat, "while", "body") 
				   || endsNonEmpty(stat, "solve", "body") 
				   || endsNonEmpty(stat, "assignment", "statement") 
				   || endsNonEmpty(stat, "for", "body") 
				   || endsNonEmpty(stat, "tryFinally", "finallyBody");
		} else if(is(cmd, "import")){
			return true;
		}
		return false;
	}
	
	  public boolean isStatementComplete(String command) {
		  
		  String[] words = command.split(" ");
		  if(words.length > 0 && CompiledRascalREPL.SHELL_VERBS.contains(words[0])){
			  return true;
		  }
		  ITree parseResult = null;
		  try {
			  parseResult = parseCommand(command, URIUtil.rootLocation("prompt"));
		  }
		  catch (ParseError pe) {
			  String[] commandLines = command.split("\n");
			  int lastLine = commandLines.length;
			  int lastColumn = commandLines[lastLine - 1].length();

			  if (pe.getEndLine() + 1 == lastLine && lastColumn < pe.getEndColumn()) { 
				  semiColonAdded = false;
				  return false;
			  }
			  if (lastLine == 1 && !semiColonAdded && pe.getEndLine() + 1 == lastLine && lastColumn == pe.getEndColumn()) { 
				  semiColonAdded = true;
				  boolean isComplete = isStatementComplete(command + ";");
				  semiColonAdded &= isComplete;
				  return isComplete;
			  }
			  return false;
		  }
		  return !semiColonAdded || mayComplete(parseResult);
	  }
	
	public ITree parseCommand(String command, ISourceLocation location) {
		//__setInterrupt(false);
		IActionExecutor<ITree> actionExecutor =  new NoActionExecutor();
		ITree tree =  new RascalParser().parse(Parser.START_COMMAND, location.getURI(), command.toCharArray(), actionExecutor, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));

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
	
	public Collection<String> completeDeclaredIdentifier(String term) {
		TreeSet<String> result = null;
		for(String var : variables.keySet()){
			if(var.startsWith(term)){
				if(result == null){
			    	 result = new TreeSet<String>();
			     }
				result.add(var);
			}
		}
		return result;
	}
	
	public Collection<String> completeImportedIdentifier(String term) {
		TreeSet<String> result = null;
		for(String mod : imports){
			if(mod.startsWith(term)){
				if(result == null){
			    	 result = new TreeSet<String>();
			     }
				result.add(mod);
			}
		}
		return result;
	}
}

class Variable {
	String name;
	String type;
	
	Variable(String type, String name){
		this.name = name;
		this.type = type;
	}
}
