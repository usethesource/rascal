module lang::rascalcore::check::Test3

value assignment5() {
    return {<x, y> = <3, 4>; 
        x = 5; return (x == 5) && (y == 4);};}