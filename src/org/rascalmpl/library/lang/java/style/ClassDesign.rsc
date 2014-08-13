module lang::java::style::ClassDesign

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import List;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;
import lang::java::style::CheckStates;

import IO;

data Message = classDesign(str category, loc pos);

/*
VisibilityModifier			DONE
FinalClass					DONE
InterfaceIsType				TBD
HideUtilityClassConstructor	TBD
DesignForExtension			TBD
MutableException			DONE
ThrowsCount					DONE
InnerTypeLast				TBD
*/

/* --- visibilityModifier ---------------------------------------------------*/

list[Message] visibilityModifier(Declaration cls, list[Declaration] parents, M3 model) {
	msgs = [];
	modifiers = model@modifiers;
	c = cls@decl;
	for(f <- fields(model, c)){
		mods = modifiers[f];
		if(\public() in mods && !({\static(), \final()} < mods)){
			msgs += classDesign("VisibilityModifier", f);
		}
	}
	
	return msgs;
}

/* --- finalClass -----------------------------------------------------------*/

list[Message] finalClass(Declaration cls, list[Declaration] parents, M3 model) {
	msgs = [];
	modifiers = model@modifiers;
	bool isPrivate(loc m) = \private() in modifiers[m];
	
	bool hasOnlyPrivateConstructors(loc class){
		ncons = 0;
		for(m <-  methods(model, class)){
			if(m.scheme == "java+constructor"){
				ncons += 1;
				if(!isPrivate(m)){
					return false;
				}
			}	
		}
		return ncons > 0;
	}
	
	c = cls@decl;
	mods = model@modifiers;
	if(hasOnlyPrivateConstructors(c) && \final() notin mods[c]){
		msgs += classDesign("FinalClass", c);
	}

    return msgs;
}

/* --- mutableException -----------------------------------------------------*/

list[Message] mutableException(Declaration cls, list[Declaration] parents, M3 model) {
	msgs = [];
	modifiers = model@modifiers;
	bool hasOnlyFinalFields(loc c){
		return all(f <- fields(model, c), \final() in modifiers[f]);
	}
	c = cls@decl;
	
	if((/Exception$/ := cls.name || /Error$/ := cls.name) && !hasOnlyFinalFields(cls@decl)){ 
			msgs += classDesign("MutableException", cls@src);
	}

    return msgs;
}

default list[Message] mutableException(Declaration cls, list[Declaration] parents, M3 model) = [];

/* --- throwsCount ----------------------------------------------------------*/

list[Message] throwsCount(Statement s: \throw(_), list[Statement] parents, M3 model) {
	updateCheckState("throwsCount", 1);
	return [];
}

default list[Message] throwsCount(Statement s, list[Statement] parents, M3 model) = [];

// update/finalize

value updateThrowsCount(value current, value delta) { if(int n := current && int d := delta) return n + d; }

list[Message] finalizeThrowsCount(Declaration d, value current) =
	(int n := current && n > 1) ? [classDesign("ThrowsCount", d@src)] : [];

