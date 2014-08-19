module experiments::Compiler::Examples::Tst


data Point = point(int i, str color = "red");

//public tuple[Point,Point] f(int i, str color = "green", bool print = false) = <point(i),point(i + 1,color = "blue")>;

value main(list[value] args) {
	return point(3, color="blue");
    //return <f(0,print = true), f(1,color = "grey")>;
}

//public value expectedResult = <<point(0,color = "red"),point(1,color = "blue")>,<point(1,color = "grey"),point(2,color = "blue")>>;