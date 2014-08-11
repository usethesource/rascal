module lang::java::style::Utils

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

/* --- various utilities ----------------------------------------------------*/

// Create a unique name for a constructor
str getConstructor(node nd) = "<getName(nd)><arity(nd)>";

// Convert structured packgae name to string

str getPackageName(p: \package(_)) = packageName2String(p);
str getPackageName(p: \package(_, _)) = packageName2String(p);
default str getPackageName(Declaration d){
	throw "cannot get packgae name from <d>";
}

str packageName2String(package(str name)) = name;
str packageName2String(package(Declaration parentPackage, str name)) = "<packageName2String(parentPackage)>.<name>";

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

//loc getDeclaredEntity(loc src, M3 model){
//	res = model@declarations<1,0>[src];
//	if(size(res) != 1){
//		throw "getEntity: undefined src <src>";
//	}
//	for(e <- res){
//		return e;
//	}
//}
