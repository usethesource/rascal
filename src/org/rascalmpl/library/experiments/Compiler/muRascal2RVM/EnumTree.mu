module EnumTree

// data BinTree = leaf(str n) | pair(BinTree left, str n, BinTree right);
declares "cons(adt(\"BinTree\",[]),\"leaf\",[label(\"n\",str())])"
declares "cons(adt(\"BinTree\",[]),\"pair\",[label(\"lhs\",adt(\"BinTree\",[])),label(\"n\",str()),label(\"rhs\",adt(\"BinTree\",[]))])"

function main[1,args] {
    return cons pair(cons leaf("-1"), cons leaf("0"), cons leaf("1"));
}