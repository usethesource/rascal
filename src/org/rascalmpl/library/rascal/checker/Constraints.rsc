module rascal::checker::Constraints

data Constraint =
          LocHasType(loc l, RType t)
        | TypeEquivType(RType t1, RType t2)
	| TypeSubtypeOfType(RType t1, RType t2)
	| TypeIsOneOf(RType t1, set[RType] ts)
        | AndConstraint(Constraint c1, Constraint c1)
	| OrConstraint(Constraint c1, Constraint c1)
	;

data RType =
          RFreshType(int n)
	;

alias Constraints = set[Constraint];

public Constraints solveConstraints(Constraints constraints) {
        bool keepgoing = true;
        while (keepgoing) {
	        Constraint c1 = getOneFrom(constraints);
		
	}
}

data RType =
          V(int sv)
        | L(RType l, RType r)
	| S(int sv)
        | C(RType t, set[RTypeConstraint] tc)
	;

data RTypeConstraint =
          Sub(RType l, RType r)
        ;
//
// Rules used for building constraints
//
data TypeRule = TypeRule(list[RType] domain, RType range);

set[TypeRule] plusRules = { 
                            TypeRule([ RIntType(), RIntType() ], RIntType()),
                            TypeRule([ RIntType(), RRealType() ], RRealType()),
                            TypeRule([ RIntType(), RNumType() ], RNumType()),

                            TypeRule([ RRealType(), RIntType() ], RRealType()),
                            TypeRule([ RRealType(), RRealType() ], RRealType()),
                            TypeRule([ RRealType(), RNumType() ], RNumType()),

                            TypeRule([ RNumType(), RIntType() ], RNumType()),
                            TypeRule([ RNumType(), RRealType() ], RNumType()),
                            TypeRule([ RNumType(), RNumType() ], RNumType()),

                            TypeRule([ RStrType(), RStrType() ], RStrType()),

                            TypeRule([ RLocType(), RStrType() ], RLocType()),

                            TypeRule([ V(0), RListType(V(1)) ], RListType(L(V(0),V(1)))),
                            TypeRule([ RListType(V(0)), V(1) ], RListType(L(V(0),V(1)))),
                            TypeRule([ RListType(V(0)), RListType(V(1)) ], RListType(L(V(0),V(1)))),

                            TypeRule([ V(0), RSetType(V(1)) ], RSetType(L(V(0),V(1)))),
                            TypeRule([ RSetType(V(0)), V(1) ], RSetType(L(V(0),V(1)))),
                            TypeRule([ RSetType(V(0)), RSetType(V(1)) ], RSetType(L(V(0),V(1)))),

                            TypeRule([ V(0), RBagType(V(1)) ], RBagType(L(V(0),V(1)))),
                            TypeRule([ RBagType(V(0)), V(1) ], RBagType(L(V(0),V(1)))),
                            TypeRule([ RBagType(V(0)), RBagType(V(1)) ], RBagType(L(V(0),V(1)))),

			    TypeRule([ RTupleType([S(0)]), RTupleType([S(1)])], RTupleType([S(0),S(1)]))
};

set[TypeRule] minusRules = {
                            TypeRule([ RIntType(), RIntType() ], RIntType()),
                            TypeRule([ RIntType(), RRealType() ], RRealType()),
                            TypeRule([ RIntType(), RNumType() ], RNumType()),

                            TypeRule([ RRealType(), RIntType() ], RRealType()),
                            TypeRule([ RRealType(), RRealType() ], RRealType()),
                            TypeRule([ RRealType(), RNumType() ], RNumType()),

                            TypeRule([ RNumType(), RIntType() ], RNumType()),
                            TypeRule([ RNumType(), RRealType() ], RNumType()),
                            TypeRule([ RNumType(), RNumType() ], RNumType()),
};

