@bootstrapParser
module lang::rascal::upgrade::UpgradePostfixStarAndPlusToPrefix

import lang::rascal::upgrade::UpgradeBase;

list[Message] report(Tree m) {
  result = [];
  visit(m) {
    case (Pattern) `[<{Pattern ","}* before>,list[<Type elem>] <Name n>,<{Pattern ","}* after>]` : 
      result += [info("found list pattern to upgrade", elem.origin)];
    case (Pattern) `{<{Pattern ","}* before>,set[<Type elem>] <Name n>,<{Pattern ","}* after>}` : 
      result += [info("found list pattern to upgrade", elem.origin)];
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
