module experiments::Compiler::Examples::Tst

data M = message(str txt);

public anno M node@message;

value main(list[value] args) { return "f"()@message("abc"); }