module experiments::Compiler::Examples::Tst2

import ParseTree;

syntax D = d: "d" | e: "e" D d;

bool dispatch(e(D _)) = true;
bool dispatch(d()) = false;

value main(list[value] args) = dispatch((D) `ed`);		// &&*/ !dispatch((D) `d`);
