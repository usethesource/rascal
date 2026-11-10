module lang::rascal::tests::concrete::AmbiguityExceptionTest

import ParseTree;
import vis::Text;
import IO;
import util::ParseErrorRecovery;
import Exception;

syntax S = a: A n | b: B n;

syntax A = "x" YZ? "$"?;
syntax B = "x" YZ?;

syntax YZ =  Y "z";

syntax Y = "y";

layout Layout = [\ \n\r\t]* !>> [\ \t\n\r];

@synopsis{Check if a tree is an amb cluster}
private bool isAmbCluster(amb(_)) = true;
private default bool isAmbCluster(Tree tree) = false;

/*
 S = a: 
 └─ A = "x"  YZ?  "$"? 
    ├─ YZ?
    │  └─ YZ = Y  "z" 
    │     └─ Y = "y" 
    └─ "$"?
*/
private S validTree() = parse(#S, "x y z $");

/*
❖
 ├─ S = a: 
 │  └─ A = "x"  YZ?  "$"? 
 │     ├─ YZ?
 │     └─ "$"?
 └─ S = b: 
    └─ B = "x"  YZ? 
       └─ YZ?
*/
private S ambTree() = parse(#S, "x", allowAmbiguity=true);

/*
 S = a: 
 └─ A = "x"  YZ?  "$"? 
    ├─ YZ?
    │  └─ !error dot=0: YZ = Y  "z" 
    │     └─ skipped
    │        ├─ v
    │        ├─ ⎵
    │        └─ z
    └─ "$"?
*/
private S errorTree() = parse(#S, "x v z $", allowRecovery=true);

/*
❖
 ├─ S = a: 
 │  └─ A = "x"  YZ?  "$"? 
 │     ├─ YZ?
 │     │  └─ !error dot=0: YZ = Y  "z" 
 │     │     └─ skipped
 │     │        ├─ v
 │     │        ├─ ⎵
 │     │        └─ z
 │     └─ "$"?
 └─ S = b: 
    └─ B = "x"  YZ? 
       └─ YZ?
          └─ !error dot=0: YZ = Y  "z" 
             └─ skipped
                ├─ v
                ├─ ⎵
                └─ z
*/
private S ambErrorTree() = parse(#S, "x v z", allowAmbiguity=true, allowRecovery=true);

test bool testValidTree() {
    S s = validTree();
    //println(prettyTree(s));
    assert !hasParseErrors(s);
    assert !isAmbCluster(s);
    return true;
}

test bool testAmbiguousTree() {
    S s = ambTree();
    //println(prettyTree(s));
    assert !hasParseErrors(s);
    assert isAmbCluster(s);
    return true;
}

test bool testErrorTree() {
    S s = errorTree();
    //println(prettyTree(s));
    assert hasParseErrors(s);
    assert !isAmbCluster(s);
    return true;
}

test bool testAmbigousErrorTree() {
    S s = ambErrorTree();
    //println(prettyTree(s));
    assert hasParseErrors(s);
    assert isAmbCluster(s);
    return true;
}

test bool testHasOnValid() = validTree() has n;
test bool testHasOnAmb() = !(ambTree() has n);
test bool testHasOnError() = errorTree() has n;
test bool testHasOnAmbError() = !(ambErrorTree() has n);

test bool testDefinedOnValid() = validTree().n?;
test bool testDefinedOnAmb() = !(ambTree().n?);
test bool testDefinedOnError() = errorTree().n?;
test bool testDefinedOnAmbError() = !(ambErrorTree().n?);

test bool testIsOnValid() = validTree() is a;
test bool testIsOnAmb() = !(ambTree() is a);
test bool testIsOnError() = errorTree() is a;
test bool testIsOnAmbError() = !(ambErrorTree() is a);

test bool testNamedFieldOnValid() = "<validTree().n>" == "x y z $";
@ignore
@expected{Ambiguity}
test bool testNamedFieldOnAmb() = "<ambTree().n>" == "x y z $";
test bool testNamedFieldOnError() = "<errorTree().n>" == "x v z $";
@ignore
@expected{ParseErrorRecovery}
test bool testNamedFieldOnAmbError() = "<ambErrorTree().n>" == "x v z $";

test bool testFieldAssignmentOnValid() {
    S s = validTree();
    s.n = s.n;
    return true;
}

@ignore
@expected{Ambiguity}
test bool testFieldAssignmentOnAmb() {
    S s = ambTree();
    s.n = validTree().n;
    return true;
}

test bool testFieldAssignmentOnError() {
    S s = errorTree();
    assert hasParseErrors(s);
    // Replace the subtree that contains errors with a valid subtree
    s.n = validTree().n;
    assert !hasParseErrors(s);
    return true;
}

@ignore
@expected{ParseErrorRecovery}
test bool testFieldAssignmentOnAmbError() {
    S s = ambErrorTree();
    s.n = validTree().n;
    return true;
}

test bool testAltOnValid() = "<(validTree().n ? (A)`x$`)>" == "x y z $";
test bool testAltOnAmb() = "<(ambTree().n ? (A)`x$`)>" == "x$";
test bool testAltOnError() = "<(errorTree().n ? (A)`x$`)>" == "x v z $";
test bool testAltOnAmbError() = "<(ambErrorTree().n ? (A)`x$`)>" == "x$";

test bool testBrackedFieldAssignmentOnValid() = "<validTree()[n=(A)`x$`]>"  == "x$";

@ignore
@expected{Ambiguity}
test bool testBrackedFieldAssignmentOnAmb() = "<ambTree()[n=(A)`x$`]>"  == "x$";

test bool testBrackedFieldAssignmentOnError() {
    S s = errorTree();
    assert hasParseErrors(s);
    S t = s[n=(A)`x$`];
    assert !hasParseErrors(t);
    return "<t>" == "x$";
}

@ignore
@expected{ParseErrorRecovery}
test bool testBrackedFieldAssignmentOnAmbError() {
    try {
        "<ambErrorTree()[n=(A)`x$`]>"  == "x$";
        return false;
    } catch ParseErrorRecovery(NoSuchField("n"), _): {
        return true;
    }
}

/**
 * These "illegal bracket field assignment" tests already work correctly but we need to make sure
 * they will not break once we implement the "bracket field assignment" semantics from the previous tests.
 */
@ignoreCompiler{"UndeclaredField is a static error"}
@expected{UndeclaredField}
test bool testIllegalBrackedFieldAssignmentOnValid() {
    q = validTree().q;
    return true;
}

@ignoreCompiler{"UndeclaredField is a static error"}
@expected{UndeclaredField}
test bool testIllegalBrackedFieldAssignmentOnAmb() {
    q = ambTree().q;
    return true;
}

@ignoreCompiler{"UndeclaredField is a static error"}
@expected{UndeclaredField}
test bool testIllegalBrackedFieldAssignmentOnError() {
    q = errorTree().q;
    return true;
}

@ignoreCompiler{"UndeclaredField is a static error"}
@expected{UndeclaredField}
test bool testIllegalBrackedFieldAssignmentOnAmbError() {
    q = ambErrorTree().q;
    return true;
}

test bool testConcreteMatchOnValid() = (S)`<A _>` := validTree();
test bool testConcreteMatchOnAmb() = (S)`<A _>` !:= ambTree();
test bool testConcreteMatchOnError() = (S)`<A _>` := errorTree();
test bool testConcreteMatchOnAmbError() = (S)`<A _>` !:= ambErrorTree();
