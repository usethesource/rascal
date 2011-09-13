@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::RascalTutor::CourseCompiler

import experiments::RascalTutor::CourseModel;
import String;
import Set;
import List;
import Relation;
import Map;
import Graph;
import IO;
import ValueIO;
import DateTime;
import experiments::RascalTutor::HTMLUtils;
import experiments::RascalTutor::HTMLGenerator;
import experiments::RascalTutor::ValueGenerator;
import Scripting;

private bool deployedMode = !writingAllowed();

bool editingAllowed = true;

public str mkConceptTemplate(ConceptName cn){
return "Name: <cn>
       '
       'Details:
       '
       'Syntax:
       '
       'Types:
       '
       'Function:
       '
       'Synopsis:
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

// Categories is deprecated but still here to guarantee correct parsing of concepts

public set[str] sectionKeywords = {"Name", "Details", "Categories", "Syntax", "Types", "Function", "Synopsis", "Description",
                                   "Examples", "Benefits", "Pitfalls", "Questions"};

private str logo = "\<img id=\"leftIcon\" height=\"40\" width=\"40\" src=\"/images/rascal-tutor-small.png\"\>";

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

// ------------------------ compile a concept ---------------------------------------

public Concept compileConcept(loc file){
   html_file = file[extension = htmlExtension];
   regen = regenerate || (!exists(html_file) || lastModified(file) > lastModified(html_file));
   
   
   setGenerating(regen);
   
   script = readFileLines(file);
   sections = getSections(script);
   
   if(!(sections["Name"]?))
      throw ConceptError("<file>: Missing section \"Name\"");
      
   name = sections["Name"][0];
   conceptName = getFullConceptName(file);
   println("Compiling <conceptName>, regenerate = <regen>");
   try {
	         
	   if(name != basename(conceptName))
	      throw ConceptError("Got concept name \"<name>\", but \"<basename(conceptName)>\" is required");
	      
	   optDetails      	= getNames(sections["Details"] ? []);
	 
	   syntaxSection 	= sections["Syntax"] ? [];
	   typesSection 	= sections["Types"] ? [];
	   functionSection 	= sections["Function"] ? [];
	   synopsisSection 	= sections["Synopsis"] ? [];
	   searchTs  		= searchTermsSynopsis(syntaxSection, typesSection, functionSection, synopsisSection);
	   questions 		= getAllQuestions(name, sections["Questions"]);
	   
	   html_body        = section("Syntax", markup(syntaxSection, conceptName)) +
                          section("Types", markup(typesSection, conceptName)) +
                          section("Function", markup(functionSection, conceptName)) +
  	                      section("Synopsis", markup(synopsisSection, conceptName)) +
  	                      section("Description", markup(sections["Description"], conceptName)) +
  	                      section("Examples", markup(sections["Examples"], conceptName)) +
  	                      section("Benefits", markup(sections["Benefits"], conceptName)) +
  	                      section("Pitfalls", markup(sections["Pitfalls"], conceptName)) +
  	                      ((isEmpty(questions)) ? "" : "<sectionHead("Questions")> <br()><for(quest <- questions){><showQuestion(conceptName,quest)> <}>"); 
	   
	   related = getAndClearRelated();
	   warnings = getAndClearWarnings();
	   
	   Concept C = concept(conceptName, file, warnings, optDetails, related, searchTs, questions);
	   if(regen)
	   	  generate(C, html_body);
	   return C;
	} catch NoSuchKey(e):
	    throw ConceptError("<conceptName>: Missing section \"<e>\"");
	  catch IOError(e):
	    throw ConceptError("<conceptName>: <e>");
	  catch e: 
	    throw ConceptError("<conceptName>: uncaught exception <e>");
}

public str showConcept(Concept C){
   html_file = C.file[extension = htmlExtension];
   return readFile(html_file);
}

public void generate(Concept C, str html_body){
   cn = C.fullName;
   childs = children(C);
   questions = C.questions;
   warnings = "";
   if(size(C.warnings) > 0){
      warnings = "\<ul\>\n";
      for(w <- C.warnings)
          warnings += li(w);
      warnings += "\</ul\>";
      warnings = section("Warnings", warnings);
   }
   navFile = catenate(courseDir, rootname(cn) + "/navigate.html");
   if(navigationPanel == ""){
      try {
	     navigationPanel = readFile(navFile);
      }
      catch e: println("can not read file <navFile>"); // do nothing
   }
   html_code = html(
  	head(title(cn) + prelude(rootname(cn))),
  	body(
  	"\<a id=\"tutorAction\" href=\"/Courses/index.html\"\><logo>\</a\>" +
  	  //warnings +
  	  table("container",
  	        tr(tdid("tdnav", navigationPanel) +
  	  
  	           tdid("tdconcept", div("conceptPane", 
  	              section("Name", showConceptPath(cn)) + searchBox(cn) +
  	              ((isEmpty(childs)) ? "" : section("Details", "<for(ch <- childs){><showConceptURL(ch)> &#032 <}>")) +
  	              html_body +
  	              editMenu(cn)
  	           ))
  	       ))
  	)
   );

    
   html_file = C.file[extension = htmlExtension];
   try {
	     writeFile(html_file, html_code);
   }
   catch e: println("can not save file <html_file>"); // do nothing
	 
   if(size(C.questions) > 0){
	  qs = C.questions;
	  quest_file = C.file[extension = questExtension];
      try {
	       writeTextValueFile(quest_file, C.questions);
	  }
	  catch e: println("can not save file <quest_file>"); // do nothing
   }
}

// Update de details section for the parent node pn of cn
// (needed when a new child has been edit via edit/new):
// - Recompute the details for parent pn
// - Use them to replace the details section in pn.html

public void updateParentDetails(ConceptName cn){
  pn = parentname(cn);
  println("updateParentDetails: cn = <cn>, pn = <pn>");
  if(rootname(pn) == pn)  // No update needed at root
     return;
  file = conceptFile(pn);
  println("updateParentDetails: file = <file>");
  pconcept = compileConcept(file);     
}

// Generate prelude of web page

public str prelude(str courseName){ 
  return "\<link type=\"text/css\" rel=\"stylesheet\" href=\"/Courses/prelude.css\"/\>
  		 '\<script type=\"text/javascript\" src=\"/Courses/jquery-1.4.2.min.js\"\>\</script\>
  		 '\<script type=\"text/javascript\" src=\"/Courses/jquery.cookie.js\"\>\</script\>
         '\<script type=\"text/javascript\" src=\"/Courses/jquery.jstree.js\"\>\</script\>
         '\<script type=\"text/javascript\" src=\"/Courses/prelude.js\"\>\</script\>
         '\<script type=\"text/javascript\" src=\"/Courses/<courseName>/course.js\"\>\</script\>\n"
         ;
}

public str jsCoursePrelude(str courseName, list[str] baseConcepts, map[ConceptName,Concept] concepts){  
  return 
  "/* Generated code for course <courseName>, generated <now()> */
  '
  '$.setRootConcept(\"<courseName>\");
  '
  'var baseConcepts = <mkJsArray(baseConcepts, "new Array()")>;
  '
  'var conceptNames = <mkJsArray(sort(toList(domain(concepts))), "new Array()")>;
  '
  'var searchTerms = {};
  '
  '<for( name <- concepts ){>
  'searchTerms[\"<name>\"] = <mkJsArray(toList(concepts[name].searchTerms), "null")>;
  '<}>";
}

public str mkJsArray(list[str] elms, str nullCase){
  int n = size(elms) - 1;
  return (n > 0) ? "new Array(<for(int i <- [0 .. n]){><(i==0)?"":",">\"<escapeForJavascript(elms[i])>\"<}>)"
                 : nullCase;
}

public str section(str name, str txt){
  return (/^\s*$/s := txt) ? "" : div(name, sectionHead(name) +  " " + txt);
}

public str searchBox(ConceptName cn){
  return "
         '\<div id=\"searchBox\"\>
         '  \<form method=\"GET\" id=\"searchForm\" action=\"/search\"\> 
         '    \<img id=\"searchIcon\" height=\"20\" width=\"20\" src=\"/images/magnify.png\"\>
         '    \<input type=\"hidden\" name=\"concept\" value=\"<cn>\"\>
         '    \<input type=\"text\" id=\"searchField\" name=\"term\" autocomplete=\"off\"\>\<br /\>
         '    \<div id=\"popups\"\>\</div\>
         '  \</form\>
         '\</div\>
         ";
}

public str editMenu(ConceptName cn){
  warnings = "/Courses/<rootname(cn)>/warnings.html";
 
  return "\n\<div id=\"editMenu\"\>" +
         "\<a id=\"tutorAction\" href=\"/Courses/index.html\"\><logo>\</a\>" +
          (editingAllowed ?
              "[\<a id=\"editAction\" href=\"/edit?concept=<cn>&new=false\"\>\<b\>Edit\</b\>\</a\>] | 
               [\<a id=\"newAction\" href=\"/edit?concept=<cn>&new=true\"\>\<b\>New Subconcept\</b\>\</a\>] |
               [\<a id=\"compileAction\" href=\"/compile?name=<rootname(cn)>\"\>\<b\>Check Course\</b\>\</a\>] |
               [\<a id=\"compileAction\" href=\"/compile?name=<rootname(cn)>&flags=regenerate\"\>\<b\>Recompile Course\</b\>\</a\>] |
               [\<a id=\"warnAction\" href=\"<warnings>\"\>\<b\>Warnings\</b\>\</a\>]"
              : "")
          +
            "\</div\>\n";
}

public list[ConceptName] children(Concept c){
  dir = catenate(courseDir, c.fullName);
  entries = [ entry | entry <- listEntries(dir), /^[A-Za-z]/ := entry, isDirectory(catenate(dir, entry))];
  res =  [ c.fullName + "/" + entry | entry <- c.details + (entries - c.details)];
  //println("children(<c.fullName>) =\> <res>");
  return res;
}

// Extract list of names from a section (e.g. Details section)

public list[str] getNames(list[str] lines){
   return [ cat | line <- lines, /<cat:[A-Z][A-Za-z0-9]*>/ := line ];
}

// Extract specific question type from Questions section

public list[str] getQuestions(str qtype, str questions){
  return [text | /<qtype>:<text:.*?>(\Z|[A-Z][a-z\-\_]+:)/s := questions];
}

// Extract specific answer type from a Question

public set[str] getAnswers(str atype, str question){
  return {text | /<atype>:\s*<text:.*>/ := question};
}     

// Extract all the questions from the Questions section
public list[Question] getAllQuestions(ConceptName cname, list[str] qsection){
   int nquestions = 1;
   n = size(qsection);
   questions = [];
   int i = 0;
   while(i < n){
     //println("getQuestions: <qsection[i]>");
     switch(qsection[i]){
       case /^QText:<question:.*>$/: {
          i += 1;
          set[str] answers = {};
          while(i < n && /^a:\s*<text:.*>/ := qsection[i]){
            answers += text;
            i += 1;
          }
          if(size(answers) == 0)
          	throw ConceptError("TextQuestion with no or malformed answers");
          questions += textQuestion("<nquestions>", markup1([question], cname), answers);
          nquestions += 1;
       }
       case /^QChoice:<question:.*>$/: {
          i += 1;
          good_answers = [];
          bad_answers = [];
          while(/^<prop:[gb]>:\s*<text:.*>/ := qsection[i] && i < n){
            if(prop == "g")
               good_answers += text;
            else
               bad_answers += text;
            i += 1;
          }
          if(size(good_answers) == 0 || size(bad_answers) == 0)
          	throw ConceptError("ChoiceQuestion with insufficient or malformed answers");
          	
          choices = [good(g) | str g <- good_answers] + [bad(b) | str b <- bad_answers];
      
          questions += choiceQuestion("<nquestions>", markup([question], cname), choices);
          nquestions += 1;
       }
 
      case /^QValue:\s*<cnd:.*>$/: {
           <i, q> = getTvQuestion(cname, valueOfExpr(), "<nquestions>", qsection, i, cnd);
           questions += q;
           nquestions += 1;
      }
      
      case /^QType:\s*<cnd:.*>$/: {
           <i, q> = getTvQuestion(cname, typeOfExpr(), "<nquestions>", qsection, i, cnd);
           questions += q;
           nquestions += 1;
      }
      
      case /^\s*$/:
            i += 1;
   
      default: {
         println("*** skipping: <qsection[i]>");
         i += 1;
      }   
     }
   }
   return questions;
}

public tuple[int, Question] getTvQuestion(ConceptName cname, TVkind kind, str name, list[str] qsection, int i, str cnd){
     n = size(qsection);
	 if(cnd != "")
	   qsection[i] = "test: <cnd>";
	 else
	   i += 1;
	 
	 setup = [];
	 desc = "";
     vars = [];
     auxVars = [];
     hint = "";
     rtype = \void();
     listing = "";
	 cndBefore = "";
	 cndAfter = "";
	 lstBefore = "";
	 lstAfter = "";
	 holeInCnd = false;
	 holeInLst = false;
	 
	 set[str] definedVars = {};
	 set[str] usedVars = {};
	 
	 while(i < n && /^[A-Z][A-Za-z]+:/ !:= qsection[i]){
	   //println(qsection[i]);
	   switch(qsection[i]){
	   
	    case /^desc:\s*<rest:.*>$/:
	     { desc += rest; i += 1; }
	   
	    case /^prep:\s*<rest:.*>$/: 
	      { setup += rest; i += 1;}
	    
	    case /^make:\s*<name:[A-Za-z0-9]+>\s*\=\s*<tp:.*>$/:
	      { try { vars += <name, parseType(tp)>; }
	        catch:
	            throw ConceptError("Question <name>: type of generated variable <name> is incorrect");
	        definedVars += name;	
	        i += 1; 
	      }
	      
	    case /^expr:\s*<name:[A-Za-z0-9]+>\s*\=\s*<expr:.*>$/:
	      { auxVars += <name, expr>;
	        u = uses(expr);
	        if(u - definedVars != {})
	           throw ConceptError("Question <name>: expr uses undefined variables: <u - definedVars>");
	        definedVars += name;
	        usedVars += u;
	        i += 1; 
	      }
	      
	    case /^type:\s*<tp:.*>$/: {
	        rtype = \void();
			try { rtype = parseType(tp); }
			catch:
			     throw ConceptError("Question <name>: cannot parse type of expected type");
	        usedVars += uses(rtype);
	        i += 1; 
		}
		case /^hint:\s*<txt:.*>$/: {
			hint = txt; 
	        usedVars += uses(txt);
	        i += 1; 
		}
		case /^test:\s*<e:.*>$/: {
		   if(cndBefore + cndAfter != "")
		      throw ConceptError("Question <name>: has already a test <cnd>");
		   if (/^<b:.*>\<\?\><a:.*>$/ := e){
		     cndBefore = b;
		     cndAfter = a;
		     holeInCnd = true;
		   } else {
		     cndBefore = e;
		   }
	       usedVars += uses(cndBefore + cndAfter);
		   i += 1;
		}
	      
	    case /^list:\s*<rest:.*>$/: {
	      if(size(rest) > 0)
	        qsection[i] = rest;
	      else
	         i += 1;
	      while(i < n && /^[A-Z][A-Za-z]+:/ !:= qsection[i] && /^test:/ !:= qsection[i]){
	        //println(qsection[i]);
	        if (/^<b:.*>\<\?\><a:.*>$/ := qsection[i]){
	          lstBefore += b;
	          lstAfter = a + "\n";
	          holeInLst = true;
	        } else {
	          if(holeInLst) 
	            lstAfter += qsection[i] + "\n";
	          else
	       		 lstBefore += qsection[i] + "\n";
	        }
	        usedVars += uses(lstBefore + lstAfter);
	        i += 1;
	       
          } //while
	    } // case

        case /^\s*$/:
          i += 1;

        default: {
          println("Skipping: <qsection[i]>"); i += 1;
        }
	   } // switch
	 } // while
	
    //println("setup = <setup>");
    //println("vars = <vars>");
    //println("auxVars = <auxVars>");
    //println("hint = <hint>");

	 //println("Details: setup = <setup>, lstBefore = <lstBefore>, holeInLst = <holeInLst>, cndBefore = <cndBefore>, cndAfter = <cndAfter>, holeInCnd = <holeInCnd>, vars = <vars>, auxVars = <auxVars>");

/*
       Lst holeInLst holeInCnd Exp
Value   +      +         +      0   ERROR
        +      +         -      0
        +      -         +      0
        +      -         -      0
        -      +         0      0   ERROR
        -      -         +      0
        -      -         -      0
        -      -         -      0   ERROR
        
Type    +      +         +      0   ERROR
        +      +         -      0
        +      -         +      0
        +      -         -      0
        -      +         0      0   ERROR
        -      -         +      +
        -      -         +      -   ERROR
        -      -         -      0
        -      -         -      0   ERROR    
*/
     if(holeInLst && holeInCnd)
        throw ConceptError("Question <name> should have at most one hole");
        
     if((lstBefore + lstAfter) == "" && holeInLst)
        throw ConceptError("Question <name> has an empty listing with a hole");
        
     if((cndBefore + cndAfter) == "" && !(holeInLst))
        throw ConceptError("Question <name> has no test");
        
     if(kind == typeOfExpr() && holeInCnd && rtype == \void())
           throw ConceptError("Type question <name> has condition with a hole and requires an expected type");
     
     if(usedVars - definedVars != {})
        throw ConceptError("Question <name>: undefined variables <usedVars - definedVars>");
        
     if(definedVars - usedVars != {})
        throw ConceptError("Question <name>: unused variables <definedVars - usedVars>");
        
     if(definedVars == {} && vars == [])
        try {
          vars = autoDeclare(cndBefore + cndAfter);
        } catch: throw ConceptError("Question <name>: illegal type in test");

     return <i, tvQuestion(name, kind, details(markup([desc], cname), setup, lstBefore, lstAfter, cndBefore, cndAfter, holeInLst, holeInCnd, vars, auxVars, rtype, hint))>;
}

// Compute the refinements induced by the path
public rel[str,str] getFullRefinements(list[str] names){
   lasti = size(names) - 1;
   if(lasti > 0)
     return {<compose(names, 0, i), compose(names, 0, i + 1)> | int i <- [0 .. lasti - 1]};
   return {};
}

// Compute the refinements induced by the path
public rel[str,str] getBaseRefinements(list[str] names){
   n = size(names);
   if(n >= 2)
      return {<names[i], names[i+1]> | int i <- [0 .. n-2]};
   return {};
}

str navigationPanel = "";
bool regenerate = false;

public Course compileCourse(ConceptName rootConcept, str flags){

   if(/regenerate/ := flags)
      regenerate = true;
      
   concepts = ();
    
   courseFiles = crawl(catenate(courseDir, rootConcept), conceptExtension);
   println("crawl returns: <courseFiles>");
   navigationPanel = div("navPane", ul(crawlNavigation(catenate(courseDir, rootConcept), conceptExtension, "")));
   navFile = catenate(courseDir, rootConcept + "/navigate.html");
   println("crawlNavigation returns:\n<navigationPanel>");
   try {
	   writeFile(navFile, navigationPanel);
   }
   catch e: println("can not save file <navFile>"); // do nothing

   for(file <- courseFiles){
       cpt = compileConcept(file);
       fullName = getFullConceptName(file);
       if(concepts[fullName]?)
       	  println("Double declaration for <fullName>");
       concepts[fullName] = cpt;      	 
   }
   C = validateCourse(rootConcept, concepts);
   baseConcepts = C.baseConcepts;
    
   // Generate global course data in JS file
   jsFile = catenate(courseDir, rootConcept + "/course.js");
  
   try {
	   writeFile(jsFile, jsCoursePrelude(rootConcept, baseConcepts, concepts));
   }
   catch e: println("can not save file <jsFile>"); // do nothing
   
   warnings = C.warnings;
   continue = "\<p\>Continue with course <showConceptURL(C.root)>\<p\>";
   
   warn_html = "";
   if(size(warnings) == 0){
     warn_html = html(head(title("No warnings in course <C.root>") + prelude(C.root)),
                      body(h1("No warnings in course <C.root>") + continue));
   } else {
     warn_html = html(head(title("Warnings in course <C.root>") + prelude(C.root)),
                      body(continue +
                           h1("<size(warnings)> warning(s) found in course <C.root>:") +
                           ul("<for(w <- warnings){><li(w)><}>") +
                           continue
                     ));
   }
   
   warnFile = catenate(courseDir, C.root + "/warnings.html");
   
   try {
      writeFile(warnFile, warn_html);
   }
   catch e: println("can not save file <warnFile>"); // do nothing
    
   return C;
}

public Course validateCourse(ConceptName rootConcept, map[ConceptName,Concept] conceptMap){
    // Global sanity checks on concept dependencies
    //Graph[ConceptName] fullRefinements = {};
    refinements = {};
    Graph[ConceptName] baseRefinements = {};
    
    for(cn <- conceptMap){
       baseRefinements += getBaseRefinements(basenames(cn));
       refinements += getFullRefinements(basenames(cn));
    }
    
    generalizations = invert(baseRefinements);
    allBaseConcepts = carrier(baseRefinements);
    allFullConcepts = carrier(refinements);
    
    undefinedFullConcepts =  allFullConcepts - domain(conceptMap);
    
    warnings = [];
    if(!isEmpty(undefinedFullConcepts))
    	warnings += ["Undefined concepts: <undefinedFullConcepts>"];
    roots = top(baseRefinements);
    if(size(roots) != 1)
        warnings += ["Root is not unique: <roots>"];
    if(roots != {rootConcept})
        warnings += ["Roots <roots> unequal to course root concept \"<rootConcept>\""];
    
    map[str, ConceptName] fullRelated = ();
    set[str] searchTs = {};
    for(cname <- conceptMap){
       C = conceptMap[cname];
       for(w <- C.warnings)
           warnings += ["<showConceptPath(cname)>: <w>"]; 
       searchTs += C.searchTerms;
       for(r <- C.related){
         //println("related.r = <r>");
         rbasenames = basenames(r);
         if(!(toSet(rbasenames) <= allBaseConcepts)){
            warnings += ["<showConceptPath(cname)>: unknown concept \"<r>\""];
         } else {
            parents = generalizations[rbasenames[0]];
            if(size(parents) > 1){
                warnings += ["<showConceptPath(cname)>: ambiguous concept \"<rbasenames[0]>\", add one of its parents <parents> to disambiguate"];
            }
            if(size(rbasenames) >= 2){
               for(int i <- [0 .. size(rbasenames)-2]){
                   if(<rbasenames[i], rbasenames[i+1]> notin baseRefinements){
                      warnings += ["<showConceptPath(cname)>: unknown concept \"<rbasenames[i]>/<rbasenames[i+1]>\""];
                   }
               } // for
            } // if
            fullPath = shortestPathPair(baseRefinements, rootConcept, last(rbasenames));
            //println("fullPath = <fullPath>");
            fullRelated[r] = compose(fullPath);
         } // else
       } // for(r <-
       
       for(d <- C.details){
           if((cname + "/" + d) notin refinements[cname]){
              warnings += ["<showConceptPath(cname)>: non-existent detail \"<d>\""];
           }
       }
       //C.warnings = cwarnings;
       //conceptMap[cname] = concept(C.fullName, C.file, cwarnings, C.details, C.related, C.searchTerms, C.questions);
                          
    } // for(cname
    
    // Map same search term with/without capitalization to the one with capitalization
    searchTerms1 = {};
    for(trm <- searchTs){
        if(/^<S:[a-z]><tail:[A-Za-z0-9]*>$/ := trm){
           if((toUpperCase(S) + tail) notin allBaseConcepts)
              searchTerms1 += {trm};
        } else 
          searchTerms1 += {trm};
   }               
    
   println("fullRelated = <fullRelated>");
   println("searchTerms1= <searchTerms1>");
   println("extended allBaseConcepts: <sort(toList(allBaseConcepts + searchTs))>");
   println("Warnings:\n<for(w <- warnings){><w>\n<}>");
   return course(rootConcept, warnings, conceptMap, refinements, sort(toList(allBaseConcepts + searchTerms1)), fullRelated);
}

set[str] exclude = {".svn", "IntroSyntaxDefinitionAndParsing", "AlgebraicDataType"};

public list[loc] crawl(loc dir, str suffix){
//  println("crawl: <dir>, <listEntries(dir)>");
  list[loc] res = [];
  dotSuffix = "." + suffix;
  for( str entry <- listEntries(dir) ){
    if(entry notin exclude){                       // TODO: TEMP
       loc sub = catenate(dir, entry);
       if(endsWith(entry, dotSuffix)) { 
      	  res += [sub]; 
      	  if(regenerate)
      	    touch(sub);
       }
       if(isDirectory(sub)) {
          res += crawl(sub, suffix);
      }
    }
  };
  return res;
}

private bool hasSubdirs(loc dir){
  return isDirectory(dir) && size([ e | e <-listEntries(dir), isDirectory(e)]) > 0;
}

public str crawlNavigation(loc dir, str suffix, str offset){
  println("crawlNavigation: <dir>, <listEntries(dir)>");
 
  dotSuffix = "." + suffix;
  dirConcept = "";
  panel = "";
  for( str entry <- listEntries(dir) ){
    if(entry notin exclude){                       // TODO: TEMP
       loc sub = catenate(dir, entry);
       if(endsWith(entry, dotSuffix)) {
          cn = getFullConceptName(sub);
          ename = substring(entry, 0, size(entry) - size(dotSuffix));
      	  dirConcept = "\<a href=\"/Courses/<cn>/<ename>.html\"\><ename>\</a\>";
       }
       if(isDirectory(sub)) {
          panel += offset + crawlNavigation(sub, suffix, offset + "  ");
       }
    }
  };
  
  if(panel == "")
     return (dirConcept == "") ? "" : offset + li(dirConcept);
  
  return offset + li("<dirConcept>\n<offset>\<ul\><panel><offset>\</ul\>") + "\n";
}


// --------------------------------- Question Presentation ---------------------------

// Present a Question

private str answerFormBegin(ConceptName cpid, QuestionName qid, str formClass){
	return "
\<form method=\"GET\" action=\"validate\" class=\"<formClass>\"\>
\<input type=\"hidden\" name=\"concept\" value=\"<cpid>\"\>
\<input type=\"hidden\" name=\"exercise\" value=\"<qid>\"\>\n";
}

private str answerFormEnd(str submitText, str submitClass){
  return "
\<input type=\"submit\" value=\"<submitText>\" class=\"<submitClass>\"\>
\</form\>";
}

private str anotherQuestionForm(ConceptName cpid, QuestionName qid){
	return answerFormBegin(cpid, qid, "anotherForm") + 
	"\<input type=\"hidden\" name=\"another\" value=\"yes\"\>\n" +
	answerFormEnd("I want another question", "anotherSubmit");
}

private str cheatForm(ConceptName cpid, QuestionName qid, str expr){
    return "";
	return answerFormBegin(cpid, qid, "cheatForm") + 
	       "\<input type=\"hidden\" name=\"expr\" value=\"<expr>\"\>\n" +
           "\<input type=\"hidden\" name=\"cheat\" value=\"yes\"\>\n" +
           answerFormEnd("I am cheating today", "cheatSubmit");
}

public str status(str id, str txt){
	return "\n\<span id=\"<id>\" class=\"answerStatus\"\>\n<txt>\n\</span\>\n";
}

public str good(){
  return "\<img height=\"25\" width=\"25\" src=\"/images/good.png\"/\>";
}

public str bad(){
   return "\<img height=\"25\" width=\"25\" src=\"/images/bad.png\"/\>";
}

public str status(QuestionName qid){
  return (qid in goodAnswer) ? good() : ((qid in badAnswer) ? bad() : "");
}

public str showQuestion(ConceptName cpid, Question q){
//println("showQuestion: <cpid>, <q>");
  qid = q.name;
  qdescr = "";
  qexpr  = "";
  qform = "";
  
  switch(q){
    case choiceQuestion(qid, descr, choices): {
      qdescr = descr;
      idx = [0 .. size(choices)-1];
      qform = "<for(int i <- idx){><(i>0)?br():"">\<input type=\"radio\" name=\"answer\" value=\"<i>\"\><choices[i].description>\n<}>";
    }
    case textQuestion(qid,descr,replies): {
      qdescr = descr;
      qform = "\<textarea rows=\"1\" cols=\"60\" name=\"answer\" class=\"answerText\"\>\</textarea\>";
    }
    case tvQuestion(qid, qkind, qdetails): {
      qdescr = qdetails.descr;
      setup  = qdetails.setup;
      lstBefore = qdetails.lstBefore;
      lstAfter = qdetails.lstAfter;
      cndBefore = qdetails.cndBefore;
      cndAfter = qdetails.cndAfter;
      holeInLst = qdetails.holeInLst;
      holeInCnd = qdetails.holeInCnd;
      vars   = qdetails.vars;
      auxVars = qdetails.auxVars;
      rtype = qdetails.rtype;
	  hint = qdetails.hint;

      VarEnv env = ();
      generatedVars = [];
      for(<name, tp> <- vars){
        tp1 = generateType(tp, env);
        env[name] = <tp1, generateValue(tp1, env)>;
        generatedVars += name;
	  }

	  for(<name, exp> <- auxVars){
         exp1 = subst(exp, env);
         //println("exp1 = <exp1>");
         try {
           env[name] = <parseType("<evalType(setup + exp1)>"), "<eval(setup + exp1)>">;
         } catch: throw "Error in computing <name>, <exp>";
      }
      //println("env = <env>");
      
      lstBefore = escapeForHtml(subst(lstBefore, env));
      lstAfter = escapeForHtml(subst(lstAfter, env));
      cndBefore = escapeForHtml(subst(cndBefore, env));
      cndAfter = escapeForHtml(subst(cndAfter, env));
      
      qform = "<for(param <- generatedVars){>\<input type=\"hidden\" name=\"<param>\" value=\"<escapeForHtml(env[param].rval)>\"\>\n<}>";
      
      qtextarea = "\<textarea rows=\"1\" cols=\"30\" name=\"answer\" class=\"answerText\"\>\</textarea\>";
      
      if(lstBefore != "" || lstAfter != ""){  // A listing is present in the question
         if(holeInLst)
            qform +=  "Fill in " + "\<pre class=\"prequestion\"\>" + lstBefore + qtextarea + lstAfter + "\</pre\>";
         else
            qform += "Given " + "\<pre class=\"prequestion\"\>" + lstBefore + "\</pre\>";
      }
      	        
      if(qkind == valueOfExpr()){ // A Value question
      	    //if(lstBefore != "")
      	    //    if (holeInLst) qform += "and make the following true:";
      	        
         if(holeInCnd)
      	    qform += "\<pre class=\"prequestion\"\>" + cndBefore + qtextarea + cndAfter +  "\</pre\>";
         else if(cndBefore + cndAfter != "")
            if(holeInLst)
               qform += " and make the following true:" + "\<pre class=\"prequestion\"\>" + cndBefore + "\</pre\>";
            else
      	       qform += ((lstBefore != "") ? "Make the following true:" : "") + "\<pre class=\"prequestion\"\>" + cndBefore + " == " + qtextarea + "\</pre\>"; 
      } else {                     // A Type question
      	if(holeInCnd)
      	   qform +=  "The type of " + tt(cndBefore) + qtextarea + tt(cndAfter) + " is " + tt(toString(generateType(rtype, env)));
         else if(holeInLst)
           qform += "and make the type of " + tt(cndBefore) + " equal to " + tt(toString(generateType(rtype, env)));  
         else
           qform += "The type of " + tt(cndBefore) + " is " + qtextarea; 
           
         qform += br();
       }
    }
    default:
      throw "Unimplemented question type: <q>";
  }
  answerForm = answerFormBegin(cpid, qid, "answerForm") + qform  + answerFormEnd("Give answer", "answerSubmit");

  return div(qid, "question",
                  b(basename("Question " + qid + ". ")) + status(qid + "good", good()) + status(qid + "bad", bad()) +
                  "\n\<span id=\"answerFeedback<qid>\" class=\"answerFeedback\"\>\</span\>\n" + 
                  qdescr +  answerForm + 
                  anotherQuestionForm(cpid, qid) + cheatForm(cpid, qid, qexpr) + br());
}

public QuestionName lastQuestion = "";

// trim layout from a string
public str trim (str txt){
    return txt;
	return
	  visit(txt){
	    case /[\ \t\n\r]/ => ""
	  };
}

public Question getQuestion(ConceptName cid, QuestionName qid){

  try {
  	quest_file = catenate(courseDir, cid + "/" + basename(cid))[extension = questExtension];
  	questions = readTextValueFile(#Questions, quest_file);
  
 	 for(q <- questions)
  		if(q.name == qid)
  			return q;
  } catch e: throw "Question file for <cid> not found";
  
  throw "Question <qid> not found";
}
