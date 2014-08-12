module lang::java::style::CheckStates

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import List;
import Set;
import IO;
import Node;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;

/* --- CheckState utilities -------------------------------------------------*/

// A check may have an associated CheckState that defines
// - triggers, a set of constructors that will create a new state
// - the initial state
// - function for updating the check's state
// - function to finalize the check's state, i.e., generate message based on the state info
 
alias CheckStateDescriptor = 
	tuple[set[str] triggers,
		  value initial, 
		  value (value current, value delta) update,
		  list[Message] (Declaration d, value v) finalize
		 ];
						
private map[str checkName, CheckStateDescriptor descr] checkStateDescriptors = ();

data CheckState = checkState(loc src, value current);

private map[str checkName, list[CheckState] states] checkStates = ();

void initCheckStates(){
	checkStates = ();
}

void registerCheckState(str checkName, 
						set[str] triggers,
						value initial, 
						value (value current, value delta) update,
						list[Message] (Declaration d, value v) finalize){
						
	checkStateDescriptors[checkName] = <triggers, initial, update, finalize>;
	checkStates[checkName] = [];
}

void enterDeclaration(Declaration decl){
	cons = getConstructor(decl);
	for(checkName <- checkStateDescriptors){
		descr = checkStateDescriptors[checkName];
		if(cons in descr.triggers){
			//println("enterDeclaration, <cons>, <checkName>, <decl@src>");
			checkStates[checkName] = push(checkState(decl@src, descr.initial), checkStates[checkName]);
		}
	}
}

void updateCheckState(str checkName, value delta){
	//println("updateCheckState: <checkName>, <delta>");
	states = checkStates[checkName];
	checkStates[checkName][0] = checkState(states[0].src, checkStateDescriptors[checkName].update(states[0].current, delta));
}

list[Message] leaveDeclaration(Declaration decl){
	msgs = [];
	cons = getConstructor(decl);
	for(checkName <- checkStateDescriptors){
		descr = checkStateDescriptors[checkName];
		if(cons in descr.triggers){
			//println("leaveDeclaration, <cons>, <checkName>, <decl@src>");
			if(checkStates[checkName][0].src != decl@src){
				throw "leaveDeclaration: entered <decl@src>, but leaving <checkStates[checkName][0].src>";
			}
			msgs += checkStateDescriptors[checkName].finalize(decl, checkStates[checkName][0].current);
			checkStates[checkName] = tail(checkStates[checkName]);
		}
	}
	return msgs;
}

/* --- experiment -----------------------------------------------------------*/

/*
 * Here we try to represent checkstates by closures: a <check>Provider return a
 * pair of functions: <update, finalize> that are to be called by the framework.
 *
 * Unfortunately, the typing is not correct: the update function should be typed more precisely,
 * but then various update functions no longer fit in the same type, see examples at the bottom

/*
tuple[void(value), list[Message]()] throwsCountProvider(Declaration d){
	int current = 0;
	
	void update(value delta) { if(int d := delta) current += d; }

	list[Message] finalize() =
		(current > 1) ? [classDesign("ThrowsCount", d@src)] : [];

	return <update, finalize>;
}

tuple[void(value), list[Message]()] classDataAbstractionCouplingProvider(Declaration d){
	set[str] current = {};

	void update(value delta) { if(str d := delta) current += delta; }

	list[Message] finalize() =
	(size(current - excludedClasses) > 7) ? [metric("ClassDataAbstractionCoupling", d@src)] : [];	

	return <update, finalize>;
}

// This only works with updates of type void(value)

public list[tuple[void(value), list[Message]()](Declaration)] exp = [throwsCountProvider, classDataAbstractionCouplingProvider];

*/