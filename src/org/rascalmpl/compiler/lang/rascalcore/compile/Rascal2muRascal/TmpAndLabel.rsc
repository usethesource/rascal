@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel

//import IO;
import List;
import ListRelation;
import String;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
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
	//tryCatchFinally = [];
	visits = [];
	resetAllCounter();
	resetOrCounter();
	functionScopes = [];
	functionDeclarations = [];
    resetClosureCounter();
}

private loc currentModuleScope = |unknown:///|;

void setModuleScope(loc l){
    currentModuleScope = l;
}

loc getModuleScope()
    = currentModuleScope;

// Generation of temporary variables and labels

private int tmpVar = -1;   						// *** state

str nextTmp(){
	tmpVar += 1;
    return "$TMP<tmpVar>";
}

str nextTmp(str name){
    tmpVar += 1;
    return "$<name><tmpVar>";
}

private int tmpLabel = -1;						// *** state

str nextLabel(){
	tmpLabel += 1;
	return "LAB<tmpLabel>";
}

str nextLabel(str prefix){
	tmpLabel += 1;
	return "<prefix><tmpLabel>";
}

// Keep track of loop nesting. This is used for
// - append
// - break/continue/fail

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

bool inLoop(str name)
    = name in domain(loops);

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

// labelled statements (if, while,

private lrel[str label, str resume] labelledStats = [];               // *** state

void initLabelledStats(){
    labelledStats = [];
}

void enterLabelled(Label label, str alt, str resume){
    if(label is \default){
        labelName = "<label.name>";
        labelledStats = <labelName, resume> + labelledStats;
    } else {
        labelledStats = <alt, resume> + labelledStats;
    }
}

void enterLabelled(Label label, str alt){
    if(label is \default){
        labelName = "<label.name>";
        labelledStats = <labelName, labelName> + labelledStats;
    } else {
        labelledStats = <alt, alt> + labelledStats;
    }
}

void enterLabelled(str alt){
    labelledStats = <alt, alt> + labelledStats;
}

void leaveLabelled(){
    if(!isEmpty(labelledStats)){
        labelledStats = tail(labelledStats);
    }
}

tuple[bool,str] inLabelled(str label){
    res = labelledStats[label];
    return isEmpty(res) ? <false, ""> : <true, res[0]>;
}

tuple[bool, str] getLabelled(){
    if(isEmpty(labelledStats)) return <false, "">;
    return <true, labelledStats[0].resume>;
}

str getLabel(Label label) =
  (label is \default) ? "<label.name>" : nextTmp();

str getLabel(Label label, str alt) =
  (label is \default) ? "<label.name>" : nextLabel(alt);

str asTmp(str name) = "TMP<name>";

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

private list[loc] functionDeclarations = []; // *** state
private bool state_inSignatureSection = false;
private bool state_usingTypeParams = false;

void enterFunctionDeclaration(loc src, bool useTypeParams){
    functionDeclarations = src + functionDeclarations;
    initLabelledStats();
    state_inSignatureSection = false;
    state_usingTypeParams = useTypeParams;
}

bool usingTypeParams() = state_usingTypeParams;

bool inSignatureSection() = state_inSignatureSection;

void enterSignatureSection(){
    state_inSignatureSection = true;
}
void leaveSignatureSection() {
    state_inSignatureSection = false;
}

void leaveFunctionDeclaration(){
    if(!isEmpty(functionDeclarations)){
        functionDeclarations = tail(functionDeclarations);
    }
    state_inSignatureSection = false;
}

loc currentFunctionDeclaration(){
    return top(functionDeclarations);
}

// Administration of function scopes;
// needed to translate 'visit' expressions and generate function declarations for 'visit' cases

private lrel[str scope,int counter] functionScopes = []; // *** state

str topFunctionScope() = isEmpty(functionScopes) ? "" : top(functionScopes)[0] /*.scope*/;

int nextVisit() {
	int counter = top(functionScopes).counter;
	functionScopes = <top(functionScopes)[0]/*.scope*/, counter + 1> + tail(functionScopes);
	return counter;
}

void enterFunctionScope(str fuid) {
	functionScopes = <fuid,0> + functionScopes;
}

void leaveFunctionScope() {
	functionScopes = tail(functionScopes);
}

private list[AType] visits = [];		// *** state

//AType topCaseType() = top(visits);

void enterVisit(AType subjectType) {
	visits = subjectType + visits;
}

bool inStringVisit(){
    return !isEmpty(visits) && isStrAType(top(visits));
}

void leaveVisit() {
	visits = tail(visits);
}

void fillCaseType(AType t) {
	visits = t + tail(visits);
}

//void clearCaseType() {
//	visits = avoid() + tail(visits);
//}

private int allCounter = 0;								// *** state

int getNextAll() {
    int counter = allCounter;
    allCounter = allCounter + 1;
    return counter;
}

void resetAllCounter() {
    allCounter = 0;
}

private int orCounter = 0;								// *** state

int getNextOr() {
    int counter = orCounter;
    orCounter = orCounter + 1;
    return counter;
}

void resetOrCounter() {
    orCounter = 0;
}

private int closureCounter = 0;

int getNextClosure(){
    int counter = closureCounter;
    closureCounter += 1;
    return counter;
}

void resetClosureCounter(){
    closureCounter = 0;
}
