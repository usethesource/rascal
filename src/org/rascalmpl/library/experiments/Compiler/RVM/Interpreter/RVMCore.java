package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.types.DefaultRascalTypeVisitor;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalType;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
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

public abstract class RVMCore {
	public final IValueFactory vf;

	protected final TypeFactory tf; 
	
	protected final static IBool Rascal_TRUE = ValueFactoryFactory.getValueFactory().bool(true);	// TODO: Used by RVMonJVM
	protected final static IBool Rascal_FALSE = ValueFactoryFactory.getValueFactory().bool(false);	// TODO: Used by RVMonJVM
	protected final IString NONE; 
	protected ArrayList<Function> functionStore;
	protected Map<String, Integer> functionMap;

	protected ArrayList<Type> constructorStore;
	
	protected IFrameObserver frameObserver;

	public final static Function noCompanionFunction = new Function("noCompanionFunction", null, null, 0, 0, false, null, 0, false, 0, 0, null, null, 0);
	public static final HashMap<String, IValue> emptyKeywordMap = new HashMap<>(0);

	protected PrintWriter stdout;
	protected PrintWriter stderr;

	// Management of active coroutines
	protected Stack<Coroutine> activeCoroutines = new Stack<>();	// TODO: Used by RVMonJVM
	protected Frame ccf = null; // The start frame of the current active coroutine (coroutine's main function)	// TODO: Used by RVMonJVM
	protected Frame cccf = null; // The candidate coroutine's start frame; used by the guard semantics // TODO: Used by RVMonJVM
	protected RascalExecutionContext rex;

	public Map<IValue, IValue> moduleVariables;
	
	List<ClassLoader> classLoaders;
	private final Map<Class<?>, Object> instanceCache;
	private final Map<String, Class<?>> classCache;
	
	public static RVMCore readFromFileAndInitialize(ISourceLocation rvmBinaryLocation, RascalExecutionContext rex) throws IOException{
		RVMExecutable rvmExecutable = RVMExecutable.read(rvmBinaryLocation);
		return ExecutionTools.initializedRVM(rvmExecutable, rex);
	}

	public static IValue readFromFileAndExecuteProgram(ISourceLocation rvmBinaryLocation, IMap keywordArguments, RascalExecutionContext rex) throws Exception{
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

	public RVMCore( RascalExecutionContext rex){
		this.rex = rex;
		rex.setRVM(this);
		
		this.instanceCache = new HashMap<Class<?>, Object>();
		this.classCache = new HashMap<String, Class<?>>();
		this.classLoaders = rex.getClassLoaders();
		
		this.vf = rex.getValueFactory();
		tf = TypeFactory.getInstance();
		this.stdout = rex.getStdOut();
		this.stderr = rex.getStdErr();
		NONE = vf.string("$nothing$");
		moduleVariables = new HashMap<IValue,IValue>();
	}
	
	public Map<IValue, IValue> getModuleVariables() { return moduleVariables; }

	public PrintWriter getStdErr() { return rex.getStdErr(); }
	
	public PrintWriter getStdOut() { return rex.getStdOut(); }
	
	URIResolverRegistry getResolverRegistry() { return URIResolverRegistry.getInstance(); }
	
	IRascalMonitor getMonitor() {return rex.getMonitor();}
	
	List<ClassLoader> getClassLoaders() { return rex.getClassLoaders(); }

	public IFrameObserver getFrameObserver() {
		// TODO Auto-generated method stub
		return frameObserver;
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
		// Assumption here is that the function called is not a nested one
		// and does not use global variables
		Function func = functionStore.get(functionMap.get(uid_func));
		return executeRVMFunction(func, posArgs, kwArgs);
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
	 * @return
	 */
	abstract public Object executeRVMFunction(Function func, IValue[] posArgs, Map<String,IValue> kwArgs);
	
	abstract public IValue executeRVMFunction(FunctionInstance func, IValue[] args);
	
	abstract public IValue executeRVMFunction(OverloadedFunctionInstance func, IValue[] args);

	abstract public IValue executeRVMFunctionInVisit(Frame root);
	
	abstract public IValue executeRVMProgram(String moduleName, String uid_main, IValue[] args, HashMap<String,IValue> kwArgs);
	
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
	
	/********************************************************************************/
	/*			Auxiliary functions that implement specific instructions and are	*/
	/*  		used both by RVM interpreter and generated JVM bytecod				*/
	/********************************************************************************/
	
	// LOAD/PUSH VAR

	protected Object LOADVAR(final Frame cf, final int varScope, final int pos){
		return CodeBlock.isMaxArg2(pos) ? LOADVARMODULE(cf, varScope) : LOADVARSCOPED(cf, varScope, pos);
	}

	protected Object LOADVARMODULE(final Frame cf, final int varScope){				
		return moduleVariables.get(cf.function.constantStore[varScope]);
	}

	protected Object LOADVARSCOPED(final Frame cf, final int varScope, final int pos){
		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {					
				return fr.stack[pos];
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
				fr.stack[pos] = ((Thrown) stack[--sp]).value;
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
	
	protected int PUSHLOCKWP(final Object[] stack, int sp, final Frame cf, final int iname){
		stack[sp++] = LOADLOCKWP(stack, cf, iname);
		return sp;
	}
	
	// CALLCONSTR
	
	@SuppressWarnings("unchecked")
	protected int CALLCONSTR(final Object[] stack, int sp, final int iconstructor, final int arity){
		
		Type constructor = constructorStore.get(iconstructor);
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
