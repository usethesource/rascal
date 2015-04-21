module experiments::Compiler::Tests::ValuesAndTypes

import ParseTree;

syntax A = "a";

syntax As = A*;

public As ValueAs1 = (As) `aaaaa`;

public As ValueAs2 = [As] "aaaaa";

public Symbol typeValueAs1 = typeOf(ValueAs1);

public value reifiedTypeAs = #As;

public list[As] ValueLA = [(As) `a`, (As) `aa`, (As) `aaa`];
