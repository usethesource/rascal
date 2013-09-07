@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl (CWI)}
module lang::java::jdt::nanopatterns::NanoPatternAnalyzer

import lang::java::jdt::Java;
import lang::java::jdt::JavaADT;
import lang::java::jdt::nanopatterns::NanoPattern;

import IO;
import ToString;
import Node;

@doc{Construct a nano pattern profile for the given method}
public NanoPatternProfile analyse(AstNode method, list[Entity] fieldsInClass) {
	list[str] classFieldnames = getClassFieldnames(fieldsInClass);
	list[str] methodVariablenames = getMethodVariablenames(method);

	method = removeAnonymousClassDeclaration(method);

	map[str, bool] patterns = ();
	patterns +=	(NO_PARAM			:hasNoParams(method));
	patterns +=	(NO_RETURN			:hasNoReturn(method));
	patterns +=	(RECURSIVE			:isRecursive(method));
	patterns +=	(SAME_NAME			:callsMethodWithSameName(method));
	patterns +=	(LEAF				:isLeaf(method));
	patterns +=	(OBJECT_CREATOR		:isObjectCreator(method));
	patterns +=	(FIELD_READER		:isFieldReader(method, classFieldnames, methodVariablenames));
	patterns +=	(FIELD_WRITER		:isFieldWriter(method, classFieldnames, methodVariablenames));
	patterns +=	(TYPE_MANIPULATOR	:isTypeManipulator(method));
	patterns +=	(STRAIGHT_LINE		:isStraightLine(method));
	patterns +=	(LOOPING			:containsLoop(method));
	patterns +=	(EXCEPTIONS			:throwsException(method));
	patterns +=	(LOCAL_READER		:isLocalReader(method, methodVariablenames));
	patterns +=	(LOCAL_WRITER		:isLocalWriter(method, methodVariablenames));
	patterns +=	(ARRAY_CREATOR		:isArrayCreator(method));
	patterns +=	(ARRAY_READER		:isArrayReader(method));
	patterns +=	(ARRAY_WRITER		:isArrayWriter(method));
	
	return nanoPatternProfile(patterns);
}

private AstNode removeAnonymousClassDeclaration(AstNode method) {
	return visit(method) {
		case anonymousClassDeclaration(_) => anonymousClassDeclaration([]) 
	} 
}

@doc{A Method has no params when it has an empty parameter list in the methodDeclaration}
public bool hasNoParams(AstNode method) {
	bool noParams = methodDeclaration(_,_,_,_,[],_,_) := method;
	return noParams;
}

@doc{A Method has no return when it returns the primitive 'void()' type or none}
public bool hasNoReturn(AstNode method) {
	bool returnsVoid = methodDeclaration(_,_,some(primitiveType(\void())),_,_,_,_) := method;
	bool hasNoReturn = methodDeclaration(_,_,none(),_,_,_,_) := method;
	
	return returnsVoid || hasNoReturn;
}

@doc{A Method is recursive when it calls a method with the same name and signature on this object instance. 
	The expression in the methodInvocation should either be empty or contain the 'thisExpression'}
public bool isRecursive(AstNode method) {
	str methodname = getMethodname(method);
	list[Entity] paramTypes = getTypesOfParameters(method);
	
	for (/methodInvocation(expression:_,_,methodname,arguments:_) := method) {
		if ((none() == expression) || (/thisExpression(_) := expression)) {
			list[Entity] argumentTypes = [argument@javaType | argument <- arguments]; 

			if (paramTypes == argumentTypes) {
				return true;
			}
		}
	} 
	
	return false;
}

