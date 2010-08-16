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
import experiments::RascalTutor::HTMLGenerator;
import experiments::RascalTutor::ValueGenerator;

public str mkConceptTemplate(ConceptName cn){
return "Name: <cn>\n\nRelated:\n\nSynopsis:\n\nDescription:\n\nExamples:\n\nBenefits:\n\nPittfalls:\n\nQuestions:\n\n";
}

//parseConcept(|stdlib:///org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Language/Expression/Number/max.concept|, courseRoot);

// Get a section from the concept description. Each starts with a capitalized keyword,e,g, "Description".
// Questions is the last section and is treated special: it contains questions that are analyzed later

public str sectionKeyword = "(?:Name|Related|Synopsis|Description|Examples|Benefits|Pittfalls|Questions)";

public list[str] getSection(str section, list[str] script){
  n = size(script);
  res = [];
  bool insection = false;
  for(int i <- [0 .. n-1]){
     if(insection){
        if(/^<sectionKeyword>:/ := script[i])
           return res;
        res += script[i];
     } else if(/^<section>:<text:.*>/ := script[i]){
     	insection = true;
     	res += text;
     }
  }
  if(insection)
    return res;
  throw "Section <section> not found";
}

public str combine(list[str] lines){
  return "<for(str s <- lines){><s>\n<}>";
}

// Strip leading and trailing whitespace

public str trim(str text){
  return (/\s*<body:.*>\s*/ := text) ? body : text;
}

public list[str] splitLines(str text){
 text = visit(text) { case /\r/ => "" };
 if(!endsWith(text, "\n"))
 	text += "\n";
   return for(/<line:.*>\n/ := text)
 	          append line;
}

public Concept parseConcept(loc file, str coursePath){
   return parseConcept(file, readFileLines(file), coursePath);
}
   
public Concept parseConcept(loc file, list[str] script, str coursePath){

   println("parseConcept: script = ***<script>***");
   name 		= trim(combine(getSection("Name", script)));
   related 		= getPath(combine(getSection("Related", script)));
   synopsisSection = getSection("Synopsis", script);
   rawSynopsis  = combine(synopsisSection);
   searchTerms =  searchTermsSynopsis(synopsisSection);
   synopsis 	= markupSynopsis(synopsisSection);
   description	= markup(getSection("Description", script));
   examples 	= markup(getSection("Examples", script));
   benefits 	= markup(getSection("Benefits", script));
   pittfalls 	= markup(getSection("Pittfalls", script));
   questions 	= getAllQuestions(name, getSection("Questions", script));
   
   fullName = getFullConceptName(file.path, coursePath);
         
   if(name != basename(fullName))
      throw "Got concept name \"<name>\", but \"<basename(fullName)>\" is required";
   
   return concept(name, file, related, synopsis, rawSynopsis, searchTerms, description, examples, benefits, pittfalls, questions);
}

