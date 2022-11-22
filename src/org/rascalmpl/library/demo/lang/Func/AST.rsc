module demo::lang::Func::AST

data Prog = prog(list[Func] funcs);
data Func = func(str name, list[str] formals, Exp body);

data Exp 
    = let(list[Binding] bindings, Exp exp)
    | cond(Exp cond, Exp then, Exp otherwise)
    | var(str name)
    | nat(int nat)
    | call(str name, list[Exp] args)

    | address(str var)
    | deref(Exp exp)
         
    | mul(Exp lhs, Exp rhs)
    | div(Exp lhs, Exp rhs)
    | add(Exp lhs, Exp rhs)
    | sub(Exp lhs, Exp rhs)
    | gt(Exp lhs, Exp rhs)
    | lt(Exp lhs, Exp rhs)
    | geq(Exp lhs, Exp rhs)
    | leq(Exp lhs, Exp rhs)
         
    | seq(Exp lhs, Exp rhs)
    | assign(Exp lhs, Exp rhs)
    ;

data Binding = binding(str var, Exp exp);
