module TreeDataType

data Args list[Tree] args;  /** ERROR: Can not redeclare a named type Args with a tree sort type. **/

// type list[Tree] Args;   /** OK **?

data Tree appl(Production prod, Args args);
data Tree cycle(Symbol symbol, int length); // NatCon => int
data Tree amb(Args args);
data Tree char(int character);				// NatCon => int

data Production prod(Symbols lhs, Symbol rhs, Attributes attributes);
data Production \list(Symbol rhs);

public bool test(){
	return false;
	
}