module lang::rascal::check::Test2
public map[&T element, int occurs] distribution(list[&T] lst) {
     map[&T element, int occurs] res = ();
     for (e <- lst) {
        res[e] ? 0 += 1;
     }
     return res; 
}