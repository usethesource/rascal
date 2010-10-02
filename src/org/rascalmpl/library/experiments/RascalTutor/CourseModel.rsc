module experiments::RascalTutor::CourseModel

import Graph;
import List;
import IO;
import String;
import Exception;

public loc courseRoot = |cwd:///src/org/rascalmpl/library/experiments/RascalTutor/Courses/|;

// A ConceptName is the "pathname" of a concept in the concept hierarchy, e.g., "Rascal/Datastructure/Set"

alias ConceptName = str;

// A QuestionName is similar to a ConceptName, but extended with a suffix, e.g., "Rascal/Datastructure/Set.1"
alias QuestionName = str;

// A Course captures all the information for the run-time execution of a course by the Tutor.

data Course = 
     course(str title,                                // Title to be displayed
			loc directory,                            // Directory where source files reside
			ConceptName root,                         // Name of the root concept
			list[str] warnings,                       // List of course compiler warnings
			map[ConceptName,Concept] concepts,        // Mapping ConceptNames to their description
			rel[ConceptName,ConceptName] refinements, // Tree structure of concept refinements
			list[str]  baseConcepts,                  // List of baseConcepts (e.g. names that occur on path of
			                                          // of some ConceptName)
			map[str,ConceptName] related,             // Mapping abbreviated concept names to full ConceptNames
            set[str] categories                       // Categories used in all concepts
     );

data Concept = 
	 concept(ConceptName name,                     	// Name of the concept
			loc file,                             	// Its source file
			list[str] warnings,                     // Explicit warnings in concept text
			list[ConceptName] details,              // Optional (ordered!) list of details
			set[str] categories,                    // Categories it belongs to
			set[ConceptName] related,            	// Set of related concepts (abbreviated ConceptNames)
			str synopsis,                         	// Text of the various sections
			str syntaxSynopsis,
			str typesSynopsis,
			str functionSynopsis,
			set[str] searchTerms,
			str description,
			str examples,
			str benefits,
			str pittfalls,
			list[Question] questions              	// List of Questions 
	);
        		
data Question = choiceQuestion(QuestionName name, str descr, list[Choice] choices)
              | textQuestion(QuestionName name, str descr, set[str] replies)
              | tvQuestion(QuestionName name, TVkind kind, TVdetails details)
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
     | \dateTime()
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

public str suffix = ".concept";

public str getFullConceptName(str path, str coursePath){
   if(/^.*<coursePath><cpath:.*$>/ := path && /^<full:.*>\.concept$/ := cpath){
      base = basename(full);
      bb = "<base>/<base>";
      if(endsWith(full, bb))
         full = replaceFirst(full, bb, base);
      return full;
    }
    throw "Malformed path <path>";  
}

// Get the basename from a ConceptName, eg 
// - basename("A/B/C") => "C"

public str basename(ConceptName cn){
  return (/^.*\/<base:[A-Za-z0-9\-\_]+>$/ := cn) ? base : cn;
}

test basename("A/B/C") == "C";

// Get all the names in a ConceptName

public list[str] basenames(ConceptName cn){
  names = [base | /<base:[A-Za-z0-9\-\_]+>/ := cn];
  n = size(names);
  // remove duplication due to main concept in directory e.g. C/C.concept
  if(n >= 2 && names[n-1] == names[n-2])
     	names = head(names, n-1);
  return names;
}

test basenames("A") == ["A"];
test basenames("A/B/C") == ["A", "B", "C"];

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
