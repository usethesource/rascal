module lang::rascal::tests::diamondImports::Top

import lang::rascal::tests::diamondImports::Bottom;

// these imports should be allowed even though Left and Right contain conflicting field names
import lang::rascal::tests::diamondImports::Left;
import lang::rascal::tests::diamondImports::Right;

@expected{UnexpectedType}
test bool fieldNameClashTest(bool leftOrRight) {
    example = leftOrRight 
        ? and(\true(), \true()) 
        : or(maybe(), maybe())
        ;

    assert Bool _ := example.lhs; // ambiguous .lhs does not always produce a Bool
    assert Exp _  := example.lhs; // ambiguous .lhs does not always produce an Exp

    return true;
}

@expected{AssertionFailed}
test bool whichGlobal(bool choice) {
    if (choice) {
        // this use should trigger an error: "ambiguous global variable reference"
        assert global == "Hello";
    }
    else {
        // this use should trigger an error: "ambiguous global variable reference"
        assert global == "World";
    }

    assert lang::rascal::tests::diamondImports::Left::global == "Hello";
    assert lang::rascal::tests::diamondImports::Right::global == "World";

    return true;
}

test bool whichConstructor(bool choice) {
    if (choice) {
        // type-checker should complain that `and` and `true` are either from Left or from Right, or advise to use `extend
        // NOTA BENE: this would be after RAP6; right now the constructors are simply overloaded and dynamically dispatched in arbitrary order.
        assert Exp2 _ := and(\true(), \true());
    }
    else {
        assert Exp _ := and(\true(), \false());
    }

    // here we declare which one to use by module
    assert Exp2 _ := lang::rascal::tests::diamondImports::Left::and(\true(), \true());  // \true() is unique because the context is provided
    assert Exp  _ := lang::rascal::tests::diamondImports::Right::and(\true(), \true()); // \true() is unique because the context is provided

    // it would be better if we could write shorter prefixes like: `Left::and(Left::\true())``
    
    // here we declare which one to use my ADT
    assert Exp2 _ := Exp2::and(\true(), \true());  // \true() is unique because the context is provided
    assert Exp  _ :=  Exp::and(\true(), \true());  // \true() is unique because the context is provided
   
    return true;
}
