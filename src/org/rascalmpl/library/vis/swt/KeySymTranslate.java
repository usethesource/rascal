/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.swt;

import static org.rascalmpl.library.vis.KeySym.KeyModifier_modAlt;
import static org.rascalmpl.library.vis.KeySym.KeyModifier_modCommand;
import static org.rascalmpl.library.vis.KeySym.KeyModifier_modCtrl;
import static org.rascalmpl.library.vis.KeySym.KeyModifier_modShift;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyAltLeft;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyAltRight;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyArrowDown;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyArrowLeft;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyArrowRight;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyArrowUp;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyBackSpace;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyBreak;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyCapsLock;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyCommandLeft;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyControlLeft;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyEnd;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyEnter;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyEscape;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF1;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF10;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF11;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF12;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF13;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF14;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF15;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF2;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF3;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF4;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF5;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF6;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF7;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF8;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyF9;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyHelp;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyHome;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyInsert;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypad0;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypad1;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypad2;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypad3;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypad4;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypad5;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypad6;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypad7;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypad8;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypad9;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypadAdd;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypadCr;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypadDecimal;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypadDivide;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypadEqual;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypadMultiply;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyKeypadSubtract;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyNumLock;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyPageDown;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyPageUp;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyPause;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyPrintScreen;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyPrintable;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyScrollLock;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyShiftLeft;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyTab;
import static org.rascalmpl.library.vis.KeySym.KeySym_keyUnknown;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.values.ValueFactoryFactory;

public class KeySymTranslate {
  private static final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	static final Type[] empty = {};
	static final Type[] modifiers = {KeyModifier_modCtrl,KeyModifier_modCommand,KeyModifier_modAlt,KeyModifier_modShift};
	static final int[] modifiersSWT = {SWT.CTRL, SWT.COMMAND, SWT.ALT, SWT.SHIFT };
	
	public static IMap toRascalModifiers(KeyEvent e,IMap prevMap,IEvaluatorContext ctx){
		for(int i = 0 ; i < modifiers.length ;i++){
			Type controlType = modifiers[i];
			IValue cons = VF.constructor(controlType);
			prevMap = prevMap.put(cons, VF.bool((e.stateMask & modifiersSWT[i]) != 0));
		}
		return prevMap;
	}
	
	public static IValue toRascalKey(KeyEvent e,IEvaluatorContext ctx){
		IValueFactory vf = VF;
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
