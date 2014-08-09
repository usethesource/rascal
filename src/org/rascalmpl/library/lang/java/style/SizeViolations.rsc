module lang::java::style::SizeViolations


import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import List;
import IO;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;

data Message = sizeViolation(str category, loc pos);

/*
ExecutableStatementCount	DONE
FileLength					DONE
LineLength					TBD
MethodLength				DONE
AnonInnerLength				TBD
ParameterNumber				DONE
OuterTypeNumber				TBD
MethodCount					DONE
*/

list[Message] sizeViolationsChecks(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations) {
	return
		  executableStatementCount(ast, model, classDeclarations, methodDeclarations)
		+ fileLength(ast, model, classDeclarations, methodDeclarations)
		+ methodLength(ast, model, classDeclarations, methodDeclarations)
		+ parameterNumber(ast, model, classDeclarations, methodDeclarations)
		;

}

list[Message] executableStatementCount(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	void checkSize(Statement ast){
		if(size([s | /Statement s := ast]) > 30){
			msgs += sizeViolation("ExecutableStatementCount", ast@src);
		}
	}
	top-down-break visit(ast){
		case \initializer(Statement initializerBody): checkSize(initializerBody);
    	case \method(_, _, _, _, Statement impl): 	checkSize(impl);
    	case \constructor(_, _, _, Statement impl): checkSize(impl);
	}
	return msgs;
}

list[Message] fileLength(Declaration ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	return (ast@src.end.line > 2000) ?  [sizeViolation("FileLength", ast@src)] : [];
}

list[Message] methodLength(Declaration ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	void checkSize(Declaration ast){
		if(ast@src.end.line - ast@src.begin.line > 150){
			msgs += sizeViolation("MethodLength", ast@src);
		}
	}
	for(m <- methodDeclarations){
		checkSize(m);
	}
	
	return msgs;
}

list[Message] parameterNumber(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
	msgs = [];
	
	for(m <- methodDeclarations){
		if(size(m.parameters) > 7){
			msgs += sizeViolation("ParameterNumber", m@src);
		}
	}
	return msgs;
}

// TODO: this check should be refined per method category
list[Message] methodCount(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations){
    return size([m | /m:\method(_, _, _, _, Statement impl) := ast]) > 100 ? sizeViolation("MethodCount", ast@src) : [];
}
