module experiments::Compiler::Examples::Tst3

import IO;
value main(){
    x = 1;
    y = 10;
    while(x < 20){
        x += 1;
        if(x > 10){
            y = 100;
            fail;
        }
    }
    return y;
}