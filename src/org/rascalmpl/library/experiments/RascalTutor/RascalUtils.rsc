@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

@bootstrapParser
module experiments::RascalTutor::RascalUtils

import experiments::RascalTutor::CourseModel;

import IO;
import String;
import List;
import lang::rascal::syntax::RascalRascal;
import Reflective;
import ParseTree;

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

str stripParameterization(str name){
  i = findFirst(name, "[");
  if(i < 0)
     return name;
  return substring(name, 0, i);
}

// Return the content of the doc tag in a list of tags.

private str getDoc(Tags tags){
	visit(tags){
     case Tag t: 
        if("<t.name>" == "doc") {
           return stripBraces("<t.contents>");
        }
   }
   return "";
}

// Remove <AUTOINSERTED> ... </AUTOINSERTED> from doc contents
public str removeAUTOINSERTED(str doc){
  if(/^<pre:.*>\<AUTOINSERTED\>.*\<\/AUTOINSERTED\>\s*<post:.*>$/s := doc)
     return "<post>";
  return doc;
}

private str autoinsert(str ins, str doc){
   return "\<AUTOINSERTED\>
          '<ins>
          '\</AUTOINSERTED\>
          '<doc>";
}

private str getModuleDoc(Header header){
  mname = stripParameterization("<header.name>");
  doc = getDoc(header.tags);
  ins = "Name: <mname>
        'Usage: `import <mname>;`";
         
  return autoinsert(ins, doc);
}

// Handling of function declarations

private bool isSimilarFunction(str functionName, Declaration decl){
  return decl is Function && 
        stripParameterization("<decl.functionDeclaration.signature.name>") == functionName && 
        "<decl.functionDeclaration.visibility>" == "public";
}

private str getFunctionSignature(FunctionDeclaration decl){
  return visit("<decl.signature>"){
           case /java\s*/ => ""
           case /\s*\n\s*/ => " "
         }
}

// Get doc for function declaration, autoinsert name and signature

private str getFunctionDoc(str mname, FunctionDeclaration fdecl, list[str] signatures){
  fname = stripParameterization("<fdecl.signature.name>");
  doc = getDoc(fdecl.tags);
  fsig = size(signatures) == 1 ? "`<signatures[0]>`" : "<for(s <- signatures){># `<s>`\n<}>";
  ins = "Name: <fname>
        'Function: 
        '<fsig>
        'Usage: `import <mname>;`";
         
  return autoinsert(ins, doc);
}

private bool isUndocumentedDataOrAlias(Declaration decl){
  return (decl is Data || decl is Alias) && getDoc(decl.tags) == "";
}

