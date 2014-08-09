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

alias OuterDeclarations = 
	tuple[	list[Declaration] allClasses,
			list[Declaration] allInterfaces,
			list[Declaration] allEnums,
			list[Declaration] allMethods,
			map[str,set[Statement]] allStatements
		 ];

OuterDeclarations getAllDeclarations(node ast){
	cls = [];
	ifaces = [];
	enums = [];
	mtds = [];
	stats = ();
	
	void add(str kind, Statement stat){
		if(stats[kind] ?) stats[kind] += {stat}; else stats[kind] = {stat};
	}

	top-downvisit(ast){
		// Collect declarations
		
		case c: \class(_, _, _, _):
	  		cls += c;
	  	case ifc: \interface(_, _, _, _):
	  		ifaces += ifc;
	  	case enm: \enum(_, _, _, _):
	  		enums += enm;
	  	case m: \method(_, _, _, _, _): 
    		mtds +=  m;
      	case m: \method(_, _, _, _):	
    		mtds += m;
    	case m: \constructor(_, _,  _, _):
			mtds += m;

		// Collect statements
		
		case s : \assert(_):
			add("assert", s);
    	case s : \assert(_, _): 
    		add("assert", s);
    	case s : \block(_):
    		add("block", s);
    	case s : \break():
    		add("break", s);
    	case s : \break(_):
    		add("break", s);
    	case s : \continue():
    		add("continue", s);
    	case s : \continue(_):
    		add("continue", s);
    	case s : \do(_, _):
    		add("do", s);
    	case s : \empty():
    		add("empty", s);
    	case s : \foreach(_, _, _):
    		add("foreach", s);
    	case s : \for(_, _, _, _):
    		add("for", s);
    	case s : \for(_, _, _):
    		add("for", s);
    	case s : \if(_, _):
    		add("if", s);
    	case s : \if(_, _, _):
    		add("if", s);
    	case s : \label(_, _):
    		add("label", s);
   		case s : \return(_):
   			add("return", s);
    	case s : \return():
    		add("return", s);
    	case s : \switch(_, _):
    		add("switch", s);
    	case s : \case(_):
    		add("case", s);
    	case s : \defaultcase():
    		add("defaultcase", s);
    	case s : \synchronizedStatement(_, _):
    		add("synchronizedStatement", s);
    	case s : \throw(Expression expression):
    		add("throw", s);
    	case s : \try(_, _):
    		add("try", s);
   		case s : \try(_, _, _)  :
   			add("try", s);                                      
    	case s : \catch(_, _):
    		add("catch", s); 
    	case s : \declarationStatement(_):
    		add("declarationStatement", s);
    	case s : \while(_, _):
    		add("while", s);
    	case s : \expressionStatement(_):
    		add("expressionStatement", s);
    	case s : \constructorCall(_, _, _):
    		add("constructorCall", s);
    	case s : \constructorCall(_, _):
    		add("constructorCall", s);
	}
	return <cls, ifaces, enums, mtds, stats>;
}

//list[Declaration] getAllClassDeclarations(node ast){
//	cls = [];
//	top-down-break visit(ast){
//	  case c: \class(_, _, _, _):
//	  		cls += c;
//	}
//	return cls;
//}
//
//list[Declaration] getAllClassDeclarations(set[node] asts){
//	cls = [];
//	for(ast <- asts){
//		top-down-break visit(ast){
//		  case c: \class(_, _, _, _):
//		  		cls += c;
//		}
//	}
//	return cls;
//}

//Declaration getClass(loc src,  node ast){
//	top-down-break visit(ast){
//	  case c: \class(_, _, _, _):
//	  	if(c@src == src){
//	  		return c;
//	  	}
//	}
//	throw "class <src> not found";
//}
//
//Declaration getClass(str name,  node ast){
//	top-down-break visit(ast){
//	  case c: \class(str name, _, _, _):
//	  	return c;
//	}
//	throw "class <src> not found";
//}

//list[Declaration] getAllMethodDeclarations(node ast){
//	mtds = [];
//	top-down-break visit(ast){
//    	case m: \method(_, _, _, _, _): 
//    		mtds +=  m;
//    	case m: \method(_, _, _, _):	
//    		mtds += m;
//    	case m: \constructor(_, _,  _, _):
//			mtds += m;
//	}
//	return mtds;
//}
//
//list[Declaration] getAllMethodDeclarations(set[node] asts){
//	mtds = [];
//	for(ast <- asts){
//		top-down-break visit(ast){
//	    	case m: \method(_, _, _, _, _): 
//	    		mtds +=  m;
//	    	case m: \method(_, _, _, _):	
//	    		mtds += m;
//	    	case m: \constructor(_, _,  _, _):
//				mtds += m;
//		}
//	}
//	return mtds;
//}

str packageName2String(package(str name)) = name;
str packageName2String(package(Declaration parentPackage, str name)) = "<packageName2String(parentPackage)>.<name>";

str getPackageName(node ast){
	top-down-break visit(ast){
		case p: \package(_):	return packageName2String(p);
    	case p: \package(_, _):	return packageName2String(p);
    }
 }