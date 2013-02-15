module lang::rascal::upgrade::UpdateNestedListAndSetPatterns

import lang::rascal::\syntax::Rascal;
import util::FileSystem;
import ParseTree;
import IO;

public void report(loc root) {
  modules = [ f | /file(f) := crawl(root), f.extension == "rsc"];
  for (m <- modules) {
    report(parse(#start[Module], m));
  }
}

public void update(loc root) {
  modules = [ f | /file(f) := crawl(root), f.extension == "rsc"];
  for (m <- modules) {
    writeFile(m, "<update(parse(#start[Module], m))>");
  }
}

public void report(Tree m) {
  visit(m) {
    case (Pattern) `[<{Pattern ","}* before>,list[<Type elem>] <Name n>,<{Pattern ","}* after>]` : 
      println("found list: <elem@\loc>");
    case (Pattern) `{<{Pattern ","}* before>,set[<Type elem>] <Name n>,<{Pattern ","}* after>}` : 
      println("found set: <elem@\loc>");
    case Pattern p : ;
  }
}

public Tree update(Tree m) =
  innermost visit(m) {
    case (Pattern) `[<{Pattern ","}* before>,list[<Type elem>] <Name n>,<{Pattern ","}* after>]` =>
         (Pattern) `[<{Pattern ","}* before>, *<Type elem> <Name n>, <{Pattern ","}* after>]`
    case (Pattern) `{<{Pattern ","}* before>,set[<Type elem>] <Name n>,<{Pattern ","}* after>}` =>
         (Pattern) `{<{Pattern ","}* before>, *<Type elem> <Name n>, <{Pattern ","}* after>}`
    case Pattern p : fail; 
  };
