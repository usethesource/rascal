module experiments::Compiler::Examples::Extending

extend ParseTree;
extend experiments::Compiler::Examples::Extended;

value main(list[value] args) {
    return size([0..666]) + f(999);
}

