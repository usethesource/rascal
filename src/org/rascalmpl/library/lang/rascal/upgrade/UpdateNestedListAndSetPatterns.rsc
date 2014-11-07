@bootstrapParser
module lang::rascal::upgrade::UpdateNestedListAndSetPatterns

import lang::rascal::\syntax::Rascal;
import util::FileSystem;
import ParseTree;
import IO;
import Message;

list[Message] report(loc root) 
   = [*report(parse(#start[Module], m)) | m <- find(root, "rsc")];
   
void update(loc root) {
  modules = [ f | /file(f) := crawl(root), f.extension == "rsc"];
  for (m <- modules) {
    writeFile(m, "<update(parse(#start[Module], m))>");
  }
}

list[Message] report(Tree m) 
  = [info("found postfix multivar", name@\loc) | /(Pattern) `<QualifiedName name>*` := m];

Tree update(Tree m) =
  visit(m) {
    case (Pattern) `<QualifiedName name>*` => (Pattern) `*<QualifiedName name>`
  };
