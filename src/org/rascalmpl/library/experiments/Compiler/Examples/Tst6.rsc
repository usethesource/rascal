module experiments::Compiler::Examples::Tst6
import ParseTree;

syntax A = "a";
syntax As0 = A* as0; 
syntax As1 = A+ as1;

// Calls with concrete parameters

int cntAs0(A* as) = size([a | A a <- as ]);

int cntAs1(A+ as) = cntAs0(as);

test bool callAs1() = cntAs0(([As0] "").as0) == 0;
