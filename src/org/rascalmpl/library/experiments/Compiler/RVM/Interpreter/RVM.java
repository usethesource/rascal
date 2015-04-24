package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

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
import java.util.Map.Entry;
import java.util.Stack;
import java.util.regex.Matcher;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;	// TODO: remove import: NOT YET: JavaCalls generate a Throw
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;
import org.rascalmpl.uri.URIResolverRegistry;


public class RVM implements java.io.Serializable {

	private static final long serialVersionUID = 2178453095307370332L;
	
	public final IValueFactory vf;
	private final TypeFactory tf;
	private final IBool Rascal_TRUE;
	private final IBool Rascal_FALSE;
	private final IString NONE; 
	
	private boolean debug = true;
	private boolean ocall_debug = false;
//	private boolean listing = false;
	private boolean trackCalls = false;
//	private boolean finalized = false;
	
	final ArrayList<Function> functionStore;
	protected final Map<String, Integer> functionMap;
	
	// Function overloading
	private final Map<String, Integer> resolver;
	private final ArrayList<OverloadedFunction> overloadedStore;
	
//	private final TypeStore typeStore;
//	private final Types types;
	
	private final ArrayList<Type> constructorStore;
	private final Map<String, Integer> constructorMap;
	
	private final Map<IValue, IValue> moduleVariables;
	PrintWriter stdout;
	PrintWriter stderr;
	
	//private Frame currentFrame;	// used for profiling
	private ILocationCollector locationCollector;
	
	// Management of active coroutines
	Stack<Coroutine> activeCoroutines = new Stack<>();
	Frame ccf = null; // The start frame of the current active coroutine (coroutine's main function)
	Frame cccf = null; // The candidate coroutine's start frame; used by the guard semantics 
	//IEvaluatorContext ctx;
	RascalExecutionContext rex;
	List<ClassLoader> classLoaders;
	
	private final Map<Class<?>, Object> instanceCache;
	private final Map<String, Class<?>> classCache;
	//private OverloadedFunction res;
	
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

	public RVM(RVMExecutable rrs, RascalExecutionContext rex) {
		
		super();

		this.vf = rex.getValueFactory();
		tf = TypeFactory.getInstance();
		//typeStore = rex.getTypeStore();
		this.instanceCache = new HashMap<Class<?>, Object>();
		this.classCache = new HashMap<String, Class<?>>();
		
		this.rex = rex;
		rex.setRVM(this);
		this.classLoaders = rex.getClassLoaders();
		this.stdout = rex.getStdOut();
		this.stderr = rex.getStdErr();
		this.debug = rex.getDebug();
		this.trackCalls = rex.getTrackCalls();
		//this.finalized = false;
		
		//this.types = new Types(this.vf);
		
		Rascal_TRUE = vf.bool(true);
		Rascal_FALSE = vf.bool(false);
		NONE = vf.string("$nothing$");
		
		this.functionMap = rrs.functionMap;
		this.functionStore = rrs.functionStore;
		
		this.constructorMap = rrs.constructorMap;
		this.constructorStore = rrs.constructorStore;

		this.resolver = rrs.resolver;
		this.overloadedStore = rrs.overloadedStore;
		
		moduleVariables = new HashMap<IValue,IValue>();
		
		MuPrimitive.init(vf);
		RascalPrimitive.init(this, rex);
		Opcode.init(stdout, rex.getProfile());
		
		this.locationCollector = NullLocationCollector.getInstance();
					
	}
	
	URIResolverRegistry getResolverRegistry() { return URIResolverRegistry.getInstance(); }
	
	IRascalMonitor getMonitor() {return rex.getMonitor();}
	
	PrintWriter getStdErr() { return rex.getStdErr(); }
	
	PrintWriter getStdOut() { return rex.getStdOut(); }
	
	Configuration getConfiguration() { return rex.getConfiguration(); }
	
	List<ClassLoader> getClassLoaders() { return rex.getClassLoaders(); }
	
	IEvaluatorContext getEvaluatorContext() { return rex.getEvaluatorContext(); }
	
	public void setLocationCollector(ILocationCollector collector){
		this.locationCollector = collector;
	}
	
