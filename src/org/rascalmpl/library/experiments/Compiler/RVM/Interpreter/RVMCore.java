package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.regex.Matcher;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.util.MemoizationCache;
import org.rascalmpl.interpreter.types.DefaultRascalTypeVisitor;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.RascalFunctionInvocationHandler;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.NullFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.DescendantDescriptor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.DIRECTION;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.FIXEDPOINT;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.PROGRESS;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.REBUILD;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.INumber;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

public abstract class RVMCore {
	public final IValueFactory vf;

	protected final TypeFactory tf; 
	
	protected final static IBool Rascal_TRUE = ValueFactoryFactory.getValueFactory().bool(true);
	protected final static IBool Rascal_FALSE = ValueFactoryFactory.getValueFactory().bool(false);
	protected final IString NOVALUE; 
	
	protected TypeStore typeStore;
	private IMap symbol_definitions;
	
	protected Function[] functionStore;
	protected Map<String, Integer> functionMap;

	protected List<Type> constructorStore;
	protected final Map<String, Integer> constructorMap;
	
	// Function overloading
	protected final Map<String, Integer> resolver;
	protected final OverloadedFunction[] overloadedStore;
	private Map<String, OverloadedFunction> overloadedStoreMap;
	
	protected IFrameObserver frameObserver;

	public final static Function noCompanionFunction = new Function("noCompanionFunction", null, null, null, 0, 0, false, false, null, null, 0, false, 0, 0, null, null, 0);
	public static final HashMap<String, IValue> emptyKeywordMap = new HashMap<>(0);

	protected PrintWriter stdout;
	protected PrintWriter stderr;

	// Management of active coroutines
	protected Stack<Coroutine> activeCoroutines = new Stack<>();
	protected Frame ccf = null; // The start frame of the current active coroutine (coroutine's main function)	
	protected Frame cccf = null; // The candidate coroutine's start frame; used by the guard semantics
	protected RascalExecutionContext rex;

	public Map<IValue, IValue> moduleVariables;
	
	private final Map<Class<?>, Object> instanceCache;
	private final Map<String, Class<?>> classCache;

	private final Types types;
	
	public static RVMCore readFromFileAndInitialize(ISourceLocation rvmBinaryLocation, RascalExecutionContext rex) throws IOException{
		RVMExecutable rvmExecutable = RVMExecutable.read(rvmBinaryLocation);
		return ExecutionTools.initializedRVM(rvmExecutable, rex);
	}

	public static IValue readFromFileAndExecuteProgram(ISourceLocation rvmBinaryLocation, Map<String, IValue> keywordArguments, RascalExecutionContext rex) throws Exception{
		RVMExecutable rvmExecutable = RVMExecutable.read(rvmBinaryLocation);
		return ExecutionTools.executeProgram(rvmExecutable, keywordArguments, rex);
	}
	
	// An exhausted coroutine instance
	public static Coroutine exhausted = new Coroutine(null) {

		@Override
		public void next(Frame previousCallFrame) {
			throw new CompilerError("Attempt to activate an exhausted coroutine instance.");
		}
		
		@Override
		public void suspend(Frame current) {
			throw new CompilerError("Attempt to suspend an exhausted coroutine instance.");
		}
		
		@Override
		public boolean isInitialized() {
			return true;
		}
		
		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Coroutine copy() {
			throw new CompilerError("Attempt to copy an exhausted coroutine instance.");
		}  
	};

	public RVMCore(RVMExecutable rvmExec, RascalExecutionContext rex){
	  this.rex = rex;
	  rex.setRVM(this);
	  rex.setFullModuleName(rvmExec.getModuleName());

	  this.instanceCache = new HashMap<Class<?>, Object>();
	  this.classCache = new HashMap<String, Class<?>>();

	  this.vf = rex.getValueFactory();
	  tf = TypeFactory.getInstance();
	  this.stdout = rex.getStdOut();
	  this.stderr = rex.getStdErr();
	  NOVALUE = vf.string("$no-value$");
	  moduleVariables = new HashMap<IValue,IValue>();

	  this.symbol_definitions = rvmExec.getSymbolDefinitions();
	  this.functionStore = rvmExec.getFunctionStore();
	  this.functionMap = rvmExec.getFunctionMap();

	  this.resolver = rvmExec.getResolver();
	  this.overloadedStore = rvmExec.getOverloadedStore();
	  mappifyOverloadedStore();

	  this.constructorStore = rvmExec.getConstructorStore();
	  this.constructorMap = rvmExec.getConstructorMap();

	  this.types = new Types(vf);

	  IFrameObserver observer = rex.getFrameObserver(); 
	  this.frameObserver = (observer == null) ? NullFrameObserver.getInstance() : observer;
	}
	
	public void shutdown(){
	  frameObserver.report();
	}
	
	public Map<IValue, IValue> getModuleVariables() { return moduleVariables; }
	
	public void updateModuleVariable(IValue name, IValue newVal){
		IValue oldVal = moduleVariables.get(name);
		if(oldVal != null && !oldVal.getType().comparable(newVal.getType())){
			throw new CompilerError("Module variable " + name + " initalized with incompatible value " + newVal + " was " + oldVal);
		}
		moduleVariables.put(name, newVal);
	}
	
	public TypeStore getTypeStore() { 
       if(typeStore == null){
           typeStore = new TypeReifier(vf).buildTypeStore(symbol_definitions);
       }
       return typeStore;
    }
	
	void mappifyOverloadedStore(){
	  overloadedStoreMap = new HashMap<>();
	  for(OverloadedFunction of : overloadedStore){
	    String name = of.getName();
	    if(overloadedStoreMap.containsKey(name)){
	      OverloadedFunction previous = overloadedStoreMap.get(name);
	      if(of.getFunctions().length > previous.getFunctions().length){
	        overloadedStoreMap.put(name, of);
	      }
	    } else {
	      overloadedStoreMap.put(name, of);
	    }
	  }
	}
	
