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
}

// Generation of temporary variables

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

// Keep track of loop nested. This is used for
// - append
// - break/continue/fail

bool inBacktrackingScope() = !isEmpty(loops);

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

str getLabel(Label label) =
  (label is \default) ? "<label.name>" : nextTmp();
  
str asTmp(str name) = "TMP_<name>";

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



