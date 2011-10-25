@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

module experiments::RascalTutor::CourseManager

// The CourseManager handles all requests from the web server:
// - compile: compile a course
// - edit: edit a concept
// - save: save a concept after editing
// - validateAnswer: validates the answer to a specific question

import List;
import String;
import Integer;
import Graph;
import Set;
import Map;
import experiments::RascalTutor::CourseModel;
import experiments::RascalTutor::HTMLUtils;
import experiments::RascalTutor::ValueGenerator;
import experiments::RascalTutor::CourseCompiler;
import ValueIO;
import IO;
import Scripting;

// Show a concept.

public str showConcept(Concept C){
   html_file = C.file[extension = htmlExtension];
   return readFile(html_file);
}

// ------------------------------------ Compiling ------------------------------------------------

// Compile a concept
// *** called from Compile servlet in RascalTutor

public str compile(ConceptName rootConcept){
  if(rootConcept in listEntries(courseDir)){
     crs = compileCourse(rootConcept);
     return showConcept(crs.concepts[rootConcept]);
  } else
     throw "Course <rootConcept> not found";
}

// ------------------------------------ Editing --------------------------------------------------

// Edit a concept
// *** called from Edit servlet in RascalTutor

public str edit(ConceptName cn, bool newConcept){
 
  str content = "";
  if(newConcept){
    content = mkConceptTemplate("");
  } else {
    file = conceptFile(cn);
  	content = escapeForHtml(readFile(file));  //TODO: IO exception (not writable, does not exist)
  }
  return html(head(title("Editing <cn>") + prelude(rootname(cn))),
              body(
               div("conceptPane",
                 div("editArea",
                    "\<form method=\"POST\" action=\"/save\" id=\"editForm\"\>
                    \<textarea rows=\"15\" cols=\"60\" wrap=\"physical\" name=\"newcontent\" id=\"editTextArea\"\><content>\</textarea\>
                    \<input type=\"hidden\" name=\"concept\" value=\"<cn>\"\> \<br /\>
                    \<input type=\"hidden\" name=\"new\" value=\"<newConcept>\"\> \<br /\>
                    \<div id=\"editErrors\"\>errors\</div\>\n
                    \<input type=\"submit\" id=\"saveButton\" value=\"Save\"\>
                    \<div id=\"pleaseWaitMessage\"\>\<img src=\"/Courses/images/loader-light.gif\" width=\"16\" height=\"16\" /\> Please hold while we process the changes in the concept.\</div\>
                    \</form\>"
                  ))
             ));
}

// Save a concept
// *** called from servlet Edit in RascalTutor

public str save(ConceptName cn, str text, bool newConcept){
  if(newConcept) {
     lines = splitLines(text);
     sections = getSections(lines);
     cname = sections["Name"][0];
     if(/[^A-Za-z0-9]/ := cname)
       return saveFeedback("Name \"<cname>\" is not a proper concept name", "");
     fullName = cn + "/" + cname;
     	
     // Does the file for this concept already exist as a subconcept?
     if(exists(catenate(courseDir, fullName)))
     	return saveFeedback("Concept <fullName> exists already", "");
     
     // We have now the proper file name for the new concept and process it
     file = courseDir + fullName + "<cname>.<conceptExtension>";

     println("Write to file <file>");
     writeFile(file, combine(lines));
     
     try {
       c = compileAndGenerateConcept(file, true);
       return saveFeedback("", showConcept(c));
     } catch CourseError(e): {
       return saveFeedback(e, "");
     }
  } else {
    // Saving an existing concept
    try {
      file = conceptFile(cn);
      println("saving to <file> modified concept file.");
      writeFile(file, text);
     
      c = compileAndGenerateConcept(file, false);
 
      return saveFeedback(showConcept(c), "");
    } catch ConceptError(e): {
       return saveFeedback(e, "");
    }
    println("Other error");
    return showConcept(cn);
  }
}

// ------------------------------------ Question Handling ----------------------------------------

