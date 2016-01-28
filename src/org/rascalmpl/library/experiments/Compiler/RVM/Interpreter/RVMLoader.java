package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.uptr.RascalValueFactory;

import org.nustaq.serialization.FSTConfiguration;

public class RVMLoader {
	
static FSTConfiguration conf;
static FSTSerializableType serializableType;
static FSTSerializableIValue serializableIValue;
static FSTRVMExecutableSerializer rvmExecutableSerializer;
static FSTFunctionSerializer functionSerializer;
static FSTCodeBlockSerializer codeblockSerializer;
	
	static {
		  
		   conf = FSTConfiguration.createDefaultConfiguration();   
		   
		   // PDB Types
		   serializableType = new FSTSerializableType();
		   conf.registerSerializer(FSTSerializableType.class, serializableType, false);
		   
		   // PDB values
		   serializableIValue =  new FSTSerializableIValue();
		   conf.registerSerializer(FSTSerializableIValue.class, serializableIValue, false);
		   
		   // Specific serializers
		   rvmExecutableSerializer = new FSTRVMExecutableSerializer();
		   conf.registerSerializer(RVMExecutable.class, rvmExecutableSerializer, false);
		   
		   functionSerializer = new FSTFunctionSerializer();
		   conf.registerSerializer(Function.class, functionSerializer, false);
		   
		   codeblockSerializer = new FSTCodeBlockSerializer();
		   conf.registerSerializer(CodeBlock.class, codeblockSerializer, false);
		
		   // For efficiency register some class that are known to occur in serialization
		   conf.registerClass(OverloadedFunction.class);
		   //conf.registerClass(FSTSerializableType.class);
		   //conf.registerClass(FSTSerializableIValue.class);
	}   
		   
	
	private IValueFactory vf;
	private TypeFactory tf;
	
	// Functions
	private ArrayList<Function> functionStore;
	private Map<String, Integer> functionMap;
	
	// Constructors
	private ArrayList<Type> constructorStore;
	private Map<String, Integer> constructorMap;
	
	// Function overloading
	private Map<String, Integer> resolver;
	private ArrayList<OverloadedFunction> overloadedStore;
	
	private TypeStore typeStore;
	private final Types types;

	public RVMLoader(IValueFactory vf, TypeStore typeStore) {
		this.vf = vf;
		this.types = new Types(this.vf);
		this.tf = TypeFactory.getInstance();
		this.typeStore = typeStore;
		FSTSerializableType.initSerialization(vf, typeStore);
		FSTSerializableIValue.initSerialization(vf, typeStore);
		FSTRVMExecutableSerializer.initSerialization(vf, typeStore);
		FSTFunctionSerializer.initSerialization(vf, typeStore);
		FSTCodeBlockSerializer.initSerialization(vf, typeStore);
	}
	
	String moduleInit(String moduleName){
		return  "/#" + moduleName + "_init(list(value());)#0";
	}
	
	String muModuleInit(String moduleName){
		return   "/#" + moduleName + "_init";
	}
	
	private void validateInstructionAdressingLimits(){
		int nfs = functionStore.size();
		//System.out.println("size functionStore: " + nfs);
		if(nfs >= CodeBlock.maxArg){
			throw new CompilerError("functionStore size " + nfs + " exceeds limit " + CodeBlock.maxArg1);
		}
		int ncs = constructorStore.size();
		//System.out.println("size constructorStore: " + ncs);
		if(ncs >= CodeBlock.maxArg){
			throw new CompilerError("constructorStore size " + ncs + " exceeds limit " + CodeBlock.maxArg1);
		}
		int nov = overloadedStore.size();
		//System.out.println("size overloadedStore: " + nov);
		if(nov >= CodeBlock.maxArg){
			throw new CompilerError("overloadedStore size " + nov + " exceeds limit " + CodeBlock.maxArg1);
		}
	}
	
	private Integer useFunctionName(String fname){
		Integer index = functionMap.get(fname);
		
		if(index == null){
			index = functionStore.size();
			functionMap.put(fname, index);
			functionStore.add(null);
			//System.out.println("useFunctionName (undef): " + index + "  => " + fname);
		}
		//System.out.println("useFunctionName: " + index + "  => " + fname);
		return index;
	}
	
