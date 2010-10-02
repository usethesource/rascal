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
import experiments::RascalTutor::HTMLUtils;
import experiments::RascalTutor::HTMLGenerator;
import experiments::RascalTutor::ValueGenerator;

public str mkConceptTemplate(ConceptName cn){
return "Name: <cn>\n\nDetails:\n\nCategories:\n\nSyntax:\n\nTypes:\n\nFunction:\n\nSynopsis:\n\nDescription:\n\nExamples:\n\nBenefits:\n\nPittfalls:\n\nQuestions:\n\n";
}

// Get a section from the concept description. Each starts with a capitalized keyword,e,g, "Description".
// Questions is the last section and is treated special: it contains questions that are analyzed later

public set[str] sectionKeywords = {"Name", "Details", "Categories", "Syntax", "Types", "Function", "Synopsis", "Description",
                                   "Examples", "Benefits", "Pittfalls", "Questions"};

private str conceptPath = "";

private str markup1(list[str] lines){
  return markup(lines, conceptPath);
}

public map[str,list[str]] getSections(list[str] script){
  sections = ();
  start = 0;
  currentSection = "";
  for(int i <- index(script)){
    if(/^<section:[A-Z][A-Za-z]*>:\s*<text:.*>/ := script[i] && section in sectionKeywords){
      if(currentSection != ""){
      	sections[currentSection] = trimLines(script, start, i);
      	//println("<currentSection> = <sections[currentSection]>");
      }
      if(/^\s*$/ := text)
         start = i + 1;       // no info following section header
      else {
        script[i] = text;    // remove the section header
        start = i;
      } 
      currentSection = section;
    }
  }
  if(currentSection != ""){
     sections[currentSection] = trimLines(script, start, size(script));
  }
  return sections;
}

