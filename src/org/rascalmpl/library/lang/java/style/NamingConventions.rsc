module lang::java::style::NamingConventions

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import IO;

/*
AbstractClassName		abstract classes					^Abstract.*$|^.*Factory$				DONE
ClassTypeParameterName	class type parameters				^[A-Z]$
ConstantName	 		constants (static, final fields)	^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$			DONE
LocalFinalVariableName	local, final variables, 													DONE
						including catch parameters			^[a-z][a-zA-Z0-9]*$						DONE
LocalVariableName	 	local, non-final variables, 												DONE
						including catch parameters			^[a-z][a-zA-Z0-9]*$						DONE
MemberName				non-static fields					^[a-z][a-zA-Z0-9]*$
MethodName				methods								^[a-z][a-zA-Z0-9]*$						DONE
MethodTypeParameterName	method type parameters				^[A-Z]$
PackageName				packages							^[a-z]+(\.[a-zA-Z_][a-zA-Z0-9_]*)*$		DONE
ParameterName			parameters							^[a-z][a-zA-Z0-9]*$						DONE
StaticVariableName		static, non-final fields			^[a-z][a-zA-Z0-9]*$						DONE
TypeName				classes and interfaces				^[A-Z][a-zA-Z0-9]*$						DONE
*/

data Message = namingConvention(str category, loc pos, str id);

list[Message] namingConventions(node ast, M3 model) {
  rel[loc name, loc src] decls = model@declarations;
  rel[loc name, Modifier modifier] modifiers = model@modifiers;
  
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
  		elemModifiers = modifiers[n];
  		println("<elementName>: <elemModifiers>, <n>, <pos>");
  		
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
  					if(\abstract() in elemModifiers){
  						if(!(/^Abstract/ := elementName || /Factory$/ := elementName)){
  							append namingConvention("AbstractClassName", pos, elementName);
  						}
  					} else
  					if(/^[A-Z][a-zA-Z0-9]*$/ !:= elementName){ append namingConvention("TypeName", pos, elementName); }
  				
  			case "java+constructor": continue;
  			
  			case "java+field":	
  					if({\static(), \final()} <=  elemModifiers){
  						elems = split(elementName, "_");
  						println("java+field: <elems>");
  						if( /^[A-Z][A-Z0-9]*$/ !:= elems[0] || any( elem <- elems[1..], /^[A-Z0-9]+$/ !:= elem)){
  							append namingConvention("ConstantName", pos, elementName);
  						}
  					} else	
  					if(/^[a-z][a-zA-Z0-9]*$/ !:= elementName){ 
  						str category = \static() in elemModifiers ? "StaticVariableName" :  "MemberName";
  						append namingConvention(category, pos, elementName); 
  					}
  			
  			case "java+method":		
					if(/^[a-z][a-zA-Z0-9]*$/ !:= elementName && \static() notin elemModifiers){ 
						append namingConvention("MethodName", pos, elementName); 
					}
  			case "java+typeVariable":
  					if(/^[A-Z]$/ !:= elementName){ 
  						// TODO: how do we diistinguish these two cases?
  						str category = isClass(pos) ? "ClassTypeParameterName" : "MethodTypeParameterName";
  						append namingConvention(category, pos, elementName); 
  					}
  			
  			case "java+parameter":
  					if(/^[a-z][a-zA-Z0-9]*$/ !:= elementName){ 
  						append namingConvention("ParameterName", pos, elementName); 
  					}
  				
  			case "java+variable":
  					if(/^[a-z][a-zA-Z0-9]*$/ !:= elementName){ 
  						str category = \final() in elemModifiers ? "LocalFinalVariableName" : "LocalVariableName";
  						append namingConvention(category, pos, elementName);
  					 }
  			
  			case "java+interface":	
  					if(/^[A-Z][a-zA-Z0-9]*$/ !:= elementName){ 
  						append namingConvention("TypeName", pos, elementName); 
  					}
  			
  			default:
  				throw "Cannot handle scheme <n>, <pos>";
  	}
  }
}