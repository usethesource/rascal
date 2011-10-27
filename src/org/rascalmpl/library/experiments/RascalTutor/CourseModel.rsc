@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::RascalTutor::CourseModel

import Graph;
import List;
import IO;
import ValueIO;
import String;
import Exception;

public loc courseDir    = |std:///experiments/RascalTutor/Courses/|;
public loc courseDirSVN = |std:///experiments/RascalTutor/Courses/.svn|;

// A ConceptName is the "pathname" of a concept in the concept hierarchy, e.g., "Rascal/Datastructure/Set"

alias ConceptName = str;

// A QuestionName is similar to a ConceptName, but extended with a suffix, e.g., "Rascal/Datastructure/Set.1"
alias QuestionName = str;

alias Questions = list[Question];

// A Course captures all the information for the run-time execution of a course by the Tutor.

data Course = 
     course(
			ConceptName root,                         // Name of the root concept
			list[str] warnings,                       // List of course compiler warnings
			map[ConceptName,Concept] concepts,        // Mapping ConceptNames to their description
			//rel[ConceptName,ConceptName] refinements, // Tree structure of concept refinements
			list[str]  baseConcepts                  // List of baseConcepts (e.g. names that occur on path of
			                                          // of some ConceptName)
//			map[str,ConceptName] related              // Mapping abbreviated concept names to full ConceptNames
     );

data Concept = 
	 concept(ConceptName fullName,                  // Full name of the concept
			loc file,                             	// Its source file
			list[str] warnings,                     // Explicit warnings in concept text
			list[ConceptName] details,              // Optional (ordered!) list of details
			//str html_synopsis,                      // HTML for synopsis sections
//			set[ConceptName] related,            	// Set of related concepts (abbreviated ConceptNames)
			set[str] searchTerms,    				// Set of search terms
			Questions questions                 	// List of Questions 
	);
        		
data Question = choiceQuestion(ConceptName fullName, QuestionName name, str descr, list[Choice] choices)
              | textQuestion(ConceptName fullName, QuestionName name, str descr, set[str] replies)
              | tvQuestion(ConceptName fullName, QuestionName name, TVkind kind, TVdetails details)
              
 // Some future possibilities:
              | commandQuestion(QuestionName name, str descr, list[str] setup, str expr, str validate)
              | funQuestion(QuestionName name, str descr, str fname, RascalType resultType, list[RascalType] argTypes, str reference)
              | moduleQuestion(QuestionName name, str descr, str mname, str fname, RascalType resultType, list[RascalType] argTypes, str reference)
			  ;
data TVdetails = details(str descr,         // Optional descriptive text
                         list[str] setup,   // setup code
                         str lstBefore,     //  listing before hole
                         str lstAfter,      // listing after hole
                         str cndBefore,     // condition before hole
                         str cndAfter,      // condition after hole
                         bool holeInLst,     // is there a hole in the listing?
                         bool holeInCnd,    // is there a hole included in the condition?
                         list[tuple[str,RascalType]] vars, 
                         list[tuple[str,str]] auxVars, 
                         RascalType rtype,
                         str hint)
              ;
data TVkind   = valueOfExpr()
              | typeOfExpr()
              ;
// TODO:
// - labels in tuples and relations are not yet handled

data RascalType =
       \bool()
     | \int(int from, int to)
     | \real(int from, int to)
     | \num(int from, int to)
     | \str()
     | \loc()
     | \datetime()
     | \list(RascalType tp)
     | \set(RascalType tp)
     | \map(RascalType key, RascalType val)
     | \tuple(list[RascalType] tps)
     | \rel(list[RascalType] tps)
     | \value()
     | \void()
     | \arb(int depth, list[RascalType] tps)	// arbitrary type of max depth and preference for leaf types
     | \same(str name)             				// a previously generated type
     ;

data Choice = good(str description)
            | bad(str description)
            ;
            
data Exception = ConceptError(str cause);
            
alias VarEnv = map[str, tuple[RascalType rtype, str rval]];
            