	/**
	 * Create an object which implements the provided interface (that is
	 * usually created by ApiGen.generate) by forwarding calls 
	 * to its methods directly to Rascal functions.
	 * 
	 * This works for interfaces which contain a specific kind of methods:
	 *    * each method should have a name which maps to a Rascal function name which 
	 *      is loaded into the current RVMCore
	 *    * the arity of the method should be equal to at least one of the Rascal 
	 *      functions with the given name
	 *    * all arguments of the methods should have a type which is a sub-type of IValue
	 *    * the return type of the methods should be either IValue or void
	 *    
	 * @param interf the interface which is to be implemented
	 * @return an object which implements this interface by forwarding to Rascal functions
	 */
	public <T> T asInterface(Class<T> interf) {
	    return interf.cast(Proxy.newProxyInstance(RVMCore.class.getClassLoader(), new Class<?> [] { interf }, new RascalFunctionInvocationHandler(this, interf)));
	}
	
	public PrintWriter getStdErr() { return rex.getStdErr(); }
	
	public PrintWriter getStdOut() { return rex.getStdOut(); }
		
	IRascalMonitor getMonitor() {return rex.getMonitor();}
	
	public IFrameObserver getFrameObserver() {
		return frameObserver;
	}
	
	public void setFrameObserver(IFrameObserver observer){
	    frameObserver = observer;
	    rex.setFrameObserver(observer);
	}
	
	protected String getFunctionName(int n) {
		for(String fname : functionMap.keySet()) {
			if(functionMap.get(fname) == n) {
				return fname;
			}
		}
		throw new CompilerError("Undefined function index " + n);
	}
	
