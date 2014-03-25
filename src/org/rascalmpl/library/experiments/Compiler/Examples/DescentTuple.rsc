module experiments::Compiler::Examples::DescentTuple

value main(list[value] args) {
	return [ v | /v:<value _, int _> <- <<<1,2>,<3,4>>,0> ];
}