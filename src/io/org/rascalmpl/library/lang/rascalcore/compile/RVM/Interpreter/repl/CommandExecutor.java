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
import java.util.SortedSet;
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
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.Java2Rascal;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.library.lang.rascal.boot.IKernel;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.repl.CompletionResult;
import org.rascalmpl.repl.LimitedLineWriter;
import org.rascalmpl.repl.LimitedWriter;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;

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
	
	private SortedSet<String> vocabulary;
	
	private final Prelude prelude;
	
	boolean semiColonAdded = false;
	
	private final IFrameObserver debugObserver;
	
	private IDEServices ideServices;
	protected final CompiledRascalREPL repl;
	private HelpManager helpManager;
	
	private HashMap<String, Variable> variables ;
	private ArrayList<String> imports;
	private HashMap<String,List<String>> syntaxDefinitions;
	private HashMap<String,List<String>> functionDeclarations;
	private HashMap<String,List<String>> dataDeclarations;
	
	private Map<IValue, IValue> moduleVariables;
	
	private final String shellModuleName = "CompiledRascalShell";
	
	private boolean forceRecompilation = true;
	private IKernel kernel;
	private StandardTextWriter indentedPrettyPrinter;

	private Settings settings;
	
	private boolean kernel_coverage;
	private boolean kernel_debug;
	private boolean kernel_profile;
	private boolean kernel_trace;
	private boolean kernel_verbose;

	private boolean compile_coverage;
	private boolean compile_debug;
	private boolean compile_enableAsserts;
	private boolean compile_optimize;
	private boolean compile_profile;
	private boolean compile_testsuite;
	private boolean compile_trace;
	private boolean compile_verbose;
	
	private boolean execute_coverage;
	private boolean execute_debug;
    private boolean execute_debugRVM;
    private boolean execute_jvm;
    private boolean execute_profile;
    private boolean execute_testsuite;
    private boolean execute_trace;
    private boolean execute_verbose;
    
    private boolean repl_verbose;
 	
	public CommandExecutor(PathConfig pcfg, PrintWriter stdout, PrintWriter stderr, IDEServices ideServices, CompiledRascalREPL repl, IFrameObserver observer) throws IOException, NoSuchRascalFunction, URISyntaxException {
		
		vf = ValueFactoryFactory.getValueFactory();
		prelude = new Prelude(vf);
		
		settings = new Settings(false);   // TODO: definition solution when we introduce generic Config files
	
		this.pcfg = settings.getPathConfig(pcfg).addSourceLoc(vf.sourceLocation("test-modules", "", ""));
		
		this.stdout = stdout;
		this.stderr = stderr; 
		this.ideServices = ideServices;
		this.repl = repl;
		this.debugObserver = observer;
		
		consoleInputLocation = vf.sourceLocation("test-modules", "", consoleInputName + ".rsc");
		// options for the kernel
		
		kernel_coverage      = settings.getBool("kernel.coverage", false);
		kernel_debug         = settings.getBool("kernel.debug", false);                            
        kernel_profile       = settings.getBool("kernel.profile", false);
        kernel_trace         = settings.getBool("kernel.trace", false);
        kernel_verbose       = settings.getBool("kernel.verbose", false);
        
        // options for compiler
        
        compile_coverage      = settings.getBool("compile.coverage", false);
        compile_debug         = settings.getBool("compile.debug", false);     
        compile_enableAsserts = settings.getBool("compile.enableAsserts", true);
        compile_optimize      = settings.getBool("compile.optimize", false);
        compile_profile       = settings.getBool("compile.profile", false);
        compile_testsuite     = settings.getBool("compile.testsuite", false);
        compile_trace         = settings.getBool("compile.trace", false);
        compile_verbose       = settings.getBool("compile.verbose", false);
       
        // options per executed command
        
        execute_coverage      = settings.getBool("execute.coverage", false);
		execute_debug         = settings.getBool("execute.debug", false);							
		execute_debugRVM      = settings.getBool("execute.debugRVM", false);
		execute_jvm           = settings.getBool("execute.jvm", true);
		execute_profile       = settings.getBool("execute.profile", false);
		execute_testsuite     = settings.getBool("execute.testsuite", false);
		execute_trace         = settings.getBool("execute.trace", false);
		execute_verbose       = settings.getBool("execute.verbose", false);
		
		// options for the repl itself
		
		repl_verbose          = settings.getBool("repl.verbose", false);
		
		kernel = Java2Rascal.Builder.bridge(vf, this.pcfg, IKernel.class)
		        .coverage(kernel_coverage)
		        .debug(kernel_debug)
		        .profile(kernel_profile)
		        .trace(kernel_trace)
		        .verbose(kernel_verbose)
		        .build();
		
		startupMessage(this.pcfg);
		
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
	
	private void startupMessage(PathConfig pcfg){
	  if(repl_verbose){
	    stderr.println(pcfg);
	  }
	}
	
	public void reset(){
		variables = new HashMap<>();
		imports = new ArrayList<>();
		syntaxDefinitions = new HashMap<>();
		functionDeclarations = new HashMap<>();
		dataDeclarations = new HashMap<>();
		moduleVariables = new HashMap<>();
		forceRecompilation = true;
		vocabulary = null;
	}
	
	public void shutdown(){
	  kernel.shutdown();
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
          String origin = " AT " + src.toString();
          if(src.getPath().equals(consoleInputPath)){
              int offset = src.getOffset();
              String subarea = modString.substring(offset, offset + src.getLength());
              origin = subarea.matches("[a-zA-Z0-9]+") ? "" : " IN '" + subarea + "'";
          } else if(txt.contains("Cannot import")){
              origin = "";
          }
          sw.append(capitalize(msgKind)).append(": ").append(txt).append(origin).append("\n");
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
	      }
          w.append(vf.string(mname));
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
	  IValue res = kernel.rascalTests(w.done(), pcfg.asConstructor(kernel),
	                                  kernel.kw_rascalTests()
	                                  .verbose(execute_verbose)
	                                  .jvm(true)
	                                  .recompile(true)
	      );
	  
	  stderr.println(((IString) ((ITuple) res).get(1)).getValue());
	}
	
	public IConstructor executeTestsRaw(String mname){
	  return kernel.rascalTestsRaw(vf.list(vf.string(mname)), pcfg.asConstructor(kernel),
	                               kernel.kw_rascalTests()
	                               .verbose(execute_verbose)
	                               .jvm(true)
	                               .recompile(true)
	      );
	}
	
	private void moduleDeclarations(StringWriter w){
	 
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
	}
	
	private IValue executeModule(String main, boolean onlyMainChanged) throws RascalShellExecutionException {
		StringWriter w = new StringWriter();
		
		w.append("module ConsoleInput\n");
		moduleDeclarations(w);
		w.append(main);
		String modString = w.toString();
		//stderr.println(modString);
		try {
			prelude.writeFile(consoleInputLocation, vf.list(vf.string(modString)));
			IBool reuseConfig = vf.bool(onlyMainChanged && !forceRecompilation /* && !anyFilesChanged()*/);
			forceRecompilation = true;
			IConstructor rvmProgram = kernel.compileAndMergeProgramIncremental(vf.string(consoleInputName), 
																	reuseConfig, 
																	pcfg.asConstructor(kernel),
																	kernel.kw_compileAndMergeProgramIncremental()
																	  .optimize(compile_optimize)
																	  .verbose(compile_verbose)
																	  .jvm(true)
																	);
			rvmConsoleExecutable = ExecutionTools.link(rvmProgram,vf.bool(true), vf.mapWriter().done());

			if(noErrors(rvmConsoleExecutable)){
				RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(pcfg, stdout, stderr)
						.forModule(shellModuleName)
						.withModuleTags(rvmConsoleExecutable.getModuleTags())
						.withModuleVariables(moduleVariables)
						.debug(execute_debug)
						.debugRVM(execute_debugRVM)
						.testsuite(execute_testsuite)
						.profile(execute_profile)
						.trace(execute_trace)
						.coverage(execute_coverage)
						.jvm(true)
						.verbose(execute_verbose)
						.observedBy(debugObserver != null ? (kernel_debug ? debugObserver : debugObserver.getObserverWhenActiveBreakpoints()) : null)
						.build();
						
				IValue val = ExecutionTools.executeProgram(rvmConsoleExecutable, new HashMap<String,IValue>(), rex);
				updateModuleVariables(rex.getModuleVariables());
				forceRecompilation = false;
				vocabulary = null;
				return val;
			} else {
				stderr.println(getErrors(modString, rvmConsoleExecutable));
				return null;
				
			}
		} catch (Thrown e){
		    if(e.getFrame() != null){
		        debugObserver.exception(e.getFrame(), e);
		    } else {
		        // this should never happen, but if it does it would be nice to know:
		        stderr.println("Internal warning: exception thrown from null frame: " + e);
		    }
		    
		    throw e;
		}
		catch (Throwable e) {
		    throw new RascalShellExecutionException("An unexpected internal error occurred", e);
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
	
	public IValue eval(String statement, ISourceLocation rootLocation) throws RascalShellExecutionException {
		ITree cmd = parseCommand(statement, rootLocation);
		
		cmd = TreeAdapter.getStartTop(cmd);
		
		if(is(cmd, "expression")){
			return evalExpression(statement, get(cmd, "expression"));
		}
		if(is(cmd, "statement")){
			return evalStatement(statement, get(cmd, "statement"));
		}
		if(is(cmd, "import")){
		    return evalImport(statement, get(cmd, "imported"));
		}
		if(is(cmd, "declaration")){
		    return evalDeclaration(statement, get(cmd, "declaration"));
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
	
	private boolean undeclareVar(String name){
		return !(variables.remove(name) == null && moduleVariables.remove(vf.string("ConsoleInput:" + name)) == null);
	}
	
	private void updateModuleVariables(Map<IValue,IValue> newModuleVariables){
		for(IValue ivar : moduleVariables.keySet()){
			String name = ((IString) ivar).getValue();
			IValue newVal = newModuleVariables.get(vf.string("ConsoleInput:" + name));
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

	private String getBaseVar(ITree assignable) throws FactTypeUseException, RascalShellExecutionException {
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
	
	private IValue evalStatement(String src, ITree stat) throws RascalShellExecutionException {
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
					if(val != null){
					    updateVar(name, val);
					}
					return val;
				} else {
					val = executeModule(makeMain(unparse(get(stat, "statement"))), true);
					if(val != null){
					    declareVar(val.getType().toString(), name);
					    updateVar(name, val);
					}
					forceRecompilation = true;
					return val;
				}
			}
			case "annotation":  {
				String annoName = unparse(get(assignable, "annotation"));
				String name = getBaseVar(assignable);
				Variable var = variables.get(name);
				val = executeModule(makeMain(unparse(get(stat, "statement"))), true);
				if(var != null && val != null){
				  annotateVar(name, annoName, val);
				}
				return val;
			}
			case "ifDefinedOrDefault": {
				String name = getBaseVar(assignable);
				Variable var = variables.get(name);
				val = executeModule(makeMain(src), true);
				if(var == null && val != null){
				  declareVar(val.getType().toString(), name);
				  updateVar(name, val);
				}
				return val;
			}
			
			case "tuple": {
				ITree elements = get(assignable, "elements");
				val = executeModule(makeMain(unparse(get(stat, "statement"))), true);
				if(val != null){
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
	
	private IValue evalDeclaration(String src, ITree cmd) throws FactTypeUseException, RascalShellExecutionException{
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
			      if(result != null){
			          updateVar(name, result);
			      } else {
			          undeclareVar(name);
			      }
			    } catch (RascalShellExecutionException e){
			      undeclareVar(name);
			      return null;
			    }
			  } else {
			    declareVar(unparse(type), name);
			    try {
			      forceRecompilation = true;
			      result = executeModule(makeMain("true;"), false);
			      if(result == null){
			          undeclareVar(name);
			      }
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
		    if(result == null){
		        alts.remove(src);
	            dataDeclarations.put(name,  alts); 
		    }
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
		        if(result == null){
		            alts.remove(src);
	                functionDeclarations.put(name,  alts); 
		        }
		        return null;
		      } catch (RascalShellExecutionException e) {
		        alts.remove(src);
		        functionDeclarations.put(name,  alts);
		        throw e;
		      }
		}
		return reportError("Not supported construct: " + src);
	}
	
	private IValue evalImport(String src, ITree imp) throws FactTypeUseException, RascalShellExecutionException{
		IValue result;
		if(is(imp, "default")){
			String impName = unparse(get(get(imp, "module"), "name"));
			if(!imports.contains(impName)){
			  imports.add(impName);
			}
			try {
				forceRecompilation = true;
				result = executeModule(makeMainOk(), false);
				if(result == null){
				    imports.remove(impName); 
				}
				return null;
			} catch (RascalShellExecutionException e){
				imports.remove(impName);
				throw e;
			}
		}
		if(is(imp, "syntax")){
			String name = unparse(get(get(imp, "syntax"), "defined"));
			List<String> alts = syntaxDefinitions.containsKey(name) 
			                          ? syntaxDefinitions.get(name)
			                          : new ArrayList<String>();
			alts.add(src);
            syntaxDefinitions.put(name, alts);
			try {
				result = executeModule(makeMainOk(), false);
				if(result ==null){
				    alts.remove(src);
				}
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
	
	private String unparse(ITree tree) throws FactTypeUseException, RascalShellExecutionException {
		try {
		    StringWriter w = new StringWriter();
		    TreeAdapter.unparse(tree, w);

		    return w.toString();
		} 
		catch (IOException e) {
            throw new RascalShellExecutionException("command", e);
        }
	}
	
	public String resultToString(IValue value) throws RascalShellExecutionException {
	    try {
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
	    catch (IOException e) {
	        // this can not happen
	        throw new RascalShellExecutionException("toString", e);
	    }
	}

	private IValue showOptions(){
		StringBuilder sb = new StringBuilder();
		return report(
				sb.append("profile:  ").append(execute_profile).append("\n")
				  .append("trace:    ").append(execute_trace).append("\n")
				  .append("coverage: ").append(execute_coverage).append("\n")
				  .toString()
				  );
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
				execute_profile =  getBooleanValue(val);
				if(execute_profile && (execute_trace || execute_coverage)){
					execute_trace = execute_coverage = false;
					return showOptions();
				}
				return report(name + " set to "  + execute_profile);
				
			case "trace":
				execute_trace = getBooleanValue(val);
				if(execute_trace && (execute_profile || execute_coverage)){
					execute_profile = execute_coverage = false;
					return showOptions();
				}
				return report(name + " set to "  + execute_trace);
				
			case "coverage":
				execute_coverage = getBooleanValue(val);
				if(execute_coverage && (execute_profile || execute_trace)){
					execute_profile = execute_trace = false;
					return showOptions();
				}
				return report(name + " set to "  + execute_coverage);
								
			default:
				return reportError("Unrecognized option: " + name);
			}
	
		case "help": case "apropos":
		    try {
		        if(helpManager == null){
		            helpManager = new HelpManager(pcfg, stdout, stderr, ideServices);
		        }

		        helpManager.handleHelp(words);
		    }
		    catch (IOException e) {
		        throw new RascalShellExecutionException("Can not provide help at the moment", e);
		    }
			break;
			
		case "edit":
		  stderr.println("edit: " + words[1]);
		  ISourceLocation loc = pcfg.resolveModule(words[1]);
		  if (loc != null) {
		      stderr.println("loc: " + loc);
		      ideServices.edit(loc);
		  }
		  else {
		      stderr.println(words[1] + " could not be found.");
		  }
		  break;
		  
		case "declarations":
		  if(syntaxDefinitions.isEmpty() 
		      && dataDeclarations.isEmpty() 
		      && functionDeclarations.isEmpty() 
		      && variables.isEmpty()
		      && imports.isEmpty()){
		    stderr.println("No declarations");
		  } else {
		    
		    StringWriter w = new StringWriter();
		    moduleDeclarations(w);
		    stderr.println(w.toString());
		  }
		  break;
			
		case "unimport":
			if(words.length > 1){
				for(int i = 1; i <words.length; i++){
					if(imports.remove(words[i])){
					    stderr.println("Import " + words[i] + " removed");
					} else {
					    stderr.println("No import " + words[i] + " found");
					}
				}
			} else {
				imports = new ArrayList<String>();
				stderr.println("All imports removed");
			}
			executeModule(makeMain("true;"), false);
			break;
			
		case "undeclare":
			if(words.length > 1){
				for(int i = 1; i <words.length; i++){
					if(!undeclareVar(words[i]) ||
					     syntaxDefinitions.remove(words[i]) == null ||
					     functionDeclarations.remove(words[i]) == null ||
					     dataDeclarations.remove(words[i]) == null){
					  stderr.println("No declaration for  " + words[i] + " found");
					} else {
					  stderr.println("Declaration for  " + words[i] + " removed");
					}
				}
			} else {
				variables =  new HashMap<>();
				syntaxDefinitions = new HashMap<>();
				functionDeclarations = new HashMap<>();
				dataDeclarations = new HashMap<>();
				stderr.println("All declarations removed");
				}
			executeModule(makeMain("true;"), false);
			break;
			
		case "clean":
		    cleanProject();
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
	
	SortedSet<String> getVocabulary(){
	  if(vocabulary != null){
	    return vocabulary;
	  }
	  ISet names = kernel.getIncrementalVocabulary();
	  vocabulary = new TreeSet<>();
	  for(IValue iname : names){
	    vocabulary.add(((IString) iname).getValue());
	  }
	  return vocabulary;
	}
	
	public Collection<String> completePartialIdentifier(String qualifier, String term) {
	  getVocabulary();
      return completePartialIdentifier(new NameCompleter(), term).getResult();
    }
	
	private NameCompleter completePartialIdentifier(NameCompleter completer, String partialIdentifier) {
      if (partialIdentifier == null || partialIdentifier.isEmpty()) {
          throw new IllegalArgumentException("The behavior with empty string is undefined.");
      }
      if (partialIdentifier.startsWith("\\")) {
          partialIdentifier = partialIdentifier.substring(1);
      }
      
      for(String completeName : vocabulary.tailSet(partialIdentifier)){
        completer.add(completeName, partialIdentifier);
      }

      return completer;
  }
	
	public Collection<String> completeDeclaredIdentifier(String term) {
	  TreeSet<String> result = new TreeSet<String>();
	  
	  for(String var : variables.keySet()){
	    if(var.startsWith(term)){
	      result.add(var);
	    }
	  }
	  for(String var : syntaxDefinitions.keySet()){
	    if(var.startsWith(term)){
	      result.add(var);
	    }
	  }
	  for(String var : functionDeclarations.keySet()){
	    if(var.startsWith(term)){
	      result.add(var);
	    }  
	  }

	  for(String var : dataDeclarations.keySet()){
	    if(var.startsWith(term)){
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
	
	public CompletionResult completeModule(String line, int cursor){
	  return repl.completeModule(line, cursor);
	}
	
	private void cleanProject() {
	    try {
	        // remove everything in the bin folder (recursively), but leave the itself folder as-is:
	        for (ISourceLocation entry : URIResolverRegistry.getInstance().list(pcfg.getBin())) {
	            prelude.remove(entry);
	        }
	    } catch (IOException e){
	        stderr.println("Could not clean project: " + e);
	    }
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
