package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;	// TODO: remove import: NOT YET: JavaCalls generate a Throw
import org.rascalmpl.interpreter.result.util.MemoizationCache;
import org.rascalmpl.interpreter.types.DefaultRascalTypeVisitor;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.NullFrameObserver;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.DescendantDescriptor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.DIRECTION;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.FIXEDPOINT;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.PROGRESS;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.REBUILD;
import org.rascalmpl.uri.URIResolverRegistry;
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
import org.rascalmpl.values.ValueFactoryFactory;


public class RVM /*implements java.io.Serializable*/ {

	//private static final long serialVersionUID = 2178453095307370332L;
	
	public final IValueFactory vf;

	protected final TypeFactory tf;
	
	protected final static IBool Rascal_TRUE = ValueFactoryFactory.getValueFactory().bool(true);
	protected final static IBool Rascal_FALSE = ValueFactoryFactory.getValueFactory().bool(false);
	private final IString NONE; 
	
	private final boolean profileRascalPrimitives = false;
	private final boolean profileMuPrimitives = false;
	private boolean ocall_debug = false;
	
	protected ArrayList<Function> functionStore;
	protected Map<String, Integer> functionMap;
	
	// Function overloading
	private final Map<String, Integer> resolver;
	protected final ArrayList<OverloadedFunction> overloadedStore;
	
	protected ArrayList<Type> constructorStore;
	private final Map<String, Integer> constructorMap;
	
	final static Function noCompanionFunction = new Function("noCompanionFunction", null, null, 0, 0, false, null, 0, false, 0, 0, null, null, 0);
	
	protected final Map<IValue, IValue> moduleVariables;
	PrintWriter stdout;
	PrintWriter stderr;
	
	protected static final HashMap<String, IValue> emptyKeywordMap = new HashMap<>(0);
	
	private final IFrameObserver frameObserver;
		
	// Management of active coroutines
	Stack<Coroutine> activeCoroutines = new Stack<>();
	Frame ccf = null; // The start frame of the current active coroutine (coroutine's main function)
	Frame cccf = null; // The candidate coroutine's start frame; used by the guard semantics 
	protected RascalExecutionContext rex;
	List<ClassLoader> classLoaders;
	
	private final Map<Class<?>, Object> instanceCache;
	private final Map<String, Class<?>> classCache;
	
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

	public RVM(RVMExecutable rvmExec, RascalExecutionContext rex) {
		
		this.rex = rex;
		rex.setRVM(this);
		
		this.vf = rex.getValueFactory();
		tf = TypeFactory.getInstance();

		this.instanceCache = new HashMap<Class<?>, Object>();
		this.classCache = new HashMap<String, Class<?>>();
		
		this.classLoaders = rex.getClassLoaders();
		this.stdout = rex.getStdOut();
		this.stderr = rex.getStdErr();
		
//		Rascal_TRUE = vf.bool(true);
//		Rascal_FALSE = vf.bool(false);
		NONE = vf.string("$nothing$");
		
		this.functionMap = rvmExec.getFunctionMap();
		this.functionStore = rvmExec.getFunctionStore();
		
		this.constructorMap = rvmExec.getConstructorMap();
		this.constructorStore = rvmExec.getConstructorStore();

		this.resolver = rvmExec.getResolver();
		this.overloadedStore = rvmExec.getOverloadedStore();
		
		moduleVariables = new HashMap<IValue,IValue>();

		Opcode.init(stdout, rex.getProfile());
		
		IFrameObserver observer = rex.getFrameObserver(); 
		this.frameObserver = (observer == null) ? NullFrameObserver.getInstance() : observer;		
	}
	
	public static RVM readFromFileAndInitialize(ISourceLocation rvmBinaryLocation, RascalExecutionContext rex) throws IOException{
		RVMExecutable rvmExecutable = RVMExecutable.read(rvmBinaryLocation);
		return ExecutionTools.initializedRVM(rvmExecutable, rex);
	}
	
	public static IValue readFromFileAndExecuteProgram(ISourceLocation rvmBinaryLocation, IMap keywordArguments, RascalExecutionContext rex) throws Exception{
		RVMExecutable rvmExecutable = RVMExecutable.read(rvmBinaryLocation);
		return ExecutionTools.executeProgram(rvmExecutable, keywordArguments, rex);
	}
	
	URIResolverRegistry getResolverRegistry() { return URIResolverRegistry.getInstance(); }
	
	IRascalMonitor getMonitor() {return rex.getMonitor();}
	
	public PrintWriter getStdErr() { return rex.getStdErr(); }
	
	public PrintWriter getStdOut() { return rex.getStdOut(); }
	
	Configuration getConfiguration() { return rex.getConfiguration(); }
	
	List<ClassLoader> getClassLoaders() { return rex.getClassLoaders(); }	
	
	public IFrameObserver getFrameObserver(){
		return frameObserver;
	}
	