@doc {A Method calls a method with same name if the called method has the same name but a different signature (not recursive) or it calls its super}
public bool callsMethodWithSameName(AstNode method) {
	str methodname = getMethodname(method);
	list[Entity] paramTypes = getTypesOfParameters(method);

	for (/methodInvocation(possibleExpression:_,_,methodname,arguments:_) := method) {
		if (some(_) := possibleExpression) {
			return true;
		}
		
		list[Entity] argumentTypes = [argument@javaType | argument <- arguments]; 

		if (paramTypes != argumentTypes) {
			return true;
		}					
	}	
	
	if (/superMethodInvocation(_,_,methodname,_) := method) {
		return true;
	}	
	
	return false;
}

@doc{A Method is a leaf when it doesn't make any method invocations}
private bool isLeaf(AstNode method) {
	bool callsMethods = /methodInvocation(_,_,_,_) := method;
	bool callsSuperMethod = /superMethodInvocation(_,_,_,_) := method;
	
	bool callsConstructor = /constructorInvocation(_,_) := method;
	bool callsSuperConstructor = /superConstructorInvocation(_,_,_) := method;
	
	return !(callsMethods || callsSuperMethod || callsConstructor || callsSuperConstructor);
}

// Object Orientation

@doc{A Method is an object creator when it instantiates a new object} 
private bool isObjectCreator(AstNode method) {
	bool objectCreator = /classInstanceCreation(_,_,_,_,_) := method;
	
	return objectCreator;
}

@doc{A Method is a field reader when it reads an attribute on an (this or other) object}
private bool isFieldReader(AstNode method, list[str] classFieldnames, list[str] methodVariablenames) {
	// re-write the tree, remove all assignments, only keep right-hand side
	alteredMethod = visit(method) {
		case assignment(_,right:_) => right 
	} 
	
	visit(alteredMethod) {
		case fieldAccess(_,_) : return true; // reads field on other or this (explicit this) object
		case simpleName(name:_) : {
			// Watch out for shadowing, method variables have precedence over class attributes
			if ((name in classFieldnames) && (name notin methodVariablenames)) {
				return true; // read field on this object (implicit this)
			}
		}
	}
	
	return false;
}

@doc{A Method is a field writer when it writes an attribute on an (this or other) object}
private bool isFieldWriter(AstNode method, list[str] classFieldnames, list[str] methodVariablenames) {
	for (/assignment(left:_,_) := method) {
		if (/fieldAccess(_,_) := left) {
			return true;
		} 
		else {
			for (/simpleName(name:_) := left) {
				// Whatch out for shadowing, method variables have precedence over class attributes
				if ((name in classFieldnames) && (name notin methodVariablenames)) {
					return true;
				}
			} 
		}
	}
	
	return false;
}

@doc{A Method is a type manipulator when it contains casts or instanceof comparisons}
private bool isTypeManipulator(AstNode method) {
	bool containsCast = /castExpression(_,_) := method;
	bool containsTypeComparison = /instanceofExpression(_,_) := method;
	
	return containsCast || containsTypeComparison;
}

@doc{A Method is a straigt line when it contains no branches}
private bool isStraightLine(AstNode method) {
	bool containsIf = /ifStatement(_,_,_) := method;
	bool containsSwitch = /switchStatement(_,_) := method;
	bool containsCondition = /conditionalExpression(_,_,_) := method;
	bool containsCatch = /catchClause(_,_) := method;
	
	return !(containsIf || containsSwitch || containsCondition || containsCatch);
}

@doc{A Method contains a loop when it contains a for, enhanced for, while or do statement}
private bool containsLoop(AstNode method) {
	bool containsFor = /forStatement(_,_,_,_) := method;
	bool containsEnhancedFor = /enhancedForStatement(_,_,_) := method;
	bool containsWhile = /whileStatement(_,_) := method;
	bool containsDo = /doStatement(_,_) := method;
	
	return containsFor || containsEnhancedFor || containsWhile || containsDo;
}

@doc{A Method throws Exceptions if they are declared in the signature} 
private bool throwsException(AstNode method) {
	bool doesntThrow = methodDeclaration(_,_,_,_,_,[],_) := method;
	
	return !doesntThrow;
}

@doc{A Method is a local reader when it reads a method variable. Can be a) A method parameter variable or B) A variable created in the method}
private bool isLocalReader(AstNode method, list[str] methodVariablenames) {
	// Re-write tree, remove all assignments, only keep right-hand side
	alteredMethod = visit(method) {
		case assigment(_,right:_) => right
	}

	for(/simpleName(name:_) := alteredMethod) {
		if (name in methodVariablenames) {
			return true;
		}
	}
	
	return false;
}

