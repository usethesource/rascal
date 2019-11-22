module lang::rascal::tests::basic::Types

import Type;

Symbol unwrap(\label(str lbl, Symbol s)) = unwrap(s);
Symbol unwrap(\parameter(str name, Symbol bound)) = unwrap(bound);
Symbol unwrap(\alias(str name, list[Symbol] parameters, Symbol s)) = unwrap(s);
default Symbol unwrap(Symbol s) = s;

// subtype

test bool subtype_same(Symbol s) = subtype(s, s);

test bool sub_type_label(Symbol s, str lbl) = subtype(s, \label(lbl, s));

test bool subtype_void(Symbol s) = subtype(\void(), s);
test bool subtype_value(Symbol s) = subtype(s, \value());

test bool subtype_int_num() = subtype(\int(), \num());
test bool subtype_real_num() = subtype(\real(), \num());
test bool subtype_rat_num() = subtype(\rat(), \num());

test bool subtype_int_str() = !subtype(\int(), \str());

test bool subtype_list(Symbol s) = subtype(\list(s), \list(s));
test bool subtype_set(Symbol s) = subtype(\set(s), \set(s));
test bool subtype_map(Symbol k, Symbol v) = subtype(\map(k,v), \map(k, v));

test bool subtype_tuple1(Symbol s, Symbol t) = subtype(\tuple([s,t]), \tuple([s, t]));
test bool subtype_tuple2(Symbol s, Symbol t, Symbol u) = subtype(\tuple([s, t, u]), \tuple([s, t, u]));

test bool subtype_rel1(Symbol s, Symbol t) = subtype(\rel([s,t]), \rel([s, t]));
test bool subtype_rel2(Symbol s, Symbol t, Symbol u) = subtype(\rel([s,t, u]), \rel([s, t, u]));

test bool subtype_lrel1(Symbol s, Symbol t) = subtype(\lrel([s,t]), \lrel([s, t]));
test bool subtype_lrel2(Symbol s, Symbol t, Symbol u) = subtype(\lrel([s,t, u]), \lrel([s, t, u]));

// lub

test bool lub_value(Symbol s) = lub(s, \value()) == \value() && lub(\value(), s) == \value();
test bool lub_void1(Symbol s) = unwrap(lub(s, \void())) == unwrap(s);
test bool lub_void2(Symbol s) = unwrap(lub(\void(), s)) == unwrap(s);

test bool lub_int_real() = lub(\int(),\real()) == \num();
test bool lub_int_rat() = lub(\int(),\rat()) == \num();
test bool lub_real_rat() = lub(\real(),\rat()) == \num();

test bool lub_list_set(Symbol s) = lub(\list(s), \set(s)) == \value();

test bool lub_intstr() = lub(\int(), \str()) == \value();


