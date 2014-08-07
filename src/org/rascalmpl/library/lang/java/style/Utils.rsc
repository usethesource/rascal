module lang::java::style::Utils

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import List;
import Set;
import IO;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

//loc getDeclaredEntity(loc src, M3 model){
//	res = model@declarations<1,0>[src];
//	if(size(res) != 1){
//		throw "getEntity: undefined src <src>";
//	}
//	for(e <- res){
//		return e;
//	}
//}

list[Declaration] getAllClassDeclarations(node ast){
	cls = [];
	top-down-break visit(ast){
	  case c: \class(_, _, _, _):
	  		cls += c;
	}
	return cls;
}

list[Declaration] getAllClassDeclarations(set[node] asts){
	cls = [];
	for(ast <- asts){
		top-down-break visit(ast){
		  case c: \class(_, _, _, _):
		  		cls += c;
		}
	}
	return cls;
}

Declaration getClass(loc src,  node ast){
	top-down-break visit(ast){
	  case c: \class(_, _, _, _):
	  	if(c@src == src){
	  		return c;
	  	}
	}
	throw "class <src> not found";
}

Declaration getClass(str name,  node ast){
	top-down-break visit(ast){
	  case c: \class(str name, _, _, _):
	  	return c;
	}
	throw "class <src> not found";
}

list[Declaration] getAllMethodDeclarations(node ast){
	mtds = [];
	top-down-break visit(ast){
    	case m: \method(_, _, _, _, _): 
    		mtds +=  m;
    	case m: \method(_, _, _, _):	
    		mtds += m;
    	case m: \constructor(_, _,  _, _):
			mtds += m;
	}
	return mtds;
}

list[Declaration] getAllMethodDeclarations(set[node] asts){
	mtds = [];
	for(ast <- asts){
		top-down-break visit(ast){
	    	case m: \method(_, _, _, _, _): 
	    		mtds +=  m;
	    	case m: \method(_, _, _, _):	
	    		mtds += m;
	    	case m: \constructor(_, _,  _, _):
				mtds += m;
		}
	}
	return mtds;
}

str packageName2String(package(str name)) = name;
str packageName2String(package(Declaration parentPackage, str name)) = "<packageName2String(parentPackage)>.<name>";

str getPackageName(node ast){
	top-down-break visit(ast){
		case p: \package(_):	return packageName2String(p);
    	case p: \package(_, _):	return packageName2String(p);
    }
 }