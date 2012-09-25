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
module CourseManager

// The CourseManager handles all requests from the web server:
// - compile: compile a course
// - edit: edit a concept
// - save: save a concept after editing
// - validateAnswer: validates the answer to a specific question
// - validateExam: validates a complete exam

import List;
import String;
import util::Math;
import  analysis::graphs::Graph;
import Set;
import Map;
import CourseModel;
import HTMLUtils;
import RascalUtils;
import ValueGenerator;
import CourseCompiler;
import ValueIO;

import IO;
import util::Eval;

// Show a concept.

public str showConcept(Concept C){
   return readFile( htmlFile(C.fullName));
}

// ------------------------------------ Compiling ------------------------------------------------

// Compile a concept
// *** called from Compile servlet in RascalTutor

public str compile(ConceptName rootConcept){
  if(rootConcept in listEntries(courseDir)){
     crs = compileCourse(rootConcept);
     return showConcept(crs.concepts[rootConcept]);
  } else
     throw ConceptError(rootConcept, "not found");
}

// ------------------------------------ Editing --------------------------------------------------

// Edit a concept
// *** called from Edit servlet in RascalTutor

public str edit(ConceptName cn, bool newConcept){
 
  str content = (newConcept) ? mkConceptTemplate("") : escapeForHtml(readConceptFile(cn));
  
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
                    \<div id=\"pleaseWaitMessage\"\>\<img src=\"/images/loader-light.gif\" width=\"16\" height=\"16\" /\> Processing the changes in concept <i(basename(cn))> and regenerating index and warnings for course <i(rootname(cn))>.\</div\>
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
     if(exists(courseDir + fullName))
     	return saveFeedback("Concept <fullName> exists already", "");

     saveConceptFile(fullName, combine(lines));
     
     try {
       c = compileAndGenerateConcept(fullName, true);
       return saveFeedback("", showConcept(c));
     } catch CourseError(e): {
       return saveFeedback(e, "");
     }
  } else {
    // Saving an existing concept
    try {
      saveConceptFile(cn, text);
      c = compileAndGenerateConcept(cn, false);
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

str studentName = "";
str studentMail = "";
str studentNumber = "";

private map[str,map[str,str]] questionParams(map[str,str] params){
   paramMaps = ();
   for(key <- params){
      if(/^<cpt:[A-Za-z0-9\/_]+>_<qid:[A-Za-z0-9]+>:<param:[^\]]+>$/ := key){
           
          //println("key = <key>, cpt = <cpt>, qid = <qid>, param = <param>");
          fullQid = "<cpt>_<qid>";
          if(!(paramMaps[fullQid]?)){
             paramMaps[fullQid] = ("concept": cpt, "exercise" : qid);
          }
          m = paramMaps[fullQid];
          m[param] = params[key];
          paramMaps[fullQid] = m;
       } else {
         switch(key){
         case "studentName": studentName = params[key];
         case "studentMail": studentMail = params[key];
         case "studentNumber" : studentNumber = params[key];
         default:
              println("unrecognized key: <key>");
         }
       
       }
   }
   println("paramMaps = <paramMaps>");
   return paramMaps;
}

private bool isExam = false;

// Validate an exam.

public examResult validateExam(str timestamp, map[str,str] params){
  isExam = true;
  pm = questionParams(params);
  //println("pm = <pm>");
  return validateAllAnswers(timestamp, pm);
}

private examResult validateAllAnswers(str timestamp, map[str,map[str,str]] paramMaps){
  int nquestions = 0;
  int npass = 0;
  answers = ();
  expectedAnswers = ();
  res = ();
  for(qid <- paramMaps){
      nquestions += 1;
      v = validateAnswer1(paramMaps[qid]);
      answers[qid] = trim(paramMaps[qid]["answer"]) ? "";
      if(v == "pass"){
         npass += 1;
         res[qid] = v;
      }
      if(/fail:<expected:.*>$/ := v){
         expectedAnswers[qid] = expected;
         res[qid] = "fail";
      }
  }
  return examResult(studentName, studentMail, studentNumber, timestamp, answers, expectedAnswers, res, npass * 10.0 / nquestions);
}

// Validate an answer, also handles the requests: "cheat" and "another"
// *** called from servlet Edit in RascalTutor

public str validateAnswer(map[str,str] params){
  isExam = false;
  pm = questionParams(params);
  //println("pm = <pm>");
  qnames = domain(pm);
  if(size(qnames) != 1)
     throw "More than one answer";
  qname = toList(qnames)[0];
  return validateAnswer1(pm[qname]);
}

public str validateAnswer1(map[str,str] params){
  ConceptName cpid = params["concept"];
  QuestionName qid = params["exercise"];
    
  answer = trim(params["answer"]) ? "";
  expr = params["exp"] ? "";
  cheat = params["cheat"] ? "no";
	another = params["another"] ? "no";
	
	lastQuestion = qid;
	q = getQuestion(cpid, qid);
	
	if(cheat == "yes") {
	  return showCheat(cpid, qid, q, params);
	}
	
	if(another == "yes") {
	   return showAnother(cpid, qid, q);
	}
	   
	switch (q) {
    case choiceQuestion(cid,qid,descr,choices): {
      try {
           int c = toInt(answer);
           expected = [txt | good(str txt) <- choices];
           return (good(_) := choices[c]) ? correctAnswer(cpid, qid) : 
                                            wrongAnswer(cpid, qid, "I expected \"<expected[0]>\"");
      } 
      catch: return wrongAnswer(cpid, qid, "");
    }
    
    case textQuestion(cid,qid,descr,replies):
      return (toLowerCase(answer) in replies) ? correctAnswer(cpid, qid) : wrongAnswer(cpid, qid, "");
      
    case tvQuestion(cid, qid, qkind, qdetails): {
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
        for(<name, tp> <- vars) {
          env[name] = <parseType(evalType(params[name] + ";")), params[name]>;
          generatedVars += name;
	      }
  
		    for(<name, exp> <- auxVars){
	          exp1 = subst(exp, env) + ";";
	          println("exp1 = <exp1>");
	          println(" eval <setup + [exp1]>: <eval(setup + [exp1])>");
	          if (\result(value res) := eval(setup + [exp1])) {
	            env[name] = <parseType("<evalType(setup + [exp1])>"), "<res>">;
	          }
	          else {
	            env[name] = <parseType("<evalType(setup + [exp1])>"), "ok">;
	          }
	          println("env[<name>] = <env[name]>");
	      }
        
        lstBefore = subst(lstBefore, env);
		lstAfter = subst(lstAfter, env);
		cndBefore = subst(cndBefore, env);
		cndAfter = subst(cndAfter, env);
          
        switch(qkind){
          case valueOfExpr(): {
	        try {
	            if(isEmpty(answer))
	               return wrongAnswer(cpid, qid, "Your answer was empty.");
	            if(lstBefore + lstAfter == ""){
	              println("YES!");
	              println(setup + ["<cndBefore><answer><cndAfter>;"]);
	              if(holeInCnd){
	                 if (\value(true) := eval(setup + ["<cndBefore><answer><cndAfter>;"])) {
	                   return correctAnswer(cpid, qid);
	                 }
	                 wrongAnswer(cpid, qid, hint);
	              } else {
	                 println("YES2");
	                 if(!endsWith(cndBefore, ";"))
	                   cndBefore += ";";
	                 computedAnswer = eval(setup + [cndBefore]);
	                 if(answer != ""){
	                    if(!endsWith(answer, ";"))
	                       answer += ";";
	                    givenAnswer = eval(setup + [answer]);
	                   if(computedAnswer == givenAnswer)
	                      return correctAnswer(cpid, qid);     
	                 }
	                 return wrongAnswer(cpid, qid, "I expected <computedAnswer.val>.");
	               } 
	            }
	            validate = (holeInLst) ? "<lstBefore><answer><lstAfter><cndBefore>"	             
	                                     : ((holeInCnd) ? "<lstBefore><cndBefore><answer><cndAfter>;"
	                                                    : "<lstBefore><cndBefore> == <answer>;");
	            
	            println("Setup = <setup>");
	            println("Evaluating validate: <validate>");
	            // TODO this code breaks if ; semicolons appear in constant strings or source code comments
	              res = eval(setup + [validate]);
	              println("result = <res>");
	              switch (<res,hint>) {
	                case <\ok(),_>          : if (cndBefore == "") return correctAnswer(cpid, qid);
	                case <\result(true),_>  : return correctAnswer(cpid, qid);
	                case <\result(false),"">: return wrongAnswer(cpid, qid, "The answer is not right; unfortunately I have no hint to offer.");	 
	               
	                default:       			  return wrongAnswer(cpid, qid, "The answer is not right. I expected <subst(hint, env)>.");                           
	              }
	          } 
	          catch ParseError(loc l):
	             return wrongAnswer(cpid, qid, "There is a parse error in your answer at line <l.begin.line>, column <l.begin.column>");
	          catch StaticError(str msg, loc l):
	             return wrongAnswer(cpid, qid, "Your answer triggers a static error: <msg>, at line <l.begin.line>, column <l.begin.column>"); 
	          catch value x: 
	             return wrongAnswer(cpid, qid, "Something unexpected went wrong. Message: <x>");
          }
          case typeOfExpr(): {
              println("typeOfExpr");
	          try {
	            if(isEmpty(answer))
	               return wrongAnswer(cpid, qid, "Your answer was empty.");
	            if(lstBefore == ""){ // Type question without listing
	               answerType = answer;
	               expectedType = "";
	               errorMsg = "";
	               if(holeInCnd){
	                  validate = cndBefore + answer + cndAfter;
	                  println("EvalType: <setup + validate>");
	                  answerType = evalType(setup + (validate + ";"));
	                  expectedType = toString(generateType(rtype, env));
	               } else {
	                  println("EvalType: <setup + cndBefore>;");
	                  expectedType = evalType(setup + (cndBefore + ";"));
	               }
	                  
	               println("answerType is <answerType>");
	               println("expectedType is <expectedType>");
	               if(equalType(answerType, expectedType))
	              		return correctAnswer(cpid, qid);
	              errorMsg = "I expected the answer <expectedType> instead of <answerType>.";
	              if(!holeInCnd){
	                 try parseType(answer); catch: errorMsg = "I expected the answer <expectedType>; \"<answer>\" is not a legal Rascal type.";
	              }
	              return  wrongAnswer(cpid, qid, errorMsg);
	            } else {   // Type question with a listing
	              if(!(holeInCnd || holeInLst)){
	                 try 
	                    parseType(answer); 
	                 catch: return wrongAnswer(cpid, qid, "Note that \"<answer>\" is not a legal Rascal type.");
	              }
	             
	              validate = (holeInLst) ? "<lstBefore><answer><lstAfter><cndBefore>"	             
	                                     : ((holeInCnd) ? "<lstBefore><cndBefore><answer><cndAfter>;"
	                                                    : "<lstBefore><cndBefore>;");
	            
	              println("Evaluating validate: <validate>");
	              output =  evalType(setup + validate);
	              println("result is <output>");
	              
	              expectedType = toString(generateType(rtype, env));
	              
	              if(equalType((holeInLst || holeInCnd) ? answerType : answer, expectedType))
	                  return correctAnswer(cpid, qid);
	              return wrongAnswer(cpid, qid, "I expected the answer <expectedType>.");
	            }
	          }              
	            catch ParseError(loc l):
	             return wrongAnswer(cpid, qid, "There is a parse error in your answer at line <l.begin.line>, column <l.begin.column>");
	            catch StaticError(str msg, loc l):
	             return wrongAnswer(cpid, qid, "Your answer triggers a static error: <msg>, at line <l.begin.line>, column <l.begin.column>"); 
	            catch value x: 
	             return wrongAnswer(cpid, qid, "Something unexpected went wrong. Message: <x>");
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
"More kudos",
"Correct!",
"You are becoming a pro!",
"You are becoming an expert!",
"You are becoming a specialist!",
"Excellent!",
"Better and better!",
"Another one down!",
"You are earning a place in the top ten!",
"Learning is fun, right?",
"Each drop of rain makes a hole in the stone.",
"A first step of a great journey.",
"It is the journey that counts.",
"The whole moon and the entire sky are reflected in one dewdrop on the grass.",
"There is no beginning to practice nor end to enlightenment; There is no beginning to enlightenment nor end to practice.",
"A journey of a thousand miles begins with a single step.",
"When you get to the top of the mountain, keep climbing.",
"No snowflake ever falls in the wrong place.",
"Sitting quietly, doing nothing, spring comes, and the grass grows by itself.",
"To follow the path, look to the master, follow the master, walk with the master, see through the master, become the master.",
"When you try to stay on the surface of the water, you sink; but when you try to sink, you float."
];

public list[str] negativeFeedback = [
"A pity!",
"A shame!",
"Try another question!",
"I know you can do better.",
"Keep trying.",
"I am suffering with you :-(",
"Give it another try!",
"With some more practice you will do better!",
"Other people mastered this, and you can do even better!",
"It is the journey that counts!",
"Learning is fun, right?",
"After climbing the hill, the view will be excellent.",
"Hard work will be rewarded!",
"There\'s no meaning to a flower unless it blooms.",
"Not the wind, not the flag; mind is moving.",
"If you understand, things are just as they are; if you do not understand, things are just as they are.",
"Knock on the sky and listen to the sound.",
"The ten thousand questions are one question. If you cut through the one question, then the ten thousand questions disappear.",
"To do a certain kind of thing, you have to be a certain kind of person.",
"When the pupil is ready to learn, a teacher will appear.",
"If the problem has a solution, worrying is pointless, in the end the problem will be solved. If the problem has no solution, there is no reason to worry, because it can\'t be solved.",
"And the end of all our exploring will be to arrive where we started and know the place for the first time.",
"It is better to practice a little than talk a lot.",
"Water which is too pure has no fish.",
"All of the significant battles are waged within the self.",
"No snowflake ever falls in the wrong place.",
"It takes a wise man to learn from his mistakes, but an even wiser man to learn from others.",
"Only when you can be extremely pliable and soft can you be extremely hard and strong.",
"Sitting quietly, doing nothing, spring comes, and the grass grows by itself.",
"The obstacle is the path.",
"To know and not do is not yet to know.",
"The tighter you squeeze, the less you have.",
"When you try to stay on the surface of the water, you sink; but when you try to sink, you float."
];

public str correctAnswer(ConceptName cpid, QuestionName qid){
    if(!isExam){
    	ecpid = escapeConcept(cpid);
    	badAnswer -= qid;
    	goodAnswer += qid;
    	feedback = (arbInt(100) < 25) ? (" ... " + getOneFrom(positiveFeedback)) : "";
    	return XMLResponses(("concept" : ecpid, "exercise" : qid, "validation" : "true", "feedback" : feedback));
    } else
        return "pass";
}

public str wrongAnswer(ConceptName cpid, QuestionName qid, str explanation){
    if(!isExam){
       ecpid = escapeConcept(cpid);
       badAnswer += qid;
       goodAnswer -= qid;
       feedback = explanation + ((arbInt(100) < 25) ? (" ... " + getOneFrom(negativeFeedback)) : "");
	   return  XMLResponses(("concept" : ecpid, "exercise" : qid, "validation" : "false", "feedback" : feedback));
	} else
	   return "fail:<explanation>";
}

public str saveFeedback(str error, str replacement){
  return (error != "") ? error : replacement;
}

public str XMLResponses(map[str,str] values){
    R = "\<responses\><for(field <- values){>\<response id=\"<field>\"\><escapeForHtml(values[field])>\</response\><}>\</responses\>";
    println("R = <R>");
    return R;
}
