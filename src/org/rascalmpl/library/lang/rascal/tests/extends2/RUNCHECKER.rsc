module lang::rascal::tests::extends2::RUNCHECKER

extend lang::rascal::tests::extends2::CHECKER;

str main(){
    return doCollect(1);
}

test bool collect1() 
    = collect(1) == "CHECKER";
    
test bool doCollect1() 
    = doCollect(1) == "CHECKER";
    
test bool indirectDoCollect1() 
    = indirectDoCollect(1, collect) == "CHECKER";