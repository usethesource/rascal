module lang::rascal::tests::basic::Types

import Type;

Symbol unwrap(\label(str lbl, Symbol s)) = unwrap(s);
Symbol unwrap(\parameter(str name, Symbol bound)) = unwrap(bound);
Symbol unwrap(\alias(str name, list[Symbol] parameters, Symbol s)) = unwrap(s);
default Symbol unwrap(Symbol s) = s;

// subtype

test bool subtype_same(type[value] s) = subtype(s, s);

@ignore{this fails for an interesting reason. TODO have to fix}
test bool sub_type_label(type[value] s, str lbl) = subtype(s, type(\label(lbl, s.symbol), ()));

test bool subtype_void(type[value] s) = subtype(#void, s);
test bool subtype_value(type[value] s) = subtype(s, #value);

test bool subtype_int_num() = subtype(\int(), \num());
test bool subtype_real_num() = subtype(\real(), \num());
test bool subtype_rat_num() = subtype(\rat(), \num());

test bool subtype_int_str() = !subtype(\int(), \str());

test bool subtype_list(type[value] s) = subtype(\list(s.symbol), \list(\value()));
test bool subtype_set(type[value] s) = subtype(\set(s.symbol), \set(\value()));
test bool subtype_map(type[value] k, type[value] v) = subtype(\map(k.symbol, v.symbol), \map(\value(), \value()));

test bool subtype_tuple1(type[value] s, type[value] t) = subtype(\tuple([s.symbol, t.symbol]), \tuple([\value(), \value()]));
test bool subtype_tuple2(type[value] s, type[value] t, type[value] u) = subtype(\tuple([s.symbol, t.symbol, u.symbol]), \tuple([\value(), \value(), \value()]));

test bool subtype_rel1(type[value] s, type[value] t) = subtype(\rel([s.symbol, t.symbol]), \rel([\value(), \value()]));
test bool subtype_rel2(type[value] s, type[value] t, type[value] u) = subtype(\rel([s.symbol, t.symbol, u.symbol]), \rel([\value(), \value(), \value()]));

test bool subtype_lrel1(type[value] s, type[value] t) = subtype(\lrel([s.symbol, t.symbol]), \lrel([\value(), \value()]));
test bool subtype_lrel2(type[value] s, type[value] t, type[value] u) = subtype(\lrel([s.symbol, t.symbol, u.symbol]), \lrel([\value(), \value(), \value()]));

// lub

test bool lub_value(type[value] s) = lub(s, #value) == #value && lub(#value, s) == #value;
test bool lub_void1(type[value] s) = lub(s, #void) == s;
test bool lub_void2(type[value] s) = lub(#void, s) == s;

test bool lub_int_real() = lub(\int(),\real()) == \num();
test bool lub_int_rat() = lub(\int(),\rat()) == \num();
test bool lub_real_rat() = lub(\real(),\rat()) == \num();

test bool lub_list_set(type[value] s) = lub(\list(s.symbol), \set(s.symbol)) == \value();

test bool lub_intstr() = lub(\int(), \str()) == \value();


