module lang::rascal::tests::functionality::Visit2

import Grammar;
import ParseTree;

 
data Symbol(int id = 0);

Grammar G0 = grammar(
  {sort("S")[id=2]},
  (
    sort("S")[id=3]:choice(
      sort("S")[id=4],
      {prod(
          sort("S")[id=5],
          [lit("0")[id=6]],
          {})}),
    lit("0")[id=7]:choice(
      lit("0")[id=8],
      {prod(
          lit("0")[id=9],
          [\char-class([range(48,48)])[id=10]],
          {})})
  ));

test bool cntLit()    {cnt = 0; visit(G0){ case lit(_): cnt += 1;}; return cnt == 4; }
 
test bool cntLitCC()  {cnt = 0; visit(G0){ case lit(_): cnt += 1; case \char-class(_): cnt += 1;} return cnt == 5; }

test bool cntInt()    {cnt = 0; visit(G0){ case int _: cnt += 1; } return cnt == 11; } // visit does go into kw params

test bool cntStr()    {cnt = 0; visit(G0){ case str _: cnt += 1; } return cnt == 8; }

test bool cntIntStr() {cnt = 0; visit(G0){ case int _: cnt += 1; case str _: cnt += 1; } return cnt == 19; }
   
test bool cntProd()   {cnt = 0; visit(G0){case prod(_,_,_): cnt += 1;} return cnt == 2; }
