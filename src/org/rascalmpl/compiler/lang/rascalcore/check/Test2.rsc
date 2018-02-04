
module lang::rascalcore::check::Test2
 lexical INT = [0-9]+; 
           start syntax EXP = intcon: INT intcon 
                      | exps: EXP+ exps
                      ;
           EXP exp;
           EXP+ es = exp.exps;