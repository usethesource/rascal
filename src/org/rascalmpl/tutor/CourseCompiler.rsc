@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

@bootstrapParser
module CourseCompiler

import CourseModel;
import util::Math;
import String;
import Set;
import List;
import Relation;
import Map;
import  analysis::graphs::Graph;
import IO;
import ValueIO;
import DateTime;
import Exception;
import HTMLUtils;
import HTMLGenerator;
import ValueGenerator;
import util::Eval;
import util::Reflective;
import RascalUtils;
import util::Benchmark;
import util::Math;
import util::Monitor;

// ------------------------ compile a course ----------------------------------------

public Course compileCourse(ConceptName rootConcept){   
   
   startJob("Compile <rootConcept>", 1000);
   begin = realTime();
   arbSeed(0); // Set the arb generation so that the same choices will be made for question generation.
   isExam = false;
   concepts = ();
   warnings = [];
   conceptList = getUncachedCourseConcepts(rootConcept);
   todo(size(conceptList));
   for(cn <- conceptList){
       try {
           event(cn, 1);
	       cpt = compileConcept(cn);
	       if(concepts[cn]?)
	       	  warnings += mkWarning(cn, "Redeclaration");
	       concepts[cn] = cpt; 
       } 
       catch ConceptError(c, msg):
           warnings += [ mkWarning(cn, msg) ];     
       catch e:
           warnings += [ mkWarning(cn, "<e>") ];      	 
   }
  
   C = makeCourse(rootConcept, concepts, warnings);
   updateCourse(C);
   cc = generateCourseControl(C);
   passed = (realTime() - begin)/1000;
   println("Compilation time: <passed/60> min. (<passed> sec.)");
   endJob(true);
   return cc;
}

str mkWarning(ConceptName cname, str msg) = "<showConceptPath(cname)>: <msg>";

// --------- Make a course -----------------------------------

public Course makeCourse(ConceptName rootConcept, map[ConceptName,Concept] conceptMap, list[str] warnings){      
    allBaseConcepts = {};
    set[str] searchTs = {};
 //   warnings = [];
    
    for(cname <- conceptMap){
       allBaseConcepts += toSet(basenames(cname));
       C = conceptMap[cname];
       for(w <- C.warnings)
           warnings += [mkWarning(cname, w)]; 
       searchTs += C.searchTerms;
    }
    
    // Map same search term with/without capitalization to the one with capitalization
    // and eliminate single letter/digit terms.
    
    searchTerms1 = {};
    for(trm <- searchTs){
        if(/^[a-zA-Z0-9]$/ !:= trm){
           if(/^<S:[a-z]><tail:[A-Za-z0-9]*>$/ := trm){
              if((toUpperCase(S) + tail) notin allBaseConcepts)
                 searchTerms1 += {trm};
              } else 
                searchTerms1 += {trm};
        }
   }               
    
   return course(rootConcept, warnings, conceptMap, sort(toList(allBaseConcepts + searchTerms1)));
}


public Course generateCourseControl(Course C){

   rootConcept = C.root;
   concepts = C.concepts;
  
   navigationPanel = makeNavigationPanel(rootConcept, concepts, "");
    
   // Generate global course data in JS file
   jsFile = courseDir + rootConcept + "course.js";
  
   try {
	   writeFile(jsFile, jsCoursePrelude(rootConcept, C.baseConcepts, concepts));
   }
   catch e: println("can not save file <jsFile>"); // do nothing
   
   warnings = C.warnings;
   continueWith = p("Continue with course <showConceptURL(C.root)>");
   
   warn_html = "";
   if(size(warnings) == 0){
     warn_html = html(head(title("No warnings in course <C.root>") + prelude(C.root)),
                      body(h1("No warnings in course <C.root>") + continueWith));
   } else {
     warn_html = html(head(title("Warnings in course <C.root>") + prelude(C.root)),
                      body(continueWith +
                           h1("<size(warnings)> warning(s) found in course <C.root>:") +
                           ul("<for(w <- warnings){><li(w)><}>") +
                           continueWith
                     ));
   }
   
   warnFile = courseDir + C.root + "warnings.html";
   
   try {
      writeFile(warnFile, warn_html);
   }
   catch e: println("cannot save file <warnFile>"); // do nothing
   
   courseFile = courseDir + C.root + "course.value";
   try {
     writeTextValueFile(courseFile,C);
   }
   catch e: println("cannot save file <courseFile>"); // do nothing
    
   println("<C.root>: generateCourseControl done");
   return C;
}

