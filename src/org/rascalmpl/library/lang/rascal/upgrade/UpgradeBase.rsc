@bootstrapParser
module lang::rascal::upgrade::UpgradeBase

import lang::rascal::\syntax::Rascal;
import util::FileSystem;
import ParseTree;
import IO;
import Message;
import Exception;

list[Message] report(loc root) 
   = [*reportFor(m) | m <- find(root, "rsc")];
   
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

