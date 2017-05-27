package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.CompilerIDs;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.IRVMWireInputStream;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.IRVMWireOutputStream;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.binary.util.WindowSizes;
import io.usethesource.vallang.io.binary.wire.IWireInputStream;
import io.usethesource.vallang.type.Type;

/**
 * Represent one overloaded function given
 * - a list of function alternatives
 * - a list of constructor alternatives
 * - a scope
 * 
 * An overloaded function is created during loading and is consulted at run-time
 * to create an OverloadedFunctionInstance for each specific call.
 * 
 * OverloadedFuntion is serialized by write and read defined here
 *
 */
public class OverloadedFunction {
	
	final String name;
	final Type funType;
	int[] functions;
	final int[] constructors;
	final String funIn;
	int scopeIn = -1;
	boolean allConcreteFunctionArgs = false;
	boolean allConcreteConstructorArgs = false;
	Map<Integer, int[]> filteredFunctions;		// Functions and constructors filtered on fingerprint of first argument
	Map<Integer, int[]> filteredConstructors;
	
	public OverloadedFunction(String name, Type funType, final int[] functions, final int[] constructors, final String funIn) {
		if(funIn == null){
			System.out.println("OverloadedFunction: funIn is null");
		}
		this.name = name;
		this.funType = funType;
		this.functions = functions;
		this.constructors = constructors;
		this.funIn = funIn;
	}
	
	public OverloadedFunction(String name, Type funType, int[] functions, int[] constructors, String funIn,
			int scopeIn, boolean allConcreteFunctionArgs, boolean allConcreteConstructorArgs,
			Map<Integer, int[]> filteredFunctions, Map<Integer, int[]> filteredConstructors) {
		this.name = name;
		this.funType = funType;
		this.functions = functions;
		this.constructors = constructors;
		this.funIn = funIn;
		this.scopeIn = scopeIn;
		this.allConcreteFunctionArgs = allConcreteFunctionArgs;
		this.allConcreteConstructorArgs = allConcreteConstructorArgs;
		this.filteredFunctions = filteredFunctions;
		this.filteredConstructors = filteredConstructors;
	}

	public boolean matchesNameAndSignature(String name, Type funType){
	  if(!this.name.equals(name)){
	    return false;
	  }
	  if(this.funType.comparable(funType)){
	    return true;
	  }
	  if(this.funType instanceof OverloadedFunctionType){
	    OverloadedFunctionType oft = (OverloadedFunctionType) this.funType;
	    for(Type ft : oft.getAlternatives()){
	      if(ft.comparable(funType)){
	        return true;
	      }
	    }
	  }
	  return false;
	}
	
	public String getName() {
	    return name;
	}
	
	public int getArity(){
		return funType.getArity();
	}
	
	public void  finalize(final Map<String, Integer> functionMap, ArrayList<Function> functionStore, Map<Integer, Integer> indexMap){
		if(funIn.length() > 0){ // != null) {
			Integer si = functionMap.get(funIn);
			if(si == null){		// Give up, containing scope is not included in final RVM image created by loader
				return;
			}
			this.setScopeIn(si);
		}
		if(indexMap != null){
			int nelems = 0;
			for(int i = 0; i < functions.length; i++){
				Integer newIndex = indexMap.get(functions[i]);
				if(newIndex != null){
					functions[nelems++] = newIndex;
				}
			}
			if(functions.length > nelems){
				int[] newFunctions = new int[nelems];
				for(int i = 0; i < nelems; i++){
					newFunctions[i] = functions[i];
				}
				functions = newFunctions;
			}
		}
		// TODO: temp consistency tests
		for(int fid : functions){
			if(fid < 0 || fid >= functionStore.size()){
				System.err.println("OverloadedFunction: function outside functionStore: " + fid);
			}
		}
//		for(int cid : constructors){
//			if(cid < 0 || cid >= functionStore.size()){
//				System.err.println("OverloadedFunction: constructor outside functionStore: " + cid);
//			}
//		}
		filter(functionStore);
	}
	
