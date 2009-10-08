module demo::Rules::ConcreteBoolVisit

import demo::Rules::BoolSyntax;
import UnitTest;

Bool reduce(Bool B) {
    Bool B2;
    return innermost visit(B) {
      case `btrue & <B2>`   => B2
      case `bfalse & <B3>`  => `bfalse`

      case `btrue | btrue`   => `btrue`
      case `btrue | bfalse`  => `btrue`
      case `bfalse | btrue`  => `btrue`
      case `bfalse | bfalse` => `bfalse`
    };
}

public bool test(){
  assertEqual(reduce(`btrue`), `btrue`);
  assertEqual(reduce(`btrue | btrue`), `btrue`);
  assertEqual(reduce(`bfalse | btrue`), `btrue`);
  assertEqual(reduce(`bfalse & bfalse`), `bfalse`);
  return report("ConcreteBoolVisit");
}
