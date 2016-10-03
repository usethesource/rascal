module experiments::Compiler::Examples::Tst1

lexical X = [xyzXYZ];
lexical XPlus = X+ xs1;

//@ignoreInterpreter{Incorrect/not implemented}
//@expected{IllegalArgument}
//test bool lexIllegalSlice() { ([XPlus] "xyz").xs1[0 .. 0]; return false; }

value main() =  ([XPlus] "xyz").xs1[0 .. 0];