public set[QuestionName] goodAnswer = {};
public set[QuestionName] badAnswer = {};

// Validate an answer, also handles the requests: "cheat" and "another"
// *** called from servlet Edit in RascalTutor

public str validateAnswer(map[str,str] params){
    ConceptName cpid = params["concept"];
    QuestionName qid = params["exercise"];
    
    answer = trim(params["answer"]) ? "";
    expr = params["exp"] ? "";
    cheat = params["cheat"] ? "no";
	another = params["another"] ? "no";
	
	lastQuestion = qid;
	q = getQuestion(cpid, qid);
	
	println("Validate: <params>");
	println("Validate: <q>");
	if(cheat == "yes")
	   return showCheat(cpid, qid, q, params);
	if(another == "yes")
	   return showAnother(cpid, qid, q);
	   
	switch(q){
      case choiceQuestion(qid,descr,choices): {
        try {
           int c = toInt(answer);
           return (good(_) := choices[c]) ? correctAnswer(cpid, qid) : wrongAnswer(cpid, qid, "");
        } catch:
           return wrongAnswer(cpid, qid);
      }
      
      case textQuestion(qid,descr,replies):
        return (answer in replies) ? correctAnswer(cpid, qid) : wrongAnswer(cpid, qid, "");
 
      case tvQuestion(qid, qkind, qdetails): {
        setup  = qdetails.setup;
        lstBefore = qdetails.lstBefore;
        lstAfter  = qdetails.lstAfter;
        cndBefore = qdetails.cndBefore;
        cndAfter  = qdetails.cndAfter;
        holeInLst = qdetails.holeInLst;
        holeInCnd = qdetails.holeInCnd;
        vars   = qdetails.vars;
        auxVars = qdetails.auxVars;
        rtype = qdetails.rtype;
        hint = qdetails.hint;
        
        println("qdetails = <qdetails>");
        
        VarEnv env = ();
        generatedVars = [];
        for(<name, tp> <- vars){
          env[name] = <parseType(evalType(params[name] + ";")), params[name]>;
          generatedVars += name;
	    }
  
	    for(<name, exp> <- auxVars){
          exp1 = subst(exp, env) + ";";
          println("exp1 = <exp1>");
          env[name] = <parseType("<evalType(setup + exp1)>"), "<eval(setup + exp1)>">;
        }
        
        lstBefore = subst(lstBefore, env);
	    lstAfter = subst(lstAfter, env);
	    cndBefore = subst(cndBefore, env);
	    cndAfter = subst(cndAfter, env);
          
        switch(qkind){
          case valueOfExpr(): {
	        try {
	            if(lstBefore + lstAfter == ""){
	              println("YES!");
	              if(holeInCnd){
	                 computedAnswer = eval(setup + (cndBefore + answer + cndAfter + ";"));
	                 if(computedAnswer == true)
	                   return correctAnswer(cpid, qid);
	                 wrongAnswer(cpid, qid, hint);
	              } else {
	                 println("YES2");
	                 if(!endsWith(cndBefore, ";"))
	                   cndBefore += ";";
	                 computedAnswer = eval(setup + cndBefore);
	                 if(!endsWith(answer, ";"))
	                    answer += ";";
	                 givenAnswer = eval(setup + answer);
	                 if(computedAnswer == givenAnswer)
	                   return correctAnswer(cpid, qid);
	                 return wrongAnswer(cpid, qid, "I expected <computedAnswer>.");
	               } 
	            }
	            validate = (holeInLst) ? lstBefore + answer + lstAfter + cndBefore	             
	                                     : ((holeInCnd) ? lstBefore + cndBefore + answer + cndAfter
	                                                    : lstBefore + cndBefore + "==" + answer);
	            
	            println("Evaluating validate: <validate>");
	            output =  shell(setup + validate);
	            println("result is <output>");
	            
	            a = size(output) -1;
	            while(a > 0 && startsWith(output[a], "cancelled") ||startsWith(output[a], "rascal"))
	               a -= 1;
	               
	            errors = [line | line <- output, /[Ee]rror/ := line];
	            
	            if(size(errors) == 0 && cndBefore == "")
	               return correctAnswer(cpid, qid);
	               
	            if(size(errors) == 0 && output[a] == "bool: true")
	              return correctAnswer(cpid, qid);
	            if(hint != ""){
	               return wrongAnswer(cpid, qid, "I expected <subst(hint, env)>.");
	            }
	            //if(!(holeInLst || holeInCnd)){
	           //    return wrongAnswer(cpid, qid, "I expected <eval(subst(cndBefore, env))>.");
	           // }  
	            return wrongAnswer(cpid, qid, "I have no expected answer for you.");
	          } catch:
	             return wrongAnswer(cpid, qid, "Something went wrong!");
	      }

          case typeOfExpr(): {
	          try {
	            if(lstBefore == ""){ // Type question without listing
	               answerType = answer;
	               expectedType = "";
	               errorMsg = "";
	               if(holeInCnd){
	                  validate = cndBefore + answer + cndAfter;
	                  println("Evaluating validate: <validate>");
	                  answerType = evalType(setup + validate);
	                  expectedType = toString(generateType(rtype, env));
	               } else
	                  expectedType = evalType(setup + cndBefore);
	                  
	               println("answerType is <answerType>");
	               println("expectedType is <expectedType>");
	               if(answerType == expectedType)
	              		return correctAnswer(cpid, qid);
	              errorMsg = "I expected the answer <expectedType> instead of <answerType>.";
	              if(!holeInCnd){
	                 try parseType(answer); catch: errorMsg = "I expected the answer <expectedType>; \"<answer>\" is not a legal Rascal type.";
	              }
	              return  wrongAnswer(cpid, qid, errorMsg);
	            } else {   // Type question with a listing
	              validate = (holeInLst) ? lstBefore + answer + lstAfter + cndBefore	             
	                                     : ((holeInCnd) ? lstBefore + cndBefore + answer + cndAfter
	                                                    : lstBefore + cndBefore);
	            
	              println("Evaluating validate: <validate>");
	              output =  shell(setup + validate);
	              println("result is <output>");
	              
	              a = size(output) -1;
	              while(a > 0 && startsWith(output[a], "cancelled") ||startsWith(output[a], "rascal"))
	                 a -= 1;
	                 
	              expectedType = toString(generateType(rtype, env));
	              
	              errors = [line | line <- output, /[Ee]rror/ := line];
	              println("errors = <errors>");
	               
	              if(size(errors) == 0 && /^<answerType:.*>:/ := output[a]){
	                 println("answerType = <answerType>, expectedType = <expectedType>, answer = <answer>");
	                 ok = ((holeInLst || holeInCnd) ? answerType : answer) == expectedType;
	                 if(ok)
	                    return correctAnswer(cpid, qid);
	                    
	                 errorMsg = "I expected the answer <expectedType> instead of <answerType>.";
	                 if(!holeInCnd){
	                    try parseType(answer); catch: errorMsg = "I expected the answer <expectedType>; \"<answer>\" is not a legal Rascal type.";
	                 }
	                 wrongAnswer(cpid, qid, errorMsg);
	              }
	              
	              errorMsg = "";
	              for(error <- errors){
	                   if(/Parse error/ := error)
	                      errorMsg = "There is a syntax error in your answer. ";
	              }
	              if(errorMsg == "" && size(errors) > 0)
	                 errorMsg = "There is an error in your answer. ";
	                 
	              errorMsg += (holeInLst) ? "I expected a value of type <expectedType>. "
	                                      : "I expected the answer <expectedType>. ";
	                                      
	              if(!(holeInCnd || holeInLst)){
	                 try parseType(answer); catch: errorMsg = "Note that \"<answer>\" is not a legal Rascal type.";
	              }
	            
	              return  wrongAnswer(cpid, qid, errorMsg);
	            }
	          } catch:
	             return wrongAnswer(cpid, qid, "Cannot assess your answer.");
	      }
	    }
      }
    }
    throw wrongAnswer(cpid, qid, "Cannot validate your answer");
}

