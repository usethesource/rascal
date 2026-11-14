module lang::rascal::tests::imports::Imports10

// An ADT qualifier `Adt::name` selects the constructors (and smart constructors)
// of `Adt`; it must not select imported functions of the same name that can never
// return a value of `Adt` (void, or a closed supertype such as value or node).

import lang::rascal::tests::imports::M10;

data QAdt = qcons(int x) | qval(int x) | qnode(int x);

test bool adtQualifierSkipsVoidFunction() {
    QAdt v = QAdt::qcons(5);
    return v.x == 5;
}

test bool adtQualifierSkipsValueFunction() {
    QAdt v = QAdt::qval(5);
    return v.x == 5;
}

test bool adtQualifierSkipsNodeFunction() {
    QAdt v = QAdt::qnode(5);
    return v.x == 5;
}

// Smart constructors of a parameterized ADT must still be selected.

data QBox[&T] = qbox(&T v);

QBox[int] qbox(int x) = qbox(0) when x < 0;

test bool adtQualifierKeepsSmartConstructor() = QBox::qbox(-1).v == 0;

test bool adtQualifierKeepsConstructor() = QBox::qbox(3).v == 3;

test bool parameterizedConstructorStillMatches() = qbox(3) := QBox::qbox(3);
