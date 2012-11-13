@doc{
  This is an experimental extension of the Rascal grammar for statically parsing embedded concrete syntax literals
}
module lang::rascal::syntax::ConcreteSyntaxLiterals

extend lang::rascal::syntax::RascalRascal;

syntax TExpression = concrete: Concrete[Expression];
syntax TPattern    = concrete: Concrete[Pattern];

syntax Concrete[&Hole] = \default: "(" Sym ")" ConcreteString[&Hole];

lexical ConcreteString[&Hole] = \default: "`" ConcretePart[&Hole]* "`";

lexical ConcretePart[&Hole]
  = text   : ![`\<\>\\\n]
  | newline: "\n" [\ \t \u00A0 \u1680 \u2000-\u2000A \u202F \u205F \u3000]* "\'"
  | hole   : ConcreteHole[&Hole]
  | lt: "\\\<"
  | gt: "\\\>"
  | bq: "\\`"
  | bs: "\\\\"
  ;
  
syntax ConcreteHole[&Hole] = \default: "\<" &Hole "\>";