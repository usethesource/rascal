module demo::Rules::ConcreteBoolVisit

import demo::Rules::BoolSyntax;
import UnitTest;

Bool reduce(Bool B) {
    Bool B2;
    return innermost visit(B) {
      case [|btrue & <B2>|]   => B2            // TODO: does work when B2 is declared here
      case [|bfalse & <B2>|]  => [|bfalse|]

      case [|btrue | btrue|]   => [|btrue|]
      case [|btrue | bfalse|]  => [|btrue|]
      case [|bfalse | btrue|]  => [|btrue|]
      case [|bfalse | bfalse|] => [|bfalse|]
    };
}

public bool test(){
  assertEqual(reduce([|btrue|]), [|btrue|]);
  assertEqual(reduce([|btrue | btrue|]), [|btrue|]);
  assertEqual(reduce([|bfalse | btrue|]), [|btrue|]);
  assertEqual(reduce([|bfalse & bfalse|]), [|bfalse|]);
  return report("ConcreteBoolVisit");
}