module tests::library::ValueIOTests
/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
*******************************************************************************/
import ValueIO;
import IO;

data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);

data Maybe[&T] = none() | some(&T t);

alias X[&T] = list[&T];

alias Y = int;

private bool  binaryWriteRead(type[&T] typ, value exp) {
   writeBinaryValueFile(|file:///tmp/xxx|,exp);
   if (&T N := readBinaryValueFile(|file:///tmp/xxx|) && N == exp) return true;
   return false;
   }
   
 public test bool binBool()= binaryWriteRead(#bool, true);
 
 public test bool binInt()= binaryWriteRead(#int, 1);
 
 public test bool binReal()= binaryWriteRead(#real, 2.5);
 
 public test bool binStr1()= binaryWriteRead(#str, "\"abc\"");
 
 public test bool binStr2()= binaryWriteRead(#str, "\"ab\\nc\"");
 
 public test bool binLoc()= binaryWriteRead(#loc, |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>));
 
 public test bool binList()= binaryWriteRead(#list[int], [1,2,3]);
 
 public test bool binSet()= binaryWriteRead(#set[int], {1,2,3});
 
 public test bool binMap()= binaryWriteRead(#map[int, int], (1:10, 2:20));
 
 public test bool binTuple()= binaryWriteRead(#tuple[int, bool, str], <1,true,"abc">);
 
 public test bool binAdt()= binaryWriteRead(#Bool, band(bor(btrue(),bfalse()),band(btrue(),btrue())));
 
 public test bool binParametrizedAdt1()= binaryWriteRead(#Maybe[value], none());
 
 public test bool binParametrizedAdt2()= binaryWriteRead(#Maybe[int], some(1));
 
 public test bool binParamAliasListInt()= binaryWriteRead(#X[int], [1]);
 
 public test bool binParamAliasInt()= binaryWriteRead(#Y, 1);
 
 private bool  textWriteRead(type[&T] typ, value exp) {
   writeTextValueFile(|file:///tmp/xxx|,exp);
   if (&T N := readTextValueFile(|file:///tmp/xxx|) && N == exp) return true;
   return false;
   }
   
 private bool  textWriteRead1(type[&T] typ, value exp) {
   writeTextValueFile(|file:///tmp/xxx|,exp);
   if (&T N := readTextValueFile(typ, |file:///tmp/xxx|) && N == exp) return true;
   return false;
   }
   
 public test bool textBool()= textWriteRead(#bool, true);
 
 public test bool textInt()= textWriteRead(#int, 1);
 
 public test bool textReal()= textWriteRead(#real, 2.5);
 
 public test bool textStr1()= textWriteRead(#str, "\"abc\"");
 
 public test bool textStr2()= textWriteRead(#str, "\"ab\\nc\"");
 
 public test bool textLoc()= textWriteRead(#loc, |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>));
 
 public test bool textList()= textWriteRead(#list[int], [1,2,3]);
 
 public test bool textSet()= textWriteRead(#set[int], {1,2,3});
 
 public test bool textMap()= textWriteRead(#map[int, int], (1:10, 2:20));
 
 public test bool textTuple()= textWriteRead(#tuple[int, bool, str], <1,true,"abc">);
 
 public test bool textAdt()= textWriteRead1(#Bool, band(bor(btrue(),bfalse()),band(btrue(),btrue())));
 