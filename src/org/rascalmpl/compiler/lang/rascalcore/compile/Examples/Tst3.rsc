module lang::rascalcore::compile::Examples::Tst3

data Maybe[&A] 
   = nothing() 
   | just(&A val)
   ;

void loadClass( Maybe[loc] file=nothing()) {}
