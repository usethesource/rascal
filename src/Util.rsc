@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}
module vis::examples::tetris::Util

// same as %, except that -1 % 4 becomes 3 instead of -1
public int modPos(int a, int \mod) {
   a = a % \mod;
   if( a < 0) {
      return \mod + a;
   } else {
      return a;
   }
}
