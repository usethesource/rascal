module experiments::Compiler::Tests::CompareValuesAndTypes

import experiments::Compiler::Tests::ValuesAndTypes;

import Type;

test bool tstValueAs1() = ValueAs1 == (As) `aaaaa`;

//test bool tstValueAs2() = ValueAs2 == [As] "aaaaa";
//
//test bool tstTypeValueAs1() = typeValueAs1 == typeOf(ValueAs1);
//
//test bool tstReifiedTypeAs() = reifiedTypeAs == #As;
//
//test bool tstValueLA() = ValueLA == [(As) `a`, (As) `aa`, (As) `aaa`];
