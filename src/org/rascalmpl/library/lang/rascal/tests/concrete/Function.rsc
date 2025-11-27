module lang::rascal::tests::concrete::Function

layout L = [\ ]*;
syntax A = "a";
syntax As = {A ","}+;
syntax G = "g" As;

int useExtraFormalInConcreteListPattern(int n, (G) `g <{A ","}+ as>`){
    int g() { x = as; return n; }
    return g();
} 

test bool useExtraFormalInConcreteListPattern1() = useExtraFormalInConcreteListPattern(42, [G] "g a,a,a") == 42;