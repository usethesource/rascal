module experiments::Compiler::Examples::Template1

value main(list[value] args) {
    str s = "<for(i <- [0..10]) {>
            '    <if(i % 2 == 0) {>
            '        i = <i>
            '    <}>
            '<}>";
    return s;    
}