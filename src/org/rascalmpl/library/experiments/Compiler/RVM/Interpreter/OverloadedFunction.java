package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;

/**
 * Represent one overloaded function given
 * - a list of function alternatives
 * - a list of constructor alternatives
 * - a scope
 * 
 * An overloaded function is created during loading and is consulted at run-time
 * to create an OverloadedFunctionInstance for each speciic call.
 *
 */
public class OverloadedFunction {
	
	final int[] functions;
	final int[] constructors;
	final String funIn;
	int scopeIn = -1;
	boolean allConcreteFunctionArgs = false;
	boolean allConcreteConstructorArgs = false;
	HashMap<Integer, int[]> filteredFunctions;		// Functions and constructors filtered on fingerprint of first argument
	HashMap<Integer, int[]> filteredConstructors;
	
	public OverloadedFunction(RVM rvm, final int[] functions, final int[] constructors, final String funIn) {
		this.functions = functions;
		this.constructors = constructors;
		this.funIn = funIn;
		filter(rvm);
	}
	
	public void  finalize(final Map<String, Integer> functionMap){
		if(funIn != null) {
			this.scopeIn = functionMap.get(funIn);
		}
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
	
	private void filter(RVM rvm){
		filterFunctions(rvm);
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
	private void filterFunctions(RVM rvm){
		if(functions.length > 1){
			filteredFunctions = new HashMap<Integer,int[]>();
		} else {
			return;
		}
		// Determine whether all first arguments are concrete
		allConcreteFunctionArgs = true;
		HashMap<Integer, ArrayList<Integer>> filtered = new HashMap<Integer, ArrayList<Integer>>();
		for(int fid : functions){
			Function fdef = rvm.functionStore.get(fid);
			if(!fdef.concreteArg){
				allConcreteFunctionArgs = false;
			}
		}
		// Collect applicable functions per fingerprint
		for(int fid : functions){
			Function fdef = rvm.functionStore.get(fid);
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
		
		for(int fp : filtered.keySet()){
			ArrayList<Integer> alts = filtered.get(fp);
			int nalts = alts.size();
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
		return scopeIn ;
	}
	
	public String getScopeFun() {
		return funIn ;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("Overloaded[");
		if(functions.length > 0){
			sb.append("functions:");
			for(int i = 0; i < functions.length; i++){
				sb.append(" ").append(functions[i]);
			}
		}
		if(constructors.length > 0){
			if(functions.length > 0){
				sb.append("; ");
			}
			sb.append("constructors:");
			for(int i = 0; i < constructors.length; i++){
				sb.append(" ").append(constructors[i]);
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
