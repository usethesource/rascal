module demo::GenericFeatherweightJava::TypeConstraints
import demo::GenericFeatherweightJava::GFJ;

data TypeOf = typeof(e e) | typeof(m m) | typeof(f f) | typeof(T t);  

data Constraint = eq(TypeOf a, TypeOf b) |
                  subtype(TypeOf a, TypeOf b) |
                  subtype(TypeOf a, set[TypeOf] alts);
                  


