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

  return job("Reporting", list[Message] (void (str, int) step) {
    bool st(str msg) { step(msg, 1); return true; };

    list[Message] result = [*reportFor(\module) | \module <- ms, st(\module.file)];
    return result;
  }, totalWork = size(ms));
}
   
list[Message] reportFor(loc l) {
  try {
    return report(parse(#start[Module], l)); 
  } catch ParseError(loc r) :
    return [warning("parse error in Rascal file",r)];
}
   
void update(loc root) {
  modules = [ f | /file(f) := crawl(root), f.extension == "rsc"];
  for (m <- modules) {
    try 
      writeFile(m, "<update(parse(#start[Module], m))>");
    catch ParseError(l):
      println("parse error in <l>, skipped");
  }
}

default list[Message] report(Tree m) = [];
default Tree update(Tree m) = m;

