@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module \String-example

String removeFirstNL(String S){
    switch (S) {
      case string("\"" <NQChar* Chars1> "\n" <NQChar* Chars2> "\"") =>
           string("\"" <Chars1> <Chars2> "\"")
    }
}

String removeAllNL(String S){
    return innermost visit (S) {
      case string("\"" <NQChar* Chars1> "\n" <NQChar* Chars2> "\"") =>
           string("\"" <Chars1> <Chars2> "\"")
    };
}
