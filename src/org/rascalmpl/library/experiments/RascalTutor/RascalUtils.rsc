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

// Handling of function declarations

// Get all function signatures for a given function name

public list[str] getFunctionSignatures(Tree M, str name){
  signatures = [];
  top-down-break visit(M){
		case FunctionDeclaration decl: if("<decl.signature.name>" == name) signatures +=  getFunctionSignature(decl);
  }
  return signatures;
}

private str getFunctionSignature(FunctionDeclaration decl){
  return visit("<decl.signature>"){
           case /java\s*/ => ""
           case /\s*\n\s*/ => " "
         }
}

// Get doc for function declaration, autoinsert name and signature

private str getFunctionDoc(Tree m, FunctionDeclaration decl){
  doc = getDoc(decl.tags);
  name = "<decl.signature.name>";
  signatures = getFunctionSignatures(m, name);
  fsig = size(signatures) == 1 ? "`<signatures[0]>`" : "<for(s <- signatures){># `<s>`\n<}>";
  ins = "Name: <name>
        'Function: 
        '<fsig>";
         
  return autoinsert(ins, doc);
}

// Get doc for data declaration, autoinsert name and declaration
private str getDataDoc(Declaration decl){
	doc = getDoc(decl.tags);
	name = "<decl.user>";
	ins =  "Name: <name>
	       'Types: 
	       '\<listing\>
	       'data <name> =\n       <decl.variants>;
	       '\</listing\>";
	return autoinsert(ins, doc);
}

public map[str,str] extractRemoteConcepts(loc L, str /*ConceptName*/ root){
  //println("extractRemoteConcepts: <L>, <root>");
  M = parseModule(readFile(L), L);
  moduleName = "";
  contentMap = ();
  top-down visit(M){
    case Header header:	{ moduleName = "<header.name>"; 
                          println("extractRemoteConcepts: <moduleName>");
                          doc =  getDoc(header.tags);
                          if(doc != ""){  		
  							writeFile(courseDir + root + moduleName + remoteLoc,  header@\loc);
  							contentMap["<root>/<moduleName>"] = doc;
  						  }
   		 				}
    case FunctionDeclaration decl: 
    					{ functionName = "<decl.signature.name>";
    					  key = "<root>/<moduleName>/<functionName>";
    					  if(!contentMap[key]? && "<decl.visibility>" == "public"){
           				     println("extractRemoteConcepts: <functionName>");
           				     doc = getFunctionDoc(M, decl);
           				     if(doc != ""){  	
  							    writeFile(courseDir + root + moduleName + functionName + remoteLoc, decl@\loc);
  							    contentMap[key] = doc;
  						     } 
  						  }
           				}
    case Declaration decl: 
                        { if(decl.user?){
                             userType = "<decl.user>";
                             key = "<root>/<moduleName>/<userType>";
                             if(!contentMap[key]?){
                                println("extractRemoteConcepts: <userType>");
                                doc = getDataDoc(decl);
           				        if(doc != ""){  	
  							       writeFile(courseDir + root + moduleName + userType + remoteLoc, decl@\loc);
  							       contentMap[key] = doc;
  						        }
  						     }
                          }
    					}
  	}
  	return contentMap;
}

// Extract the contents of the doc string for itemName

public str extractDoc(loc L, str itemName){
  L1 = L[offset=-1][length=-1][begin=<-1,-1>][end=<-1,-1>];
  //println("extractDoc: <L1>, <itemName>");
  M = parseModule(readFile(L1), L1);
  top-down visit(M){
		case Header header: if("<header.name>" == itemName) return getDoc(header.tags);
		case FunctionDeclaration decl: if("<decl.signature.name>" == itemName) return getFunctionDoc(M, decl);
		case Declaration d: if(d.user? && "<d.user>" == itemName) return getDataDoc(d);
  }
  println("extractDoc: <L1>, <itemName>, return empty");
  return "";
}



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
         if("<header.name>" == itemName){
            return replaceDoc(itemName, header.tags, oldFileContent, newDocContent, L1);
         }
    case FunctionDeclaration decl: 
   		 if("<decl.signature.name>" == itemName) {
   		 	return replaceDoc(itemName, decl.tags, oldFileContent, newDocContent, L1);
   		 }
    case Declaration d: 
   		 if(d.user? && "<d.user>" == itemName) {
   		 	return replaceDoc(itemName, d.tags, oldFileContent, newDocContent, L1);
   		 }
  }
  return false;
}




