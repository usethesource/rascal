module lang::rascalcore::check::Test1 

import ParseTree;

syntax Commands
    = \commandlist: EvalCommand+ commands
    ;

syntax EvalCommand = "e";

value commands2patch(Commands pt) {
    return pt.args[0].args;
}