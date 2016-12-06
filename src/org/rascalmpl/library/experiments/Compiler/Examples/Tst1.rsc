module experiments::Compiler::Examples::Tst1

import IO;

value main(){
    loc home = resolveLocation(|cwd://../shapes|);
    println(home);
    return true;
}