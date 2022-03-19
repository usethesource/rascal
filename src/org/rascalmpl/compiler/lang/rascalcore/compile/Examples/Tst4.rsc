module lang::rascalcore::compile::Examples::Tst4

import ParseTree;

Tree T =
appl(
  prod(
    sort("Expression"),
    [label(
        "function",
        alt({
            lex("BuiltIn"),
            lex("Identifier")
          }))],
    {}),
  [appl(
      regular(alt({
            lex("BuiltIn"),
            lex("Identifier")
          })),
      [appl(
          prod(
            lex("BuiltIn"),
            [lit("hoi")],
            {}),
          [appl(
              prod(
                lit("hoi"),
                [
                  \char-class([range(104,104)]),
                  \char-class([range(111,111)]),
                  \char-class([range(105,105)])
                ],
                {}),
              [char(104),char(111),char(105)])],
          src=|unknown:///|(0,3,<1,0>,<1,3>))],
      src=|unknown:///|(0,3,<1,0>,<1,3>))],
  src=|unknown:///|(0,3,<1,0>,<1,3>));
  
 
 data Tree(loc src = |unknown:///|);
 
 value main(){
  
    T2 = visit(T) {
                case Tree t => t[@\loc = |blabla:///| ]
                    when t@\loc?
              };
    return T2.prod.def;
 }             