// Extract the path named from a Related section
public list[str] getPath(str related){
   return [ path | /<path:[A-Za-z\-\_\/]+>/ := related];
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
     println("getQuestions: <qsection[i]>");
     switch(qsection[i]){
       case /^Text:<question:.*>$/: {
          i += 1;
          set[str] answers = {};
          while(i < n && /^a:\s*<text:.*>/ := qsection[i]){
            answers += text;
            i += 1;
          }
          if(size(answers) == 0)
          	throw "TextQuestion with no or malformed answers";
          questions += textQuestion("<cname><nquestions>", markup([question]), answers);
          nquestions += 1;
       }
     case /^Choice:<question:.*>$/: {
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
          	throw "ChoiceQuestion with insufficient or malformed answers";
          	
          choices = [good(g) | g <- good_answers] + [bad(b) | b <- bad_answers];
          questions += choiceQuestion("<cname><nquestions>", markup([question]), choices);
          nquestions += 1;
       }
       
     case /^Type:\s*<tp:.*>\s*$/: {
          setup = (size(tp) != 0) ? [tp] : [];
          i += 1;
          while(i < n && /^[A-Z][A-Za-z]+:/ !:= qsection[i] ){
             if(/\S+/ := qsection[i])
             	setup += qsection[i];
             i += 1;
          }
          try {
          println("Type question: tp = <tp>, setup = <setup>");
            tp = last(setup);
            setup = head(setup, size(setup)-1);
            parseType(tp); // to make sure it is correct
         	questions += typeQuestion("<cname><nquestions>", "", setup, tp);
         	nquestions += 1;
          } catch:
            throw "TypeQuestion: syntax error in type specification <tp>";
      }
      
      case /^Expr:\s*<expr:.*>$/: {
          setup = (size(expr) != 0) ? [expr] : [];
          i += 1;
          while(i < n && /^[A-Z][A-Za-z]+:/ !:= qsection[i] ){
             if(/\S+/ := qsection[i])
             	setup += qsection[i];
             i += 1;
          }
          try {
            println("Expr question: expr = <expr>, setup = <setup>");
            expr = last(setup);
            setup = head(setup, size(setup)-1);
            // TODO: to make sure it is correct
         	questions += exprQuestion("<cname><nquestions>", "", setup, expr);
         	nquestions += 1;
          } catch:
            throw "ExprQuestion: syntax error in expression";
      }
      
      case /^ExprType:\s*<expr:.*>$/: {
          setup = (size(expr) != 0) ? [expr] : [];
          i += 1;
          while(i < n && /^[A-Z][A-Za-z]+:/ !:= qsection[i] ){
             if(/\S+/ := qsection[i])
             	setup += qsection[i];
             i += 1;
          }
          try {
            // TODO: to make sure it is correct
            println("ExprType question: expr = <expr>, setup = <setup>");
            expr = last(setup);
            setup = head(setup, size(setup)-1);
         	questions += exprTypeQuestion("<cname><nquestions>", "", setup, expr);
         	nquestions += 1;
          } catch:
            throw "ExprQuestion: syntax error in expression";
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
       println("fullName = <fullName>");
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
    
    for(cn <- conceptMap){
       // println("cn = <cn>");
       baseRefinements += getBaseRefinements(basenames(cn));
       fullRefinements += getFullRefinements(basenames(cn));
    }
    
    generalizations = invert(baseRefinements);
    println("baseRefinements = <baseRefinements>");
    println("fullRefinements = <fullRefinements>");
    
    allBaseConcepts = carrier(baseRefinements);
    allFullConcepts = carrier(fullRefinements);
    
    println("allBaseConcepts=<allBaseConcepts>");
     println("allFullConcepts=<allFullConcepts>");
    
    undefinedFullConcepts =  allFullConcepts - domain(conceptMap);
    
    if(!isEmpty(undefinedFullConcepts))
    	println("*** Undefined concepts: <undefinedFullConcepts>");
    roots = top(baseRefinements);
    if(size(roots) != 1)
        println("Root is not unique: <roots>");
    if(roots != {rootConcept})
        println("Roots = <roots> unequal course name <rootConcept>");
    
    map[str, ConceptName] fullRelated = ();
    set[str] searchTerms = {};
    for(cname <- conceptMap){
       C = conceptMap[cname];
       searchTerms += C.searchTerms;
       for(r <- C.related){
         println("related.r = <r>");
         rbasenames = basenames(r);
         if(!(toSet(rbasenames) <= allBaseConcepts))
         	println("*** <cname>: unknown related concept <r>");
         else {
            parents = generalizations[rbasenames[0]];
            if(size(parents) > 1)
               println("*** <cname>: ambiguous related concept <rbasenames[0]>, choose from <parents>");
            if(size(rbasenames) >= 2){
               for(int i <- [0 .. size(rbasenames)-2]){
                   if(<rbasenames[i], rbasenames[i+1]> notin baseRefinements)
                      println("*** <cname>: related concept contains non-existing refinement <rbasenames[i]>/<rbasenames[i+1]>");
               } // for
            } // if
            fullPath = shortestPathPair(baseRefinements, rootConcept, last(rbasenames));
            println("fullPath = <fullPath>");
            fullRelated[r] = compose(fullPath);
         } // else
       } // for(r <-
    } // for(cname
    
    println("fullRelated = <fullRelated>");
    println("searchTerms= <searchTerms>");
    println("extended allBaseConcepts: <sort(toList(allBaseConcepts + searchTerms))>");
    return course(title, courseDir, rootConcept, conceptMap, fullRefinements, sort(toList(allBaseConcepts + searchTerms)), fullRelated);
}

public loc courseRoot = |file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/RascalTutor/Courses/|;

public loc catenate(loc basedir, str entry){
   baseuri = basedir.uri;
   if(!endsWith(baseuri, "/"))
   	baseuri += "/";
   return basedir[uri=baseuri + entry];
}

public list[loc] crawl(loc dir, str suffix){
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

public Course cc(){
   c = compileCourse("Rascal", "Rascal Tutorial", courseRoot);
   writeTextValueFile(|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/RascalTutor/Courses/Rascal/Rascal.course|, c);
   println(c);
   return c; 
}
