@doc{this module is under construction}
@contributor{Jurgen Vinju}
@contributor{Paul Klint}
@contributor{Ashim Shahi}
@contributor{Bas Basten}
module lang::java::style::StyleChecker

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import IO;
import lang::xml::DOM;
import Relation;
import Set;
import List;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;

import lang::java::style::BlockChecks;
import lang::java::style::ClassDesign;
import lang::java::style::Coding;
import lang::java::style::Imports;
import lang::java::style::Metrics;
import lang::java::style::Miscellaneous;
import lang::java::style::NamingConventions;
import lang::java::style::SizeViolations;

alias Checker = list[Message] (node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations);

private set[Checker] active() = {
  blockChecks,
  classDesignChecks,
  codingChecks,
  importsChecks,
  metricsChecks,
  miscellaneousChecks,
  namingConventionsChecks,
  sizeViolationsChecks
};  

@doc{For testing on the console; we should assume only a model for the current AST is in the model}
list[Message] styleChecker(M3 model, set[node] asts, set[Checker] checkers = active()){
	msgs = [];
    for(ast <- asts){
    	classDeclarations = getAllClassDeclarations(ast);
		classDeclarations = getAllMethodDeclarations(ast);
		msgs += [*checker(ast, model, classDeclarations, classDeclarations) | Checker checker <- checkers];
    }
    return msgs;
 }
   
@doc{For integration into OSSMETER, we get the models and the ASTs per file}   
list[Message] styleChecker(map[loc, M3] models, map[loc, node] asts, set[Checker] checkers = active()) 
  = [*checker(asts[f], models[f]) | f <- models, checker <- checkers];  
  

list[Message] main(loc dir = |project://java-checkstyle-tests|){
  
  m3model = createM3FromEclipseProject(dir);
  asts = createAstsFromDirectory(dir, true);
  return styleChecker(m3model, asts, checkers = active());
} 

// temporary functions for regression testing with checkstyle

@doc{measure if Rascal reports issues that CheckStyle also does}
test bool precision() {
  rascal = main();
  checkstyle = getCheckStyleMessages();
  
  println("comparing checkstyle:
          '  <size(checkstyle)> messages: <checkstyle>
          'with rascal:
          '  <size(rascal)> messages: <rascal>
          '");

  rascalPerFile = index({< path, mr> | mr <- rascal, /.*src\/<path:.*>$/ := mr.pos.path});
  checkstylePerFile = index({< path, mc> | mc:<l,_> <- checkstyle, /.*src\/<path:.*>$/ := l.path});
  
  missingFiles = rascalPerFile<0> - checkstylePerFile<0>;
  
  println("Rascal found errors in <size(rascalPerFile<0>)> files
          'Checkstyle found errors in <size(checkstylePerFile<0>)> files
          '<if (size(missingFiles) > 0) {>and these <size(missingFiles)> files are missing from checkstyle: 
          '  <missingFiles>
          '  counting for <(0 | it + size(rascalPerFile[m]) | m <- missingFiles)> missing messages.<} else {>and no files are missing.<}>
          '");
          
  rascalCategories = { mr.category | mr <- rascal};
  checkstyleCategories = { c | mc:<l,c> <- checkstyle};
  missingCategories = rascalCategories - checkstyleCategories;
  
  println("Rascal generated <size(rascalCategories)> different categories.
          'Checkstyle generated <size(checkstyleCategories)> different categories.
          '<if (size(missingCategories) > 0) {>and these <size(missingCategories)> are missing from checkstyle:
          '  <sort(missingCategories)>
          '  (compare to <sort(checkstyleCategories - rascalCategories)>)<}>
          '");   
  
  // filter for common files and report per file
  
  for (path <- rascalPerFile, path in checkstylePerFile, bprintln("analyzing file <path>")) {
       rascalPerCategory = index({<mr.category, <mr.pos.begin.line, mr.pos.end.line>> | mr <- rascalPerFile[path]});
       checkstylePerCategory = index({ <cat, mc.begin.line> | <mc,cat> <- checkstylePerFile[path]});
       missingCategories = rascalPerCategory<0> - checkstylePerCategory<0>;
       
       println("  Rascal found errors in <size(rascalPerCategory<0>)> categories.
               '  Checkstyle found errors in <size(checkstylePerCategory<0>)> categories.
               '  <if (size(missingCategories) > 0) {>and these <size(missingCategories)> categories are missing from checkstyle: 
               '  <missingCategories>
               '  counting for <(0 | it + size(rascalPerCategory[m]) | m <- missingCategories)> missing messages in <checkstylePerCategory><} else {>  and no categories are missing.<}>
               '");
       
       int matched = 0;
       int notmatched = 0;
               
       for (cat <- rascalPerCategory, cat in checkstylePerCategory, bprintln("  analyzing category <cat>")) {
         for (<sl, el> <- rascalPerCategory[cat], !any(l <- checkstylePerCategory[cat], l >= sl && l <= el)) {
            println("    line number not matched by checkstyle: <cat>, <path>, <rascalPerCategory[cat]>");
            notmatched += 1;
         }
         
         for (cat in checkstylePerCategory, <sl, el> <- rascalPerCategory[cat], l <- checkstylePerCategory[cat], l >= sl && l <= el) {
            println("    match found: <cat>, <path>, <l>");
            matched += 1;
         }
       }
       
       if (matched > 0 && notmatched > 0)  {
         println("  of the <matched + notmatched> messages, there were <matched> matches and <notmatched> missed messages in <path>
                 '");
       }        
  }
  
  return false;        
}

rel[loc, str] getCheckStyleMessages(loc checkStyleXmlOutput = |project://java-checkstyle-tests/lib/output.xml|) {
   txt = readFile(checkStyleXmlOutput);
   dom = parseXMLDOM(txt);
   str fix(str x) {
     return /^.*\.<y:[A-Za-z]*>Check$/ := x ? y : x;
   }
   r =  { <|file:///<fname>|(0,0,<toInt(l),0>,<toInt(l),0>), fix(ch)> 
        | /element(_, "file", cs:[*_,attribute(_,"name", fname),*_]) := dom
        , /e:element(_, "error", as) := cs
        , {*_,attribute(_, "source", ch), attribute(_,"line", l)} := {*as}
        };
   return r;
}


