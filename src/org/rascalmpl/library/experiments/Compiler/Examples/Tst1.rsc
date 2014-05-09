module experiments::Compiler::Examples::Tst1

 loc Loc = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);
 loc Loc2 = |file:///home/paulk/pico2.trm|(0,1,<2,3>,<4,5>);	
    //	@Test public void testLocation() {
    		
    		
    		public test bool testLocation1() {Loc ; return true;}
    		public test bool testLocation2() =  Loc == Loc;
    		public test bool testLocation3() = Loc != Loc2;
    		
    		public test bool testLocation4() = Loc.uri == "file:///home/paulk/pico.trm";
    		public test bool testLocation5() = Loc.offset == 0;
    		public test bool testLocation6() = Loc.length == 1;
    		public test bool testLocation7() = Loc.begin.line == 2;
    		public test bool testLocation8() = Loc.begin.column == 3;
    		public test bool testLocation9() = Loc.end.line == 4;
    		public test bool testLocation10() = Loc.end.column == 5;
    		public test bool testLocation11() = Loc.path == "/home/paulk/pico.trm";
    		
    		public test bool assertTrue() { loc l = Loc; l.uri = "file:///home/paulk/pico2.trm"; l.uri == "file:///home/paulk/pico2.trm";}
    		public test bool assertTrue() { loc l = Loc; l.offset = 10; l.offset == 10;}
    		public test bool assertTrue() { loc l = Loc; l.length = 11; l.length == 11;}
    		public test bool assertTrue() { loc l = Loc; l.end.line = 14; l.end.line == 14;}
    		public test bool assertTrue() { loc l = Loc; l.begin.line = 1; l.begin.line == 1;}
    		public test bool assertTrue() { loc l = Loc; l.begin.column = 13; l.begin.column == 13;}
    		public test bool assertTrue() { loc l = Loc; l.end.column = 15; l.end.column == 15;}
    		
    		public test bool assertTrue() {loc l = Loc[uri= "file:///home/paulk/pico.trm"]; l == |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);}
    		public test bool assertTrue() {loc l = Loc[offset = 10]; l == |file:///home/paulk/pico.trm|(10,1,<2,3>,<4,5>);}
    		public test bool assertTrue() {loc l = Loc[length = 11]; l ==  |file:///home/paulk/pico.trm|(0,11,<2,3>,<4,5>);}
    		public test bool assertTrue() {loc l = Loc[begin = <1,4>]; l == |file:///home/paulk/pico.trm|(0,1,<1,4>,<4,5>);}
    		public test bool assertTrue() {loc l = Loc[end = <14,38>]; l ==  |file:///home/paulk/pico.trm|(0,1,<2,3>,<14,38>);}
    