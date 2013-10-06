@bootstrapParser
module experiments::Compiler::Rascal2muRascal::TmpAndLabel

import Prelude;
import lang::rascal::\syntax::Rascal;
/*
 * Management of temporaries and labels.
 */
 
public void resetTmpAndLabel(){
	tmpVar = -1;
	tmpLabel = -1;
	loops = [];
	itVariables = [];
	writerVariables = [];
	tryCatchFinally = [];
}

// Generation of temporary variables and labels

public int tmpVar = -1;   						// *** state

public str nextTmp(){
	tmpVar += 1;
    return "TMP<tmpVar>";
}

public int tmpLabel = -1;						// *** state

public str nextLabel(){
	tmpLabel += 1;
	return "LAB<tmpLabel>";
}

public str nextLabel(str prefix){
	tmpLabel += 1;
	return "<prefix><tmpLabel>";
}

// Keep track of loop nested. This is used for
// - append
// - break/continue/fail

bool inBacktrackingScope() = !isEmpty(backtrackingScopes);

private list[str] loops = [];					// *** state

void enterLoop(str name){
  loops = name + loops;
}

str currentLoop(){
  return top(loops);
}

void leaveLoop(){
  loops = tail(loops);
}

// Backtracking scopes also include if-then-else scopes that allow backtracking in case of using fail
private list[str] backtrackingScopes = [];

void enterBacktrackingScope(str name){
  backtrackingScopes = name + backtrackingScopes;
}

str currentBacktrackingScope(){
  return top(backtrackingScopes);
}

void leaveBacktrackingScope(){
  backtrackingScopes = tail(backtrackingScopes);
}

str getLabel(Label label) =
  (label is \default) ? "<label.name>" : nextTmp();
  
str asTmp(str name) = "TMP_<name>";
str asUnwrapedThrown(str name) = name + "_unwraped";

// Keep track of possibly nested "it" variables in reducers

private list[str] itVariables = [];				// *** state

void pushIt(str name){
  itVariables = name + itVariables;
}

void popIt(){
  itVariables = tail(itVariables);
}

str topIt() = top(itVariables);

// Administration for possibly nested list/set writers related to splicing list/set elements

private list[str] writerVariables = [];			// *** state

void enterWriter(str name){
  writerVariables = name + writerVariables;
}

void leaveWriter(){
  writerVariables = tail(writerVariables);
}

// Administration of try-catch-finally blocks

// The stack of try-catch-finally block is managed to check whether there is a finally block 
// that must be executed before 'return' if any
private list[bool] tryCatchFinally = [];

bool hasFinally() = !isEmpty(tryCatchFinally);

void enterTryCatchFinally() {
	tryCatchFinally = true + tryCatchFinally;
}

void leaveTryCatchFinally() {
	tryCatchFinally = tail(tryCatchFinally);
}

// Administration of function scopes; 
// needed to translate 'visit' expressions and generate function declarations for 'visit' cases

private list[str] functionScopes = [];

str topFunctionScope() = top(functionScopes);

void enterFunctionScope(str fuid) = fuid + functionScopes;

void leaveFunctionScope() = tail(functionScopes);

