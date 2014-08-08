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
  ra = main();
  cs = getCheckStyleMessages();
  
  println("comparing checkstyle:
          '  <cs>
          'with rascal
          '  <ra>");

  // first hash on file names
  
  ram = index({< path, mr> | mr <- ra, /.*src\/<path:.*>$/ := mr.pos.path});
  csm = index({< path, mc> | mc:<l,_> <- cs, /.*src\/<path:.*>$/ := l.path});
  
  
  for (path <- ram) {
    if (path in csm) {
       racm = index({<mr.category, <mr.pos.begin.line, mr.pos.end.line>> | mr <- ram[path]});
       cscm = index({ <cat, mc.begin.line> | <mc,cat> <- csm[path]});
       
       for (cat <- racm) {
         if (cat in cscm, <sl, el> <- racm[cat], l <- cscm[cat], l >= sl && l <= el) {
            println("match found: <cat>, <path>, <l>");
            break;
         }
         else {
            println("not found by checkstyle: <cat>, <path>, <racm[cat]>");          
         }
       }
    }
    else {
       println("not found by checkstyle: <ram[path]>");
    }
  }
  
  return false;        
}

rel[loc, str] getCheckStyleMessages(loc checkStyleXmlOutput = |project://java-checkstyle-tests/lib/output.xml|) {
   txt = readFile(checkStyleXmlOutput);
   dom = parseXMLDOM(txt);
   r =  { <|file:///<fname>|(0,0,<toInt(l),0>,<toInt(l),0>), ch> 
        | /element(_, "file", cs:[*_,attribute(_,"name", fname),*_]) := dom
        , /e:element(_, "error", as) := cs
        , {*_,attribute(_, "source", /^.*\.<ch:[A-Za-z]*>Check$/), attribute(_,"line", l)} := {*as}
        };
   return r;
}


