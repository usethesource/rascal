@bootstrapParser
module lang::rascal::upgrade::UpgradePostfixStarAndPlusToPrefix

import util::FileSystem;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;

list[message] report(loc root) 
   = [*report(parse(#start[Module], m)) | m <- find(root, "rsc")];

void update(loc root) {
  for (m <- find(root, "rsc")) {
    writeFile(m, "<update(parse(#start[Module], m))>");
  }
}

list[Message] report(Tree m) {
  result = [];
  visit(m) {
    case (Pattern) `[<{Pattern ","}* before>,list[<Type elem>] <Name n>,<{Pattern ","}* after>]` : 
      result += [message("found list pattern to upgrade", elem@\loc)];
    case (Pattern) `{<{Pattern ","}* before>,set[<Type elem>] <Name n>,<{Pattern ","}* after>}` : 
      result += [message("found list pattern to upgrade", elem@\loc)];
    case Pattern p : ;
  }
  
  return result;
}

public Tree update(Tree m) =
  innermost visit(m) {
    case (Pattern) `[<{Pattern ","}* before>,list[<Type elem>] <Name n>,<{Pattern ","}* after>]` =>
         (Pattern) `[<{Pattern ","}* before>, *<Type elem> <Name n>, <{Pattern ","}* after>]`
    case (Pattern) `{<{Pattern ","}* before>,set[<Type elem>] <Name n>,<{Pattern ","}* after>}` =>
         (Pattern) `{<{Pattern ","}* before>, *<Type elem> <Name n>, <{Pattern ","}* after>}`
    case Pattern p : fail; 
  };
