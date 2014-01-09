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

str currentLoop(DataTarget target){
  if(target is empty)
     return currentLoop();
  else
     return "<target.label>";
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

private lrel[str scope,int counter] functionScopes = [];

str topFunctionScope() = top(functionScopes).scope;

int nextVisit() {
	int counter = top(functionScopes).counter;
	functionScopes = <top(functionScopes).scope, counter + 1> + tail(functionScopes);
	return counter;
}

void enterFunctionScope(str fuid) { 
	functionScopes = <fuid,0> + functionScopes; 
}

void leaveFunctionScope() { 
	functionScopes = tail(functionScopes); 
}

private list[Symbol] visits = [];

Symbol topCaseType() = top(visits);

void enterVisit() {
	visits = Symbol::\void() + visits;
}

void leaveVisit() {
	visits = tail(visits);
}

void fillCaseType(Symbol t) {
	visits = t + tail(visits);
}

void clearCaseType() {
	visits = Symbol::\void() + tail(visits);
}
