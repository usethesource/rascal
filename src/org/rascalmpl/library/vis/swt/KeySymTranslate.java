package org.rascalmpl.library.vis.swt;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.rascalmpl.interpreter.IEvaluatorContext;

import static org.rascalmpl.library.vis.KeySym.*;

public class KeySymTranslate {
	static final Type[] empty = {};
	static final Type[] modifiers = {KeyModifier_modCtrl,KeyModifier_modCommand,KeyModifier_modAlt,KeyModifier_modShift};
	static final int[] modifiersSWT = {SWT.CTRL, SWT.COMMAND, SWT.ALT, SWT.SHIFT };
	
	public static IMap toRascalModifiers(KeyEvent e,IMap prevMap,IEvaluatorContext ctx){
		ValueFactory vf = ValueFactory.getInstance();
		for(int i = 0 ; i < modifiers.length ;i++){
			Type controlType = modifiers[i];
			IValue cons = vf.constructor(controlType);
			prevMap = prevMap.put(cons, vf.bool((e.stateMask & modifiersSWT[i]) != 0));
		}
		return prevMap;
	}
	
	public static IValue toRascalKey(KeyEvent e,IEvaluatorContext ctx){
		ValueFactory vf = ValueFactory.getInstance();
		if(e.keyCode >= ' ' && e.keyCode < '~'){
			String keySym = "" + (char)e.keyCode;
			return vf.constructor(KeySym_keyPrintable, vf.string(keySym));
		} else {
			Type cons = unPrintableKeyName(e);
			if(cons == KeySym_keyUnknown){
				return vf.constructor(KeySym_keyUnknown,vf.integer(e.keyCode));
			} else {
				return vf.constructor(cons);
			}
		}
		
	}

	public static Type unPrintableKeyName(KeyEvent e){
		switch(e.keyCode){
		case SWT.ALT: 
			if(e.keyCode == SWT.LEFT) return KeySym_keyAltLeft;
			else return KeySym_keyAltRight;
		case SWT.ARROW_DOWN: return KeySym_keyArrowDown;
		case SWT.ARROW_LEFT: return KeySym_keyArrowLeft;
		case SWT.ARROW_RIGHT: return KeySym_keyArrowRight;
		case SWT.ARROW_UP: return KeySym_keyArrowUp;
		case SWT.BREAK: return KeySym_keyBreak;
		case SWT.CAPS_LOCK: return KeySym_keyCapsLock;
		case SWT.COMMAND: return KeySym_keyCommandLeft;
			//if(e.keyLocation == SWT.LEFT) return KeySym_keyCommandLeft;
			//else return KeySym_keyCommandRight;
		case SWT.CTRL: return KeySym_keyControlLeft;
			//if(e.keyLocation == SWT.LEFT) return KeySym_keyControlLeft;
			//else return KeySym_keyControlRight;
		case SWT.END: return KeySym_keyEnd;
		case SWT.F1: return KeySym_keyF1;
		case SWT.F10: return KeySym_keyF10;
		case SWT.F11: return KeySym_keyF11;
		case SWT.F12: return KeySym_keyF12;
		case SWT.F13: return KeySym_keyF13;
		case SWT.F14: return KeySym_keyF14;
		case SWT.F15: return KeySym_keyF15;
		//case SWT.F16: return KeySym_keyF16;
		//case SWT.F17: return KeySym_keyF17;
		//case SWT.F18: return KeySym_keyF18;
		//case SWT.F19: return KeySym_keyF19;
		case SWT.F2: return KeySym_keyF2;
		//case SWT.F20: return KeySym_keyF20;
		case SWT.F3: return KeySym_keyF3;
		case SWT.F4: return KeySym_keyF4;
		case SWT.F5: return KeySym_keyF5;
		case SWT.F6: return KeySym_keyF6;
		case SWT.F7: return KeySym_keyF7;
		case SWT.F8: return KeySym_keyF8;
		case SWT.F9: return KeySym_keyF9;
		case SWT.HELP: return KeySym_keyHelp;
		case SWT.HOME: return KeySym_keyHome;
		case SWT.INSERT: return KeySym_keyInsert;
		case SWT.KEYPAD_0: return KeySym_keyKeypad0;
		case SWT.KEYPAD_1: return KeySym_keyKeypad1;
		case SWT.KEYPAD_2: return KeySym_keyKeypad2;
		case SWT.KEYPAD_3: return KeySym_keyKeypad3;
		case SWT.KEYPAD_4: return KeySym_keyKeypad4;
		case SWT.KEYPAD_5: return KeySym_keyKeypad5;
		case SWT.KEYPAD_6: return KeySym_keyKeypad6;
		case SWT.KEYPAD_7: return KeySym_keyKeypad7;
		case SWT.KEYPAD_8: return KeySym_keyKeypad8;
		case SWT.KEYPAD_9: return KeySym_keyKeypad9;
		case SWT.KEYPAD_ADD: return KeySym_keyKeypadAdd;
		case SWT.KEYPAD_CR: return KeySym_keyKeypadCr;
		case SWT.KEYPAD_DECIMAL: return KeySym_keyKeypadDecimal;
		case SWT.KEYPAD_DIVIDE: return KeySym_keyKeypadDivide;
		case SWT.KEYPAD_EQUAL: return KeySym_keyKeypadEqual;
		case SWT.KEYPAD_MULTIPLY: return KeySym_keyKeypadMultiply;
		case SWT.KEYPAD_SUBTRACT: return KeySym_keyKeypadSubtract;
		case SWT.NUM_LOCK: return KeySym_keyNumLock;
		case SWT.PAGE_DOWN: return KeySym_keyPageDown;
		case SWT.PAGE_UP: return KeySym_keyPageUp;
		case SWT.PAUSE: return KeySym_keyPause;
		case SWT.PRINT_SCREEN: return KeySym_keyPrintScreen;
		case SWT.SCROLL_LOCK: return KeySym_keyScrollLock;
		case SWT.SHIFT: return KeySym_keyShiftLeft;
			//if(e.keyLocation == SWT.LEFT) return KeySym_keyShiftLeft;
			//else return KeySym_keyShiftRight;
		case 8 : return KeySym_keyBackSpace;
		case '\t': return KeySym_keyTab;
		case 13: return KeySym_keyEnter;
		case 27: return KeySym_keyEscape;
		default: return KeySym_keyUnknown;
		}
		
	}
	
}
