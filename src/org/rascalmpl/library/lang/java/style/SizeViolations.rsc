module lang::java::style::SizeViolations


import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import IO;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

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

list[Message] sizeViolations(node ast, M3 model) {
	return
		   executableStatementCount(ast, model)
		+  fileLength(ast, model)
		+  methodLength(ast, model)
		+ parameterNumber(ast, model)
		;

}

list[Message] executableStatementCount(node ast, M3 model){
	msgs = [];
	void checkSize(node ast){
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

list[Message] fileLength(node ast, M3 model){
	return (ast@src.end.line > 2000) ?  sizeViolation("FileLength", ast@src);
}

list[Message] methodLength(node ast, M3 model){
	msgs = [];
	void checkSize(node ast){
		if(ast@src.end.line - ast@src.begin.line > 150){
			msgs += sizeViolation("MethodLength", ast@src);
		}
	}
	top-down-break visit(ast){
    	case m: \method(_, _, _, _, Statement impl): checkSize(m);
    	case m: \constructor(_, _, _, Statement impl): checkSize(m);
	}
	return msgs;
}

list[Message] parameterNumber(node ast, M3 model){
	parameters = model@parameters;
	void checkSize(node ast){
		if(size(parameters[ast@src]) > 7){
			msgs += sizeViolation("ParameterNumber", ast@src);
		}
	}
	top-down-break visit(ast){
    	case m: \method(_, _, _, _, Statement impl): checkSize(m);
    	case m: \constructor(_, _, _, Statement impl): checkSize(m);
	}
	return msgs;
}

// TODO: this check should be refined er method category
list[Message] methodCount(node ast, M3 model){
    return size([m | /m:\method(_, _, _, _, Statement impl) := ast]) > 100 ? sizeViolation("MethodCount", ast@src) : [];
}