	private void declareFunction(Function f){
		Integer index = functionMap.get(f.getName());
		if(index == null){
			index = functionStore.size();
			functionMap.put(f.getName(), index);
			functionStore.add(f);
			f.funId = index ;
		} else {
			functionStore.set(index, f);
			f.funId = index ;
		}
		
		//System.out.println("declareFunction: " + index + "  => " + f.getName());
	}
	
	private Integer useConstructorName(String cname) {
		Integer index = constructorMap.get(cname) ;
		if(index == null) {
			index = constructorStore.size();
			constructorMap.put(cname, index);
			constructorStore.add(null);
		}
		//stdout.println("useConstructorName: " + index + "  => " + cname);
		return index;
	}
	
	private void declareConstructor(String cname, IConstructor symbol) {
		
		// TODO: Debatable. We convert the extended form of prod to the simpler one. This
		// should be done earlier
		if(symbol.getConstructorType() == RascalValueFactory.Symbol_Prod){
			//System.out.println("declareConstructor: " + symbol);
			IValue sort = symbol.get("sort");
			IValue parameters = symbol.get("parameters");
			IValue attributes = symbol.get("attributes");
			//constr = tf.constructor(typeStore, Factory.Production_Default, "prod", symbol, "sort", parameters, "parameters",  attributes, "attributes");
			symbol = vf.constructor(RascalValueFactory.Production_Default, sort, parameters, attributes);
		}	
		Type constr = symbolToType(symbol);
		Integer index = constructorMap.get(cname);
		if(index == null) {
			index = constructorStore.size();
			constructorMap.put(cname, index);
			constructorStore.add(constr);
			//typeStore.declareConstructor(constr);
		} else {
			constructorStore.set(index, constr);
		}
		//System.out.println("declareConstructor: " + index + "  => " + cname);
	}
	
	Type symbolToType(IConstructor symbol) {
		return types.symbolToType(symbol, typeStore);
	}
	
	private void addResolver(IMap resolver) {
		for(IValue fuid : resolver) {
			String of = ((IString) fuid).getValue();
			int index = ((IInteger) resolver.get(fuid)).intValue();
			this.resolver.put(of, index);
		}
	}
	
	private void fillOverloadedStore(IList overloadedStore) {
		for(IValue of : overloadedStore) {
			
			ITuple ofTuple = (ITuple) of;
			
			String funName = ((IString) ofTuple.get(0)).getValue();
			
			IConstructor funType = (IConstructor) ofTuple.get(1);
			
			String scopeIn = ((IString) ofTuple.get(2)).getValue();
//			if(scopeIn.equals("")) {					// TODO <<<< Remind Ferry!
//				scopeIn = null;
//			}
			IList fuids = (IList) ofTuple.get(3);		// function alternatives
			int[] funs = new int[fuids.length()];
			int alt = 0;
			for(IValue fuid : fuids) {
				String name = ((IString) fuid).getValue();
				//System.out.println("fillOverloadedStore: add function " + name);
				
				Integer index = useFunctionName(name);
				funs[alt++] = index;
			}
			
			fuids = (IList) ofTuple.get(4);				// constructor alternatives
			int[] constrs = new int[fuids.length()];
			alt = 0;
			for(IValue fuid : fuids) {
				Integer index = useConstructorName(((IString) fuid).getValue());
				constrs[alt++] = index;
			}
			OverloadedFunction res = new OverloadedFunction(functionStore, funs, constrs, scopeIn);
			this.overloadedStore.add(res);
		}
	}
	
	private void validateExecutable(){
		int nfun = functionStore.size();
		if(nfun != functionMap.size()){
			System.err.println("functionStore and functionMap have different size: " + nfun + " vs " + functionMap.size());
		}
		for(String fname : functionMap.keySet()){
			int n = functionMap.get(fname);
			if(functionStore.get(n) == null){
				System.err.println("FunctionStore has null entry for: "+ fname + " at index " + n);
			}
		}
		
		int ncon = constructorStore.size();
		if(ncon != constructorMap.size()){
			System.err.println("constructorStore and constructorMap have different size: " + ncon + " vs " + constructorMap.size());
		}
		for(String cname : constructorMap.keySet()){
			int n = constructorMap.get(cname);
			if(constructorStore.get(n) == null){
				System.err.println("ConstructorStore has null entry for: "+ cname + " at index " + n);
			}
		}
	}
	
