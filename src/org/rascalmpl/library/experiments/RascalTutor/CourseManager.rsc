module experiments::RascalTutor::CourseManager

// The CourseManager handles all requests from the web server:
// - showConcept: displays the HTML page for a concept
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

loc courseRoot = |cwd:///src/org/rascalmpl/library/experiments/RascalTutor/Courses/|;

public Course thisCourse = course("",|file://X/|,"",[], (),{},[],(),{});

public ConceptName root = "";

public loc directory = courseRoot;

public map[ConceptName, Concept] concepts = ();

public list[str] conceptNames = [];

public Graph[ConceptName] refinements = {};

public list[str] baseConcepts = [];

public map[str, ConceptName] related = ();

public set[str] categories = {};

set[str] enabledCategories = {};

map[str, Course] courses = ();

// Initialize CourseManager. 
// ** Be aware that this function should be called at the beginning of each function that can be
// ** called from a servlet to ensure proper initialisation.

private void initialize(){
  if(root == ""){
     //c = compileCourse("Rascal", "Rascal Tutorial", courseRoot);
     c = compileCourse("Test", "Testing", courseRoot);
     reinitialize(c, {});
  }
}

private void reinitialize(Course c, set[str] enabled){
     thisCourse = c;
     root = c.root;
     directory = c.directory;
     concepts = c.concepts;
     conceptNames = sort(toList(domain(concepts)));
     refinements = c.refinements;
     baseConcepts = c.baseConcepts;
     related = c.related;
     categories = c.categories;
     enabledCategories = isEmpty(enabled) ? categories : enabled;
}

// Start a new course
// *** called from servlet Start in RascalTutor

public str start(str name){
 if(name in courses){
   reinitialize(courses[name], {});
   return showConcept(name);
 }
 if(name in listEntries(courseRoot)){
    c = compileCourse(name, name, courseRoot);
    courses[name] = c;
    reinitialize(c, {});
    return showConcept(name);
 } else
   throw "Course <name> not found";
}


public set[QuestionName] goodAnswer = {};
public set[QuestionName] badAnswer = {};

//TODO: the webserver should be configured to serve prelude.css as a page
// with mime-type text/css. Right now it is erved as "text" and is not recognized
// by the browser.
//
// In the mean time we just paste prelude.css in the generated html-page.

public str prelude(){
  css = readFile(|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/RascalTutor/prelude.css|);
  nbc = size(baseConcepts) - 1;
  //println("prelude: <baseConcepts>");
 
  return "\n\<script type=\"text/javascript\" src=\"jquery-1.4.2.min.js\"\>\</script\>\n" +
         "\n\<script type=\"text/javascript\" src=\"prelude.js\"\>\</script\>\n" +
  
        // "\<link type=\"text/css\" rel=\"stylesheet\" href=\"prelude.css\"/\>\n";
         "\<style type=\"text/css\"\><css>\</style\>" +
          "\n\<script type=\"text/javascript\"\>var baseConcepts = new Array(<for(int i <- [0 .. nbc]){><(i==0)?"":",">\"<escapeForJavascript(baseConcepts[i])>\"<}>);
          \</script\>\n";
}

// Present a concept
// *** called from servlet Show in RascalTutor

public str showConcept(ConceptName cn){
 
  initialize();
//  println("showConcept(<cn>), <concepts>");
  try {
    C = concepts[cn];
//    println("concept = <C>");
    return showConcept(cn, C);
   } catch NoSuchKey(value key): {
     options = [ name | name <- conceptNames, endsWith(name, "/" + cn)];
     if(size(options) == 0)
        return html(head(title("Concept <cn> does not exist") + prelude()),
                    body(h1("Concept <cn> does not exist, please add concept or correct link!")));
     if(size(options) == 1)
       return showConcept(options[0], concepts[options[0]]);
     else {
        return html(head(title("Ambiguous concept <cn>") + prelude()),
                    body(h1("Concept <cn> is ambiguous, select one of:") +
                         ul("<for(name <- options){> \<li\><showConceptPath(name)>\</li\>\n<}>")));
     }
  }
}

public str showConcept(ConceptName cn, Concept C){
  childs = children(cn);
  //println("childs=<childs>");
  questions = C.questions;
  return html(
  	head(title(C.name) + prelude()),
  	body(
  	  "[\<a id=\"tutorAction\" href=\"http://localhost:8081/Courses/index.html\"\>\<b\>RascalTutor Home\</b\>\</a\>]" +
  	  section("Name", showConceptPath(cn)) + navigationMenu(cn) + categoryMenu(cn) +
  	  searchBox(cn) + 
  	  ((isEmpty(childs)) ? "" : section("Details", "<for(ref <- childs){><showConceptURL(ref, basename(ref))> &#032 <}>")) +
      section("Syntax", C.syntaxSynopsis) +
      section("Types", C.typesSynopsis) +
      section("Function", C.functionSynopsis) +
  	  section("Synopsis", C.synopsis) +
  	  section("Description", C.description) +
  	  section("Examples", C.examples) +
  	  section("Benefits", C.benefits) +
  	  section("Pittfalls", C.pittfalls) +
  	  ((isEmpty(questions)) ? "" : "<sectionHead("Questions")> <br()><for(quest <- questions){><showQuestion(cn,quest)> <}>") +
  	  editMenu(cn)
  	)
  );
}

public str section(str name, str txt){
  return (/^\s*$/s := txt) ? "" : div(name, sectionHead(name) +  " " + txt);
}



public str searchBox(ConceptName cn){
  return "\n\<div id=\"searchBox\"\>
              \<form method=\"GET\" id=\"searchForm\" action=\"/search\"\> 
              \<img id=\"searchIcon\" height=\"20\" width=\"20\" src=\"images/magnify.png\"\>
              \<input type=\"hidden\" name=\"concept\" value=\"<cn>\"\>
              \<input type=\"text\" id=\"searchField\" name=\"term\" autocomplete=\"off\"\>\<br /\>
              \<div id=\"popups\"\>\</div\>
              \</form\>
            \</div\>\n";
}

public str categoryMenu(ConceptName cn){
  return "\n\<div id=\"categoryMenu\"\>
  		\<form method=\"GET\" id=\"categoryForm\" action=\"/category\"\>
  		View: 
  		\<input type=\"hidden\" name=\"concept\" value=\"<cn>\"\>
  		<for(cat <- sort(toList(categories))){>\<input type=\"checkbox\" name=\"<cat>\" class=\"categoryButton\" <cat in enabledCategories ? "checked" : "">\><cat>\n<}>
  		\</form\>
  		\</div\>\n";
}

public str navigationMenu(ConceptName cn){
  return "\n\<div id=\"navigationMenu\"\>
         \<a href=\"show?concept=<navLeft(cn)>\"\>\<img id=\"leftIcon\" height=\"40\" width=\"40\" src=\"images/left_arrow.png\"\>\</a\>
         \<a href=\"show?concept=<navUp(cn)>\"\>\<img id=\"upIcon\" height=\"40\" width=\"40\" src=\"images/up_arrow.png\"\>\</a\>
         \<a href=\"show?concept=<navDown(cn)>\"\>\<img id=\"downIcon\" height=\"40\" width=\"40\" src=\"images/down_arrow.png\"\>\</a\>
         \<a href=\"show?concept=<navRight(cn)>\"\>\<img id=\"rightIcon\" height=\"40\" width=\"40\" src=\"images/right_arrow.png\"\>\</a\>
         \</div\>\n";
}

public str navUp(ConceptName cn){
   //println("navUp(<cn>)");
   bns = basenames(cn);
   if(size(bns) == 1)
      return cn;
   res = compose(slice(bns, 0, size(bns)-1));
   //println("returns <res>");
   return res;
}

list[ConceptName] children(ConceptName cn){
  //println("children(<cn>)");
  try { C = concepts[cn];
       res = [cn + "/" + r | r <- C.details, isEnabled(cn + "/" + r)] +
             sort([r | r <- refinements[cn], isEnabled(r), basename(r) notin C.details]);
       //println("children(<cn>) returns <res>");
       return res;
   } catch NoSuchKey(k): {
     println("*** children(<cn>): no such key <k>");
     return [];
   }
}

public str navDown(ConceptName cn){
  childs = children(cn);
  if(size(childs) == 0)
     return cn;
  return childs[0];
}

public str navRight(ConceptName cn){
 //println("navRight(<cn>)");
  parent = navUp(cn);
  if(parent == cn)
     return cn;
  siblings = children(parent);
  //println("navRight(<cn>): <siblings>");
  for(int i <- index(siblings)){
      if(siblings[i] == cn)
         return (i + 1) < size(siblings) ? siblings[i+1] : parent;
  }
  return navRight(parent);
}

public str navLeft(ConceptName cn){
  //println("navLeft(<cn>)");
  parent = navUp(cn);
   //println("navLeft(<cn>): parent = <parent>");
  if(parent == cn)
     return cn;
  siblings = children(parent);
  //println("navLeft(<cn>): <siblings>");
  for(int i <- index(siblings)){
      if(siblings[i] == cn)
        return (i > 0) ? siblings[i-1] : cn;
  }
  return navLeft(parent);
}
     

// Enable/disable a category
// *** called from servlet Category in RascalTutor

public str category(map[str,str] categories){
  initialize();
  println("category(<categories>)");
  cn = categories["concept"];
  enabledCategories = { cat | cat <- categories, cat != "concept", categories[cat] == "true"};
  println("enabledCategories = <enabledCategories>");
  return showConcept(cn);
}

private bool isEnabled(ConceptName cn){
  cats = concepts[cn].categories;
  if(isEmpty(cats))
    cats = categories;
    
  if("Beginner" in enabledCategories && "Beginner" notin cats)
     return false;

  return !isEmpty(cats & enabledCategories);
}

public str editMenu(ConceptName cn){
  return "\n\<div id=\"editMenu\"\>
              [\<a id=\"editAction\" href=\"/edit?concept=<cn>&new=false&check=false\"\>\<b\>Edit\</b\>\</a\>] | 
              [\<a id=\"newAction\" href=\"/edit?concept=<cn>&new=true&check=true\"\>\<b\>New\</b\>\</a\>] |
              [\<a id=\"checkAction\" href=\"/edit?concept=<cn>&new=false&check=true\"\>\<b\>Check\</b\>\</a\>] |
              [\<a id=\"tutorAction\" href=\"http://localhost:8081/Courses/index.html\"\>\<b\>RascalTutor Home\</b\>\</a\>]
            \</div\>\n";
}

// Edit a concept
// *** called from  Edit in RascalTutor

public str edit(ConceptName cn, bool newConcept, bool check){
  initialize();
 
  str content = "";
  if(newConcept){
    content = mkConceptTemplate("");
  } else {
    if(check)
       return doCheck(cn);
  	c = concepts[cn];
  	content = escapeForHtml(readFile(c.file));  //TODO: IO exception (not writable, does not exist)
  }
  return html(head(title("Editing <cn>") + prelude()),
              body(
              "\n\<div id=\"editArea\"\>
                    \<form method=\"POST\" action=\"/save\" id=\"editForm\"\>
                    \<textarea rows=\"15\" cols=\"60\" name=\"newcontent\" id=\"editTextArea\"\><content>\</textarea\>
                    \<input type=\"hidden\" name=\"concept\" value=\"<cn>\"\> \<br /\>
                    \<input type=\"hidden\" name=\"new\" value=\"<newConcept>\"\> \<br /\>
                    \<div id=\"editErrors\"\>errors\</div\>\n
                    \<input type=\"submit\" value=\"Save\"\>
                    \</form\>
                  \</div\>\n"
             ));
}

public str doCheck(ConceptName cn){
    thisCourse = recompileCourse(thisCourse);
    warnings = thisCourse.warnings;
    reinitialize(thisCourse, enabledCategories);
    back = "\<a href=\"show?concept=<cn>\"\>" +
                     "\<img width=\"30\" height=\"30\" src=\"images/back.png\"\>" +
           "\</a\>";
    return  html(head(title("Warnings") + prelude()),
              body(back +
                h1("\<img width=\"30\" height=\"30\" src=\"images/warning.png\"\>" +
                   "I found <size(warnings)> warnings:") +
                ul("<for(w <- warnings){><li(w)><}>") +
                back
              ));
}

public list[str] getPathNames(str path){
  return [ name | /<name:[A-Za-z]+>(\/|$)/ := path ];
}

// Save a concept
// *** called from servlet Edit in RascalTutor

public str save(ConceptName cn, str text, bool newConcept){
  initialize();
  if(newConcept) {
     lines = splitLines(text);
     sections = getSections(lines);
     cname = sections["Name"][0];
     if(/[A-Za-z0-9]+/ !:= cname)
       return saveFeedback("Name \"<cname>\" is not a proper concept name", "");
     fullName = cn + "/" + cname;

     // Does the file for this concept already exist as main concept?
     file = directory[file = directory.file + "/" + fullName + suffix];
     if(exists(file))
     	return saveFeedback("File <file> exists already", "");
     	
     // Does the file for this concept already exist as a subconcept?
     file = directory[file = directory.file + "/" + fullName + "/" + cname + suffix];
     if(exists(file))
     	return saveFeedback("File <file> exists already", "");
     
     // Create proper directory if it does not yet exist
     dir = directory[file = directory.file + "/" + fullName];	
     if(!isDirectory(dir)){
       println("Create dir <dir>");
       if(!mkDirectory(dir))
       	  return saveFeedback("Cannot create directory <dir>", "");
     }
     
     // We have now the proper file name for the new concept and process it
     file = directory[file = directory.file + "/" + fullName + "/" + cname + suffix];

     println("Write to file <file>");
     writeFile(file, combine(lines));
     try {
       concepts[fullName] = parseConcept(file, sections, directory.path);
       thisCourse.concepts = concepts;
       reinitialize(thisCourse, enabledCategories);
       return saveFeedback("", showConcept(fullName));
     } catch CourseError(e): {
       return saveFeedback(e, "");
     }
  } else {
    c = concepts[cn];
    try {
      writeFile(c.file, text);
      println("Parsing concept");
      concepts[cn] = parseConcept(c.file, directory.path);
      thisCourse.concepts = concepts;
      thisCourse = recompileCourse(thisCourse);
      warnings = thisCourse.warnings;
      reinitialize(thisCourse, enabledCategories);
      println("parsed, returning feedback");
      return saveFeedback("", showConcept(cn));
    } catch ConceptError(e): {
       return saveFeedback(e, "");
    }
    println("Other error");
    return showConcept(cn);
  }
}

// TODO: This functionality should be in the library
/*
public bool contains(str subject, str key){
   if(size(subject) == 0)
     return false;
   for(int i <- [ 0 .. size(subject) -1])
      if(startsWith(substring(subject, i), key))
      	return true;
   return false;
}
*/

public list[str] doSearch(str term){
  if(size(term) == 0)
    return [];
  println("conceptNames=<conceptNames>");
  return [name | name <- conceptNames,
          
                ( /^[A-Za-z0-9]+$/ := term 
                  && (startsWith(name, term) || endsWith(name, "/" + term) || /\/<term>\// := name)
                ) ||
                (term in concepts[name].searchTerms)
         ];
}

private str plural(int n){
  return (n != 1) ? "s" : "";
}

public str search(ConceptName cn, str term){
  allResults = doSearch(term);
  enabledResults = [res | res <- allResults, isEnabled(res)];
  otherResults = allResults - enabledResults;
  output = br() + br();
  
  if(size(enabledResults) == 1)
    return showConcept(allResults[0]);
    
  back = "\<a href=\"show?concept=<cn>\"\>" +
                     "\<img width=\"30\" height=\"30\" src=\"images/back.png\"\>" +
           "\</a\>";
  if(size(allResults) == 0)
    output +=  h1("I found no results found for <i(term)>") + categoryMenu(cn) + searchBox(cn);
  else {
    N = size(enabledResults);
    output += h1("I found <N> result<plural(N)> for <i(term)> in viewed categories:") + categoryMenu(cn) + searchBox(cn) +
                                 ul("<for(res <- enabledResults){>\<li\><showConceptPath(res)>\</li\>\n<}>");
    if(size(otherResults) != 0){
      N = size(otherResults);
      output += h1("I found <N> result<plural(N)> for <i(term)> in other categories:") +
                                 ul("<for(res <- otherResults){>\<li\><showConceptPath(res)> \<small\>categories: <concepts[res].categories - enabledCategories>\</small\>\</li\>\n<}>");
    }
  }
  
  return html(head(title("Search results for <term>") + prelude()), body(back + output + back));
}

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
	return answerFormBegin(cpid, qid, "cheatForm") + 
	       "\<input type=\"hidden\" name=\"expr\" value=\"<expr>\"\>\n" +
           "\<input type=\"hidden\" name=\"cheat\" value=\"yes\"\>\n" +
           answerFormEnd("I am cheating today", "cheatSubmit");
}

public str div(str id, str txt){
	return "\n\<div id=\"<id>\"\>\n<txt>\n\</div\>\n";
}

public str div(str id, str class, str txt){
	return "\n\<div id=\"<id>\" class=\"<class>\"\>\n<txt>\n\</div\>\n";
}

public str status(str id, str txt){
	return "\n\<span id=\"<id>\" class=\"answerStatus\"\>\n<txt>\n\</span\>\n";
}

public str good(){
  return "\<img height=\"25\" width=\"25\" src=\"images/good.png\"/\>";
}

public str bad(){
   return "\<img height=\"25\" width=\"25\" src=\"images/bad.png\"/\>";
}

public str status(QuestionName qid){
  return (qid in goodAnswer) ? good() : ((qid in badAnswer) ? bad() : "");
}

public str showQuestion(ConceptName cpid, Question q){
println("showQuestion: <cpid>, <q>");
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
         println("exp1 = <exp1>");
         try {
           env[name] = <parseType("<evalType(setup + exp1)>"), "<eval(setup + exp1)>">;
         } catch: throw "Error in computing <name>, <exp>";
      }
      println("env = <env>");
      
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
  c = concepts[cid];
  for(q <- c.questions)
  	if(q.name == qid)
  		return q;
  throw "Question <qid> not found";
}

// Validate an answer, also handles the requests: "cheat" and "another"
// *** called from servlet Edit in RascalTutor

public str validateAnswer(map[str,str] params){
    ConceptName cpid = params["concept"];
    QuestionName qid = params["exercise"];
    
    answer = trim(params["answer"]) ? "";
    expr = params["exp"] ? "";
    cheat = params["cheat"] ? "no";
	another = params["another"] ? "no";
	
    initialize();
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


