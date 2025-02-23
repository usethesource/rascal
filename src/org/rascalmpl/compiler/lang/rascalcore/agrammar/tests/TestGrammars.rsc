@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::agrammar::tests::TestGrammars

//import lang::rascalcore::agrammar::definition::Grammar;
import lang::rascalcore::check::AType;

public AGrammar GEMPTY = grammar({sort("S")}, ());

AType \sort(str sname)      = aadt(sname, [], contextFreeSyntax());

private AProduction pr(AType rhs, list[AType] lhs) {
  return prod(rhs,lhs);
}

public AGrammar G0 = grammar({sort("S")}, (
    sort("S"): achoice(sort("S"), { pr(sort("S"), [ alit("0") ]) }),
    alit("0"): achoice(alit("0"), { pr(alit("0"),[\achar-class([arange(48,48)])]) })
));

public map[AType sort, AProduction def] Lit1 = (
  alit("*"): achoice(alit("*"), { pr(alit("*"),[\achar-class([arange(42,42)])]) }),
  alit("+"): achoice(alit("+"), { pr(alit("+"),[\achar-class([arange(43,43)])]) }),
  alit("0"): achoice(alit("0"), { pr(alit("0"),[\achar-class([arange(48,48)])]) }),
  alit("1"): achoice(alit("1"), { pr(alit("1"),[\achar-class([arange(49,49)])]) })
);

public AGrammar GEXP = grammar({sort("E")}, (
    sort("E"): achoice(sort("E"), { pr(sort("E"), [sort("E"), alit("*"), sort("B")]),
                                   pr(sort("E"), [sort("E"), alit("+"), sort("B")]),
                                   pr(sort("E"), [sort("B")])
                                 }),
    sort("B"): achoice(sort("B"), { pr(sort("B"), [alit("0")]),
                                  pr(sort("B"), [alit("1")])
                                 })
) + Lit1);

public AGrammar GEXPPRIO = grammar( {sort("E")},
(
    sort("E"):  achoice(sort("E"), { pr(sort("E"),  [sort("T"), sort("E1")])}),
    sort("E1"): achoice(sort("E1"),{ pr(sort("E1"), [alit("+"), sort("T"), sort("E1")]),
                                    pr(sort("E1"), [])
                                  }),
    
    
    sort("T"):  achoice(sort("T"), { pr(sort("T"),  [sort("F"), sort("T1")]) }),
   
    sort("T1"): achoice(sort("T1"),{ pr(sort("F"), [alit("*"), sort("F"), sort("T1")]),
                                   pr(sort("T1"), []) }),
                                    
    sort("F"): achoice(sort("F"),  { pr(sort("F"),  [alit("("), sort("E"), alit(")")]),
                                    pr(sort("F"),  [alit("id")])
                                  }),
    
    alit("+"): achoice(alit("+"), { pr(alit("+"),[\achar-class([arange(43,43)])]) }),
    alit("*"): achoice(alit("*"), { pr(alit("*"),[\achar-class([arange(42,42)])]) }),
    
    alit("("): achoice(alit("("), { pr(alit("("), [\achar-class([arange(40,40)])]) }),
    alit(")"): achoice(alit(")"), { pr(alit(")"), [\achar-class([arange(41,41)])]) }),
    
    alit("id"): achoice(alit("id"), { pr(alit("id"), [\achar-class([arange(105,105)]),\achar-class([arange(100,100)])]) })
));