public str showCheat(ConceptName cpid, QuestionName qid, Question q, map[str,str] params){
   switch(q){
      case choiceQuestion(qid,descr,choices): {
        gcnt = 0;
        for(ch <- choices)
           if(good(txt) := ch)
           	  gcnt += 1;
        plural = (gcnt > 1) ? "s" : "";
        return cheatAnswer(cpid, qid, "The expected answer<plural>: <for(ch <- choices){><(good(txt) := ch)?txt:""> <}>");
      }
      
      case textQuestion(qid,descr,replies): {
        plural = (size(replies) > 1) ? "s" : "";
        return cheatAnswer(cpid, qid, "The expected answer<plural>: <for(r <- replies){><r> <}>");
      }
      
      case tvQuestion(qid, qkind, qdetails): {
        setup  = qdetails.setup;
        lstBefore = qdetails.lstBefore;
        lstAfter  = qdetails.lstAfter;
        cndBefore = qdetails.cndBefore;
        cndAfter  = qdetails.cndAfter;
        holeInLst = qdetails.holeInLst;
        holeInCnd = qdetails.holeInCnd;
        vars   = qdetails.vars;
        auxVars = qdetails.auxVars;
        rtype = qdetails.rtype;
        hint = qdetails.hint;
        
        switch(qkind){
          case valueOfExpr():
            return cheatAnswer(cpid, qid, "The expected answer: <hint>");
          
          case typeOfExpr():
            return cheatAnswer(cpid, qid, "The expected answer: <rtype>");
        }
      }
    }
    throw "Cannot give cheat for: <qid>";
}

