module lang::rascal::tests::basic::Types

import Type;

test bool subtype_int() = subtype(\int(), \int());
test bool subtype_intstr() = !subtype(\int(), \str());

test bool subtype_void(Symbol s) = subtype(\void(), s);
test bool subtype_value(Symbol s) = subtype(s, \value());

test bool subtype2(Symbol s) = subtype(s, s);
test bool subtype_list(Symbol s) = subtype(\list(s), \list(s));
test bool subtype_set(Symbol s) = subtype(\set(s), \set(s));
test bool subtype_map(Symbol k, Symbol v) = subtype(\map(k,v), \map(k, v));

test bool subtype_tuple1(Symbol s, Symbol t) = subtype(\tuple([s,t]), \tuple([s, t]));
test bool subtype_tuple2(Symbol s, Symbol t, Symbol u) = subtype(\tuple([s,t, u]), \tuple([s, t, u]));

test bool subtype_rel1(Symbol s, Symbol t) = subtype(\rel([s,t]), \rel([s, t]));
test bool subtype_rel2(Symbol s, Symbol t, Symbol u) = subtype(\rel([s,t, u]), \rel([s, t, u]));

test bool subtype_lrel1(Symbol s, Symbol t) = subtype(\lrel([s,t]), \lrel([s, t]));
test bool subtype_lrel2(Symbol s, Symbol t, Symbol u) = subtype(\lrel([s,t, u]), \lrel([s, t, u]));


test bool lub_int() = lub(\int(), \int()) == \int();
test bool lub_intstr() = lub(\int(), \str()) == \value();


