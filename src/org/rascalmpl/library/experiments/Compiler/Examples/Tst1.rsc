module experiments::Compiler::Examples::Tst1

import ParseTree;

syntax D = dd: "d";
syntax D = ee: "e" D d;


bool dispatch(ee(D _)) = true;
bool dispatch(dd()) = false;

bool fun(int n) = true;
bool fun(str s) = true;

value main(list[value] args) = dispatch((D) `ed`); // && !*/ dispatch((D) `d`);