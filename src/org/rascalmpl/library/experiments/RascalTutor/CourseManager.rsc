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

loc courseRoot = |file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/RascalTutor/Courses/|;

public Course thisCourse = course("",|file://X/|,"",(),{},[],());

public ConceptName root = "";

public loc directory = |file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/RascalTutor/Courses/|;

public map[ConceptName, Concept] concepts = ();

public list[str] conceptNames = [];

public Graph[ConceptName] refinements = {};

public list[str] baseConcepts = [];

public map[str, ConceptName] related = ();

// Initialize CourseManager. 
// ** Be aware that this function should be called at the beginning of each function that can be
// ** called from a servlet to ensure proper initialisation.

private void initialize(){
  if(root == ""){
     //c = readTextValueFile(#Course, |file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/RascalTutor/Courses/Rascal/Rascal.course|);
     //c = course("Rascal Tutorial",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/RascalTutor/Courses/|,"Rascal",("Rascal/Expression/Integer/Addition":concept("Addition",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Expression/Integer/Addition.concept|,[],"\<tt\> \<i\>Exp\</i\>\<sub\>1\</sub\> + \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>"," Exp1 + Exp2\n\n",{"+"}," ","","","",[]),"Rascal/Expression":concept("Expression",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Expression/Expression.concept|,[],"","\n\n",{}," ","","","",[]),"Rascal/Datastructure/Tuple":concept("Tuple",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Datastructure/Tuple/Tuple.concept|,[],"","\n\n",{},"xxxx yyyy","","","",[]),"Rascal/Expression/Integer":concept("Integer",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Expression/Integer/Integer.concept|,[],"","\n\n",{}," ","","","",[]),"Rascal/Datastructure/Set/Comprehension":concept("Comprehension",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Datastructure/Set/Comprehension.concept|,[],"\<tt\> {\<i\>Exp\</i\> | \<i\>Gen\</i\>\<i\>Or\</i\>\<i\>Test\</i\>\<sub\>1\</sub\>, \<i\>Gen\</i\>\<i\>Or\</i\>\<i\>Test\</i\>\<sub\>2\</sub\>, ... }\</tt\>"," {Exp | GenOrTest1, GenOrTest2, ... }\n\n",{"...","}",",","|","{"}," A set comprehension ...",""," ","",[]),"Rascal/Expression/Real/Addition":concept("Addition",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Expression/Real/Addition.concept|,[],"\<tt\>\t\<i\>Exp\</i\>\<sub\>1\</sub\> + \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>","\tExp1 + Exp2\n\n",{"+","\t"}," ","","","",[]),"Rascal":concept("Rascal",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Rascal.concept|,[],"","\n\n",{}," Rascal is a language for \<i\>meta-programming\</i\>, that is, it is intended for reading, analyzing, and transforming other programs. Main features are:\<ul\>\n\<li\> Statically typed: many programming errors are caught before execution.\</li\>\n\<li\> Value-oriented: all values are immutable once they have been created.\</li\>\n\<li\> ...\</li\>\n\n\</ul\>\n","\<ul\>\n\<li\> A program to refactor Java source code.\</li\>\n\<li\> Implementation of a \<i\>domain-specific language\</i\> (DSL) for describing business processes.\</li\>\n\n\</ul\>\n","\<ul\>\n\<li\> Very high-level language: many problems can be solved with a concise Rascal program.\</li\>\n\<li\> Rich feature set.\</li\>\n\n\</ul\>\n","\<ul\>\n\<li\> Rascal programs are not yet highly optimized so execution maybe slow for some programs.\</li\>\n\n\</ul\>\n",[]),"Rascal/Expression/Integer/Multiplication":concept("Multiplication",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Expression/Integer/Multiplication.concept|,[],"\<tt\>  \<i\>Exp\</i\>\<sub\>1\</sub\> * \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>","  Exp1 * Exp2\n\n",{"*"}," xxxyyyzzzaaa 111 222 333 4444 555 666 777"," \<ul\>\n\<li\> wqqwqw\</li\>\n\<li\> zzzzz\</li\>\n\<li\> A \<b\>big\</b\> example\</li\>\n\n\</ul\>\n","","",[]),"Rascal/Datastructure/Tuple/Toople":concept("Toople",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Datastructure/Tuple/Toople/Toople.concept|,[],"","\n\n",{},"","","","",[]),"Rascal/Datastructure/Set/Union":concept("Union",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Datastructure/Set/Union.concept|,[],"\<tt\> \<i\>Exp\</i\>\<sub\>1\</sub\> + \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>"," Exp1 + Exp2\n\n",{"+"}," Union is an operation that combines two sets. The \<tt\>+\</tt\> operator is used to denote set union.","\<ul\>\n\<li\> \<tt\> {1, 2, 3} + {3, 4}\</tt\> // Union of two sets of integers; result is \<tt\>{1, 2, 3, 4}\</tt\>.\</li\>\n\n\</ul\>\n"," ","",[choiceQuestion("Union1"," What is the value of \<tt\>{1,3,5} + {3,4,5}\</tt\>?",[good("\<tt\>{1,3,4,5}\</tt\>"),good("\<tt\>{5,4,3,1}\</tt\>"),bad("\<tt\>{1,3,5,3,4,5}\</tt\>")]),typeQuestion("Union2","","set[int]"),exprQuestion("Union3","","\<set[int]\> + \<set[int]\>")]),"Rascal/Datastructure/Set":concept("Set",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Datastructure/Set/Set.concept|,[],"\<tt\> {\<i\>Exp\</i\>\<sub\>1\</sub\>, \<i\>Exp\</i\>\<sub\>2\</sub\>, ...}\</tt\>"," {Exp1, Exp2, ...}\n\n",{"...}",",","{"}," A set is an unordered collection of values without duplicates.\<h1\>Heading level 1\</h1\>\nHere is some text for the main section of the description. Now let\'\\s try a subsection ...\<h2\>Heading level 2\</h2\>\n... and \<b\>here\</b\> is the text of that subsection!"," This is just plain text with possible \<b\>markup\</b\>.Here is a set of integers: \<tt\>{3,2,1}\</tt\>. The type of this set is \<tt\>set[int]\</tt\> as can be seen when we type it into the Rascal evaluator:\<pre class=\"screen\"\>\<b\>rascal\>\</b\>{1, 2, 3}\nset[int]: {3,2,1}\n\</pre\>Here is a program listing:\<pre class=\"listing\"\>if(x \< 0)\n   x = 5;\n\</pre\>Other examples of sets are:\<ul\>\n\<li\> \<tt\> {1, 2, 3}\</tt\> // A set of integers\</li\>\n\<li\> \<tt\> {} \</tt\>       // The empty set\</li\>\n\<li\> \<tt\> {\"abc\"}\</tt\>   // A set containing a single string\</li\>\n\n\</ul\>\n"," Sets allow the storage of, in principle unbounded, numbers of values and provide many effcicient operations such as, for instance, membership test, union, intersection and many more.","\<ul\>\n\<li\> If the order of elements is relevant, one can better use lists than sets.\</li\>\n\<li\> If the multiplicity of elements is relevant, one can better use a map to represent the number of occurrences of a value.\</li\>\n\n\</ul\>\n",[textQuestion("Set1"," The type of the value \<tt\>{1, 2, 3}\</tt\> is",{"xxx","set[int]"}),choiceQuestion("Set2"," What is the value of \<tt\>{1,3,5} + {3,4,5}\</tt\>?",[good("{1,3,4,5}"),good("{5,4,3,1}"),bad("{1,3,5,3,4,5}")]),typeQuestion("Set3","","set[arb]"),typeQuestion("Set4","","set[str]"),typeQuestion("Set5","","set[list[int]]"),exprQuestion("Set6","","\<set[int]\> + \<set[int]\>")]),"Rascal/Datastructure/Map/Comprehension":concept("Comprehension",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Datastructure/Map/Comprehension.concept|,[],"\<tt\> (\<i\>Exp\</i\> | \<i\>Gen\</i\>\<i\>Or\</i\>\<i\>Test\</i\>\<sub\>1\</sub\>, \<i\>Gen\</i\>\<i\>Or\</i\>\<i\>Test\</i\>\<sub\>2\</sub\>, ... )\</tt\>"," (Exp | GenOrTest1, GenOrTest2, ... )\n\n",{"...",",","|",")","("}," A map comprehension ...",""," ","",[]),"Rascal/Expression/Real/Multiplication":concept("Multiplication",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Expression/Real/Multiplication.concept|,[],"\<tt\>   \<i\>Exp\</i\>\<sub\>1\</sub\> * \<i\>Exp\</i\>\<sub\>2\</sub\>\</tt\>","   Exp1 * Exp2\n\n",{"*"}," ","","","",[]),"Rascal/Datastructure":concept("Datastructure",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Datastructure/Datastructure.concept|,[],"","\n\n",{}," Rascal has several built-in datatypes such as Booleans, Integers, Reals. Rascal also provides structured datatypes such such as lists, sets, maps and relations and also provides user-defined data structures","\<ul\>\n\<li\> \<tt\>data YesOrNo = yes() | no();\", []),\</tt\> // A set of integers\</li\>\n\n\</ul\>\n","\<ul\>\n\<li\> A rich set of options for representating application data.\</li\>\n\<li\> Efficient implementation.\</li\>\n\<li\> Static typechecking to prevent programming errors.\</li\>\n\n\</ul\>\n","\<ul\>\n\<li\> The choice between different data structures may be hard. \</li\>\n\n\</ul\>\n",[]),"Rascal/Expression/Integer/arbInt":concept("arbInt",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Expression/Integer/arbInt.concept|,[],"\<ul\>\<li\>\<tt\> int arbInt()\</tt\>\</li\>\n\<li\>\<tt\>          int arbInt(int limit)\</tt\>\</li\>\n\</ul\>"," int arbInt()\n          int arbInt(int limit)\n\n",{"int","arbInt","limit",")","(","()"}," \<tt\>arbInt\</tt\> generates an arbitrary integer. With the extra argument limit, an integer value in the range [0 .. \<tt\>limit\</tt\>].","","","",[]),"Rascal/Datastructure/Map":concept("Map",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Datastructure/Map/Map.concept|,[],"\<tt\> ( \<i\>Key\</i\>\<sub\>1\</sub\> : \<i\>Value\</i\>\<sub\>1\</sub\>, ... )\</tt\>"," ( Key1 : Value1, ... )\n\n",{"...",",",":",")","("}," A map is a dictionary ...",""," ","",[]),"Rascal/Expression/Real":concept("Real",|file:///Users/paulklint/software/source/roll/rascal/src/org/rascalmpl/library/experiments/Tutor/Courses/Rascal/Expression/Real/Real.concept|,[],"","\n\n",{}," ","","","",[])),{<"Rascal/Datastructure/Tuple","Rascal/Datastructure/Tuple/Toople">,<"Rascal/Datastructure","Rascal/Datastructure/Set">,<"Rascal/Expression/Real","Rascal/Expression/Real/Multiplication">,<"Rascal/Expression/Integer","Rascal/Expression/Integer/arbInt">,<"Rascal/Datastructure","Rascal/Datastructure/Map">,<"Rascal/Datastructure/Map","Rascal/Datastructure/Map/Comprehension">,<"Rascal","Rascal/Expression">,<"Rascal/Datastructure/Set","Rascal/Datastructure/Set/Union">,<"Rascal/Expression/Real","Rascal/Expression/Real/Addition">,<"Rascal/Expression","Rascal/Expression/Real">,<"Rascal/Datastructure/Set","Rascal/Datastructure/Set/Comprehension">,<"Rascal","Rascal/Datastructure">,<"Rascal/Expression/Integer","Rascal/Expression/Integer/Multiplication">,<"Rascal/Expression","Rascal/Expression/Integer">,<"Rascal/Datastructure","Rascal/Datastructure/Tuple">,<"Rascal/Expression/Integer","Rascal/Expression/Integer/Addition">},["\t","(","()",")","*","+",",","...","...}",":","Addition","Comprehension","Datastructure","Expression","Integer","Map","Multiplication","Rascal","Real","Set","Toople","Tuple","Union","arbInt","int","limit","{","|","}"],());
     c = compileCourse("Rascal", "Rascal Tutorial", courseRoot);

     reinitialize(c);
  }
}

private void reinitialize(Course c){
     thisCourse = c;
     root = c.root;
     directory = c.directory;
     concepts = c.concepts;
     conceptNames = sort(toList(domain(concepts)));
     refinements = c.refinements;
     baseConcepts = c.baseConcepts;
     related = c.related;
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
  println("prelude: <baseConcepts>");
 
  return "\n\<script type=\"text/javascript\" src=\"jquery-1.4.2.min.js\"\>\</script\>\n" +
         "\n\<script type=\"text/javascript\" src=\"prelude.js\"\>\</script\>\n" +
  
        // "\<link type=\"text/css\" rel=\"stylesheet\" href=\"prelude.css\"/\>\n";
         "\<style type=\"text/css\"\><css>\</style\>" +
          "\n\<script type=\"text/javascript\"\>var baseConcepts = new Array(<for(int i <- [0 .. nbc]){><(i==0)?"":",">\"<baseConcepts[i]>\"<}>);
          \</script\>\n";
}

// Present a concept
// *** called from servlet Show in RascalTutor

public str showConcept(ConceptName id){
 
  initialize();
  C = concepts[id];
  refs = sort(toList(refinements[id]));
  questions = C.questions;
  return html(
  	head(title(C.name) + prelude()),
  	body(
  	  section("Name", showConceptPath(id)) +
  	  searchBox() + 
  	  ((isEmpty(refs)) ? "" : "<sectionHead("Details")> <for(ref <- refs){><showConceptURL(ref, basename(ref))> &#032 <}>") +
  	  ((isEmpty(C.related)) ? "" : p("<b("Related")>: <for(rl <- C.related){><showConceptURL(related[rl], basename(rl))> &#032 <}>")) +
  	  section("Synopsis", C.synopsis) +
  	  section("Description", C.description) +
  	  section("Examples", C.examples) +
  	  section("Benefits", C.benefits) +
  	  section("Pittfalls", C.pittfalls) +
  	  ((isEmpty(questions)) ? "" : "<sectionHead("Questions")> <br()><for(quest <- questions){><showQuestion(id,quest)> <}>") +
  	  editMenu(id)
  	)
  );
}

public str section(str name, str txt){
println("section: <name>: \<\<\<<txt>\>\>\>");
  return (/^\s*$/s := txt) ? "" : div(name, sectionHead(name) +  " " + txt);
 // return div(name, b(name) + ": " + txt);
}

public str showConceptURL(ConceptName c, str name){
   return "\<a href=\"show?concept=<c>\"\><name>\</a\>";
}

public str showConceptURL(ConceptName c){
   return showConceptURL(c, c);
}

public str showConceptPath(ConceptName cn){
  names = basenames(cn);
  return "<for(int i <- [0 .. size(names)-1]){><(i==0)?"":"/"><showConceptURL(compose(names, 0, i), names[i])><}>";
}

public str searchBox(){
  return "\n\<div id=\"searchBox\"\>
              \<form method=\"GET\" id=\"searchForm\" action=\"/search\"\>\<b\>Search\</b\>\<br /\>
              \<input type=\"text\" id=\"searchField\" name=\"term\" autocomplete=\"off\"\>\<br /\>
              \<div id=\"popups\"\>\</div\>
              \</form\>
            \</div\>\n";
}

public str editMenu(ConceptName cn){
  return "\n\<div id=\"editMenu\"\>
              [\<a id=\"editAction\" href=\"/edit?concept=<cn>&new=false\"\>\<b\>Edit\</b\>\</a\>] | 
              [\<a id=\"newAction\" href=\"/edit?concept=<cn>&new=true\"\>\<b\>New\</b\>\</a\>]
            \</div\>\n";
}

// Edit a concept
// *** called from servlet Edit in TutorLauncher

public str edit(ConceptName cn, bool newConcept){
  initialize();
  str content = "";
  if(newConcept){
    content = mkConceptTemplate(cn + "/");
  } else {
  	c = concepts[cn];
  	content = readFile(c.file);  //TODO: IO exception (not writable, does not exist)
  }
  return html(head(title("Editing <cn>") + prelude()),
              body(
              "\n\<div id=\"editArea\"\>
                    \<form method=\"POST\" action=\"/save\"\>
                    \<textarea rows=\"20\" cols=\"60\" name=\"newcontent\" class=\"editTextarea\"\><content>\</textarea\>
                    \<input type=\"hidden\" name=\"concept\" value=\"<cn>\"\> \<br /\>
                    \<input type=\"hidden\" name=\"new\" value=\"<newConcept>\"\> \<br /\>
                    \<input type=\"submit\" value=\"Save\" class=\"editSubmit\"\>
                    \</form\>
                  \</div\>\n"
             ));
}

public list[str] getPathNames(str path){
  return [ name | /<name:[A-Za-z]+>(\/|$)/ := path ];
}

// Edit a concept
// *** called from servlet Edit in TutorLauncher

public str save(ConceptName cn, str text, bool newConcept){
  initialize();
  if(newConcept) {
     lines = splitLines(text);
     fullName = trim(combine(getSection("Name", lines)));
     path = getPathNames(fullName);
     
     if(size(path) == 0)
     	return saveError("Name \"<fullName>\" is not a proper concept name");
     	
     cname = last(path);
     parent = head(path, size(path)-1);
     parentName = "<for(int i <- index(parent)){><(i>0)?"/":""><parent[i]><}>";
     
     // Does the concept name start with the root concept?
     
     println("name = <fullName>; path = <path>; parent = <parent>; cname = <cname>");
     if(path[0] != root)
        return saveError("Concept name should start with root concept \"<root>\"");
     
     // Does the parent directory exist?
     file = directory[file = directory.file + "/" + parentName];
     if(!exists(file))
     	return saveError("Parent directory <file> does not exist (spelling error?)");
     
     // Does the file for this concept already exist as main concept?
     file = directory[file = directory.file + "/" + fullName + suffix];
     if(exists(file))
     	return saveError("File <file> exists already");
     	
     // Does the file for this concept already exist as a subconcept?
     file = directory[file = directory.file + "/" + fullName + "/" + cname + suffix];
     if(exists(file))
     	return saveError("File <file> exists already");
     
     // Create proper directory if it does not yet exist
     dir = directory[file = directory.file + "/" + fullName];	
     if(!isDirectory(dir)){
       println("Create dir <dir>");
       if(!mkDirectory(dir))
       	  return saveError("Cannot create directory <dir>");
     }
     
     // We have now the proper file name for the new concept and process it
     file = directory[file = directory.file + "/" + fullName + "/" + cname + suffix];
     lines[0] = "Name:" + cname;  // Replace full path name by t concept name
     println("lines = <lines>");
     println("Write to file <file>");
     writeFile(file, combine(lines));
     concepts[fullName] = parseConcept(file, lines, directory.path);
     thisCourse.concepts = concepts;
     reinitialize(recompileCourse(thisCourse));
     return showConcept(fullName);
  } else {
    c = concepts[cn];
    writeFile(c.file, text);
    concepts[cn] = parseConcept(c.file, directory.path);
    return showConcept(cn);
  }
}

public str saveError(str msg){
  throw msg;
}

// TODO: This should be in the library

public bool contains(str subject, str key){
   if(size(subject) == 0)
     return false;
   for(int i <- [ 0 .. size(subject) -1])
      if(startsWith(substring(subject, i), key))
      	return true;
   return false;
}

public list[str] doSearch(str term){
  if(size(term) == 0)
    return [];
  if(/^[A-Za-z]*$/ := term)
  	return [showConceptPath(name) | name <- conceptNames, /<term>/ := name];
  if(term == "(" || term == ")" || term == ","){
     // Skip synopsis that contains function declaration
     return [showConceptPath(name) | name <- conceptNames, 
                                     /[A-Za-z0-9]+\(.*\)/ !:= concepts[name].rawSynopsis,
                                     contains(concepts[name].rawSynopsis, term) ];
  }
  return [showConceptPath(name) | name <- conceptNames, contains(concepts[name].rawSynopsis, term) ];
}

public str search(str term){
  results = doSearch(term);
  return html(body(title("Search results for <term>") + prelude()),
             (size(results) == 0) ? ("I found no results found for <i(term)>" + searchBox())
                              : ("I found the following results for <i(term)>:" + searchBox() +
                                 ul("<for(res <- results){>\<li\><res>\</li\>\n<}>"))
         );
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
      idx = [0 .. size(choices)-1];
      qdescr = descr;
      qform = "<for(int i <- idx){><(i>0)?br():"">\<input type=\"radio\" name=\"answer\" value=\"<i>\"\><choices[i].description>\n<}>";
    }
    case textQuestion(qid,descr,replies): {
      qdescr = descr;
      qform = "\<textarea rows=\"1\" cols=\"60\" name=\"answer\"\>\</textarea\>";
    }
    
    case typeQuestion(qid,descr,setup,tp): {
      qtype = generateType(tp);
      qexpr = escapeForHtml(generateValue(qtype));
      qdescr = descr + "What is the type of <tt(qexpr)>?";
      qform = "\<input type=\"hidden\" name=\"expr\" value=\"<qexpr>\"\>" +
              "\<textarea rows=\"1\" cols=\"60\" name=\"answer\"\>\</textarea\>";
    }                               
                                     
    case exprQuestion(qid,descr,setup,expr): {
      qexpr = escapeForHtml(generateExpr(expr));
      qdescr = descr + "What is the value of <tt(qexpr)>?";
      qform = "\<input type=\"hidden\" name=\"expr\" value=\"<qexpr>\"\>" +
              "\<textarea rows=\"1\" cols=\"60\" name=\"answer\"\>\</textarea\>";
    }
    case exprTypeQuestion(qid,descr,setup,expr): {
      qexpr = escapeForHtml(generateExpr(expr));
      qdescr = descr + "What is the type of <tt(qexpr)>?";
      qform = "\<input type=\"hidden\" name=\"expr\" value=\"<qexpr>\"\>" +
              "\<textarea rows=\"1\" cols=\"60\" name=\"answer\"\>\</textarea\>";
    }
    default:
      throw "Unimplemented question type: <q>";
  }
  answerForm = answerFormBegin(cpid, qid, "answerForm") + qform  + br() + answerFormEnd("Give answer", "answerSubmit");

  return div(qid, b(basename(qid)) + " " + status(qid + "good", good()) + status(qid + "bad", bad()) +  br() +  
                  qdescr + "\n\<span id=\"answerFeedback<qid>\" class=\"answerFeedback\"\>\</span\>\n" +
                  answerForm + 
                  anotherQuestionForm(cpid, qid) + 
                  cheatForm(cpid, qid, qexpr) +  br() +
                  hr());
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
  //println("getQuestion(<cid>, <qid>)");
  //println("concepts=<concepts>");
  c = concepts[cid];
  for(q <- c.questions)
  	if(q.name == qid)
  		return q;
  throw "Question <qid> not found";
}

// Validate an answer, also handles the requests: "cheat" and "another"
// *** called from servlet Edit in TutorLauncher

public str validateAnswer(ConceptName cpid, QuestionName qid, str answer, str expr, bool cheat, bool another){
    initialize();
	lastQuestion = qid;
	q = getQuestion(cpid, qid);
	if(cheat)
	   return showCheat(cpid, qid, q, expr);
	if(another)
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
        return (trim(answer) in replies) ? correctAnswer(cpid, qid) : wrongAnswer(cpid, qid, "");
        
      case typeQuestion(qid,descr,setup,tp):
        	try {
              expected = evalType(setup + expr);
              println("expected = <expected>; answer = <answer>");
        	  return (trim(answer) == expected) ? correctAnswer(cpid, qid) : wrongAnswer(cpid, qid, "I expected as answer: <expected>.");
        	} catch:
        		 return wrongAnswer(cpid, qid, "Something went wrong!");
        
      case exprQuestion(qid,descr,setup,tp): {
          try {
            expected = eval(setup + expr);
            given = eval(answer);
            return (given == expected) ? correctAnswer(cpid, qid) : wrongAnswer(cpid, qid, "I expected as answer: <expected>.");
          } catch:
             return wrongAnswer(cpid, qid, "Something went wrong!");
        }
        
        case exprTypeQuestion(qid,descr,setup,tp): {
          try {
            expected = evalType(setup + expr);
            return (answer == expected) ? correctAnswer(cpid, qid) : wrongAnswer(cpid, qid, "I expected as answer: <expected>.");
          } catch:
             return wrongAnswer(cpid, qid, "Something went wrong!");
        }
    }
    throw "Cannot validate answer: <qid>";
}

