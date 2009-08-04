module demo::Rules::BoolConcrete

import demo::Rules::BoolSyntax;
import UnitTest;

rule a1 btrue & <Bool B2>   => B2;
rule a2 bfalse & <Bool B2>  => bfalse;

rule o1 btrue | btrue       => btrue;
rule o2 btrue | bfalse      => btrue;
rule o3 bfalse | btrue      => btrue;
rule o4 bfalse | bfalse     => bfalse;

public bool test(){
  assertEqual(btrue, btrue);
  assertEqual(btrue | btrue, btrue);
  assertEqual(bfalse | btrue, btrue);
  assertEqual(bfalse & bfalse, bfalse);
  return report();
}