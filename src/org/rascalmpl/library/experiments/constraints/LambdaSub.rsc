module experiments::constraints::LambdaSub

/* Lambda expressions with subtypes as defined in:

	@article{Tomb:2005cd,
	author = {Tomb, Aaron and Flanagan, Cormac},
	title = {{Automatic type inference via partial evaluation.}},
	journal = {PPDP},
	year = {2005},
	pages = {106--116}
	}
*/

data EXP(int id = -1) = 
         \int (int nval)
       | \real(real rval)
       | var(str name)
       | apply(EXP e1, EXP e2)
       | lambda(EXP x, TYPE t, EXP e)
       | \if(EXP e1, EXP e2, EXP e3)
       | op(OP op, EXP e1, EXP e2)
       ;
data OP = add() | mul() ;

data TYPE =
         base(BASE b)
       | func(TYPE t1, TYPE t2)
       ;

data BASE = intType() | realType() ;

bool base_subtype(BASE b, BASE b) = true;
bool base_subtype(intType(), realType()) = true;

default bool base_subtype(BASE b1, BASE b2) = false;

bool subtype(base(BASE b1), base(BASE b2)) = base_subtype(b1, b2);

bool subtype(func(TYPE a1, TYPE b1), func(TYPE a2, TYPE b2)) = subtype(a2, a1) && subtype(b1, b2);