	public void resetLocationCollector(){
		this.locationCollector = NullLocationCollector.getInstance();
	}
	
//	public void validateInstructionAdressingLimits(){
//		int nfs = functionStore.size();
//		//System.out.println("size functionStore: " + nfs);
//		if(nfs >= CodeBlock.maxArg){
//			throw new CompilerError("functionStore size " + nfs + "exceeds limit " + CodeBlock.maxArg);
//		}
//		int ncs = constructorStore.size();
//		//System.out.println("size constructorStore: " + ncs);
//		if(ncs >= CodeBlock.maxArg){
//			throw new CompilerError("constructorStore size " + ncs + "exceeds limit " + CodeBlock.maxArg);
//		}
//		int nov = overloadedStore.size();
//		//System.out.println("size overloadedStore: " + nov);
//		if(nov >= CodeBlock.maxArg){
//			throw new CompilerError("overloadedStore size " + nov + "exceeds limit " + CodeBlock.maxArg);
//		}
//	}
//	
//	public Integer useFunctionName(String fname){
//		Integer index = functionMap.get(fname);
//		
//		if(index == null){
//			index = functionStore.size();
//			functionMap.put(fname, index);
//			functionStore.add(null);
//		}
//		//stdout.println("useFunctionName: " + index + "  => " + fname);
//		return index;
//	}
//	
//	public void declare(Function f){
//		Integer index = functionMap.get(f.getName());
//		if(index == null){
//			index = functionStore.size();
//			functionMap.put(f.getName(), index);
//			functionStore.add(f);
//		} else {
//			functionStore.set(index, f);
//		}
//		//stdout.println("declare: " + index + "  => " + f.getName());
//	}
//	
//	public Integer useConstructorName(String cname) {
//		Integer index = constructorMap.get(cname) ;
//		if(index == null) {
//			index = constructorStore.size();
//			constructorMap.put(cname, index);
//			constructorStore.add(null);
//		}
//		//stdout.println("useConstructorName: " + index + "  => " + cname);
//		return index;
//	}
//	
//	public void declareConstructor(String cname, IConstructor symbol) {
//		Type constr = types.symbolToType(symbol, typeStore);
//		Integer index = constructorMap.get(cname);
//		if(index == null) {
//			index = constructorStore.size();
//			constructorMap.put(cname, index);
//			constructorStore.add(constr);
//		} else {
//			constructorStore.set(index, constr);
//		}
//		//stdout.println("declareConstructor: " + index + "  => " + cname);
//	}
//	
//	public Type symbolToType(IConstructor symbol) {
//		return types.symbolToType(symbol, typeStore);
//	}
//	
//	public void addResolver(IMap resolver) {
//		for(IValue fuid : resolver) {
//			String of = ((IString) fuid).getValue();
//			int index = ((IInteger) resolver.get(fuid)).intValue();
//			this.resolver.put(of, index);
//		}
//	}
//	
//	public void fillOverloadedStore(IList overloadedStore) {
//		for(IValue of : overloadedStore) {
//			
//			ITuple ofTuple = (ITuple) of;
//			
//			String funName = ((IString) ofTuple.get(0)).getValue();
//			
//			IConstructor funType = (IConstructor) ofTuple.get(1);
//			
//			String scopeIn = ((IString) ofTuple.get(2)).getValue();
//			if(scopeIn.equals("")) {
//				scopeIn = null;
//			}
//			IList fuids = (IList) ofTuple.get(3);
//			int[] funs = new int[fuids.length()];
//			int i = 0;
//			for(IValue fuid : fuids) {
//				String name = ((IString) fuid).getValue();
//				//stdout.println("fillOverloadedStore: add function " + name);
//				
//				Integer index = useFunctionName(name);
////				if(index == null){
////					throw new CompilerError("No definition for " + fuid + " in functionMap, i = " + i);
////				}
//				funs[i++] = index;
//			}
//			fuids = (IList) ofTuple.get(4);
//			int[] constrs = new int[fuids.length()];
//			i = 0;
//			for(IValue fuid : fuids) {
//				Integer index = useConstructorName(((IString) fuid).getValue());
////				if(index == null){
////					throw new CompilerError("No definition for " + fuid + " in constructorMap");
////				}
//				constrs[i++] = index;
//			}
//			res = new OverloadedFunction(this, funs, constrs, scopeIn);
//			this.overloadedStore.add(res);
//		}
//	}
	
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
	private String asString(Object o){
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
	
	private String asString(Object o, int w){
		String repr = asString(o);
		return (repr.length() < w) ? repr : repr.substring(0, w) + "...";
	}
	
//	private void finalizeInstructions(){
//		// Finalize the instruction generation of all functions, if needed
//		if(!finalized){
//			finalized = true;
//			for(Function f : functionStore) {
//				f.finalize(functionMap, constructorMap, resolver, listing);
//			}
//			for(OverloadedFunction of : overloadedStore) {
//				of.finalize(functionMap);
//			}
//		}
//	}

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
	
	public IValue executeFunction(String uid_func, IValue[] args){
		// Assumption here is that the function called is not a nested one
		// and does not use global variables
		Function func = functionStore.get(functionMap.get(uid_func));
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		Frame cf = root;
		
		// Pass the program arguments to main
		for(int i = 0; i < args.length; i++){
			cf.stack[i] = args[i]; 
		}
		cf.stack[args.length] = new HashMap<String, IValue>();
		Object o = executeProgram(root, cf);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return narrow(o); 
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
			
	private String trace = "";
	
	
	public String getTrace() {
		return trace;
	}
	
	public void appendToTrace(String trace) {
		this.trace = this.trace + trace + "\n";
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
	
	public IValue executeProgram(String moduleName, String uid_main, IValue[] args) {
		
		rex.setCurrentModuleName(moduleName);
		
		//finalizeInstructions();
		
		Function main_function = functionStore.get(functionMap.get(uid_main));

		
		if (main_function == null) {
			throw RascalRuntimeException.noMainFunction(null);
		}
		
		if (main_function.nformals != 2) { // List of IValues and empty map of keyword parameters
			throw new CompilerError("Function " + uid_main + " should have two arguments");
		}
		
		Frame root = new Frame(main_function.scopeId, null, main_function.maxstack, main_function);
		Frame cf = root;
		cf.stack[0] = vf.list(args); // pass the program argument to main_function as a IList object
		cf.stack[1] = new HashMap<String, IValue>();
		cf.src = main_function.src;
		
		Object o = executeProgram(root, cf);
		if(o != null && o instanceof Thrown){
			throw (Thrown) o;
		}
		IValue res = narrow(o);
		if(debug) {
			stdout.println("TRACE:");
			stdout.println(getTrace());
		}
		return res;
	}
	
	void print_step(int pc, Object[] stack, int sp, Frame cf){
		int startpc = pc - 1;
		stdout.printf("[%03d] %s, scope %d\n", startpc, cf.function.name, cf.scopeId);
		
		for (int i = 0; i < sp; i++) {
			stdout.println("\t   " + (i < cf.function.nlocals ? "*" : " ") + i + ": " + asString(stack[i], 40));
		}
		stdout.printf("%5s %s\n" , "", cf.function.codeblock.toString(startpc));
		stdout.flush();
	}
	
	int LOADVAR(int varScope, int pos, Frame cf, Object[] stack, int sp){
		if(CodeBlock.isMaxArg2(pos)){				
			stack[sp++] = moduleVariables.get(cf.function.constantStore[varScope]);
			return sp;
		}
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {					
				stack[sp++] = fr.stack[pos];
				return sp;
			}
		}
		throw new CompilerError("LOADVAR cannot find matching scope: " + varScope, cf);
	}
	
	int LOADVARREF(int varScope, int pos, Frame cf, Object[] stack, int sp){
		if(CodeBlock.isMaxArg2(pos)){				
			stack[sp++] = moduleVariables.get(cf.function.constantStore[varScope]);
			return sp;
		}
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {					
				stack[sp++] = new Reference(fr.stack, pos);
				return sp;
			}
		}
		throw new CompilerError("LOADVARREF cannot find matching scope: " + varScope, cf);
	}
	
	int LOADVARDEREF(int varScope, int pos, Frame cf, Object[] stack, int sp){
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {
				Reference ref = (Reference) fr.stack[pos];
				stack[sp++] = ref.stack[ref.pos];
				return sp;
			}
		}
		throw new CompilerError("LOADVARDEREF cannot find matching scope: " + varScope, cf);
	}
	
