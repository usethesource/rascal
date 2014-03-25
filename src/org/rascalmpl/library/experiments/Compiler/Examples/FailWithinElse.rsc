module experiments::Compiler::Examples::FailWithinElse

value main(list[value] args) {
    str trace = "";
    if(true) {
        if(false) {
           ;
        } else {
           trace += "fail inner!";
           fail;
        }
    } else {
        trace += "else outer!";
    }
    return trace;
}