module lang::rascalcore::grammar::tests::TestGrammars

//import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::check::AType;

public AGrammar GEMPTY = grammar({sort("S")}, ());

AType \sort(str sname)      = aadt(sname, [], contextFreeSyntax());

private AProduction pr(AType rhs, list[AType] lhs) {
  return prod(rhs,lhs);
}

public AGrammar G0 = grammar({sort("S")}, (
    sort("S"): choice(sort("S"), { pr(sort("S"), [ alit("0") ]) }),
    alit("0"): choice(alit("0"), { pr(alit("0"),[\achar-class([arange(48,48)])]) })
));

public map[AType sort, AProduction def] Lit1 = (
  alit("*"): choice(alit("*"), { pr(alit("*"),[\achar-class([arange(42,42)])]) }),
  alit("+"): choice(alit("+"), { pr(alit("+"),[\achar-class([arange(43,43)])]) }),
  alit("0"): choice(alit("0"), { pr(alit("0"),[\achar-class([arange(48,48)])]) }),
  alit("1"): choice(alit("1"), { pr(alit("1"),[\achar-class([arange(49,49)])]) })
);

public AGrammar GEXP = grammar({sort("E")}, (
    sort("E"): choice(sort("E"), { pr(sort("E"), [sort("E"), alit("*"), sort("B")]),
                                   pr(sort("E"), [sort("E"), alit("+"), sort("B")]),
                                   pr(sort("E"), [sort("B")])
                                 }),
    sort("B"): choice(sort("B"), { pr(sort("B"), [alit("0")]),
                                  pr(sort("B"), [alit("1")])
                                 })
) + Lit1);

public AGrammar GEXPPRIO = grammar( {sort("E")},
(
    sort("E"):  choice(sort("E"), { pr(sort("E"),  [sort("T"), sort("E1")])}),
    sort("E1"): choice(sort("E1"),{ pr(sort("E1"), [alit("+"), sort("T"), sort("E1")]),
                                    pr(sort("E1"), [])
                                  }),
    
    
    sort("T"):  choice(sort("T"), { pr(sort("T"),  [sort("F"), sort("T1")]) }),
   
    sort("T1"): choice(sort("T1"),{ pr(sort("F"), [alit("*"), sort("F"), sort("T1")]),
                                   pr(sort("T1"), []) }),
                                    
    sort("F"): choice(sort("F"),  { pr(sort("F"),  [alit("("), sort("E"), alit(")")]),
                                    pr(sort("F"),  [alit("id")])
                                  }),
    
    alit("+"): choice(alit("+"), { pr(alit("+"),[\achar-class([arange(43,43)])]) }),
    alit("*"): choice(alit("*"), { pr(alit("*"),[\achar-class([arange(42,42)])]) }),
    
    alit("("): choice(alit("("), { pr(alit("("), [\achar-class([arange(40,40)])]) }),
    alit(")"): choice(alit(")"), { pr(alit(")"), [\achar-class([arange(41,41)])]) }),
    
    alit("id"): choice(alit("id"), { pr(alit("id"), [\achar-class([arange(105,105)]),\achar-class([arange(100,100)])]) })
));