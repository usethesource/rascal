module lang::java::style::Metrics

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import Set;
import Type;
import Node;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;

import IO;

/*
BooleanExpressionComplexity		DONE
ClassDataAbstractionCoupling	DONE
ClassFanOutComplexity			DONE
CyclomaticComplexity			DONE
NPathComplexity					DONE
JavaNCSS						TBD
*/

data Message = metric(str category, loc pos);


/* --- booleanExpressionComplexity ------------------------------------------*/

bool isBooleanOperator(\infix(_, str operator, _)) = operator in {"&&", "&", "||", "|", "^"};
bool isBooleanOperator(\prefix("!", _)) = true;
default bool isBooleanOperator(Expression e) = false;

int countBooleanOperators(list[Expression] parents){
	nOperators = 0;
	for(parent <- parents){
		if(isBooleanOperator(parent)){
			nOperators += 1;
		}
	}
	return nOperators;
}

list[Message] booleanExpressionComplexity(Expression exp,  list[Expression] parents, node ast, M3 model) =
	(exp has src && isBooleanOperator(exp) && countBooleanOperators(parents) > 2) ? [metric("BooleanExpressionComplexity", exp@src)] : [];

/* --- classDataAbstractionCoupling -----------------------------------------*/

set[str] excludedClasses = {
	"boolean", "byte", "char", "double", "float", "int", "long", "short", "void", 
	"Boolean", "Byte", "Character", "Double", "Float", "Integer", "Long", "Short", 
	"Void", "Object", "Class", "String", "StringBuffer", "StringBuilder", 
	"ArrayIndexOutOfBoundsException", "Exception", "RuntimeException", 
	"IllegalArgumentException", "IllegalStateException", "IndexOutOfBoundsException", 
	"NullPointerException", "Throwable", "SecurityException", "UnsupportedOperationException", 
	"List", "ArrayList", "Deque", "Queue", "LinkedList", "Set", "HashSet", "SortedSet", "TreeSet", 
	"Map", "HashMap", "SortedMap", "TreeMap"};

str getTypeName(TypeSymbol tp){
	if(tp has decl){
		p = tp.decl.path;
		res = p[findLast(p, "/")+1 .. ];
		//println("getName: <tp>, <res>");
		return res;
	} else {
		res = getName(tp);
		//println("getName: <tp>, <res>");
		return res;
	}
}

list[Message] classDataAbstractionCoupling(Expression exp: \newObject(_, _, _, _),  list[Expression] parents, node ast, M3 model) {
	updateCheckState("classDataAbstractionCoupling", getTypeName(exp@typ));
	return [];
}
	
list[Message] classDataAbstractionCoupling(Expression exp: \newObject(_, _, _),  list[Expression] parents, node ast, M3 model){
	updateCheckState("classDataAbstractionCoupling", getTypeName(exp@typ));
	return [];
}

list[Message] classDataAbstractionCoupling(Expression exp: \newObject(_, _),  list[Expression] parents, node ast, M3 model){
	updateCheckState("classDataAbstractionCoupling", getTypeName(exp@typ));
	return [];
}
	
// update/finalize

value updateClassDataAbstractionCoupling(value current, value delta) { 
	if(set[str] cs:= current && str d := delta) return cs + d; 
}

list[Message] finalizeClassDataAbstractionCoupling(Declaration d, value current) =
	(set[str] s := current && size(s- excludedClasses) > 7) ? [metric("ClassDataAbstractionCoupling", d@src)] : [];	


/* --- classFanOutComplexity ------------------------------------------------*/

str getTypeName(loc s) = s.path[1..];

set[str] getTypeNames(set[loc] locs) = { getTypeName(l) | l <- locs };

list[Message] classFanOutComplexity(Declaration cls, list[Declaration] parents, node ast, M3 model) {

	println("<cls@src>: <getTypeNames(model@typeDependency[model@containment[cls@decl]])>");
	return size(getTypeNames(model@typeDependency[model@containment[cls@decl]]) - excludedClasses) > 7 ? [ metric("ClassFanOutComplexity", cls@src) ] : [];
	
	}

//list[Message] classFanOutComplexity(node ast, M3 model, OuterDeclarations decls){
//	return 
//		[ metric("ClassFanOutComplexity", c) | c <- classes(model), size(model@typeDependency[model@containment[c]] - excludedClasses) > 7 ];
//}

