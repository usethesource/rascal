module Test2
import demo::PicoAbstract::PicoAbstractSyntax;
import demo::PicoAbstract::PicoAnalysis;

public void test(){
assert
 {<"x",pp(asgStat("x",sub(id("x"),natCon(1)))@(pos:3))>,
                             <"s",pp(asgStat("s",conc(id("s"),strCon("#")))@(pos:4))>,
                             <"x",pp(asgStat("x",natCon(3))@(pos:1))>})
                
 ==
 {<"s",pp(asgStat("s",conc(id("s"),strCon("#"))))>,
  <"x",pp(asgStat("x",sub(id("x"),natCon(1))))>,
  <"x",pp(asgStat("x",natCon(3)))>}
  ;  
  }import Test                       