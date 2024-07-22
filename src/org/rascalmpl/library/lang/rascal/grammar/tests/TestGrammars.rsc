module lang::rascal::grammar::tests::TestGrammars

import Grammar;
import ParseTree;

public Grammar GEMPTY = grammar({sort("S")}, ());

private Production pr(Symbol rhs, list[Symbol] lhs) {
  return prod(rhs,lhs,{});
}

public Grammar G0 = grammar({sort("S")}, (
    sort("S"): choice(sort("S"), { pr(sort("S"), [ lit("0") ]) }),
    lit("0"): choice(lit("0"), { pr(lit("0"),[\char-class([range(48,48)])]) })
));

public map[Symbol sort, Production def] Lit1 = (
  lit("*"): choice(lit("*"), { pr(lit("*"),[\char-class([range(42,42)])]) }),
  lit("+"): choice(lit("+"), { pr(lit("+"),[\char-class([range(43,43)])]) }),
  lit("0"): choice(lit("0"), { pr(lit("0"),[\char-class([range(48,48)])]) }),
  lit("1"): choice(lit("1"), { pr(lit("1"),[\char-class([range(49,49)])]) })
);

public Grammar GEXP = grammar({sort("E")}, (
    sort("E"): choice(sort("E"), { pr(sort("E"), [sort("E"), lit("*"), sort("B")]),
                                   pr(sort("E"), [sort("E"), lit("+"), sort("B")]),
                                   pr(sort("E"), [sort("B")])
                                 }),
    sort("B"): choice(sort("B"), { pr(sort("B"), [lit("0")]),
                                  pr(sort("B"), [lit("1")])
                                 })
) + Lit1);

public Grammar GEXPPRIO = grammar( {sort("E")},
(
    sort("E"):  choice(sort("E"), { pr(sort("E"),  [sort("T"), sort("E1")])}),
    sort("E1"): choice(sort("E1"),{ pr(sort("E1"), [lit("+"), sort("T"), sort("E1")]),
                                    pr(sort("E1"), [])
                                  }),
    
    
    sort("T"):  choice(sort("T"), { pr(sort("T"),  [sort("F"), sort("T1")]) }),
   
    sort("T1"): choice(sort("T1"),{ pr(sort("F"), [lit("*"), sort("F"), sort("T1")]),
                                   pr(sort("T1"), []) }),
                                    
    sort("F"): choice(sort("F"),  { pr(sort("F"),  [lit("("), sort("E"), lit(")")]),
                                    pr(sort("F"),  [lit("id")])
                                  }),
    
    lit("+"): choice(lit("+"), { pr(lit("+"),[\char-class([range(43,43)])]) }),
    lit("*"): choice(lit("*"), { pr(lit("*"),[\char-class([range(42,42)])]) }),
    
    lit("("): choice(lit("("), { pr(lit("("), [\char-class([range(40,40)])]) }),
    lit(")"): choice(lit(")"), { pr(lit(")"), [\char-class([range(41,41)])]) }),
    
    lit("id"): choice(lit("id"), { pr(lit("id"), [\char-class([range(105,105)]),\char-class([range(100,100)])]) })
));
