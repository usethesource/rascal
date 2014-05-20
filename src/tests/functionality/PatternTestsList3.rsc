module tests::functionality::PatternTestsList3

 data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);
 
  // matchList3
  
  		public test bool matchList48() = [a(), b()] := [a(), b()];
  		public test bool matchList49() = ([DATA X1, b()] := [a(), b()]) && (X1 == a());
  
  		public test bool matchList50() = ([DATA X2, DATA Y, c()] !:= [a(), b()]);
  
  		public test bool matchList51() = ([e(int X3), b()] := [e(3), b()]) && (X3 == 3);
  		public test bool matchList52() = ([e(int X4)] := [e(3)]) && (X4 == 3);
  		public test bool matchList53() = ([e(int X5)] !:= [a()]);
  
  		public test bool matchList54() = ([a(), f([a(), b(), DATA X6])] := [a(), f([a(),b(),c()])]) && (X6 == c());
  
  		public test bool matchList55() = ([a(), f([a(), b(), DATA X7]), *DATA Y7] := [a(), f([a(),b(),c()]), b()]) && (X7 == c() && Y7 == [b()]);
  		public test bool matchList56() = ([DATA A1, f([A1, b(), DATA X8])] := [a(), f([a(),b(),c()])]) && (A1 == a());
  		public test bool matchList57() = ([A1, f([A1, b(), DATA X8])] := [a(), f([a(),b(),c()])]) && (A1 == a());
  		
  		public test bool matchList58() = ([f([DATA A1, b(), DATA X8]), A1] := [f([a(),b(),c()]), a()]) && (A1 == a());
  		public test bool matchList59() = ([f([A1, b(), DATA X8]), A1] := [f([a(),b(),c()]), a()]) && (A1 == a());
  
  		public test bool matchList60() = ([DATA A2, f([A2, b(), *DATA SX1]), *SX1] := [a(), f([a(),b(),c()]), c()]) && (A2 == a()) && (SX1 ==[c()]);
  
  		public test bool matchList61() = ([DATA A3, f([A3, b(), *DATA SX2]), *SX2] !:= [d(), f([a(),b(),c()]), a()]);
  		public test bool matchList62() = ([DATA A4, f([A4, b(), *DATA SX3]), *SX3] !:= [c(), f([a(),b(),c()]), d()]);
  
   //	matchListSet
  
  		public test bool matchListSet1() = [a(), b()] := [a(), b()];
  		public test bool matchListSet2() = ([DATA X1, b()] := [a(), b()]) && (X1 == a());
  
  		public test bool matchListSet3() = ([DATA X2, DATA Y2, c()] !:= [a(), b()]);
  
  		public test bool matchListSet4() = ([e(int X3), b()] := [e(3), b()]) && (X3 == 3);
  		public test bool matchListSet5() = ([e(int X4)] := [e(3)]) && (X4 == 3);
  		public test bool matchListSet6() = ([e(int X5)] !:= [a()]);
  
  		public test bool matchListSet7() = ([a(), f({a(), b(), DATA X6})] := [a(), f({a(),b(),c()})]) && (X6 == c());
  		public test bool matchListSet8() = ({a(), f([a(), b(), DATA X7])} := {a(), f([a(),b(),c()])}) && (X7 == c());
  
  		public test bool matchListSet9() = ([a(), f({a(), b(), DATA X8}), *DATA Y8] := [a(), f({a(),b(),c()}), b()]) && (X8 == c() && Y8 == [b()]);
  		public test bool matchListSet10() = ({a(), f([a(), b(), DATA X9]), *DATA Y9} := {a(), f([a(),b(),c()]), b()}) && (X9 == c() && Y9 == {b()});
  
  		public test bool matchListSet11() = ([DATA A1, f({A1, b(), DATA X10})] := [a(), f({a(),b(),c()})]) && (A1 == a());
  		public test bool matchListSet12() = ({DATA A2, f([A2, b(), DATA X11])} := {a(), f([a(),b(),c()])}) && (A2 == a());
  
   //	matchSet2
  
  		public test bool matchSet60() = {a(), b()} := {a(), b()};
  		public test bool matchSet61() = ({DATA X1, b()} := {a(), b()}) && (X1 == a());
  
  		public test bool matchSet62() = {DATA X2, DATA Y2, c()} !:= {a(), b()};
  
  		public test bool matchSet63() = ({e(int X3), b()} := {e(3), b()}) && (X3 == 3);
  		public test bool matchSet64() = ({e(int X4)} := {e(3)}) && (X4 == 3);
  		public test bool matchSet65() = ({e(int X5)} !:= {a()});
  		
  		public test bool matchSet66() = ({e(int X3), g(X3)} := {e(3), g(3)}) && (X3 == 3);
  		public test bool matchSet67() = ({e(X3), g(X3), h(X3)} := {e(3), h(3), g(3)}) && (X3 == 3);
  
  		public test bool matchSet68() = ({a(), s({a(), b(), DATA X6})} := {a(), s({a(),b(),c()})}) && (X6 == c());
  		public test bool matchSet69() = ({s({a(), b(), DATA X7}), a()} := {a(), s({a(),b(),c()})}) && (X7 == c());
  
  		public test bool matchSet70() = ({a(), s({a(), b(), DATA X8}), *DATA Y8} := {a(), b(), s({a(),b(),c()})}) && (X8 == c() && Y8 == {b()});
  		public test bool matchSet71() = ({DATA A1, s({A1, b(), DATA X9})} := {a(), s({a(),b(),c()})}) && (A1 == a());
  		public test bool matchSet72() = ({DATA A2, s({A2, b(), DATA X10})} := {s({a(),b(),c()}), a()}) && (A2 == a());
  
  		public test bool matchSet73() = ({DATA A3, s({A3, b(), *DATA SX1}), *SX1} := {a(), s({a(),b(),c()}), c()}) && (A3== a()) && (SX1 =={c()});
  		public test bool matchSet74() = ({DATA A4, s({A4, b(), *DATA SX2}), *SX2} := {s({a(),b(),c()}), a(), c()}) && (A4== a()) && (SX2 =={c()});
  		public test bool matchSet75() = ({DATA A5, s({A5, b(), *DATA SX3}), *SX3} := {c(), s({a(),b(),c()}), a()}) && (A5 == a()) && (SX3 =={c()});
  
  		public test bool matchSet76() = ({DATA A6, s({A6, b(), *DATA SX4}), *SX4} !:= {d(), s({a(),b(),c()}), a()});
  		public test bool matchSet77() = ({DATA A7, s({A7, b(), *DATA SX5}), *SX5} !:= {c(), s({a(),b(),c()}), d()});
  		
  		public test bool matchSet78() = ({DATA A8, s({A8, b()})} := {s({a(),b()}), a()}) && (A8 == a());
  		public test bool matchSet79() = ({s({DATA A9, b()}), A9} := {s({a(),b()}), a()});
  		public test bool matchSet80() = ({s({DATA A9, b()}), A9} := {s({a(),b()}), a()}) && (A9 == a());
  		public test bool matchSet81() = ({s({DATA A10, b(), *DATA SX6}), A10, *SX6} := {c(), s({a(),b(),c()}), a()}) && (A10 == a()) && (SX6 =={c()});