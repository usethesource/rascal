module demo::ConcretePico::UseDef

import languages::pico::syntax::Pico;  // Pico concrete syntax
import demo::ConcretePico::Programs;   // Example programs

import IO;
import UnitTest;

/*
 * Compute variable uses:
 * - Associate all variable uses in an expression with the enclosing expression.
 * - Associate all variable uses in an assignment statement with that statement.
 */
public rel[\PICO-ID, Tree] uses(PROGRAM P) {
  println("Expressions: ", {E | EXP E <- P});
  return {Id | EXP E <- P, `<\PICO-ID Id>` <- E};
  //return {Id | EXP E <- P, `<\PICO-ID Id>` <- E};
}

public rel[\PICO-ID, STATEMENT] defs(PROGRAM P) { 
  return {<Id, S> | STATEMENT S <- P, ` <\PICO-ID Id> := <EXP Exp> ` := S};
}

public bool test(){

  assertEqual(uses(`begin declare x : natural; x := 3; x := x + 1 end`), {});
  
  assertEqual(checkProgram(`begin declare x : natural; y := "a"  end`), 
                  [message("Undeclared variable y")]);
                  
  assertEqual(checkProgram(`begin declare x : natural; x := "a"  end`), 
                  [message("Type error: expected natural got \"a\"")]);
                  
  assertEqual(checkProgram(`begin declare x : natural; x := 2 + "a"  end`), 
                  [message("Type error: expected natural got \"a\"")]);

  assertEqual(uses(small), {} );  
  
  //assertEqual(defs(annotate(small)), {<"s",7>,<"x",1>,<"x",3>});
  
  //assertEqual(uses(annotate(fac)), {<"repnr",37>,<"rep",9>,<"output",5>,<"repnr",13>,<"input",27>,<"input",32>,<"output",9>,<"input",7>}); 

  //assertEqual(defs(annotate(fac)), {<"repnr",7>,<"output",9>,<"input",1>,<"repnr",13>,<"input",27>,<"rep",5>,<"output",3>} );

  return report("AbstractPico::UseDef");
}