	private void validateOverloading(){
		for(String oname : resolver.keySet()){
			int n = resolver.get(oname);
			if(overloadedStore.get(n) == null){
				System.err.println("OverloadedStore has null entry for: "+ oname + " at index " + n);
			}
		}
	}
	
	private void finalizeInstructions(){
		int i = 0;
		for(String fname : functionMap.keySet()){
			if(functionMap.get(fname) == null){
				System.out.println("finalizeInstructions, null for function : " + fname);
			}
		}
		for(Function f : functionStore) {
			if(f == null){
				String nameAtIndex = "**unknown**";
				for(String fname : functionMap.keySet()){
					if(functionMap.get(fname) == i){
						nameAtIndex = fname;
						break;
					}
				}
				System.out.println("finalizeInstructions, null at index: " + i + ", " + nameAtIndex);
			} else {
				//System.out.println("finalizeInstructions: " + f.name);
			}
			f.finalize(functionMap, constructorMap, resolver);
			i++;
		}
		for(OverloadedFunction of : overloadedStore) {
			of.finalize(functionMap, functionStore);
		}
	}
	
	public RVMExecutable load(IConstructor program, boolean jvm) {
		
		long start = Timing.getCpuTime();
		
		functionStore = new ArrayList<Function>();
		constructorStore = new ArrayList<Type>();

		functionMap = new HashMap<String, Integer>();
		constructorMap = new HashMap<String, Integer>();
		
		resolver = new HashMap<String,Integer>();
		overloadedStore = new ArrayList<OverloadedFunction>();
		
		IMap imported_module_tags = (IMap) program.get("imported_module_tags");
		IConstructor main_module = (IConstructor) program.get("main_module");
		IMap moduleTags = imported_module_tags.put(main_module.get("name"), main_module.get("module_tags"));

		/** Imported types */

		IMap imported_types = (IMap) program.get("imported_types");
		Iterator<Entry<IValue, IValue>> entries = imported_types.entryIterator();
		while(entries.hasNext()) {
			Entry<IValue, IValue> entry = entries.next();
			declareConstructor(((IString) entry.getKey()).getValue(), (IConstructor) entry.getValue());
		}

		/** Imported functions */
		
		ArrayList<String> initializers = new ArrayList<String>();  	// initializers of imported modules
		ArrayList<String> testsuites =  new ArrayList<String>();	// testsuites of imported modules

		IList imported_declarations = (IList) program.get("imported_declarations");
		for(IValue imp : imported_declarations){
			IConstructor declaration = (IConstructor) imp;
			if (declaration.getName().contentEquals("FUNCTION")) {
				String name = ((IString) declaration.get("qname")).getValue();

				//System.out.println("IMPORTED FUNCTION: " + name);
				
				if(name.endsWith("_init(list(value());)#0")){
					initializers.add(name);
				}
				if(!name.endsWith("_testsuite(list(value());)#0")){
					//testsuites.add(name);
					loadInstructions(name, declaration, false);
				}
			}
			if (declaration.getName().contentEquals("COROUTINE")) {
				String name = ((IString) declaration.get("qname")).getValue();
				loadInstructions(name, declaration, true);
			}
		}

		/** Types from main module */

		IMap types = (IMap) main_module.get("types");
		entries = types.entryIterator();
		while(entries.hasNext()) {
			Entry<IValue, IValue> entry = entries.next();
			declareConstructor(((IString) entry.getKey()).getValue(), (IConstructor) entry.getValue());
		}

		/** Declarations for  main module */
		
		String moduleName = ((IString) main_module.get("name")).getValue();
		
		String main = "/main()#0";
		String main_testsuite = /*"/" + moduleName + */ "_testsuite()#0";
		
		String mu_main = "/MAIN";
		String mu_main_testsuite = "/TESTSUITE";
	
		
		String module_init = moduleInit(moduleName);
		String mu_module_init = muModuleInit(moduleName);

		String uid_module_main = "";
		String uid_module_init = "";
		String uid_module_main_testsuite = "";

		IList declarations = (IList) main_module.get("declarations");
		for (IValue ideclaration : declarations) {
			IConstructor declaration = (IConstructor) ideclaration;

			if (declaration.getName().contentEquals("FUNCTION")) {
				String name = ((IString) declaration.get("qname")).getValue();
				
				//System.out.println("FUNCTION: " + name);
				
				if(name.endsWith(main) || name.endsWith(mu_main)) {
					uid_module_main = name;					// Get main's uid in current module
				}
				
				if(name.endsWith(main_testsuite) || name.endsWith(mu_main_testsuite)) {
					uid_module_main_testsuite = name;		// Get  testsuite's main uid in current module
				}
				
				if(name.endsWith(module_init) || name.endsWith(mu_module_init)) {
					uid_module_init = name;					// Get module_init's uid in current module
				}
				if(name.endsWith("_testsuite(list(value());)#0")){
					testsuites.add(name);
				}
				loadInstructions(name, declaration, false);
			}

			if(declaration.getName().contentEquals("COROUTINE")) {
				String name = ((IString) declaration.get("qname")).getValue();
				loadInstructions(name, declaration, true);
			}
		}

		/** Overloading resolution */

		// Overloading resolution of imported functions
		IMap imported_overloading_resolvers = (IMap) program.get("imported_overloading_resolvers");
		addResolver(imported_overloading_resolvers);
		
		IList imported_overloaded_functions = (IList) program.get("imported_overloaded_functions");
		fillOverloadedStore(imported_overloaded_functions);

		// Overloading resolution of functions in main module
		addResolver((IMap) main_module.get("resolver"));
		fillOverloadedStore((IList) main_module.get("overloaded_functions"));

		/** Finalize & validate */

		finalizeInstructions();

		validateInstructionAdressingLimits();

		validateExecutable();

		validateOverloading();

		System.out.println("Loading: " +  (Timing.getCpuTime() - start)/1000000 + " ms");
		return new RVMExecutable(((IString) main_module.get("name")).getValue(),
							     moduleTags,
								 (IMap) main_module.get("symbol_definitions"),
								 functionMap, 
								 functionStore, 
								 constructorMap, 
								 constructorStore,	
								 resolver, 
								 overloadedStore,  
								 initializers, 
								 testsuites, 
								 uid_module_init, 
								 uid_module_main, 
								 uid_module_main_testsuite,
								 typeStore,
								 vf,
								 jvm);
	}
	