public str showCheat(ConceptName cpid, QuestionName qid, Question q, str expr){
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
        
      case typeQuestion(qid,descr,setup,tp):
        try {
          expected = evalType(expr);
          return cheatAnswer(cpid, qid, "The expected answer: <expected>");
        } catch:
        	cheatAnswer(cpid, qid, "Error while coputing the cheat");
        
      case exprQuestion(qid,descr,setup,tp): {
          try {
            expected = eval(expr);
            return cheatAnswer(cpid, qid, "The expected answer: <expected>");
          } catch:
             return cheatAnswer(cpid, qid, "Error while computing the cheat");
        }
        case exprTypeQuestion(qid,descr,setup,tp): {
          try {
            expected = evalType(expr);
            return cheatAnswer(cpid, qid, "The expected answer: <expected>");
          } catch:
             return cheatAnswer(cpid, qid, "Error while computing the cheat");
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

public str XMLResponses(map[str,str] values){
    return "\<responses\><for(field <- values){>\<response id=\"<field>\"\><escapeForHtml(values[field])>\</response\><}>\</responses\>";
}

public str escapeForHtml(str txt){
  return
    visit(txt){
      case /^\</ => "&lt;"
      case /^\>/ => "&gt;"
      case /^"/ => "&quot;"
      case /^&/ => "&amp;"
    }
}