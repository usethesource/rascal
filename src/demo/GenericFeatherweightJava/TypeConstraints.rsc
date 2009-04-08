module TypeConstraints
import GFJ;

data Constraint = eq(T a, T b) |
                  subtype(T a, T b) |
                  subtype(T a, set[T] alts);

T typeOf(e expr); // type of expr
T origTypeOf(e expr); // type of expr in original CT
T typeOf(f field); // type of field decl
T typeOf(m method); // return type of method
L decl(m method); // class that declares method
set[T] rootDecl(M m); // root definitions of method m
L decl(f field); // class that declares field  
T param(M m, int i); // the ith formal parameter of m
T actual(T formal, E e); // actual type of T in E
T actual(T formal, C c); // actual type of T in C