@doc{A Method is a local writer when it writes to a method variable or if it declares and instantiates new local method variable}
private bool isLocalWriter(AstNode method, list[str] methodVariablenames) {
	for (/assignment(left:_,_) := method) {
		for (/simpleName(name:_) := left) {
			if (name in methodVariablenames) {
				return true;
			} 
		}
	}
	
	if (/variableDeclarationFragment(_,some(_)) := method) {
		return true; 
	} 
	
	return false;
}

@doc{A Method is an array creator if it contains a array creation statement}
private bool isArrayCreator(AstNode method) {
	bool createsArray = /arrayCreation(_,_,_) := method;
	return createsArray;
}

@doc{A Method is an array reader when an array is accessed or if the collection in an enhanced for statements is of an array type}
private bool isArrayReader(AstNode method) {
	// Re-write method ast tree. Remove all assigments, only keep right-hand side
	alteredMethod = visit(method) {
		case assignment(_,right:_) => right
	}
	
	visit(alteredMethod) {
		case arrayAccess(_,_) : return true; 
		case enhancedForStatement(_,collection:_,_) : {
			if ((collection@javaType) ?) {
				if (/array(_) := (collection@javaType)) {
					return true;
				}
			}
		}
	} 
	
	return false;
}

@doc{A Method is an array writer if an array gets initialized or if it is accessed in the left-hand side of an assignment} 
private bool isArrayWriter(AstNode method) {
	if (/arrayInitializer(_) := method) {
		return true;
	}
	
	for (/assignment(left:_,_) := method) {
		if (arrayAccess(_,_) := left) {
			return true;
		}
	}
	
	return false;
}

@doc{Display the given nano pattern profile as text}
public str asString(NanoPatternProfile profile) {
	return toString([patternName | patternName <- profile.patterns, profile.patterns[patternName]]);
}

private str getMethodname(AstNode method) {
	str methodname = "";
	
	if (methodDeclaration(_,_,_,name:_,_,_,_) := method) {
		methodname = name;
	}
	
	return methodname;
}

private list[Entity] getTypesOfParameters(AstNode method) {
	list[Entity] types = [];
	
	if (methodDeclaration(_,_,_,_,params:_,_,_) := method) {
		for (param <- params) {
			
			if (singleVariableDeclaration(_,_,paramType:_,_,_) := param) {
				if ((paramType@javaType) ?) {
					types += paramType@javaType;
				}
			}  
		}
	}
	
	return types;
}

private list[str] getClassFieldnames(list[Entity] fieldsInClass) {
	return [fieldname | current <- fieldsInClass, /field(fieldname:_) := current];
}

private list[str] getMethodVariablenames(AstNode method) {
	list[str] variableNames = [];
	
	// 1: Get all the method parameternames
	if (methodDeclaration(_,_,_,_,params:_,_,_) := method) {
		for (param <- params) {		
			if (singleVariableDeclaration(name:_,_,_,_,_) := param) {
				variableNames += name;				
			}
		}
	}
	
	// 2: Find all the locally created variables
	for(/variableDeclarationFragment(name:_,_) := method) {
		variableNames += name;
	}
	
	return variableNames;
}

private AstNode getMethodImplementation(AstNode method) {
	if (methodDeclaration(_,_,_,_,_,_,some(implementation:_)) := method) {
		return implementation;
	}
	
	return nullLiteral();	
}
