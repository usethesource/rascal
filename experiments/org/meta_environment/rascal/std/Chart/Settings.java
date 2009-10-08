package org.meta_environment.rascal.std.Chart;

import java.awt.Color;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.Constructor;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class Settings {
	
	public static final Color LighterGrey = new Color(0.9435f,0.9348f,0.9435f);
	
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

	static int getInteger(){
		if(lastSetting != null && lastSetting.getType().isIntegerType()){
			return ((IInteger) lastSetting).intValue();
		}throw new ImplementationError("Last used setting not an integer");
	}
	
	static float getReal(){
		if(lastSetting != null && lastSetting.getType().isRealType()){
			return ((IReal) lastSetting).floatValue();
		}throw new ImplementationError("Last used setting not a real");
	}

	static String getString(){
		if(lastSetting != null && lastSetting.getType().isStringType()){
			return ((IString) lastSetting).getValue();
		}throw new ImplementationError("Last used setting not a string");
	}
	
	static String[] getListString(){
		if(lastSetting != null && lastSetting.getType().isListType() &&
				lastSetting.getType().getElementType().isStringType()){
			IList lst = (IList) lastSetting;
			String[] result = new String[lst.length()];
			for(int i = 0; i < lst.length(); i++)
				result[i] = ((IString) lst.get(i)).getValue();
			return result;
		}throw new ImplementationError("Last used setting not a list of strings");
	}
}