public str showAnother(ConceptName cpid, QuestionName qid, Question q){
    return XMLResponses(("concept" : cpid, "exercise" : qid, "another" : showQuestion(cpid, q)));
}

public str cheatAnswer(ConceptName cpid, QuestionName qid, str cheat){
    return XMLResponses(("concept" : cpid, "exercise" : qid, "validation" : "true", "feedback" : cheat));
}

public list[str] positiveFeedback = [
"Good!",
"Go on like this!",
"I knew you could make this one!",
"You are making good progress!",
"Well done!",
"Yes!",
"Correct!",
"You are becoming a pro!",
"You are becoming an expert!",
"You are becoming a specialist!",
"Excellent!",
"Better and better!",
"Another one down!",
"You are earning a place in the top ten!",
"Learning is fun, right?"
];

public list[str] negativeFeedback = [
"A pity!",
"A shame!",
"Try another question!",
"I know you can do better.",
"Nope!",
"I am suffering with you :-(",
"Give it another try!",
"With some more practice you will do better!",
"Other people mastered this, and you can do even better!",
"It is the journey that counts!",
"Learning is fun, right?",
"After climbing the hill, the view will be excellent.",
"Hard work will be rewarded!"
];

public str correctAnswer(ConceptName cpid, QuestionName qid){
    badAnswer -= qid;
    goodAnswer += qid;
    feedback = (arbInt(100) < 25) ? getOneFrom(positiveFeedback) : "";
    return XMLResponses(("concept" : cpid, "exercise" : qid, "validation" : "true", "feedback" : feedback));
}

public str wrongAnswer(ConceptName cpid, QuestionName qid, str explanation){
    badAnswer += qid;
    goodAnswer -= qid;
    feedback = explanation + ((arbInt(100) < 25) ? (" " + getOneFrom(negativeFeedback)) : "");
	return  XMLResponses(("concept" : cpid, "exercise" : qid, "validation" : "false", "feedback" : feedback));
}

public str saveFeedback(str error, str replacement){
  return (error != "") ? error : replacement;
}

public str XMLResponses(map[str,str] values){
    return "\<responses\><for(field <- values){>\<response id=\"<field>\"\><escapeForHtml(values[field])>\</response\><}>\</responses\>";
}