module lang::rascalcore::check::Test4

import demo::lang::Exp::Concrete::WithLayout::Syntax;

import String;
import ParseTree;                                                   

int eval(str txt) = eval(parse(#start[Exp], txt).top); 

value fn(Tree t) = t.top;

//import ParseTree;
//
//syntax A = "a";
//syntax As = {A ","}+ as;
//
//value fn(start[As] pt){
//    args = pt.top.as;
//}


//@doc{
//Convert a sequence of commands to a textual patch value to be applied to the editor
//containing the commands. The patch is based on the results of evaluating the commands
//and comparing the outputs with what is in the source (pt) itself. Differences in command
//output are reconciled through the patch. 
//
//A patch is list of tuples from loc to str. Loc to "" represents removal.
//A loc with length=0 to x represents insertion of x.
//}
//lrel[loc, str] commands2patch(start[Commands] pt) {
//  args = pt.
//  top.
//  args[0].
//  args; // the list of Commands (EvalCommand+)
//}