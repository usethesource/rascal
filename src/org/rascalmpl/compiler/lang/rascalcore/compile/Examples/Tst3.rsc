module lang::rascalcore::compile::Examples::Tst3

import IO;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeInstantiation;

AType curry_type =
afunc(
   afunc(
      aparameter(
        "S",
        avalue(),
        closed=true),
      [aparameter(
          "U",
          avalue(),
          closed=false)],
      []),
    [
      afunc(
        aparameter(
          "S",
          avalue(),
          closed=false),
        [
          aparameter(
            "T",
            avalue(),
            closed=false),
          aparameter(
            "U",
            avalue(),
            closed=false)
        ],
        [],
        alabel="f"),
      aparameter(
        "T",
        avalue(),
        closed=false,
        alabel="t")
    ],
    []);

AType curry_ret = curry_type.ret;
AType curry_arg0 = curry_type.formals[0];
AType curry_arg1 = curry_type.formals[1];
    
 AType addition_type =
 afunc(
    aint(),
    [
      aint(alabel="i"),
      aint(alabel="j")
    ],
    []);
    
 void main(){
    b1 = matchRascalTypeParams(makeUniqueTypeParams(curry_arg0, "1"), makeUniqueTypeParams(curry_type, "2"), ());
    b2 = matchRascalTypeParams(makeUniqueTypeParams(curry_arg1, "1"), makeUniqueTypeParams(addition_type, "2"), b1);
    
    iprintln(b2);
 }