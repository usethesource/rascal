module Test3
import languages::pico::syntax::Pico;

str f(str s) { return s; }

public str t(){
    m = f("abc");        // <--- gaat altijd goed
    n = 10;
    m = f("def<n>");     // <--- gaat fout bij import pico syntax
    return m;
}