	public Map<IValue, IValue> getModuleVariables() { return moduleVariables; }
	
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
		throw new CompilerError("Cannot convert object back to IValue: " + result);
	}
	
	/**
	 * Represent any object that can occur on the RVM stack stack as string
	 * @param some stack object
	 * @return its string representation
	 */
	@SuppressWarnings("rawtypes")
	protected String asString(Object o){
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
				alts = alts + functionStore.get(fun).getName() + "; ";
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
			return "THROWN[ " + asString(((Thrown) o).value) + " ]";
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

	private String getFunctionName(int n) {
		for(String fname : functionMap.keySet()) {
			if(functionMap.get(fname) == n) {
				return fname;
			}
		}
		throw new CompilerError("Undefined function index " + n);
	}

	public String getConstructorName(int n) {
		for(String cname : constructorMap.keySet()) {
			if(constructorMap.get(cname) == n) {
				return cname;
			}
		}
		throw new CompilerError("Undefined constructor index " + n);
	}
	
	public String getOverloadedFunctionName(int n) {
		for(String ofname : resolver.keySet()) {
			if(resolver.get(ofname) == n) {
				return ofname;
			}
		}
		throw new CompilerError("Undefined overloaded function index " + n);
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
	
	public Function getFunction(String name, Type returnType, Type argumentTypes){
		for(Function f : functionStore){
			if(f.name.contains("/" + name + "(") && f.ftype instanceof FunctionType){
				FunctionType ft = (FunctionType) f.ftype;
				if(returnType.equals(ft.getReturnType()) &&
				   argumentTypes.equals(ft.getArgumentTypes())){
					return f;
				}
			}
		}
		return null;
	}
	
	/**
	 * execute a single function, on-overloaded, function
	 * 
	 * @param uid_func	Internal function name
	 * @param posArgs		Arguments
	 * @param kwArgs	Keyword arguments
	 * @return
	 */
	public Object executeFunction(String uid_func, IValue[] posArgs, Map<String,IValue> kwArgs){
		// Assumption here is that the function called is not a nested one
		// and does not use global variables
		Function func = functionStore.get(functionMap.get(uid_func));
		return executeFunction(func, posArgs, kwArgs);
	}
	
	/**
	 * execute a single function, on-overloaded, function
	 * 
	 * @param uid_func	Internal function name
	 * @param posArgs		Argumens
	 * @param kwArgs	Keyword arguments
	 * @return
	 */
	public Object executeFunction(Function func, IValue[] posArgs, Map<String,IValue> kwArgs){
		// Assumption here is that the function called is not a nested one
		// and does not use global variables
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		Frame cf = root;
		
		// Pass the program arguments to main
		for(int i = 0; i < posArgs.length; i++){
			cf.stack[i] = posArgs[i]; 
		}
		cf.stack[func.nformals-1] =  kwArgs; // new HashMap<String, IValue>();
		//cf.stack[func.nformals] = kwArgs == null ? new HashMap<String, IValue>() : kwArgs;
		Object o = executeProgram(root, cf);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		//return narrow(o); 
		return o;
	}
	
	public Frame makeFrameForVisit(FunctionInstance func){
		return new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
	}
	
	public IValue executeFunctionInVisit(Frame root){
		Frame cf = root;
		// Pass the subject argument
		
		Object o = executeProgram(root, cf);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return (IValue)o;
	}
	
	public IValue executeFunction(FunctionInstance func, IValue[] args){
		Frame root = new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
		Frame cf = root;
		
		// Pass the program arguments to main
		for(int i = 0; i < args.length; i++) {
			cf.stack[i] = args[i]; 
		}
		Object o = executeProgram(root, cf);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return narrow(o);
	}
	
	public IValue executeFunction(FunctionInstance func, Object[] args){
		Frame root = new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
		Frame cf = root;
		
		// Pass the program arguments to main
		for(int i = 0; i < args.length; i++) {
			cf.stack[i] = args[i]; 
		}
		Object o = executeProgram(root, cf);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return narrow(o); 
	}
	
	public IValue executeFunction(OverloadedFunctionInstance func, IValue[] args){
		Function firstFunc = functionStore.get(func.getFunctions()[0]); // TODO: null?
		int arity = args.length;
		int scopeId = func.env.scopeId;
		Frame root = new Frame(scopeId, null, func.env, arity+2, firstFunc);
		root.sp = arity;
		
		OverloadedFunctionInstanceCall c_ofun_call_next = 
				scopeId == -1 ? new OverloadedFunctionInstanceCall(root, func.getFunctions(), func.getConstructors(), root, null, arity)  // changed root to cf
        					  : OverloadedFunctionInstanceCall.computeOverloadedFunctionInstanceCall(root, func.getFunctions(), func.getConstructors(), scopeId, null, arity);
				
		Frame cf = c_ofun_call_next.nextFrame(functionStore);
		// Pass the program arguments to func
		for(int i = 0; i < args.length; i++) {
			cf.stack[i] = args[i]; 
		}
		cf.sp = args.length;
		cf.previousCallFrame = null;		// ensure that func will retrun here
		Object o = executeProgram(root, cf, /*arity,*/ /*cf.function.codeblock.getInstructions(),*/ c_ofun_call_next);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return narrow(o); 
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
	
	public IValue executeProgram(String moduleName, String uid_main, IValue[] args, HashMap<String,IValue> kwArgs) {
		
		String oldModuleName = rex.getCurrentModuleName();
		rex.setCurrentModuleName(moduleName);
		
		Function main_function = functionStore.get(functionMap.get(uid_main));

		if (main_function == null) {
			throw RascalRuntimeException.noMainFunction(null);
		}
		
//		if (main_function.nformals != 2) { // List of IValues and empty map of keyword parameters
//			throw new CompilerError("Function " + uid_main + " should have two arguments");
//		}
		
		Frame root = new Frame(main_function.scopeId, null, main_function.maxstack, main_function);
		Frame cf = root;
		//cf.stack[0] = vf.list(args); // pass the program argument to main_function as a IList object
		cf.stack[0] = kwArgs == null ? new HashMap<String, IValue>() : kwArgs;
		cf.src = main_function.src;
		
		Object o = executeProgram(root, cf);
		if(o != null && o instanceof Thrown){
			throw (Thrown) o;
		}
		IValue res = narrow(o);

		rex.setCurrentModuleName(oldModuleName);
		return res;
	}
	
	@SuppressWarnings("unchecked")
	protected int CHECKMEMO(Object[] stack, int sp, Frame cf){;
	
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
	
	int VISIT(boolean direction,  boolean progress, boolean fixedpoint, boolean rebuild, Object[] stack, int sp){
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
	
	Object VALUESUBTYPE(Type reqType, Object accu){
		return vf.bool(rex.isSubtypeOf(((IValue) accu).getType(), reqType));
	}
	
	Object LOADVAR(int varScope, int pos, Frame cf){
		if(CodeBlock.isMaxArg2(pos)){				
			return moduleVariables.get(cf.function.constantStore[varScope]);
		}
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {					
				return fr.stack[pos];
			}
		}
		throw new CompilerError("LOADVAR cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}
	
	Object LOADVARREF(int varScope, int pos, Frame cf){
		if(CodeBlock.isMaxArg2(pos)){				
			return moduleVariables.get(cf.function.constantStore[varScope]);
		}
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {					
				return new Reference(fr.stack, pos);
			}
		}
		throw new CompilerError("LOADVARREF cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}
	
	Object LOADVARDEREF(int varScope, int pos, Frame cf){
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {
				Reference ref = (Reference) fr.stack[pos];
				return ref.stack[ref.pos];
			}
		}
		throw new CompilerError("LOADVARDEREF cannot find matching scope: " + varScope, cf);
	}
	
	void STOREVAR(int varScope, int pos, Frame cf, Object accu){
		if(CodeBlock.isMaxArg2(pos)){
			IValue mvar = cf.function.constantStore[varScope];
			moduleVariables.put(mvar, (IValue)accu);
			return;
		}
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {
				// TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues
				fr.stack[pos] = accu;
				return;
			}
		}
		throw new CompilerError("STOREVAR cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}
	
	void RESETVAR(int varScope, int pos, Frame cf){
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
	
	int UNWRAPTHROWNVAR(int varScope, int pos, Frame cf, Object[] stack, int sp){
		if(CodeBlock.isMaxArg2(pos)){
			IValue mvar = cf.function.constantStore[varScope];
			moduleVariables.put(mvar, (IValue)stack[sp -1]);
			return sp;
		}
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {
				// TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues
				fr.stack[pos] = ((Thrown) stack[--sp]).value;
				return sp;
			}
		}
		throw new CompilerError("UNWRAPTHROWNVAR cannot find matching scope: " + varScope, cf);
	}
	
	void STOREVARDEREF(int varScope, int pos, Frame cf, Object accu){
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) { 
			if (fr.scopeId == varScope) {
				Reference ref = (Reference) fr.stack[pos];
				ref.stack[ref.pos] = accu;
			}
		}
		throw new CompilerError("STOREVARDEREF cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
	}
	
	@SuppressWarnings("unchecked")
	Object LOADVARKWP(int varScope, String name, Frame cf){
		for(Frame f = cf.previousScope; f != null; f = f.previousCallFrame) {
			if (f.scopeId == varScope) {	
				if(f.function.nformals > 0){
					Object okargs = f.stack[f.function.nformals - 1];
					if(okargs instanceof Map<?,?>){	// Not all frames provide kwargs, i.e. generated PHI functions.
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
	
	@SuppressWarnings("unchecked")
	void STOREVARKWP(int varScope, String name, Frame cf, Object accu){
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
							Entry<Type, IValue> defaultValue = defaults.get(name);
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
	@SuppressWarnings("unchecked")
	Object LOADLOCKWP(String name, Frame cf, Object[] stack){
		Map<String, Map.Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) stack[cf.function.nformals];
		Map.Entry<Type, IValue> defaultValue = defaults.get(name);
		Frame f = cf;
		
		// TODO: UNCOMMENT TO GET KEYWORD PARAMETER PROPAGATION
		//for(Frame f = cf; f != null; f = f.previousCallFrame) {
			int nf = f.function.nformals;
			if(nf > 0){								// Some generated functions have zero args, i.e. EQUIVALENCE
				Object okargs = f.stack[nf - 1];
				if(okargs instanceof Map<?,?>){	// Not all frames provide kwargs, i.e. generated PHI functions.
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
	
	@SuppressWarnings("unchecked")
	int CALLCONSTR(Type constructor, int arity, Object[] stack, int sp){
		IValue[] args = new IValue[constructor.getArity()];

		java.util.Map<String,IValue> kwargs;
		Type type = (Type) stack[--sp];
		//if(type.getArity() > 0){
			// Constructors with keyword parameters
			kwargs = (java.util.Map<String,IValue>) stack[--sp];
		//} else {
		//	kwargs = new HashMap<String,IValue>();
		//}

		for(int i = 0; i < constructor.getArity(); i++) {
			args[constructor.getArity() - 1 - i] = (IValue) stack[--sp];
		}
		stack[sp++] = vf.constructor(constructor, args, kwargs);
		return sp;
	}
	
	int APPLY(Function fun, int arity, Frame root, Frame cf, Object[] stack, int sp){
		assert arity <= fun.nformals : "APPLY, too many arguments at " + cf.src;
		assert fun.scopeIn == -1 : "APPLY, illegal scope at " + cf.src;
		FunctionInstance fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
		sp = sp - arity;
		stack[sp++] = fun_instance;
		return sp;
	}
	
	int APPLYDYN(int arity, Frame cf, Object[] stack, int sp){
		FunctionInstance fun_instance;
		Object src = stack[--sp];
		if(src instanceof FunctionInstance) {
			fun_instance = (FunctionInstance) src;
			assert arity + fun_instance.next <= fun_instance.function.nformals : "APPLYDYN, too many arguments at " + cf.src;
			fun_instance = fun_instance.applyPartial(arity, stack, sp);
		} else {
			throw new CompilerError("Unexpected argument type for APPLYDYN: " + asString(src), cf);
		}
		sp = sp - arity;
		stack[sp++] = fun_instance;
		return sp;
	}

	int PRINTLN(int arity, Object[] stack, int sp){
		StringBuilder w = new StringBuilder();
		for(int i = arity - 1; i >= 0; i--){
			String str = (stack[sp - 1 - i] instanceof IString) ? ((IString) stack[sp - 1 - i]).toString() : asString(stack[sp - 1 - i]);
			w.append(str).append(" ");
		}
		stdout.println(w.toString());
		sp = sp - arity + 1;
		return sp;
	}
	
	private Object executeProgram(Frame root, Frame cf) {
		return executeProgram(root, cf, null);
	}
	
	@SuppressWarnings("unchecked")
	private Object executeProgram(final Frame root, Frame cf, OverloadedFunctionInstanceCall c_ofun_call) {
		Object[] stack = cf.stack;		                              	// current stack
		int sp = cf.function.getNlocals();				                  	// current stack pointer
		long [] instructions = cf.function.codeblock.getInstructions(); 	// current instruction sequence
		int pc = 0;				                                      	// current program counter
		int postOp = 0;													// postprocessing operator (following main switch)
		int pos = 0;
		ArrayList<Frame> stacktrace = new ArrayList<Frame>();
		Thrown thrown = null;
		int arity;
		long instruction;
		int op;
		Object rval;
		
		Object accu = null;
		
		// Overloading specific
		Stack<OverloadedFunctionInstanceCall> ocalls = new Stack<OverloadedFunctionInstanceCall>();
		if(c_ofun_call != null){
			ocalls.push(c_ofun_call);
		}
		//OverloadedFunctionInstanceCall c_ofun_call = null;
		
		frameObserver.enter(cf);
		 
		try {
			NEXT_INSTRUCTION: while (true) {
				
				frameObserver.observeRVM(this, cf, pc, stack, sp, accu);
				
				instruction = instructions[pc++];
				op = CodeBlock.fetchOp(instruction);
				
				String name;
				
				INSTRUCTION: switch (op) {
				
				case Opcode.OP_PUSHACCU:
					stack[sp++] = accu;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_POPACCU:
					accu = stack[--sp];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_POP:
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC0:
//					assert 0 < cf.function.nlocals : "LOADLOC0: pos larger that nlocals at " + cf.src;
//					assert stack[0] != null: "Local variable 0 is null";
					accu = stack[0]; //if(a == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC1:
//					assert 1 < cf.function.nlocals : "LOADLOC1: pos larger that nlocals at " + cf.src;
//					assert stack[1] != null: "Local variable 1 is null";
					accu = stack[1]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION; 
					
				case Opcode.OP_LOADLOC2:
//					assert 2 < cf.function.nlocals : "LOADLOC2: pos larger that nlocals at " + cf.src;
//					assert stack[2] != null: "Local variable 2 is null";
					accu = stack[2]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION; 
					
				case Opcode.OP_LOADLOC3:
//					assert 3 < cf.function.nlocals : "LOADLOC3: pos larger that nlocals at " + cf.src;
//					assert stack[3] != null: "Local variable 3 is null";
					accu = stack[3]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC4:
//					assert 4 < cf.function.nlocals : "LOADLOC4: pos larger that nlocals at " + cf.src;
//					assert stack[4] != null: "Local variable 4 is null";
					accu = stack[4]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC5:
//					assert 5 < cf.function.nlocals : "LOADLOC5: pos larger that nlocals at " + cf.src;
//					assert stack[5] != null: "Local variable 5 is null";
					accu = stack[5]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC6:
//					assert 6 < cf.function.nlocals : "LOADLOC6: pos larger that nlocals at " + cf.src;
//					assert stack[6] != null: "Local variable 6 is null";
					accu = stack[6]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC7:
//					assert 7 < cf.function.nlocals : "LOADLOC7: pos larger that nlocals at " + cf.src;
//					assert stack[7] != null: "Local variable 7 is null";
					accu = stack[7]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC8:
//					assert 8 < cf.function.nlocals : "LOADLOC8: pos larger that nlocals at " + cf.src;
//					assert stack[8] != null: "Local variable 8 is null";
					accu = stack[8]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC9:
//					assert 9 < cf.function.nlocals : "LOADLOC9: pos larger that nlocals at " + cf.src;
//					assert stack[9] != null: "Local variable 9 is null";
					accu = stack[9]; // if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADLOC:
					pos = CodeBlock.fetchArg1(instruction);
//					assert pos < cf.function.nlocals : "LOADLOC: pos larger that nlocals at " + cf.src;
//					assert stack[pos] != null: "Local variable " + pos + " is null";
					accu = stack[pos]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_PUSHLOC:
					pos = CodeBlock.fetchArg1(instruction);
//					assert pos < cf.function.nlocals : "LOADLOC: pos larger that nlocals at " + cf.src;
//					assert stack[pos] != null: "Local variable " + pos + " is null";
					accu = stack[pos]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					stack[sp++] = accu;
					continue NEXT_INSTRUCTION;	
					
				case Opcode.OP_RESETLOCS:
					IList positions = (IList) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					for(IValue v : positions){
						stack[((IInteger) v).intValue()] = null;
					}
					//stack[sp++] = Rascal_TRUE;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_RESETLOC:	
					stack[CodeBlock.fetchArg1(instruction)] = null;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADBOOL:
					accu = CodeBlock.fetchArg1(instruction) == 1 ? Rascal_TRUE : Rascal_FALSE;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADINT:
					accu = CodeBlock.fetchArg1(instruction);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADCON:
					accu = cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCON:
					accu = stack[sp++] = cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOCREF:
					accu = new Reference(stack, CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHLOCREF:
					stack[sp++] = new Reference(stack, CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHEMPTYKWMAP:
					// TODO: use unique copy of emptyKeywordMap and delay creation of new copy to assignment
					// to keyword parameter
					//stack[sp++] = emptyKeywordMap;
					stack[sp++] = new HashMap<String,IValue>();
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_CALLMUPRIM0:	
					accu = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute0();
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLMUPRIM0:	
					stack[sp++] = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute0();
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLMUPRIM1:	
					accu = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute1(accu);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLMUPRIM1:	
					stack[sp++] = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute1(accu);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLMUPRIM2:	
					accu = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute2(stack[sp - 1], accu);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLMUPRIM2:	
					stack[sp - 1] = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute2(stack[sp - 1], accu);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLMUPRIMN:
					sp = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].executeN(stack, sp, CodeBlock.fetchArg2(instruction));
					accu = stack[--sp];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLMUPRIMN:
					sp = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].executeN(stack, sp, CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;	
				
				case Opcode.OP_JMP:
					pc = CodeBlock.fetchArg1(instruction);
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPTRUE:
					if (((IBool) accu).getValue()) {
						pc = CodeBlock.fetchArg1(instruction);
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPFALSE:
					if (!((IBool) accu).getValue()) {
						pc = CodeBlock.fetchArg1(instruction);
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_TYPESWITCH:
					IValue val = (IValue) stack[--sp];
					Type t = null;
					if(val instanceof IConstructor) {
						t = ((IConstructor) val).getConstructorType();
					} else {
						t = val.getType();
					}
					int labelIndex = ToplevelType.getToplevelTypeAsInt(t);
					IList labels = (IList) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					pc = ((IInteger) labels.get(labelIndex)).intValue();
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_SWITCH:
					val = (IValue) stack[--sp];
					IMap caseLabels = (IMap) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					int caseDefault = CodeBlock.fetchArg2(instruction);
					boolean useConcreteFingerprint = instructions[pc++] == 1;
					IInteger fp = vf.integer(ToplevelType.getFingerprint(val, useConcreteFingerprint));
					
					IInteger x = (IInteger) caseLabels.get(fp);
					//stdout.println("SWITCH: fp = " + fp  + ", val = " + val + ", x = " + x + ", useConcreteFingerprint = " + useConcreteFingerprint);
					if(x == null){
							//stack[sp++] = vf.bool(false);
							pc = caseDefault;
					} else {
						pc = x.intValue();
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADTYPE:
					accu = cf.function.typeConstantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHTYPE:
					stack[sp++] = cf.function.typeConstantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;	 
					
				case Opcode.OP_LOADLOCDEREF: {
					Reference ref = (Reference) stack[CodeBlock.fetchArg1(instruction)];
					accu = ref.stack[ref.pos];
					continue NEXT_INSTRUCTION;
				}
				case Opcode.OP_PUSHLOCDEREF: {
					Reference ref = (Reference) stack[CodeBlock.fetchArg1(instruction)];
					stack[sp++] = ref.stack[ref.pos];
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_STORELOC:
					pos = CodeBlock.fetchArg1(instruction);
					assert pos < cf.function.getNlocals() : "STORELOC: pos larger that nlocals at " + cf.src;
					stack[pos] = accu;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_STORELOCDEREF:
					Reference ref = (Reference) stack[CodeBlock.fetchArg1(instruction)];
					ref.stack[ref.pos] = accu; // TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues    
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_PUSH_ROOT_FUN:
					// Loads functions that are defined at the root
					stack[sp++] = new FunctionInstance(functionStore.get(CodeBlock.fetchArg1(instruction)), root, this);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSH_NESTED_FUN: { 
					// Loads nested functions and closures (anonymous nested functions)
					stack[sp++] = FunctionInstance.computeFunctionInstance(functionStore.get(CodeBlock.fetchArg1(instruction)), cf, CodeBlock.fetchArg2(instruction), this);
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_PUSHOFUN:
					OverloadedFunction of = overloadedStore.get(CodeBlock.fetchArg1(instruction));
					stack[sp++] = of.scopeIn == -1 ? new OverloadedFunctionInstance(of.functions, of.constructors, root, functionStore, constructorStore, this)
					                               : OverloadedFunctionInstance.computeOverloadedFunctionInstance(of.functions, of.constructors, cf, of.scopeIn, functionStore, constructorStore, this);
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_PUSHCONSTR:
					Type constructor = constructorStore.get(CodeBlock.fetchArg1(instruction));  
					stack[sp++] = constructor;
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADVAR:
					 accu = LOADVAR(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf);
					 if(accu == null){ 
						 postOp = Opcode.POSTOP_CHECKUNDEF; break; 
					 }
					 continue NEXT_INSTRUCTION;
					 
				case Opcode.OP_PUSHVAR:
					 accu = LOADVAR(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf);
					 if(accu == null){ 
						 postOp = Opcode.POSTOP_CHECKUNDEF; break; 
					 }
					 stack[sp++] = accu;
					 continue NEXT_INSTRUCTION;
					 
				case Opcode.OP_RESETVAR:
					RESETVAR(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADVARREF: 
					accu = LOADVARREF(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHVARREF: 
					stack[sp++] = LOADVARREF(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf);
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADVARDEREF: 
					accu = LOADVARDEREF(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf);
					if(accu == null){ 
						postOp = Opcode.POSTOP_CHECKUNDEF; break;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHVARDEREF: 
					accu = LOADVARDEREF(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf);
					if(accu == null){ 
						postOp = Opcode.POSTOP_CHECKUNDEF; break;
					}
					stack[sp++] = accu;
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_STOREVAR:
					STOREVAR(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf, accu);
					continue NEXT_INSTRUCTION;
						
				case Opcode.OP_STOREVARDEREF:
					STOREVARDEREF(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf, accu);
					continue NEXT_INSTRUCTION;
									
				case Opcode.OP_CALLCONSTR:
					sp = CALLCONSTR(constructorStore.get(CodeBlock.fetchArg1(instruction)), CodeBlock.fetchArg2(instruction), stack, sp);
					continue NEXT_INSTRUCTION;
										
				case Opcode.OP_CALLDYN:				
				case Opcode.OP_CALL:
					if(!frameObserver.observe(cf)){
						return Rascal_FALSE;
					}
					// In case of CALLDYN, the stack top value of type 'Type' leads to a constructor call
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof Type) {
						Type constr = (Type) stack[--sp];
						arity = constr.getArity();
						IValue[] args = new IValue[arity]; 
						for(int i = arity - 1; i >= 0; i--) {
							args[i] = (IValue) stack[sp - arity + i];
						}
						sp = sp - arity;
						stack[sp++] = vf.constructor(constr, args);
						continue NEXT_INSTRUCTION;
					}
					
					cf.pc = pc;
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof FunctionInstance){
						FunctionInstance fun_instance = (FunctionInstance) stack[--sp];
						arity = CodeBlock.fetchArg1(instruction);
						// In case of partial parameter binding
						if(fun_instance.next + arity < fun_instance.function.nformals) {
							fun_instance = fun_instance.applyPartial(arity, stack, sp);
							sp = sp - arity;
						    stack[sp++] = fun_instance;
						    continue NEXT_INSTRUCTION;
						}
						cf = cf.getFrame(fun_instance.function, fun_instance.env, fun_instance.args, arity, sp);
					} else if(op == Opcode.OP_CALL) {
						Function fun = functionStore.get(CodeBlock.fetchArg1(instruction));
						arity = CodeBlock.fetchArg2(instruction);
						// In case of partial parameter binding
						if(arity < fun.nformals) {
							FunctionInstance fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
							sp = sp - arity;
						    stack[sp++] = fun_instance;
						    continue NEXT_INSTRUCTION;
						}
						cf = cf.getFrame(fun, root, arity, sp);
						
					} else {
						throw new CompilerError("Unexpected argument type for CALLDYN: " + asString(stack[sp - 1]), cf);
					}
					
					frameObserver.enter(cf);
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_OCALLDYN:
				case Opcode.OP_OCALL:					
					Object funcObject = (op == Opcode.OP_OCALLDYN) ? stack[--sp] : null;
					// Get function arguments from the stack
					arity = CodeBlock.fetchArg2(instruction);
					
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					if(!frameObserver.observe(cf)){
						return Rascal_FALSE;
					}
					cf.sp = sp;
					cf.pc = pc;
					
					OverloadedFunctionInstanceCall c_ofun_call_next = null;
					
					if(op == Opcode.OP_OCALLDYN) {
						// Get function types to perform a type-based dynamic resolution
						Type types = cf.function.codeblock.getConstantType(CodeBlock.fetchArg1(instruction));
						// Objects of three types may appear on the stack:
						// 	1. FunctionInstance due to closures
						if(funcObject instanceof FunctionInstance) {
							FunctionInstance fun_instance = (FunctionInstance) funcObject;
							cf = cf.getFrame(fun_instance.function, fun_instance.env, arity, sp);
							instructions = cf.function.codeblock.getInstructions();
							stack = cf.stack;
							sp = cf.sp;
							pc = cf.pc;
							frameObserver.enter(cf);;
							continue NEXT_INSTRUCTION;
						}
					 	// 2. OverloadedFunctionInstance due to named Rascal functions
						OverloadedFunctionInstance of_instance = (OverloadedFunctionInstance) funcObject;
						c_ofun_call_next = new OverloadedFunctionInstanceCall(cf, of_instance.getFunctions(), of_instance.getConstructors(), of_instance.env, types, arity);
					} else {
						of = overloadedStore.get(CodeBlock.fetchArg1(instruction));
						Object arg0 = stack[sp - arity];
						c_ofun_call_next = of.scopeIn == -1 ? new OverloadedFunctionInstanceCall(cf, of.getFunctions(arg0), of.getConstructors(arg0), cf, null, arity)  // changed root to cf
								                            : OverloadedFunctionInstanceCall.computeOverloadedFunctionInstanceCall(cf, of.getFunctions(arg0), of.getConstructors(arg0), of.scopeIn, null, arity);
					}
					
					if(ocall_debug) {
						if(op == Opcode.OP_OCALL) {
							stdout.println("OVERLOADED FUNCTION CALL: " + getOverloadedFunctionName(CodeBlock.fetchArg1(instruction)));
						} else {
							stdout.println("OVERLOADED FUNCTION CALLDYN: ");
						}
						stdout.println("	with alternatives:");
						for(int index : c_ofun_call_next.getFunctions()) {
							stdout.println("		" + getFunctionName(index));
						}
						stdout.flush();
					}
					
					Frame frame = c_ofun_call_next.nextFrame(functionStore);
					
					if(frame != null) {
						c_ofun_call = c_ofun_call_next;
						ocalls.push(c_ofun_call);
					
						if(ocall_debug){ stdout.println("		" + "try alternative: " + frame.function.name); stdout.flush();}

						cf = frame;
						frameObserver.enter(cf);
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
					} else {
						constructor = c_ofun_call_next.nextConstructor(constructorStore);
						
//					    if(constructor instanceof NonTerminalType){
//								NonTerminalType nt = (NonTerminalType) constructor;
//							IConstructor symbol = nt.getSymbol();
//							Type parameters = (Type) symbol.get("parameters");
//							Type attributes = (Type) symbol.get("attributes");
//							constructor = tf.constructor(rex.getTypeStore(), Factory.Production_Default, "prod", symbol, "sort", parameters, "parameters",  attributes, "attributes");
//						}
						sp = sp - arity;
						stack[sp++] = vf.constructor(constructor, c_ofun_call_next.getConstructorArguments(constructor.getArity()));
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CHECKARGTYPEANDCOPY:
					pos = CodeBlock.fetchArg1(instruction);
					Type argType = ((IValue) stack[pos]).getType();
					Type paramType = cf.function.typeConstantStore[CodeBlock.fetchArg2(instruction)];
					
					int pos2 = (int) instructions[pc++];
					
					if(argType.isSubtypeOf(paramType)){
						stack[pos2] = stack[pos];
						accu = vf.bool(true);
						continue NEXT_INSTRUCTION;
					}
					if(argType instanceof RascalType){
						RascalType atype = (RascalType) argType;
						RascalType ptype = (RascalType) paramType;
						if(ptype.isNonterminal() &&  atype.isSubtypeOfNonTerminal(ptype)){
							stack[pos2] = stack[pos];
							accu = vf.bool(true);
							continue NEXT_INSTRUCTION;
						}
					}
						
					accu = vf.bool(false);
					//System.out.println("OP_CHECKARGTYPEANDCOPY: " + argType + ", " + paramType + " => false");
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_FAILRETURN:
					assert cf.previousCallFrame == c_ofun_call.cf : "FAILRETURN, incorrect frame at" + cf.src;
					
					frame = c_ofun_call.nextFrame(functionStore);				
					if(frame != null) {
						
						if(ocall_debug){ stdout.println("		" + "try alternative: " + frame.function.name); stdout.flush(); }
						
						cf = frame;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
					} else {
						cf = c_ofun_call.cf;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
						constructor = c_ofun_call.nextConstructor(constructorStore);
						stack[sp++] = vf.constructor(constructor, c_ofun_call.getConstructorArguments(constructor.getArity()));
						ocalls.pop();
						c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_VISIT:
					boolean direction = ((IBool) cf.function.constantStore[CodeBlock.fetchArg1(instruction)]).getValue();
					boolean progress = ((IBool) cf.function.constantStore[CodeBlock.fetchArg2(instruction)]).getValue();
					boolean fixedpoint = ((IBool) cf.function.constantStore[(int)instructions[pc++]]).getValue();
					boolean rebuild = ((IBool) cf.function.constantStore[(int)instructions[pc++]]).getValue();
					sp = VISIT(direction, progress, fixedpoint, rebuild, stack, sp);
					if(sp > 0){
						continue NEXT_INSTRUCTION;
					}
					// Fall through to force a function return;
					sp = -sp;
					op = Opcode.OP_RETURN1;
					
				case Opcode.OP_FILTERRETURN:
				case Opcode.OP_RETURN0:
				case Opcode.OP_RETURN1:
					
					// Overloading specific
					if(c_ofun_call != null && cf.previousCallFrame == c_ofun_call.cf) {
						ocalls.pop();
						c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
					}
				
					rval = null;
					boolean returns = cf.isCoroutine || op == Opcode.OP_RETURN1 || op == Opcode.OP_FILTERRETURN;
					if(op == Opcode.OP_RETURN1 || cf.isCoroutine) {
						if(cf.isCoroutine) {
							rval = Rascal_TRUE;
							if(op == Opcode.OP_RETURN1) {
								arity = CodeBlock.fetchArg1(instruction);
								int[] refs = cf.function.refs;
								if(arity != refs.length) {
									throw new CompilerError("Coroutine " + cf.function.name + ": arity of return (" + arity  + ") unequal to number of reference parameters (" +  refs.length + ")", cf);
								}
								for(int i = 0; i < arity; i++) {
									ref = (Reference) stack[refs[arity - 1 - i]];
									ref.stack[ref.pos] = stack[--sp];
								}
							}
						} else {
							rval = stack[sp - 1];
						}
					}
					assert sp == ((op == Opcode.OP_RETURN0) ? cf.function.getNlocals() : cf.function.getNlocals() + 1)
							: "On return from " + cf.function.name + ": " + (sp - cf.function.getNlocals()) + " spurious stack elements";
					
					// if the current frame is the frame of a top active coroutine, 
					// then pop this coroutine from the stack of active coroutines
					if(cf == ccf) {
						activeCoroutines.pop();
						ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					}
					
					frameObserver.leave(cf, rval);
					cf = cf.previousCallFrame;
					
					if(cf == null) {
						if(returns) {
							return rval; 
						} else { 
							return NONE;
						}
					}
					
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					if(returns) {
						stack[sp++] = rval;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLJAVA:
					String methodName =  ((IString) cf.function.constantStore[(int) instructions[pc++]]).getValue();
					String className =  ((IString) cf.function.constantStore[(int) instructions[pc++]]).getValue();
					Type parameterTypes = cf.function.typeConstantStore[(int) instructions[pc++]];
					Type keywordTypes = cf.function.typeConstantStore[(int) instructions[pc++]];
					int reflect = (int) instructions[pc++];
					arity = parameterTypes.getArity();
					try {
						//int sp1 = sp;
					    sp = callJavaMethod(methodName, className, parameterTypes, keywordTypes, reflect, stack, sp);
					    //assert sp == sp1 - arity + 1;
					} catch (Throw e) {
						stacktrace.add(cf);
						thrown = Thrown.getInstance(e.getException(), e.getLocation(), cf);
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					} catch (Thrown e){
						stacktrace.add(cf);
						thrown = e;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					} catch (Throwable e){
						thrown = Thrown.getInstance(e, cf.src, cf);
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					} 
					
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_CREATE:
				case Opcode.OP_CREATEDYN:
					if(op == Opcode.OP_CREATE) {
						cccf = cf.getCoroutineFrame(functionStore.get(CodeBlock.fetchArg1(instruction)), root, CodeBlock.fetchArg2(instruction), sp);
					} else {
						arity = CodeBlock.fetchArg1(instruction);
						Object src = stack[--sp];
						if(src instanceof FunctionInstance) {
							// In case of partial parameter binding
							FunctionInstance fun_instance = (FunctionInstance) src;
							cccf = cf.getCoroutineFrame(fun_instance, arity, sp);
						} else {
							throw new CompilerError("Unexpected argument type for INIT: " + src.getClass() + ", " + src, cf);
						}
					}
					sp = cf.sp;
					// Instead of suspending a coroutine instance during INIT, execute it until GUARD;
					// Let INIT postpone creation of an actual coroutine instance (delegated to GUARD), which also implies no stack management of active coroutines until GUARD;
					cccf.previousCallFrame = cf;
					
					cf.sp = sp;
					cf.pc = pc;
					instructions = cccf.function.codeblock.getInstructions();
					cf = cccf;
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_GUARD:
					rval = stack[sp - 1];
					boolean precondition;
					if(rval instanceof IBool) {
						precondition = ((IBool) rval).getValue();
//					} else if(rval instanceof Boolean) {
//						precondition = (Boolean) rval;
					} else {
						throw new CompilerError("Guard's expression has to be boolean!", cf);
					}
					
					if(cf == cccf) {
						Coroutine coroutine = null;
						Frame prev = cf.previousCallFrame;
						if(precondition) {
							coroutine = new Coroutine(cccf);
							coroutine.isInitialized = true;
							coroutine.suspend(cf);
						}
						cccf = null;
						--sp;
						cf.pc = pc;
						cf.sp = sp;
						cf = prev;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
						stack[sp++] = precondition ? coroutine : exhausted;
						continue NEXT_INSTRUCTION;
					}
					
					if(!precondition) {
						cf.pc = pc;
						cf.sp = sp;
						cf = cf.previousCallFrame;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
						stack[sp++] = Rascal_FALSE;
						continue NEXT_INSTRUCTION;
					}
					--sp;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_APPLY:
					sp =  APPLY(functionStore.get(CodeBlock.fetchArg1(instruction)), CodeBlock.fetchArg2(instruction), root, cf, stack, sp);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_APPLYDYN:
					sp = APPLYDYN(CodeBlock.fetchArg1(instruction), cf, stack, sp);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_NEXT0:
				case Opcode.OP_NEXT1:
					Coroutine coroutine = (Coroutine) stack[--sp];
					
					// Merged the hasNext and next semantics
					if(!coroutine.hasNext()) {
						if(op == Opcode.OP_NEXT1) {
							--sp;
						}
						stack[sp++] = Rascal_FALSE;
						continue NEXT_INSTRUCTION;
					}
					// put the coroutine onto the stack of active coroutines
					activeCoroutines.push(coroutine);
					ccf = coroutine.start;
					coroutine.next(cf);
					
					instructions = coroutine.frame.function.codeblock.getInstructions();
				
					coroutine.frame.stack[coroutine.frame.sp++] = 		// Always leave an entry on the stack
							(op == Opcode.OP_NEXT1) ? stack[--sp] : null;
					
					cf.pc = pc;
					cf.sp = sp;
					
					cf = coroutine.frame;
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_YIELD0:	
				case Opcode.OP_YIELD1:
					coroutine = activeCoroutines.pop();
					ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					Frame prev = coroutine.start.previousCallFrame;
					rval = Rascal_TRUE; // In fact, yield has to always return TRUE
					if(op == Opcode.OP_YIELD1) {
						arity = CodeBlock.fetchArg1(instruction);
						int[] refs = cf.function.refs; 
						
						if(arity != refs.length) {
							throw new CompilerError("The 'yield' within a coroutine has to take the same number of arguments as the number of its reference parameters; arity: " + arity + "; reference parameter number: " + refs.length, cf);
						}
						
						for(int i = 0; i < arity; i++) {
							ref = (Reference) stack[refs[arity - 1 - i]]; // Takes the reference parameters of the top active coroutine instance
							ref.stack[ref.pos] = stack[--sp];
						}
					}
					cf.pc = pc;
					cf.sp = sp;
					coroutine.suspend(cf);
					cf = prev;
					if(op == Opcode.OP_YIELD1 && cf == null) {
						return rval;
					}
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					stack[sp++] = rval;	 								// Corresponding next will always find an entry on the stack
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_EXHAUST:
					if(cf == ccf) {
						activeCoroutines.pop();
						ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					}
					
					cf = cf.previousCallFrame;
					if(cf == null) {
						return Rascal_FALSE;    // 'Exhaust' has to always return FALSE, i.e., signal a failure;
					}
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					stack[sp++] = Rascal_FALSE; // 'Exhaust' has to always return FALSE, i.e., signal a failure;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLPRIMN:
					arity = CodeBlock.fetchArg2(instruction);
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						sp = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].executeN(stack, sp, CodeBlock.fetchArg2(instruction), cf, rex);
						accu = stack[--sp];
					} catch (Thrown exception) {
						thrown = exception;
						sp = sp - arity;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLPRIMN:
					arity = CodeBlock.fetchArg2(instruction);
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						sp = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].executeN(stack, sp, CodeBlock.fetchArg2(instruction), cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						sp = sp - arity;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLPRIM0:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						accu = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute0(cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLPRIM0:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						stack[sp++] = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute0(cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLPRIM1:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						accu = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute1(accu, cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLPRIM1:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						stack[sp++] = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute1(accu, cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLPRIM2:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						accu = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute2(stack[sp - 1], accu, cf, rex);
						sp--;
					} catch (Thrown exception) {
						thrown = exception;
						sp = sp - 1;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLPRIM2:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						stack[sp - 1] = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute2(stack[sp - 1], accu, cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						sp = sp - 1;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_UNWRAPTHROWNLOC: {
					pos = CodeBlock.fetchArg1(instruction);
					assert pos < cf.function.getNlocals() : "UNWRAPTHROWNLOC: pos larger that nlocals at " + cf.src;
					stack[pos] = ((Thrown) stack[--sp]).value;
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_UNWRAPTHROWNVAR:
					sp = UNWRAPTHROWNVAR(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf, stack, sp);
					continue NEXT_INSTRUCTION;
					
				// Some specialized MuPrimitives
					
				case Opcode.OP_SUBSCRIPTARRAY:
					accu = ((Object[]) stack[sp - 1])[((Integer) accu)];
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBSCRIPTLIST:
					accu = ((IList) stack[sp - 1]).get((Integer) accu);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LESSINT:
					accu = vf.bool(((Integer) stack[sp - 1]) < ((Integer) accu));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_GREATEREQUALINT:
					accu = vf.bool(((Integer) stack[sp - 1]) >= ((Integer) accu));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_ADDINT:
					accu = ((Integer) stack[sp - 1]) + ((Integer) accu);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBTRACTINT:
					accu = ((Integer) stack[sp - 1]) - ((Integer) accu);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_ANDBOOL:
					accu = ((IBool) stack[sp - 1]).and((IBool) accu);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_TYPEOF:
					if(accu instanceof HashSet<?>){	// For the benefit of set matching
						HashSet<IValue> mset = (HashSet<IValue>) accu;
						if(mset.isEmpty()){
							accu = tf.setType(tf.voidType());
						} else {
							IValue v = mset.iterator().next();
							accu = tf.setType(v.getType());
						}
					} else {
						accu = ((IValue) accu).getType();
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBTYPE:
					accu = vf.bool(((Type) stack[sp - 1]).isSubtypeOf((Type) accu));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_VALUESUBTYPE:
					accu = VALUESUBTYPE(cf.function.typeConstantStore[CodeBlock.fetchArg1(instruction)], accu);
					continue NEXT_INSTRUCTION;
								
				case Opcode.OP_LABEL:
					throw new CompilerError("LABEL instruction at runtime", cf);
					
				case Opcode.OP_HALT:
					return stack[sp - 1];

				case Opcode.OP_PRINTLN:
					sp =  PRINTLN(CodeBlock.fetchArg1(instruction), stack, sp);
					continue NEXT_INSTRUCTION;	
					
				case Opcode.OP_THROW:
					Object obj = stack[--sp];
					thrown = null;
					cf.src = (ISourceLocation) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					frameObserver.observe(cf);
					
					if(obj instanceof IValue) {
						//stacktrace = new ArrayList<Frame>();
						//stacktrace.add(cf);
						thrown = Thrown.getInstance((IValue) obj, null, cf);
					} else {
						// Then, an object of type 'Thrown' is on top of the stack
						thrown = (Thrown) obj;
					}
					postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
					break INSTRUCTION;
					
				case Opcode.OP_LOADLOCKWP:
					accu = LOADLOCKWP(((IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg1(instruction))).getValue(), cf, stack);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHLOCKWP:
					stack[sp++] = LOADLOCKWP(((IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg1(instruction))).getValue(), cf, stack);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADVARKWP:
					accu = LOADVARKWP(CodeBlock.fetchArg1(instruction), ((IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg2(instruction))).getValue(), cf);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHVARKWP:
					stack[sp++] = LOADVARKWP(CodeBlock.fetchArg1(instruction), ((IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg2(instruction))).getValue(), cf);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_STORELOCKWP:
					val = (IValue) accu;
					name = ((IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg1(instruction))).getValue();
					Map<String, IValue> kargs = (Map<String, IValue>) stack[cf.function.nformals - 1];
					if(kargs == emptyKeywordMap){
						System.err.println("Creating new kw map while updating: " + name);
						kargs = new HashMap<>();
						stack[cf.function.nformals - 1] = kargs;
					}
					kargs.put(name, val);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_STOREVARKWP:
					STOREVARKWP(CodeBlock.fetchArg1(instruction), 
									 ((IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg2(instruction))).getValue(),
		 	  						 cf, accu);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CHECKMEMO:
					sp = CHECKMEMO(stack, sp, cf);
					if(sp > 0){
						continue NEXT_INSTRUCTION;
					}
					sp = - sp;
					op = Opcode.OP_RETURN1;
					
					// Specialized copy of RETURN code
					
					// Overloading specific
					if(c_ofun_call != null && cf.previousCallFrame == c_ofun_call.cf) {
						ocalls.pop();
						c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
					}
					
					rval = stack[sp - 1];

					assert sp ==  cf.function.getNlocals() + 1
							: "On return from " + cf.function.name + ": " + (sp - cf.function.getNlocals()) + " spurious stack elements";
					
					// if the current frame is the frame of a top active coroutine, 
					// then pop this coroutine from the stack of active coroutines
					if(cf == ccf) {
						activeCoroutines.pop();
						ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					}
					
					if(!frameObserver.leave(cf,  rval)){
						return Rascal_FALSE;
					}
					cf = cf.previousCallFrame;
					
					if(cf == null) {
						return rval; 
					}
					
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					stack[sp++] = rval;
					continue NEXT_INSTRUCTION;
								
				default:
					throw new CompilerError("RVM main loop -- cannot decode instruction", cf);
				}
				
				switch(postOp){
				
				case Opcode.POSTOP_CHECKUNDEF:
				case Opcode.POSTOP_HANDLEEXCEPTION:
					// EXCEPTION HANDLING
					if(postOp == Opcode.POSTOP_CHECKUNDEF) {
						//stacktrace = new ArrayList<Frame>();
						//stacktrace.add(cf);
						thrown = RascalRuntimeException.uninitializedVariable("name to be provided", cf);
					}
					cf.pc = pc;
					// First, try to find a handler in the current frame function,
					// given the current instruction index and the value type,
					// then, if not found, look up the caller function(s)
					
					for(Frame f = cf; f != null; f = f.previousCallFrame) {
						int handler = f.function.getHandler(f.pc - 1, thrown.value.getType());
						if(handler != -1) {
							int fromSP = f.function.getFromSP();
							if(f != cf) {
								cf = f;
								instructions = cf.function.codeblock.getInstructions();
								stack = cf.stack;
								sp = cf.sp;
								pc = cf.pc;
							}
							pc = handler;
							sp = fromSP;
							stack[sp++] = thrown;
							thrown = null;
							continue NEXT_INSTRUCTION;
						}
						if(c_ofun_call != null && f.previousCallFrame == c_ofun_call.cf) {
							ocalls.pop();
							c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
						}
					}
					// If a handler has not been found in the caller functions...
//					stdout.println("EXCEPTION " + thrown + " at: " + cf.src);
//					for(Frame f = cf; f != null; f = f.previousCallFrame) {
//						stdout.println("\t" + f.toString());
//					}
//					stdout.flush();
					if(frameObserver.exception(cf, thrown)){
						continue NEXT_INSTRUCTION;
					} else {
						return thrown;
					}
				}
				
			}
		}
		catch (Thrown e) {
			throw e; 
			// this is a normal Rascal exception, but we want to handle the next case here exceptionally, which 
			// should not happen normally and hints at a compiler or run-time bug:
		}
		catch (Exception e) {
			stdout.println("EXCEPTION " + e + " at: " + cf.src);
			for(Frame f = cf; f != null; f = f.previousCallFrame) {
				stdout.println("\t" + f.toString());
			}
			stdout.flush();
			e.printStackTrace(stderr);
			stderr.flush();
			String e2s = (e instanceof CompilerError) ? e.getMessage() : e.toString();
			throw new CompilerError(e2s + "; function: " + cf + "; instruction: " + cf.function.codeblock.toString(pc - 1), cf );
		}
	}
	
	public Class<?> getJavaClass(String className){
		Class<?> clazz = classCache.get(className);
		if(clazz != null){
			return clazz;
		}
		try {
			clazz = this.getClass().getClassLoader().loadClass(className);
		} catch(ClassNotFoundException e1) {
			// If the class is not found, try other class loaders
			for(ClassLoader loader : this.classLoaders) {
				try {
					clazz = loader.loadClass(className);
					break;
				} catch(ClassNotFoundException e2) {
					;
				}
			}
		}
		if(clazz == null) {
			throw new CompilerError("Class " + className + " not found");
		}
		classCache.put(className, clazz);
		return clazz;
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
	
	int callJavaMethod(String methodName, String className, Type parameterTypes, Type keywordTypes, int reflect, Object[] stack, int sp) throws Throw, Throwable {
		Class<?> clazz = null;
		try {
			clazz = getJavaClass(className);
			Object instance = getJavaClassInstance(clazz);
			
			Method m = clazz.getMethod(methodName, makeJavaTypes(methodName, className, parameterTypes, keywordTypes, reflect));
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
				parameters[arity + kwArity] = converted.contains(className + "." + methodName) ? this.rex : null /*this.getEvaluatorContext()*/; // TODO: remove CTX
			}
			stack[sp - arity - kwMaps] =  m.invoke(instance, parameters);
			return sp - arity - kwMaps + 1;
		} 
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
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
//			e.printStackTrace();
		}
//		return sp;
	}
	
	private HashSet<String> converted = new HashSet<String>(Arrays.asList(
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
			"org.rascalmpl.library.util.ReflectiveCompiled.diff",
			"org.rascalmpl.library.util.ReflectiveCompiled.watch"

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
			 *  util::Reflective
			 *  
			 *  util::Webserver
			 *  
			 *  vis::Figure::color
			 *  
			 *  Traversal::getTraversalContext
			 *  
			 *  tutor::HTMLGenerator
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
			
	Class<?>[] makeJavaTypes(String methodName, String className, Type parameterTypes, Type keywordTypes, int reflect){
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
