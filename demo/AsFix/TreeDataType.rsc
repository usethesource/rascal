module TreeDataType

data Tree appl(Production prod, Args args);
data Tree cycle(Symbol symbol, int length); // NatCon => int
data Tree amb(Args args);
data Tree char(int character);				// NatCon => int

type list[Tree] Args;

data Production prod(Symbols lhs, Symbol rhs, Attributes attributes);
data Production \list(Symbol rhs);