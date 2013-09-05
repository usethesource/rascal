module analysis::m3::TypeSymbol

data TypeSymbol = \any();

bool subtype(\any(), \any()) = true; 
bool lub(\any(), \any()) = \any();
