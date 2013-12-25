module experiments::Compiler::Examples::WhilePlusBacktracking

value main(list[value] args) {
    list[list[int]] res = [];
    l:while([*int x, *int y] := [1,2,3]) {
        res = res + [ x ];
        fail l;
    }
    
    while(true) {
        res = res + [ [999] ];
        fail;
    }
    
    n = 0;
    while([*int x, *int y] := [3,4,3,4], n < 3) {
        if(x == y) {
            res = res + [ x ];
            n = n + 1;
        } else {
            res = res + [ [0] ];
            fail;
        }
    }
    
    n = 0;
    while(n < 3) {
        res = res + [ [10] ];
        n = n + 1;
    }
    
    n = 0;
    while(1 == 1, n < 3) {
        res = res + [ [11] ];
        n = n + 1;
    }
    
    n = 0;
    while(1 == 2 || n < 3) {
        res = res + [ [12] ];
        n = n + 1;
    }
    
    return res;
}