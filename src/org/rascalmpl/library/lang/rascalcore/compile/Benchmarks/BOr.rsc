module lang::rascalcore::compile::Benchmarks::BOr

value main() {
    for(int i <- [0..5000]) {
        if( [*int x,*int y] := [1,2,3,4,5,6,7,8,9,10] || ([*int x,*int y] := [1,2,3,4,5,6,7,8,9,10] || [*int w,*int z] := [1,2,3,4,5,6,7,8,9,10] ) ) {
            fail;
        }
    }
    return true;
}