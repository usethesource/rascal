module lang::rascalcore::compile::Examples::Right
extend lang::rascalcore::compile::Examples::Bottom;
data Exp = and(Bool lhs, Bool rhs);
data Exp2 = or(Exp lhs, Exp rhs);