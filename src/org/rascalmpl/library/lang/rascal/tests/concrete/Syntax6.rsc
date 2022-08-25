module lang::rascal::tests::concrete::Syntax6

import ParseTree;

syntax Exp = left Exp "+" Exp | LitA | LitB | AZ;
syntax LitA = "a";
syntax LitB = "b";
lexical AZ = [a-z];

test bool expIsAmbiguous() = /amb(_) := parse(#Exp, "a+a", allowAmbiguity=true);

test bool visitAndDeepMatchGoThroughAmb() {
  t = parse(#Exp, "a+a", allowAmbiguity=true);
  
  u = visit(t) {
    case (Exp) `<LitA _>` => (Exp) `<LitB b>` when LitB b := [LitB] "b"  
  }

  return t != u && /LitB _ := u;
}
