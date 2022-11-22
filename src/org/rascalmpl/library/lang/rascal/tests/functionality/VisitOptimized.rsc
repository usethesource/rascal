@license{
  Copyright (c) 2009-2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::tests::functionality::VisitOptimized

// visit implementations may group patterns by outermost function
// symbol. In issue #1719 we saw that this optimization introduced
// a bug that would leak pattern variables between cases of the same
// outermost function symbol when the pattern failed halfway through.
// these tests would trigger that bug.

data Bool 
    = \true()
    | \false()
    | \and(Bool lhs, Bool rhs)
    | \or(Bool lhs, Bool rhs)
    | \not(Bool arg)
    | \cond(Bool cond, Bool lhs, Bool rhs)
    | \null()
    ; 

public list[Bool] examples
    = [
        and(and(and(\true(), and(\false(), \true())), \true()), \false()),
        and(\true(), cond(and(\false(), \true()), or(\false(), \true()), \false())),
        or(\false(), \true()),
        and(\true(), cond(and(\true(), \true()), or(\false(), \true()),  \false())),
        and(\true(), cond(and(\false(), \true()), null(), cond(\true(), null(), null())))
    ];

Bool reduce(Bool b) = innermost visit(b) {
    case \and(\true(), X)           => X
    case \and(X:\false(), _)        => X
    case \and(Bool X,\true())       => X
    case \and(_, X:\false())        => X
    case \or(X:\true(),_)           => X
    case \or(_, X:\true())          => X
    case \or(\false(), X)           => X
    case \or(Bool X, \false())      => X 
    case \cond(\true(),X,_)         => X
    case \cond(\false(),_,X)        => X 
    case 1 => 1
};

bool normal(\false()) = true;
bool normal(\true()) = true;
bool normal(null()) = true;
default bool normal(Bool _) = false;

test bool allExamplesNormalAfterVisit() = all(x <- examples, normal(reduce(x)));