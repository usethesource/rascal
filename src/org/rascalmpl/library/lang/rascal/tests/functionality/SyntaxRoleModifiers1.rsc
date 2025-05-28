module lang::rascal::tests::functionality::SyntaxRoleModifiers1

import Type;

lexical Z = [a-z]+;

syntax A = "a" | "b" "b";

layout L = [\ ]*;

data A = a();

test bool useOfDifferentModifierTypesWithinASingleScope() {
    data[A] anExample = a();
    syntax[A] anotherExample = [syntax[A]] "a";
    return anExample != anotherExample 
        && anotherExample := (A) `a`;
}

test bool dispatchingOnSyntaxRole() {
    int         f(lexical[&T] _) = 1;
    int         f(syntax[&T] _)  = 2;
    default int f(data[&T] _)    = 4;

    return f(a()) == 4
        && f([lexical[Z]] "z") == 1
        && f([syntax[A]] "a")  == 2;
}

test bool canNestSyntaxModifiers() {
    data[data[A]] v1 = a();
    data[syntax[A]] v2 = a();
    data[lexical[A]] v3 = a();
    data[layout[A]] v4 = a();
    syntax[syntax[A]] v5 = [syntax[A]] "a";
    syntax[data[A]] v6 = [syntax[A]] "a";
    syntax[keyword[A]] v7 = [syntax[A]] "a";
    syntax[layout[A]] v8 = [syntax[A]] "a";
    syntax[lexical[A]] v9 = [syntax[A]] "a";

    // nothing crashed so far and the values remain equal:
    return {a()} == {v1,v2,v3,v4}
        && {[syntax[A]] "a"} == {v5,v6,v7,v8,v9}; 
}

test bool namePreservation1() {
    // this is a simple ad-hoc mock for the real implode function:
    data[&T] implode(syntax[&T] t:appl(prod(sort("A"),_,_),_)) = a();

    // the type signature of `implode` guarantees name preservation,
    // so implode of a syntax A will produce a data A without a type-checking error:
    data[A] x = implode([syntax[A]] "a");

    // this is not the real test. Of course the dynamic type would be data[A].
    // the real test was the above assignment, where the static type system for
    // assignment and function return types would fail if we had a bug here.
    return adt("A",[]) == typeOf(x);
}

test bool namePreservation2() {
    // this is a simple ad-hoc mock for the real explode function:
    syntax[&T] explode(data[&T] t:a()) = [syntax[A]] "a";

    // the type signature of `implode` guarantees name preservation,
    // so explode of a data A will produce a syntax A without a type-checking error:
    syntax[A] x = explode(a());

    // this is not the real test. Of course the dynamic type would be data[A].
    // the real test was the above assignment, where the static type system for
    // assignment and function return types would fail if we had a bug here.
    return sort("A") == typeOf(x);
}
 
test bool dataMatchesSyntaxTreesToo1() {
    return data[Tree] _ := [syntax[A]] "a";
}

@expected{UnexpectedType}
test bool dataMatchesSyntaxTreesToo2() {
    // even though this is a type-preserving function,
    // it will up-cast &T to Tree if called with a non-terminal
    data[&T] castIt(data[&T] i) = i;

    // here &T should be bound to adt("Tree",[]) and not to sort("A"), statically.
    // but I can't write a negative test for the type-checker here I think..
    syntax[A] shouldNotAssignStatically = castIt([syntax[A]] "a");
        
    // we are never supposed to end up here
    return false;
}

data X = x();

test bool dataIsANodeToo() {
    data[&T] washGeneric(data[&T] x) = x;

    // this would fail the type-checker if `data[&T] `is not sub-type of `node`
    return washGeneric(x()) == x();
}