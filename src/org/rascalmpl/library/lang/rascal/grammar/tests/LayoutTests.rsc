module lang::rascal::grammar::tests::LayoutTests

import Grammar;
import lang::rascal::grammar::definition::Layout;

import lang::rascal::grammar::tests::TestGrammars;

test bool intermix1() = 
    intermix([lit("a")], layouts("$default$"), {}) ==  [lit("a")];

test bool intermix2() = 
    intermix([lit("a"), lit("b")], layouts("$default$"), {}) ==
    [lit("a"),layouts("$default$"),lit("b")];

test bool intermix3() = 
    intermix([lit("a"), lit("b"), lit("c")], layouts("$default$"), {}) ==
    [lit("a"),layouts("$default$"),lit("b"),layouts("$default$"),lit("c")];

test bool intermix4() =
    intermix([lit("a"), \iter(sort("Exp")), lit("c")], layouts("$default$"), {})
    ==
    [ lit("a"),
      layouts("$default$"),
      \iter-seps(
        sort("Exp"),
        [layouts("$default$")]),
      layouts("$default$"),
      lit("c")
    ];
    
test bool intermix5() = 
    intermix([lit("a"), \iter-star(sort("Exp")), lit("c")], layouts("$default$"), {})
    ==
    [ lit("a"),
      layouts("$default$"),
      \iter-star-seps(
         sort("Exp"),
         [layouts("$default$")]),
      layouts("$default$"),
      lit("c")
    ];

test bool intermix6() =
    intermix([lit("a"), \iter-seps(sort("Exp"), [lit("b")]), lit("c")], layouts("$default$"), {})
    ==
    [ lit("a"),
      layouts("$default$"),
      \iter-seps(
        sort("Exp"),
        [ layouts("$default$"),
          lit("b"),
          layouts("$default$")
        ]),
      layouts("$default$"),
      lit("c")
    ];
 
 test bool layouts1() =
    layouts(GEXP, layouts("$default$"), {})
    ==
 grammar(
  {sort("E")},
  (
    lit("+"):choice(
      lit("+"),
      {prod(
          lit("+"),
          [\char-class([range(43,43)])],
          {})}),
    lit("*"):choice(
      lit("*"),
      {prod(
          lit("*"),
          [\char-class([range(42,42)])],
          {})}),
    sort("B"):choice(
      sort("B"),
      {
        prod(
          sort("B"),
          [lit("0")],
          {}),
        prod(
          sort("B"),
          [lit("1")],  
                    {})
      }),
    lit("0"):choice(
      lit("0"),
      {prod(
          lit("0"),
          [\char-class([range(48,48)])],
          {})}),
    sort("E"):choice(
      sort("E"),
      {
        prod(
          sort("E"),
          [sort("B")],
          {}),
        prod(
          sort("E"),
          [
            sort("E"),
            layouts("$default$"),
            lit("+"),
            layouts("$default$"),
            sort("B")
          ],
          {}),
        prod( 
          sort("E"),
          [
            sort("E"),
            layouts("$default$"),
            lit("*"),
            layouts("$default$"),
            sort("B")
          ],
          {})
      }),
    lit("1"):choice(
      lit("1"),
      {prod(
          lit("1"),
          [\char-class([range(49,49)])],
          {})})
  ));
value main() =
    layouts(GEXPPRIO, layouts("$default$"), {});
