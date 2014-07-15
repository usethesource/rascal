module lang::java::style::NamingConventions

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

/*
AbstractClassName		abstract classes					^Abstract.*$|^.*Factory$
ClassTypeParameterName	class type parameters				^[A-Z]$
ConstantName	 		constants (static, final fields)	^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$
LocalFinalVariableName	local, final variables, 
						including catch parameters			^[a-z][a-zA-Z0-9]*$
LocalVariableName	 	local, non-final variables, 
						including catch parameters			^[a-z][a-zA-Z0-9]*$
MemberName				non-static fields					^[a-z][a-zA-Z0-9]*$
MethodName				methods								^[a-z][a-zA-Z0-9]*$
MethodTypeParameterName	method type parameters				^[A-Z]$
PackageName				packages							^[a-z]+(\.[a-zA-Z_][a-zA-Z0-9_]*)*$
ParameterName			parameters							^[a-z][a-zA-Z0-9]*$
StaticVariableName		static, non-final fields			^[a-z][a-zA-Z0-9]*$
TypeName				classes and interfaces				^[A-Z][a-zA-Z0-9]*$
*/

data Message = namingConvention(str category, loc pos, str id);

list[Message] namingConventions(node ast, M3 model) {
  rel[loc name, loc src] decls = model@declarations;
  
  return 
  	for(<n, pos> <- decls){
  		path = n.path;
  		elementName = path[findLast(path, "/") + 1 ..];
  		sep = findLast(elementName, "(");
  		if(sep > 0){
  			elementName =  elementName[ .. sep];
  		} else {
  			sep = findLast(elementName, ".");
  			if(sep > 0){
  				elementName =  elementName[ .. sep];
  			}
  		}
  		switch(n.scheme){
  			case "java+compilationUnit": {
  					elems = split(elementName, ".");
  					if( /^[a-z]+$/ !:= elems[0]  || any(elem <- elems[1..], /^[a-zA-Z_][a-zA-Z0-9_]*$/ !:= elem)){
  						append namingConvention("PackageName", pos, elementName); 
  					}
  				}
  			
  			case "java+package": {
  					elems = split(elementName, ".");
  					if( /^[a-z]+$/ !:= elems[0]  || any(elem <- elems[1..], /^[a-zA-Z_][a-zA-Z0-9_]*$/ !:= elem)){
  						append namingConvention("PackageName", pos, elementName); 
  					}
  				}
  			case "java+class":
  					if(/^[A-Z][a-zA-Z0-9]*$/ !:= elementName){ append namingConvention("TypeName", pos, elementName); }
  				
  			case "java+constructor": continue;
  			
  			case "java+field":		
  					if(/^[a-z][a-zA-Z0-9]*$/ !:= elementName){ append namingConvention("MemberName", pos, elementName); }
  			
  			case "java+method":		
					if(/^[a-z][a-zA-Z0-9]*$/ !:= elementName){ append namingConvention("MethodName", pos, elementName); }
  			
  			case "java+parameter":
  					if(/^[a-z][a-zA-Z0-9]*$/ !:= elementName){ append namingConvention("ParameterName", pos, elementName); }
  				
  			case "java+variable":	
  					if(/^[a-z][a-zA-Z0-9]*$/ !:= elementName){ append namingConvention("ParameterName", pos, elementName); }
  			
  			case "java+interface":	
  					if(/^[A-Z][a-zA-Z0-9]*$/ !:= elementName){ append namingConvention("TypeName", pos, elementName); }
  			
  			default:
  				throw "Cannot handle scheme <n.scheme>";
  	}
  }
}