	int STOREVAR(int varScope, int pos, Frame cf, Object[] stack, int sp){
		if(CodeBlock.isMaxArg2(pos)){
			IValue mvar = cf.function.constantStore[varScope];
			moduleVariables.put(mvar, (IValue)stack[sp -1]);
			return sp;
		}
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == varScope) {
				// TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues
				fr.stack[pos] = stack[sp - 1];
				return sp;
			}
		}
		throw new CompilerError("STOREVAR cannot find matching scope: " + varScope, cf);
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
	
	int STOREVARDEREF(int varScope, int pos, Frame cf, Object[] stack, int sp){
		for (Frame fr = cf; fr != null; fr = fr.previousScope) { 
			if (fr.scopeId == varScope) {
				Reference ref = (Reference) fr.stack[pos];
				ref.stack[ref.pos] = stack[sp - 1];
				return sp;
			}
		}
		throw new CompilerError("STOREVARDEREF cannot find matching scope: " + varScope, cf);
	}
	
	@SuppressWarnings("unchecked")
	int LOADVARKWP(int varScope, String name, Frame cf, Object[] stack, int sp){
		for(Frame f = cf; f != null; f = f.previousCallFrame) {
			if (f.scopeId == varScope) {	
				if(f.function.nformals > 0){
					Object okargs = f.stack[f.function.nformals - 1];
					if(okargs instanceof HashMap<?,?>){	// Not all frames provide kwargs, i.e. generated PHI functions.
						HashMap<String, IValue> kargs = (HashMap<String,IValue>) okargs;
						if(kargs.containsKey(name)) {
							IValue val = kargs.get(name);
							//if(val.getType().isSubtypeOf(defaultValue.getKey())) {
							stack[sp++] = val;
							return sp;
							//}
						}
						Map<String, Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) f.stack[f.function.nformals];

						if(defaults.containsKey(name)) {
							Entry<Type, IValue> defaultValue = defaults.get(name);
							//if(val.getType().isSubtypeOf(defaultValue.getKey())) {
							stack[sp++] = defaultValue.getValue();
							return sp;
							//}
						}
					}
				}
			}
		}				
		throw new CompilerError("LOADVARKWP cannot find matching scope: " + varScope, cf);
	}
	
	@SuppressWarnings("unchecked")
	int STOREVARKWP(int varScope, String name, Frame cf, Object[] stack, int sp){
		IValue val = (IValue) stack[sp - 1];
		for(Frame f = cf; f != null; f = f.previousCallFrame) {
			if (f.scopeId == varScope) {
				if(f.function.nformals > 0){
					Object okargs = f.stack[f.function.nformals - 1];
					if(okargs instanceof HashMap<?,?>){	// Not all frames provide kwargs, i.e. generated PHI functions.
						HashMap<String, IValue> kargs = (HashMap<String,IValue>) f.stack[f.function.nformals - 1];
						if(kargs.containsKey(name)) {
							val = kargs.get(name);
							//if(val.getType().isSubtypeOf(defaultValue.getKey())) {
							kargs.put(name,  val);
							return sp;
							//}
						}
						Map<String, Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) f.stack[f.function.nformals];

						if(defaults.containsKey(name)) {
							Entry<Type, IValue> defaultValue = defaults.get(name);
							//if(val.getType().isSubtypeOf(defaultValue.getKey())) {
							stack[sp++] = defaultValue.getValue();
							return sp;
							//}
						}
					}
				}
			}
		}				
		throw new CompilerError("STOREVARKWP cannot find matching scope: " + varScope, cf);
	}
	@SuppressWarnings("unchecked")
	int LOADLOCKWP(String name, Frame cf, Object[] stack, int sp){
		Map<String, Map.Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) stack[cf.function.nformals];
		Map.Entry<Type, IValue> defaultValue = defaults.get(name);
		for(Frame f = cf; f != null; f = f.previousCallFrame) {
			Object okargs = f.stack[f.function.nformals - 1];
			if(okargs instanceof HashMap<?,?>){	// Not all frames provide kwargs, i.e. generated PHI functions.
				HashMap<String, IValue> kargs = (HashMap<String,IValue>) okargs;
				if(kargs.containsKey(name)) {
					IValue val = kargs.get(name);
					if(val.getType().isSubtypeOf(defaultValue.getKey())) {
						stack[sp++] = val;
						return sp;
					}
				}
			}
		}				
		stack[sp++] = defaultValue.getValue();
		return sp;
	}
	
	@SuppressWarnings("unchecked")
	int CALLCONSTR(Type constructor, int arity, Object[] stack, int sp){
		IValue[] args = new IValue[constructor.getArity()];

		java.util.Map<String,IValue> kwargs;
		Type type = (Type) stack[--sp];
		if(type.getArity() > 0){
			// Constructors with keyword parameters
			kwargs = (java.util.Map<String,IValue>) stack[--sp];
		} else {
			kwargs = new HashMap<String,IValue>();
		}

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
	
	@SuppressWarnings("unchecked")
	private Object executeProgram(Frame root, Frame cf) {
		Object[] stack = cf.stack;		                              	// current stack
		int sp = cf.function.nlocals;				                  	// current stack pointer
		int [] instructions = cf.function.codeblock.getInstructions(); 	// current instruction sequence
		int pc = 0;				                                      	// current program counter
		int postOp = 0;													// postprocessing operator (following main switch)
		int pos = 0;
		ArrayList<Frame> stacktrace = new ArrayList<Frame>();
		Thrown thrown = null;
		int arity;
		int instruction;
		int op;
		Object rval;
		
		// Overloading specific
		Stack<OverloadedFunctionInstanceCall> ocalls = new Stack<OverloadedFunctionInstanceCall>();
		OverloadedFunctionInstanceCall c_ofun_call = null;
		
		if(trackCalls) { cf.printEnter(stdout); stdout.flush(); }
		
		try {
			NEXT_INSTRUCTION: while (true) {
				
				assert pc >= 0 && pc < instructions.length : "Illegal pc value: " + pc + " at " + cf.src;
				assert sp >= cf.function.nlocals :           "sp value is " + sp + " (should be at least " + cf.function.nlocals +  ") at " + cf.src;
				assert cf.function.isCoroutine || 
				       cf.function.name.contains("Library/") ||
				       cf.function.name.contains("/RASCAL_ALL") ||
				       cf.function.name.contains("/PHI") ||
				       cf.function.name.contains("/GEN_") ||
				       cf.function.name.contains("/ALL_") ||
				       cf.function.name.contains("/OR_") ||
				       cf.function.name.contains("/IMPLICATION_") ||
				       cf.function.name.contains("/EQUIVALENCE_") ||
				       cf.function.name.contains("/closure") ||
				       cf.stack[cf.function.nformals - 1] instanceof HashMap<?,?>:
															 "HashMap with keyword parameters expected, got " + cf.stack[cf.function.nformals - 1];
				
				instruction = instructions[pc++];
				op = CodeBlock.fetchOp(instruction);

				if (debug) {
					print_step(pc, stack, sp, cf);
				}
				
				String name;
				INSTRUCTION: switch (op) {
					
				case Opcode.OP_POP:
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC0:
					assert 0 < cf.function.nlocals : "LOADLOC0: pos larger that nlocals at " + cf.src;
					assert stack[0] != null: "Local variable 0 is null";
					stack[sp++] = stack[0]; continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC1:
					assert 1 < cf.function.nlocals : "LOADLOC1: pos larger that nlocals at " + cf.src;
					assert stack[1] != null: "Local variable 1 is null";
					stack[sp++] = stack[1]; continue NEXT_INSTRUCTION; 
					
				case Opcode.OP_LOADLOC2:
					assert 2 < cf.function.nlocals : "LOADLOC2: pos larger that nlocals at " + cf.src;
					assert stack[2] != null: "Local variable 2 is null";
					stack[sp++] = stack[2]; continue NEXT_INSTRUCTION; 
					
				case Opcode.OP_LOADLOC3:
					assert 3 < cf.function.nlocals : "LOADLOC3: pos larger that nlocals at " + cf.src;
					assert stack[3] != null: "Local variable 3 is null";
					stack[sp++] = stack[3]; continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC4:
					assert 4 < cf.function.nlocals : "LOADLOC4: pos larger that nlocals at " + cf.src;
					assert stack[4] != null: "Local variable 4 is null";
					stack[sp++] = stack[4]; continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC5:
					assert 5 < cf.function.nlocals : "LOADLOC5: pos larger that nlocals at " + cf.src;
					assert stack[5] != null: "Local variable 5 is null";
					stack[sp++] = stack[5]; continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC6:
					assert 6 < cf.function.nlocals : "LOADLOC6: pos larger that nlocals at " + cf.src;
					assert stack[6] != null: "Local variable 6 is null";
					stack[sp++] = stack[6]; continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC7:
					assert 7 < cf.function.nlocals : "LOADLOC7: pos larger that nlocals at " + cf.src;
					assert stack[7] != null: "Local variable 7 is null";
					stack[sp++] = stack[7]; continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC8:
					assert 8 < cf.function.nlocals : "LOADLOC8: pos larger that nlocals at " + cf.src;
					assert stack[8] != null: "Local variable 8 is null";
					stack[sp++] = stack[8]; continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC9:
					assert 9 < cf.function.nlocals : "LOADLOC9: pos larger that nlocals at " + cf.src;
					assert stack[9] != null: "Local variable 9 is null";
					stack[sp++] = stack[9]; continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADLOC:
					pos = CodeBlock.fetchArg1(instruction);
					assert pos < cf.function.nlocals : "LOADLOC: pos larger that nlocals at " + cf.src;
					assert stack[pos] != null: "Local variable " + pos + " is null";
					stack[sp++] = stack[pos];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_RESETLOCS:
					IList positions = (IList) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					for(IValue v : positions){
						stack[((IInteger) v).intValue()] = null;
					}
					stack[sp++] = Rascal_TRUE;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADBOOL:
					stack[sp++] = CodeBlock.fetchArg1(instruction) == 1 ? Rascal_TRUE : Rascal_FALSE;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADINT:
					stack[sp++] = CodeBlock.fetchArg1(instruction);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADCON:
					stack[sp++] = cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOCREF:
					stack[sp++] = new Reference(stack, CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_CALLMUPRIM:	
					sp = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute(stack, sp, CodeBlock.fetchArg2(instruction));
					assert stack[sp - 1] != null: "MuPrimitive returns null";
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_JMP:
					pc = CodeBlock.fetchArg1(instruction);
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPTRUE:
					if (((IBool) stack[sp - 1]).getValue()) {
						pc = CodeBlock.fetchArg1(instruction);
					}
					sp--;
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPFALSE:
					if (!((IBool) stack[sp - 1]).getValue()) {
						pc = CodeBlock.fetchArg1(instruction);
					}
					sp--;
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
							stack[sp++] = vf.bool(false);
							pc = caseDefault;
					} else {
						pc = x.intValue();
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADTYPE:
					stack[sp++] = cf.function.typeConstantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;
					 
				case Opcode.OP_LOADLOCDEREF: {
					Reference ref = (Reference) stack[CodeBlock.fetchArg1(instruction)];
					stack[sp++] = ref.stack[ref.pos];
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_STORELOC:
					pos = CodeBlock.fetchArg1(instruction);
					assert pos < cf.function.nlocals : "STORELOC: pos larger that nlocals at " + cf.src;
					stack[pos] = stack[sp - 1];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_UNWRAPTHROWNLOC: {
					pos = CodeBlock.fetchArg1(instruction);
					assert pos < cf.function.nlocals : "UNWRAPTHROWNLOC: pos larger that nlocals at " + cf.src;
					stack[pos] = ((Thrown) stack[--sp]).value;
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_STORELOCDEREF:
					Reference ref = (Reference) stack[CodeBlock.fetchArg1(instruction)];
					ref.stack[ref.pos] = stack[sp - 1]; // TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues    
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADFUN:
					// Loads functions that are defined at the root
					stack[sp++] = new FunctionInstance(functionStore.get(CodeBlock.fetchArg1(instruction)), root, this);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOAD_NESTED_FUN: { 
					// Loads nested functions and closures (anonymous nested functions)
					stack[sp++] = FunctionInstance.computeFunctionInstance(functionStore.get(CodeBlock.fetchArg1(instruction)), cf, CodeBlock.fetchArg2(instruction), this);
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_LOADOFUN:
					OverloadedFunction of = overloadedStore.get(CodeBlock.fetchArg1(instruction));
					stack[sp++] = of.scopeIn == -1 ? new OverloadedFunctionInstance(of.functions, of.constructors, root, functionStore, constructorStore, this)
					                               : OverloadedFunctionInstance.computeOverloadedFunctionInstance(of.functions, of.constructors, cf, of.scopeIn, functionStore, constructorStore, this);
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADCONSTR:
					Type constructor = constructorStore.get(CodeBlock.fetchArg1(instruction));  
					stack[sp++] = constructor;
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADVAR:
					 sp = LOADVAR(CodeBlock.fetchArg1(instruction), 
							 	  CodeBlock.fetchArg2(instruction), cf, stack, sp);
					 continue NEXT_INSTRUCTION;
					 
				case Opcode.OP_LOADVARREF: 
					sp = LOADVARREF(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf, stack, sp);
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADVARDEREF: 
					sp = LOADVARDEREF(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf, stack, sp);
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_STOREVAR:
					sp = STOREVAR(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf, stack, sp);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_UNWRAPTHROWNVAR:
					sp = UNWRAPTHROWNVAR(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf, stack, sp);
					continue NEXT_INSTRUCTION;
									
				case Opcode.OP_STOREVARDEREF:
					sp = STOREVARDEREF(CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), cf, stack, sp);
					continue NEXT_INSTRUCTION;
									
				case Opcode.OP_CALLCONSTR:
					sp = CALLCONSTR(constructorStore.get(CodeBlock.fetchArg1(instruction)), CodeBlock.fetchArg2(instruction), stack, sp);
					continue NEXT_INSTRUCTION;
										
				case Opcode.OP_CALLDYN:				
				case Opcode.OP_CALL:
					
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
					
//					// Specific to delimited continuations (experimental)
//					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof Coroutine) {
//						arity = CodeBlock.fetchArg1(instruction);
//						Coroutine coroutine = (Coroutine) stack[--sp];
//						// Merged the hasNext and next semantics
//						activeCoroutines.push(coroutine);
//						ccf = coroutine.start;
//						coroutine.next(cf);
//						instructions = coroutine.frame.function.codeblock.getInstructions();
//						coroutine.frame.stack[coroutine.frame.sp++] = arity == 1 ? stack[--sp] : null;
//						cf.pc = pc;
//						cf.sp = sp;
//						cf = coroutine.frame;
//						stack = cf.stack;
//						sp = cf.sp;
//						pc = cf.pc;
//						continue NEXT_INSTRUCTION;
//					}
					
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
					
					if(trackCalls) { cf.printEnter(stdout); stdout.flush();}
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
					
					cf.src = (ISourceLocation) cf.function.constantStore[instructions[pc++]];
					locationCollector.registerLocation(cf.src);
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
							if(trackCalls) { cf.printEnter(stdout); stdout.flush();}
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
						if(trackCalls) { cf.printEnter(stdout); stdout.flush(); }
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
					} else {
						constructor = c_ofun_call_next.nextConstructor(constructorStore);
						sp = sp - arity;
						stack[sp++] = vf.constructor(constructor, c_ofun_call_next.getConstructorArguments(constructor.getArity()));
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CHECKARGTYPEANDCOPY:
					pos = CodeBlock.fetchArg1(instruction);
					Type argType = ((IValue) stack[pos]).getType();
					Type paramType = cf.function.typeConstantStore[CodeBlock.fetchArg2(instruction)];
					
					int pos2 = instructions[pc++];
					if(argType.isSubtypeOf(paramType)){
						stack[pos2] = stack[pos];
						stack[sp++] = vf.bool(true);
					} else {
						stack[sp++] = vf.bool(false);
					}
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
					assert sp == ((op == Opcode.OP_RETURN0) ? cf.function.nlocals : cf.function.nlocals + 1)
							: "On return from " + cf.function.name + ": " + (sp - cf.function.nlocals) + " spurious stack elements";
					
					// if the current frame is the frame of a top active coroutine, 
					// then pop this coroutine from the stack of active coroutines
					if(cf == ccf) {
						activeCoroutines.pop();
						ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					}
					
					if(trackCalls) { cf.printBack(stdout, rval); }
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
					String methodName =  ((IString) cf.function.constantStore[instructions[pc++]]).getValue();
					String className =  ((IString) cf.function.constantStore[instructions[pc++]]).getValue();
					Type parameterTypes = cf.function.typeConstantStore[instructions[pc++]];
					Type keywordTypes = cf.function.typeConstantStore[instructions[pc++]];
					int reflect = instructions[pc++];
					arity = parameterTypes.getArity();
					try {
						//int sp1 = sp;
					    sp = callJavaMethod(methodName, className, parameterTypes, keywordTypes, reflect, stack, sp);
					    //assert sp == sp1 - arity + 1;
					} catch(Throw e) {
						stacktrace.add(cf);
						thrown = Thrown.getInstance(e.getException(), e.getLocation(), cf);
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					} catch (Thrown e){
						stacktrace.add(cf);
						thrown = e;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					} catch (Exception e){
						e.printStackTrace(stderr);
						stderr.flush();
						throw new CompilerError("Exception in CALLJAVA: " + className + "." + methodName + "; message: "+ e.getMessage() + e.getCause(), cf );
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
					
				case Opcode.OP_CALLPRIM:
					arity = CodeBlock.fetchArg2(instruction);
					cf.src = (ISourceLocation) cf.function.constantStore[instructions[pc++]];
					locationCollector.registerLocation(cf.src);
					try {
						//sp1 = sp;
						sp = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute(stack, sp, arity, cf);
						//assert sp == sp1 - arity + 1;
					} catch(Exception exception) {
						if(!(exception instanceof Thrown)){
							throw exception;
						}
						thrown = (Thrown) exception;
						//thrown.stacktrace.add(cf);
						sp = sp - arity;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					}
					
					continue NEXT_INSTRUCTION;
					
				// Some specialized MuPrimitives
					
				case Opcode.OP_SUBSCRIPTARRAY:
					stack[sp - 2] = ((Object[]) stack[sp - 2])[((Integer) stack[sp - 1])];
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBSCRIPTLIST:
					stack[sp - 2] = ((IList) stack[sp - 2]).get((Integer) stack[sp - 1]);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LESSINT:
					stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) < ((Integer) stack[sp - 1]));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_GREATEREQUALINT:
					stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) >= ((Integer) stack[sp - 1]));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_ADDINT:
					stack[sp - 2] = ((Integer) stack[sp - 2]) + ((Integer) stack[sp - 1]);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBTRACTINT:
					stack[sp - 2] = ((Integer) stack[sp - 2]) - ((Integer) stack[sp - 1]);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_ANDBOOL:
					stack[sp - 2] = ((IBool) stack[sp - 2]).and((IBool) stack[sp - 1]);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_TYPEOF:
					if(stack[sp - 1] instanceof HashSet<?>){	// For the benefit of set matching
						HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
						if(mset.isEmpty()){
							stack[sp - 1] = tf.setType(tf.voidType());
						} else {
							IValue v = mset.iterator().next();
							stack[sp - 1] = tf.setType(v.getType());
						}
					} else {
						stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBTYPE:
					stack[sp - 2] = vf.bool(((Type) stack[sp - 2]).isSubtypeOf((Type) stack[sp - 1]));
					sp--;
					continue NEXT_INSTRUCTION;
								
				case Opcode.OP_LABEL:
					throw new CompilerError("LABEL instruction at runtime", cf);
					
				case Opcode.OP_HALT:
					if (debug) {
						stdout.println("Program halted:");
						for (int i = 0; i < sp; i++) {
							stdout.println(i + ": " + stack[i]);
						}
					}
					return stack[sp - 1];

				case Opcode.OP_PRINTLN:
					sp =  PRINTLN(CodeBlock.fetchArg1(instruction), stack, sp);
					continue NEXT_INSTRUCTION;	
					
				case Opcode.OP_THROW:
					Object obj = stack[--sp];
					thrown = null;
					cf.src = (ISourceLocation) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					locationCollector.registerLocation(cf.src);
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
					sp = LOADLOCKWP(((IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg1(instruction))).getValue(), cf, stack, sp);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADVARKWP:
					sp = LOADVARKWP(CodeBlock.fetchArg1(instruction), ((IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg2(instruction))).getValue(), cf, stack, sp);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_STORELOCKWP:
					val = (IValue) stack[sp - 1];
					name = ((IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg1(instruction))).getValue();
					HashMap<String, IValue> kargs = (HashMap<String, IValue>) stack[cf.function.nformals - 1];
					kargs.put(name, val);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_STOREVARKWP:
					sp = STOREVARKWP(CodeBlock.fetchArg1(instruction), 
									 ((IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg2(instruction))).getValue(),
		 	  						 cf, stack, sp);
					continue NEXT_INSTRUCTION;

// Experimental, will be removed soon
					
//					case Opcode.OP_JMPINDEXED:
//					labelIndex = ((IInteger) stack[--sp]).intValue();
//					labels = (IList) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
//					pc = ((IInteger) labels.get(labelIndex)).intValue();
//					continue NEXT_INSTRUCTION;
					
//				case Opcode.OP_LOADCONT:
//					s = CodeBlock.fetchArg1(instruction);
//					assert stack[0] instanceof Coroutine;
//					for(Frame fr = cf; fr != null; fr = fr.previousScope) {
//						if (fr.scopeId == s) {
//							// TODO: unsafe in general case (the coroutine object should be copied)
//							stack[sp++] = fr.stack[0];
//							continue NEXT_INSTRUCTION;
//						}
//					}
//					throw new CompilerError("LOADCONT cannot find matching scope: " + s, cf);
				
//				case Opcode.OP_RESET:
//					fun_instance = (FunctionInstance) stack[--sp]; // A function of zero arguments
//					cf.pc = pc;
//					cf = cf.getCoroutineFrame(fun_instance, 0, sp);
//					activeCoroutines.push(new Coroutine(cf));
//					ccf = cf;
//					instructions = cf.function.codeblock.getInstructions();
//					stack = cf.stack;
//					sp = cf.sp;
//					pc = cf.pc;
//					continue NEXT_INSTRUCTION;
					
//				case Opcode.OP_SHIFT:
//					fun_instance = (FunctionInstance) stack[--sp]; // A function of one argument (continuation)
//					coroutine = activeCoroutines.pop();
//					ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
//					cf.pc = pc;
//					cf.sp = sp;
//					prev = coroutine.start.previousCallFrame;
//					coroutine.suspend(cf);
//					cf = prev;
//					sp = cf.sp;
//					fun_instance.args = new Object[] { coroutine };
//					cf = cf.getCoroutineFrame(fun_instance, 0, sp);
//					activeCoroutines.push(new Coroutine(cf));
//					ccf = cf;
//					instructions = cf.function.codeblock.getInstructions();
//					stack = cf.stack;
//					sp = cf.sp;
//					pc = cf.pc;
//					continue NEXT_INSTRUCTION;
								
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
					stdout.println("EXCEPTION " + thrown + " at: " + cf.src);
					for(Frame f = cf; f != null; f = f.previousCallFrame) {
						stdout.println("\t" + f.toString());
					}
					stdout.flush();
					return thrown;
				}
				
			}
		} catch (Exception e) {
			if(e instanceof Thrown){
				throw e;
			}
			stdout.println("EXCEPTION " + e + " at: " + cf.src);
			for(Frame f = cf; f != null; f = f.previousCallFrame) {
				stdout.println("\t" + f.toString());
			}
			stdout.flush();
			e.printStackTrace(stderr);
			stderr.flush();
			String e2s = (e instanceof CompilerError) ? e.getMessage() : e.toString();
			throw new CompilerError(e2s + "; function: " + cf + "; instruction: " + cf.function.codeblock.toString(pc - 1), cf );
			
			//stdout.println("PANIC: (instruction execution): " + e.getMessage());
			//e.printStackTrace();
			//stderr.println(e.getStackTrace());
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
		if(instance != null){
			return instance;
		}
//		Class<?> clazz = null;
//		try {
//			clazz = this.getClass().getClassLoader().loadClass(className);
//		} catch(ClassNotFoundException e1) {
//			// If the class is not found, try other class loaders
//			for(ClassLoader loader : this.classLoaders) {
//				//for(ClassLoader loader : ctx.getEvaluator().getClassLoaders()) {
//				try {
//					clazz = loader.loadClass(className);
//					break;
//				} catch(ClassNotFoundException e2) {
//					;
//				}
//			}
//		}
		try{
			Constructor<?> constructor = clazz.getConstructor(IValueFactory.class);
			instance = constructor.newInstance(vf);
			instanceCache.put(clazz, instance);
			return instance;
		} catch (IllegalArgumentException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new ImplementationError(e.getMessage(), e);
		} 
	}
	
	int callJavaMethod(String methodName, String className, Type parameterTypes, Type keywordTypes, int reflect, Object[] stack, int sp) throws Throw {
		Class<?> clazz = null;
		try {
//			try {
//				clazz = this.getClass().getClassLoader().loadClass(className);
//			} catch(ClassNotFoundException e1) {
//				// If the class is not found, try other class loaders
//				for(ClassLoader loader : this.classLoaders) {
//					//for(ClassLoader loader : ctx.getEvaluator().getClassLoaders()) {
//					try {
//						clazz = loader.loadClass(className);
//						break;
//					} catch(ClassNotFoundException e2) {
//						;
//					}
//				}
//			}
//			
//			if(clazz == null) {
//				throw new CompilerError("Class " + className + " not found, while trying to call method"  + methodName);
//			}
			
//			Constructor<?> cons;
//			cons = clazz.getConstructor(IValueFactory.class);
//			Object instance = cons.newInstance(vf);
			clazz = getJavaClass(className);
			Object instance = getJavaClassInstance(clazz);
			
			Method m = clazz.getMethod(methodName, makeJavaTypes(methodName, className, parameterTypes, keywordTypes, reflect));
			int arity = parameterTypes.getArity();
			int kwArity = keywordTypes.getArity();
			int kwMaps = kwArity > 0 ? 2 : 0;
			Object[] parameters = new Object[arity + kwArity + reflect];
			int i = 0;
			while(i < arity){
				parameters[i] = stack[sp - arity - kwMaps + i];
				i++;
			}
			if(kwArity > 0){
				@SuppressWarnings("unchecked")
				Map<String, IValue> kwMap = (Map<String, IValue>) stack[sp - 2];
				@SuppressWarnings("unchecked")
				Map<String, Map.Entry<Type, IValue>> kwDefaultMap = (Map<String, Map.Entry<Type, IValue>>) stack[sp - 1];

				while(i < arity + kwArity){
					String key = keywordTypes.getFieldName(i - arity);
					IValue val = kwMap.get(key);
					if(val == null){
						val = kwDefaultMap.get(key).getValue();
					}
					parameters[i] = val;
					i++;
				}
			}
			
			if(reflect == 1) {
				parameters[arity + kwArity] = converted.contains(className + "." + methodName) ? this.rex : this.getEvaluatorContext(); // TODO: remove CTX
			}
			stack[sp - arity - kwMaps] =  m.invoke(instance, parameters);
			return sp - arity - kwMaps + 1;
		} 
		catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			if(e.getTargetException() instanceof Throw) {
				throw (Throw) e.getTargetException();
			}
			if(e.getTargetException() instanceof Thrown){
				throw (Thrown) e.getTargetException();
			}
			e.printStackTrace();
		}
		return sp;
	}
	
	HashSet<String> converted = new HashSet<String>(Arrays.asList(
			"org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ParsingTools.parseFragment",
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
			
			"org.rascalmpl.library.PreludeCompiled.implode",
			"org.rascalmpl.library.PreludeCompiled.parse",
			"org.rascalmpl.library.PreludeCompiled.print",
			"org.rascalmpl.library.PreludeCompiled.println",
			"org.rascalmpl.library.PreludeCompiled.iprint",
			"org.rascalmpl.library.PreludeCompiled.iprintln",
			"org.rascalmpl.library.PreludeCompiled.rprint",
			"org.rascalmpl.library.PreludeCompiled.rprintln",
			"org.rascalmpl.library.util.MonitorCompiled.startJob",
			"org.rascalmpl.library.util.MonitorCompiled.event",
			"org.rascalmpl.library.util.MonitorCompiled.endJob",
			"org.rascalmpl.library.util.MonitorCompiled.todo",
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
			 * experiments::Compiler::RVM::Interpreter::ParsingTools::parseFragment
			 * experiments::Compiler::RVM::Run::executeProgram
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
	
	private static class JavaClasses implements ITypeVisitor<Class<?>, RuntimeException> {

		@Override
		public Class<?> visitBool(org.eclipse.imp.pdb.facts.type.Type boolType) {
			return IBool.class;
		}

		@Override
		public Class<?> visitReal(org.eclipse.imp.pdb.facts.type.Type type) {
			return IReal.class;
		}

		@Override
		public Class<?> visitInteger(org.eclipse.imp.pdb.facts.type.Type type) {
			return IInteger.class;
		}
		
		@Override
		public Class<?> visitRational(org.eclipse.imp.pdb.facts.type.Type type) {
			return IRational.class;
		}
		
		@Override
		public Class<?> visitNumber(org.eclipse.imp.pdb.facts.type.Type type) {
			return INumber.class;
		}

		@Override
		public Class<?> visitList(org.eclipse.imp.pdb.facts.type.Type type) {
			return IList.class;
		}

		@Override
		public Class<?> visitMap(org.eclipse.imp.pdb.facts.type.Type type) {
			return IMap.class;
		}

		@Override
		public Class<?> visitAlias(org.eclipse.imp.pdb.facts.type.Type type) {
			return type.getAliased().accept(this);
		}

		@Override
		public Class<?> visitAbstractData(org.eclipse.imp.pdb.facts.type.Type type) {
			return IConstructor.class;
		}

		@Override
		public Class<?> visitSet(org.eclipse.imp.pdb.facts.type.Type type) {
			return ISet.class;
		}

		@Override
		public Class<?> visitSourceLocation(org.eclipse.imp.pdb.facts.type.Type type) {
			return ISourceLocation.class;
		}

		@Override
		public Class<?> visitString(org.eclipse.imp.pdb.facts.type.Type type) {
			return IString.class;
		}

		@Override
		public Class<?> visitNode(org.eclipse.imp.pdb.facts.type.Type type) {
			return INode.class;
		}

		@Override
		public Class<?> visitConstructor(org.eclipse.imp.pdb.facts.type.Type type) {
			return IConstructor.class;
		}

		@Override
		public Class<?> visitTuple(org.eclipse.imp.pdb.facts.type.Type type) {
			return ITuple.class;
		}

		@Override
		public Class<?> visitValue(org.eclipse.imp.pdb.facts.type.Type type) {
			return IValue.class;
		}

		@Override
		public Class<?> visitVoid(org.eclipse.imp.pdb.facts.type.Type type) {
			return null;
		}

		@Override
		public Class<?> visitParameter(org.eclipse.imp.pdb.facts.type.Type parameterType) {
			return parameterType.getBound().accept(this);
		}

		@Override
		public Class<?> visitExternal(
				org.eclipse.imp.pdb.facts.type.Type externalType) {
			return IValue.class;
		}

		@Override
		public Class<?> visitDateTime(Type type) {
			return IDateTime.class;
		}
	}
}