// Common utilities

public str conceptExtension = "concept";
public str htmlExtension = "html";
public str questExtension = "quest";

public str getFullConceptName(loc l){
   if (/^.*Courses\/<name:.*$>/ := l.parent)  
     return name;
   throw "Concept not rooted in course path? <l> not in <courseDir.path>?";
}

// Get the basename from a ConceptName, eg 
// - basename("A/B/C") => "C"

public str basename(ConceptName cn){
  return (/^.*\/<base:[A-Za-z0-9\-\_]+>$/ := cn) ? base : cn;
}

// Get the parentname from a ConceptName, eg 
// - basename("A/B/C") => "A/B"

public str parentname(ConceptName cn){
  return (/<parent:^.*>\/<base:[A-Za-z0-9\-\_]+>$/ := cn) ? parent : cn;
}

//test basename("A/B/C") == "C";

// Get the root name from a ConeptName, e.g.
// - rootname("A/B/C") => "A"

public str rootname(ConceptName cn){
  return (/<root:[A-Za-z0-9\-\_]+>.*$/ := cn) ? root : cn;
}

public list[str] getPathNames(str path){
  return [ name | /<name:[A-Za-z]+>(\/|$)/ := path ];
}

// Get all the names in a ConceptName

public list[str] basenames(ConceptName cn){
  names = [base | /<base:[A-Za-z0-9\-\_]+>/ := cn];
  nn = size(names);
  // remove duplication due to main concept in directory e.g. C/C.concept
  if(nn >= 2 && names[nn-1] == names[nn-2])
     	names = head(names, nn-1);
  return names;
}

//test basenames("A") == ["A"];
//test basenames("A/B/C") == ["A", "B", "C"];

// Compose a sublist of a list of names to a ConceptName
public str compose(list[str] names, int from, int to){
   str res = "";
   for(int i <- [from .. to])
   	res += (res == "") ? names[i] : ("/" + names[i]);
   return res;
}

public str compose(list[str] names){
  return compose(names, 0, size(names)-1);
}

public loc catenate(loc basedir, str entry){
   baseuri = basedir.uri;
   if(!endsWith(baseuri, "/"))
   	baseuri += "/";
   return basedir[uri=baseuri + entry];
}

public loc conceptFile(str cn){
  return catenate(courseDir, cn + "/" + basename(cn) + ".concept");
}


public str mkConceptTemplate(ConceptName cn){
return "Name: <cn>
       '
       'Synopsis:
       '
       'Syntax:
       '
       'Types:
       '
       'Function:
       '
       'Details:
       '
       'Description:
       '
       'Examples:
       '
       'Benefits:
       '
       'Pitfalls:
       '
       'Questions:
       '
       ";
}

// Get a section from the concept description. Each starts with a capitalized keyword,e,g, "Description".
// Questions is the last section and is treated special: it contains questions that are analyzed later

public list[str] sectionKeywords = ["Name",  "Synopsis", "Syntax", "Types", "Function", "Details", "Description",
                                   "Examples", "Benefits", "Pitfalls", "Questions"];

public str logo = "\<img id=\"leftIcon\" height=\"40\" width=\"40\" src=\"/Courses/images/rascal-tutor-small.png\"\>";

public map[str,list[str]] getSections(list[str] script){
  sections = ();
  StartLine = 0;
  currentSection = "";
  for(int i <- index(script)){
    if(/^<section:[A-Z][A-Za-z]*>:\s*<text:.*>/ := script[i] && section in sectionKeywords){
      if(currentSection != ""){
      	sections[currentSection] = trimLines(script, StartLine, i);
      	//println("<currentSection> = <sections[currentSection]>");
      }
      if(/^\s*$/ := text)
         StartLine = i + 1;       // no info following section header
      else {
        script[i] = text;    // remove the section header
        StartLine = i;
      } 
      currentSection = section;
    }
  }
  if(currentSection != ""){
     sections[currentSection] = trimLines(script, StartLine, size(script));
  }
  return sections;
}