	public String findVarName(Frame cf, int s, int pos){
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == s) {
				return findLocalName(fr, pos);
			}
		}
		return "** unknown variable **";
	}

	public String findLocalName(Frame cf, int pos){
		IString name =  ((IString) cf.function.localNames.get(vf.integer(pos)));
		return (name != null) ? name.getValue() : "** unknown variable **";
	}
	
	public Function getFunction(String name, Type returnType, Type argumentTypes){
	  next:
	    for(Function f : functionStore){
	      if(f.name.contains("/" + name + "(") && f.ftype instanceof FunctionType){
	        FunctionType ft = (FunctionType) f.ftype;
	        int arity = argumentTypes.getArity();
	        if(returnType.equals(ft.getReturnType()) && arity == ft.getArgumentTypes().getArity()){
	          for(int i = 0; i < arity; i++){ // ignore field names
	            if(!argumentTypes.getFieldType(i).equals(ft.getArgumentTypes().getFieldType(i))){
	              continue next;
	            }
	          }
	          return f;
	        }
	      }
	    }
	return null;
	}
	
	private ArrayList<Integer> getFunctionByNameAndArity(String name, int arity){
	  ArrayList<Integer> functions = new ArrayList<>();
      for(int n = 0; n < functionStore.length; n++){
        Function f = functionStore[n];
          if(f.name.contains("/" + name + "(") && f.ftype instanceof FunctionType){
              FunctionType ft = (FunctionType) f.ftype;
              if(ft.getArgumentTypes().getArity() == arity){
                  functions.add(n);
              }
          }
      }
      return functions;
  }
	
	public OverloadedFunction getOverloadedFunctionByNameAndArity(String name, int arity) throws NoSuchRascalFunction {
//    System.err.println("getFirstOverloadedFunctionByNameAndArity: " + name + ", " + arity);  
	  OverloadedFunction of = overloadedStoreMap.get(name);
	  Type tp = null;
	  //System.err.println(of);
	  ArrayList<Integer> filteredAlts = new ArrayList<>();
	  if(of != null){
	    for (int alt : of.getFunctions()) {
	      //System.err.println("alt: " + functionStore[alt].ftype);
	      if (functionStore[alt].ftype.getArity() == arity) {
	        tp = functionStore[alt].ftype;
	        filteredAlts.add(alt);
	      }
	    }
	    if(filteredAlts.size() > 0){
	      int falts[] = new int[filteredAlts.size()];
	      for(int i = 0; i < falts.length; i++){
	        falts[i] = filteredAlts.get(i);
	      }
	      return new OverloadedFunction(name, tp,  falts, new int[] { }, "");
	    }
	  }
	  filteredAlts = getFunctionByNameAndArity(name, arity);
	  if(filteredAlts.size() > 0){
	    tp = tf.tupleType(tf.valueType()); // arbitrary!
        int falts[] = new int[filteredAlts.size()];
        for(int i = 0; i < falts.length; i++){
          falts[i] = filteredAlts.get(i);
        }
        return new OverloadedFunction(name, tp,  falts, new int[] { }, "");
      }
	  

	  // ?? why are constructors not part of overloaded functions?
	  for(int i = 0; i < constructorStore.size(); i++){
	    tp = constructorStore.get(i);
	    if (tp.getName().equals(name) && tp.getArity() == arity) {
	      return new OverloadedFunction(name, tp,  new int[]{}, new int[] { i }, "");
	    }
	  }

	  throw new NoSuchRascalFunction(name);
	}
	
	// deprecated
	public OverloadedFunction getOverloadedFunction(String signature) throws NoSuchRascalFunction{
		OverloadedFunction result = null;
		
		String name = types.getFunctionName(signature);
		Type funType = types.getFunctionType(signature);
		FunctionType ft = (FunctionType) funType;
		int arity = ft.getArity();
		
		for(OverloadedFunction of : overloadedStore){
			if(of.matchesNameAndSignature(name, funType)){
				if(result == null){
					result = of;
				} else {
					if(result.getFunctions().length < of.getFunctions().length ||
					   result.getConstructors().length < of.getConstructors().length){
						result = of;
					}
				}
			}
		}
		
		if(result != null){
			return result;
		}
		
		ArrayList<Integer> filteredAlts = getFunctionByNameAndArity(name, arity);
		if(filteredAlts.size() > 0){
		  Type tp = tf.tupleType(tf.valueType()); // arbitrary!
		  int falts[] = new int[filteredAlts.size()];
		  for(int i = 0; i < falts.length; i++){
		    falts[i] = filteredAlts.get(i);
		  }
		  return new OverloadedFunction(name, tp,  falts, new int[] { }, "");
		}

		for(int i = 0; i < constructorStore.size(); i++){
			Type tp = constructorStore.get(i);
			if(name.equals(tp.getName()) && ft.getFieldTypes().comparable(funType.getFieldTypes()) 
					                     && ft.getReturnType().comparable(tp.getAbstractDataType())
					){
				return new OverloadedFunction(name, tp,  new int[]{}, new int[] { i }, "");
			}
		}
		
	
		// No overloaded function or constructor matched, only nonterminals remain ...
		for(int i = 0; i < functionStore.length; i++){
			Function fun = functionStore[i];
			if(fun.name.contains("/" + name + "(")){
				if(fun.ftype instanceof FunctionType){
					FunctionType tp = (FunctionType) fun.ftype;
					if(tp.comparable(ft)){
						return new OverloadedFunction(name, tp, new int[]{ i }, new int[] { }, "");
					}
				}
			}
		}
		throw new NoSuchRascalFunction(signature);
	}
	
	public Function getCompanionDefaultsFunction(String name, Type ftype){
		all:
			for(Function f : functionStore){
				//if(f.name.contains("companion")) System.err.println("getCompanionDefaultsFunction " + f.name);
				if(f.name.contains("companion-defaults") && 
						f.name.contains("::" + name + "(")){
					FunctionType ft = (FunctionType) f.ftype;
					if(ftype.getAbstractDataType().equals(ft.getReturnType())){
						if(ftype.isAbstractData()){
							//System.err.println("getCompanionDefaultsFunction1: " + name + ", " + ftype);
							return f;
						}
						if(ftype.getFieldTypes().getArity() == ft.getArgumentTypes().getArity()){
							for(int i = 0; i < ftype.getFieldTypes().getArity(); i++){
								if(!ftype.getFieldType(i).equals(ft.getArgumentTypes().getFieldType(i))){
									continue all;
								}
							}
							//System.err.println("getCompanionDefaultsFunction2: " + name + ", " + ftype);
							return f;
						}
					}
				}
			}
		//System.err.println("getCompanionDefaultsFunction3: " + name + ", " + ftype);
		return noCompanionFunction;
	}
	
	public Function getCompanionFieldDefaultFunction(Type adtType, String fieldName){
		String key = adtType.toString() + "::" + fieldName + "-companion-default";
		for(Function f : functionStore){
			if(f.name.equals(key)){
				return f;
			}
		}
		return noCompanionFunction;
	}
	
	public Frame makeFrameForVisit(FunctionInstance func){
		return new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
	}
	
	/**
	 * Execute a single, not-overloaded, function
	 * 
	 * @param uid_func	Internal function name
	 * @param posArgs	Positional arguments
	 * @param kwArgs	Keyword arguments
	 * @return
	 */
	public Object executeRVMFunction(String uid_func, IValue[] posArgs, Map<String,IValue> kwArgs){
		// Assumption here is that the function called is not a nested one and does not use global variables
		Function func = functionStore[functionMap.get(uid_func)];
		return executeRVMFunction(func, posArgs, kwArgs);
	}
	
	/**
	 * Execute an OverloadedFunction
	 * @param func	   OverloadedFunction
	 * @param posArgs  Arguments (last one is a map with keyword arguments)
	 * @param kwArgs   Keyword arguments
	 * @return		   Result of function execution
	 */
	public IValue executeRVMFunction(OverloadedFunction func, IValue[] posArgs, Map<String, IValue> kwArgs){
		if(func.getFunctions().length > 0){
				Function firstFunc = functionStore[func.getFunctions()[0]]; 
				Frame root = new Frame(func.scopeIn, null, null, func.getArity()+2, firstFunc);
				OverloadedFunctionInstance ofi = OverloadedFunctionInstance.computeOverloadedFunctionInstance(func.functions, func.constructors, root, func.scopeIn, functionStore, constructorStore, this);
				return executeRVMFunction(ofi, posArgs, kwArgs);
		} else {
			Type cons = constructorStore.get(func.getConstructors()[0]);
			return vf.constructor(cons, posArgs, kwArgs);
		}
	}
	
	public IList executeTests(ITestResultListener testResultListener, RascalExecutionContext rex){
	  IListWriter w = vf.listWriter();
	  for(Function f : functionStore){
	    if(f.isTest){
	      w.append(f.executeTest(testResultListener, getTypeStore(), rex));
	    }
	  }
	  return w.done();
	}
	
	/************************************************************************************/
	/*		Abstract methods to execute RVMFunctions and RVMPrograms					*/
	/************************************************************************************/
		
	/**
	 * Execute a single, not-overloaded, function
	 * 
	 * @param func		Function
	 * @param posArgs	Positional arguments
	 * @param kwArgs	Keyword arguments
	 * @return			Result of function execution
	 */
	abstract public Object executeRVMFunction(Function func, IValue[] posArgs, Map<String,IValue> kwArgs);
	
	/**
	 * Execute a FunctionInstance
	 * @param func		Function instance
	 * @param posArgs	Arguments (last one is a map with keyword arguments)
	 * @param kwArgs    Keyword arguments
	 * @return			Result of function execution
	 */
	abstract public IValue executeRVMFunction(FunctionInstance func, IValue[] posArgs, Map<String, IValue> kwArgs);
	
	/**
	 * Execute an OverloadedFunctionInstance
	 * @param func	   OverloadedFunctionInstance
	 * @param posArgs  Positional arguments
	 * @param kwArgs   Keyword arguments
	 * @return		   Result of function execution
	 */
	abstract public IValue executeRVMFunction(OverloadedFunctionInstance func, IValue[] posArgs, Map<String, IValue> kwArgs);

	/**
	 * Execute a function during a visit
	 * @param root	Frame in which the function will be executed
	 * @return		Result of function execution
	 */
	abstract public IValue executeRVMFunctionInVisit(Frame root);
	
	/**
	 * Execute a main program in a Rascal module
	 * @param moduleName	Name of the module
	 * @param uid_main		Internal name of the main function
	 * @param posArgs		Positional arguments
	 * @param kwArgs		Keyword arguments
	 * @return				Result of executing main function
	 */
	abstract public IValue executeRVMProgram(String moduleName, String uid_main, IValue[] posArgs, Map<String,IValue> kwArgs);
	
	/**
	 * Narrow an Object as occurring on the RVM runtime stack to an IValue that can be returned.
	 * Note that various non-IValues can occur:
	 * - Coroutine
	 * - Reference
	 * - FunctionInstance
	 * - Object[] (is converted to an IList)
	 * @param result to be returned
	 * @return converted result or an exception
	 */
	protected IValue narrow(Object result){
		if(result instanceof Integer) {
			return vf.integer((Integer)result);
		}
		if(result instanceof IValue) {
			return (IValue) result;
		}
		if(result instanceof Thrown) {
			((Thrown) result).printStackTrace(stdout);
			return vf.string(((Thrown) result).toString());
		}
		if(result instanceof Object[]) {
			IListWriter w = vf.listWriter();
			Object[] lst = (Object[]) result;
			for(int i = 0; i < lst.length; i++){
				w.append(narrow(lst[i]));
			}
			return w.done();
		}
		if(result == null){
			return null;
		}
		throw new CompilerError("Cannot convert object back to IValue: " + result);
	}
	
	/**
	 * Represent any object that can occur on the RVM stack stack as string
	 * @param some stack object
	 * @return its string representation
	 */
	@SuppressWarnings("rawtypes")
	protected static String asString(Object o){
		if(o == null)
			return "null";
		if(o instanceof Integer)
			return ((Integer)o).toString() + " [Java]";
		if(o instanceof String)
			return ((String)o) + " [Java]";
		if(o instanceof IValue)
			return ((IValue) o).toString() +" [IValue]";
		if(o instanceof Type)
			return ((Type) o).toString() + " [Type]";
		if(o instanceof Object[]){
			StringBuilder w = new StringBuilder();
			Object[] lst = (Object[]) o;
			w.append("[");
			for(int i = 0; i < lst.length; i++){
				w.append(asString(lst[i]));
				if(i < lst.length - 1)
						w.append(", ");
			}
			w.append("]");
			return w.toString() + " [Object[]]";
		}
		if(o instanceof Coroutine){
			if(((Coroutine)o).frame  != null && ((Coroutine)o).frame.function != null){
				return "Coroutine[" + ((Coroutine)o).frame.function.getName() + "]";
			} else {
				return "Coroutine[**no name**]";
			}
		}
		if(o instanceof Function){
			return "Function[" + ((Function)o).getName() + "]";
		}
		if(o instanceof FunctionInstance){
			return "Function[" + ((FunctionInstance)o).function.getName() + "]";
		}
		if(o instanceof OverloadedFunctionInstance) {
			OverloadedFunctionInstance of = (OverloadedFunctionInstance) o;
			String alts = "";
			for(Integer fun : of.getFunctions()) {
				alts = alts + fun + "; ";
			}
			return "OverloadedFunction[ alts: " + alts + "]";
		}
		if(o instanceof Reference){
			Reference ref = (Reference) o;
			return "Reference[" + ref.stack + ", " + ref.pos + "]";
		}
		if(o instanceof IListWriter){
			return "ListWriter[" + ((IListWriter) o).toString() + "]";
		}
		if(o instanceof ISetWriter){
			return "SetWriter[" + ((ISetWriter) o).toString() + "]";
		}
		if(o instanceof IMapWriter){
			return "MapWriter[" + ((IMapWriter) o).toString() + "]";
		}
		if(o instanceof Matcher){
			return "Matcher[" + ((Matcher) o).pattern() + "]";
		}
		if(o instanceof Thrown) {
			return "THROWN[ " + asString(((Thrown) o).getValue()) + " ]";
		}
		
		if(o instanceof StringBuilder){
			return "StringBuilder[" + ((StringBuilder) o).toString() + "]";
		}
		if(o instanceof HashSet){
			return "HashSet[" + ((HashSet<?>) o).toString() + "]";
		}
		if(o instanceof Map){
			return "Map[" + ((Map<?, ?>) o).toString() + "]";
		}
		if(o instanceof HashMap){
			return "HashMap[" + ((HashMap<?, ?>) o).toString() + "]";
		}
		if(o instanceof Map.Entry){
			return "Map.Entry[" + ((Map.Entry) o).toString() + "]";
		}
		
		return o.getClass().getName();
	
		//throw new CompilerError("asString cannot convert: " + o);
	}
	
	public String asString(Object o, int w){
		String repr = asString(o);
		return (repr.length() < w) ? repr : repr.substring(0, w) + "...";
	}
	
	/********************************************************************************/
	/*			Auxiliary functions that implement specific instructions and are	*/
	/*  		used both by RVM interpreter and generated JVM bytecod				*/
	/********************************************************************************/
	
	// LOAD/PUSH VAR

	protected Object LOADVAR(final Frame cf, final int varScope, final int pos){
		return CodeBlock.isMaxArg2(pos) ? LOADVARMODULE(cf, varScope) : LOADVARSCOPED(cf, varScope, pos);
	}

	protected Object LOADVARMODULE(final Frame cf, final int varScope){				
		return moduleVariables.get(cf.function.constantStore[varScope]);		// TODO: undefined case
	}

	protected Object LOADVARSCOPED(final Frame cf, final int varScope, final int pos){
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {					
				return fr.stack[pos];											// TODO: undefined case
			}
		}
		throw new CompilerError("LOADVAR cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}

	protected int PUSHVAR(final Object[] stack, int sp, final Frame cf, final int varScope, final int pos){
		stack[sp++] =  CodeBlock.isMaxArg2(pos) ? LOADVARMODULE(cf, varScope) : LOADVARSCOPED(cf, varScope, pos);
		return sp;
	}

	protected int PUSHVARMODULE(final Object[] stack, int sp, final Frame cf, final int varScope){
		stack[sp++] = LOADVARMODULE(cf, varScope);
		return sp;
	}

	protected int PUSHVARSCOPED(final Object[] stack, int sp, final Frame cf, final int varScope, final int pos){
		stack[sp++] = LOADVARSCOPED(cf, varScope, pos);
		return sp;
	}
		
	// LOAD/PUSH VARREF
	
	protected Object LOADVARREF(final Frame cf, final int varScope, final int pos){
		return CodeBlock.isMaxArg2(pos) ? LOADVARREFMODULE(cf, varScope) : LOADVARREFSCOPED(cf, varScope, pos);
	}
	
	protected Object LOADVARREFMODULE(final Frame cf, final int varScope){
		return moduleVariables.get(cf.function.constantStore[varScope]);
	}
	
	protected Object LOADVARREFSCOPED(final Frame cf, final int varScope, final int pos){
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {					
				return new Reference(fr.stack, pos);
			}
		}
		throw new CompilerError("LOADVARREF cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}
	
	protected int PUSHVARREF(final Object[] stack, int sp, final Frame cf, final int varScope, final int pos){
		stack[sp++] = CodeBlock.isMaxArg2(pos) ? LOADVARREFMODULE(cf, varScope) : LOADVARREFSCOPED(cf, varScope, pos);
		return sp;
	}
	
	protected int PUSHVARREFMODULE(final Object[] stack, int sp, final Frame cf, final int varScope){
		stack[sp++] = LOADVARREFMODULE(cf, varScope);
		return sp;
	}
	
	protected int PUSHVARREFSCOPED(final Object[] stack, int sp, final Frame cf, final int varScope, final int pos){
		stack[sp++] = LOADVARREFSCOPED(cf, varScope, pos);
		return sp;
	}
	
	// LOAD/PUSH VARDEREF
	
	protected Object LOADVARDEREF(Frame cf, int varScope, int pos){
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {
				Reference ref = (Reference) fr.stack[pos];
				return ref.stack[ref.pos];
			}
		}
		throw new CompilerError("LOADVARDEREF cannot find matching scope: " + varScope, cf);
	}
	
	protected int PUSHVARDEREF(final Object[] stack, int sp, final Frame cf, final int varScope, final int pos){
		stack[sp++] = LOADVARDEREF(cf, varScope, pos);
		return sp;
	}
	
	// STOREVAR
	
	protected void STOREVAR(final Frame cf, final int varScope, final int pos, final Object accu){
		if(CodeBlock.isMaxArg2(pos)){
			STOREVARMODULE(cf, varScope, accu);
		} else {
			STOREVARSCOPED(cf, varScope, pos, accu);
		}
	}

	protected void STOREVARMODULE(final Frame cf, final int varScope, final Object accu){
		IValue mvar = cf.function.constantStore[varScope];
		moduleVariables.put(mvar, (IValue)accu);
		return;
	}

	protected void STOREVARSCOPED(Frame cf, int varScope, int pos, Object accu){
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {
				// TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues
				fr.stack[pos] = accu;
				return;
			}
		}
		throw new CompilerError("STOREVAR cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}
	
	// RESETVAR
	
	protected void RESETVAR(Frame cf, int varScope, int pos){
		if(CodeBlock.isMaxArg2(pos)){
			IValue mvar = cf.function.constantStore[varScope];
			moduleVariables.put(mvar, null);
			return;
		}
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {
				// TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues
				fr.stack[pos] = null;
				return;
			}
		}
		throw new CompilerError("RESETVAR cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}
	
	// UNWRAPTHROWNVAR
	
	protected int UNWRAPTHROWNVAR(final Object[] stack, final int sp, final Frame cf, final int varScope, final int pos){
		return CodeBlock.isMaxArg2(pos) ? UNWRAPTHROWNVARMODULE(stack, sp, cf, varScope)
									    : UNWRAPTHROWNVARSCOPED(stack, sp, cf, varScope, pos);
	}
	
	protected int UNWRAPTHROWNVARMODULE(final Object[] stack, final int sp, final Frame cf, final int varScope){
		IValue mvar = cf.function.constantStore[varScope];
		moduleVariables.put(mvar, (IValue)stack[sp - 1]);
		return sp;
	}
	
	protected int UNWRAPTHROWNVARSCOPED(final Object[] stack, int sp, final Frame cf, final int varScope, final int pos){
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {
				// TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues
				fr.stack[pos] = ((Thrown) stack[--sp]).getValue();
				return sp;
			}
		}
		throw new CompilerError("UNWRAPTHROWNVAR cannot find matching scope: " + varScope, cf);
	}
	
	protected void STOREVARDEREF(Frame cf, int varScope, int pos, Object accu){
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) { 
			if (fr.scopeId == varScope) {
				Reference ref = (Reference) fr.stack[pos];
				ref.stack[ref.pos] = accu;
			}
		}
		throw new CompilerError("STOREVARDEREF cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}
	
	// STORELOCKWP
	
	@SuppressWarnings("unchecked")
	protected void STORELOCKWP(final Object[] stack, Frame cf, int iname, Object accu){
		String name = ((IString) cf.function.codeblock.getConstantValue(iname)).getValue();
		Map<String, IValue> kargs = (Map<String, IValue>) stack[cf.function.nformals - 1];
		if(kargs == emptyKeywordMap){
			System.err.println("Creating new kw map while updating: " + name);
			kargs = new HashMap<>();
			stack[cf.function.nformals - 1] = kargs;
		}
		kargs.put(name, (IValue) accu);
	}
	
	// LOAD/PUSH VARKWP
	
	@SuppressWarnings("unchecked")
	protected Object LOADVARKWP(final Frame cf, final int varScope, final int iname){
	  String name = ((IString) cf.function.codeblock.getConstantValue(iname)).getValue();

	  for(Frame f = cf.previousScope; f != null; f = f.previousCallFrame) {
	    if (f.scopeId == varScope) {    
	      if(f.function.nformals > 0){
	        Object okargs = f.stack[f.function.nformals - 1];
	        if(okargs instanceof Map<?,?>){ // Not all frames provide kwargs, i.e. generated PHI functions.
	          Map<String, IValue> kargs = (Map<String,IValue>) okargs;
	          if(kargs.containsKey(name)) {
	            IValue val = kargs.get(name);
	            //if(val.getType().isSubtypeOf(defaultValue.getKey())) {
	            return val;
	            //}
	          }
	          Map<String, Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) f.stack[f.function.nformals];

	          if(defaults.containsKey(name)) {
	            Entry<Type, IValue> defaultValue = defaults.get(name);
	            //if(val.getType().isSubtypeOf(defaultValue.getKey())) {
	            return defaultValue.getValue();
	            //}
	          }
	        }
	      }
	    }
	  }               
	  throw new CompilerError("LOADVARKWP cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}
	
	protected int PUSHVARKWP(final Object[] stack, int sp, final Frame cf, final int varScope, final int iname){
		stack[sp++] = LOADVARKWP(cf, varScope, iname);
		return sp;
	}
	
	// STOREVARKWP
	
	@SuppressWarnings("unchecked")
	protected void STOREVARKWP(final Frame cf, final int varScope, final int iname, final Object accu){
		
		String name = ((IString) cf.function.codeblock.getConstantValue(iname)).getValue();
		IValue val = (IValue) accu;
		for(Frame f = cf.previousScope; f != null; f = f.previousCallFrame) {
			if (f.scopeId == varScope) {
				if(f.function.nformals > 0){
					Object okargs = f.stack[f.function.nformals - 1];
					if(okargs instanceof Map<?,?>){	// Not all frames provide kwargs, i.e. generated PHI functions.
						Map<String, IValue> kargs = (Map<String,IValue>) f.stack[f.function.nformals - 1];
						
						if(kargs.containsKey(name)) {
							val = kargs.get(name);
							//if(val.getType().isSubtypeOf(defaultValue.getKey())) {
							if(kargs == emptyKeywordMap){
								System.err.println("Creating new kw map while updating: " + name);
								kargs = new HashMap<>();
								f.stack[f.function.nformals - 1] = kargs;
							}
							kargs.put(name,  val);
							return;
							//}
						}
						Map<String, Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) f.stack[f.function.nformals];

						if(defaults.containsKey(name)) {
							//Entry<Type, IValue> defaultValue = defaults.get(name);
							//if(val.getType().isSubtypeOf(defaultValue.getKey())) {
							kargs.put(name,val);
							return;
							//}
						}
					}
				}
			}
		}				
		throw new CompilerError("STOREVARKWP cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}
	
	// LOAD/PUSH LOCKWP
	
	@SuppressWarnings("unchecked")
	protected Object LOADLOCKWP(final Object[] stack, final Frame cf, final int iname){
	  String name = ((IString) cf.function.codeblock.getConstantValue(iname)).getValue();

	  Map<String, Map.Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) stack[cf.function.nformals];
	  Map.Entry<Type, IValue> defaultValue = defaults.get(name);
	  Frame f = cf;

	  // TODO: UNCOMMENT TO GET KEYWORD PARAMETER PROPAGATION
	  //for(Frame f = cf; f != null; f = f.previousCallFrame) {
	  int nf = f.function.nformals;
	  if(nf > 0){                              // Some generated functions have zero args, i.e. EQUIVALENCE
	    Object okargs = f.stack[nf - 1];
	    if(okargs instanceof Map<?,?>){        // Not all frames provide kwargs, i.e. generated PHI functions.
	      Map<String, IValue> kargs = (Map<String,IValue>) okargs;
	      if(kargs.containsKey(name)) {
	        IValue val = kargs.get(name);
	        if(val.getType().isSubtypeOf(defaultValue.getKey())) {
	          return val;
	        }
	      }
	    }
	  }
	  //}             
	  return defaultValue.getValue();
	}
	
	protected int PUSHLOCKWP(final Object[] stack, int sp, final Frame cf, final int iname){
		stack[sp++] = LOADLOCKWP(stack, cf, iname);
		return sp;
	}
	
	// CALLCONSTR
	
	@SuppressWarnings({"unchecked", "unused"})
	protected int CALLCONSTR(final Object[] stack, int sp, final int iconstructor, final int arity){
		
		Type constructor = constructorStore.get(iconstructor);
		IValue[] args = new IValue[constructor.getArity()];

		java.util.Map<String,IValue> kwargs;
		
		Type type = (Type) stack[--sp];		// TODO: emove from instruction
		
		kwargs = (java.util.Map<String,IValue>) stack[--sp];

		for(int i = 0; i < constructor.getArity(); i++) {
			args[constructor.getArity() - 1 - i] = (IValue) stack[--sp];
		}
		stack[sp++] = vf.constructor(constructor, args, kwargs);
		return sp;
	}

	protected int PRINTLN(Object[] stack, int sp, int arity){
		StringBuilder w = new StringBuilder();
		for(int i = arity - 1; i >= 0; i--){
			String str = (stack[sp - 1 - i] instanceof IString) ? ((IString) stack[sp - 1 - i]).toString() : asString(stack[sp - 1 - i]);
			w.append(str).append(" ");
		}
		stdout.println(w.toString());
		sp = sp - arity + 1;
		return sp;
	}
	
	public int VISIT(Object[] stack,  int sp, boolean direction, boolean progress, boolean fixedpoint, boolean rebuild){
		FunctionInstance phi = (FunctionInstance)stack[sp - 8];
		IValue subject = (IValue) stack[sp - 7];
		Reference refMatched = (Reference) stack[sp - 6];
		Reference refChanged = (Reference) stack[sp - 5];
		Reference refLeaveVisit = (Reference) stack[sp - 4];
		Reference refBegin = (Reference) stack[sp - 3];
		Reference refEnd = (Reference) stack[sp - 2];
		DescendantDescriptor descriptor = (DescendantDescriptor) stack[sp - 1];
		DIRECTION tr_direction = direction ? DIRECTION.BottomUp : DIRECTION.TopDown;
		PROGRESS tr_progress = progress ? PROGRESS.Continuing : PROGRESS.Breaking;
		FIXEDPOINT tr_fixedpoint = fixedpoint ? FIXEDPOINT.Yes : FIXEDPOINT.No;
		REBUILD tr_rebuild = rebuild ? REBUILD.Yes :REBUILD.No;
		IValue res = new Traverse(vf).traverse(tr_direction, tr_progress, tr_fixedpoint, tr_rebuild, subject, phi, refMatched, refChanged, refLeaveVisit, refBegin, refEnd, this, descriptor);
		stack[sp - 8] = res;
		sp -= 7;
		// Trick: we return a negative sp to force a function return;
		return ((IBool)refLeaveVisit.getValue()).getValue() ? -sp : sp;
	}
	
	@SuppressWarnings("unchecked")
	public int CHECKMEMO(Object[] stack, int sp, Frame cf){;
	
	    Function fun = cf.function;
		MemoizationCache<IValue> cache = fun.memoization == null ? null : fun.memoization.get();
		if(cache == null){
			cache = new MemoizationCache<>();
			fun.memoization = new SoftReference<>(cache);
		}
		int nformals = fun.nformals;
		IValue[] args = new IValue[nformals - 1];
		for(int i = 0; i < nformals - 1; i++){
			args[i] = (IValue) stack[i];
		}

		IValue result = cache.getStoredResult(args, (Map<String,IValue>)stack[nformals - 1]);
		if(result == null){
			return sp + 1;
		}
		stack[sp++] = result;
		return -sp;					// Trick: we return a negative sp to force a function return;
	}
	
	public Class<?> getJavaClass(String className){
		Class<?> clazz = classCache.get(className);
		if(clazz != null){
			return clazz;
		}
		
		try {
			clazz = this.getClass().getClassLoader().loadClass(className);
			classCache.put(className, clazz);
	        return clazz;
		} 
		catch(ClassNotFoundException | NoClassDefFoundError e1) {
			throw new CompilerError("Class " + className + " not found", e1);
		}
	}
	
	public Object getJavaClassInstance(Class<?> clazz){
		Object instance = instanceCache.get(clazz);
		if (instance != null){
			return instance;
		}
		try {
			Constructor<?> constructor = clazz.getConstructor(IValueFactory.class);
			instance = constructor.newInstance(vf);
			instanceCache.put(clazz, instance);
			return instance;
		} catch (IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException | NoSuchMethodException e) {
			throw new ImplementationError(e.getMessage(), e);
		} 
	}
	
	Method getJavaMethod(Class<?> clazz, String methodName, String className, Type parameterTypes, Type keywordTypes, int reflect) {
	    try {
	        return clazz.getMethod(methodName, makeJavaTypes(methodName, className, parameterTypes, keywordTypes, reflect));
	    }
	    catch (NoSuchMethodException | SecurityException e) {
	        throw new CompilerError("could not find Java method", e);
	    }
    }
	
	int callJavaMethod(String methodName, String className, Type parameterTypes, Type keywordTypes, int reflect, Object[] stack, int sp) throws Throw, Throwable {
	    Class<?> clazz = getJavaClass(className);
	    Method m = getJavaMethod(clazz, methodName, className, parameterTypes, keywordTypes, reflect);
	    return callJavaMethod(clazz, m, parameterTypes, keywordTypes, reflect, stack, sp);
	}


    int callJavaMethod(Class<?> clazz, Method m, Type parameterTypes, Type keywordTypes, int reflect, Object[] stack, int sp) throws Throw, Throwable {
	    try {
			Object instance = getJavaClassInstance(clazz);
			int arity = parameterTypes.getArity();
			int kwArity = keywordTypes.getArity();
			int kwMaps = kwArity > 0 ? 2 : 0;
			Object[] parameters = new Object[arity + kwArity + reflect];
			for(int i = arity - 1; i >= 0; i--){
				parameters[i] = stack[sp - arity - kwMaps + i];
			}

			if(kwArity > 0){
				@SuppressWarnings("unchecked")
				Map<String, IValue> kwMap = (Map<String, IValue>) stack[sp - 2];
				@SuppressWarnings("unchecked")
				Map<String, Map.Entry<Type, IValue>> kwDefaultMap = (Map<String, Map.Entry<Type, IValue>>) stack[sp - 1];
				
				for(int i = arity + kwArity - 1; i >= arity; i--){
					String key = keywordTypes.getFieldName(i - arity);
					IValue val = kwMap.get(key);
					if(val == null){
						val = kwDefaultMap.get(key).getValue();
					}
					parameters[i] = val;
				}
			}
			
			if(reflect == 1) {
				parameters[arity + kwArity] = converted.contains(clazz.getName() + "." + m.getName()) ? this.rex : null /*this.getEvaluatorContext()*/; // TODO: remove CTX
			}
			stack[sp - arity - kwMaps] =  m.invoke(instance, parameters);
			return sp - arity - kwMaps + 1;
		} 
		catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
			throw new CompilerError("could not call Java method", e);
		} 
		catch (InvocationTargetException e) {
			if(e.getTargetException() instanceof Throw) {
				throw (Throw) e.getTargetException();
			}
			if(e.getTargetException() instanceof Thrown){
				throw (Thrown) e.getTargetException();
			}
			
			throw e.getTargetException();
		}
	}
	
	// Reflective methods where the CTX arguments has already been replaced by a REX argument
	private static HashSet<String> converted = new HashSet<String>(Arrays.asList(
			"org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ParsingTools.parseFragment",
			"org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram.executeProgram",
			"org.rascalmpl.library.experiments.Compiler.CoverageCompiled.startCoverage",
			"org.rascalmpl.library.experiments.Compiler.CoverageCompiled.stopCoverage",
			"org.rascalmpl.library.experiments.Compiler.CoverageCompiled.getCoverage",
			"org.rascalmpl.library.experiments.Compiler.ProfileCompiled.startProfile",
			"org.rascalmpl.library.experiments.Compiler.ProfileCompiled.stopProfile",
			"org.rascalmpl.library.experiments.Compiler.ProfileCompiled.getProfile",
			"org.rascalmpl.library.experiments.Compiler.ProfileCompiled.reportProfile",
			
			"org.rascalmpl.library.lang.csv.IOCompiled.readCSV",
			"org.rascalmpl.library.lang.csv.IOCompiled.getCSVType",
			"org.rascalmpl.library.lang.csv.IOCompiled.writeCSV",
			"org.rascalmpl.library.lang.json.IOCompiled.fromJSON",
			
			"org.rascalmpl.library.PreludeCompiled.delAnnotation",
			"org.rascalmpl.library.PreludeCompiled.delAnnotations",
			"org.rascalmpl.library.PreludeCompiled.implode",
			"org.rascalmpl.library.PreludeCompiled.parse",
			"org.rascalmpl.library.PreludeCompiled.print",
			"org.rascalmpl.library.PreludeCompiled.println",
			"org.rascalmpl.library.PreludeCompiled.iprint",
			"org.rascalmpl.library.PreludeCompiled.iprintln",
			"org.rascalmpl.library.PreludeCompiled.rprint",
			"org.rascalmpl.library.PreludeCompiled.rprintln",
			"org.rascalmpl.library.PreludeCompiled.sort",
			"org.rascalmpl.library.util.MonitorCompiled.startJob",
			"org.rascalmpl.library.util.MonitorCompiled.event",
			"org.rascalmpl.library.util.MonitorCompiled.endJob",
			"org.rascalmpl.library.util.MonitorCompiled.todo",
			"org.rascalmpl.library.util.ReflectiveCompiled.parseModule",
			"org.rascalmpl.library.util.ReflectiveCompiled.getModuleLocation",
			"org.rascalmpl.library.util.ReflectiveCompiled.getSearchPathLocation",
			"org.rascalmpl.library.util.ReflectiveCompiled.inCompiledMode",
			"org.rascalmpl.library.util.ReflectiveCompiled.parseNamedModuleWithSpaces",
			"org.rascalmpl.library.util.ReflectiveCompiled.diff",
			"org.rascalmpl.library.util.ReflectiveCompiled.watch",
			"org.rascalmpl.library.util.WebserverCompiled.serve"
		
			/*
			 * 	TODO:
			 * cobra::util::outputlogger::startLog
			 * cobra::util::outputlogger::getLog
			 * cobra::quickcheck::_quickcheck
			 * cobra::quickcheck::arbitrary
			 * 
			 * experiments::resource::Resource::registerResource
			 * experiments::resource::Resource::getTypedResource
			 * experiments::resource::Resource::generateTypedInterfaceInternal
			
			 * experiments::vis2::vega::Vega::color
			 * 
			 * lang::aterm::IO::readTextATermFile
			 * lang::aterm::IO::writeTextATermFile
			 * 
			 * lang::html::IO::readHTMLFile
			 * 
			 * lang::java::m3::AST::setEnvironmentOptions
			 * lang::java::m3::AST::createAstFromFile
			 * lang::java::m3::AST::createAstFromString
			 * lang::java::m3::Core::createM3FromFile
			 * lang::java::m3::Core::createM3FromFile
			 *  lang::java::m3::Core::createM3FromJarClass
			 *  
			 *  lang::jvm::run::RunClassFile::runClassFile
			 *  lang::jvm::transform::SerializeClass::serialize
			 *  
			 *  lang::rsf::IO::readRSF
			 *  lang::rsf::IO::getRSFTypes
			 *  lang::rsf::IO::readRSFRelation
			 *  
			 *  lang::yaml::Model::loadYAML
			 *  lang::yaml::Model::dumpYAML
			 *  
			 *  resource::jdbc::JDBC::registerJDBCClass
			 *  util::tasks::Manager
			 *  util::Eval
			 *  util::Monitor
			 *  vis::Figure::color
			 *  
			 *  Traversal::getTraversalContext
			 *  
			 *  **eclipse**
			 *  util::Editors
			 *  util::FastPrint
			 *  util::HtmlDisplay
			 *  util::IDE
			 *  util::ResourceMarkers
			 *  vis::Render
			 *  vis::RenderSWT
			 *  
			 */
	));
			
	private static Class<?>[] makeJavaTypes(String methodName, String className, Type parameterTypes, Type keywordTypes, int reflect){
		JavaClasses javaClasses = new JavaClasses();
		int arity = parameterTypes.getArity();
		int kwArity = keywordTypes.getArity();
		Class<?>[] jtypes = new Class<?>[arity + kwArity + reflect];
		
		int i = 0;
		while(i < parameterTypes.getArity()){
			jtypes[i] = parameterTypes.getFieldType(i).accept(javaClasses);
			i++;
		}
		
		while(i < arity + kwArity){
			jtypes[i] = keywordTypes.getFieldType(i -  arity).accept(javaClasses);
			i++;
		}
		
		if(reflect == 1) {
			jtypes[arity + kwArity] = converted.contains(className + "." + methodName) 
									  ? RascalExecutionContext.class 
									  : IEvaluatorContext.class;				// TODO: remove CTX
		}
		return jtypes;
	}

	private static class JavaClasses extends DefaultRascalTypeVisitor<Class<?>, RuntimeException> {

		public JavaClasses() {
			super(IValue.class);
		}

		@Override
		public Class<?> visitNonTerminal(RascalType type)
				throws RuntimeException {
			return IConstructor.class;
		}
		
		@Override
		public Class<?> visitBool(org.rascalmpl.value.type.Type boolType) {
			return IBool.class;
		}

		@Override
		public Class<?> visitReal(org.rascalmpl.value.type.Type type) {
			return IReal.class;
		}

		@Override
		public Class<?> visitInteger(org.rascalmpl.value.type.Type type) {
			return IInteger.class;
		}
		
		@Override
		public Class<?> visitRational(org.rascalmpl.value.type.Type type) {
			return IRational.class;
		}
		
		@Override
		public Class<?> visitNumber(org.rascalmpl.value.type.Type type) {
			return INumber.class;
		}

		@Override
		public Class<?> visitList(org.rascalmpl.value.type.Type type) {
			return IList.class;
		}

		@Override
		public Class<?> visitMap(org.rascalmpl.value.type.Type type) {
			return IMap.class;
		}

		@Override
		public Class<?> visitAlias(org.rascalmpl.value.type.Type type) {
			return type.getAliased().accept(this);
		}

		@Override
		public Class<?> visitAbstractData(org.rascalmpl.value.type.Type type) {
			return IConstructor.class;
		}

		@Override
		public Class<?> visitSet(org.rascalmpl.value.type.Type type) {
			return ISet.class;
		}

		@Override
		public Class<?> visitSourceLocation(org.rascalmpl.value.type.Type type) {
			return ISourceLocation.class;
		}

		@Override
		public Class<?> visitString(org.rascalmpl.value.type.Type type) {
			return IString.class;
		}

		@Override
		public Class<?> visitNode(org.rascalmpl.value.type.Type type) {
			return INode.class;
		}

		@Override
		public Class<?> visitConstructor(org.rascalmpl.value.type.Type type) {
			return IConstructor.class;
		}

		@Override
		public Class<?> visitTuple(org.rascalmpl.value.type.Type type) {
			return ITuple.class;
		}

		@Override
		public Class<?> visitValue(org.rascalmpl.value.type.Type type) {
			return IValue.class;
		}

		@Override
		public Class<?> visitVoid(org.rascalmpl.value.type.Type type) {
			return null;
		}

		@Override
		public Class<?> visitParameter(org.rascalmpl.value.type.Type parameterType) {
			return parameterType.getBound().accept(this);
		}

		@Override
		public Class<?> visitDateTime(Type type) {
			return IDateTime.class;
		}
	}
}
