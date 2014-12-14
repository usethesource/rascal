module experiments::Compiler::Examples::Tst

import ParseTree;
import IO;
import String;

data D = d1(int n) | d2(str s);

test bool tst0() = "\"<"aaa">\"" == "\"aaa\"";
test bool tst0() = "\"<1234>\"" == "\"1234\"";

str gen() = "a\"c";

test bool tst1() = "<d1(5)>" == "d1(5)";
test bool tst2() = "<d2("abc")>" == "d2(\"abc\")";
test bool tst3() = "<"<d2("abc")>">" == "d2(\"abc\")";
test bool tst2() = "<for(n <- [0..2]){>pqr\"<d2("abc")>\"xyz<}>" == "pqr\"d2(\"abc\")\"xyzpqr\"d2(\"abc\")\"xyz";
test bool tst2() = "<for(n <- [0..2]){>pqr\"<d2("a\"c")>\"xyz<}>" == "pqr\"d2(\"a\\\"c\")\"xyzpqr\"d2(\"a\\\"c\")\"xyz";
test bool tst2() = "<for(n <- [0..2]){>pqr\"<d2("<gen()>")>\"xyz<}>" == "pqr\"d2(\"a\\\"c\")\"xyzpqr\"d2(\"a\\\"c\")\"xyz";


test bool tst4() = "<sort("S")>" == "sort(\"S\")";
test bool tst5() = "<"<sort("S")>">" == "sort(\"S\")";

test bool tst2() = "<for(n <- [0..2]){><sort("S")><}>" == "sort(\"S\")sort(\"S\")";

public str esc(Symbol s){
    res = esc("<s>");
    println("esc(<s>) =\> <res>");
    return res;
}

test bool tstEsc1() = esc(sort("S")) == "sort(\\\"S\\\")";
test bool tstEsc2() = esc(lit(":")) == "lit(\\\":\\\")";
test bool tstEsc3() = esc(lit("\"")) == "lit(\\\"\\\\\\\"\\\")";

test bool tstEsc4() = "<esc(sort("S"))>" == "sort(\\\"S\\\")";
test bool tstEsc5() = "<esc(lit("\""))>" == "lit(\\\"\\\\\\\"\\\")";

test bool tstEsc6() = "<esc(sort("S"))>" == "<"sort(\\\"S\\\")">";
test bool tstEsc7() = "<esc(lit(":"))>" == "<"lit(\\\":\\\")">";
test bool tstEsc8() = "<esc(lit("\""))>" == "<"lit(\\\"\\\\\\\"\\\")">";

test bool tstEsc9() = "<for(s <- [sort("S"), lit("\"")]){><esc(s)><}>" ==  "sort(\\\"S\\\")lit(\\\"\\\\\\\"\\\")";
test bool tstEsc10() = "\"<for(s <- [sort("S"), lit("\"")]){>\"<esc(s)>\"<}>\"" ==  "\"\"sort(\\\"S\\\")\"\"lit(\\\"\\\\\\\"\\\")\"\"";

map[str,str] javaStringEscapes = ( "\n":"\\n", "\"":"\\\"", "\t":"\\t", "\r":"\\r","\\u":"\\\\u","\\":"\\\\");

public str esc(str s){
    return escape(s, javaStringEscapes);
}

value main(list[value] args) {
 	p = sort("S"); //sort(uniqueProductions)[0];
    src =  "
           '  // Production declarations
           '   <for(n <- [0..2]){>
           '   (IConstructor) _read(\"<esc("<p>")>\", Factory.Production);<}>
           '  
           '}";
           return src;
}
