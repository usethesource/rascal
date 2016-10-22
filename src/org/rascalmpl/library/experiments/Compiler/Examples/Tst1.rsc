module experiments::Compiler::Examples::Tst1
       
test bool setComprehension() = [   n-1 | int n <- [14, 24, 52, 76, 89, -68, 19, -9, -76]] == [13,23,51,75,88,-69,18,-10,-77];