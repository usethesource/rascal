module lang::rascal::tests::functionality::InterpolationTests

test bool interpolateWhile()  { x = 10; return "<while (x > 0) {> <{x -= 1; x; }> <}>" == " 9  8  7  6  5  4  3  2  1  0 "; }
    
test bool interpolateDoWhile() { x = 10; return "<do {> <{x -= 1; x; }> <} while (x > 0)>" == " 9  8  7  6  5  4  3  2  1  0 "; }

test bool interpolateIfThenElse() = "abc <if (1 > 0) {> GT <} else {> LT <}> cde" == "abc  GT  cde"; 
    
test bool interpolateIfThenTrue() = "abc <if (1 > 0) {> GT <}> cde" == "abc  GT  cde";

test bool interpolateIfThenFalse() = "abc <if (0 > 0) {> GT <}> cde" == "abc  cde";

test bool interpolateFor() = "abc <for (i <- [1,2,3]) {> print <i> <}> cde" == "abc  print 1  print 2  print 3  cde";

test bool interpolateForNested() = 
    "<for (x <- [1,2,3]) {>outer <x> <for (y <- [4,5,6]) {>inner <x>,<y> <}><}>" ==
    "outer 1 inner 1,4 inner 1,5 inner 1,6 outer 2 inner 2,4 inner 2,5 inner 2,6 outer 3 inner 3,4 inner 3,5 inner 3,6 ";

test bool interpolatePreFor() = "<for (i <- [1,2,3]) { j = i + 1;> <j> <}>" == " 2  3  4 ";

test bool interpolatePostWhile() { x = 5; return "<while (x > 0) {> <x> < x -= 1; }>" == " 5  4  3  2  1 ";}

test bool interpolateFor2() {
    str s = "<for(i <- [0..10]) {>
            '    <if(i % 2 == 0) {>
            '        i = <i>
            '    <}>
            '<}>";
    return s ==
    "\n    \n        i = 0\n    \n\n    \n\n    \n        i = 2\n    \n\n    \n\n    \n        i = 4\n    \n\n    \n\n    \n        i = 6\n    \n\n    \n\n    \n        i = 8\n    \n\n    \n";    
}

test bool interpolateQuotes() {
    str s = " \" <0 + 0> \" ";
    return s == " \" 0 \" ";   
}
