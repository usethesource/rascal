module lang::java::style::BlockChecks

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import List;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import IO;

/*
EmptyBlock			DONE
LeftCurly			TBD
NeedBraces			TBD
RightCurly			TBD
AvoidNestedBlocks	DONE
*/

data Message = blockCheck(str category, loc pos);

list[Message] blockChecks(node ast, M3 model, list[Declaration] allClasses, list[Declaration] allMethods) {
	return 
		  emptyBlock(ast, model, allClasses, allMethods) 
		+ avoidNestedBlocks(ast, model, allClasses, allMethods)
		;
}

list[Message] emptyBlock(node ast, M3 model, list[Declaration] allClasses, list[Declaration] allMethods) {
  msgs = [];
  bool isEmpty(empty()) = true;
  bool isEmpty(block([])) = true;
  default bool isEmpty(Statement _) = false;
  
  void check(str category, Statement body) {
  	if(isEmpty(body)){
  			msgs += blockCheck(category, body@src);
  		}
  }
  
  top-down-break visit(ast){
  	
  	case a: \catch(_, body):	check("EmptyCatchBlock", body); 
  		
  	case a : \do(body, _):		check("EmptyDoBlock", body);
  	case a : \while(_, body):	check("EmptyWhileBlock", body);
  	case a : \for(_, _, _, Statement body) :
  								check("EmptyForBlock", body);
  	case a : \for(_, _, Statement body) :
  								check("EmptyForBlock", body);
  	case a : \if(_, Statement thenBranch):
  								check("EmptyThenBlock", thenBranch); 
  	case a : \if(_, Statement thenBranch, Statement elseBranch): {
  								check("EmptyThenBlock", thenBranch);
  								check("EmptyElseBlock", elseBranch);
  								}
  	case a: \try(Statement body, _):
  								check("EmptyTryBlock", body);
 
  	case a : \try(body, _, Statement \finally) : {
  								check("EmptyTryBlock", body);
  								check("EmptyFinallyBlock", \finally);
  								}
  	case a : \initializer(Statement initializerBody):
  								check("EmptyInitializerBlock", initializerBody);
  }

  return msgs;
}

//list[Message] avoidNestedBlocks(node ast, M3 model) {
//	return [blockCheck("NestedBlock", nested@src) | /\block(body1) := ast, size(body1) > 0 , /Statement nested:block(_) := body1];
//}

list[Message] avoidNestedBlocks(node ast, M3 model, list[Declaration] allClasses, list[Declaration] allMethods){
	msgs = [];
	void checkNesting(list[Statement] stats){
		for(stat <- stats){
			if(block(body1) := stat && /nested:\block(body1) := stat){
				msgs +=blockCheck("NestedBlock", nested@src);
			}
		}
	}
	top-down-break visit(ast){
		case \initializer(block(stats)): 			checkNesting(stats);
    	case \method(_, _, _, _, block(stats)):		checkNesting(stats);
    	case \constructor(_, _, _, block(stats)): 	checkNesting(stats);
	}
	return msgs;
}