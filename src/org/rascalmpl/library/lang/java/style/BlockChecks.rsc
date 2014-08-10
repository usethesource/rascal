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

// emptyBlock

bool isEmpty(empty()) = true;
bool isEmpty(block([])) = true;
default bool isEmpty(Statement _) = false;


list[Message] emptyBlock(\catch(_, body), list[Statement] parents, node ast, M3 model) =
	isEmpty(body) ? [blockCheck("EmptyBlock", body@src)] : [];
	
list[Message] emptyBlock(\do(body, _), list[Statement] parents, node ast, M3 model) =
	isEmpty(body) ? [blockCheck("EmptyBlock", body@src)] : [];

list[Message] emptyBlock(\while(_, body), list[Statement] parents, node ast, M3 model) =
	isEmpty(body) ? [blockCheck("EmptyBlock", body@src)] : [];
	
list[Message] emptyBlock( \for(_, _, _, Statement body), list[Statement] parents, node ast, M3 model) =
	isEmpty(body) ? [blockCheck("EmptyBlock", body@src)] : [];

list[Message] emptyBlock( \for(_, _, Statement body), list[Statement] parents, node ast, M3 model) =
	isEmpty(body) ? [blockCheck("EmptyBlock", body@src)] : [];
	
list[Message] emptyBlock( \try(Statement body, _), list[Statement] parents, node ast, M3 model) =
	isEmpty(body) ? [blockCheck("EmptyBlock", body@src)] : [];	
	
list[Message] emptyBlock( \try(body, _, Statement \finally), list[Statement] parents, node ast, M3 model) =
	isEmpty(body) ? [blockCheck("EmptyBlock", body@src)] : [];		
	
list[Message] emptyBlock(\initializer(Statement initializerBody), list[Statement] parents, node ast, M3 model) =
	isEmpty(initializerBody) ? [blockCheck("EmptyBlock", initializerBody@src)] : [];	
	
list[Message] emptyBlock(\if(_, Statement thenBranch), list[Statement] parents, node ast, M3 model) =
	isEmpty(thenBranch) ? [blockCheck("EmptyBlock", thenBranch@src)] : [];
	
list[Message] emptyBlock(\if(_, Statement thenBranch, Statement elseBranch), list[Statement] parents, node ast, M3 model) =
	(isEmpty(thenBranch) ? [blockCheck("EmptyBlock", thenBranch@src)] : []) +
	(isEmpty(elseBranch) ? [blockCheck("EmptyBlock", elseBranch@src)] : []);

default list[Message] emptyBlock(\if(_, Statement thenBranch, Statement elseBranch), list[Statement] parents, node ast, M3 model) = [];	

//list[Message] emptyBlock(node ast, M3 model, OuterDeclarations decls) {
//  msgs = [];
//  bool isEmpty(empty()) = true;
//  bool isEmpty(block([])) = true;
//  default bool isEmpty(Statement _) = false;
//  
//  void check(str category, Statement body) {
//  	if(isEmpty(body)){
//  			msgs += blockCheck(category, body@src);
//  		}
//  }
//  
//  top-down-break visit(ast){
//  	
//  	case \catch(_, body):	check("EmptyBlock", body); 
//  		
//  	case \do(body, _):		check("EmptyBlock", body);
//  	case \while(_, body):	check("EmptyBlock", body);
//  	case \for(_, _, _, Statement body) :
//  								check("EmptyBlock", body);
//  	case \for(_, _, Statement body) :
//  								check("EmptyBlock", body);
//  	case \if(_, Statement thenBranch):
//  								check("EmptyBlock", thenBranch); 
//  	case \if(_, Statement thenBranch, Statement elseBranch): {
//  								check("EmptyBlock", thenBranch);
//  								check("EmptyBlock", elseBranch);
//  								}
//  	case \try(Statement body, _):
//  								check("EmptyBlock", body);
// 
//  	case \try(body, _, Statement \finally) : {
//  								check("EmptyBlock", body);
//  								check("EmptyBlock", \finally);
//  								}
//  	case \initializer(Statement initializerBody):
//  								check("EmptyBlock", initializerBody);
//  }
//
//  return msgs;
//}

// needBraces

bool isBlock(Statement body) = block(_) := body;

list[Message] needBraces(\do(_, Statement body), list[Statement] parents, node ast, M3 model) =
	!isBlock(body) ? [blockCheck("NeedBraces", body@src)] : [];
	
list[Message] needBraces(\while(_, Statement body), list[Statement] parents, node ast, M3 model) =
	!isBlock(body) ? [blockCheck("NeedBraces", body@src)] : [];
	
list[Message] needBraces(\for(_, _, _, Statement body), list[Statement] parents, node ast, M3 model) =
	!isBlock(body) ? [blockCheck("NeedBraces", body@src)] : [];

list[Message] needBraces(\for(_, _, Statement body), list[Statement] parents, node ast, M3 model) =
	!isBlock(body) ? [blockCheck("NeedBraces", body@src)] : [];

list[Message] needBraces(\if(_, Statement thenBranch), list[Statement] parents, node ast, M3 model) =
	!isBlock(thenBranch) ? [blockCheck("NeedBraces", thenBranch@src)] : [];

list[Message] needBraces(\if(_, Statement thenBranch, Statement elseBranch), list[Statement] parents, node ast, M3 model) =
	(!isBlock(thenBranch) ? [blockCheck("NeedBraces", thenBranch@src)] : []) +
	(!isBlock(elseBranch) ? [blockCheck("NeedBraces", elseBranch@src)] : []);

default list[Message] needBraces(Statement s, list[Statement] parents, node ast, M3 model) = [];
		
//list[Message] needBraces(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	
//	void check(Statement body){
//		if(block(_) !:= body){
//			msgs += blockCheck("NeedBraces", body@src);
//		}
//	}
//
//	visit(ast){
//		case \do(body, _):		
//			check(body);
//  		case \while(_, body):	
//  			check(body);
//		case \for(_, _, _, Statement body) :
//  			check(body);
//  		case \for(_, _, Statement body) :
//  			check(body);
//  		case \if(_, Statement thenBranch):
//  			check(thenBranch); 
//  		case \if(_, Statement thenBranch, Statement elseBranch): {
//  				check(thenBranch);
//  				check(elseBranch);
//  			}
//		}
//	return msgs;
//}

list[Message] avoidNestedBlocks(b: block(stats), list[Statement] parents, node ast, M3 model) {
	if(size(parents) > 0 && isBlock(head(parents))){
		return [blockCheck("NestedBlock", b@src)];
	}
	return [];
}

default list[Message] avoidNestedBlocks(Statement s, list[Statement] parents, node ast, M3 model) = [];
	
//list[Message] avoidNestedBlocks(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	void checkNesting(list[Statement] stats){
//		for(stat <- stats){
//			if(block(body1) := stat && /nested:\block(body1) := stat){
//				msgs +=blockCheck("NestedBlock", nested@src);
//			}
//		}
//	}
//	top-down-break visit(ast){
//		case \initializer(block(stats)): 			checkNesting(stats);
//    	case \method(_, _, _, _, block(stats)):		checkNesting(stats);
//    	case \constructor(_, _, _, block(stats)): 	checkNesting(stats);
//	}
//	return msgs;
//}
