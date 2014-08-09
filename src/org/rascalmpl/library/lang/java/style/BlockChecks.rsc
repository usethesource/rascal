module lang::java::style::BlockChecks

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import List;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;
import lang::java::style::Utils;

import IO;

/*
EmptyBlock			DONE
LeftCurly			TBD
NeedBraces			DONE
RightCurly			TBD
AvoidNestedBlocks	DONE
*/

data Message = blockCheck(str category, loc pos);



list[Message] emptyBlock(node ast, M3 model, OuterDeclarations decls) {
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
  	
  	case \catch(_, body):	check("EmptyBlock", body); 
  		
  	case \do(body, _):		check("EmptyBlock", body);
  	case \while(_, body):	check("EmptyBlock", body);
  	case \for(_, _, _, Statement body) :
  								check("EmptyBlock", body);
  	case \for(_, _, Statement body) :
  								check("EmptyBlock", body);
  	case \if(_, Statement thenBranch):
  								check("EmptyBlock", thenBranch); 
  	case \if(_, Statement thenBranch, Statement elseBranch): {
  								check("EmptyBlock", thenBranch);
  								check("EmptyBlock", elseBranch);
  								}
  	case \try(Statement body, _):
  								check("EmptyBlock", body);
 
  	case \try(body, _, Statement \finally) : {
  								check("EmptyBlock", body);
  								check("EmptyBlock", \finally);
  								}
  	case \initializer(Statement initializerBody):
  								check("EmptyBlock", initializerBody);
  }

  return msgs;
}

list[Message] needBraces(node ast, M3 model, OuterDeclarations decls){
	msgs = [];
	
	void check(Statement body){
		if(block(_) !:= body){
			msgs += blockCheck("NeedBraces", body@src);
		}
	}

	visit(ast){
		case \do(body, _):		
			check(body);
  		case \while(_, body):	
  			check(body);
		case \for(_, _, _, Statement body) :
  			check(body);
  		case \for(_, _, Statement body) :
  			check(body);
  		case \if(_, Statement thenBranch):
  			check(thenBranch); 
  		case \if(_, Statement thenBranch, Statement elseBranch): {
  				check(thenBranch);
  				check(elseBranch);
  			}
		}
	return msgs;
}

list[Message] avoidNestedBlocks(node ast, M3 model, OuterDeclarations decls){
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
