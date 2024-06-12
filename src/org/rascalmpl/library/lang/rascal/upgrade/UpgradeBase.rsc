@bootstrapParser
module lang::rascal::upgrade::UpgradeBase

import lang::rascal::\syntax::Rascal;
import util::FileSystem;
import ParseTree;
import IO;
import Message;
import Exception;
import util::Reflective;
import Set;
import util::Monitor;

list[Message] reportForProject(str projectName) 
  = reportForPathConfig(getProjectPathConfig(|project://<projectName>|));

list[Message] reportForPathConfig(PathConfig pcfg)
  = [ *report(root) | root <- pcfg.srcs];

list[Message] report(loc root) {
  set[loc] ms = find(root, "rsc");

  return job("Reporting for <root>", list[Message] (void (str, int) step) {
    bool st(str msg) { step(msg, 1); return true; };

    return [*reportFor(\module) | \module <- ms, st(\module.file)];
  }, totalWork = size(ms));
}
   
list[Message] reportFor(loc l) {
  try {
    return report(parse(#start[Module], l)); 
  } catch ParseError(loc r) :
    return [warning("parse error in Rascal file",r)];
}
  
void updateProject(str projectName) { 
  updatePathConfig(getProjectPathConfig(|project://<projectName>|));
}

void updatePathConfig(PathConfig pcfg) {
  for (root <- pcfg.srcs) {
    update(root); 
  }
}

void update(loc root) {
  set[loc] ms = find(root, "rsc");

  job("Updating <root>", bool (void (str, int) step) {
    for (loc m <- ms) {
      try {
        step(m.file, 1);
        writeFile(m, "<update(parse(#start[Module], m))>");
      }
      catch ParseError(l): {
        println("parse error in <l>, skipped");
      }
    }
    
    return true;
  }, totalWork=size(ms));
}

@synopsis{Definition to override in an extending module for reporting on a specific upgrade refactoring.}
default list[Message] report(Tree _) = [];

@synopsis{Definition to override in an extending module for implementing a specific upgrade refactoring.}
default Tree update(Tree m) = m;