private str getDataOrAliasSignature(Declaration decl){
  println("getDataOrAliasSignature: <decl>");
  if(decl is Alias){
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

// Get doc for data declaration, autoinsert name and declaration
private str getDataDoc(str mname, Declaration decl, list[str] sigs){
    println("getDataDoc: <decl>, <sigs>");
	doc = getDoc(decl.tags);
	name = stripParameterization("<decl.user>");
	ins =  "Name: <name>
	       'Types: 
	       '\<listing\>
	       '<intercalate("\n", sigs)>
	       '\</listing\>
	       'Usage: `import <mname>;`";
	return autoinsert(ins, doc);
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
   functionName = stripParameterization("<fdecl.signature.name>");
   doc = "";
   key = "<libRoot>/<moduleName>/<functionName>";
   if(!contentMap[key]? && "<fdecl.visibility>" == "public"){
      println("extractRemoteConcepts: <functionName>");
      fsigs = [getFunctionSignature(fdecl)];
      while(current+1 < size(declarations) && isSimilarFunction(functionName, declarations[current+1])){
            fsigs += getFunctionSignature(declarations[current+1].functionDeclaration);
            current += 1;
      }
      doc = getFunctionDoc(moduleName, fdecl, fsigs);
      if(doc != "" && writing){  	
	     writeFile(courseDir + libRoot + moduleName + functionName + remoteLoc, fdecl@\loc);
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
  userType = stripParameterization("<decl.user>");
  println("userType = <userType>");
  key = "<libRoot>/<moduleName>/<userType>";
  doc = "";
  if(!contentMap[key]?){
     println("extractRemoteConcepts: <userType>");
     sigs = [getDataOrAliasSignature(decl)];
      while(current+1 < size(declarations) && isUndocumentedDataOrAlias(declarations[current+1])){
            sigs += getDataOrAliasSignature(declarations[current+1]);
            current += 1;
      }
     doc = getDataDoc(moduleName, decl, sigs);
     if(doc != "" && writing){  	
	    writeFile(courseDir + libRoot + moduleName + userType + remoteLoc, decl@\loc);
	    contentMap[key] = doc;
	 }
  }
  return <current + 1, doc>;
}

// Extract ALL relevant docs from a Rascal library file:
// - L: location of the library
// - root: the concept that will act as root for all concepts in this library.

public map[str,str] extractRemoteConcepts(loc L, str /*ConceptName*/ root){
  //println("extractRemoteConcepts: <L>, <root>");
  M = parseModule(readFile(L), L);
  //rprintln(M);
  moduleName = "";
  declarations = [];
  contentMap = ();
  libRoot = root;
  top-down visit(M){
    case Header header: {
       moduleName = stripParameterization("<header.name>"); 
       doc =  getModuleDoc(header);
       println("extractRemoteConcepts: <moduleName>");
     
       if(doc != ""){  		
   	      writeFile(courseDir + root + moduleName + remoteLoc,  header@\loc);
  	      contentMap["<root>/<moduleName>"] = doc;
       }
     }
     case Declaration d: { declarations += d; }
  }

  int i = 0;
  while(i < size(declarations)){
    Declaration decl = declarations[i];
    if(decl is Function){
       <i, doc> = extractFunctionDeclaration(i, true);
    } else if(decl is Data || decl is Alias){
      <i, doc> = extractDataOrAliasDeclaration(i, true);
    } else {
      i += 1;
  	}
  }
  return contentMap;
}

// ---- Functions for editing individual concepts in a library file ----

// Extract the contents of the doc string for itemName
// This function is used when editing a single doc entry in a library file.
// L: location of library file
// itemName: the name of the item for which documentation has to be extracted.
// Returns:  the documentation associated with itemName.

public str extractDoc(loc L, str itemName){
  L1 = L[offset=-1][length=-1][begin=<-1,-1>][end=<-1,-1>];
  //println("extractDoc: <L1>, <itemName>");
  M = parseModule(readFile(L1), L1);
  moduleName = "";
  declarations = [];
  contentMap = ();
  top-down visit(M){
		case Header header: { moduleName = stripParameterization("<header.name>"); if(moduleName == itemName) return getModuleDoc(header); }
		case Declaration d: { declarations += d; }
  }
  
  int i = 0;
  while(i < size(declarations)){
    Declaration decl = declarations[i];
    if(decl is Function){
       <i, doc> = extractFunctionDeclaration(i, false);
       if(stripParameterization("<decl.functionDeclaration.signature.name>") == itemName) return doc;
    } else if(decl is Data || decl is Alias){
      <i, doc> = extractDataOrAliasDeclaration(i, false);
      if(stripParameterization("<decl.user>") == itemName) return doc;
    } else {
      i += 1;
  	}
  }
  
  println("extractDoc: <L1>, <itemName>, returns empty");
  return "";
}

// Physical string replacement.

public str replace(str old, int begin, int length, str repl){
  before = substring(old, 0, begin);
  after = substring(old, begin+length);
  return "<before><repl><after>";
}

private bool replaceDoc(str itemName, Tags tags, str oldFileContent, str newDocContent, loc L){
	visit(tags){
     case Tag t: 
        if("<t.name>" == "doc") {
           	l = t@\loc;
            newFileContent = replace(oldFileContent, l.offset, l.length, "@doc{\n<newDocContent>\n}");
            //println("newFileContent = <newFileContent>");
            writeFile(L, newFileContent);
            return true;
        }
   }
   return false;
}

public bool replaceDoc(loc L, str itemName, str newDocContent){
  L1 = L[offset=-1][length=-1][begin=<-1,-1>][end=<-1,-1>];
  println("replaceDoc: <L1>, <itemName>, <newDocContent>");
  oldFileContent = readFile(L1);
  M = parseModule(oldFileContent, L1);
  newDocContent = removeAUTOINSERTED(newDocContent);
  top-down visit(M){
    case Header header: 
         if(stripParameterization("<header.name>") == itemName){
            return replaceDoc(itemName, header.tags, oldFileContent, newDocContent, L1);
         }
    case FunctionDeclaration decl: 
   		 if(stripParameterization("<decl.signature.name>") == itemName) {
   		 	return replaceDoc(itemName, decl.tags, oldFileContent, newDocContent, L1);
   		 }
    case Declaration d: 
   		 if((d is Data || d is Alias) && stripParameterization("<d.user>") == itemName) {
   		 	return replaceDoc(itemName, d.tags, oldFileContent, newDocContent, L1);
   		 }
  }
  return false;
}