	void printArray(){
		System.out.print("[ ");for(int i : functions) System.out.print(i + " "); System.out.println("]");
	}
	
	boolean compareIntArrays(int[] a, int[] b){
		if(a == null && b != null){
			System.out.println("Array a null, b is not null");
			return false;
		}
		if(a != null && b == null){
			System.out.println("Array a not null, b is null");
			return false;
		}
		if(a == null && b == null){
			System.out.println("Array a  null, b is null");
			return false;
		}
		if(a.length != b.length){
			System.out.println("Different length: " + a.length + " vs " + b.length);
		}
		for(int i = 0; i < a.length; i++){
			if(a[i] != b[i]){
				System.out.println("Differ at index " + i);
				return false;
			}
		}
		return true;
	}
	boolean compareIntMaps(Map<Integer, int[]> a, Map<Integer, int[]> b){
		if(a.size() != b.size()){
			System.out.println("Different length: " + a.size() + " vs " + b.size());
		}
		for(int i : a.keySet()){
			if(!compareIntArrays(a.get(i), b.get(i))){
				System.out.println("Maps differ at index " + i);
				return false;
			}
		}
		return true;
	}
	
	public boolean comparable(OverloadedFunction other){
		if(!compareIntArrays(functions, other.functions)){
			System.out.println("functions differ: " + functions + " vs " + other.functions);
			printArray();	System.out.print(" vs "); printArray();
			return false;
		}
		if(!compareIntArrays(constructors, other.constructors)){
			System.out.println("constructors differ: " + constructors + " vs " + other.constructors);
			printArray();	System.out.print(" vs "); printArray();
			return false;
		}
		if(!funIn.equals(other.funIn)){
			System.out.println("funIn differ");
			return false;
		}
		if(getScopeIn() != other.getScopeIn()){
			System.out.println("scopeIn differ");
			return false;
		}
		if(allConcreteFunctionArgs != other.allConcreteFunctionArgs){
			System.out.println("allConcreteFunctionArgs differ");
			return false;
		}
		if(allConcreteConstructorArgs != other.allConcreteConstructorArgs){
			System.out.println("allConcreteConstructorArgs differ");
			return false;
		}
		if(filteredFunctions != null && other.filteredFunctions != null && !compareIntMaps(filteredFunctions, other.filteredFunctions)){
			System.out.println("filteredFunctions differ");
			return false;
		}
		if(filteredConstructors != null && other.filteredConstructors != null && !compareIntMaps(filteredConstructors, other.filteredConstructors)){
			System.out.println("filteredConstructors differ");
			return false;
		}
		return true;
	}
	
	/**
	 * Get the overloaded alternatives for this function given (first) actual parameter args0
	 * @param arg0	first actual parameter
	 * @return	list of overloadings
	 */
	public int[] getFunctions(Object arg0){
		if(functions.length <= 1 || !(arg0 instanceof IValue)){
			return functions;
		}

		int fp = ToplevelType.getFingerprint((IValue) arg0, allConcreteFunctionArgs);
		int[] funs = filteredFunctions.get(fp);
		return funs == null ? filteredFunctions.get(0) : funs;
	}
	
	/**
	 * Get the overloaded constructor alternatives for this function given (first) actual parameter args0
	 * @param arg0	first actual parameter
	 * @return	list of overloadings
	 * 
	 * Note: at the moment we do not (yet?) filter constructor alternatives
	 */
	public int[] getConstructors(Object arg0) {
			return constructors;
	}
	
	private void filter(ArrayList<Function> functionStore){
		filterFunctions(functionStore);
		//filterConstructors(rvm);
	}
	
