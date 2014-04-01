package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

public class RVM {

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

	IEvaluatorContext ctx;

	public RVM(IValueFactory vf, IEvaluatorContext ctx, boolean debug, boolean profile) {
		super();

		this.vf = vf;

		this.ctx = ctx;
		this.stdout = ctx.getStdOut();
		this.stderr = ctx.getStdErr();
		this.debug = debug;
		this.finalized = false;

		this.types = new Types(this.vf);

		functionStore = new ArrayList<Function>();
		constructorStore = new ArrayList<Type>();

		functionMap = new HashMap<String, Integer>();
		constructorMap = new HashMap<String, Integer>();

		resolver = new HashMap<String, Integer>();
		overloadedStore = new ArrayList<OverloadedFunction>();
	}

	public RVM(IValueFactory vf) {
		this(vf, null, false, false);
	}

	public void declare(Function f) {
		if (functionMap.get(f.getName()) != null) {
			throw new RuntimeException("PANIC: Double declaration of function: " + f.getName());
		}
		int fss = functionStore.size() ;
		f.funId = fss ;   // ID of function to find entry in dynrun
		functionMap.put(f.getName(), fss);
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

			// All functions are created create int based dispatcher
			codeEmittor.emitDynDispatch(functionMap.size());
			
			for (Map.Entry<String, Integer> e : functionMap.entrySet()) {
				String fname = e.getKey();
				codeEmittor.emitDynCaLL(fname, e.getValue());
			}
			codeEmittor.emitDynFinalize();

			int oid = 0;
			for (OverloadedFunction of : overloadedStore) {
				of.finalize(codeEmittor, functionMap, oid++);
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
			try {
				// runner = new RVMRun(vf, ctx, debug, profile);
				// runner.inject(functionStore, overloadedStore, constructorStore, typeStore);
				String packageName = "org.rascalmpl.library.experiments.Compiler.RVM.Interpreter";
				String className = "Running";

				Generator codeEmittor = new Generator(packageName, className);

				finalize(codeEmittor);
				rvmGenCode = codeEmittor.finalizeCode();

				// codeEmittor.dump("/Users/ferryrietveld/rasdev/rascal/bin/org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Running.class");
				// codeEmittor.dump("/Users/ferryrietveld/Running.class");
				codeEmittor.dump("/Running.class");

				// Oneshot classloader
				Class<?> generatedClassV1 = new ClassLoader(RVM.class.getClassLoader()) {
					public Class<?> defineClass(String name, byte[] bytes) {
						return super.defineClass(name, bytes, 0, bytes.length);
					}

					public Class<?> loadClass(String name) {
						try {
							return super.loadClass(name);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						return null;
					}
				}.defineClass("org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Running", rvmGenCode);

				// Experimental

				// ClassLoader cl = RVM.class.getClassLoader();
				// Class<?> generatedClassV2 = null;
				// generatedClassV2 = cl.loadClass("org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Running");

				// Constructor<?>[] cons1 = generatedClassV1.getConstructors();
				Constructor<?>[] cons2 = generatedClassV1.getConstructors();

				runner = (RVMRun) cons2[0].newInstance(vf, ctx, debug, profile);
				// runner = new RVMRun(vf, ctx, profile, profile) ;
				runner.inject(functionStore, overloadedStore, constructorStore, typeStore, functionMap);

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

		Object o = null;
		if (uid_main.contains("Simple/main")) {
			o = runner.dynRun(uid_main, args);
		} else {
			o = runner.executeProgram(root, cf);
		}
		if (o != null && o instanceof Thrown) {
			throw (Thrown) o;
		}
		IValue res = narrow(o);
		if (debug) {
			stdout.println("TRACE:");
			stdout.println(getTrace());
		}
		// runner = null ;
		return res;
	}
}
