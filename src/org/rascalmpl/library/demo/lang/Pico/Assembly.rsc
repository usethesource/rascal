module demo::lang::Pico::Assembly

import demo::lang::Pico::Abstract;

data Instr 
     = dclNat(PicoId Id)    // Reserve a memory location for a natural variable
     | dclStr(PicoId Id)    // Reserve a memory location for a string variable
     | pushNat(int intCon)  // Push integer constant on the stack
     | pushStr(str strCon)  // Push string constant on the stack
     | rvalue(PicoId Id)    // Push the value of a variable on the stack
     | lvalue(PicoId Id)    // Push the address of a variable on the stack
     | assign()             // Assign value on top, to variable at address top-1
     | add2()               // Replace top two stack values by their sum
     | sub2()               // Replace top two stack values by their difference
     | conc2()              // Replace top two stack values by their concatenation
     | label(str label)     // Associate a label with the next instruction
     | go(str label)        // Go to instruction with given label
     | gotrue(str label)    // Go to instruction with given label, if top equals 0
     | gofalse(str label)   // Go to instruction with given label, if top not equal to 0
     ;
