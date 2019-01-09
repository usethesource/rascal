@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel

import List;
import lang::rascalcore::check::AType;
import lang::rascal::\syntax::Rascal;
import lang::rascalcore::compile::muRascal::AST;

/*
 * Management of temporaries and labels.
 */
 
public void resetTmpAndLabel(){
	tmpVar = -1;
	tmpLabel = -1;
	loops = [];
	backtrackingScopes = [];
	itVariables = [];
	writerVariables = [];
	tryCatchFinally = [];
	visits = [];
	resetAllCounter();
	resetOrCounter();
}

// Generation of temporary variables and labels

public int tmpVar = -1;   						// *** state

public str nextTmp(){
	tmpVar += 1;
    return "TMP<tmpVar>";
}

public str nextTmp(str name){
    tmpVar += 1;
    return "<name>_<tmpVar>";
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

// Keep track of loop nesting. This is used for
// - append
// - break/continue/fail

bool inBacktrackingScope() = !isEmpty(backtrackingScopes);

private lrel[str label,str fuid] loops = []; // *** state

void enterLoop(str name, str fuid){
  loops = <name,fuid> + loops;
}

str currentLoop(){
  return top(loops).label;
}

str currentLoop(DataTarget target){
  if(target is empty)
     return currentLoop();
  else
     return "<target.label>";
}

str getCurrentLoopScope() {
  return top(loops).fuid;
}

str getCurrentLoopScope(DataTarget target) {
  if(target is empty) {
      return getCurrentLoopScope();
  } else {
      return topFunctionScope();
  }
}

void leaveLoop(){
  loops = tail(loops);
}

// Backtracking scopes also include if-then-else scopes that allow backtracking in case of using fail
private list[str] backtrackingScopes = [];	// *** state

void enterBacktrackingScope(str name){
  backtrackingScopes = name + backtrackingScopes;
}

str currentBacktrackingScope(){
  return top(backtrackingScopes);
}

bool haveEnteredBacktrackingScope(str name)
    = name in backtrackingScopes;

void leaveBacktrackingScope(){
  backtrackingScopes = tail(backtrackingScopes);
}

str getLabel(Label label) =
  (label is \default) ? "<label.name>" : nextTmp();

str getLabel(Label label, str alt) =
  (label is \default) ? "<label.name>" : nextLabel(alt);
  
str asTmp(str name) = "TMP_<name>";
str asUnwrappedThrown(str name) = name + "_unwrapped";

// Keep track of possibly nested "it" variables in reducers

private list[MuExp] itVariables = []; // *** state

void pushIt(MuExp var){
  itVariables = var + itVariables;
}

void popIt(){
  itVariables = tail(itVariables);
}

MuExp topIt() = top(itVariables);

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
private list[bool] tryCatchFinally = [];	// *** state

bool hasFinally() = !isEmpty(tryCatchFinally);

void enterTryCatchFinally() {
	tryCatchFinally = true + tryCatchFinally;
}

void leaveTryCatchFinally() {
	tryCatchFinally = tail(tryCatchFinally);
}

private list[loc] functionDeclarations = []; // *** state

void enterFunctionDeclaration(loc src){
    functionDeclarations = src + functionDeclarations;
}

void leaveFunctionDeclaration(){
    functionDeclarations = tail(functionDeclarations);
}

loc currentFunctionDeclaration(){
    return top(functionDeclarations);
}

// Administration of function scopes; 
// needed to translate 'visit' expressions and generate function declarations for 'visit' cases

private lrel[str scope,int counter] functionScopes = []; // *** state

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

private list[AType] visits = [];		// *** state

AType topCaseType() = top(visits);

void enterVisit() {
	visits = avoid() + visits;
}

bool inStringVisit(){
	top(visits) == astr();
}

void leaveVisit() {
	visits = tail(visits);
}

void fillCaseType(AType t) {
	visits = t + tail(visits);
}

void clearCaseType() {
	visits = avoid() + tail(visits);
}

int allCounter = 0;								// *** state

int getNextAll() {
    int counter = allCounter;
    allCounter = allCounter + 1;
    return counter;
}

void resetAllCounter() {
    allCounter = 0;
}

int orCounter = 0;								// *** state

int getNextOr() {
    int counter = orCounter;
    orCounter = orCounter + 1;
    return counter;
}

void resetOrCounter() {
    orCounter = 0;
}

