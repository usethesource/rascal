@synopsis{Standard format for boolean formulas in conjunctive normal form.}
@description{
This format is used by the yearly SAT competition, see https://www.satcompetition.org/ .
}
@examples{
This is a sample `.cnf` file:
```dimacs
c A sample .cnf file.
p cnf 3 2
1 -3 0
2 3 -1 0 
```
}
module lang::dimacs::\syntax::Dimacs

layout L = [\t\ \r]*;

lexical Comment = comment: "c" ![\n]* "\n";
lexical Prologue = prologue: "p" "cnf" Number variables Number clauses ![\n]* "\n";

lexical Number 
  = positive: [0-9]+ !>> [0-9]
  | non-assoc negative: "-" Number number
  ;
              
start syntax Dimacs
  = Prologue prologue {Line "\n"}+ lines "\n";

syntax Line 
  = disjunct: Disjunct disjunct
  | comment: Comment comment
  ;

syntax Disjunct = Number+ numbers; 
