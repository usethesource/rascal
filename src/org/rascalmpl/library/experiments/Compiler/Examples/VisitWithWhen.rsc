module experiments::Compiler::Examples::VisitWithWhen

list[int] testStuff() = visit([1,2]) {
    case list[int] l => [f]
        when [1,f] := l 
};

value main(list[value] args) {
	return testStuff();
}