public str makeNavigationPanel(ConceptName rootConcept, map[ConceptName,Concept] concepts, str offset){
   navigationPanel = div("navPane", ul(makeNavigationPanel1(rootConcept, concepts, "")));
   
   navFile = courseDir + rootConcept + "navigate.html";
   
   try {
	   writeFile(navFile, navigationPanel);
   }
   catch e: println("can not save file <navFile>"); // do nothing
   
  return navigationPanel;
}

public str makeNavigationPanel1(ConceptName rootConcept, map[ConceptName,Concept] concepts, str offset){
  try {
    panel = "";
    base = basename(rootConcept);
    dirConcept = "\<a id=\"<rootConcept>\" href=\"/<rootConcept>/<base>.html\"\><base>\</a\>";
    
    if(concepts[rootConcept]?){
       for(child <- children(concepts[rootConcept])){
           r = makeNavigationPanel1(child, concepts, offset + "  ");
           if(r != "")
      	     panel += offset + r;
      }
    }
    return (panel == "") ? li(dirConcept) : offset + li("<dirConcept>\n<offset>\<ul\><panel><offset>\</ul\>") + "\n";
  } catch: println("IGNORING: <rootConcept>"); return "";
}

public str getNavigationPanel(ConceptName rootConcept){
  return ahref("navPanePlaceHolder", "/<rootname(rootConcept)>/navigate.html", "Navigation" ) +"\<script type=\"text/javascript\"\> var navigationPaneSource=\"/<rootname(rootConcept)>/navigate.html\"; \</script\>";// panelCache[rootConcept];
}


// ------------------------ compile a concept ---------------------------------------

// Compile the concept at location file.
// If updateParent == true we also compile the parent concept (necessary when a new concept is added)

