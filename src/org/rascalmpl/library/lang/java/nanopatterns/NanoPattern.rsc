@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl (CWI)}
module lang::java::jdt::nanopatterns::NanoPattern

data AnalysisResult = analysisResult(list[MethodAndProfile] profiles); 
data MethodAndProfile = methodAndProfile(str classname, str methodname, str action, NanoPatternProfile profile);

data NanoPatternProfile = nanoPatternProfile(map[str patternName, bool present] patterns);
	
public str NO_PARAM			= "NoParam";
public str NO_RETURN 		= "NoReturn";
public str RECURSIVE		= "Recursive";
public str SAME_NAME 		= "SameName";
public str LEAF				= "Leaf";
public str OBJECT_CREATOR	= "ObjectCreator";
public str FIELD_READER 	= "FieldReader";
public str FIELD_WRITER		= "FieldWriter";
public str TYPE_MANIPULATOR	= "TypeManipulator";
public str STRAIGHT_LINE	= "StraightLine";
public str LOOPING			= "Looping";
public str EXCEPTIONS		= "Exceptions";
public str LOCAL_READER 	= "LocalReader";
public str LOCAL_WRITER		= "LocalWriter";
public str ARRAY_CREATOR	= "ArrayCreator";
public str ARRAY_READER		= "ArrayReader";
public str ARRAY_WRITER		= "ArrayWriter"; 
