module lang::rascalcore::check::Test1 

//bool isEmpty(list[&T] xxx) = false;

test bool dtstIntersection(list[&T] lst) {

    bool check = true;
    for([*l1, *l2] := lst) {
        lhs1 = lst & l1;
    }
    return check;
}