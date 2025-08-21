module lang::rascal::tests::basic::Types

import Type;


// equals is an equivalence relation
test bool typeEqualsReflexive(type[value] t) = t == t;

test bool typeEqualsTransitive(type[value] s, type[value] t, type[value] u)
    = s == t && t == u ==> s == u;

test bool typeEqualsSymmetric(type[value] s, type[value] t) 
    = t == s <==> s == t;

// subtype is a partial order

test bool subtypeReflexive(type[value] s) = subtype(s, s);

test bool subtypeTransitive(type[value] s, type[value] t, type[value] u)
    = subtype(s, t) && subtype(t,u) ==> subtype(s, u);

test bool subtypeAntiSymmetric(type[value] s, type[value] t)
    = subtype(s, t) && subtype(t, s) ==> s == t;

// lattice laws
test bool lubIsIdempotent(type[value] s)
    = lub(s, s) == s;

test bool glbIsIdempotent(type[value] s)
    = glb(s, s) == s;

test bool valueIsMax(type[value] s, type[value] t)
    = subtype(lub(s, t), #value);

test bool voidIsMin(type[value] s, type[value] t)
    = subtype(#void, glb(s, t));

test bool voidIsMin2(type[value] s) = subtype(#void, s);

test bool valueIsMax2(type[value] s)   = subtype(s, #value);

test bool absorption1(type[value] a, type[value] b)
    = lub(a, glb(a, b)) == a;

test bool absorption2(type[value] a, type[value] b)
    = glb(a, lub(a, b)) == a;

test bool lubReflectsSubtype(type[value] a, type[value] b)
    = subtype(a, lub(a, b)) && subtype(b, lub(a,b));

test bool glbReflectsSubtype(type[value] a, type[value] b)
    = subtype(glb(a, b), a) && subtype(glb(a,b), b);

test bool monotonicity(type[value] a1, type[value] a2, type[value] b1, type[value] b2)
    = subtype(a1, a2) && subtype(b1, b2)
        ==> subtype(glb(a1, b2), glb(a2,b2)) && subtype(glb(a1, b1), glb(a2, b2));

// labels

test bool subtypeLabelsAreAliasesLeft(type[value] s, str lbl) = subtype(s, type(\label(lbl, s.symbol), ()));
test bool subtypeLabelsAreAliasesRight(type[value] s, str lbl) = subtype(type(\label(lbl, s.symbol), ()), s);

// ADT'S

data Exp = exp();
test bool subtypeNode() = subtype(#Exp, #node);

// numerics

test bool subtype_int_num() = subtype(\int(), \num());
test bool subtype_real_num() = subtype(\real(), \num());
test bool subtype_rat_num() = subtype(\rat(), \num());
test bool subtypeNum(type[num] t) = subtype(t, #num);
test bool lubNum(type[num] t, type[num] u) = lub(t, u) == #num;

// smoke test int != str
test bool subtype_int_str() = !subtype(\int(), \str());

// co-variance
test bool subtype_list(type[value] s) = subtype(\list(s.symbol), \list(\value()));
test bool subtype_set(type[value] s) = subtype(\set(s.symbol), \set(\value()));
test bool subtype_map(type[value] k, type[value] v) = subtype(\map(k.symbol, v.symbol), \map(\value(), \value()));

test bool subtype_tuple1(type[value] s, type[value] t) = subtype(\tuple([s.symbol, t.symbol]), \tuple([\value(), \value()]));
test bool subtype_tuple2(type[value] s, type[value] t, type[value] u) = subtype(\tuple([s.symbol, t.symbol, u.symbol]), \tuple([\value(), \value(), \value()]));

test bool subtype_rel1(type[value] s, type[value] t) = subtype(\rel([s.symbol, t.symbol]), \rel([\value(), \value()]));
test bool subtype_rel2(type[value] s, type[value] t, type[value] u) = subtype(\rel([s.symbol, t.symbol, u.symbol]), \rel([\value(), \value(), \value()]));

test bool subtype_lrel1(type[value] s, type[value] t) = subtype(\lrel([s.symbol, t.symbol]), \lrel([\value(), \value()]));
test bool subtype_lrel2(type[value] s, type[value] t, type[value] u) = subtype(\lrel([s.symbol, t.symbol, u.symbol]), \lrel([\value(), \value(), \value()]));

// tuples

// there can exist no tuples that take a void parameter, hence types with those void parameters represent an _empty set_ of values, hence such tuples types are equivalent to `void`
test bool tuplesWithVoidParametersDoNotExist1() = equivalent(type(\tuple([\void()]),()), #void);
test bool tuplesWithVoidParametersDoNotExist2() = equivalent(type(\tuple([\void(),\void()]),()),#void);

// functions

// there can exist no functions that take a void parameter, hence types with those void parameters represent an _empty set_ of values, hence such function types are equivalent to `void`
test bool functionsWithVoidParametersDoNotExist1() = #int(void) == #void;
test bool functionsWithVoidParametersDoNotExist2() = #int(void, void) == #void;
test bool functionsWithVoidParametersDoNotExist3() = #int(void a, void b) == #void;

test bool functionParametersAreCoVariant() = subtype(#int(int), #int(num));
test bool functionParametersAreContraVariant() = subtype(#int(num), #int(int));
test bool functionsParametersLubUp() = lub(#int(int), #int(str)) == #int(value);

test bool functionReturnsAreCoVariant() = subtype(#int(int), #value(int));

// lub

test bool lub_value(type[value] s) = lub(s, #value) == #value && lub(#value, s) == #value;
test bool lub_void1(type[value] s) = lub(s, #void) == s;
test bool lub_void2(type[value] s) = lub(#void, s) == s;

test bool lub_int_real() = lub(\int(),\real()) == \num();
test bool lub_int_rat() = lub(\int(),\rat()) == \num();
test bool lub_real_rat() = lub(\real(),\rat()) == \num();

test bool lub_list_set(type[value] s) = lub(\list(s.symbol), \set(s.symbol)) == \value();

test bool lub_intstr() = lub(\int(), \str()) == \value();


