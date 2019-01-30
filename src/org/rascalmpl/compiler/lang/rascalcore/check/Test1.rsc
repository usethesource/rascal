module lang::rascalcore::check::Test1 
                                  
start syntax A = "a";

A main() {
    start[A] x = [A]"a";
    return x.top;  
}               