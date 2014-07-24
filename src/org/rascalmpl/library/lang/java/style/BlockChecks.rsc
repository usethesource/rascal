module lang::java::style::BlockChecks

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;

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

list[Message] blockChecks(node ast, M3 model) {
	return emptyBlocks(ast, model) + avoidNestedBlocks(ast, model);
}

list[Message] emptyBlock(node ast, M3 model) {
  msgs = [];
  bool isEmpty(empty()) = true;
  bool isEmpty(block([])) = true;
  default bool isEmpty(Statement _) = false;
  
  void check(str category, Statement parent, Statement body) {
  	if(isEmpty(body)){
  			msgs += blockCheck("emptyCatchBlock", parent@src);
  		}
  }
  
  visit(ast){
  
  	/* Missing: STATIC_INIT, what is it? */
  	
  	case a: \catch(_, body):	check("emptyCatchBlock", a, body); 
  		
  	case a : \do(body, _):		check("emptyDoBlock", a, body);
  	case a : \while(_, body):	check("emptyWhileBlock", a, body);
  	case a : \for(_, _, Statement body) :
  								check("emptyForBlock", a, body);
  	case a : \if(_, Statement thenBranch):
  								check("emptyThenBlock", a, thenBranch); 
  	case a : \if(_, Statement thenBranch, Statement elseBranch): {
  								check("emptyThenBlock", a, thenBranch);
  								check("emptyElseBlock", a, elseBranch);
  								}
  	case a: \try(Statement body, _):
  								check("emptyTryBlock", a, body);
 
  	case a : \try(body, _, Statement \finally) : {
  								check("emptyTryBlock", a, body);
  								check("emptyFinallyBlock", a, \finally);
  								}
  	case a : \initializer(Statement initializerBody):
  								check("emptyInitializerBlock", a, initializerBody);
  }

  return msgs;
}

list[Message] avoidNestedBlocks(node ast, M3 model) {
	return [blockCheck("nestedBlock", a@src) | /a:\block(_,body1) := ast, /block(_) := body1];
}