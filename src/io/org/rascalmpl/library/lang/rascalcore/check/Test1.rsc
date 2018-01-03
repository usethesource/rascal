module lang::rascalcore::check::Test1

value f(){
    try { x = 1;
    } catch value e: throw e;

}