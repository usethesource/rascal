module lang::rascalcore::compile::Examples::Tst4

import lang::rascalcore::check::AType;
import lang::rascalcore::check::BasicRascalConfig;

value a1() = alub(
      overloadedAType({
        <|project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/compile/Examples/Tst3.rsc|(399,6,<18,6>,<18,12>),constructorId(),acons(
          aadt(
            "LanguageService",
            [],
            dataSyntax()),
          [afunc(
              aadt(
                "Tree",
                [],
                dataSyntax()),
              [
                astr(),
                aloc()
              ],
              [],
              alabel="parser")],
          [],
          alabel="parser")>,
        <|project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/compile/Examples/Tst3.rsc|(121,101,<11,0>,<12,59>),functionId(),afunc(
          afunc(
            aparameter(
              "T",
              avalue(),
              closed=true),
            [
              avalue(),
              aloc()
            ],
            []),
          [areified(
              aparameter(
                "T",
                avalue(),
                closed=false),
              alabel="grammar")],
          [],
          returnsViaAllPath=true,
          abstractFingerprint=0,
          alabel="parser")>
      }),
      
      acons(
      aadt(
        "LanguageService",
        [],
        dataSyntax()),
      [afunc(
          alist(aadt(
              "DocumentSymbol",
              [],
              dataSyntax())),
          [aadt(
              "Tree",
              [],
              dataSyntax())],
          [],
          alabel="outliner")],
      [],
      alabel="outliner")
      );
      
      
 value a2() = alub(
 
    afunc(
          afunc(
            aparameter(
              "T",
              avalue(),
              closed=true),
            [
              avalue(),
              aloc()
            ],
            []),
          [areified(
              aparameter(
                "T",
                avalue(),
                closed=false),
              alabel="grammar")],
          [],
          returnsViaAllPath=true,
          abstractFingerprint=0,
          alabel="parser")
 ,
 
   acons(
      aadt(
        "LanguageService",
        [],
        dataSyntax()),
      [afunc(
          alist(aadt(
              "DocumentSymbol",
              [],
              dataSyntax())),
          [aadt(
              "Tree",
              [],
              dataSyntax())],
          [],
          alabel="outliner")],
      [])
);


AType a3() = alub(
       afunc(
            aparameter(
              "T",
              avalue(),
              closed=true),
            [
              avalue(),
              aloc()
            ],
            [])
,
      
      afunc(
          alist(aadt(
              "DocumentSymbol",
              [],
              dataSyntax())),
          [aadt(
              "Tree",
              [],
              dataSyntax())],
          [],
          alabel="outliner")
);  


AType a4() = alub(
        aparameter(
              "T",
              avalue(),
              closed=true)
              ,
         alist(aadt(
              "DocumentSymbol",
              [],
              dataSyntax()))
);        