	/**
	 * Preprocess all overloadings to make run-time lookup as fast as possible:
	 * - All overloads with the same fingerprint are collected and placed in a table.
	 * - All overloads that (a) map to fingerprint 0 (b) are a default functions are
	 *   are placed at the end of the list of alternatives for each fingerprint.
	 *   
	 * @param rvm	needed for access to the function declarations via rvm.functionStore
	 */
	private void filterFunctions(ArrayList<Function> functionStore){
		if(functions.length > 1){
			filteredFunctions = new HashMap<Integer,int[]>();
		} else {
			return;
		}
		// Determine whether all first arguments are concrete
		allConcreteFunctionArgs = true;
		HashMap<Integer, ArrayList<Integer>> filtered = new HashMap<Integer, ArrayList<Integer>>();
		for(int fid : functions){
			Function fdef = functionStore.get(fid);
			if(!fdef.concreteArg){
				allConcreteFunctionArgs = false;
			}
		}
		// Collect applicable functions per fingerprint
		for(int fid : functions){
			Function fdef = functionStore.get(fid);
			int fp = fdef.isDefault ? 0 : (allConcreteFunctionArgs ? fdef.concreteFingerprint : fdef.abstractFingerprint);
			ArrayList<Integer> alts = filtered.get(fp);
			if(alts == null){
				alts = new ArrayList<Integer>();
				filtered.put(fp,  alts);
			}
			alts.add(fid);
		}
		
		// Transform the table into one with the required values of type: int[]
		// On the fly attach all default functions to each entry
		ArrayList<Integer> defaults = filtered.get(0);
		if(defaults == null){
			defaults = new ArrayList<Integer>();
			filtered.put(0,  defaults);
		}
		int ndefaults = defaults.size();
		
		// TODO: values in alts may also occur in defaults, then the list will contain duplicate elements
		for(int fp : filtered.keySet()){
			ArrayList<Integer> alts = filtered.get(fp);
			int nalts = alts.size();
			
            //defaults.removeIf(x -> alts.contains(x));
			
			int[] funs = new int[nalts + ndefaults];
			for(int i = 0; i < nalts; i++){
				funs[i] = alts.get(i);
			}
			for(int i = 0; i < ndefaults; i++){
				funs[nalts + i] = defaults.get(i);
			}
			filteredFunctions.put(fp, funs);
		}
	}
	
	// The next four members are needed by the bytecode generator.
	public int[] getFunctions() {
		return functions;
	}

	public int[] getConstructors() {
		return constructors;
	}
	
	public int getScope() {
		return getScopeIn() ;
	}
	
	public String getScopeFun() {
		return funIn ;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("Overloaded[" + name + ":" + funType + ":");
		if(functions.length > 0){
			sb.append("functions:");
			for(int i = 0; i < functions.length; i++){
				sb.append(" ").append(functions[i]);
			}
			sb.append(" allConcreteFunctionArgs=").append(allConcreteFunctionArgs);
		}
		if(constructors.length > 0){
			if(functions.length > 0){
				sb.append("; ");
			}
			sb.append("constructors:");
			for(int i = 0; i < constructors.length; i++){
				sb.append(" ").append(constructors[i]);
			}
			sb.append(" allConcreteConstructorArgs=").append(allConcreteConstructorArgs);
		}
		sb.append("]");
		return sb.toString();
	}

	public int getScopeIn() {
		return scopeIn;
	}

	public void setScopeIn(int scopeIn) {
		this.scopeIn = scopeIn;
	}

