module lang::rascalcore::compile::Examples::Tst1

import lang::rascalcore::check::ATypeBase;
import lang::rascalcore::check::ATypeUtils;
import IO;

map[AType, set[AType]] defs = (
  aadt(
    "A",
    [],
    contextFreeSyntax()):{aprod(achoice(
        aadt(
          "A",
          [],
          contextFreeSyntax()),
        {prod(
            aadt(
              "A",
              [],
              contextFreeSyntax()),
            [alit("a")])}))});


value main() {
    res = adefinitions2definitions(defs);
    println("main:"); iprintln(res);
    return res;
    //return aprod2prod(p);
}