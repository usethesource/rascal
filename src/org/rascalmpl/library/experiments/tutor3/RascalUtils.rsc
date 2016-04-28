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
import Map;
import Set;
import lang::rascal::\syntax::Rascal;
import util::Reflective;
import ParseTree;
import Node;

list[loc] libSearchPath = [|clib-rascal:///|, |clib-rascal-eclipse:///|];

//public loc courseDir    = |courses:///|;
public loc courseDir    =|file:///Users/paulklint/git/rascal/src/org/rascalmpl/courses|;

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
  e = (e > 0) ? e  : size(content);
  return substring(content, b, e) + "\n";
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
  return "# <basename(name)>";
}

str makeUsage(str name){
  return ".Usage
         '`import <replaceAll(name, "/", "::")>;`
         '";
}

str getSynopsis(str txt){
    if(/.*\.Synopsis\n<synopsis:[^\n]*\n>/m := txt){
       return synopsis;
    }
    return "";
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

private str getModuleDoc(str parent, Header header){
  mname = normalizeName("<header.name>");
  doc = getDoc(header.tags);
  return "
         '
         '[[<basename(parent)>-<basename(mname)>]]
         '<makeName(mname)>
         '<makeUsage(mname)>
         '
         '<doc>
         '";
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
  fsig = size(signatures) == 1 ? "`<signatures[0]>`" : "<for(s <- signatures){>* `<s>`\n<}>";
  res = "
        '[[<basename(mname)>-<fname>]]
        '## <basename(fname)>
        '
        '.Function 
        '<fsig>
        '
        '<doc>
        '";
   println("getFunctionDoc:");
   println(res);
   return res;
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

  if(getDoc(decl.tags) == ""){
     println("getDataOrAliasSignature, empty doc: @@<decl>@@");
     return "<decl>";
  }
  //variants = "<decl.variants>";
  //if(!contains(variants, "\n"))
  //   return "data <decl.user> = <decl.variants>;";
  
  res= "data <decl.user> 
       '     = <decl.variants>
	   '";

  emptyTags = (Tags) ``;
  decl.tags = emptyTags;
  println("getDataOrAliasSignature: @@<decl>@@");
  return "<decl>";
}

// Get doc for data declaration, insert name and declaration
private str getDataDoc(str mname, Declaration decl, list[str] sigs){
    //println("getDataDoc: <decl>, <sigs>");
	doc = getDoc(decl.tags);
	nname = normalizeName("<decl.user>");
	println("sigs = @@<sigs>@@");
    dsig = size(sigs) == 1 ? "<sigs[0]>" : 
	                         "<for(s <- sigs){><s>\n<}>";
	
	return "
	       '[[<basename(mname)>-<nname>]]
	       '## <basename(nname)>
	       '.Types
	       '[source,rascal]
	       '----
	       '"
	       + dsig
	       + "\n----\n"
	       + doc;
}

private map[str,str] contentMap = ();
//private str libRoot = "";
private str moduleName = "";
private list[Declaration] declarations = [];

// Extract a function declaration from a list of Declarations.
// Subsequent declarations for functions with the same name (without their own doc tag) are merged.
// current: index of current declaration in declarations.
// Returns: <next, doc>:
//          next: the index of the declaration following the ones used
//          doc:  the generated documentation string.

private tuple[int,str] extractFunctionDeclaration(int current){
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
      if(doc != ""){  	
		 contentMap[key] = doc;
	  }
   } 
   return <current + 1, doc>;
}

// Extract a data or alias declaration from a list of Declarations.
// Subsequent declarations for data with (without their own doc tag) are merged.
// current: index of current declaration in declarations.
// Returns: <next, doc>:
//          next: the index of the declaration following the ones used
//          doc:  the generated documentation string.


private tuple[int,str] extractDataOrAliasDeclaration(int current){
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
     if(doc != ""){  	
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
	return "
	       '[[<basename(mname)>-<nname>]]
	       '##<makeName(nname)>
	       '.Types
	       '[source,rascal]
	       '----
	       '<sig>
	       '----\n"
	       + doc
	       ;
}

// Extract an annotation declaration from a list of Declarations.
// current: index of current declaration in declarations.
// Returns: <next, doc>:
//          next: the index of the declaration following the one used (always +1).
//          doc:  the generated documentation string.

private tuple[int,str] extractAnnotationDeclaration(int current){
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
     if(doc != ""){  	
	    contentMap[key] = doc;
	 }
  }
  return <current + 1, doc>;
}

// Extract ALL relevant docs from a Rascal library file:
// - L: location of the library
// - root: the concept that will act as root for all concepts in this library.

public str extractRemoteConcepts(str parent, str Lstr){
 
  loc L = readTextValueString(#loc, Lstr);
  L1 = L.top;
  //println("extractRemoteConcepts: <L>, <L1>");

  try {
    Module M = parseModuleWithSpaces(L1/*, libSearchPath*/).top;
  
 
    declarations = [];
    contentMap = ();
 //   libRoot = root;
  
    Header header = M.header;
    moduleName = normalizeName("<header.name>"); 
    moduleNameAsString = "<moduleName>";
    doc =  getModuleDoc(parent, header);
    println("extractRemoteConcepts: <moduleName>: \'<doc>\'");
    if(doc != ""){  		
       contentMap[moduleNameAsString] = doc;
    }
  
    declarations = [tl.declaration | Toplevel tl <- M.body.toplevels];
    //println("<size(declarations)> declarations");
    int i = 0;
    while(i < size(declarations)){
      Declaration decl = declarations[i];
      if(decl is function){
         <i, doc> = extractFunctionDeclaration(i);
      } else if(decl is \data || decl is \alias){
        <i, doc> = extractDataOrAliasDeclaration(i);
      } else if(decl is \annotation){
        <i, doc> = extractAnnotationDeclaration(i);
      } else {
        i += 1;
  	  }
    }
    sorted_keys = sort(toList(domain(contentMap)));
    result = contentMap[moduleNameAsString];
    for(key <- sorted_keys){
        if(key != moduleNameAsString){
           txt = contentMap[key];
           synopsis = getSynopsis(txt);
           result += "* \<\<<basename(moduleNameAsString)>-<key>,<key>\>\>: <synopsis>\n";
        }
     }
     
    println("result = <result>");
    println("sorted_keys = <sorted_keys>");
    for(key <- sorted_keys){
        println("<key>:");
        println(contentMap[key]);
        if(key != moduleNameAsString){
           result += contentMap[key];
        }
    }
    return result;
  } catch PathNotFound(loc l): {
    println("Referred module has disappeared: <L>");
    return "";
  }
  println("AT END OF extractRemoteConcepts");
}

value main(){
   println(extractRemoteConcepts("PARENT", "|std:///ParseTree.rsc|"));
    return true;
}