	/*
	 * Utility function for instruction loading
	 */
	
	// Get Boolean field from an instruction

	private boolean getBooleanField(IConstructor instruction, String field) {
		return ((IBool) instruction.get(field)).getValue();
	}

	// Get integer field from an instruction

	private int getIntField(IConstructor instruction, String field) {
		return ((IInteger) instruction.get(field)).intValue();
	}

	// Get String field from an instruction

	private String getStrField(IConstructor instruction, String field) {
		return ((IString) instruction.get(field)).getValue();
	}
	
	// Get Location field from an instruction

	private ISourceLocation getLocField(IConstructor instruction, String field) {
		return ((ISourceLocation) instruction.get(field));
	}
	private IList getListField(IConstructor instruction, String field) {
		return ((IList) instruction.get(field));
	}

	/**
	 * Load the instructions of a function in a RVM.
	 * 
	 * @param name of the function to be loaded
	 * @param declaration the declaration of that function
	 * @param rvm in which function will be loaded
	 */
	private void loadInstructions(String name, IConstructor declaration, boolean isCoroutine){
	
		int continuationPoints = 0 ;
		Type ftype = isCoroutine ? tf.voidType() : symbolToType((IConstructor) declaration.get("ftype"));
		
		
		String scopeIn = ((IString) declaration.get("scopeIn")).getValue();
//		if(scopeIn.equals("")) {
//			scopeIn = null;
//		}
		
		Integer nlocals = ((IInteger) declaration.get("nlocals")).intValue();
		IMap localNames = ((IMap) declaration.get("localNames"));
		Integer nformals = ((IInteger) declaration.get("nformals")).intValue();
		
		Integer maxstack = ((IInteger) declaration.get("maxStack")).intValue();
		IList code = (IList) declaration.get("instructions");
		ISourceLocation src = (ISourceLocation) declaration.get("src");
		boolean isDefault = false;
		boolean isConcreteArg = false;
		int abstractFingerprint = 0;
		int concreteFingerprint = 0;
		if(!isCoroutine){
			isDefault = ((IBool) declaration.get("isDefault")).getValue();
			isConcreteArg = ((IBool) declaration.get("isConcreteArg")).getValue();
			abstractFingerprint = ((IInteger) declaration.get("abstractFingerprint")).intValue();
			concreteFingerprint = ((IInteger) declaration.get("concreteFingerprint")).intValue();
		}
		CodeBlock codeblock = new CodeBlock(name, vf);
		// Loading instructions
		try {
		for (int i = 0; i < code.length(); i++) {
			IConstructor instruction = (IConstructor) code.get(i);
			String opcode = instruction.getName();

			switch (opcode) {
			case "LOADCON":
				codeblock.LOADCON(instruction.get("val"));
				break;
				
			case "PUSHCON":
				codeblock.PUSHCON(instruction.get("val"));
				break;

			case "LOADVAR":
				codeblock.LOADVAR(getStrField(instruction, "fuid"), 
								  getIntField(instruction, "pos"));
				break;
				
			case "PUSHVAR":
				codeblock.PUSHVAR(getStrField(instruction, "fuid"), 
								  getIntField(instruction, "pos"));
				break;

			case "LOADLOC":
				codeblock.LOADLOC(getIntField(instruction, "pos"));
				break;
				
			case "PUSHLOC":
				codeblock.PUSHLOC(getIntField(instruction, "pos"));
				break;

			case "STOREVAR":
				codeblock.STOREVAR(getStrField(instruction, "fuid"), 
								   getIntField(instruction, "pos"));
				break;
				
			case "RESETVAR":
				codeblock.RESETVAR(getStrField(instruction, "fuid"), 
								   getIntField(instruction, "pos"));
				break;

			case "STORELOC":
				codeblock.STORELOC(getIntField(instruction, "pos"));
				break;
				
			case "RESETLOC":
				codeblock.RESETLOC(getIntField(instruction, "pos"));
				break;

			case "LABEL":
				codeblock = codeblock.LABEL(getStrField(instruction, "label"));
				break;

			case "CALL":
				codeblock.CALL(getStrField(instruction, "fuid"), getIntField(instruction, "arity"),++continuationPoints);
				break;

			case "CALLDYN":
				codeblock.CALLDYN( getIntField(instruction, "arity"), ++continuationPoints);
				break;
				
			case "APPLY":
				codeblock.APPLY(getStrField(instruction, "fuid"), 
								getIntField(instruction, "arity"));
				break;
				
			case "APPLYDYN":
				codeblock.APPLYDYN(getIntField(instruction, "arity"));
				break;

			case "LOADFUN":
				codeblock.LOADFUN(getStrField(instruction, "fuid"));
				break;

			case "RETURN0":
				codeblock.RETURN0();
				break;

			case "RETURN1":
				codeblock.RETURN1(getIntField(instruction, "arity"));
				break;

			case "JMP":
				codeblock.JMP(getStrField(instruction, "label"));
				break;

			case "JMPTRUE":
				codeblock.JMPTRUE(getStrField(instruction, "label"));
				break;

			case "JMPFALSE":
				codeblock.JMPFALSE(getStrField(instruction, "label"));
				break;

			case "HALT":
				codeblock.HALT();
				break;
				
			case "CREATE":
				codeblock.CREATE(getStrField(instruction, "fuid"), 
								 getIntField(instruction, "arity"));
				break;

			case "CREATEDYN":
				codeblock.CREATEDYN(getIntField(instruction, "arity"));
				break;

			case "NEXT0":
				codeblock.NEXT0();
				break;

			case "NEXT1":
				codeblock.NEXT1();
				break;

			case "YIELD0":
				codeblock.YIELD0(++continuationPoints);
				break;

			case "YIELD1":
				codeblock.YIELD1(getIntField(instruction, "arity"),++continuationPoints);
				break;

			case "PRINTLN":
				codeblock.PRINTLN(getIntField(instruction, "arity"));
				break;

			case "POP":
				codeblock.POP();
				break;

			case "LOADLOCREF":
				codeblock.LOADLOCREF(getIntField(instruction, "pos"));
				break;
				
			case "PUSHLOCREF":
				codeblock.PUSHLOCREF(getIntField(instruction, "pos"));
				break;

			case "LOADVARREF":
				codeblock.LOADVARREF(getStrField(instruction, "fuid"), 
									 getIntField(instruction, "pos"));
				break;
				
			case "PUSHVARREF":
				codeblock.PUSHVARREF(getStrField(instruction, "fuid"), 
									 getIntField(instruction, "pos"));
				break;

			case "LOADLOCDEREF":
				codeblock.LOADLOCDEREF(getIntField(instruction, "pos"));
				break;
				
			case "PUSHLOCDEREF":
				codeblock.PUSHLOCDEREF(getIntField(instruction, "pos"));
				break;

			case "LOADVARDEREF":
				codeblock.LOADVARDEREF(getStrField(instruction, "fuid"), 
									   getIntField(instruction, "pos"));
				break;
				
			case "PUSHVARDEREF":
				codeblock.PUSHVARDEREF(getStrField(instruction, "fuid"), 
									   getIntField(instruction, "pos"));
				break;

			case "STORELOCDEREF":
				codeblock.STORELOCDEREF(getIntField(instruction, "pos"));
				break;

			case "STOREVARDEREF":
				codeblock.STOREVARDEREF(getStrField(instruction, "fuid"), 
										getIntField(instruction, "pos"));
				break;

			case "LOAD_NESTED_FUN":
				codeblock.LOADNESTEDFUN(getStrField(instruction, "fuid"), 
										getStrField(instruction, "scopeIn"));
				break;

			case "LOADCONSTR":
				codeblock.LOADCONSTR(getStrField(instruction, "fuid"));
				break;

			case "CALLCONSTR":
				codeblock.CALLCONSTR(getStrField(instruction, "fuid"), 
									 getIntField(instruction, "arity")/*, getLocField(instruction, "src")*/);
				break;

			case "LOADTYPE":
				codeblock.LOADTYPE(symbolToType((IConstructor) instruction.get("type")));
				break;
				
			case "PUSHTYPE":
				codeblock.PUSHTYPE(symbolToType((IConstructor) instruction.get("type")));
				break;
				
			case "LOADBOOL":
				codeblock.LOADBOOL(getBooleanField(instruction, "bval"));
				break;

			case "LOADINT":
				codeblock.LOADINT(getIntField(instruction, "nval"));
				break;

			case "FAILRETURN":
				codeblock.FAILRETURN();
				break;

			case "LOADOFUN" :
				codeblock.LOADOFUN(getStrField(instruction, "fuid"));
				break;

			case "OCALL" :
				codeblock.OCALL(getStrField(instruction, "fuid"), 
								getIntField(instruction, "arity"), 
								getLocField(instruction, "src"));
				break;

			case "OCALLDYN" :
				codeblock.OCALLDYN(symbolToType((IConstructor) instruction.get("types")), 
								   getIntField(instruction, "arity"), 
								   getLocField(instruction, "src"));
				break;

			case "CALLJAVA":
				codeblock.CALLJAVA(getStrField(instruction, "name"), 
						           getStrField(instruction, "class"), 
						 		   symbolToType((IConstructor) instruction.get("parameterTypes")), 
						 		   symbolToType((IConstructor) instruction.get("keywordTypes")), 
						 		   getIntField(instruction, "reflect"));
				break;

			case "THROW":
				codeblock.THROW(getLocField(instruction, "src"));
				break;
			
			case "TYPESWITCH":
				codeblock.TYPESWITCH((IList)instruction.get("labels"));
				break;
				
			case "UNWRAPTHROWNLOC":
				codeblock.UNWRAPTHROWNLOC(getIntField(instruction, "pos"));
				break;
				
			case "FILTERRETURN":
				codeblock.FILTERRETURN();
				break;
				
			case "EXHAUST":
				codeblock.EXHAUST();
				break;
				
			case "GUARD":
				codeblock.GUARD(++continuationPoints);
				break;
				
			case "SUBSCRIPTARRAY":
				codeblock.SUBSCRIPTARRAY();
				break;
				
			case "SUBSCRIPTLIST":
				codeblock.SUBSCRIPTLIST();
				break;
				
			case "LESSINT":
				codeblock.LESSINT();
				break;
				
			case "GREATEREQUALINT":
				codeblock.GREATEREQUALINT();
				break;
				
			case "ADDINT":
				codeblock.ADDINT();
				break;
				
			case "SUBTRACTINT":
				codeblock.SUBTRACTINT();
				break;
				
			case "ANDBOOL":
				codeblock.ANDBOOL();
				break;
				
			case "TYPEOF":
				codeblock.TYPEOF();
				break;
				
			case "SUBTYPE":
				codeblock.SUBTYPE();
				break;
				
			case "CHECKARGTYPEANDCOPY":
				codeblock.CHECKARGTYPEANDCOPY(getIntField(instruction, "pos1"),
									  symbolToType((IConstructor) instruction.get("type")),
									  getIntField(instruction, "pos2"));
				break;
				
//			case "JMPINDEXED":
//				codeblock.JMPINDEXED((IList)instruction.get("labels"));
//				break;
				
			case "LOADLOCKWP":
				codeblock.LOADLOCKWP(getStrField(instruction, "name"));
				break;
				
			case "PUSHLOCKWP":
				codeblock.PUSHLOCKWP(getStrField(instruction, "name"));
				break;
				
			case "LOADVARKWP":
				codeblock.LOADVARKWP(getStrField(instruction, "fuid"), 
									 getStrField(instruction, "name"));
				break;
				
			case "PUSHVARKWP":
				codeblock.PUSHVARKWP(getStrField(instruction, "fuid"), 
									 getStrField(instruction, "name"));
				break;
				
			case "STORELOCKWP":
				codeblock.STORELOCKWP(getStrField(instruction, "name"));
				break;
				
			case "STOREVARKWP":
				codeblock.STOREVARKWP(getStrField(instruction, "fuid"), 
									  getStrField(instruction, "name"));
				break;
				
			case "UNWRAPTHROWNVAR":
				codeblock.UNWRAPTHROWNVAR(getStrField(instruction, "fuid"), 
									      getIntField(instruction, "pos"));
				break;
			
			case "SWITCH":
				codeblock.SWITCH((IMap)instruction.get("caseLabels"),
								 getStrField(instruction, "caseDefault"),
								 getBooleanField(instruction, "useConcreteFingerprint"));
				break;
				
			case "RESETLOCS":
				codeblock.RESETLOCS(getListField(instruction, "positions"));
				break;
				
			case "VISIT":
				codeblock.VISIT(getBooleanField(instruction, "direction"),
								getBooleanField(instruction, "fixedpoint"),
								getBooleanField(instruction, "progress"),
								getBooleanField(instruction, "rebuild"));
				break;
				
			case "CHECKMEMO":
				codeblock.CHECKMEMO();
				break;
				
			case "LOADEMPTYKWMAP":
				codeblock.LOADEMPTYKWMAP();
				break;
				
			case "VALUESUBTYPE":
				codeblock.VALUESUBTYPE(symbolToType((IConstructor) instruction.get("type")));
				break;
				
			case "CALLMUPRIM0":
				codeblock.CALLMUPRIM0(MuPrimitive.valueOf(getStrField(instruction, "name")));
				break;
				
			case "PUSHCALLMUPRIM0":
				codeblock.PUSHCALLMUPRIM0(MuPrimitive.valueOf(getStrField(instruction, "name")));
				break;
				
			case "CALLMUPRIM1":
				codeblock.CALLMUPRIM1(MuPrimitive.valueOf(getStrField(instruction, "name")));
				break;
				
			case "PUSHCALLMUPRIM1":
				codeblock.PUSHCALLMUPRIM1(MuPrimitive.valueOf(getStrField(instruction, "name")));
				break;
				
			case "CALLMUPRIM2":
				codeblock.CALLMUPRIM2(MuPrimitive.valueOf(getStrField(instruction, "name")));
				break;
				
			case "PUSHCALLMUPRIM2":
				codeblock.PUSHCALLMUPRIM2(MuPrimitive.valueOf(getStrField(instruction, "name")));
				break;
				
			case "CALLMUPRIMN":
				codeblock.CALLMUPRIMN(MuPrimitive.valueOf(getStrField(instruction, "name")), 
									 getIntField(instruction, "arity"));
				break;
				
			case "PUSHCALLMUPRIMN":
				codeblock.PUSHCALLMUPRIMN(MuPrimitive.valueOf(getStrField(instruction, "name")), 
									 getIntField(instruction, "arity"));
				break;
			
			case "CALLPRIM0":
				codeblock.CALLPRIM0(RascalPrimitive.valueOf(getStrField(instruction, "name")), 
								   getLocField(instruction, "src"));
				break;
				
			case "PUSHCALLPRIM0":
				codeblock.PUSHCALLPRIM0(RascalPrimitive.valueOf(getStrField(instruction, "name")), 
								   getLocField(instruction, "src"));
				break;
				
			case "CALLPRIM1":
				codeblock.CALLPRIM1(RascalPrimitive.valueOf(getStrField(instruction, "name")), 
								   getLocField(instruction, "src"));
				break;
				
			case "PUSHCALLPRIM1":
				codeblock.PUSHCALLPRIM1(RascalPrimitive.valueOf(getStrField(instruction, "name")), 
								   getLocField(instruction, "src"));
				break;
				
			case "CALLPRIM2":
				codeblock.CALLPRIM2(RascalPrimitive.valueOf(getStrField(instruction, "name")), 
								   getLocField(instruction, "src"));
				break;
				
			case "PUSHCALLPRIM2":
				codeblock.PUSHCALLPRIM2(RascalPrimitive.valueOf(getStrField(instruction, "name")), 
								   getLocField(instruction, "src"));
				break;
				
			case "CALLPRIMN":
				codeblock.CALLPRIMN(RascalPrimitive.valueOf(getStrField(instruction, "name")), 
								   getIntField(instruction, "arity"), 
								   getLocField(instruction, "src"));
				break;
				
			case "PUSHCALLPRIMN":
				codeblock.PUSHCALLPRIMN(RascalPrimitive.valueOf(getStrField(instruction, "name")), 
								   getIntField(instruction, "arity"), 
								   getLocField(instruction, "src"));
				break;
			
			case "POPACCU":
				codeblock.POPACCU();
				break;
				
			case "PUSHACCU":
				codeblock.PUSHACCU();
				break;
				
			default:
				throw new CompilerError("In function " + name + ", unknown instruction: " + opcode);
			}

		}
		} catch (Exception e){
			throw new CompilerError("In function " + name + " : " + e.getMessage());
		}
		
		
		Function function = new Function(name, 
										 ftype, 
										 scopeIn, 
										 nformals, 
										 nlocals, 
										 isDefault, 
										 localNames, 
										 maxstack,
										 isConcreteArg,
										 abstractFingerprint,
										 concreteFingerprint, 
										 codeblock, src, continuationPoints);
		
		IList exceptions = (IList) declaration.get("exceptions");
		function.attachExceptionTable(exceptions, this);
		
		if(isCoroutine) {
			function.isCoroutine = true;
			IList refList = (IList) declaration.get("refs");
			int[] refs = new int[refList.length()];
			int i = 0;
			for(IValue ref : refList) {
				refs[i++] = ((IInteger) ref).intValue();
			}
			function.refs = refs;
		} else {
			
			boolean isVarArgs = ((IBool) declaration.get("isVarArgs")).getValue();
			function.isVarArgs = isVarArgs;
		}
		declareFunction(function);
	}
}
