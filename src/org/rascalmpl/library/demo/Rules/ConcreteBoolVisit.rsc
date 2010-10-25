module demo::Rules::ConcreteBoolVisit

import demo::Rules::BoolSyntax;

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

  test reduce(`btrue`) == `btrue`;
  test reduce(`btrue | btrue`) == `btrue`;
  test reduce(`bfalse | btrue`) ==  `btrue`;
  test reduce(`bfalse & bfalse`) == `bfalse`;