public list[str] trimLines(list[str] lines, int StartLine, int end){
  //println("trimlines(<size(lines)>,StartLine=<StartLine>,end=<end>");
  while(StartLine < end && /^\s*$/ := lines[StartLine])
    StartLine += 1;
  while(end > StartLine && /^\s*$/ := lines[end - 1])
    end -= 1;
  //println("slice(<StartLine>,<end-StartLine>)");
  if(StartLine != end)
  	return slice(lines, StartLine, end - StartLine);
  return [];
}

public list[str] splitLines(str text){
 text = visit(text) { case /\r/ => "" };
 if(!endsWith(text, "\n"))
 	text += "\n";
   return for(/<line:.*>\n/ := text)
 	          append line;
}

public str combine(list[str] lines){
  return "<for(str s <- lines){><s>\n<}>";
}


public list[ConceptName] children(Concept c){
  dir = catenate(courseDir, c.fullName);
  entries = [ entry | entry <- listEntries(dir), /^[A-Za-z]/ := entry, isDirectory(catenate(dir, entry))];
  res =  [ c.fullName + "/" + entry | entry <- c.details + (entries - c.details)];
  //println("children(<c.fullName>) =\> <res>");
  return res;
}

public list[ConceptName] children(loc file){
  fullName = getFullConceptName(file);
  cdetails = getDetails(file);
  //println("<file>, getDetails: <cdetails>");
  dir = catenate(courseDir, fullName);
  entries = [ entry | entry <- listEntries(dir), /^[A-Za-z]/ := entry, isDirectory(catenate(dir, entry))];
  res =  [ fullName + "/" + entry | entry <- cdetails + (entries - cdetails)];
  //println("children(<fullName>) =\> <res>");
  return res;
}

public str getSynopsis(loc file){
   try {
     script = readFileLines(file);
     sections = getSections(script);
     return intercalate(" ", sections["Synopsis"] ? "");
   } catch: return "";
}

// Extract list of names from a section (e.g. Details section)

public list[str] getNames(list[str] lines){
   return [ cat | line <- lines, /<cat:[A-Z][A-Za-z0-9]*>/ := line ];
}

public list[str] getDetails(loc file){
   try {
     script = readFileLines(file);
     sections = getSections(script);
     return getNames(sections["Details"] ? []);
   } catch: return [];
}

set[str] exclude = {".svn"};

map[str,Course] courseCache = ();

public Course getCourse(str name){
  if(courseCache[name]?)
     return courseCache[name];
  courseFile = catenate(courseDir, name + "/course.value");
  if(exists(courseFile)){
     theCourse = readTextValueFile(#Course, courseFile);
     courseCache[name] = theCourse;
     return theCourse;
  }
  throw ConceptError("No such course <name>");
}

public void updateCourse(Course c){
  courseCache[c.root] = c;
  courseFiles[c.root] = for(cn <- c.concepts)
                            append c.concepts[cn].file;
}

map[str,list[loc]] courseFiles = ();

public list[loc] getCourseFiles(ConceptName rootConcept){
  if(courseFiles[rootConcept]?)
     return courseFiles[rootConcept];
  files = [];
  try {
   theCourse = getCourse(rootConcept);
   files = for(cn <- theCourse.concepts)
               append theCourse.concepts[cn].file;
  } catch: {
     files = crawl(catenate(courseDir, rootConcept), conceptExtension);
  }
  
  courseFiles[rootConcept] = files;
  return files;
}

public list[loc] crawl(loc dir, str suffix){
//  println("crawl: <dir>, <listEntries(dir)>");
  list[loc] res = [];
  dotSuffix = "." + suffix;
  for( str entry <- listEntries(dir) ){
    if(entry notin exclude){                       // TODO: TEMP
       loc sub = catenate(dir, entry);
       if(endsWith(entry, dotSuffix)) { 
      	  res += [sub]; 
       }
       if(isDirectory(sub)) {
          res += crawl(sub, suffix);
      }
    }
  };
  return res;
}

