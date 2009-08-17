module demo::Rules::ConcreteBool

import demo::Rules::BoolSyntax;
import UnitTest;

// An atypical Rascal example that reminds us of algebraic specifications
// with concrete syntax in the style of ASF+SDF.

// We import the syntax for concrete Boolean expression of type Bool with 
// constants btrue and bfalse and operators & and |.

// Also see AbstractBool.rsc for a, abstract version of Booleans.

// Rewrite rules are used to simplify & and | terms.

Bool b = [|btrue|];

rule a1 [| btrue & <Bool B2> |]   => B2;
rule a2 [| bfalse & <Bool B2> |]  => [|bfalse|];

rule o1 [| btrue | btrue |]       => [|btrue|];
rule o2 [| btrue | bfalse |]      =>[| btrue|];
rule o3 [| bfalse | btrue |]      => [|btrue|];
rule o4 [| bfalse | bfalse |]     => [|bfalse|];

public bool test(){
  assertEqual([|btrue|], [|btrue|]);
  assertEqual([|btrue | btrue|], [|btrue|]);
  assertEqual([|bfalse | btrue|], [|btrue|]);
  assertEqual([|bfalse & bfalse|], [|bfalse|]);
  return report("ConcreteBool");
}