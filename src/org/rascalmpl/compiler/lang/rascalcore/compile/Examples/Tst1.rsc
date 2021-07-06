module lang::rascalcore::compile::Examples::Tst1

layout Whitespace = [\ \t\n]*;
syntax A = "a";

syntax P[&T] = "{" &T ppar "}";

syntax PA = P[A] papar;
    
value main() //test bool PA7() 
    //= (P[A]) `{a}`;
    = [P[A]] "{a}";