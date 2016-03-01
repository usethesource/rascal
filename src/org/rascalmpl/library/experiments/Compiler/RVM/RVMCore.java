package org.rascalmpl.library.experiments.Compiler.RVM;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Coroutine;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.FunctionInstance;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunctionInstance;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Reference;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class RVMCore {
	public final IValueFactory vf;

	protected final TypeFactory tf; 
	
	protected final static IBool Rascal_TRUE = ValueFactoryFactory.getValueFactory().bool(true);	// TODO: Used by RVMonJVM
	protected final static IBool Rascal_FALSE = ValueFactoryFactory.getValueFactory().bool(false);	// TODO: Used by RVMonJVM
	protected final IString NONE; 
	protected ArrayList<Function> functionStore;
	protected Map<String, Integer> functionMap;

	protected ArrayList<Type> constructorStore;

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
		this.vf = rex.getValueFactory();
		tf = TypeFactory.getInstance();
		this.stdout = rex.getStdOut();
		this.stderr = rex.getStdErr();
		NONE = vf.string("$nothing$");
		moduleVariables = new HashMap<IValue,IValue>();
	}
	
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
	/*  		used by RVM interpreter and generated JVM bytecod					*/
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
}
