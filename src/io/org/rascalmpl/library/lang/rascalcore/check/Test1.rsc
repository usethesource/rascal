module lang::rascalcore::check::Test1
    
value main(){
    list[int] l;
    
    if([*a] := l) return a + a;
}