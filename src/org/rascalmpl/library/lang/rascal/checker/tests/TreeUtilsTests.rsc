module lang::rascal::checker::tests::TreeUtilsTests

import lang::rascal::checker::TreeUtils;
import ParseTree;

syntax A = "a";
syntax As = {A ","}*;

// Result of getProductionChildren([A] "a")
public list[Tree] TL1 = [appl(prod(lit("a"), [\char-class([range(97,97)])], {}), [char(97)])];

//Result of getProductionChildren([As] "a,a")
list[Tree] TL2 = [appl(
    regular(\iter-star-seps(
        sort("A"),
        [
          layouts("$default$"),
          lit(","),
          layouts("$default$")
        ])),
    [appl(
        prod(
          sort("A"),
          [lit("a")],
          {}),
        [appl(
            prod(
              lit("a"),
              [\char-class([range(97,97)])],
              {}),
            [char(97)])])[
        @\loc=|stdin:///|(0,1,<1,0>,<1,1>)
      ],appl(
        prod(
          layouts("$default$"),
          [],
          {}),
        [])[
        @\loc=|stdin:///|(1,0,<1,1>,<1,1>)
      ],appl(
        prod(
          lit(","),
          [\char-class([range(44,44)])],
          {}),
        [char(44)]),appl(
        prod(
          layouts("$default$"),
          [],
          {}),
        [])[
        @\loc=|stdin:///|(2,0,<1,2>,<1,2>)
      ],appl(
        prod(
          sort("A"),
          [lit("a")],
          {}),
        [appl(
            prod(
              lit("a"),
              [\char-class([range(97,97)])],
              {}),
            [char(97)])])[
        @\loc=|stdin:///|(2,1,<1,2>,<1,3>)
      ]])[
    @\loc=|stdin:///|(0,3,<1,0>,<1,3>)
  ]];


test bool getProductionChildren11() = 
       getProductionChildren([A] "a")
    == getProductionChildren([A] "a")
    ;


test bool getProductionChildren12() = TL1 := getProductionChildren([A] "a");

test bool getProductionChildren13() = TL1 := getProductionChildren((A) `a`);

Tree TL10 = TL1[0];

test bool getProductionChildren14() = TL10 := getProductionChildren([A] "a")[0];

test bool getProductionChildren15() = [appl(prod(lit("a"), [\char-class([range(97,97)])], {}), [char(97)])] :=
									  [appl(prod(lit("a"), [\char-class([range(97,97)])], {}), [char(97)])] ;

test bool getProductionChildren16() = TL1 := [appl(prod(lit("a"), [\char-class([range(97,97)])], {}), [char(97)])] ;


test bool getProductionChildren17() = TL1 == getProductionChildren([A] "a");

test bool getProductionChildren21() = 
       getProductionChildren([As] "a,a")
    == getProductionChildren([As] "a,a")
    ;

test bool getProductionChildren22() = TL2 := getProductionChildren([As] "a,a");

Tree TL20 = TL2[0];
test bool getProductionChildren23() = TL20 := getProductionChildren([As] "a,a")[0];

test bool getProductionChildren24() = TL2 == getProductionChildren([As] "a,a");


