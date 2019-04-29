module lang::rascalcore::check::Test1 

int f(int x){
    switch(x){
    
    case 1: return 10;
    case 2: return 20;
    case x: if(x < 100) return x * 100; else fail;
    default:
        return 100000;
    }
}