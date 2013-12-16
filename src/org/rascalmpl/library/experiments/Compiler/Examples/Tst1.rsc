module experiments::Compiler::Examples::Tst1

import ParseTree;

syntax A = "a";
syntax B = "b";
start syntax AB = A B;

value main(list[value] args) = (AB) `ab` := [AB] "ab";

Tree parsedFragment =
appl(
  regular(\iter-star(lex("ConcretePart"))),
  [appl(
      prod(
        label(
          "text",
          lex("ConcretePart")),
        [conditional(
            iter(\char-class([
                  range(1,9),
                  range(11,59),
                  range(61,61),
                  range(63,91),
                  range(93,95),
                  range(97,16777215)
                ])),
            {\not-follow(\char-class([
                    range(1,9),
                    range(11,59),
                    range(61,61),
                    range(63,91),
                    range(93,95),
                    range(97,16777215)
                  ]))})],
        {}),
      [appl(
          regular(iter(\char-class([
                  range(1,9),
                  range(11,59),
                  range(61,61),
                  range(63,91),
                  range(93,95),
                  range(97,16777215)
                ]))),
          [
            char(97),
            char(98)
          ])])]);
          
Tree subject =
          appl(
  prod(
    sort("AB"),
    [
      sort("A"),
      layouts("$default$"),
      sort("B")
    ],
    {}),
  [
    appl(
      prod(
        sort("A"),
        [lit("a")],
        {}),
      [appl(
          prod(
            lit("a"),
            [\char-class([range(97,97)])],
            {}),
          [char(97)])]),
    appl(
      prod(
        layouts("$default$"),
        [],
        {}),
      []),
    appl(
      prod(
        sort("B"),
        [lit("b")],
        {}),
      [appl(
          prod(
            lit("b"),
            [\char-class([range(98,98)])],
            {}),
          [char(98)])])
  ]);