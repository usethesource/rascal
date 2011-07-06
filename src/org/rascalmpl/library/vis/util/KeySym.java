package org.rascalmpl.library.vis.util;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;

public class KeySym {
	static final Type[] empty = {};
	static final String[] modifiers = {"modCtrl","modCommand","modAlt","modShift"};
	static final int[] modifiersSWT = {SWT.CTRL, SWT.COMMAND, SWT.ALT, SWT.SHIFT };
	
	public static IMap toRascalModifiers(KeyEvent e,IMap prevMap,IEvaluatorContext ctx){
		TypeFactory tf = TypeFactory.getInstance();
		ValueFactory vf = ValueFactory.getInstance();
		Environment env = ctx.getCurrentEnvt();
		for(int i = 0 ; i < modifiers.length ;i++){
			Type controlType = env.lookupFirstConstructor(modifiers[i],tf.tupleEmpty());
			IValue cons = vf.constructor(controlType);
			prevMap = prevMap.put(cons, vf.bool((e.stateMask & modifiersSWT[i]) != 0));
		}
		return prevMap;
	}
	
	public static IValue toRascalKey(KeyEvent e,IEvaluatorContext ctx){
		TypeFactory tf = TypeFactory.getInstance();
		ValueFactory vf = ValueFactory.getInstance();
		Environment env = ctx.getCurrentEnvt();
		if(e.keyCode >= ' ' && e.keyCode < '~'){
			String keySym = "" + (char)e.keyCode;
			IString keySymR = vf.string(keySym);
			Type printableKeyType = env.lookupFirstConstructor("keyPrintable", tf.tupleType(tf.stringType()));
			return vf.constructor(printableKeyType, keySymR);
		} else {
			String name = unPrintableKeyName(e);
			if(name.equals("keyUnknown")){
				Type unkownType = env.lookupFirstConstructor(name,tf.tupleType(tf.integerType()));
				return vf.constructor(unkownType,vf.integer(e.keyCode));
			} else {
				Type printableKeyType = env.lookupFirstConstructor(name,tf.tupleEmpty());
				return vf.constructor(printableKeyType);
			}
		}
		
	}

	public static String unPrintableKeyName(KeyEvent e){
		switch(e.keyCode){
		case SWT.ALT: 
//			if(e.keyCode == SWT.LEFT) return "keyAltLeft";
			/*else*/ return "keyAltRight";
		case SWT.ARROW_DOWN: return "keyArrowDown";
		case SWT.ARROW_LEFT: return "keyArrowLeft";
		case SWT.ARROW_RIGHT: return "keyArrowRight";
		case SWT.ARROW_UP: return "keyArrowUp";
		case SWT.BREAK: return "keyBreak";
		case SWT.CAPS_LOCK: return "keyCapsLock";
		case SWT.COMMAND:
//			if(e.keyLocation == SWT.LEFT) return "keyCommandLeft";
			/*else*/ return "keyCommandRight";
		case SWT.CTRL:
//			if(e.keyLocation == SWT.LEFT) return "keyControlLeft";
			/*else*/ return "keyControlRight";
		case SWT.END: return "keyEnd";
		case SWT.F1: return "keyF1";
		case SWT.F10: return "keyF10";
		case SWT.F11: return "keyF11";
		case SWT.F12: return "keyF12";
		case SWT.F13: return "keyF13";
		case SWT.F14: return "keyF14";
		case SWT.F15: return "keyF15";
//		case SWT.F16: return "keyF16";
//		case SWT.F17: return "keyF17";
//		case SWT.F18: return "keyF18";
//		case SWT.F19: return "keyF19";
		case SWT.F2: return "keyF2";
//		case SWT.F20: return "keyF20";
		case SWT.F3: return "keyF3";
		case SWT.F4: return "keyF4";
		case SWT.F5: return "keyF5";
		case SWT.F6: return "keyF6";
		case SWT.F7: return "keyF7";
		case SWT.F8: return "keyF8";
		case SWT.F9: return "keyF9";
		case SWT.HELP: return "keyHelp";
		case SWT.HOME: return "keyHome";
		case SWT.INSERT: return "keyInsert";
		case SWT.KEYPAD_0: return "keyKeypad0";
		case SWT.KEYPAD_1: return "keyKeypad1";
		case SWT.KEYPAD_2: return "keyKeypad2";
		case SWT.KEYPAD_3: return "keyKeypad3";
		case SWT.KEYPAD_4: return "keyKeypad4";
		case SWT.KEYPAD_5: return "keyKeypad5";
		case SWT.KEYPAD_6: return "keyKeypad6";
		case SWT.KEYPAD_7: return "keyKeypad7";
		case SWT.KEYPAD_8: return "keyKeypad8";
		case SWT.KEYPAD_9: return "keyKeypad9";
		case SWT.KEYPAD_ADD: return "keyKeypadAdd";
		case SWT.KEYPAD_CR: return "keyKeypadCr";
		case SWT.KEYPAD_DECIMAL: return "keyKeypadDecimal";
		case SWT.KEYPAD_DIVIDE: return "keyKeypadDivide";
		case SWT.KEYPAD_EQUAL: return "keyKeypadEqual";
		case SWT.KEYPAD_MULTIPLY: return "keyKeypadMultiply";
		case SWT.KEYPAD_SUBTRACT: return "keyKeypadSubtract";
		case SWT.NUM_LOCK: return "keyNumLock";
		case SWT.PAGE_DOWN: return "keyPageDown";
		case SWT.PAGE_UP: return "keyPageUp";
		case SWT.PAUSE: return "keyPause";
		case SWT.PRINT_SCREEN: return "keyPrintScreen";
		case SWT.SCROLL_LOCK: return "keyScrollLock";
		case SWT.SHIFT: 
//			if(e.keyLocation == SWT.LEFT) return "keyShiftLeft";
			/*else*/ return "keyShiftRight";
		case 8 : return "keyBackSpace";
		case '\t': return "keyTab";
		case 13: return "keyEnter";
		case 27: return "keyEscape";
		default: return "keyUnknown";
		}
		
	}
	
}
