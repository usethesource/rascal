module lang::java::style::ClassDesign

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import IO;

data Message = classDesign(str category, loc pos);

/*
VisibilityModifier			DONE
FinalClass					DONE
InterfaceIsType				TBD
HideUtilityClassConstructor	TBD
DesignForExtension			TBD
MutableException			DONE
ThrowsCount					DONE
InnerTypeLast				TBD
*/

list[Message] classDesign(node ast, M3 model) {
	return
		     visibilityModifier(ast, model)
		   + finalClass(ast, model)
		   + throwsCount(ast, model)
		   + mutableException(ast, model)
		;
}

list[Message] visibilityModifier(node ast, M3 model){
	annotations = model@annotations;
	msgs = [];
	void checkVis(node ast, name){
		annos = annotations[ast@src];
		if((\public() in annos() & /^serialVersionUID/ !:= name) || \protected in annos){
			msgs += classDesign("VisibilityModifier", ast@src);
		}
		
		top-down-break visit(ast){
			case \initializer(Statement initializerBody): checkSize(initializerBody);
    		case \method(_, _, _, _, Statement impl): 	checkSize(impl);
    		case \constructor(_, _, _, Statement impl): checkSize(impl);
		}
	}
	
	top-down-break visit(ast){
		case m: \field(Type \type, list[Expression] fragments): checkVis(m, intercalate(".", fragments));
	}
	return msgs;
}

list[Message] finalClass(node ast, M3 model){
    annotations = model@annotations;
	msgs = [];
	void notPrivate(node ast) = \private() notin annotations[ast@src];
	
	bool hasOnlyPrivateMethods(node ast){
		top-down-break visit(ast){
			case m: \initializer(Statement initializerBody):	if(notPrivate(ast)) return false;
    		case m: \method(_, _, _, _, Statement impl): 		if(notPrivate(ast)) return false;
    		case m: \constructor(_, _, _, Statement impl): 		if(notPrivate(ast)) return false;
		}
		return true;
	}
	void checkFinalClass(node ast) { 
		if(hasOnyPrivateMethods(ast) && \final() notin annotations[c]){
			msgs += classDesign("FinalClass", ast@src);
		}
	}
	
	top-down-break visit(ast){
		case c: \class(str name, list[Type] extends, list[Type] implements, list[Declaration] body):
			checkFinalClass(c);
	
    	case c: \class(list[Declaration] body):
    		checkFinalClass(c);
    }
    return msgs;
}

list[Message] mutableException(node ast, M3 model){
	annotations = model@annotations;
	msgs = [];
	bool hasOnlyFinalFields(node ast){
		top-down-break visit(ast){
			case m: \field(Type \type, list[Expression] fragments): 
				if(\final() notin annotations[m]){
					return false;
				}
		}
		return true;
	}

	top-down-break visit(ast){
		case c: \class(str name, list[Type] extends, list[Type] implements, list[Declaration] body):
			if((/Exception$/ := name || /Error$/ := name) && !hasOnlyFinalFields(c)){ 
				msgs += classDesign("MutableException", c@src);
			}
	}
    return msgs;
}

list[Message] throwsCount(node ast, M3 model){
	msgs = [];

	void cntExceptions(node ast, list[Expression] exceptions){
		if(size(exceptions) > 1){
			msgs += classDesign("TrhowsCount", ast@src);
		}
	}
	top-down-break visit(ast){
		case m: \method(_, _, _, list[Expression] exceptions, _):	cntExceptions(m, exceptions);
    	case m: \method(_, _, _, list[Expression] exceptions): 		cntExceptions(m, exceptions);
    	case m: \constructor(_, _, list[Expression] exceptions, _):	cntExceptions(m, exceptions);
	}
	return msgs;
}


