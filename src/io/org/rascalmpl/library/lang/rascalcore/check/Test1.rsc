module lang::rascalcore::check::Test1
 
import lang::rascalcore::check::Test2;

@doc{

}
public map[&T element, int occurs] distribution(list[&T] lst) {
     map[&T element, int occurs] res = ();
     &T e;
    
        res[e] ? 0 += 1;

     return res; 
}