    public void write(IRVMWireOutputStream out) throws IOException {
        
        out.startMessage(CompilerIDs.OverloadedFunction.ID);
        
        out.writeField(CompilerIDs.OverloadedFunction.NAME, name);

        out.writeField(CompilerIDs.OverloadedFunction.FUN_TYPE, funType, WindowSizes.TINY_WINDOW);
        
        out.writeField(CompilerIDs.OverloadedFunction.FUNCTIONS, functions);
        
        out.writeField(CompilerIDs.OverloadedFunction.CONSTRUCTORS, constructors);
        
        out.writeField(CompilerIDs.OverloadedFunction.FUN_IN, funIn);
        
        out.writeField(CompilerIDs.OverloadedFunction.SCOPE_IN, scopeIn);
        
        if(allConcreteFunctionArgs){
            out.writeField(CompilerIDs.OverloadedFunction.ALL_CONCRETE_FUNCTION_ARGS, 1);
        }
        
        if(allConcreteConstructorArgs){
            out.writeField(CompilerIDs.OverloadedFunction.ALL_CONCRETE_CONSTRUCTOR_ARGS, 1);
        }
        
        if(filteredFunctions != null){
            out.writeFieldIntIntArray(CompilerIDs.OverloadedFunction.FILTERED_FUNCTIONS, filteredFunctions);
        }
        
        if(filteredConstructors != null){
            out.writeFieldIntIntArray(CompilerIDs.OverloadedFunction.FILTERED_CONSTRUCTORS, filteredConstructors);
        }
        
        out.endMessage();
    }
   
    static OverloadedFunction read(IRVMWireInputStream in) throws IOException {
        String name = "unitialized name";
        Type funType = null;
        int[] functions = new int[0];
        int[] constructors = new int[0];
        String funIn = "unitialized funIn";
        int scopeIn = -1;
        boolean allConcreteFunctionArgs = false;
        boolean allConcreteConstructorArgs = false;
        Map<Integer, int[]> emptyMap = new HashMap<>();
        Map<Integer, int[]> filteredFunctions = emptyMap;
        Map<Integer, int[]> filteredConstructors = emptyMap;
        
        in.next();
        assert in.current() == IWireInputStream.MESSAGE_START;
        if(in.message() != CompilerIDs.OverloadedFunction.ID){
            throw new IOException("Unexpected message: " + in.message());
        }
        while(in.next() != IWireInputStream.MESSAGE_END){
            switch(in.field()){
                
                case CompilerIDs.OverloadedFunction.NAME: {
                    name = in.getString();
                    break;
                }
                
                case CompilerIDs.OverloadedFunction.FUN_TYPE: {
                    funType = in.readType();
                    break;
                }
                
                case CompilerIDs.OverloadedFunction.FUNCTIONS: {
                    functions = in.getIntegers();
                    break;
                }
                
                case CompilerIDs.OverloadedFunction.CONSTRUCTORS: {
                    constructors = in.getIntegers();
                    break;
                }
                
                case CompilerIDs.OverloadedFunction.FUN_IN: {
                    funIn = in.getString();
                    break;
                }
                
                case CompilerIDs.OverloadedFunction.SCOPE_IN: {
                    scopeIn = in.getInteger();
                    break;
                }
                
                case CompilerIDs.OverloadedFunction.ALL_CONCRETE_FUNCTION_ARGS: {
                    int n = in.getInteger();
                    allConcreteFunctionArgs = n == 1;
                    break;
                }
                
                case CompilerIDs.OverloadedFunction.ALL_CONCRETE_CONSTRUCTOR_ARGS: {
                    int n = in.getInteger();
                    allConcreteConstructorArgs = n == 1;
                    break;
                }
                
                case CompilerIDs.OverloadedFunction.FILTERED_FUNCTIONS: {
                    filteredFunctions = in.readIntIntArrayMap();
                    break;
                }
                
                case CompilerIDs.OverloadedFunction.FILTERED_CONSTRUCTORS: {
                    filteredConstructors = in.readIntIntArrayMap();
                    break;
                }
                
                default: {
                    System.err.println("OverloadedFunction.read, skips " + in.field());
                    // skip field, normally next takes care of it
                    in.skipNestedField();
                }
            }
        }
        
        return new OverloadedFunction(name, funType, functions, constructors, funIn, scopeIn, allConcreteFunctionArgs, allConcreteConstructorArgs,
            filteredFunctions,filteredConstructors);
        
    }

}

