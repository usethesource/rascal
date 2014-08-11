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

/* --- emptyBlock -----------------------------------------------------------*/

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

/* --- needBraces -----------------------------------------------------------*/

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
	
/* --- avoidNestedBlocks ----------------------------------------------------*/

list[Message] avoidNestedBlocks(b: block(stats), list[Statement] parents, node ast, M3 model) {
	if(size(parents) > 0 && isBlock(head(parents))){
		return [blockCheck("NestedBlock", b@src)];
	}
	return [];
}

default list[Message] avoidNestedBlocks(Statement s, list[Statement] parents, node ast, M3 model) = [];
