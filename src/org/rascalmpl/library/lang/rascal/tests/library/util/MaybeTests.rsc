module lang::rascal::tests::library::util::MaybeTests

import util::Maybe;

test bool nothingStrEqual(){
    Maybe[str] x = nothing();
    Maybe[str] y = nothing();
    return x == nothing() && y == nothing() && x == y;
}

test bool just2nothing(){
    x = just(3);
    x = nothing();
    return x == nothing();
}

test bool inc1(){
    Maybe[int] x = just(3);
    x.val += 1;
    return x.val == 4;
}

test bool inc2(){
    x = just(3);
    x.val += 1;
    return x.val == 4;
}

test bool inc3(){
    x = just((1 : "a"));
    x.val[1] ? "aaa" += "xxx";
    return x.val == (1:"axxx");
}

test bool inc4(){
    x = just((1 : "a"));
    x.val[2] ? "aaa" += "xxx";
    return x.val == (1:"a",2:"aaaxxx");
}

data X = contain(
            Maybe[bool] p,
            Maybe[int] kw1 = just(2),
            Maybe[str] kw2 = nothing()
        );

test bool contain1(){
    c = contain(nothing());
    return c.p == nothing() && c.kw1.val == 2 && c.kw2 == nothing();
}

test bool contain2(){
    c = contain(nothing(), kw1 = nothing(), kw2 = just("hi"));
    return c.p == nothing() && c.kw1 == nothing() && c.kw2 == just("hi");
}

test bool contain3(){
    c = contain(nothing());
    c.kw1 = nothing();
    c.kw2 = just("hi");
    return c.p == nothing() && c.kw1 == nothing() && c.kw2 == just("hi");
}