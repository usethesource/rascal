@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel

import IO;
import List;
import lang::rascalcore::check::AType;
import lang::rascal::\syntax::Rascal;
import lang::rascalcore::compile::muRascal::AST;

/*
 * Management of temporaries and labels.
 */
 
public void resetTmpAndLabel(){
    currentModule = |unknown:///|;
	tmpVar = -1;
	tmpLabel = -1;
	loops = [];
	backtrackingScopes = [];
	resumptionScopes = ();
	itVariables = [];
	writerVariables = [];
	tryCatchFinally = [];
	visits = [];
	resetAllCounter();
	resetOrCounter();
	functionScopes = [];
	functionDeclarations = [];
}

loc moduleScope = |unknown:///|;

void setModuleScope(loc l){
    moduleScope = l;
}  

loc getModuleScope()
    = moduleScope;

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
private map[str,str] resumptionScopes = (); // *** state

void initBacktrackingScopes(){
    backtrackingScopes = [];
    resumptionScopes = ();
}

void enterBacktrackingScope(str name){
  println("enterBacktrackingScope: <name>; <backtrackingScopes>");
  backtrackingScopes = name + backtrackingScopes;
}

str getBacktrackingScope(){
    return backtrackingScopes[0];
}

str getResumptionScope(){
  println("getResumptionScope: <backtrackingScopes>");
  return top(backtrackingScopes) ? "";
}

bool haveEnteredBacktrackingScope(str name)
    = name in backtrackingScopes;

str getResumptionScope(str name){
    println("getResumptionScope: <name>; <backtrackingScopes>");
    println("resumptionScopes: <resumptionScopes>");
    while(resumptionScopes[name]?){
        name = resumptionScopes[name];
    }
    println("getResumptionScope ==\> <name>");
    return name;
}

void leaveBacktrackingScope(str name){
  println("leaveBacktrackingScope: <name>; <backtrackingScopes>");
  i = indexOf(backtrackingScopes, name);
  if(i < 0) throw "leaveBacktrackingScope: <name> not found";
  if(i == 0){
    backtrackingScopes = tail(backtrackingScopes);
    if(!isEmpty(backtrackingScopes)){
        previous = backtrackingScopes[0];
        if(!resumptionScopes[previous]?){
            resumptionScopes[previous] = name;
        }
    } else {
        resumptionScopes = ();
    }
    //println(resumptionScopes);
    return;
  }
  
  resumptionPoint = backtrackingScopes[i - 1];
  backtrackingScopes = backtrackingScopes[i+1 ..];
  if(!isEmpty(backtrackingScopes)){
    previous = backtrackingScopes[0];
    if(!resumptionScopes[previous]?){
        resumptionScopes[previous] = resumptionPoint;
    }
  }
  //println(resumptionScopes);
}
  
MuExp updateBTScope(MuExp exp, str fromScope, str toScope){
    //return exp; //<<<
    if(fromScope == toScope) return exp;
    
    println("updateBTScope: <fromScope> =\> <toScope>; <exp>");
    return visit(exp){
        //case muSucceed(fromScope) => muSucceed(toScope)
        case muContinue(fromScope):{ println("muContinue(<fromScope>) =\> muContine(<toScope>)"); insert muContinue(toScope); }
        case muFail(fromScope): { println("muFail(<fromScope>) =\> muFail(<toScope>)"); insert muFail(toScope); }
    };
}

str getLabel(Label label) =
  (label is \default) ? "<label.name>" : nextTmp();

str getLabel(Label label, str alt) =
  (label is \default) ? "<label.name>" : nextLabel(alt);
  
str asTmp(str name) = "TMP_<name>";

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
    initBacktrackingScopes();
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

str topFunctionScope() = isEmpty(functionScopes) ? "" : top(functionScopes).scope;

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

bool inStringVisit()
    = top(visits) == astr();

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
