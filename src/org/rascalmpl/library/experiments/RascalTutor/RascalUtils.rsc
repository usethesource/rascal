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

void writeDeclConcept(str moduleName, str itemName, loc itemLoc, str doc, str /*ConceptName*/ root){
  println("writeDeclConcept: <itemName>, <doc>");
  destination = courseDir + root + moduleName + itemName + "<itemName>.concept";
  writeFile(destination, doc);
  writeFile(courseDir + root + moduleName + itemName + remoteLoc, itemLoc);
}

void writeModuleConcept(str moduleName, loc moduleLoc, str doc, str /*ConceptName*/ root){
  println("writeModuleConcept: <moduleName>, <doc>");
  destination = courseDir + root + moduleName + "<moduleName>.concept";
  writeFile(destination, doc);
  writeFile(courseDir + root + moduleName + remoteLoc, moduleLoc);
}

public void extractRemoteConcepts(loc L, str /*ConceptName*/ root){
  println("extractRemoteConcepts: <L>, <root>");
  M = parseModule(readFile(L), L);
  moduleName = "";
  top-down visit(M){
    case Header header:	{ moduleName = "<header.name>"; 
                          writeModuleConcept(moduleName, header@\loc, getDoc(header.tags), root);
   		 				}
    case FunctionDeclaration decl: 
    					{ functionName = "<decl.signature.name>";
           				  writeDeclConcept(moduleName, functionName, decl@\loc, getDoc(decl.tags), root);
           				}
  }
}

// Extract the contents of the doc string for itemName

public str extractDoc(loc L, str itemName){
  L1 = L[offset=-1][length=-1][begin=<-1,-1>][end=<-1,-1>];
  println("extractDoc: <L1>, <itemName>");
  M = parseModule(readFile(L1), L1);
  top-down visit(M){
    case Header header: if("<header.name>" == itemName) return getDoc(header.tags);
    case FunctionDeclaration decl: if("<decl.signature.name>" == itemName) return getDoc(decl.tags);
  }
  println("extractDoc: <L1>, <itemName>, return empty");
  return "";
}

public str replace(str old, int begin, int length, str repl){
  before = substring(old, 0, begin);
  after = substring(old, begin+length);
  return "<before><repl><after>";
}

private void replaceDoc(str itemName, Tags tags, str oldFileContent, str newDocContent, loc L){
	visit(tags){
     case Tag t: 
        if("<t.name>" == "doc") {
           	l = t@\loc;
            newFileContent = replace(oldFileContent, l.offset, l.length, "@doc{\n<newDocContent>\n}");
            println("newFileContent = <newFileContent>");
            writeFile(L, newFileContent);
            return;
        }
   }
   throw "Could not replace doc content for <itemName> in <L>";
}


public void replaceDoc(loc L, str itemName, str newDocContent){
  L1 = L[offset=-1][length=-1][begin=<-1,-1>][end=<-1,-1>];
  println("replaceDoc: <L1>, <itemName>, <newDocContent>");
  oldFileContent = readFile(L1);
  M = parseModule(oldFileContent, L1);
  top-down visit(M){
    case Header header: 
         if("<header.name>" == itemName){
            replaceDoc(itemName, header.tags, oldFileContent, newDocContent, L1);
            return;
         }
    case FunctionDeclaration decl: 
   		 if("<decl.signature.name>" == itemName) {
   		 	replaceDoc(itemName, decl.tags, oldFileContent, newDocContent, L1);
   		 	return;
   		 }
  }
  throw "Could not replace doc content for <itemName> in <L>";
}