set[TypeRule] prodRules = {
                            TypeRule([ RIntType(), RIntType() ], RIntType()),
                            TypeRule([ RIntType(), RRealType() ], RRealType()),
                            TypeRule([ RIntType(), RNumType() ], RNumType()),

                            TypeRule([ RRealType(), RIntType() ], RRealType()),
                            TypeRule([ RRealType(), RRealType() ], RRealType()),
                            TypeRule([ RRealType(), RNumType() ], RNumType()),

                            TypeRule([ RNumType(), RIntType() ], RNumType()),
                            TypeRule([ RNumType(), RRealType() ], RNumType()),
                            TypeRule([ RNumType(), RNumType() ], RNumType()),
};

set[TypeRule] divRules = {
                            TypeRule([ RIntType(), RIntType() ], RIntType()),
                            TypeRule([ RIntType(), RRealType() ], RRealType()),
                            TypeRule([ RIntType(), RNumType() ], RNumType()),

                            TypeRule([ RRealType(), RIntType() ], RRealType()),
                            TypeRule([ RRealType(), RRealType() ], RRealType()),
                            TypeRule([ RRealType(), RNumType() ], RNumType()),

                            TypeRule([ RNumType(), RIntType() ], RNumType()),
                            TypeRule([ RNumType(), RRealType() ], RNumType()),
                            TypeRule([ RNumType(), RNumType() ], RNumType()),
};

set[TypeRule] modRules = {
                            TypeRule([ RIntType(), RIntType() ], RIntType()),

                            TypeRule([ RNumType(), RIntType() ], RNumType()),
                            TypeRule([ RNumType(), RRealType() ], RNumType()),
                            TypeRule([ RNumType(), RNumType() ], RNumType()),
};

set[TypeRule] ltRules = {
                            TypeRule([ RBoolType(), RBoolType() ], RBoolType()),

                            TypeRule([ RIntType(), RIntType() ], RBoolType()),
                            TypeRule([ RIntType(), RRealType() ], RBoolType()),
                            TypeRule([ RIntType(), RNumType() ], RBoolType()),

                            TypeRule([ RRealType(), RIntType() ], RBoolType()),
                            TypeRule([ RRealType(), RRealType() ], RBoolType()),
                            TypeRule([ RRealType(), RNumType() ], RBoolType()),

                            TypeRule([ RNumType(), RIntType() ], RBoolType()),
                            TypeRule([ RNumType(), RRealType() ], RBoolType()),
                            TypeRule([ RNumType(), RNumType() ], RBoolType()),

                            TypeRule([ RStrType(), RStrType() ], RBoolType()),
};

set[TypeRule] lteqRules = {
                            TypeRule([ RBoolType(), RBoolType() ], RBoolType()),

                            TypeRule([ RIntType(), RIntType() ], RBoolType()),
                            TypeRule([ RIntType(), RRealType() ], RBoolType()),
                            TypeRule([ RIntType(), RNumType() ], RBoolType()),

                            TypeRule([ RRealType(), RIntType() ], RBoolType()),
                            TypeRule([ RRealType(), RRealType() ], RBoolType()),
                            TypeRule([ RRealType(), RNumType() ], RBoolType()),

                            TypeRule([ RNumType(), RIntType() ], RBoolType()),
                            TypeRule([ RNumType(), RRealType() ], RBoolType()),
                            TypeRule([ RNumType(), RNumType() ], RBoolType()),

                            TypeRule([ RStrType(), RStrType() ], RBoolType()),
};

set[TypeRule] gtRules = {
                            TypeRule([ RBoolType(), RBoolType() ], RBoolType()),

                            TypeRule([ RIntType(), RIntType() ], RBoolType()),
                            TypeRule([ RIntType(), RRealType() ], RBoolType()),
                            TypeRule([ RIntType(), RNumType() ], RBoolType()),

                            TypeRule([ RRealType(), RIntType() ], RBoolType()),
                            TypeRule([ RRealType(), RRealType() ], RBoolType()),
                            TypeRule([ RRealType(), RNumType() ], RBoolType()),

                            TypeRule([ RNumType(), RIntType() ], RBoolType()),
                            TypeRule([ RNumType(), RRealType() ], RBoolType()),
                            TypeRule([ RNumType(), RNumType() ], RBoolType()),

                            TypeRule([ RStrType(), RStrType() ], RBoolType()),
};

