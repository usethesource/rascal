package org.meta_environment.rascal.std.Chart;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.Constructor;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class Settings {
	static IList settings;
	private static IValue lastSetting;
	
	static void supported(IValue v, String s, String[] provided){
		for(int i = 0; i < provided.length; i ++){
			if(provided[i].	equals(s))
				return;
		}
		throw RuntimeExceptionFactory.illegalArgument(v, null, null);
	}
	
	static void validate(String[] provided, IList settings){
		Settings.settings = settings;
		for(IValue s : settings){
			Constructor scons = (Constructor) s;
			supported(s, scons.getName(), provided);
		}
	}
    
	static boolean has(String name){
		for(IValue s : settings){
			Constructor scons = (Constructor) s;
			if(scons.getName().equals(name)){
				if(scons.arity() > 0){
					lastSetting = scons.get(0);
				}
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private static int lastInteger(){
		if(lastSetting != null && lastSetting.getType().isIntegerType()){
			return ((IInteger) lastSetting).intValue();
		}
		throw new ImplementationError("Last used setting not an integer");
	}
	
	@SuppressWarnings("unused")
	private static float lastReal(){
		if(lastSetting != null && lastSetting.getType().isRealType()){
			return ((IReal) lastSetting).floatValue();
		}
		throw new ImplementationError("Last used setting not a real");
	}
	@SuppressWarnings("unused")
	private static String lastString(){
		if(lastSetting != null && lastSetting.getType().isStringType()){
			return ((IString) lastSetting).getValue();
		}
		throw new ImplementationError("Last used setting not a string");
	}
}
