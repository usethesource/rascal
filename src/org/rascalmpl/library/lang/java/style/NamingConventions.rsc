module lang::java::style::NamingConventions

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
 
list[Message] namingConventions(Declaration d: \compilationUnit(Declaration package, _, _),  list[Declaration] parents, node ast, M3 model) {
	pname = getPackageName(package);
	elems = split(".", pname);
  	if( /^[a-z]+$/ !:= elems[0]  || size(elems) > 1 && any(elem <- elems[1..], /^[a-zA-Z_][a-zA-Z0-9_]*$/ !:= elem)){
  		return [namingConvention("PackageName", d@src, pname)]; 
  	}
  	return [];
}

list[Message] namingConventions(Declaration d: \class(str name, _, _, _),  list[Declaration] parents, node ast, M3 model) {
	elemModifiers = d@modifiers ? {};
	if(\abstract() in elemModifiers){
  		if(!(/^Abstract/ := name || /Factory$/ := name)){
  			return [namingConvention("AbstractClassName", d@src, name)];
  		}
  	} else
  		if(/^[A-Z][a-zA-Z0-9]*$/ !:= name){ return [namingConvention("TypeName", d@src, name)]; }
  	return [];
}
 
list[Message] namingConventions(Declaration d: \field(Type \type, list[Expression] fragments),  list[Declaration] parents, node ast, M3 model) {
 	msgs = [];
 	elemModifiers = d@modifiers ? {};
 	for(fragment <- fragments){
 		if(fragment has name){
 			name = fragment.name;
 			if({\static(), \final()} <=  elemModifiers){
  				elems = split(name, "_");
  				if( /^[A-Z][A-Z0-9]*$/ !:= elems[0] || size(elems) > 1 && any( elem <- elems[1..], /^[A-Z0-9]+$/ !:= elem)){
  					msgs += namingConvention("ConstantName", fragment@src, name);
  				}
  			} else	
  				if(/^[a-z][a-zA-Z0-9]*$/ !:= name){ 
  					str category = \static() in elemModifiers ? "StaticVariableName" :  "MemberName";
  					msgs += namingConvention(category, fragment@src, name); 
  				}
 		}
 	}
 	return msgs;
}

list[Message] namingConventions(Declaration d: \method(_, str name, _, _, _),  list[Declaration] parents, node ast, M3 model) {
	elemModifiers = d@modifiers ? {};
	if(/^[a-z][a-zA-Z0-9]*$/ !:= name && \static() notin elemModifiers){ 
		return [namingConvention("MethodName", d@src, name)]; 
	}
	return [];
}

// TODO merge with above
list[Message] namingConventions(Declaration d: \method(_, str name, _, _),  list[Declaration] parents, node ast, M3 model) {
	elemModifiers = d@modifiers ? {};
	if(/^[a-z][a-zA-Z0-9]*$/ !:= name && \static() notin elemModifiers){ 
		return [namingConvention("MethodName", d@src, name)]; 
	}
	return [];
}
   
list[Message] namingConventions(Declaration d: \parameter(_, str name, _),  list[Declaration] parents, node ast, M3 model) =
	(/^[a-z][a-zA-Z0-9]*$/ !:= name) ? [ namingConvention("ParameterName", d@src, name) ] : [];

list[Message] namingConventions(Declaration d: \typeParameter(str name, _),  list[Declaration] parents, node ast, M3 model){
	println("namingConventions: <d>");
	if(/^[A-Z]$/ !:= name){ 
  		// TODO: how do we distinguish these two cases?
  		str category = isClass(d@decl) ? "ClassTypeParameterName" : "MethodTypeParameterName";
  		return [ namingConvention(category, d@src, name) ]; 
  	}
  	return [];
}

list[Message] namingConventions(Declaration d: \enum(str name, _, _, _),  list[Declaration] parents, node ast, M3 model) =
	(/^[A-Z][a-zA-Z0-9]*$/ !:= name) ? [ namingConvention("TypeName", d@src, name) ] : [];

list[Message] namingConventions(Declaration d: \enumConstant(str name, _, _),  list[Declaration] parents, node ast, M3 model) =
  (/^[A-Z][a-zA-Z0-9]*$/ !:= name) ? [ namingConvention("TypeName", d@src, name) ] : [];
  
list[Message] namingConventions(Declaration d: \enumConstant(str name, _),  list[Declaration] parents, node ast, M3 model) =
  (/^[A-Z][a-zA-Z0-9]*$/ !:= name) ? [ namingConvention("TypeName", d@src, name) ] : [];
 
list[Message] namingConventions(Declaration d: \interface(str name, _, _, _),  list[Declaration] parents, node ast, M3 model) =
  (/^[A-Z][a-zA-Z0-9]*$/ !:= name) ? [ namingConvention("TypeName", d@src, name) ] : [];
   
list[Message] namingConventions(Declaration d: \variables(Type \type, list[Expression] \fragments),  list[Declaration] parents, node ast, M3 model) {
	elemModifiers = d@modifiers ? {};
	msgs = [];
	for(fragment <- fragments){
		if(fragment has name){
			name = fragment.name;
 			if(/^[a-z][a-zA-Z0-9]*$/ !:= name){ 
  				str category = \final() in elemModifiers ? "LocalFinalVariableName" : "LocalVariableName";
  				msgs += namingConvention(category, fragment@src, name);
  			 }
  		}
  	}
  	return msgs;
} 