public Concept compileAndGenerateConcept(ConceptName cn, bool updateParent){

   C = compileConcept(cn);
   println("Compiling <cn> ... done.");
   courseFile = courseDir + rootname(cn) + "course.value";
   try {
     theCourse = readTextValueFile(#Course, courseFile);
     concepts = theCourse.concepts;
     concepts[cn] = C;
     if(updateParent){
        pn = parentname(cn);
        if(rootname(pn) != pn) { // No update needed at root
           file = conceptFile(pn);
           println("<cn>: updateParentDetails: pn = <pn>");
           concepts[pn] =  compileConcept(pn);    
        }
     }
     theCourse = makeCourse(theCourse.root, concepts, []);
     println("<C.fullName>: validated course");
     generateCourseControl(theCourse);
     println("<cn>: generated control");
     updateCourse(theCourse);
     return C;
   }
   catch e: { println("can not read file <courseFile>");
              theCourse = compileCourse(rootname(cn));
              updateCourse(theCourse);
              return theCourse.concepts[cn];
            }
}

public Concept compileExam(ConceptName conceptName){
	prevIsExam = isExam;
	isExam = true;
	dir = courseDir;
	courseDir = examsDir + "exams";
	c = compileConcept(conceptName);
	isExam = prevIsExam;
	courseDir = dir;
	return c;
}

public Concept compileConcept(ConceptName conceptName){
   
   println("compileConcept: <conceptName>");
   sections = getSections(conceptName);
   //println("<conceptName>: sections = <sections>");

   if(!(sections["Name"]?))
      throw ConceptError(conceptName, "Missing section \"Name\"");
      
   name = sections["Name"][0];
  
   //println("<conceptName>: getSections done.");
   
   try {
	   local_warnings = [];
	   if(name != basename(conceptName))
	      throw ConceptError(conceptName, "Got concept name \"<name>\", but \"<basename(conceptName)>\" is required");
	      
	   optDetails      	= getNames(sections["Details"] ? []);
	   
	   // Verify that all given details do exist
	   remove_details = [];
	   for(detailName <- optDetails){
	       if(!exists(courseDir + "<conceptName>/<detailName>")){
	         remove_details += [detailName];
	       }
	   }
	        
	   for(detailName <- remove_details){
	       optDetails -= detailName;
	       local_warnings += "non-existing detail <detailName>";
	   }
	   
	   //optionsSection    = sections["Options"] ? [];
	   
	   //isExam = false;
	   //if(size(optionsSection) > 0 && /exam/ := optionsSection[0])
	   //   isExam = true;
	 
	   syntaxSection 	= sections["Syntax"] ? [];
	   typesSection 	= sections["Types"] ? [];
	   functionSection 	= sections["Function"] ? [];
	   synopsisSection 	= sections["Synopsis"] ? [];
	   usageSection     = sections["Usage"] ? [];
	 
	   searchTs  		= searchTermsSynopsis(syntaxSection, typesSection, functionSection, synopsisSection);
	   
	   	       
	   questions 		= getAllQuestions(conceptName, sections["Questions"] ? []);
	   

	   html_synopsis    = "<section("Synopsis", markup(synopsisSection, conceptName))>
	                       <section("Syntax", markup(syntaxSection, conceptName))>
                           <section("Types", markup(typesSection, conceptName))>
                           <section("Function", markup(functionSection, conceptName))>
                           <section("Usage", markup(usageSection, conceptName))>";
  	   html_body        = "<section("Description", markup(sections["Description"] ? [], conceptName))>
  	                       <section("Examples", markup(sections["Examples"] ? [], conceptName))>
  	                       <section("Benefits", markup(sections["Benefits"] ? [], conceptName))>
  	                       <section("Pitfalls", markup(sections["Pitfalls"] ? [], conceptName))>
  	                       <showQuestionsSection(conceptName, questions)>";
	   warnings         = getAndClearWarnings() + local_warnings;
	       
	   C =  concept(conceptName, warnings, optDetails, searchTs, questions);
	   //println("<conceptName>: creating concept done: <C>");
	   generate(C, escapeForHtml("<for(line <- synopsisSection){> <line><}>"),  html_synopsis, html_body);
	   //println("<conceptName>: generating HTML done.");
	   return C;

	} catch NoSuchKey(e):
	    throw ConceptError(conceptName, "Missing section \"<e>\"");
	  catch IO(e):
	    throw ConceptError(conceptName, "IO error: <e>");
	  catch value e: 
	    throw ConceptError(conceptName, "Uncaught exception: <e>");
}

public void generate(Concept C, str synopsis, str html_synopsis, str html_body){
   cn = C.fullName;
   childs = children(C);
   questions = C.questions;
  
   html_code = html(
  	head(title(cn) + 
  	     meta("description", "<cn>: <synopsis>") +
  	     meta("keywords", "<cn>, Rascal, meta-programming, software analysis, software transformation") +
  	     prelude(rootname(cn))),
  	body(
  	  isExam ?  h1("Online Exam <basename(cn)>") + html_body
  	  
  	         :  table("container",
		  	        tr(tdid("tdlogo", "\<a id=\"tutorAction\" href=\"/index.html\"\><logo>\</a\>") +
		  	           tdid("tdsearch", searchBox(cn))) +
		  	        
		  	        tr(tdid("tdnav", getNavigationPanel(rootname(cn))) +
		  	  
		  	           tdid("tdconcept", div("conceptPane", 
		  	              section("Name", showConceptPath(cn)) +
		  	              html_synopsis +
		  	              ((isEmpty(childs)) ? "" : section("Details", "<for(ch <- childs){><refToResolvedConcept(ch, true)> &#032 <}>")) +
		  	              html_body +
		  	              editMenu(C)
		  	           ))
	  	       ))
  	)
   );
   
   html_file = htmlFile(cn);
   //println("Writing to <html_file>");
   try {
	     writeFile(html_file, html_code);
   }
   catch e: println("can not save file <html_file>"); // do nothing
	 
   if(size(C.questions) > 0){
	  qs = C.questions;
	  quest_file = questFile(isExam ? examsDir + "results" : courseDir, cn);
      try {
	       writeTextValueFile(quest_file, C.questions);
	  }
	  catch e: println("can not save file <quest_file>"); // do nothing
   }
   
   if(isExam){
      lock_file = lockFile(cn);
      try {
          writeFile(lock_file, restrictAccess());
      }
      catch e: println("can not write file <lock_file>"); // do nothing
   }
}

// Generate prelude of web page

public str prelude(str courseName){
  if(isExam)
	   return "\<link type=\"text/css\" rel=\"stylesheet\" href=\"/exam.css\"/\>
	  		 '\<script type=\"text/javascript\" src=\"/jquery-1.4.2.min.js\"\>\</script\>
	  		 '\<script type=\"text/javascript\" src=\"/exam.js\"\>\</script\>";
  else
	  return "\<link type=\"text/css\" rel=\"stylesheet\" href=\"/prelude.css\"/\>
	  		 '\<link type=\"text/css\" rel=\"stylesheet\" href=\"/jquery.autocomplete.css\"/\>
	  		 '\<script type=\"text/javascript\" src=\"/jquery-1.4.2.min.js\"\>\</script\>
	  		 '\<script type=\"text/javascript\" src=\"/jquery.colorbox-min.js\"\>\</script\>
	  		 '\<script type=\"text/javascript\" src=\"/jquery.cookie.js\"\>\</script\>
	         '\<script type=\"text/javascript\" src=\"/jquery.jstree.js\"\>\</script\>
	         '\<script type=\"text/javascript\" src=\"/jquery.autocomplete.js\"\>\</script\>
	         '\<script type=\"text/javascript\" src=\"/jquery.history.js\"\>\</script\>
	         '\<script type=\"text/javascript\" src=\"/globals.js\"\>\</script\>
	         '\<script type=\"text/javascript\" src=\"/prelude.js\"\>\</script\>
	         '\<script type=\"text/javascript\" src=\"/<courseName>/course.js\"\>\</script\>\n"
	         ;
}

public str jsCoursePrelude(str courseName, list[str] baseConcepts, map[ConceptName,Concept] concepts){  
  map[str, set[ConceptName]] searchIndex = ();
  for(name <- concepts){
      for(trm <- concepts[name].searchTerms)
          if(searchIndex[trm]?)            // TODO: concise form gives type error.
             searchIndex[trm] += {name};
          else
             searchIndex[trm] = {name};
  }
  sortedSearchKeys = sort(toList(domain(searchIndex)));
  return
  "/* Generated code for course <courseName> */
  '
  'var baseConcepts = <mkJsArray(baseConcepts, "new Array()")>;
  '
  'var conceptNames = <mkJsArray(sort(toList(domain(concepts))), "new Array()")>;
  '
  'var searchTerms = {};
  '
  '<for( trm <- sortedSearchKeys ){>
  'searchTerms[\"<escapeForJavascript(trm)>\"] = <mkJsArray(toList(searchIndex[trm]), "null")>;
  '<}>"; 
}

public str mkJsArray(list[str] elms, str nullCase){
  int n = size(elms);
  return (n > 0) ? "new Array(<for(int i <- [0 .. (n-1)]){><(i==0)?"":",">\"<escapeForJavascript(elms[i])>\"<}>)"
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

public str editMenu(Concept C){
  cn = C.fullName;
  warnings = "/<rootname(cn)>/warnings.html";
  n = size(C.warnings);
  msg = "";
  if(n == 1)
     msg = inlineError(" 1 warning in this concept");
  if(n > 1)
     msg = inlineError(" <n> warnings in this concept");
 
  return "\n\<a id=\"tutorAction\" href=\"/index.html\"\><logo>\</a\>" +
         "\<div id=\"editMenu\"\>" +
              "[\<a id=\"editAction\" href=\"/edit?concept=<cn>&new=false\"\>\<b\>Edit\</b\>\</a\>] | 
               [\<a id=\"newAction\" href=\"/edit?concept=<cn>&new=true\"\>\<b\>New Subconcept\</b\>\</a\>] |
               [\<a id=\"compileAction\" href=\"/compile?name=<rootname(cn)>\"\>\<b\>Recompile Course\</b\>\</a\>] |
               [\<a id=\"warnAction\" href=\"<warnings>\"\>\<b\>Warnings\</b\>\</a\>]"
          +    msg
          + "\</div\>\n"
          + "\<span class=\"editMenuFooter\"\>Is this page unclear, or have you spotted an error? Please add a comment below and help us to improve it. "
          + "For all other questions and remarks, visit \<a href=\"http://ask.rascal-mpl.org\"\>ask.rascal-mpl.org\</a\>. \</span\>";
}


public str restrictAccess() =
  "AuthType Basic
  'AuthName \"Unpublisched exam\"
  'AuthUserFile /srv/www/vhosts/exam.rascal-mpl.org/exampass
  'Require user rascal";

// --------------------- compile questions ---------------------------------

// Extract specific question type from Questions section

public list[str] getQuestions(str qtype, str questions){
  return [text | /<qtype>:<text:.*?>(\Z|[A-Z][a-z\-\_]+:)/s := questions];
}

// Extract specific answer type from a Question

public set[str] getAnswers(str atype, str question){
  return {text | /<atype>:\s*<text:.*>/ := question};
}

private str makeQname(str uname, int n){
  return (uname == "") ? "<n>" : substring(uname, 1, size(uname)-1);
}


// Extract all the questions from the Questions section
public list[Question] getAllQuestions(ConceptName cname, list[str] qsection){
   int nquestions = 1;
   nq = size(qsection);
   questions = [];
   int i = 0;
   while(i < nq){
     //println("getQuestions: <qsection[i]>");
     switch(qsection[i]){
       case /^[Qq][Tt]ext<uname:\[[A-Za-z0-9]+\]>?:\s*<question:.*>$/: {
 		  qname = makeQname(uname, nquestions);
         
          i += 1;
          set[str] answers = {};
          while(i < nq && /^a:\s*<text:.*>/ := qsection[i]){
            answers += toLowerCase(text);
            i += 1;
          }
          if(size(answers) == 0)
          	throw ConceptError(cname, "TextQuestion with no or malformed answers");
          
          questions += textQuestion(cname, qname, markup([question], cname), answers);
          nquestions += 1;
       }
       case /^[Qq][Cc]hoice<uname:\[[A-Za-z0-9]+\]>?:<question:.*>$/: {
          qname = makeQname(uname, nquestions);
          //println("qname = <qname>");
          i += 1;
          good_answers = [];
          bad_answers = [];
          while(i < nq && /^<prop:[gb]>:\s*<text:.*>/ := qsection[i]) {
            if(prop == "g")
               good_answers += text;
            else
               bad_answers += text;
            i += 1;
          }
          if(size(good_answers) == 0 || size(bad_answers) == 0)
          	throw ConceptError(cname, "ChoiceQuestion with insufficient or malformed answers");
         
         // println("<good_answers>, <bad_answers>");
      
          questions += choiceQuestion(cname, qname, markup([question], cname), [good(q) | q <- good_answers] + [bad(q) | q <- bad_answers]);
          nquestions += 1;
       }
 
      case /^[Qq][Vv]alue<uname:\[[A-Za-z0-9]+\]>?:\s*<cnd:.*>$/: {
           qname = makeQname(uname, nquestions);
           <i, q> = getTvQuestion(cname, valueOfExpr(), qname, qsection, i, cnd);
           questions += q;
           nquestions += 1;
      }
      
      case /^[Qq][Tt]ype<uname:\[[A-Za-z0-9]+\]>?:\s*<cnd:.*>$/: {
           qname = makeQname(uname, nquestions);
           <i, q> = getTvQuestion(cname, typeOfExpr(), qname, qsection, i, cnd);
           questions += q;
           nquestions += 1;
      }
      
      case /^[Qq][Uu]se<uname:\[[A-Za-z0-9]+\]>?:\s*<cpt:\S+>\s+<q:\S+>$/:{
           qname = makeQname(uname, nquestions);
           crs = basename(cname);
           if(/<crs1:[A-Za-z]+>:<rst:.+>$/ := cpt){
              crs = crs1;
              cpt = rst;
           }
           options = resolveConcept(crs, cpt);
           if(size(options) != 1){
              addWarning("Unknown or ambiguous concept <cpt>");
           } else {  
	           ucpid = options[0];
	           try {
	                uq = getQuestion(ucpid, q);
	                uq.fullName = cname;
	                uq.name = qname;
	                questions += uq;
	                nquestions += 1;
	           } catch: addWarning("No question <q> in <ucpid>");
	       }
           i += 1;
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

public tuple[int, Question] getTvQuestion(ConceptName cname, TVkind kind, str qname, list[str] qsection, int i, str cnd){
     //println("getTvQuestion: <cname>, <qname>");
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
     rtype = RascalType::\void();
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
	            throw ConceptError(cname, "Question <qname>: type of generated variable <name> is incorrect");
	        definedVars += name;	
	        i += 1; 
	      }
	      
	    case /^expr:\s*<name:[A-Za-z0-9]+>\s*\=\s*<expr:.*>$/:
	      { auxVars += <name, expr>;
	        u = uses(expr);
	        if(u - definedVars != {})
	           throw ConceptError(cname, "Question <qname>: expr uses undefined variables: <u - definedVars>");
	        definedVars += name;
	        usedVars += u;
	        i += 1; 
	      }
	      
	    case /^type:\s*<tp:.*>$/: {
	        rtype = RascalType::\void();
			try { rtype = parseType(tp); }
			catch:
			     throw ConceptError(cname, "Question <qname>: cannot parse type of expected type");
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
		      throw ConceptError(cname, "Question <qname>: has already a test <cnd>");
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
        throw ConceptError(cname, "Question <qname> should have at most one hole");
        
     if((lstBefore + lstAfter) == "" && holeInLst)
        throw ConceptError(cname, "Question <qname> has an empty listing with a hole");
        
     if((cndBefore + cndAfter) == "" && !(holeInLst))
        throw ConceptError(cname, "Question <qname> has no test");
        
     if(kind == typeOfExpr() && holeInCnd && rtype == RascalType::\void())
           throw ConceptError(cname, "Type question <qname> has condition with a hole and requires an expected type");
     
     if(usedVars - definedVars != {})
        throw ConceptError(cname, "Question <qname>: undefined variables <usedVars - definedVars>");
        
     if(definedVars - usedVars != {})
        throw ConceptError(cname, "Question <qname>: unused variables <definedVars - usedVars>");
        
     if(definedVars == {} && vars == [])
        try {
          vars = autoDeclare(cndBefore + cndAfter);
        } catch: throw ConceptError(cname, "Question <qname>: illegal type in test");

     return <i, tvQuestion(cname, qname, kind, details(markup([desc], cname), setup, lstBefore, lstAfter, cndBefore, cndAfter, holeInLst, holeInCnd, vars, auxVars, rtype, hint))>;
}


// --------------------------------- Question Presentation ---------------------------

// Present a Question



private str namePar(str q, str name) = "name=\"<escapeConcept(q)>:<name>\"";

private str answerFormBegin(ConceptName cpid, QuestionName qid, str formClass){
    validate = isExam ? "validateExam" : "validate";
    if(isExam)
       return "";
	return "\n\<form method=\"GET\" action=\"<validate>\" class=\"<formClass>\"\>";
}

private str answerFormEnd(str submitText, str submitClass){
  return "
\<input type=\"submit\" value=\"<submitText>\" class=\"<submitClass>\"\>
\</form\>";
}

private str anotherQuestionForm(ConceptName cpid, QuestionName qid){
    cp_qid = "<cpid>_<qid>";
	return answerFormBegin(cpid, qid, "anotherForm") + 
	"\<input type=\"hidden\" <namePar(cp_qid, "another")> value=\"yes\"\>\n" +
	answerFormEnd("I want another question", "anotherSubmit");
}

private str cheatForm(ConceptName cpid, QuestionName qid, str expr){
   cp_qid = "<cpid>_<qid>";
    return "";
	return answerFormBegin(cpid, qid, "cheatForm") + 
	       "\<input type=\"hidden\"  <namePar(cp_qid, "expr")> value=\"<expr>\"\>\n" +
           "\<input type=\"hidden\"  <namePar(cp_qid, "cheat")> value=\"yes\"\>\n" +
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

private str showStudentId(){
  return table("studentInfo",
               tr(td("Your name") +           td("\<input type=\"text\" size=\"30\" name=\"studentName\"   id=\"studentName\"\>")) +
               tr(td("Your email address")  + td("\<input type=\"text\" size=\"30\" name=\"studentMail\"   id=\"studentMail\"\>")) +
               tr(td("Your student number") + td("\<input type=\"text\" size=\"30\" name=\"studentNumber\" id=\"studentNumber\"\>"))
         );
}

public str showQuestionsSection(ConceptName conceptName, list[Question] questions){
  if(size(questions) == 0)
     return "";
  student = isExam ? showStudentId() : "";
  formBegin = isExam ? "\n\<form method=\"POST\" action=\"/validateExam\" class=\"examAnswerForm\"\><br()>" : "";
  idField   = isExam ? "\<input type=\"hidden\" name=\"examName\" id=\"examName\" value=\"<escapeConcept(conceptName)>\"\>" : "";
  submit = isExam ? answerFormEnd("Submit your answers", "examSubmit") : "";
  return div("questions",
             "<formBegin>
             '<idField>
             '<student><br()>
             '<sectionHead("Questions")> <br()>
             '<for(quest <- questions){><showQuestion(conceptName,quest)> <}><br()>
             '<submit><br()>"
             );
}

public str showQuestion(ConceptName cpid, Question q){
  //println("showQuestion: <cpid>, <q>");
  qid = q.name;
  qdescr = "";
  qexpr  = "";
  qform = "";
  cq = "<cpid>_<qid>";
  
  switch(q){
    case choiceQuestion(cid,qid, descr, choices): {
      qdescr = descr;     
      avail = index(choices);
      
      idx = [];
      bool oneGood = false;
      while(!oneGood || size(idx) < 3){
      	<k, avail> = takeOneFrom(avail);
      	if(good(_) := choices[k]){
      	   if(!oneGood){
      	      oneGood = true;
      	      idx = idx + [k];
      	   }
      	} else {
      	   idx += [k];
      	}
      }
      
      altcnt = 0;
      qform = "<for(int i <- index(idx)){>
              '\<input type=\"radio\" <namePar(cq,"answer")> id=\"<cq>_<idx[i]>\" value=\"<i>@<idx[i]>@<idx>\"\>
              '\<label for=\"<cq>_<idx[i]>\"\><choices[idx[i]].description> \</label\><br()><}>";      
    }
    case textQuestion(cid,qid,descr,replies): {
      qdescr = descr;
      qform = "\<input type=\"text\" size=\"60\" <namePar(cq,"answer")> class=\"answerText\"\>";
    }
    
    case tvQuestion(cid,qid, qkind, qdetails): {
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
        //println("tv: <name>, <tp>");
        tp1 = generateType(tp, env);
        env[name] = <tp1, generateValue(tp1, env)>;
        generatedVars += name;
	  }
	  //println("env = <env>");

	  for(<str name, str exp> <- auxVars){
         exp1 = subst(exp, env);
         //println("exp1 = <exp1>");
         try {
           tp = parseType("<evalType(setup + (exp1 + ";"))>");
           r = eval(setup + (exp1 + ";")).val;
           env[name] = <tp, "<r>">;
           //println("env[<name>] = <env[name]>");
         }
         catch ParseError(loc l):
	           throw "Parse error while computing <name> = <exp1> at line <l.begin.line>, column <l.begin.column>";
	     catch StaticError(str msg, loc l):
	           throw "Static error while computing <name> = <exp1>: <msg>, at line <l.begin.line>, column <l.begin.column>"; 
	     catch value x: 
	           throw "Something unexpected went wrong while computing <name> = <exp1>: Message: <x>";
      }
      //println("env = <env>");
      
      lstBefore = escapeForHtml(subst(lstBefore, env));
      lstAfter = escapeForHtml(subst(lstAfter, env));
      cndBefore = escapeForHtml(subst(cndBefore, env));
      cndAfter = escapeForHtml(subst(cndAfter, env));
      
      qform = "<for(param <- generatedVars){>\<input type=\"hidden\" <namePar(cq,param)> value=\"<escapeForHtml(env[param].rval)>\"\>\n<}>";
      
      qtextarea = "\<input type=\"text\" size=\"30\" <namePar(cq,"answer")> class=\"answerText\"\>";
      
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
  
  sep = "_";
  ecpid = escapeConcept(cpid);
  answerForm = answerFormBegin(ecpid, qid, "answerForm") + qform  + (!isExam ? answerFormEnd("Give answer", "answerSubmit") : "");

  return div("<ecpid><sep><qid>", "question",
                  b(basename("Question [" + qid + "]. ")) + 
                  ((!isExam) ? (status("good<sep><ecpid><sep><qid>", good()) + status("bad<sep><ecpid><sep><qid>", bad()) +
                                  "\n\<span id=\"answerFeedback<sep><ecpid><sep><qid>\" class=\"answerFeedback\"\>\</span\>\n")
                                : "") +
                  qdescr + br() + answerForm +
                  ((!isExam) ? (anotherQuestionForm(ecpid, qid) + cheatForm(ecpid, qid, qexpr))
                                : "") + 
                  br());
}

public QuestionName lastQuestion = "";

// trim layout from a string
public str trim (str txt){
    return txt;
	return
	  visit(txt){
	    case /[\ \t\n\r]/ => ""
	  }
}

public Question getQuestion(ConceptName cid, QuestionName qid){

  cid = unescapeConcept(cid);
  try {
  	quest_file = ((isExam ? examsDir + "results/" : courseDir) + cid + basename(cid))[extension = questExtension];
  	questions = readTextValueFile(#Questions, quest_file);
  
 	 for(q <- questions)
  		if(q.name == qid)
  			return q;
  } catch e: throw "Question file for <cid> not found";
  
  throw "Question <qid> not found";
}
