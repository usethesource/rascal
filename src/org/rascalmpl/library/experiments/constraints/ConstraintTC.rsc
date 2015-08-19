module experiments::constraints::ConstraintTC


// Lambda expressions with subtypes

data EXP(int id = -1) = 
         \int (int nval)
       | \real(real rval)
       | var(str x)
       | apply(EXP e1, EXP e2)
       | lambda(EXP x, TYPE t, EXP e)
       | \if(EXP e1, EXP e2, EXP e3)
       | op(Op op, EXP e1, EXP e2)
       ;
data OP = add() | mul() ;

data TYPE =
         base(BASE b)
       | funct(TTYPE t1, TYPE t2)
       ;

data BASE = intType() | realType() ;

bool base_subtype(BASE b, BASE b) = true;
bool base_subtype(intType(), realType()) = true;

default bool base_subtype(BASE b1, BASE b2) = false;

bool subtype(base(BASE b1), base(BASE b2)) = base_subtype(b1, b2);

bool subtype(func(TYPE a1, TYPE b1), func(TYPE a2, TYPE b2)) = subtype(a2, a1) && subtype(b1, b2);

// Constraints

data TYPE = cvar(CVAR cv);

data CVAR = cvar(str name);

data CONS = eq(CVAR v1, TYPE)
          | subtype(CVAR v1, CVAR v2)
          ;
alias CONSTRAINTS = list[CONS];

CONSTRAINTS tc(exp:\int(_)) = [ eq(e.id, base(intType())) ];

CONSTRAINTS tc(exp:\real(_)) = [ eq(e.id, base(realType())) ];

CONSTRAINTS tc(Bindings b, exp: var(x)) = [ eq(exp.id, bindings[x]) ]; 

CONSTRAINTS tc(Bindings b, exp:apply(E e1, E e2)) = [ eq(e1.id, func(t1, t)), eq(e2.id, t2), subtype(t2, t1), eq(exp.id, t) ];
     //when func(t1, t) := tc(B, e1),
     //     t2 := tc(B, e2),
     //     subtype(t2, t1);
          
CONSTRAINTS tc(exp: lambda(EXP x, TYPE t1, EXP e)) = [eq(x.id, t1), eq(exp.id, func(t1, e.id))]
                                                     + tc(binding(x, x.id, b), e);

//func(t1, t2)
//     when t2 := tc(bind(x, t1) + e, e);
     
CONSTRAINTS tc(exp: \if(EXP e1, EXP e2, EXP e3)) = [ eq(e1.id, base(intType())), subtype(e2.id, e.id), subtype(e3.id, exp.id) ] 
                                                   + tc(e1) + tc(e2) + tc(e3);
     //when base(intType()) := tc(env, e1),
     //     t2 := tc(e2),
     //     t3 := tc(e3),
     //     subtype(t2, t),
     //     subtype(t3, t);

CONSTRAINTS tc(B env, op(OP _, E e1, E e2)) = base(b3)
     when base(b1) := tc(env, e1),
          base(b2) := tc(env, e2),
          subtype(base(b1), base(b3)),
          subtype(base(b2), base(b3));
