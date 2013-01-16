@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
//START
module demo::common::WordReplacementTest

import demo::common::WordReplacement;

public test bool t1() =  capitalize("1") == "1";
public test bool t2() =  capitalize("rascal") == "Rascal";
public test bool t3() =  capAll1("turn this into a title") == "Turn This Into A Title";
public test bool t4() =  capAll2("turn this into a title") == "Turn This Into A Title";