// NOTE: The following checks are alerady part of the OSSMETER metrics are not repeated here.

//--------------------------------
// cyclomaticComplexity

//list[Message] cyclomaticComplexity(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	void checkCC(Statement body){
//		int cnt = 1;
//		visit(body){
//			case \do(_, _): 			cnt += 1;
//			case \while(_, _): 			cnt += 1;
//    		case \foreach(_, _, _): 	cnt += 1;
//    		case \for(_, _, _, _) : 	cnt += 1;
//    		case \for(_, _, _): 		cnt += 1;
//    		case \if(_, _): 			cnt += 1;
//    		case \if(_, _, _): 			cnt += 1;
//			case \case(_): 				cnt += 1;
//			case \conditional(_, _, _): cnt += 1;
//  			case \catch(_, _): 			cnt += 1;
//   			case \infix(_, "&&", _): cnt += 1;
//    		case \infix(_, "||", _): cnt + 1;
//		}
//		if(cnt > 10) msgs += metric("CyclomaticComplexity", body@src);
//	}
//	
//	top-down-break visit(ast){
//		case \initializer(Statement initializerBody): checkCC(initializerBody);
//    	case \method(_, _, _, _, Statement impl): 	checkCC(impl);
//    	case \constructor(_, _, _, Statement impl): checkCC(impl);
//	}
//	return msgs;
//}

// nPathComplexity

//list[Message] nPathComplexity(node ast, M3 model, OuterDeclarations decls){
//	msgs = [];
//	void checkNPath(Statement body){
//		if(nPath(body) > 200){
//			msgs += metric("NPathComplexity", body@src);
//		}
//	}
//	top-down-break visit(ast){
//		case \initializer(Statement initializerBody): checkNPath(initializerBody);
//    	case \method(_, _, _, _, Statement impl): 	checkNPath(impl);
//    	case \constructor(_, _, _, Statement impl): checkNPath(impl);
//	}
//	return msgs;
//}
//
//// http://pmd.sourceforge.net/pmd-4.3.0/xref/net/sourceforge/pmd/rules/design/NpathComplexity.html
//
//int nPathExpression(Expression e){
//	cnt = 1;
//	visit(e){
//		case \infix(_, "&&", _): 	
//			cnt += 1;
//    	case \infix(_, "||", _): 	
//    		cnt += 1;
//	}
//	return cnt;
//}
//
//int nPath(node ast){
//	switch(ast){
//			case \block(list[Statement] statements):
//					return (1 | it * nPath(s) | s <- statements);
//					
//			case \do(body,cond): 		
//					return nPathExpression(cond) + nPath(body) + 1;
//					
//			case \while(cond, body): 	
//					return nPathExpression(cond) + nPath(body) + 1;
//					
//    		case \foreach(_, _,body): 	
//    				return nPath(body);
//    				
//    		case \for(_, cond, _, body):
//    				return nPathExpression(cond) + nPath(body) + 1;
//    				
//    		case \for(_, _, body): 		
//    				return nPath(body);
//    				
//    		case \if(cond, thenBranch): 
//    				return nPathExpression(cond) + nPath(thenBranch) + 1;
//    				
//    		case \if(cond, thenBranch, elseBranch):
//    				return nPathExpression(cond) + nPath(thenBranch) + nPath(elseBranch);
//    				
//			case \switch(Expression expression, list[Statement] statements):
//					return nPathExpression(expression) + (0 | it + nPath(s) | s <- statements);
//					
//			case \case(_):
//					return 0;
//					
//			case \defaultCase():
//					return 0;
//			
//			case \try(Statement body, list[Statement] catchClauses):
//					return nPath(body) + (0 | it + nPath(s) | s <- catchClauses);
//					
//    		case \try(Statement body, list[Statement] catchClauses, Statement \finally):                                        
//					return nPath(body) + (0 | it + nPath(s) | s <- catchClauses) + nPath(\finally);
//										
//  			case \catch(_, body): 		
//  					return nPath(body);
//
//    			case \return(Expression e): 
//    				return nPathExpression(e);
//    		
//    		default:
//    			return 1;
//		}
//}
