module lang::rascalcore::check::Test6

data Tree 
     = amb(set[Tree] alternatives) // <3> 
     ;

&T <:Tree amb(set[&T <:Tree] alternatives) { 
    return amb(alternatives);
  
}