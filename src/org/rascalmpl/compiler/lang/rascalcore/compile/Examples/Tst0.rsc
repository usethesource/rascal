module lang::rascalcore::compile::Examples::Tst0

// import Exception;
// import ParseTree;
// import IO;
 
//layout Whitespace = [\ \t\n]*;
              
start syntax D = "d";
 
// void main() { iprintln(#D); }

value main() = [D] "d";
//test bool parseD1() = (D)`d` := parse(#D, "d");

// data D = d(map[int,set[int]] m);
    
// value main(){
//     x = d(());
//     x.m[0] ? {} += 4;
//     return x;

// }