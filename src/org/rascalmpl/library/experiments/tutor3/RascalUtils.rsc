@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

@bootstrapParser
module experiments::tutor3::RascalUtils

//import CourseModel;
import Exception;

import IO;
import ValueIO;
import String;
import List;
import lang::rascal::\syntax::Rascal;
import util::Reflective;
import ParseTree;
import Node;

list[loc] libSearchPath = [|clib-rascal:///|, |clib-rascal-eclipse:///|];

//public loc courseDir    = |courses:///|;
public loc courseDir    =|file:///Users/paulklint/git/rascal/src/org/rascalmpl/courses|;
public str remoteLoc      = "remote-loc.value";

// A ConceptName is the "pathname" of a concept in the concept hierarchy, e.g., "Rascal/Datastructure/Set"

alias ConceptName = str;

// Get the basename from a ConceptName, eg 
// - basename("A/B/C") => "C"

public str basename(ConceptName cn){
  return (/^.*\/<base:[A-Za-z0-9\-\_]+>$/ := cn) ? base : cn;
}

// Rascal utilities
// ------------------ Extract and replace concepts from Rascal sources external to the Tutor ----------------

// Strip surrounding braces from doc tag content

str stripBraces(str content){
  b = findFirst(content, "{");
  b = (b >= 0) ? b + 1 : 0;
  e = findLast(content, "}");
  e = (e > 0) ? e - 1 : size(content);
  return substring(content, b, e);
}

str de_escape(str name){
  if(startsWith(name, "\\"))
     return substring(name, 1);
  return name;
}

str normalizeName(str name){
  name = de_escape(name);
  i = findFirst(name, "[");
  if(i > 0)
     name = substring(name, 0, i);
  return replaceAll(name, "::", "/");
}

str makeName(str name){
  return "# Name
         '<basename(name)>";
}

str makeUsage(str name){
  return "# Usage
         '`import <replaceAll(name, "/", "::")>;`";
}

// Return the content of the doc tag in a list of tags.

private str getDoc(Tags tags){
	top-down-break visit(tags){
     case Tag t: 
        if("<t.name>" == "doc") {
           return stripBraces("<t.contents>");
        }
   }
   return "";
}

private str getModuleDoc(Header header){
  mname = normalizeName("<header.name>");
  doc = getDoc(header.tags);
  return //"<makeName(mname)>
         "<makeUsage(mname)>
         '<doc>";
}

// Handling of function declarations

private bool isSimilarFunction(str functionName, Declaration decl){
  return decl is function && 
        normalizeName("<decl.functionDeclaration.signature.name>") == functionName && 
        "<decl.functionDeclaration.visibility>" == "public";
}

private str getFunctionSignature(FunctionDeclaration decl){
  return top-down-break visit("<decl.signature>"){
           case /java\s*/ => ""
           case /\s*\n\s*/ => " "
         }
}

// Get doc for function declaration, insert name and signature

private str getFunctionDoc(str mname, FunctionDeclaration fdecl, list[str] signatures){
  fname = normalizeName("<fdecl.signature.name>");
  doc = getDoc(fdecl.tags);
  if(doc == "")
     return "";
  fsig = size(signatures) == 1 ? "`<signatures[0]>`" : "<for(s <- signatures){># `<s>`\n<}>";
  return  //"<makeName(fname)>
        "# Function 
        '<fsig>
        '<makeUsage(mname)>
        '<doc>";
}

private bool isUndocumentedDataOrAlias(Declaration decl){
  return (decl is \data || decl is \alias) && getDoc(decl.tags) == "";
}

private str getDataOrAliasSignature(Declaration decl){
  //println("getDataOrAliasSignature: <decl>");
  if(decl is \alias){
   if(getDoc(decl.tags) == "")
     return "<decl>";
   return "alias <decl.user> = <decl.base>;";
  }

  if(getDoc(decl.tags) == "")
     return "<decl>";
  variants = "<decl.variants>";
  if(!contains(variants, "\n"))
     return "data <decl.user> = <variants>;";
  return "data <decl.user>\n     = <variants>
	                       '     ;";
}

// Get doc for data declaration, insert name and declaration
private str getDataDoc(str mname, Declaration decl, list[str] sigs){
    //println("getDataDoc: <decl>, <sigs>");
	doc = getDoc(decl.tags);
	nname = normalizeName("<decl.user>");
	return //"<makeName(nname)>
	       "Types: 
	       '```rascal
	       '<intercalate("\n", sigs)>
	       '```
	       '<makeUsage(mname)>
	       '<doc>";
}

private map[str,str] contentMap = ();
private str libRoot = "";
private str moduleName = "";
private list[Declaration] declarations = [];

// Extract a function declaration from a list of Declarations.
// Subsequent declarations for functions with the same name (without their own doc tag) are merged.
// current: index of current declaration in declarations.
// writing: should we write a remoteLoc file?
// Returns: <next, doc>:
//          next: the index of the declaration following the ones used
//          doc:  the generated documentation string.

private tuple[int,str] extractFunctionDeclaration(int current, bool writing){
   decl = declarations[current];
   fdecl = decl.functionDeclaration;
   functionName = normalizeName("<fdecl.signature.name>");
   doc = "";
   key = "<functionName>";
   //key = "<libRoot>/<moduleName>/<functionName>";
   if(!contentMap[key]? && "<fdecl.visibility>" == "public"){
      //println("extractFunctionDeclaration: <functionName>");
      fsigs = [getFunctionSignature(fdecl)];
      while(current+1 < size(declarations) && isSimilarFunction(functionName, declarations[current+1])){
            fsigs += getFunctionSignature(declarations[current+1].functionDeclaration);
            current += 1;
      }
      doc = getFunctionDoc(moduleName, fdecl, fsigs);
      if(doc != "" && writing){  	
	     //writeFile(courseDir + libRoot + moduleName + functionName + remoteLoc, fdecl@\loc);
		 contentMap[key] = doc;
	  }
   } 
   return <current + 1, doc>;
}

// Extract a data or alias declaration from a list of Declarations.
// Subsequent declarations for data with (without their own doc tag) are merged.
// current: index of current declaration in declarations.
// writing: should we write a remoteLoc file?
// Returns: <next, doc>:
//          next: the index of the declaration following the ones used
//          doc:  the generated documentation string.


private tuple[int,str] extractDataOrAliasDeclaration(int current, bool writing){
  decl = declarations[current];
  userType = normalizeName("<decl.user>");
  key = "<userType>";
  //key = "<libRoot>/<moduleName>/<userType>";
  doc = "";
  if(!contentMap[key]?){
     //println("extractDataOrAliasDeclaration: <userType>");
     sigs = [getDataOrAliasSignature(decl)];
      while(current+1 < size(declarations) && isUndocumentedDataOrAlias(declarations[current+1])){
            sigs += getDataOrAliasSignature(declarations[current+1]);
            current += 1;
      }
     doc = getDataDoc(moduleName, decl, sigs);
     if(doc != "" && writing){  	
	    //writeFile(courseDir + libRoot + moduleName + userType + remoteLoc, decl@\loc);
	    contentMap[key] = doc;
	 }
  }
  return <current + 1, doc>;
}

private str getAnnotationSignature(Declaration decl){
  //println("getAnnotationSignature: <decl>");
  if(getDoc(decl.tags) == "")
     return "<decl>";
  return "anno <decl.annoType> <decl.onType>@<decl.name>;";
}

// Get doc for annotation  declaration, insert name and declaration
private str getAnnotationDoc(str mname, Declaration decl, str sig){
    //println("getAnnotationDoc: <decl>, <sig>");
	doc = getDoc(decl.tags);
	nname = normalizeName("<decl.name>");
	return //"<makeName(nname)>
	       "Types: 
	       '```rascal
	       '<sig>
	       '```
	       '<makeUsage(mname)>
	       '<doc>";
}

// Extract an annotation declaration from a list of Declarations.
// current: index of current declaration in declarations.
// writing: should we write a remoteLoc file?
// Returns: <next, doc>:
//          next: the index of the declaration following the one used (always +1).
//          doc:  the generated documentation string.

private tuple[int,str] extractAnnotationDeclaration(int current, bool writing){
  decl = declarations[current];
  annoType = "<decl.annoType>";
  onType   = "<decl.onType>";
  nname     = de_escape("<decl.name>");
  //println("nname = <nname>");
  key = "<nname>";
  //key = "<libRoot>/<moduleName>/<nname>";
  doc = "";
  if(!contentMap[key]?){
     //println("extractAnnotationDeclaration: <name>");
     sig = getAnnotationSignature(decl);
     doc = getAnnotationDoc(moduleName, decl, sig);
     if(doc != "" && writing){  	
	    //writeFile(courseDir + libRoot + moduleName + nname + remoteLoc, decl@\loc);
	    contentMap[key] = doc;
	 }
  }
  return <current + 1, doc>;
}

// Extract ALL relevant docs from a Rascal library file:
// - L: location of the library
// - root: the concept that will act as root for all concepts in this library.

public map[str,str] extractRemoteConcepts(str Lstr, str /*ConceptName*/ root){
 
  loc L = readTextValueString(#loc, Lstr);
  L1 = L.top;
  //println("extractRemoteConcepts: <L>, <L1>, <root>");

  try {
    Module M = parseModuleWithSpaces(L1/*, libSearchPath*/).top;
  
 
  declarations = [];
  contentMap = ();
  libRoot = root;
  
  Header header = M.header;
  moduleName = normalizeName("<header.name>"); 
  doc =  getModuleDoc(header);
  //println("extractRemoteConcepts: <moduleName>: \'<doc>\'");
  if(doc != ""){  		
     //writeFile(courseDir + root + moduleName + remoteLoc,  header@\loc);
     contentMap["<moduleName>"] = doc;
      //contentMap["<root>/<moduleName>"] = doc;
  }
  
  declarations = [tl.declaration | Toplevel tl <- M.body.toplevels];
  //("<size(declarations)> declarations");
  int i = 0;
  while(i < size(declarations)){
    Declaration decl = declarations[i];
    if(decl is function){
       <i, doc> = extractFunctionDeclaration(i, true);
    } else if(decl is \data || decl is \alias){
      <i, doc> = extractDataOrAliasDeclaration(i, true);
    } else if(decl is \annotation){
      <i, doc> = extractAnnotationDeclaration(i, true);
    } else {
      i += 1;
  	}
  }
  return contentMap;

  }
  catch PathNotFound(loc l): {
    println("Referred module has disappeared: <L>, as referred to in <root>");
    return ();
  }
  println("AT END OF extractRemoteConcepts");
}

// ---- Functions for editing individual concepts in a library file ----

// Extract the contents of the doc string for itemName
// This function is used when editing a single doc entry in a library file.
// L: location of library file
// itemName: the name of the item for which documentation has to be extracted.
// Returns:  the documentation associated with itemName.

public str extractDoc(loc L, str itemName){
 // L1 = L[offset=-1][length=-1][begin=<-1,-1>][end=<-1,-1>];
  L1 = L.top;
  //println("extractDoc: <L1>, <itemName>");
  Module M = parseModuleWithSpaces(L1/*, libSearchPath*/).top;
  Header header = M.header;
  moduleName = basename(normalizeName("<header.name>")); 
  if(moduleName == itemName) 
  	 return getModuleDoc(header);

  declarations = [tl.declaration | Toplevel tl <- M.body.toplevels];
  contentMap = ();
  
  int i = 0;
  while(i < size(declarations)){
    Declaration decl = declarations[i];
    if(decl is function){
       <i, doc> = extractFunctionDeclaration(i, false);
       if(normalizeName("<decl.functionDeclaration.signature.name>") == itemName) return doc;
    } else if(decl is \data || decl is \alias){
      <i, doc> = extractDataOrAliasDeclaration(i, false);
      if(normalizeName("<decl.user>") == itemName) return doc;
    } else if(decl is \annotation){
      <i, doc> = extractAnnotationDeclaration(i, true);
      if(de_escape("<decl.name>") == itemName) return doc;
    } else {
      i += 1;
  	}
  }
  
  //println("extractDoc: <L1>, <itemName>, returns empty");
  return "";
}

value main(){
    concepts = extractRemoteConcepts("|std:///Boolean.rsc|", "Test/Library");
    for(cp <- concepts){
        println("<cp>:");
        println(concepts[cp]);
    }
    return true;
}