public list[str] trimLines(list[str] lines, int start, int end){
  //println("trimlines(<size(lines)>,start=<start>,end=<end>");
  while(start < end && /^\s*$/ := lines[start])
    start += 1;
  while(end > start && /^\s*$/ := lines[end - 1])
    end -= 1;
  //println("slice(<start>,<end-start>)");
  if(start != end)
  	return slice(lines, start, end - start);
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

str compiledExtension = "concept.pre";

public Concept parseConcept(loc file, str coursePath){
   binFile = file[extension = compiledExtension];
   //println("binFile=<binFile>; <exists(binFile)>; <lastModified(binFile)>; <lastModified(file)>");
   if(exists(binFile) && lastModified(binFile) > lastModified(file)){
     //println(" reading concept from file ...");
     try {
        C = readTextValueFile(#Concept, binFile);
        return C;
     } catch: println("Reading <binFile> failed, regenerating it");
   }
   return parseConcept(file, readFileLines(file), coursePath);
}

public Concept parseConcept(loc file, list[str] script, str coursePath){
   return parseConcept(file, getSections(script), coursePath);
}

public Concept parseConcept(loc file, map[str,list[str]] sections, str coursePath){

   //println("parseConcept: file=<file>, coursePath=<coursePath>");
   
   if(!(sections["Name"]?))
      throw ConceptError("<file>: Missing section \"Name\"");
      
   name = sections["Name"][0];
   fullName = getFullConceptName(file.path, coursePath);
   try {
	         
	   if(name != basename(fullName))
	      throw ConceptError("Got concept name \"<name>\", but \"<basename(fullName)>\" is required");
	      
	   conceptPath = "Courses/" + fullName;
//     conceptPath = fullName;
	   
	   optDetails      	= getNames(sections["Details"] ? []);
	   optCategories   	= toSet(getNames(sections["Categories"] ? []));
	 
	   syntaxSection 	= sections["Syntax"] ? [];
	   typesSection 	= sections["Types"] ? [];
	   functionSection 	= sections["Function"] ? [];
	   synopsisSection 	= sections["Synopsis"] ? [];
	   searchTerms  	= searchTermsSynopsis(syntaxSection, typesSection, functionSection, synopsisSection);
       syntaxSynopsis   = markup1(syntaxSection);
       typesSynopsis    = markup1(typesSection);
       functionSynopsis = markup1(functionSection);
	   synopsis 		= markup1(synopsisSection);
	   description		= markup1(sections["Description"]);
	   examples 		= markup1(sections["Examples"]);
	   benefits 		= markup1(sections["Benefits"]);
	   pittfalls 		= markup1(sections["Pittfalls"]);
	   questions 		= getAllQuestions(name, sections["Questions"]);
	   
	   related = getAndClearRelated();
	   warnings = getAndClearWarnings();
	   
	   Concept C = concept(name, file, warnings, optDetails, optCategories, related, synopsis,
	                       syntaxSynopsis, typesSynopsis, functionSynopsis, 
	                       searchTerms, description, examples, benefits, pittfalls, questions);
	   binFile = file[extension = compiledExtension];
	   writeTextValueFile(binFile, C);
	   return C;
	} catch NoSuchKey(e):
	    throw ConceptError("<fullName>: Missing section \"<e>\"");
	  catch IOError(e):
	    throw ConceptError("<fullName>: <e>");
	  catch e: 
	    throw ConceptError("<fullName>: uncaught exception <e>");
}

// Extract categories

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
          questions += textQuestion("<nquestions>", markup1([question]), answers);
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
      
          questions += choiceQuestion("<nquestions>", markup1([question]), choices);
          nquestions += 1;
       }
 
      case /^QValue:\s*<cnd:.*>$/: {
           <i, q> = getTvQuestion(valueOfExpr(), "<nquestions>", qsection, i, cnd);
           questions += q;
           nquestions += 1;
      }
      
      case /^QType:\s*<cnd:.*>$/: {
           <i, q> = getTvQuestion(typeOfExpr(), "<nquestions>", qsection, i, cnd);
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

public tuple[int, Question] getTvQuestion(TVkind kind, str name, list[str] qsection, int i, str cnd){
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

     return <i, tvQuestion(name, kind, details(markup1([desc]), setup, lstBefore, lstAfter, cndBefore, cndAfter, holeInLst, holeInCnd, vars, auxVars, rtype, hint))>;
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

public Course recompileCourse(Course course){
  return validatedCourse(course.root, course.title, course.directory, course.concepts);
}
   
public Course compileCourse(ConceptName rootConcept, str title, loc courseDir){
    map[ConceptName,Concept] conceptMap = ();
    coursePath = courseRoot.path;
    courseFiles = crawl(catenate(courseDir, rootConcept), suffix);
    for(file <- courseFiles){
       cpt = parseConcept(file, coursePath);
       fullName = getFullConceptName(file.path, coursePath);
       if(conceptMap[fullName]?)
       	  println("Double declaration for <fullName>");
       conceptMap[fullName] = cpt;       	 
    }
    return validatedCourse(rootConcept, title, courseDir, conceptMap);
}

public Course validatedCourse(ConceptName rootConcept, str title, loc courseDir, map[ConceptName,Concept] conceptMap){
    // Global sanity checks on concept dependencies
    Graph[ConceptName] fullRefinements = {};
    Graph[ConceptName] baseRefinements = {};
    set[str] categories = {};
    
    for(cn <- conceptMap){
       // println("cn = <cn>");
       baseRefinements += getBaseRefinements(basenames(cn));
       fullRefinements += getFullRefinements(basenames(cn));
       categories += conceptMap[cn].categories;
    }
    
    generalizations = invert(baseRefinements);
    println("baseRefinements = <baseRefinements>");
    println("fullRefinements = <fullRefinements>");
    
    allBaseConcepts = carrier(baseRefinements);
    allFullConcepts = carrier(fullRefinements);
    
    println("allBaseConcepts=<allBaseConcepts>");
     println("allFullConcepts=<allFullConcepts>");
    
    undefinedFullConcepts =  allFullConcepts - domain(conceptMap);
    
    warnings = [];
    if(!isEmpty(undefinedFullConcepts))
    	warnings += "Undefined concepts: <undefinedFullConcepts>";
    roots = top(baseRefinements);
    if(size(roots) != 1)
        warnings += "Root is not unique: <roots>";
    if(roots != {rootConcept})
        warnings += "Roots <roots> unequal to course name \"<rootConcept>\"";
    
    map[str, ConceptName] fullRelated = ();
    set[str] searchTerms = {};
    for(cname <- conceptMap){
       C = conceptMap[cname];
       searchTerms += C.searchTerms;
       for(r <- C.related){
         //println("related.r = <r>");
         rbasenames = basenames(r);
         if(!(toSet(rbasenames) <= allBaseConcepts))
         	warnings += "<showConceptPath(cname)>: unknown concept \"<r>\"";
         else {
            parents = generalizations[rbasenames[0]];
            if(size(parents) > 1)
               warnings += "<showConceptPath(cname)>: ambiguous concept \"<rbasenames[0]>\", add one of the parents <parents>";
            if(size(rbasenames) >= 2){
               for(int i <- [0 .. size(rbasenames)-2]){
                   if(<rbasenames[i], rbasenames[i+1]> notin baseRefinements)
                      warnings += "<showConceptPath(cname)>: unknown concept \"<rbasenames[i]>/<rbasenames[i+1]>\"";
               } // for
            } // if
            fullPath = shortestPathPair(baseRefinements, rootConcept, last(rbasenames));
            //println("fullPath = <fullPath>");
            fullRelated[r] = compose(fullPath);
         } // else
       } // for(r <-
    } // for(cname
    
    for(cname <- conceptMap){
       C = conceptMap[cname];
       for(d <- C.details){
         if((cname + "/" + d) notin fullRefinements[cname])
            warnings += "<showConceptPath(cname)>: non-existent detail \"<d>\"";
       }
       for(w <- C.warnings)
         warnings += "<showConceptPath(cname)>: <w>";
    }
    
    // Map same search term with/without capitalization to the one with capitalization
    searchTerms1 = {};
    for(trm <- searchTerms){
        if(/^<S:[a-z]><tail:[A-Za-z0-9]*>$/ := trm){
           if((toUpperCase(S) + tail) notin allBaseConcepts)
              searchTerms1 += {trm};
        } else 
          searchTerms1 += {trm};
   }               
    
   // println("fullRelated = <fullRelated>");
   // println("searchTerms1= <searchTerms1>");
   // println("extended allBaseConcepts: <sort(toList(allBaseConcepts + searchTerms))>");
   // println("Warnings:\n<for(w <- warnings){><w>\n<}>");
    return course(title, courseDir, rootConcept, warnings, conceptMap, fullRefinements, sort(toList(allBaseConcepts + searchTerms1)), fullRelated, categories);
}

public loc catenate(loc basedir, str entry){
   baseuri = basedir.uri;
   if(!endsWith(baseuri, "/"))
   	baseuri += "/";
   return basedir[uri=baseuri + entry];
}

public list[loc] crawl(loc dir, str suffix){
  //println("crawl: <dir>, <listEntries(dir)>");
  list[loc] res = [];
  for( str entry <- listEntries(dir) ){
    loc sub = catenate(dir, entry);
    if(endsWith(entry, suffix))
      	res += [sub];
    if(isDirectory(sub))
      	res += crawl(sub, suffix);
  };
  return res;
}

// --- Fo testing purposes:

public Concept tst(){
   return parseConcept(|cwd:///src/org/rascalmpl/library/experiments/RascalTutor/Courses/Rascal/Expressions/Values/Set/Intersection/Intersection.concept|, "/org/rascalmpl/library/experiments/RascalTutor/Courses/");
}

public void cc(){
   c = compileCourse("Rascal", "Rascal Tutorial", courseRoot);
}

public str tst2(){
   l = |cwd:///src/org/rascalmpl/library/experiments/RascalTutor/Courses/xxx|;
   writeTextValueFile(l, "a b c d");
   s = readTextValueFile(#str, l);
   return s;
}