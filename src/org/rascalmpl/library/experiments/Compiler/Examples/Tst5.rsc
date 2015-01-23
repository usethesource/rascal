module experiments::Compiler::Examples::Tst5

value main(list[value] args) {
    str s = "<for(i <- [0..10]) {>
            '...XXX
            '<}>D";
    return s;    
}