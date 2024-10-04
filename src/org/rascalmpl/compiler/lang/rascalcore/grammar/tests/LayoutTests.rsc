module lang::rascalcore::grammar::tests::LayoutTests

//import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::check::AType;
import lang::rascalcore::grammar::definition::Layout;

import lang::rascalcore::grammar::tests::TestGrammars;
 
test bool intermix1() = 
    intermix([alit("a")], layouts("$default$"), {}) ==  [alit("a")];

test bool intermix2() = 
    intermix([alit("a"), alit("b")], layouts("$default$"), {}) ==
    [alit("a"),layouts("$default$"),alit("b")];

test bool intermix3() = 
    intermix([alit("a"), alit("b"), alit("c")], layouts("$default$"), {}) ==
    [alit("a"),layouts("$default$"),alit("b"),layouts("$default$"),alit("c")];

test bool intermix4() =
    intermix([alit("a"), \iter(sort("Exp")), alit("c")], layouts("$default$"), {})
    ==
    [ alit("a"),
      layouts("$default$"),
      \iter-seps(
        sort("Exp"),
        [layouts("$default$")]),
      layouts("$default$"),
      alit("c")
    ];
    
test bool intermix5() = 
    intermix([alit("a"), \iter-star(sort("Exp")), alit("c")], layouts("$default$"), {})
    ==
    [ alit("a"),
      layouts("$default$"),
      \iter-star-seps(
         sort("Exp"),
         [layouts("$default$")]),
      layouts("$default$"),
      alit("c")
    ];

test bool intermix6() =
    intermix([alit("a"), \iter-seps(sort("Exp"), [alit("b")]), alit("c")], layouts("$default$"), {})
    ==
    [ alit("a"),
      layouts("$default$"),
      \iter-seps(
        sort("Exp"),
        [ layouts("$default$"),
          alit("b"),
          layouts("$default$")
        ]),
      layouts("$default$"),
      alit("c")
    ];
 
 test bool layouts1() =
    layouts(GEXP, layouts("$default$"), {})
    ==
 grammar(
  {sort("E")},
  (
    alit("+"):choice(
      alit("+"),
      {prod(
          alit("+"),
          [\achar-class([arange(43,43)])])}),
    alit("*"):choice(
      alit("*"),
      {prod(
          alit("*"),
          [\achar-class([arange(42,42)])])}),
    sort("B"):choice(
      sort("B"),
      {
        prod(
          sort("B"),
          [alit("0")]),
        prod(
          sort("B"),
          [alit("1")])
      }),
    alit("0"):choice(
      alit("0"),
      {prod(
          alit("0"),
          [\achar-class([arange(48,48)])])}),
    sort("E"):choice(
      sort("E"),
      {
        prod(
          sort("E"),
          [sort("B")]),
        prod(
          sort("E"),
          [
            sort("E"),
            layouts("$default$"),
            alit("+"),
            layouts("$default$"),
            sort("B")
          ]),
        prod( 
          sort("E"),
          [
            sort("E"),
            layouts("$default$"),
            alit("*"),
            layouts("$default$"),
            sort("B")
          ])
      }),
    alit("1"):choice(
      alit("1"),
      {prod(
          alit("1"),
          [\achar-class([arange(49,49)])])})
  ));
//value main() =
//    layouts(GEXPPRIO, layouts("$default$"), {});