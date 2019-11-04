module lang::rascal::tests::functionality::Visit2


import Grammar;
//import lang::rascal::grammar::definition::Parameters;
//import lang::rascal::grammar::definition::Literals;
//import IO;
//import String;
import ParseTree;

anno int Symbol@id;

Grammar G0 = grammar(
  {sort("S")[
      @id=2
    ]},
  (
    sort("S")[
      @id=3
    ]:choice(
      sort("S")[
        @id=4
      ],
      {prod(
          sort("S")[
            @id=5
          ],
          [lit("0")[
              @id=6
            ]],
          {})}),
    lit("0")[
      @id=7
    ]:choice(
      lit("0")[
        @id=8
      ],
      {prod(
          lit("0")[
            @id=9
          ],
          [\char-class([range(48,48)])[
              @id=10
            ]],
          {})})
  ));

test bool cntLit()    {cnt = 0; visit(G0){ case lit(_): cnt += 1;}; return cnt == 4; }
 
test bool cntLitCC()  {cnt = 0; visit(G0){ case lit(_): cnt += 1; case \char-class(_): cnt += 1;} return cnt == 5; }

test bool cntInt()    {cnt = 0; visit(G0){ case int n: cnt += 1; } return cnt == 2; }

test bool cntStr()    {cnt = 0; visit(G0){ case str s: cnt += 1; } return cnt == 8; }

test bool cntIntStr() {cnt = 0; visit(G0){ case int n: cnt += 1; case str s: cnt += 1; } return cnt == 10; }
   
test bool cntProd()   {cnt = 0; visit(G0){case prod(_,_,_): cnt += 1;} return cnt == 2; }