set[TypeRule] gteqRules = {
                            TypeRule([ RBoolType(), RBoolType() ], RBoolType()),

                            TypeRule([ RIntType(), RIntType() ], RBoolType()),
                            TypeRule([ RIntType(), RRealType() ], RBoolType()),
                            TypeRule([ RIntType(), RNumType() ], RBoolType()),

                            TypeRule([ RRealType(), RIntType() ], RBoolType()),
                            TypeRule([ RRealType(), RRealType() ], RBoolType()),
                            TypeRule([ RRealType(), RNumType() ], RBoolType()),

                            TypeRule([ RNumType(), RIntType() ], RBoolType()),
                            TypeRule([ RNumType(), RRealType() ], RBoolType()),
                            TypeRule([ RNumType(), RNumType() ], RBoolType()),

                            TypeRule([ RStrType(), RStrType() ], RBoolType()),
};

set[TypeRule] eqRules = {
                            TypeRule([ RBoolType(), RBoolType() ], RBoolType()),

                            TypeRule([ RIntType(), RIntType() ], RBoolType()),
                            TypeRule([ RIntType(), RRealType() ], RBoolType()),
                            TypeRule([ RIntType(), RNumType() ], RBoolType()),

                            TypeRule([ RRealType(), RIntType() ], RBoolType()),
                            TypeRule([ RRealType(), RRealType() ], RBoolType()),
                            TypeRule([ RRealType(), RNumType() ], RBoolType()),

                            TypeRule([ RNumType(), RIntType() ], RBoolType()),
                            TypeRule([ RNumType(), RRealType() ], RBoolType()),
                            TypeRule([ RNumType(), RNumType() ], RBoolType()),

                            TypeRule([ RStrType(), RStrType() ], RBoolType()),

			    TypeRule([ RValueType(), V(0) ], RBoolType()),
};

set[TypeRule] neqRules = {
                            TypeRule([ RBoolType(), RBoolType() ], RBoolType()),

                            TypeRule([ RIntType(), RIntType() ], RBoolType()),
                            TypeRule([ RIntType(), RRealType() ], RBoolType()),
                            TypeRule([ RIntType(), RNumType() ], RBoolType()),

                            TypeRule([ RRealType(), RIntType() ], RBoolType()),
                            TypeRule([ RRealType(), RRealType() ], RBoolType()),
                            TypeRule([ RRealType(), RNumType() ], RBoolType()),

                            TypeRule([ RNumType(), RIntType() ], RBoolType()),
                            TypeRule([ RNumType(), RRealType() ], RBoolType()),
                            TypeRule([ RNumType(), RNumType() ], RBoolType()),

                            TypeRule([ RStrType(), RStrType() ], RBoolType()),

			    TypeRule([ RValueType(), V(0) ], RBoolType()),
};

set[TypeRule] inRules = {
                            TypeRule([ V(0), RListType(V(1)) ], C(RBoolType(),{Sub(V(0),V(1))})),
                            TypeRule([ V(0), RSetType(V(1)) ], C(RBoolType(),{Sub(V(0),V(1))})),
                            TypeRule([ V(0), RBagType(V(1)) ], C(RBoolType(),{Sub(V(0),V(1))})),
                            TypeRule([ V(0), RMapType(V(1),V(2)) ], C(RBoolType(),{Sub(V(0),V(1))})),
};

set[TypeRule] notinRules = {
                            TypeRule([ V(0), RListType(V(1)) ], C(RBoolType(),{Sub(V(0),V(1))})),
                            TypeRule([ V(0), RSetType(V(1)) ], C(RBoolType(),{Sub(V(0),V(1))})),
                            TypeRule([ V(0), RBagType(V(1)) ], C(RBoolType(),{Sub(V(0),V(1))})),
                            TypeRule([ V(0), RMapType(V(1),V(2)) ], C(RBoolType(),{Sub(V(0),V(1))})),
};
