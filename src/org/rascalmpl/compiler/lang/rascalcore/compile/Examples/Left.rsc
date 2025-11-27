module lang::rascalcore::compile::Examples::Left

extend lang::rascalcore::compile::Examples::Bottom;
data Exp = or(Exp lhs, Exp rhs)| maybe() | \true() | \false();