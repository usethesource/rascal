package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.print.DocFlavor.URL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class RVM extends ClassLoader {

	public final IValueFactory vf;

	private boolean debug = true;
	private boolean listing = false;
	private boolean finalized = false;

	protected final ArrayList<Function> functionStore;
	protected final Map<String, Integer> functionMap;

	// Function overloading
	private final Map<String, Integer> resolver;
	private final ArrayList<OverloadedFunction> overloadedStore;

	private final TypeStore typeStore = new TypeStore();
	private final Types types;

	private final ArrayList<Type> constructorStore;
	private final Map<String, Integer> constructorMap;

	PrintWriter stdout;
	PrintWriter stderr;

	// Management of active coroutines
	// Stack<Coroutine> activeCoroutines = new Stack<>();
	// Frame ccf = null; // The start frame of the current active coroutine
	// // (coroutine's main function)
	// Frame cccf = null; // The candidate coroutine's start frame; used by the
	// // guard semantics
	IEvaluatorContext ctx;

	// // An exhausted coroutine instance
	// public static Coroutine exhausted = new Coroutine(null) {
	//
	// @Override
	// public void next(Frame previousCallFrame) {
	// throw new RuntimeException("Internal error: an attempt to activate an exhausted coroutine instance.");
	// }
	//
	// @Override
	// public void suspend(Frame current) {
	// throw new RuntimeException("Internal error: an attempt to suspend an exhausted coroutine instance.");
	// }
	//
	// @Override
	// public boolean isInitialized() {
	// return true;
	// }
	//
	// @Override
	// public boolean hasNext() {
	// return false;
	// }
	//
	// @Override
	// public Coroutine copy() {
	// throw new RuntimeException("Internal error: an attempt to copy an exhausted coroutine instance.");
	// }
	// };

	public RVM(IValueFactory vf, IEvaluatorContext ctx, boolean debug, boolean profile) {
		super();

		this.vf = vf;
		// tf = TypeFactory.getInstance();

		this.ctx = ctx;
		this.stdout = ctx.getStdOut();
		this.stderr = ctx.getStdErr();
		this.debug = debug;
		this.finalized = false;

		this.types = new Types(this.vf);

		// TRUE = true;
		// FALSE = false;
		// Rascal_TRUE = vf.bool(true);
		// Rascal_FALSE = vf.bool(false);
		// NONE = vf.string("$nothing$");
		functionStore = new ArrayList<Function>();
		constructorStore = new ArrayList<Type>();

		functionMap = new HashMap<String, Integer>();
		constructorMap = new HashMap<String, Integer>();

		resolver = new HashMap<String, Integer>();
		overloadedStore = new ArrayList<OverloadedFunction>();

		// moduleVariables = new HashMap<IValue, IValue>();

		// MuPrimitive.init(vf, stdout, profile);
		// RascalPrimitive.init(vf, this, profile);
		// Opcode.init(stdout, profile);
	}

	public RVM(IValueFactory vf) {
		this(vf, null, false, false);
	}

	public void declare(Function f) {
		if (functionMap.get(f.getName()) != null) {
			throw new RuntimeException("PANIC: Double declaration of function: " + f.getName());
		}
		functionMap.put(f.getName(), functionStore.size());
		functionStore.add(f);
	}

	public void declareConstructor(String name, IConstructor symbol) {
		Type constr = types.symbolToType(symbol, typeStore);
		if (constructorMap.get(name) != null) {
			throw new RuntimeException("PANIC: Double declaration of constructor: " + name);
		}
		constructorMap.put(name, constructorStore.size());
		constructorStore.add(constr);
	}

	public Type symbolToType(IConstructor symbol) {
		return types.symbolToType(symbol, typeStore);
	}

	public void addResolver(IMap resolver) {
		for (IValue fuid : resolver) {
			String of = ((IString) fuid).getValue();
			int index = ((IInteger) resolver.get(fuid)).intValue();
			this.resolver.put(of, index);
		}
	}

	public void fillOverloadedStore(IList overloadedStore) {
		for (IValue of : overloadedStore) {
			ITuple ofTuple = (ITuple) of;
			String scopeIn = ((IString) ofTuple.get(0)).getValue();
			if (scopeIn.equals("")) {
				scopeIn = null;
			}
			IList fuids = (IList) ofTuple.get(1);
			int[] funs = new int[fuids.length()];
			int i = 0;
			for (IValue fuid : fuids) {
				Integer index = functionMap.get(((IString) fuid).getValue());
				if (index == null) {
					throw new RuntimeException("No definition for " + fuid + " in functionMap");
				}
				funs[i++] = index;
			}
			fuids = (IList) ofTuple.get(2);
			int[] constrs = new int[fuids.length()];
			i = 0;
			for (IValue fuid : fuids) {
				Integer index = constructorMap.get(((IString) fuid).getValue());
				if (index == null) {
					throw new RuntimeException("No definition for " + fuid + " in constructorMap");
				}
				constrs[i++] = index;
			}
			this.overloadedStore.add(new OverloadedFunction(funs, constrs, scopeIn));
		}
	}

	/**
	 * Narrow an Object as occurring on the RVM runtime stack to an IValue that can be returned. Note that various non-IValues can occur: - Coroutine - Reference - FunctionInstance -
	 * Object[] (is converted to an IList)
	 * 
	 * @param result
	 *            to be returned
	 * @return converted result or an exception
	 */
	private IValue narrow(Object result) {
		if (result instanceof Boolean) {
			return vf.bool((Boolean) result);
		}
		if (result instanceof Integer) {
			return vf.integer((Integer) result);
		}
		if (result instanceof IValue) {
			return (IValue) result;
		}
		if (result instanceof Thrown) {
			((Thrown) result).printStackTrace(stdout);
			return vf.string(((Thrown) result).toString());
		}
		if (result instanceof Object[]) {
			IListWriter w = vf.listWriter();
			Object[] lst = (Object[]) result;
			for (int i = 0; i < lst.length; i++) {
				w.append(narrow(lst[i]));
			}
			return w.done();
		}
		throw new RuntimeException("PANIC: Cannot convert object back to IValue: " + result);
	}

	public void finalize(Generator codeEmittor) {
		// Finalize the instruction generation of all functions, if needed
		if (!finalized) {
			finalized = true;
			codeEmittor.emitClass("org/rascalmpl/library/experiments/Compiler/RVM/Interpreter", "Running");

			for (Function f : functionStore) {
				f.finalize(codeEmittor, functionMap, constructorMap, resolver, listing);
			}
			// All functions are created

			codeEmittor.emitDynPrelude();
			codeEmittor.emitDynDispatch(functionMap.size());
			
			for (Map.Entry<String, Integer> e : functionMap.entrySet()) {
				String fname = e.getKey();
				codeEmittor.emitDynCaLL(fname, e.getValue());
			}
			codeEmittor.emitDynFinalize();

			int oid = 0;
			for (OverloadedFunction of : overloadedStore) {
				of.finalize(functionMap, oid++);
			}
		}
	}

	public String getFunctionName(int n) {
		for (String fname : functionMap.keySet()) {
			if (functionMap.get(fname) == n) {
				return fname;
			}
		}
		throw new RuntimeException("PANIC: undefined function index " + n);
	}

	public String getConstructorName(int n) {
		for (String cname : constructorMap.keySet()) {
			if (constructorMap.get(cname) == n) {
				return cname;
			}
		}
		throw new RuntimeException("PANIC: undefined constructor index " + n);
	}

	public String getOverloadedFunctionName(int n) {
		for (String ofname : resolver.keySet()) {
			if (resolver.get(ofname) == n) {
				return ofname;
			}
		}
		throw new RuntimeException("PANIC: undefined overloaded function index " + n);
	}

	// Should never be called.
	public IValue executeFunction(String uid_func, IValue[] args) {
		Object o = runner.executeFunction(uid_func, args);
		if (o instanceof Thrown) {
			throw (Thrown) o;
		}
		return narrow(o);
	}

	// Should never be called.
	public IValue executeFunction(FunctionInstance func, IValue[] args) {
		Object o = runner.executeFunction(func, args);

		if (o instanceof Thrown) {
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

	RVMRun runner = null;

	public IValue executeProgram(String uid_main, IValue[] args) {
		boolean profile = false;
		byte[] rvmGenCode = null;
		if (!finalized) {
			Generator codeEmittor = new Generator();
			runner = new RVMRun(vf, ctx, debug, profile);
			finalize(codeEmittor);
			runner.inject(functionStore, overloadedStore, constructorStore, typeStore);
			codeEmittor.dump();
			rvmGenCode = codeEmittor.finalizeCode();

			// Experimental

			// ClassLoader cl = RVMRun.class.getClassLoader() ;

			// try {
			// Class<?> generatedClassV2 = cl.loadClass("org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Running") ;
			// } catch (ClassNotFoundException e1) {
			// e1.printStackTrace();
			// }
			ClassLoader cl = RVM.class.getClassLoader() ;
			Class<?> generatedClassV2 = null;
			try {
				generatedClassV2 = cl.loadClass("org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Running");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			//java.net.URL u  = cl.getResource("org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Runner.class") ;
			try {	
				Object doOIt;
				// System.out.println(u); 
				Constructor<?>[] cons = generatedClassV2.getConstructors();

				doOIt = cons[0].newInstance(vf, ctx, debug, profile);

				((RVMRun) doOIt).inject(functionStore, overloadedStore, constructorStore, typeStore);

				System.out.println("Starting 	to execute!");
				//((IDynamicRun) doOIt).dynRun(uid_main, args);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// End

		Function main_function = functionStore.get(functionMap.get(uid_main));

		if (main_function == null) {
			throw new RuntimeException("PANIC: No function " + uid_main + " found");
		}

		if (main_function.nformals != 2) { // List of IValues and empty map of
											// keyword parameters
			throw new RuntimeException("PANIC: function " + uid_main + " should have two arguments");
		}

		Frame root = new Frame(main_function.scopeId, null, main_function.maxstack, main_function);
		Frame cf = root;
		cf.stack[0] = vf.list(args); // pass the program argument to
										// main_function as a IList object
		cf.stack[1] = vf.mapWriter().done();
		Object o = runner.executeProgram(root, cf);
		if (o != null && o instanceof Thrown) {
			throw (Thrown) o;
		}
		IValue res = narrow(o);
		if (debug) {
			stdout.println("TRACE:");
			stdout.println(getTrace());
		}
		return res;
	}
}
