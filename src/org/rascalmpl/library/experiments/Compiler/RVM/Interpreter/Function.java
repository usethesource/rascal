package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.NameMangler;


public class Function {
	 final String name;
	 final Type ftype;
	 int scopeId;
	 private String funIn;
	 int scopeIn = -1;
	 final int nformals;
	 final int nlocals;
	 final int maxstack;
	 final CodeBlock codeblock;
	 IValue[] constantStore;
	 Type[] typeConstantStore;
	 
	 int[] froms;
	 int[] tos;
	 int[] types;
	 int[] handlers;

	 String[] fromLabels;
	 String[] toLabels;
     String[] handlerLabels;
     int[] fromSPs;
     int lastHandler = -1;
	 
	 public IList exceptions = null ;
	 public int continuationPoints = 0;
	 
	 boolean isCoroutine = false;
	 int[] refs;
	 
	 boolean isVarArgs = false;
	 
	 final ISourceLocation src;		
	 final IMap localNames;
	public int funId;
	
	public Function(final String name, final Type ftype, final String funIn, final int nformals, final int nlocals, final IMap localNames, final int maxstack, final CodeBlock codeblock, final ISourceLocation src, int cpts){
		this.name = name;
		this.ftype = ftype;
		this.funIn = funIn;
		this.nformals = nformals;
		this.nlocals = nlocals;
		this.localNames = localNames;
		this.maxstack = maxstack;
		this.codeblock = codeblock;
		this.src = src;
		this.continuationPoints = cpts ;
	}
	

	public void finalize(BytecodeGenerator codeEmittor, final Map<String, Integer> codeMap, final Map<String, Integer> constructorMap, final Map<String, Integer> resolver, final boolean listing) {

		codeEmittor.emitMethod(NameMangler.mangle(name), isCoroutine, continuationPoints, fromLabels, toLabels, types, handlerLabels, false);
		codeblock.done(codeEmittor, name, codeMap, constructorMap, resolver, listing,false);
		
		this.scopeId = codeblock.getFunctionIndex(name);
		if (funIn != null) {
			this.scopeIn = codeblock.getFunctionIndex(funIn);
		}
		this.constantStore = codeblock.getConstants();
		this.typeConstantStore = codeblock.getTypeConstants();

		codeEmittor.closeMethod();
	}
	
	public void  finalize(final Map<String, Integer> codeMap, final Map<String, Integer> constructorMap, final Map<String, Integer> resolver, final boolean listing){
		codeblock.done(name, codeMap, constructorMap, resolver, listing);
		this.scopeId = codeblock.getFunctionIndex(name);
		if(funIn != null) {
			this.scopeIn = codeblock.getFunctionIndex(funIn);
		}
		this.constantStore = codeblock.getConstants();
		this.typeConstantStore = codeblock.getTypeConstants();
	}
	
	public void attachExceptionTable(final IList exceptions, final IRVM rvm) {
		froms = new int[exceptions.length()];
		fromLabels = new String[exceptions.length()];

		tos = new int[exceptions.length()];
		toLabels = new String[exceptions.length()];
	
		types = new int[exceptions.length()];

		handlers = new int[exceptions.length()];
		handlerLabels = new String[exceptions.length()];
		fromSPs = new int[exceptions.length()];
		
		int i = 0;
		for(IValue entry : exceptions) {
			ITuple tuple = (ITuple) entry;
			String from = ((IString) tuple.get(0)).getValue();
			String to = ((IString) tuple.get(1)).getValue();
			Type type = rvm.symbolToType((IConstructor) tuple.get(2));
			String handler = ((IString) tuple.get(3)).getValue();
			int fromSP =  ((IInteger) tuple.get(4)).intValue();
			
			froms[i] = codeblock.getLabelPC(from);
			fromLabels[i] = from;
			tos[i] = codeblock.getLabelPC(to);
			toLabels[i] = to;

			types[i] = codeblock.getTypeConstantIndex(type);

			handlers[i] = codeblock.getLabelPC(handler);			
			handlerLabels[i] = handler;			
			handlers[i] = codeblock.getLabelPC(handler);
			fromSPs[i] = fromSP;
			i++;
		}
		this.exceptions = exceptions ;
	}
	
	public int getHandler(final int pc, final Type type) {
		int i = 0;
		lastHandler = -1;
		for(int from : froms) {
			if(pc >= from) {
				if(pc < tos[i]) {
					// In the range...
					if(type.isSubtypeOf(codeblock.getConstantType(types[i]))) {
						lastHandler = i;
						return handlers[i];
					}
				}
			}
			i++;
		}
		return -1;
	}
	
	public int getFromSP(){
		return nlocals + fromSPs[lastHandler];
	}
	
	public String getName() {
		return name;
	}
	
	public String getPrintableName(){
		int from = name.lastIndexOf("/")+1;
		int to = name.indexOf("(", from);
		if(to < 0){
			to = name.length();
		}
		return name.substring(from, to);
	}
	
	public String getQualifiedName(){
		return name.substring(0, name.indexOf("("));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FUNCTION ").append(name).append(" ").append(ftype);
		for(int i = 0; i < constantStore.length; i++){
			sb.append("constant "). append(i).append(": "). append(constantStore[i]);
		}
		for(int i = 0; i < typeConstantStore.length; i++){
			sb.append("type constant "). append(i).append(": "). append(typeConstantStore[i]);
		}
		sb.append(codeblock);
		return sb.toString();
	}
	
}
